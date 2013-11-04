/*
 * IntegerComparisonTerm.java
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

/**
 * This class implements comparisons for integers.
 */
public abstract class IntegerComparisonTerm
  extends ComparisonTerm
{

  /**
   * The number.
   */
  protected int number;

  protected IntegerComparisonTerm(int comparison, int number)
  {
    this.comparison = comparison;
    this.number = number;
  }

  /**
   * Return the number to compare with.
   */
  public int getNumber()
  {
    return number;
  }

  /** 
   * Return the type of comparison.
   */
  public int getComparison()
  {
    return super.comparison;
  }

  protected boolean match(int i)
  {
    switch (comparison)
    {
      case LE:
        return i<=number;
      case LT:
        return i<number;
      case EQ:
        return i==number;
      case NE:
        return i!=number;
      case GT:
        return i>number;
      case GE:
        return i>=number;
    }
    return false;
  }

  /**
   * Equality comparison.
   */
  public boolean equals(Object other)
  {
    return (other instanceof IntegerComparisonTerm &&
        ((IntegerComparisonTerm)other).number==number &&
        super.equals(other));
  }

  /**
   * Compute a hashCode for this object.
   */
  public int hashCode()
  {
    return number + super.hashCode();
  }
}
