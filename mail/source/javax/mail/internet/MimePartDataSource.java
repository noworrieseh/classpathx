/*
 * MimePartDataSource.java
 * Copyright (C) 2002 The Free Software Foundation
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

package javax.mail.internet;

import java.io.*;
import java.net.UnknownServiceException;
import javax.activation.DataSource;
import javax.mail.*;

/**
 * A utility class that implements a DataSource out of a MimePart.
 * This class is primarily meant for service providers.
 *
 * @author <a href="mailto:dog@gnu.org">Chris Burdess</a>
 */
public class MimePartDataSource
  implements DataSource, MessageAware
{

  /*
   * The part.
   */
  private MimePart part;

  /*
   * Manages a MessageContext on behalf of the part.
   * @see #getMessageContext
   */
  private MessageContext context;

  /**
   * Constructor, that constructs a DataSource from a MimePart.
   */
  public MimePartDataSource(MimePart part)
  {
    this.part = part;
  }

  /**
   * Returns an input stream from this MimePart.
   * <p>
   * This method applies the appropriate transfer-decoding, based on the
   * Content-Transfer-Encoding attribute of this MimePart. Thus the returned
   * input stream is a decoded stream of bytes.
   * <p>
   * This implementation obtains the raw content from the Part using the
   * <code>getContentStream()</code> method and decodes it using the 
   * <code>MimeUtility.decode()</code> method.
   * @return decoded input stream
   */
  public InputStream getInputStream()
    throws IOException
  {
    try
    {
      InputStream is;
      if (part instanceof MimeBodyPart)
        is = ((MimeBodyPart)part).getContentStream();
      else if (part instanceof MimeMessage)
        is = ((MimeMessage)part).getContentStream();
      else
        throw new MessagingException("Unknown part type");
      
      String encoding = part.getEncoding();
      return (encoding!=null) ? MimeUtility.decode(is, encoding) : is;
    }
    catch (MessagingException e)
    {
      throw new IOException(e.getMessage());
    }
  }

  /**
   * DataSource method to return an output stream.
   * <p>
   * This implementation throws the UnknownServiceException.
   */
  public OutputStream getOutputStream()
    throws IOException
  {
    throw new UnknownServiceException();
  }

  /**
   * Returns the content-type of this DataSource.
   * <p>
   * This implementation just invokes the getContentType method on the 
   * MimePart.
   */
  public String getContentType()
  {
    try
    {
      return part.getContentType();
    }
    catch (MessagingException e)
    {
      return null;
    }
  }

  /**
   * DataSource method to return a name.
   * <p>
   * This implementation just returns an empty string.
   */
  public String getName()
  {
    // Shouldn't this return the filename parameter of the
    // Content-Disposition of a MimeBodyPart, if available?
    return "";
  }

  /**
   * Return the MessageContext for the current part.
   */
  public MessageContext getMessageContext()
  {
    if (context==null)
      context = new MessageContext(part);
    return context;
  }
  
}
