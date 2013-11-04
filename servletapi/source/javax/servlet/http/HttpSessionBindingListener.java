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

import java.util.EventListener;

/**
 * Objects that implement this interface will be called when they are bound or
 * unbound into a <code>HttpSession</code> with a
 * <code>HttpSessionBindingEvent</code>.
 *
 * @see javax.servlet.http.HttpSession
 * @see javax.servlet.http.HttpSession#setAttribute(java.lang.String, java.lang.Object)
 * @see javax.servlet.http.HttpSession#removeAttribute(java.lang.String)
 * @see javax.servlet.http.HttpSession#invalidate()
 * @see javax.servlet.http.HttpSessionBindingEvent
 *
 * @version 3.0
 * @since 2.0
 */
public interface HttpSessionBindingListener
    extends EventListener
{

    /**
     * Called when the object is bound to a session.
     *
     * @param event The event object containing the name and session
     */
    void valueBound(HttpSessionBindingEvent event);

    /**
     * Called when the object is unbound from a session.
     *
     * @param event The event object containing the name and session
     */
    void valueUnbound(HttpSessionBindingEvent event);

}
