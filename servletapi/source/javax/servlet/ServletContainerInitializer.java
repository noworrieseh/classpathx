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

import java.util.Set;

/**
 * Servlet context startup listener.
 * This allows for the dynamic programmatic registration of servlets,
 * filters, and listeners in response to a web application being initialized
 * in the container.
 * @version 3.0
 * @since 3.0
 * @author Chris Burdess
 */
public interface ServletContainerInitializer
{

    /**
     * Notifies of the startup of the specified servlet context.
     * @param t the set of application classes that extend or have been
     * annotated by {@link javax.servlet.HandlesTypes}, or null if there are
     * no such classes
     * @param context the servlet context being started
     */
    public abstract void onStartup(Set<Class<?>> t, ServletContext context)
        throws ServletException;

}
