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
 * XXX
 */
public abstract class PageContext 
{

  //  static final strings used in attribute name table
  //  [mjw] We don't know what the real values are XXX!!!

  /**
   * XXX
   */
  public static final String APPCONTEXT = "XXX_APPCONTEXT";

  /**
   * XXX
   */
  public static final String CONFIG = "XXX_CONFIG";

  /**
   * XXX
   */
  public static final String EXCEPTION = "XXX_EXCEPTION";

  /**
   * XXX
   */
  public static final String OUT = "XXX_OUT";

  /**
   * XXX
   */
  public static final String PAGE = "XXX_PAGE";

  /**
   * XXX
   */
  public static final String PAGECONTEXT = "XXX_PAGECONTEXT";

  /**
   * XXX
   */
  public static final String REQUEST = "XXX_REQUEST";

  /**
   * XXX
   */
  public static final String RESPONSE = "XXX_RESPONSE";

  /**
   * XXX
   */
  public static final String SESSION = "XXX_SESSION";

  //  static final ints used to denote the scope
  //  [mjw] We don't know what the real values are XXX!!!

  /**
   * XXX
   */
  public static final int APPLICATION_SCOPE = -1; //  XXX

  /**
   * XXX
   */
  public static final int PAGE_SCOPE = -2; //  XXX

  /**
   * XXX
   */
  public static final int REQUEST_SCOPE = -3; //  XXX

  /**
   * XXX
   */
  public static final int SESSION_SCOPE = -4; //  XXX

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
  
  /**
   * XXX
   *
   * @param fac XXX
   * @param s XXX
   * @param req XXX
   * @param res XXX
   * @param needsSession XXX
   * @param bufferSize XXX
   * @param autoflush XXX
   *
   * @exception IOException XXX
   * @exception IllegalStateException XXX
   * @exception IllegalArgumentException XXX
   */
  protected PageContext (JspFactory fac,
			 Servlet s,
			 ServletRequest req,
			 ServletResponse res,
			 boolean needsSession,
			 int bufferSize,
			 boolean autoflush)
    throws IOException, IllegalStateException, IllegalArgumentException 
  {
    // What on earth is this for? Is it GNUJSP specific?
    this.factory = fac;
    this.servlet = s;
    this.request = req;
    this.response = res;
    this.needsSession = needsSession;

    this.initialOut = createInitialOut(bufferSize, autoflush);
    this.out = initialOut;

    //  [mjw] Should we also create:
    //  - attributes
    attributes = new Hashtable();
    //  - context
    context = s.getServletConfig().getServletContext();
    //  - session
    if (req instanceof HttpServletRequest) 
      {
	session = ((HttpServletRequest)request).getSession();
      }
    else 
      {
	session = null; //  XXX [mjw] ehe...
      }
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
  protected abstract JspWriter createInitialOut(int bufferSize,
						boolean autoflush)
    throws IOException, IllegalArgumentException;

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
   * @param out
   *
   * @exception IOException
   * @exception ServletException
   * @exception IllegalArgumentException
   * @exception SecurityException
   */
  public abstract void include(String urlPath, JspWriter out)
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

}
