/*
 * SizeTerm.java
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
 * This class implements comparisons for Message sizes.
 *
 * @author <a href="mailto:dog@gnu.org">Chris Burdess</a>
 */
public final class SizeTerm
  extends IntegerComparisonTerm
{

  /**
   * Constructor.
   * @param comparison the Comparison type
   * @param size the size
   */
  public SizeTerm(int comparison, int size)
  {
    super(comparison, size);
  }

  /**
   * The match method.
   * @param msg the size comparator is applied to this Message's size
   * @return true if the size is equal, otherwise false
   */
  public boolean match(Message msg)
  {
    try
    {
      int size = msg.getSize();
      if (size!=-1)
        return super.match(size);
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
    return (other instanceof SizeTerm && super.equals(other));
  }
  
}
