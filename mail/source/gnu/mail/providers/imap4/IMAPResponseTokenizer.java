/*
 * IMAPResponseTokenizer.java
 * Copyright (C) 2003 Chris Burdess <dog@gnu.org>
 * 
 * This file is part of GNU JavaMail, a library.
 * 
 * GNU JavaMail is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 * GNU JavaMail is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 * 
 * As a special exception, if you link this library with other files to
 * produce an executable, this library does not by itself cause the
 * resulting executable to be covered by the GNU General Public License.
 * This exception does not however invalidate any other reasons why the
 * executable file might be covered by the GNU General Public License.
 */

package gnu.mail.providers.imap4;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.IOException;
import java.net.ProtocolException;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * An object that can parse an underlying socket stream containing IMAP
 * protocol server responses into IMAPResponse tokens.
 *
 * @author <a href='mailto:dog@gnu.org'>Chris Burdess</a>
 * @version 0.1
 */
public class IMAPResponseTokenizer implements IMAPConstants
{

  /**
   * The server stream.
   */
  protected InputStream in;

  private byte[] buffer;

  private static final int BUFFER_SIZE = 4096; // TODO review
  private static final String DEFAULT_ENCODING = "US-ASCII";
  
  private static final int STATE_TAG = 0;
  private static final int STATE_COUNT = 1;
  private static final int STATE_ID = 2;
  private static final int STATE_MAYBE_CODE = 3;
  private static final int STATE_CODE = 4;
  private static final int STATE_CONTENT_LENGTH = 5;
  private static final int STATE_CONTENT = 6;
  private static final int STATE_TEXT = 7;
  private static final int STATE_STATUS = 8;

  /**
   * Constructor.
   * @param in the server socket input stream
   */
  public IMAPResponseTokenizer(InputStream in) {
    this.in = in;
  }

  /*
   * Reads bytes, from the underlying stream if necessary, or from the
   * cache. If moreNeeded is specified, always performs a read to append
   * more to the cache.
   */
  byte[] read(boolean moreNeeded)
    throws IOException
  {
    if (buffer!=null && !moreNeeded && buffer.length>0)
      return buffer;
    int max = in.available();
    if (max<1)
      max = BUFFER_SIZE;
    byte[] tmp = new byte[max];
    int len = 0;
    while (len==0)
      len = in.read(tmp, 0, max);
    if (len==-1)
      return null; // EOF
    int blen = (buffer==null) ? 0 : buffer.length;
    byte[] uni = new byte[blen+len];
    if (blen!=0)
      System.arraycopy(buffer, 0, uni, 0, blen);
    System.arraycopy(tmp, 0, uni, blen, len);
    buffer = uni;
    return buffer;
  }

  /*
   * Invalidates the byte cache up to the specified index.
   */
  void mark(int index) {
    int len = buffer.length;
    int start = index + 1;
    if (start<len)
    {
      int n = (len-start);
      byte[] tmp = new byte[n];
      System.arraycopy(buffer, start, tmp, 0, n);
      buffer = tmp;
    }
    else
      buffer = null;
  }

