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
import javax.servlet.jsp.tagext.BodyContent;

/**
 * The definition of a page.
 *
 * @author Mark Wielaard
 * @author Nic Ferrier <nferrier@tapsellferrier.co.uk>
 */
public abstract class PageContext 
extends JspContext
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

  public abstract Exception getException();

  public abstract ServletConfig getServletConfig();

  public abstract ServletContext getServletContext();

  public abstract HttpSession getSession();

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
    throws IOException, ServletException;

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
    throws IOException, ServletException;

  /**
   * XXX
   *
   * @param urlPath
   * @param flush 
   *
   * @since JSP 2.0
   * 
   * @exception IOException
   * @exception ServletException
   * @exception IllegalArgumentException
   * @exception SecurityException
   */
  public abstract void include(String urlPath, boolean flush)
    throws IOException, ServletException;

  /**
   * Handle an exception created by the page.
   * @param e
   * @exception ServletException
   * @exception IOException
   * @exception NullPointerException
   * @exception SecurityException
   */
  public abstract void handlePageException (Exception e)
    throws ServletException, IOException;

  /**
   * Handle an exception created by the page.
   * @param t
   * @exception ServletException
   * @exception IOException
   * @exception NullPointerException
   * @exception SecurityException
   */
  public abstract void handlePageException (Throwable t)
    throws ServletException, IOException;

  /**
   * Release this page context.
   */
  public abstract void release ();

  /**
   * get the current page (a servlet)
   * 
   * @return XXX
   */
  public abstract Object getPage();

  /**
   * get the current servlet request object
   * 
   * @return the request
   */
  public abstract ServletRequest getRequest();

  /**
   * get the current servlet response object
   * 
   * @return the response
   */
  public abstract ServletResponse getResponse();

  /**
   * XXX
   * @return a BodyContent
   */
  public BodyContent pushBody()
  {
    return null; // BAD! XXX
  }

  /**
   * 
   * @return a JspWriter
   */
  public JspWriter popBody()
  {
    return (JspWriter) this.attributes.get( PageContext.OUT );
  }

  /**
   * 
   * @return an ErrorData
   * @since JSP 2.0
   */
  public ErrorData getErrorData()
  {
    return null;
  }

}
