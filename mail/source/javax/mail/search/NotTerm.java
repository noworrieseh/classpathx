/*
 * NotTerm.java
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
 * This class implements the logical NEGATION operator.
 */
public final class NotTerm
  extends SearchTerm
{

  /**
   * The search term to negate.
   */
  protected SearchTerm term;

  public NotTerm(SearchTerm t)
  {
    term = t;
  }

  /**
   * Return the term to negate.
   */
  public SearchTerm getTerm()
  {
    return term;
  }

  public boolean match(Message msg)
  {
    return !term.match(msg);
  }

  /**
   * Equality comparison.
   */
  public boolean equals(Object other)
  {
    return (other instanceof NotTerm &&
      ((NotTerm)other).term.equals(term));
  }

  /**
   * Compute a hashCode for this object.
   */
  public int hashCode()
  {
    return term.hashCode() << 1;
  }
  
}
