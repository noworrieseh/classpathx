/*
  GNU-Classpath Extensions: Servlet API
  Copyright (C) 1998, 1999, 2001   Free Software Foundation, Inc.

  For more information on the classpathx please mail: classpathx-discuss@gnu.org

  This program is free software; you can redistribute it and/or
  modify it under the terms of the GNU Lesser General Public License
  as published by the Free Software Foundation; either version 2
  of the License, or (at your option) any later version.

  This program is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  GNU General Public License for more details.

  You should have received a copy of the GNU Lesser General Public License
  along with this program; if not, write to the Free Software
  Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
*/
package javax.servlet;

import java.io.IOException;
import java.io.Serializable;
import java.util.Enumeration;


/**
 * Abstract base class for all servlets.
 *
 * @version Servlet API 2.4
 * @since Servlet API 1.0
 * @author Paul Siegmann (pauls@euronet.nl)
 */
public abstract class GenericServlet
implements Servlet, ServletConfig, Serializable 
{
  private ServletConfig servletConfig;

  /**
   * Does nothing.
   *
   * @since Servlet API 1.0
   */
  public GenericServlet() 
  {
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
   * <code>super.init(servletConfig)</code> otherwise the other methods in
   * GenericServlet are not garanteed to work.
   *
   * @since Servlet API 1.0
   *
   * @param servletConfig This servlet configuration class
   * @exception ServletException If an error occurs
   */
  public void init(ServletConfig servletConfig)
    throws ServletException 
  {
    this.servletConfig = servletConfig;
    init();
  }

  /**
   * Automatically called by <code>init(ServletConfig servletConfig)</code>.
   * This version does nothing.
   *
   * @since Servlet API 2.1
   *
   * @exception ServletException If an error occurs
   */
  public void init()
    throws ServletException 
  {
  }

  /**
   * Returns the servlets context
   *
   * @since Servlet API 1.0
   *
   * @return The context
   */
  public ServletContext getServletContext() 
  {
    return servletConfig.getServletContext();
  }


  /**
   * Gets a servlet's initialization parameter
   *
   * @since Servlet API 1.0
   *
   * @param name the name of the wanted parameter
   * @return the value of the wanted parameter.
   * null if the named parameter is not present.
   */
  public String getInitParameter(String name) 
  {
    return servletConfig.getInitParameter(name);
  }


  /**
   * Gets all the initialization parameters
   *
   * @since Servlet API 1.0
   *
   * @return an Enumeration of all the parameters
   */
  public Enumeration getInitParameterNames() 
  {
    return servletConfig.getInitParameterNames();
  }


  /**
   * Writes the class name and a message to the log.
   * Calls <code>getServletContext().log()</code>.
   *
   * @since Servlet API 1.0
   *
   * @param message the message to write
   */
  public void log(String message) 
  {
    servletConfig.getServletContext().log(getServletName() + ": " + message);
  }


  /**
   * Writes the class name, a message and a stack trace to the log.
   * Calls <code>getServletContext().log()</code>.
   *
   * @since Servlet API 2.1
   * @param message the message to write
   * @param th the object that was thrown to cause this log
   */
  public void log(String message, Throwable th) 
  {
    servletConfig.getServletContext().log(getServletName() + ": " + message, th);
  }


  /**
   * The servlet programmer can put other additional info (version
   * number, etc) here.
   * <p>
   * The Servlet 2.1 Spec says that this should return an empty string.
   * The Servlet 2.1 API says that this should return null unless overridden.
   * This version returns the servlet's class name which seems more usefull.
   *
   * @since Servlet API 1.0
   *
   * @return The String holding the information
   */
  public String getServletInfo() 
  {
    return this.getClass().getName();
  }


  /**
   * Gets the servlet servletConfig class
   *
   * @since Servlet API 1.0
   *
   * @return The servletConfig class
   */
  public ServletConfig getServletConfig() 
  {
    return servletConfig;
  }


  /**
   * Called by the server every time it wants the servlet to handle
   * a request.
   * 
   * @since Servlet API 1.0
   *
   * @param request all the request information
   * @param response class to write all the response data to
   * @exception ServletException If an error occurs
   * @exception IOException If an error occurs
   */
  public abstract void service(ServletRequest request,
                               ServletResponse response)
    throws ServletException, IOException;


  /**
   * Called by the server when it no longer needs the servlet.
   * The servlet programmer should use this method to free all
   * the resources the servlet is holding.
   * <p>
   * This version does nothing because it has nothing to clean up.
   * <p>
   * Note that the the 2.1 Spec says that this should do nothing,
   * but the 2.1 API Doc says that it logs the destroy action.
   *
   * @since Servlet API 1.0
   */
  public void destroy() 
  {
    // log("destroy");
  }


  /**
   * Gets you the name of this servlet's <em>instance</em>.
   * Calls its servletConfig's getServletName.
   */
  public String getServletName() 
  {
    return getServletConfig().getServletName();
  }
}
