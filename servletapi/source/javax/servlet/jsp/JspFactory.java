/*
 * JspFactory.java -- XXX
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

import javax.servlet.Servlet;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * XXX
 */
public abstract class JspFactory 
{

  //  Class variables

  //  Holds the default JspFactory
  //  set with setDefaultFactory()
  //  returned by getDefaultFactory() 
  private static JspFactory defaultFactory;

  //  Constructors

  /**
   * XXX [mjw] Why is this public, shouldn't it be protected?
   */
  public JspFactory() {}


  //  Methods

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
   * Release a PageContext.
   */
  public abstract void releasePageContext (PageContext ctx);

  /**
   * XXX
   *
   * @return XXX
   */
  public static JspFactory getDefaultFactory() 
  {
    return defaultFactory;
  }
	

  /**
   * XXX
   *
   * @param fac XXX
   */
  public static void setDefaultFactory(JspFactory fac) 
  {
    defaultFactory = fac;
  }

}
