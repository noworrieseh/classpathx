/*
 * pageContext.java -- XXX
 *
 * Copyright (c) 1999 by Free Software Foundation, Inc.
 * Written by Mark Wielaard (mark@klomp.org)
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Library General Public License as published
 * by the Free Software Foundation, version 2. (see COPYING.LIB)
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software Foundation
 * Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307 USA
 */

package javax.servlet.jsp;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Hashtable;

import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletRequest;

/**
 * The definition of a page.
 *
 * @author Mark Wielaard
 * @author Nic Ferrier <nferrier@tapsellferrier.co.uk>
 */
public abstract class PageContext 
{

  //  static final strings used in attribute name table
  //  [mjw] We don't know what the real values are XXX!!!

  /**
   * Used as the key for application stuff in the PageContext table.
   */
  public static final String APPLICATION = "javax.servlet.jsp.APPLICATION";

  /**
   * XXX
   */
  public static final String PAGECONTEXT = "javax.servlet.jsp.PAGECONTEXT";

  /**
   * XXX
   */
  public static final String CONFIG = "javax.servlet.jsp.CONFIG";

  /**
   * XXX
   */
  public static final String EXCEPTION = "javax.servlet.jsp.Exception";

  /**
   * XXX
   */
  public static final String OUT = "javax.servlet.jsp.OUT";

  /**
   * XXX
   */
  public static final String PAGE = "javax.servlet.jsp.PAGE";

  /**
   * XXX
   */
  public static final String REQUEST = "javax.servlet.jsp.REQUEST";

  /**
   * XXX
   */
  public static final String RESPONSE = "javax.servlet.jsp.RESPONSE";

  /**
   * XXX
   */
  public static final String SESSION = "javax.servlet.jsp.SESSION";


  /**
   * Denotes application scope.
   */
  public static final int APPLICATION_SCOPE = -1;

  /**
   * Denotes page scope.
   */
  public static final int PAGE_SCOPE = -2;

  /**
   * Denotes request scope.
   */
  public static final int REQUEST_SCOPE = -3;

  /**
   * Denotes session scope.
   */
  public static final int SESSION_SCOPE = -4;

  //  static final ints
  //  [mjw] We also do not know these values XXX!!!

  /**
   * XXX
   */
  public static final int DEFAULT_BUFFER = -1; //  XXX

  /**
   * XXX
   */
  public static final int NO_BUFFER = 0; //  XXX

  //  protected instance variables

  /**
   * XXX [mjw] Why transient?
   */
  protected transient Hashtable attributes;

  /**
   * XXX
   */
  protected ServletContext context;

  /**
   * XXX
   */
  protected JspFactory factory;

  /**
   * XXX
   */
  protected boolean needsSession;

  /**
   * XXX [mjw] Why transient?
   */
  protected transient JspWriter out;

  /**
   * XXX [mjw] Why transient?
   */
  protected transient ServletRequest request;

  /**
   * XXX [mjw] Why transient?
   */
  protected transient ServletResponse response;

  /**
   * XXX
   */
  protected Servlet servlet;

  /**
   * XXX [mjw] Why transient?
   */
  protected transient HttpSession session;

  //  Private Instance Variables

  //  XXX [mjw] Do we really need this?
  private JspWriter initialOut;

  //  Constructor

  /**
   * The JSP API constructor.
   */
  public PageContext ()
  {
  }

  //  Instance Methods

  /**
   * XXX
   *
   * @param name XXX
   * @param scope XXX
   * @exception IllegalArgumentException
   */
  protected void _checkForNameCollision(String name, int scope)
    throws IllegalArgumentException 
  {
    //  XXX [mjw] What should the default behaviour be?
  }

  /**
   * XXX
   *
   * @param name
   * @return XXX
   */
  public Object getAttribute(String name) 
  {
    return attributes.get(name);
  }

  /**
   * XXX
   *
   * @param name XXX
   * @return XXX
   */
  public int getAttributesScope(String name) 
  {
    return 0; //  XXX [mjw] I have no idea
  }

