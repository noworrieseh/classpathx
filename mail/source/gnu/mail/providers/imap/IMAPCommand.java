/*
 * IMAPCommand.java
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

package gnu.mail.providers.imap;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * An IMAP4rev1 client command.
 *
 * @author <a href='mailto:dog@gnu.org'>Chris Burdess</a>
 * @version 0.1
 */
public class IMAPCommand
{

  /**
   * The tag for this command.
   */
  protected String tag;
  
  /**
   * The command name.
   */
  protected String name;

  /**
   * The command parameters.
   */
  protected List parameters;

  /**
   * Constructor.
   */
  public IMAPCommand(String tag, String name)
  {
    this.tag = tag;
    this.name = name;
  }

  public String getTag()
  {
    return tag;
  }

  /**
   * Add a parameter to this command.
   */
  public void add(String parameter)
  {
    if (parameters==null)
      parameters = new ArrayList();
    parameters.add(parameter);
  }

  public String toString()
  {
    StringBuffer buffer = new StringBuffer();
    buffer.append(tag);
    buffer.append(' ');
    buffer.append(name);
    if (parameters!=null)
    {
      for (Iterator i = parameters.iterator(); i.hasNext(); )
      {
        buffer.append(' ');
        buffer.append((String)i.next());
      }
    }
    buffer.append('\n');
    return buffer.toString();
  }

}
