/*
 * POP3Headers.java
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

package gnu.mail.providers.pop3;

import java.io.InputStream;
import javax.mail.MessagingException;
import javax.mail.internet.InternetHeaders;

/**
 * Utility class to determine if headers have actually been read.
 *
 * @author <a href='mailto:dog@gnu.org'>Chris Burdess</a>
 * @version 1.2
 */
public final class POP3Headers 
  extends InternetHeaders
{

  boolean empty = true;

  POP3Headers(InputStream in)
    throws MessagingException
  {
    super(in);
  }

  public boolean isEmpty()
  {
    return empty;
  }
  
  public void addHeaderLine(String line)
  {
    super.addHeaderLine(line);
    empty = false;
  }

}
