/*
 * AndTerm.java
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

import javax.mail.Message;

/**
 * This class implements the logical AND operator on individual SearchTerms.
 *
 * @author <a href="mailto:dog@gnu.org">Chris Burdess</a>
 * @version 1.3
 */
public final class AndTerm
  extends SearchTerm
{

  /**
   * The array of terms on which the AND operator should be applied.
   */
  protected SearchTerm[] terms;

  /**
   * Constructor that takes two terms.
   * @param t1 first term
   * @param t2 second term
   */
  public AndTerm (SearchTerm t1, SearchTerm t2)
  {
    terms = new SearchTerm[2];
    terms[0] = t1;
    terms[1] = t2;
  }

  /**
   * Constructor that takes an array of SearchTerms.
   * @param t array of terms
   */
  public AndTerm(SearchTerm[] t)
  {
    terms = new SearchTerm[t.length];
    System.arraycopy (t, 0, terms, 0, t.length);
  }

  /**
   * Return the search terms.
   */
  public SearchTerm[] getTerms ()
  {
    return (SearchTerm[]) terms.clone ();
  }

  /**
   * The AND operation.
   * <p>
   * The terms specified in the constructor are applied to the given object 
   * and the AND operator is applied to their results.
   * @param msg The specified SearchTerms are applied to this Message 
   * and the AND operator is applied to their results.
   * @return true if the AND succeds, otherwise false
   */
  public boolean match (Message message)
  {
    for (int i = 0; i < terms.length; i++)
      {
        if (!terms[i].match (message))
          {
            return false;
          }
      }
    return true;
  }

  /**
   * Equality comparison.
   */
  public boolean equals (Object other)
  {
    if (other instanceof AndTerm)
      {
        AndTerm andterm = (AndTerm)other;
        if (andterm.terms.length != terms.length)
          {
            return false;
          }
        for (int i = 0; i < terms.length; i++)
          {
            if (!terms[i].equals (andterm.terms[i]))
              {
                return false;
              }
          }
        return true;
      }
    return false;
  }
  
  /**
   * Compute a hashCode for this object.
   */
  public int hashCode ()
  {
    int acc = 0;
    for (int i = 0; i < terms.length; i++)
      {
        acc += terms[i].hashCode ();
      }
    return acc;
  }
  
}
