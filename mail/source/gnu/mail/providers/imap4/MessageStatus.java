/*
 * MessageStatus.java
 * Copyright (C) 2003 Chris Burdess <dog@gnu.org>
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2 of the License, or (at your option) any later version.
 * 
 * You also have permission to link it with the Sun Microsystems, Inc. 
 * JavaMail(tm) extension and run that combination.
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

package gnu.mail.providers.imap4;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Represents the state of a message.
 *
 * @author <a href='mailto:dog@gnu.org'>Chris Burdess</a>
 * @version 0.1
 */
public final class MessageStatus
{

  int messageNumber;

  byte[] content;

  Map properties;

  MessageStatus(int messageNumber)
  {
    this.messageNumber = messageNumber;
    properties = new HashMap();
  }
  
  public int getMessageNumber()
  {
    return messageNumber;
  }

  public byte[] getContent()
  {
    return content;
  }

  public Object get(String key)
  {
    return properties.get(key);
  }

  public Set keySet()
  {
    return properties.keySet();
  }

  void put(String key, Object value)
  {
    properties.put(key, value);
  }

}