  /**
   * Returns the next IMAPResponse.
   */
  public IMAPResponse next()
    throws IOException
  {
    // Perform read
    byte[] buf = read(false);
    if (buf==null)
      return null; // pass EOF back up the chain
    int len = buf.length;
    
    IMAPResponse response = new IMAPResponse();
    ByteArrayOutputStream genericSink = new ByteArrayOutputStream();
    ByteArrayOutputStream contentSink = null;
    int contentCount = 0, contentLength = -1;
    Stack context = new Stack();
    int state = STATE_TAG;
    boolean inQuote = false;
    boolean inContent = false;
    for (int i=0; i<len; i++)
    {
      byte b = buf[i];
      switch (state)
      {
        case STATE_TAG: // expect tag
          if (i==0 && b==0x2a) // untagged
            response.tag = IMAPResponse.UNTAGGED;
          else if (i==0 && b==0x2b) // continuation
            response.tag = IMAPResponse.CONTINUATION;
          else if (b==0x20) // delimiter
          {
            if (response.tag==null)
            {
              byte[] tb = genericSink.toByteArray();
              response.tag = new String(tb, DEFAULT_ENCODING);
            }
            genericSink.reset();
            if (response.isContinuation())
              state = STATE_TEXT;
            else
              state = STATE_COUNT;
          }
          else // tag content
            genericSink.write(b);
          break;
        case STATE_COUNT: // expect count or id
          if (b<0x30 || b>0x39)
            state = STATE_ID;
          if (b==0x20) // delimiter
          {
            byte[] cb = genericSink.toByteArray();
            genericSink.reset();
            String cs = new String(cb, DEFAULT_ENCODING);
            try
            {
              response.count = Integer.parseInt(cs);
            }
            catch (NumberFormatException e)
            {
              throw new ProtocolException("Expecting number: "+cs);
            }
            state = STATE_ID;
          }
          else
            genericSink.write(b);
          break;
        case STATE_ID: // expect id
          if (b==0x20) // delimiter
          {
            byte[] ib = genericSink.toByteArray();
            genericSink.reset();
            response.id = new String(ib, DEFAULT_ENCODING).intern();
            state = STATE_MAYBE_CODE;
          }
          else if (b==0x0a) // EOL
          {
            byte[] ib = genericSink.toByteArray();
            genericSink.reset();
            response.id = new String(ib, DEFAULT_ENCODING).intern();
            state = STATE_TAG;
            // mark bytes read
            mark(i);
            return response;
          }
          else if (b!=0x0d) // id content
            genericSink.write(b);
          break;
        case STATE_MAYBE_CODE: // expect code or text
          if (b==0x28 || b==0x5b)
          {
            List top = new ArrayList();
            //response.code = top;
            context.push(top);
            state = STATE_CODE;
          }
          else
          {
            if (response.id==FETCH)
            {
              // We can't have text here so it must be the beginning of
              // FETCH FLAGS. Go back to ID state.
              genericSink.reset();
              byte[] fetchBytes =
                new byte[]{0x46, 0x45, 0x54, 0x43, 0x48, 0x20};
              genericSink.write(fetchBytes);
              genericSink.write(b);
              state = STATE_ID;
            }
            else if (response.id==STATUS)
            {
              // We are in the mailbox name part of the STATUS response
              genericSink.write(b);
              state = STATE_STATUS;
            }
            else
            {
              genericSink.write(b);
              state = STATE_TEXT;
            }
          }
          break;
        case STATE_STATUS:
          if (b==0x20) // delimiter
          {
            response.mailbox = genericSink.toString();
            genericSink.reset();
            state = STATE_MAYBE_CODE;
          }
          else
            genericSink.write(b);
          break;
        case STATE_CODE: // response code inside parentheses
          if (b==0x22) // quote delimiter
          {
            inQuote = !inQuote;
            //genericSink.write(b);
          }
          else if (inQuote)
          {
            genericSink.write(b);
          }
          else
          {
            if (b==0x28 || b==0x5b) // start parenthesis/bracket
            {
              List top = new ArrayList();
              List parent = (List)context.peek();
              parent.add(top);
              context.push(top);
            }
            else if (b==0x29 || b==0x5d) // end parenthesis/bracket
            {
              List top = (List)context.pop();
              // flush genericSink
              byte[] tb = genericSink.toByteArray();
              if (tb.length>0)
              {
                genericSink.reset();
                String token = new String(tb, DEFAULT_ENCODING).intern();
                top.add(token);
              }
              if (context.size()==0) // set response code
                response.code = top;
            }
            else if (b==0x7b) // content length
            {
              // TODO process genericSink
              genericSink.reset();
              state = STATE_CONTENT_LENGTH;
            }
            else if (b==0x20) // token delimiter
            {
              if (context.size()==0) // end state
                state = STATE_TEXT;
              else // add token
              {
                List top = (List)context.peek();
                // flush genericSink
                byte[] tb = genericSink.toByteArray();
                if (tb.length>0)
                {
                  genericSink.reset();
                  String token = new String(tb, DEFAULT_ENCODING).intern();
                  top.add(token);
                }
              }
            }
            else if (b==0x0a) // EOL
            {
              state = STATE_TAG;
              // mark bytes read
              mark(i);
              return response;
            }
            else if (b!=0x0d) // ignore CR
              genericSink.write(b);
          }
          break;
        case STATE_CONTENT_LENGTH:
          if (b==0x7d) // end content length
          {
            byte[] cb = genericSink.toByteArray();
            genericSink.reset();
            String cs = new String(cb, DEFAULT_ENCODING);
            try
            {
              contentLength = Integer.parseInt(cs);
            }
            catch (NumberFormatException e)
            {
              throw new ProtocolException("Expecting number: "+cs);
            }
            state = STATE_CONTENT;
            contentSink = new ByteArrayOutputStream();
            contentCount = 0;
          }
          else
            genericSink.write(b);
          break;
        case STATE_CONTENT:
          if (contentCount>contentLength)
          {
            response.content = contentSink.toByteArray();
            contentSink = null;
            inContent = false;
            state = STATE_CODE;
          }
          else
          {
            if (!inContent && b!=0x20 && b!=0x0d && b!=0x0a)
              inContent = true;
            if (inContent)
              contentSink.write(b);
            contentCount++;
          }
          break;
        case STATE_TEXT: // human-readable text
          if (b==0x0a) // delimiter
          {
            byte[] tb = genericSink.toByteArray();
            genericSink.reset();
            response.text = new String(tb, DEFAULT_ENCODING);
            state = STATE_TAG;
            // mark bytes read
            mark(i);
            return response;
          }
          else if (b!=0x0d) // ignore CR
            genericSink.write(b);
          break;
      }
    }
    // get more bytes
    buf = read(true);
    return next();
  }

}
