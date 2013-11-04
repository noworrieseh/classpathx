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
import java.io.Serializable;
import java.util.Enumeration;
import java.util.ResourceBundle;

/**
 * Abstract base class for all servlets.
 *
 * @version 3.0
 * @since 1.0
 * @author Paul Siegmann (pauls@euronet.nl)
 */
public abstract class GenericServlet
    implements Servlet, ServletConfig, Serializable 
{

    private static final ResourceBundle L10N =
        ResourceBundle.getBundle("javax.servlet.L10N");
    private ServletConfig config;

    /**
     * Does nothing.
     */
    public GenericServlet() 
    {
    }

    /**
     * Called by the server when it no longer needs the servlet.
     * The servlet programmer should use this method to free all
     * the resources the servlet is holding.
     * <p>
     * This version does nothing because it has nothing to clean up.
     * <p>
     * Note that the the 2.1 Spec says that this should do nothing,
     * but the 2.1 API Doc says that it logs the destroy action.
     */
    public void destroy() 
    {
    }

    /**
     * Gets a servlet's initialization parameter
     *
     * @param name the name of the wanted parameter
     * @return the value of the wanted parameter.
     * null if the named parameter is not present.
     */
    public String getInitParameter(String name) 
    {
        return config.getInitParameter(name);
    }

    /**
     * Gets all the initialization parameters
     *
     * @return an Enumeration of all the parameters
     */
    public Enumeration getInitParameterNames() 
    {
        return config.getInitParameterNames();
    }

    /**
     * Gets the servlet config class
     *
     * @return The config class
     */
    public ServletConfig getServletConfig() 
    {
        return config;
    }

    /**
     * Returns the servlets context
     *
     * @return The context
     */
    public ServletContext getServletContext() 
    {
        return config.getServletContext();
    }

    /**
     * The servlet programmer can put other additional info (version
     * number, etc) here.
     *
     * @return The String holding the information
     */
    public String getServletInfo() 
    {
        return "";
    }

    /**
     * Initializes the servlet.
     * Called by the server exactly once during the lifetime of the servlet.
     * This method can be used to setup resources (connections to a
     * database for example) for this servlet.
     * <p>
     * This version saves the ServletConfig and calls <code>init()</code>.
     * This means that a servlet can just override <code>init()</code>.
     * Note that if a servlet overrides this method it should call
     * <code>super.init(config)</code> otherwise the other methods in
     * GenericServlet are not garanteed to work.
     *
     * @param config This servlet configuration class
     * @exception ServletException If an error occurs
     */
    public void init(ServletConfig config)
        throws ServletException 
    {
        this.config = config;
        init();
    }

    /**
     * Automatically called by <code>init(ServletConfig config)</code>.
     * This version does nothing.
     *
     * @since 2.1
     *
     * @exception ServletException If an error occurs
     */
    public void init()
        throws ServletException 
    {
    }



    /**
     * Writes the class name and a message to the log.
     * Calls <code>getServletContext().log()</code>.
     *
     * @param message the message to write
     */
    public void log(String message) 
    {
        config.getServletContext().log(getServletName() + ": " + message);
    }


    /**
     * Writes the class name, a message and a stack trace to the log.
     * Calls <code>getServletContext().log()</code>.
     *
     * @since 2.1
     * @param message the message to write
     * @param throwable the object that was thrown to cause this log
     */
    public void log(String message, Throwable throwable) 
    {
        config.getServletContext().log(getServletName() + ": " + message, throwable);
    }

    /**
     * Called by the server every time it wants the servlet to handle
     * a request.
     * 
     * @param request all the request information
     * @param response class to write all the response data to
     * @exception ServletException If an error occurs
     * @exception IOException If an error occurs
     */
    public abstract void service(ServletRequest request, ServletResponse response)
        throws ServletException, IOException;

    /**
     * Gets you the name of this servlet's <em>instance</em>.
     * Calls its config's getServletName.
     */
    public String getServletName() 
    {
        ServletConfig config = getServletConfig(); // NB may be overridden
        if (config != null)
          {
            return config.getServletName();
          }
        else
          {
            throw new IllegalStateException(L10N.getString("err.servlet_config_not_initialized"));     
          }
    }

}
