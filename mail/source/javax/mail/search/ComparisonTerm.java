/*
 * ComparisonTerm.java
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

/**
 * This class models the comparison operator.
 * This is an abstract class; subclasses implement comparisons 
 * for different datatypes.
 *
 * @author <a href="mailto:dog@gnu.org">Chris Burdess</a>
 */
public abstract class ComparisonTerm
  extends SearchTerm
{

  public static final int LE = 1;
  public static final int LT = 2;
  public static final int EQ = 3;
  public static final int NE = 4;
  public static final int GT = 5;
  public static final int GE = 6;

  /**
   * The comparison.
   */
  protected int comparison;

  /**
   * Equality comparison.
   */
  public boolean equals(Object other)
  {
    return ((other instanceof ComparisonTerm) && 
        ((ComparisonTerm)other).comparison==comparison);
  }

  /**
   * Compute a hashCode for this object.
   */
  public int hashCode()
  {
    return comparison;
  }

}