  /**
   * XXX
   *
   * @param scope XXX
   * @return XXX
   */
  public Enumeration getAttributesInScope(int scope) 
  {
    return null; //  XXX [mjw] I have no idea
  }

  /**
   * XXX
   *
   * @param name XXX
   */
  public void removeAttribute(String name) 
  {
    attributes.remove(name);
  }

  /**
   * XXX
   *
   * @param name XXX
   * @param scope XXX
   */
  public void removeAttributeInScope(String name, int scope) 
  {
    attributes.remove(name); //  XXX [mjw] scope ???
  }

  /**
   * XXX
   *
   * @param name XXX
   * @param attribute XXX
   * @exception NullPointerException XXX
   */
  public void setAttribute(String name, Object attribute)
    throws NullPointerException
  {
    setAttributeWithScope(name, attribute, PAGE_SCOPE);
  }

  /**
   * XXX
   *
   * @param name XXX
   * @param attribute XXX
   * @param scope XXX
   * @exception IllegalArgumentException XXX
   * @exception NullPointerException XXX
   */
  public void setAttributeWithScope(String name,
				    Object attribute,
				    int scope)
    throws IllegalArgumentException, NullPointerException 
  {
    attributes.put(name, attribute); //  XXX [mjw] scope???
  }

  public Exception getException() 
  {
    return (Exception) attributes.get(EXCEPTION);
    //  XXX [mjw] is this right?
  }

  public JspFactory getFactory() 
  {
    return this.factory;
  }

  public JspWriter getInitialOut() 
  {
    return this.initialOut;
  }

  public boolean getNeedsSession() 
  {
    return this.needsSession;
  }

  public JspWriter getOut() 
  {
    return this.out;
  }

  public int getOutBufferSize() 
  {
    return this.out.getBufferSize();
  }

  public Servlet getServlet() 
  {
    return this.servlet;
  }

  public ServletConfig getServletConfig() 
  {
    return this.servlet.getServletConfig();
  }

  public ServletContext getServletContext() 
  {
    return this.context;
  }

  public ServletRequest getServletRequest() 
  {
    return this.request;
  }

  public ServletResponse getServletResponse() 
  {
    return this.response;
  }

  public HttpSession getSession() 
  {
    return this.session;
  }

  public boolean isAutoFlush() 
  {
    return this.out.isAutoFlush();
  }

  public boolean isOutBuffered() 
  {
    return (this.out.getBufferSize() != 0);
  }

  //  Abstract methods

  /**
   * XXX
   *
   * @param bufferSize XXX
   * @param autoflush XXX
   * @exception IOException XXX
   * @exception IllegalArgumentException XXX
   */
  //protected abstract JspWriter createInitialOut(int bufferSize,
  //					boolean autoflush)
  //throws IOException, IllegalArgumentException;

  /**
   * Initialize the JSP page context.
   */
  public abstract void initialize (Servlet servlet,
				   ServletRequest request,
				   ServletResponse response,
				   String errorPageURL,
				   boolean needsSession,
				   int bufferSize,
				   boolean autoFlush)
    throws IOException, IllegalStateException, IllegalArgumentException;

  /**
   * XXX
   *
   * @param urlPath
   *
   * @exception IOException
   * @exception ServletException
   * @exception IllegalArgumentException
   * @exception IllegalStateException
   * @exception SecurityException
   */
  public abstract void forward(String urlPath)
    throws IOException, ServletException, IllegalArgumentException,
    IllegalStateException, SecurityException;

  /**
   * XXX
   *
   * @param urlPath
   *
   * @exception IOException
   * @exception ServletException
   * @exception IllegalArgumentException
   * @exception SecurityException
   */
  public abstract void include(String urlPath)
    throws IOException, ServletException, IllegalArgumentException, SecurityException;

  /**
   * Handle an exception created by the page.
   */
  public abstract void handlePageException (Exception e)
    throws ServletException, IOException;

  /**
   * Handle an exception created by the page.
   */
  public abstract void handlePageException (Throwable t)
    throws ServletException, IOException;

  /**
   * Release this page context.
   */
  public abstract void release ();
}
