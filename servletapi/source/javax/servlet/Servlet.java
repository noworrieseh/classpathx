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
 * This is the interface for all servlets.
 * <P>
 * Servlets handle server request.
 * <P>
 * Servlets have 5 phases in their lifespan, as follows:
 * <OL>
 * <LI>Creation<BR>
 * This is an ordinary constructor call by the server.
 * <LI>init<BR>
 * The server who created the servlet calls the <CODE>init</CODE> method
 * somewhere between creation and the first request it ever gives
 * the servlet to handle.
 * <LI>service<BR>
 * For every incoming request the server calls the <CODE>service</CODE> method.
 * The server packages all the request data in a ServletRequest object, and
 * creates a ServletResponse object for the servlet to write reply data to.<BR>
 * Note that the service method is run in a seperate thread.<BR>
 * This is also the great advantage of using servlets versus traditional cgi
 * scripting: instead of forking of a proces for every request only a new
 * thread is created.
 * <LI>destroy<BR>
 * This method is called by the server indicating that the server no longer
 * requires this servlet's services. The serlvet is expected to release any
 * resources it is holding using this method.<BR>
 * (With resources things like database connections etc are meant).
 * <LI>Destruction<BR>
 * This happens whenever the garbage collector happens to feel like
 * reclaiming the memory used by this servlet.
 * </OL>
 *
 * @version 3.0
 * @since 1.0
 * @author Paul Siegmann (pauls@euronet.nl)
 */
public interface Servlet
{

    /**
     * Initializes the servlet.
     * Called by the server exactly once during the lifetime of the servlet.
     * This method can be used to setup resources (connections to a
     * database for example) for this servlet. The servlet should store the
     * <code>ServletConfig</code> so it can return it again when the
     * <code>getConfig()</code> method is called. If the the servlet is
     * temporarily or permanently unavailable it should throw an
     * <code>UnavailableException</code>.
     * @see javax.servlet.UnavailableException
     *
     * @param config This servlet configuration class
     * @exception ServletException If an unexpected error occurs
     * @exception UnavailableException If servlet is temporarily or permanently
     * unavailable
     */
    void init(ServletConfig config)
        throws ServletException;

    /**
     * Gets the servlet config class. This should be the same
     * <code>ServletConfig</code> that was handed to the <code>init()</code>
     * method.
     *
     * @return The config class
     */
    ServletConfig getServletConfig();

    /**
     * Called by the server every time it wants the servlet to handle
     * a request. The servlet engine doesn't have to wait until the service
     * call is finished but can start another thread and call the service method
     * again to handle multiple concurrent requests. If a servlet doesn't want
     * this to happen it has to implement the <code>SingleThreadModel</code>
     * interface.
     * @see javax.servlet.SingleThreadModel
     * 
     * @param request all the request information
     * @param response class to write all the response data to
     * @exception ServletException If an error occurs
     * @exception IOException If an error occurs
     */
    void service(ServletRequest request, ServletResponse response)
        throws IOException, ServletException;

    /** 
     * Gets a string containing information about the servlet.
     * This String is provided by the Servlet writer and may contain
     * things like the Servlet's name, author, version... stuff like that.
     */
    String getServletInfo();

    /**
     * Called by the server when it no longer needs the servlet.
     * The servlet programmer should use this method to free all
     * the resources the servlet is holding.
     */
    void destroy();

}

