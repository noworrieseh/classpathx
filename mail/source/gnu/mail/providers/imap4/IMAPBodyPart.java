/*
 * IMAPBodyPart.java
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

import java.io.InputStream;
import java.io.IOException;
import java.util.Iterator;
import javax.activation.DataHandler;
import javax.mail.MessagingException;
import javax.mail.internet.ContentType;
import javax.mail.internet.InternetHeaders;
import javax.mail.internet.MimeBodyPart;

/**
 * A MIME body part of an IMAP multipart message.
 *
 * @author <a href='mailto:dog@gnu.org'>Chris Burdess</a>
 * @version 0.1
 */
public class IMAPBodyPart
  extends MimeBodyPart
  implements IMAPConstants
{

  /**
   * The message this part belongs to.
   */
  protected IMAPMessage message;

  /**
   * The section used to refer to this part.
   */
  protected String section;

  /**
   * The size of this part's content in bytes.
   */
  protected int size;

  /**
   * The number of text lines of this part's content.
   */
  protected int lines;

  /*
   * Multipart content.
   */
  IMAPMultipart multipart;
  
  /**
   * Called by the IMAPMessage.
   */
  protected IMAPBodyPart(IMAPMessage message,
      IMAPMultipart parent,
      String section,
      InternetHeaders headers,
      int size,
      int lines)
    throws MessagingException
  {
    super(headers, null);
    this.parent = parent;
    this.message = message;
    this.section = section;
    this.size = size;
    this.lines = lines;
  }

  /**
   * Fetches the message body.
   */
  void fetchContent()
    throws MessagingException
  {
    String[] commands = new String[1];
    commands[0] = new StringBuffer("BODY.PEEK[")
      .append(section)
      .append(']')
      .toString();
    fetch(commands);
  }

  void fetch(String[] commands)
    throws MessagingException
  {
    try
    {
      IMAPConnection connection =
        ((IMAPStore)message.getFolder().getStore()).connection;
      int msgnum = message.getMessageNumber();
      int[] messages = new int[] { msgnum };
      synchronized (connection)
      {
        MessageStatus[] ms = connection.fetch(messages, commands);
        for (int i=0; i<ms.length; i++)
        {
          if (ms[i].getMessageNumber()==msgnum)
            update(ms[i]);
        }
                                                  }
    }
    catch (IOException e)
    {
      throw new MessagingException(e.getMessage(), e);
    }
  }

  void update(MessageStatus status)
    throws MessagingException
  {
    for (Iterator i = status.keySet().iterator(); i.hasNext(); )
    {
      String key = (String)i.next();
      if (key==BODY)
      {
        content = status.getContent();
      }
      else
        throw new MessagingException("Unknown message status key: "+key);
    }
  }

  // -- Simple accessors --

  public int getSize()
    throws MessagingException
  {
    return size;
  }

  public int getLineCount()
    throws MessagingException
  {
    return lines;
  }

  // -- Content access --

  /**
   * Returns a data handler for this part's content.
   */
  public DataHandler getDataHandler()
    throws MessagingException
  {
    ContentType ct = new ContentType(getContentType());
    if ("multipart".equalsIgnoreCase(ct.getPrimaryType()))
    {
      return new DataHandler(new IMAPMultipartDataSource(multipart));
    }
    else
    {
      if (content==null)
        fetchContent();
      return super.getDataHandler();
    }
  }
  
  /**
   * Returns the raw content stream.
   */
  protected InputStream getContentStream()
    throws MessagingException
  {
    if (content==null)
      fetchContent();
    return super.getContentStream();
  }

}
