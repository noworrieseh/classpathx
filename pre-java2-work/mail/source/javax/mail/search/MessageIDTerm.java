/*
 * MessageIDTerm.java
 * Copyright (C) 2001 dog <dog@dog.net.uk>
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

package javax.mail.search;

import javax.mail.Message;

/**
 * This term models the RFC822 "MessageId" - a message-id for Internet 
 * messages that is supposed to be unique per message.
 * Clients can use this term to search a folder for a message 
 * given its MessageId.
 * <p>
 * The MessageId is represented as a String.
 */
public final class MessageIDTerm
  extends StringTerm
{

  /**
   * Constructor.
   * @param msgid the msgid to search for
   */
  public MessageIDTerm(String msgid)
  {
    super(msgid);
  }

  /**
   * The match method.
   * @param msg the match is applied to this Message's Message-ID header
   * @return true if the match succeeds, otherwise false
   */
  public boolean match(Message msg)
  {
    try
    {
      String[] messageIDs = msg.getHeader("Message-ID");
      if (messageIDs!=null)
      {
        for (int i = 0; i<messageIDs.length; i++)
        {
          if (super.match(messageIDs[i]))
            return true;
        }
      }
    }
    catch (Exception e)
    {
    }
    return false;
  }

  /**
   * Equality comparison.
   */
  public boolean equals(Object other)
  {
    return (other instanceof MessageIDTerm && super.equals(other));
  }
  
}
