/*
 * MessageContext.java
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

package javax.mail;

/**
 * The context in which a piece of Message content is contained. 
 * A MessageContext object is returned by the <code>getMessageContext</code>
 * method of the MessageAware interface. MessageAware is typically 
 * implemented by DataSources to allow a DataContentHandler to pass on 
 * information about the context in which a data content object is operating.
 *
 * @author <a href="mailto:dog@gnu.org">Chris Burdess</a>
 * @version 1.3
 */
public class MessageContext
{

  /**
   * The Part to describe.
   */
  private Part part;

  /**
   * Create a MessageContext object describing the context of the given Part.
   */
  public MessageContext(Part part)
  {
    this.part = part;
  }

  /**
   * Return the Part that contains the content. May be null.
   */
  public Part getPart()
  {
    return part;
  }

  /**
   * Return the Message that contains the content.
   * Follows the parent chain up through containing Multipart objects 
   * until it comes to a Message object, or null.
   */
  public Message getMessage()
  {
    Part p = part;
    while (p!=null)
    {
      if (p instanceof Message)
        return (Message)p;
      if (p instanceof BodyPart)
      {
        BodyPart bp = (BodyPart)p;
        Multipart mp = bp.getParent();
        p = mp.getParent();
      }
      else
        p = null;
    }
    return null;
  }

  /**
   * Return the Session we're operating in.
   */
  public Session getSession()
  {
    Message message = getMessage();
    if (message!=null)
      return message.session;
    return null;
  }

}
