/*
 * AddressException.java
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

/**
 * The exception thrown when a wrongly formatted address is encountered.
 *
 * @author <a href="mailto:dog@gnu.org">Chris Burdess</a>
 */
public class AddressException
  extends ParseException
{

  /**
   * The string being parsed.
   */
  protected String ref;

  /**
   * The index in the string where the error occurred, or -1 if not known.
   */
  protected int pos;

  /**
   * Constructs an AddressException with no detail message.
   */
  public AddressException()
  {
    this(null, null, -1);
  }

  /**
   * Constructs an AddressException with the specified detail message.
   * @param s the detail message
   */
  public AddressException(String s)
  {
    this(s, null, -1);
  }

  /**
   * Constructs an AddressException with the specified detail message and
   * reference info.
   * @param s the detail message
   * @param ref the reference info
   */
  public AddressException(String s, String ref)
  {
    this(s, ref, -1);
  }

  /**
   * Constructs an AddressException with the specified detail message and
   * reference info.
   * @param s the detail message
   * @param ref the reference info
   * @param pos the index in the string where the error occurred
   */
  public AddressException(String s, String ref, int pos)
  {
    super(s);
    this.ref = ref;
    this.pos = pos;
  }

  /**
   * Get the string that was being parsed when the error was detected
   * (null if not relevant).
   */
  public String getRef()
  {
    return ref;
  }

  /**
   * Get the position with the reference string where the error was detected 
   * (-1 if not relevant).
   */
  public int getPos()
  {
    return pos;
  }

  public String toString()
  {
    StringBuffer buffer = new StringBuffer();
    buffer.append(super.toString());
    if (ref!=null)
    {
      buffer.append(" in string ");
      buffer.append(ref);
      if (pos>-1)
      {
        buffer.append(" at position ");
        buffer.append(pos);
      }
    }
    return buffer.toString();
  }
  
}
