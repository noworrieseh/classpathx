/*
 * StringTerm.java
 * Copyright (C) 2002 The Free Software Foundation
 * 
 * This file is part of GNU JavaMail, a library.
 * 
 * GNU JavaMail is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 * GNU JavaMail is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 * As a special exception, if you link this library with other files to
 * produce an executable, this library does not by itself cause the
 * resulting executable to be covered by the GNU General Public License.
 * This exception does not however invalidate any other reasons why the
 * executable file might be covered by the GNU General Public License.
 */

package javax.mail.search;

/**
 * This class implements the match method for Strings.
 * The current implementation provides only for substring matching.
 * We could add comparisons (like strcmp ...).
 *
 * @author <a href="mailto:dog@gnu.org">Chris Burdess</a>
 */
public abstract class StringTerm
  extends SearchTerm
{

  /**
   * The pattern.
   */
  protected String pattern;

  /**
   * Ignore case when comparing?
   */
  protected boolean ignoreCase;

  protected StringTerm(String pattern)
  {
    this(pattern, true);
  }

  protected StringTerm(String pattern, boolean ignoreCase)
  {
    this.pattern = pattern;
    this.ignoreCase = ignoreCase;
  }

  /**
   * Return the string to match with.
   */
  public String getPattern()
  {
    return pattern;
  }

  /**
   * Return true if we should ignore case when matching.
   */
  public boolean getIgnoreCase()
  {
    return ignoreCase;
  }

  // locate pattern in s
  protected boolean match(String s)
  {
    int patlen = pattern.length();
    int len = s.length()-patlen;
    for (int i = 0; i<=len; i++)
    {
      if (s.regionMatches(ignoreCase, i, pattern, 0, patlen))
        return true;
    }
    return false;
  }

  /**
   * Equality comparison.
   */
  public boolean equals(Object other)
  {
    if (other instanceof StringTerm)
    {
      StringTerm st = (StringTerm)other;
      if (ignoreCase)
        return st.pattern.equalsIgnoreCase(pattern) && 
          st.ignoreCase==ignoreCase;
      else
        return st.pattern.equals(pattern) && 
          st.ignoreCase==ignoreCase;
    }
    return false;
  }

  /**
   * Compute a hashCode for this object.
   */
  public int hashCode()
  {
    return (ignoreCase) ? pattern.hashCode() : ~pattern.hashCode();
  }
  
}
