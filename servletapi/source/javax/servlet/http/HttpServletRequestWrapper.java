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
package javax.servlet.http;

import javax.servlet.ServletRequestWrapper;

import java.util.Enumeration;

import java.security.Principal;

/**
 * Serves a convenience class for developers to adapt to HttpServletRequests
 * by default, passes all method calls which are parte of the {@link HttpServletRequest}
 * interface through to the wrapped object
 *
 * @since Servlet API 2.3
 */
public class HttpServletRequestWrapper extends ServletRequestWrapper 
  implements HttpServletRequest
{
  private HttpServletRequest _impl;

  /**
   * Creates a new HttpServletRequestWrapper which wraps the passed
   * HttpServletRequest
   *
   * @param wrappedObject the object to be wrapped
   *
   * @throws java.lang.IllegalArgumentException if wrappedObject is null
   */
  public HttpServletRequestWrapper (HttpServletRequest wrappedRequest)
  {
    super (wrappedRequest);
    _impl = wrappedRequest;
  }

  /**
   * By default passes the call to the underlying HttpServletRequest
   */
  public String getAuthType ()
  {
    return _impl.getAuthType ();
  }

  /**
   * By default passes the call to the underlying HttpServletRequest
   */
  public Cookie [] getCookies ()
  {
    return _impl.getCookies ();
  }

  /**
   * By default passes the call to the underlying HttpServletRequest
   */
  public long getDateHeader (String name)
  {
    return _impl.getDateHeader (name);
  }

  /**
   * By default passes the call to the underlying HttpServletRequest
   */
  public String getHeader (String name)
  {
    return _impl.getHeader (name);
  }

  /**
   * By default passes the call to the underlying HttpServletRequest
   */
  public Enumeration getHeaders (String name)
  {
    return _impl.getHeaders (name);
  }

  /**
   * By default passes the call to the underlying HttpServletRequest
   */
  public Enumeration getHeaderNames ()
  {
    return _impl.getHeaderNames ();
  }

  /**
   * By default passes the call to the underlying HttpServletRequest
   */
  public int getIntHeader (String name)
  {
    return _impl.getIntHeader (name);
  }

  /**
   * By default passes the call to the underlying HttpServletRequest
   */
  public String getMethod ()
  {
    return _impl.getMethod ();
  }

  /**
   * By default passes the call to the underlying HttpServletRequest
   */
  public String getPathInfo ()
  {
    return _impl.getPathInfo ();
  }

  /**
   * By default passes the call to the underlying HttpServletRequest
   */
  public String getPathTranslated ()
  {
    return _impl.getPathTranslated ();
  }

  /**
   * By default passes the call to the underlying HttpServletRequest
   */
  public String getContextPath ()
  {
    return _impl.getContextPath ();
  }

  /**
   * By default passes the call to the underlying HttpServletRequest
   */
  public String getQueryString ()
  {
    return _impl.getQueryString ();
  }

  /**
   * By default passes the call to the underlying HttpServletRequest
   */
  public String getRemoteUser ()
  {
    return _impl.getRemoteUser ();
  }

  /**
   * By default passes the call to the underlying HttpServletRequest
   */
  public boolean isUserInRole (String role)
  {
    return _impl.isUserInRole (role);
  }

  /**
   * By default passes the call to the underlying HttpServletRequest
   */
  public Principal getUserPrincipal ()
  {
    return _impl.getUserPrincipal ();
  }

  /**
   * By default passes the call to the underlying HttpServletRequest
   */
  public String getRequestedSessionId ()
  {
    return _impl.getRequestedSessionId ();
  }

  /**
   * By default passes the call to the underlying HttpServletRequest
   */
  public String getRequestURI ()
  {
    return _impl.getRequestURI ();
  }

  /**
   * By default passes the call to the underlying HttpServletRequest
   */
  public StringBuffer getRequestURL ()
  {
    return _impl.getRequestURL ();
  }

  /**
   * By default passes the call to the underlying HttpServletRequest
   */
  public String getServletPath ()
  {
    return _impl.getServletPath ();
  }

  /**
   * By default passes the call to the underlying HttpServletRequest
   */
  public HttpSession getSession (boolean create)
  {
    return _impl.getSession (create);
  }

  /**
   * By default passes the call to the underlying HttpServletRequest
   */
  public HttpSession getSession ()
  {
    return _impl.getSession ();
  }

  /**
   * By default passes the call to the underlying HttpServletRequest
   */
  public boolean isRequestedSessionIdValid ()
  {
    return _impl.isRequestedSessionIdValid ();
  }

  /**
   * By default passes the call to the underlying HttpServletRequest
   */
  public boolean isRequestedSessionIdFromCookie ()
  {
    return _impl.isRequestedSessionIdFromCookie ();
  }

  /**
   * By default passes the call to the underlying HttpServletRequest
   */
  public boolean isRequestedSessionIdFromURL ()
  {
    return _impl.isRequestedSessionIdFromURL ();
  }

  /**
   * By default passes the call to the underlying HttpServletRequest
   */
  public boolean isRequestedSessionIdFromUrl ()
  {
    return _impl.isRequestedSessionIdFromUrl ();
  }
}
