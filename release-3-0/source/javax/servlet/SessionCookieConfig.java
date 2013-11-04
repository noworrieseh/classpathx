/*
 * Copyright (C) 2013 Free Software Foundation, Inc.
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
package javax.servlet;

/**
 * Configuration for session tracking cookie properties.
 * @version 3.0
 * @since 3.0
 * @author Chris Burdess
 */
public interface SessionCookieConfig
{

    /**
     * Set the cookie name assigned to session tracking for this context.
     * @see #getName
     */
    void setName(String name);

    /**
     * Returns the cookie name assigned to session tracking for this
     * context. The default is <code>JSESSIONID</code>.
     * @see #setName
     */
    String getName();

    /**
     * Sets the cookie domain assigned to session tracking for this context.
     * @see #getDomain
     */
    void setDomain(String s);

    /**
     * Return the cookie domain assigned to session tracking for this context.
     * The default is <code>null</code>.
     * @see #setDomain
     */
    String getDomain();

    /**
     * Sets the cookie path assigned to session tracking for this context.
     * @see #getPath
     */
    void setPath(String s);

    /**
     * Returns the cookie path assigned to session tracking for this context.
     * The default is the context path of the context.
     * @see #getPath
     */
    String getPath();

    /**
     * Sets the comment assigned to session tracking cookies for this context.
     * @see #getComment
     */
    void setComment(String s);

    /**
     * Returns the comment assigned to session tracking cookies for this
     * context.
     * The default is <code>null</code>.
     * @see #setComment
     */
    String getComment();

    /**
     * Marks session tracking cookies for this context as HTTP only.
     * @see #isHttpOnly
     */
    void setHttpOnly(boolean httpOnly);

    /**
     * Indicates whether session tracking cookies for this context are HTTP
     * only.
     * @see #setHttpOnly
     */
    boolean isHttpOnly();

    /**
     * Marks session tracking cookies for this context as secure.
     * @see #isSecure
     */
    void setSecure(boolean secure);

    /**
     * Indicates whether session tracking cookies for this context are
     * secure.
     * @see #setSecure
     */
    boolean isSecure();

    /**
     * Sets the lifetime in seconds for session tracking cookies for this
     * context.
     * @see #getMaxAge
     */
    void setMaxAge(int maxAge);

    /**
     * Returns the lifetime in seconds for session tracking cookies for this
     * context.
     * @see #setMaxAge
     */
    int getMaxAge();

}
