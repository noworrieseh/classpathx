/*
 * NotTerm.java
 * Copyright (C) 2002, 2013 The Free Software Foundation
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

import javax.mail.Message;

/**
 * Provides the logical negation of the target term.
 *
 * @author <a href="mailto:dog@gnu.org">Chris Burdess</a>
 * @version 1.5
 */
public final class NotTerm
  extends SearchTerm
{

  /**
   * The search term to negate.
   */
  private SearchTerm term;

  public NotTerm(SearchTerm t)
  {
    term = t;
  }

  /**
   * Returns the term to negate.
   */
  public SearchTerm getTerm()
  {
    return term;
  }

  /**
   * Returns true only if the term specified in this term does not match the
   * given message.
   */
  public boolean match(Message msg)
  {
    return !term.match(msg);
  }

  public boolean equals(Object other)
  {
    return (other instanceof NotTerm &&
           ((NotTerm) other).term.equals(term));
  }

  public int hashCode()
  {
    return term.hashCode() << 1;
  }

}

