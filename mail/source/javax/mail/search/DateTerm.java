/*
 * DateTerm.java
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

import java.util.Date;

/**
 * This class implements comparisons for Dates.
 *
 * @author <a href="mailto:dog@gnu.org">Chris Burdess</a>
 */
public abstract class DateTerm
  extends ComparisonTerm
{

  /**
   * The date.
   */
  protected Date date;

  /**
   * Constructor.
   * @param comparison the comparison type
   */
  protected DateTerm(int comparison, Date date)
  {
    this.comparison = comparison;
    this.date = date;
  }

  /**
   * Return the Date to compare with.
   */
  public Date getDate()
  {
    return new Date(date.getTime());
  }

  /**
   * Return the type of comparison.
   */
  public int getComparison()
  {
    return comparison;
  }

  /**
   * The date comparison method.
   * @param d the date in the constructor is compared with this date
   * @return true if the dates match, otherwise false
   */
  protected boolean match(Date d)
  {
    switch (comparison)
    {
      case LE:
        return d.before(date) || d.equals(date);
      case LT:
        return d.before(date);
      case EQ:
        return d.equals(date);
      case NE:
        return !d.equals(date);
      case GT:
        return d.after(date);
      case GE:
        return d.after(date) || d.equals(date);
    }
    return false;
  }

  /**
   * Equality comparison.
   */
  public boolean equals(Object other)
  {
    return ((other instanceof DateTerm) &&
        ((DateTerm)other).date.equals(date) &&
        super.equals(other));
  }

  /**
   * Compute a hashCode for this object.
   */
  public int hashCode()
  {
    return date.hashCode() + super.hashCode();
  }

}
