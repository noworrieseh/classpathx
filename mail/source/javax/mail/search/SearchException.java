/*
 * SearchException.java
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

import javax.mail.MessagingException;

/**
 * An exception thrown to indicate that a search expression could not be
 * handled by the store implementation.
 *
 * @author <a href="mailto:dog@gnu.org">Chris Burdess</a>
 * @version 1.5
 */
public class SearchException
  extends MessagingException
{

  /**
   * Constructor with no detail message.
   */
  public SearchException()
  {
  }

  /**
   * Constructor with the specified detail message.
   * @param s the detail message
   */
  public SearchException(String s)
  {
    super(s);
  }

}

