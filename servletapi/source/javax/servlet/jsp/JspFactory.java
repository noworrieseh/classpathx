/*
 * Copyright (C) 1999, 2013 Free Software Foundation, Inc.
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
package javax.servlet.jsp;

import javax.servlet.Servlet;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * Access to various static resources available to the page.
 */
public abstract class JspFactory 
{

    //  Holds the default JspFactory
    //  set with setDefaultFactory()
    //  returned by getDefaultFactory() 
    private static JspFactory defaultFactory;

    /**
     * Sets the default factory to use.
     * @param factory the new default factory
     */
    public static void setDefaultFactory(JspFactory factory) 
    {
        defaultFactory = factory;
    }

    /**
     * Returns the default factory.
     */
    public static JspFactory getDefaultFactory() 
    {
        return defaultFactory;
    }

    /**
     * Get a PageContext object associated with a request/response.
     */
    public abstract PageContext getPageContext(Servlet servlet,
            ServletRequest request,
            ServletResponse response,
            String errorPageURL,
            boolean needsSession,
            int buffer,
            boolean autoflush);

    /**
     * Release a PageContext obtained via a previous call to
     * {@link #getPageContext}.
     */
    public abstract void releasePageContext (PageContext ctx);

    /**
     * Returns implementation-dependent information on the JSP engine.
     */
    public abstract JspEngineInfo getEngineInfo();

}
