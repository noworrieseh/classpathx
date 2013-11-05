/*
 * ComparisonTerm.java
 * Copyright (C) 2002 The Free Software Foundation
 *
 * This file is part of GNU Classpath Extensions (classpathx).
 * For more information please visit https://www.gnu.org/software/classpathx/
 *
 * classpathx is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * classpathx is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with classpathx.
 * If not, see <http://www.gnu.org/licenses/>.
 */

package javax.mail.search;

/**
 * A comparison for scalar values.
 *
 * @author <a href="mailto:dog@gnu.org">Chris Burdess</a>
 * @version 1.5
 */
public abstract class ComparisonTerm
  extends SearchTerm
{

  /**
   * The less than or equal to operator.
   */
  public static final int LE = 1;

  /**
   * The less than operator.
   */
  public static final int LT = 2;

  /**
   * The equality operator.
   */
  public static final int EQ = 3;

  /**
   * The not equal to operator.
   */
  public static final int NE = 4;

  /**
   * The greater than operator.
   */
  public static final int GT = 5;

  /**
   * The greater than or equal to operator.
   */
  public static final int GE = 6;

  /**
   * The comparison operator.
   */
  protected int comparison;

  public boolean equals(Object other)
  {
    return ((other instanceof ComparisonTerm) &&
       ((ComparisonTerm)other).comparison == comparison);
  }

  public int hashCode()
  {
    return comparison;
  }

}

