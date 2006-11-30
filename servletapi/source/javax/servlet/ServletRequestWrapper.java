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

import java.util.Enumeration;
import java.util.Locale;
import java.util.Map;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.io.BufferedReader;

/**
 * This class provides an adapter that makes it easy to wrap a request
 * The default behavior of this class is to pass all method calls in 
 * the {@link ServletRequest} interface through to the underlying request
 * object
 *
 * @since Servlet API 2.3
 * @author Charles Lowell (cowboyd@pobox.com)
 */

public class ServletRequestWrapper
  implements ServletRequest
{

  private ServletRequest _impl;

  /**
   * Create a new wrapper which will wrap the given request
   * the default behaviour is to pass all method calls in
   * the ServletRequest interface to the 
   *
   * @since Servlet API 2.3
   *
   * @param wrappedRequest the request that will be wrapped
   * by this object 
   * @throws java.lang.IllegalArgumentException if wrappedRequest is null
   */
  public ServletRequestWrapper(ServletRequest wrappedRequest)
  {
    if (wrappedRequest == null) 
      {
        throw new IllegalArgumentException("Constructor called with " +
                                           "null argument");
      }
    _impl = wrappedRequest;
  }
  
  /**
   * Get the request wrapped by this object
   *
   * @since Servlet API 2.3
   *
   * @return the wrapped request object
   */

  public ServletRequest getRequest()
  {
    return _impl;
  }

  /**
   * Calls the wrapped request object's method
   */
  public Object getAttribute(String name)
  {
    return _impl.getAttribute(name);
  }
  /**
   * Calls the wrapped request object's method
   */
  public Enumeration getAttributeNames()
  {
    return _impl.getAttributeNames();
  }

  /**
   * Calls the wrapped request object's method
   */
  public String getCharacterEncoding()
  {
    return _impl.getCharacterEncoding();
  }
  /**
   * Calls the wrapped request object's method
   */
  public void setCharacterEncoding(String enc)
    throws UnsupportedEncodingException
  {
    _impl.setCharacterEncoding(enc);
  }
  /**
   * Calls the wrapped request object's method
   */
  public int getContentLength()
  {
    return _impl.getContentLength();
  }
  /**
   * Calls the wrapped request object's method
   */
  public String getContentType()
  {
    return _impl.getContentType();
  }
  /**
   * Calls the underlying request object's method
   * @throws IOException
   * @throws IllegalStateException
   */
  public ServletInputStream getInputStream()
    throws IOException
  {
    return _impl.getInputStream();
  }
  
  public String getLocalAddr()
      throws IOException
  {
      return _impl.getLocalAddr();
  }

  public String getLocalName()
      throws IOException
  {
      return _impl.getLocalName();
  }

  public int getLocalPort()
      throws IOException
  {
      return _impl.getLocalPort();
  }

  /**
   * Calls the underlying request object's method
   */
  public String getParameter(String name)
  {
    return _impl.getParameter(name);
  }
  /**
   * Calls the underlying request object's method
   */
  public Map getParameterMap()
  {
    return _impl.getParameterMap();
  }
   /**
   * Calls the underlying request object's method
   */
  public Enumeration getParameterNames()
  {
    return _impl.getParameterNames();
  }
  /**
   * Calls the underlying request object's method
   */
  public String[] getParameterValues(String name)
  {
    return _impl.getParameterValues(name);
  }
  /**
   * Calls the underlying request object's method
   */
  public String getProtocol()
  {
    return _impl.getProtocol();
  }
  /**
   * Calls the underlying request object's method
   */
  public String getScheme()
  {
    return _impl.getScheme();
  }
  /**
   * Calls the underlying request object's method
   */
  public String getServerName()
  {
    return _impl.getServerName();
  }
  /**
   * Calls the underlying request object's method
   */
  public int getServerPort()
  {
    return _impl.getServerPort();
  }
  /**
   * Calls the underlying request object's method
   * @throws IOException
   * @throws java.io.UnsupportedEncodingException
   * @throws IllegalStateException
   */
  public BufferedReader getReader()
    throws IOException
  {
    return _impl.getReader();
  }
  /**
   * Calls the underlying request object's method
   */
  public String getRemoteAddr()
  {
    return _impl.getRemoteAddr();
  }
  /**
   * Calls the underlying request object's method
   */
  public String getRemoteHost()
  {
    return _impl.getRemoteHost();
  }
  
  public int getRemotePort()
  {
      return _impl.getRemotePort();
  }

  /**
   * Calls the underlying request object's method
   */
  public void setAttribute(String name, Object value)
  {
    _impl.setAttribute(name, value);
  }
  /**
   * Calls the underlying request object's method
   */
  public void removeAttribute(String name)
  {
    _impl.removeAttribute(name);
  }
  /**
   * Calls the underlying request object's method
   */
  public Locale getLocale()
  {
    return _impl.getLocale();
  }
  /**
   * Calls the underlying request object's method
   */
  public Enumeration getLocales()
  {
    return _impl.getLocales();
  }
  /**
   * Calls the underlying request object's method
   */
  public boolean isSecure()
  {
    return _impl.isSecure();
  }
  /**
   * Calls the underlying request object's method
   */
  public RequestDispatcher getRequestDispatcher(String path)
  {
    return _impl.getRequestDispatcher(path);
  }
  /**
   * Calls the underlying request object's method
   * @deprecated Should use getRealPath from the current ServletContext.
   * @see javax.servlet.ServletContext#getRealPath(java.lang.String)
   */
  public String getRealPath(String path)
  {
    return _impl.getRealPath(path);
  }

  /**
   * Sets a new wrapper
   *
   * @since Servlet API 2.3
   *
   * @param wrappedRequest the request that will be wrapped
   * by this object 
   * @throws java.lang.IllegalArgumentException if wrappedRequest is null
   */
  public void setRequest(ServletRequest wrappedRequest)
  {
    if (wrappedRequest == null) 
      {
        throw new IllegalArgumentException("setRequest called with " +
                                           "null argument");
      }
    _impl  = wrappedRequest;
  }
  
}

