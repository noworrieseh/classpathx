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

import javax.servlet.ServletResponseWrapper;

import java.io.IOException;

/**
 * Provided as a convenience to a developer that wishes to write an adapter
 * to an HttpServletResponse Object. By Default it passes all methods that
 * are part of the HttpServletResponse interface to the underlying 
 * implementation
 *
 * @since Servlet API 2.3
 */
public class HttpServletResponseWrapper extends ServletResponseWrapper 
  implements HttpServletResponse
{
  private HttpServletResponse _impl;

  /**
   * Create a new HttpServletResponseWrapper to act as an adapter for the 
   * supplied HttpServletResponse. The Default behavior is to pass all methods
   * that are part of the HttpServletResponse interface through to the underlying 
   * object 
   * 
   * @param wrappedObject the object to be wrapped
   *
   * @throws java.lang.IllegalArgumentException if wrappedResponse is null
   */
  public HttpServletResponseWrapper (HttpServletResponse wrappedResponse)
  {
    super (wrappedResponse);
    _impl = wrappedResponse;
  }

  /**
   * By default passes the call to the underlying HttpServletResponse
   */
  public void addCookie (Cookie cookie)
  {
    _impl.addCookie (cookie);
  }

  /**
   * By default passes the call to the underlying HttpServletResponse
   */
  public boolean containsHeader (String name)
  {
    return _impl.containsHeader (name);
  }

  /**
   * By default passes the call to the underlying HttpServletResponse
   */
  public String encodeURL (String url)
  {
    return _impl.encodeURL (url);
  }

  /**
   * By default passes the call to the underlying HttpServletResponse
   */
  public String encodeRedirectURL (String url)
  {
    return _impl.encodeRedirectURL (url);
  }

  /**
   * By default passes the call to the underlying HttpServletResponse
   */
  public String encodeUrl (String url)
  {
    return _impl.encodeUrl (url);
  }

  /**
   * By default passes the call to the underlying HttpServletResponse
   */
  public String encodeRedirectUrl (String url)
  {
    return _impl.encodeRedirectUrl (url);
  }
  
  /**
   * By default passes the call to the underlying HttpServletResponse
   */
  public void sendError (int status,String message)
    throws IOException
  {
    _impl.sendError (status,message);
  }

  /**
   * By default passes the call to the underlying HttpServletResponse
   */
  public void sendError (int status)
    throws IOException
  {
    _impl.sendError (status);
  }

  /**
   * By default passes the call to the underlying HttpServletResponse
   */
  public void sendRedirect (String location)
    throws IOException
  {
    _impl.sendRedirect (location);
  }

  /**
   * By default passes the call to the underlying HttpServletResponse
   */
  public void setDateHeader (String name,long date)
  {
    _impl.setDateHeader (name,date);
  }

  /**
   * By default passes the call to the underlying HttpServletResponse
   */
  public void addDateHeader (String name,long date)
  {
    _impl.addDateHeader (name,date);
  }

  /**
   * By default passes the call to the underlying HttpServletResponse
   */
  public void setHeader (String name,String value)
  {
    _impl.setHeader (name,value);
  }

  /**
   * By default passes the call to the underlying HttpServletResponse
   */
  public void addHeader (String name,String value)
  {
    _impl.addHeader (name,value);
  }

  /**
   * By default passes the call to the underlying HttpServletResponse
   */
  public void setIntHeader (String name,int value)
  {
    _impl.setIntHeader (name,value);
  }

  /**
   * By default passes the call to the underlying HttpServletResponse
   */
  public void addIntHeader (String name,int value)
  {
    _impl.addIntHeader (name,value);
  }
  
  /**
   * By default passes the call to the underlying HttpServletResponse
   */
  public void setStatus (int status)
  {
    _impl.setStatus (status);
  }
  
  /**
   * By default passes the call to the underlying HttpServletResponse
   */
  public void setStatus (int status,String msg)
  {
    _impl.setStatus (status,msg);
  }
}
