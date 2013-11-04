/*
 * Copyright (C) 1998, 1999, 2001, 2013 Free Software Foundation, Inc.
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
package javax.servlet.http;

/**
 * Send to an Object that implements <code>HttpSessionBindingListener</code>
 * when bound into a session or unbound from a session. Gives access to the
 * session and the name used to bind the Object to the session.
 *
 * @see javax.servlet.http.HttpSession
 * @see javax.servlet.http.HttpSession#putAttribute(java.lang.String, java.lang.Object)
 * @see javax.servlet.http.HttpSession#removeAttribute(java.lang.String)
 * @see javax.servlet.http.HttpSession#invalidate()
 * @see javax.servlet.http.HttpSessionBindingListener
 *
 * @version 3.0
 * @since 2.0
 */
public class HttpSessionBindingEvent
    extends HttpSessionEvent
{

    private String name;
    private Object value = null;

    /**
     * Creates a new <code>HttpSessionBindingEvent</code> given the session
     * and the name used.
     *
     * @since 2.0
     *
     * @param session which the Object was bound to or unbound from
     * @param name which was used to refer to the object
     */
    public HttpSessionBindingEvent(HttpSession session, String name) 
    {
        super(session);
        this.name = name;
    }

    /**
     * Creates a new <code>HttpSessionBindingEvent</code> given the session
     * and the name used.
     *
     * @since 2.3
     *
     * @param session which the Object was bound to or unbound from
     * @param name which was used to refer to the object
     * @param value 
     */
    public HttpSessionBindingEvent(HttpSession session,
            String name, Object value) 
    {
        this(session, name);
        this.value = value;
    }

    /**
     * Returns the name used to refer to this Object.
     *
     * @since 2.0
     */
    public String getName() 
    {
        return name;
    }

    /**
     * Returns the value.
     *
     * @since 2.3
     */
    public Object getValue() 
    {
        return value;
    }

}
