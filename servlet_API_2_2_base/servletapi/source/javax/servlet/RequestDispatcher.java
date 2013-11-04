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
 * @version Servlet API 2.2
 * @since Servlet API 2.1
 * @author Paul Siegmann (pauls@euronet.nl)
 */
public interface RequestDispatcher 
{

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
   * @since Servlet API 2.1
   *
   * @param request the original request
   * @param response the response to which output should be written
   * @exception ServletException can be thrown by the target resource
   * @exception IOException if an I/O-error occurs
   * @exception IllegalStateException if <code>getOutputStream</code> or <code>getWriter</code> has already been called on the <code>response</code>
   */
  void forward(ServletRequest request,ServletResponse response)
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
   * @since Servlet API 2.1
   *
   * @param request the original request
   * @param response the original response
   * @exception ServletException can be thrown by the target resource
   * @exception IOException if an I/O-error occurs
   */
  void include(ServletRequest request,ServletResponse response)
  throws ServletException, IOException;

}
