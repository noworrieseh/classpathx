/*
 * CRLFInputStream.java
 * Copyright (C) 2002 The Free Software Foundation
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

package gnu.mail.util;

import java.io.FilterInputStream;
import java.io.InputStream;
import java.io.IOException;

/**
 * An input stream that filters out CR/LF pairs into LFs.
 *
 * @author <a href="mailto:dog@gnu.org">Chris Burdess</a>
 */
public class CRLFInputStream
  extends FilterInputStream
{

  /**
   * The CR octet.
   */
  public static final int CR = 13;

  /**
   * The LF octet.
   */
  public static final int LF = 10;

	/**
	 * Buffer.
	 */
	protected int buf = -1;

	/**
	 * Buffer at time of mark.
	 */
	protected int markBuf = -1;
	
  /**
   * Constructs a CR/LF input stream connected to the specified input
   * stream.
   */
  public CRLFInputStream(InputStream in) 
  {
    super(in);
  }

  /**
   * Reads the next byte of data from this input stream.
   * Returns -1 if the end of the stream has been reached.
   * @exception IOException if an I/O error occurs
   */
  public int read()
    throws IOException
  {
		int c;
		if (buf!=-1)
		{
			c = buf;
			buf = -1;
			return c;
		}
		else
		{
			c = super.read();
			if (c==CR)
			{
				buf = super.read();
				if (buf==LF)
				{
					c = buf;
					buf = -1;
				}
			}
		}
    return c;
  }

  /**
   * Reads up to b.length bytes of data from this input stream into
   * an array of bytes.
   * Returns -1 if the end of the stream has been reached.
   * @exception IOException if an I/O error occurs
   */
  public int read(byte[] b)
    throws IOException
  {
    return read(b, 0, b.length);
  }

  /**
   * Reads up to len bytes of data from this input stream into an
   * array of bytes, starting at the specified offset.
   * Returns -1 if the end of the stream has been reached.
   * @exception IOException if an I/O error occurs
   */
  public int read(byte[] b, int off, int len)
    throws IOException
  {
		int shift = 0;
		if (buf!=-1)
		{
			// Push buf onto start of byte array
			b[off] = (byte)buf;
			off++;
			len--;
			buf = -1;
			shift++;
		}
    int l = super.read(b, off, len);
    l = removeCRLF(b, off-shift, l);
    return l;
	}

	/**
	 * Indicates whether this stream supports the mark and reset methods.
	 */
	public boolean markSupported()
	{
		return in.markSupported();
	}

	/**
	 * Marks the current position in this stream.
	 */
	public void mark(int readlimit)
	{
		in.mark(readlimit);
		markBuf = buf;
	}

	/**
	 * Repositions this stream to the position at the time the mark method was
	 * called.
	 */
	public void reset()
		throws IOException
	{
		in.reset();
		buf = markBuf;
	}
	
  /*
   * Reads up to len bytes of data from this input stream into an
   * array of bytes, starting at the specified offset.
   * Returns -1 if the end of the stream has been reached.
   * @exception IOException if an I/O error occurs
   *
  public int read(byte[] b, int off, int len)
    throws IOException
  {
    int l = doRead(b, off, len);
    l = removeCRs(b, off, l);
    return l;
  }

  /*
   * Slight modification of PushbackInputStream.read() not to do a
   * blocking read on the underlying stream if there are bytes in the
   * buffer.
   *
  private int doRead(byte[] b, int off, int len)
    throws IOException
  {
    if (len<=0)
      return 0;
    int avail = buf.length-pos;
    if (avail>0)
    {
      if (len<avail)
      avail = len;
      System.arraycopy(buf, pos, b, off, avail);
      pos += avail;
      off += avail;
      len -= avail;
    }
    else if (len>0)
    {
      len = super.read(b, off, len);
      if (len==-1)
        return avail == 0 ? -1 : avail;
      return avail + len;
    }
    return avail;
  }

  /*
   * Reads a line of input terminated by LF.
   * @exception IOException if an I/O error occurs
   *
  public String readLine()
    throws IOException
  {
    StringBuffer sb = new StringBuffer();
    boolean done = false, eos = false;
    while (!done)
    {
      byte[] b = new byte[buf.length];
      int l = read(b);
      if (l==-1)
      {
        done = true;
        eos = true;
      }
      else
      {
        for (int i=0; i<l; i++)
        {
          if (b[i]==LF)
          {
            if (i>0)
              sb.append(new String(b, 0, i));
            if (l-(i+1)>0)
              unread(b, i+1, (l-i)-1);
            done = true;
            break;
          }
        }
        if (!done && l>0)
          sb.append(new String(b, 0, l));
      }
    }
    if (eos && sb.length()<1)
      return null;
    else
      return sb.toString();
  }*/

  private int removeCRLF(byte[] b, int off, int len)
  {
		int end = off+len;
		for (int i=off; i<end; i++)
		{
			if (b[i]==CR)
			{
				if (i+1==end)
				{
					// This is the last byte, impossible to determine whether CRLF
					buf = CR;
					len--;
				}
				else if (b[i+1]==LF)
				{
					// Shift left
					end--;
					for (int j=i; j<end; j++)
						b[j] = b[j+1];
					len--;
					end = off+len;
				}
			}
		}
    return len;
  }

}
