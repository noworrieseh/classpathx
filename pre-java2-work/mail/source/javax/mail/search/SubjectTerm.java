/*
 * SubjectTerm.java
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
 * This class implements comparisons for the Message Subject header.
 * The comparison is case-insensitive.
 */
public final class SubjectTerm
  extends StringTerm
{

  /**
   * Constructor.
   * @param pattern the pattern to search for
   */
  public SubjectTerm(String pattern)
  {
    super(pattern);
  }

  /**
   * The match method.
   * @param msg the pattern match is applied to this Message's subject header
   * @return true if the pattern match succeeds, otherwise false
   */
  public boolean match(Message msg)
  {
    try
    {
      String subject = msg.getSubject();
      if (subject!=null)
        return super.match(subject);
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
    return (other instanceof SubjectTerm && super.equals(other));
  }

}
