/*
 * ListEntry.java
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

import java.util.List;

/**
 * An item in an IMAP LIST or LSUB response.
 *
 * @author <a href='mailto:dog@gnu.org'>Chris Burdess</a>
 * @version 0.1
 */
public class ListEntry
{

  List attributes;

  char delimiter;

  String mailbox;

  public String toString()
  {
    StringBuffer buffer = new StringBuffer();
    if (attributes!=null)
    {
      buffer.append("\u001b[00;35m");
      buffer.append(attributes);
      buffer.append("\u001b[00m ");
    }
    buffer.append("\"\u001b[00;31m");
    buffer.append(delimiter);
    buffer.append("\u001b[00m\" ");
    buffer.append(mailbox);
    return buffer.toString();
  }
  
}
