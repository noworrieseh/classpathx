/*
 * HeaderTerm.java
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

package javax.mail.search;

import javax.mail.Message;

/**
 * This class implements comparisons for Message headers.
 * The comparison is case-insensitive.
 *
 * @author <a href="mailto:dog@gnu.org">Chris Burdess</a>
 */
public final class HeaderTerm
  extends StringTerm
{

  /**
   * The name of the header.
   */
  protected String headerName;

  /**
   * Constructor.
   * @param headerName The name of the header
   * @param pattern The pattern to search for
   */
  public HeaderTerm(String headerName, String pattern)
  {
    super(pattern);
    this.headerName = headerName;
  }

  /**
   * Return the name of the header to compare with.
   */
  public String getHeaderName()
  {
    return headerName;
  }

  /**
   * The header match method.
   * @param msg The match is applied to this Message's header
   * @return true if the match succeeds, otherwise false
   */
  public boolean match(Message msg)
  {
    try
    {
      String[] headers = msg.getHeader(headerName);
      if (headers!=null)
      {
        for (int i = 0; i<headers.length; i++)
        {
          if (super.match(headers[i]))
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
    if (other instanceof HeaderTerm)
    {
      HeaderTerm ht = (HeaderTerm)other;
      return ht.headerName.equalsIgnoreCase(headerName) &&
        super.equals(ht);
    }
    return false;
  }

  /**
   * Compute a hashCode for this object.
   */
  public int hashCode()
  {
    return headerName.toLowerCase().hashCode() + super.hashCode();
  }
  
}
