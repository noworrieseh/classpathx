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
package javax.servlet;

import java.io.IOException;

/**
 * This interface implements methods to forward a request or include
 * output from another (active) source such as another servlet.
 * <p>
 * A servlet can get an object that implements this interface from
 * the <code>ServletContext</code> by calling the
 * <code>getRequestDispatcher()</code> method.
 * <p>
 * If the servlet engine can it should provide a (wrapper) object which
 * implements this interface when a servlet calls
 * <code>getRequestDispatcher()</code>.
 *
 * @see javax.servlet.ServletContext#getRequestDispatcher(java.lang.String)
 * 
 * @version 3.0
 * @since 2.1
 * @author Paul Siegmann (pauls@euronet.nl)
 * @author Chris Burdess
 */
public interface RequestDispatcher 
{

    /**
     * Name of the attribute for the exception object.
     * @since 3.0
     */
    public static final String ERROR_EXCEPTION = "javax.servlet.error.exception";

    /**
     * Name of the attribute for the type of the exception object.
     * @since 3.0
     */
    public static final String ERROR_EXCEPTION_TYPE = "javax.servlet.error.exception_type";

    /**
     * Name of the attribute for the exception message.
     * @since 3.0
     */
    public static final String ERROR_MESSAGE = "javax.servlet.error.message";

    /**
     * Name of the attribute for the request-URI that caused the exception.
     * @since 3.0
     */
    public static final String ERROR_REQUEST_URI = "javax.servlet.error.request_uri";

    /**
     * Name of the attribute for the servlet name that caused the exception.
     * @since 3.0
     */
    public static final String ERROR_SERVLET_NAME = "javax.servlet.error.servlet_name";

    /**
     * Name of the attribute for the response status during an error.
     * @since 3.0
     */
    public static final String ERROR_STATUS_CODE = "javax.servlet.error.status_code";

    /**
     * Name of the attribute for the request-URI during a forward.
     * @since 3.0
     */
    public static final String FORWARD_REQUEST_URI = "javax.servlet.forward.request_uri";

    /**
     * Name of the attribute for the context path during a forward.
     * @since 3.0
     */
    public static final String FORWARD_CONTEXT_PATH = "javax.servlet.forward.context_path";

    /**
     * Name of the attribute for the path info during a forward.
     * @since 3.0
     */
    public static final String FORWARD_PATH_INFO = "javax.servlet.forward.path_info";

    /**
     * Name of the attribute for the servlet path during a forward.
     * @since 3.0
     */
    public static final String FORWARD_SERVLET_PATH = "javax.servlet.forward.servlet_path";

    /**
     * Name of the attribute for the query-string during a forward.
     * @since 3.0
     */
    public static final String FORWARD_QUERY_STRING = "javax.servlet.forward.query_string";

    /**
     * Name of the attribute for the request-URI during an include.
     * @since 3.0
     */
    public static final String INCLUDE_REQUEST_URI = "javax.servlet.include.request_uri";

    /**
     * Name of the attribute for the context path during an include.
     * @since 3.0
     */
    public static final String INCLUDE_CONTEXT_PATH = "javax.servlet.include.context_path";

    /**
     * Name of the attribute for the path info during an include.
     * @since 3.0
     */
    public static final String INCLUDE_PATH_INFO = "javax.servlet.include.path_info";

    /**
     * Name of the attribute for the servlet path during an include.
     * @since 3.0
     */
    public static final String INCLUDE_SERVLET_PATH = "javax.servlet.include.servlet_path";

    /**
     * Name of the attribute for the query-string during an include.
     * @since 3.0
     */
    public static final String INCLUDE_QUERY_STRING = "javax.servlet.include.query_string";

    /**
     * Forwards a <code>ServletRequest</code> to the resource represented by the
     * <code>RequestDispatcher</code>.
     * <p>
     * A servlet can call this method if it has not yet requested an
     * <code>OutputStream</code> or a <code>Writer</code> from the
     * <code>response</code>.
     * <p>
     * Note that the <code>RequestDispatcher</code> can change the
     * <code>request</code> object before handing it to the target resource
     * depending on the string that was given to
     * <code>getRequestDispatcher()</code>.
     *
     * @since 2.1
     *
     * @param request the original request
     * @param response the response to which output should be written
     * @exception ServletException can be thrown by the target resource
     * @exception IOException if an I/O-error occurs
     * @exception IllegalStateException if <code>getOutputStream</code> or <code>getWriter</code> has already been called on the <code>response</code>
     */
    void forward(ServletRequest request, ServletResponse response)
        throws ServletException, IOException;

    /**
     * Includes into the <code>ServletResponse</code> any output written by the
     * resource represented by the <code>RequestDispatcher</code>.
     * <p>
     * Note that the target resource can only use the <code>OutputStream</code>
     * or <code>Writer</code> that the original caller uses. It can not set any
     * headers. Also note that any sessions should be started before calling
     * include.
     * <p>
     * The RequestDispatcher will not alter the original <code>request</code>
     * before handing it to the target resource.
     *
     * @since 2.1
     *
     * @param request the original request
     * @param response the original response
     * @exception ServletException can be thrown by the target resource
     * @exception IOException if an I/O-error occurs
     */
    void include(ServletRequest request, ServletResponse response)
        throws ServletException, IOException;

}
