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
import java.io.PrintWriter;

import java.util.Locale;

/**
 * A convenience class for developers to adapt to a ServletResponse. All
 * methods which are part of the {@link ServletResponse} interface fall
 * through to the underlying wrapped object.
 *
 * @version Servlet API 2.3
 * @since Servlet API 2.3
 * @author Charles Lowell (cowboyd@pobox.com)
 */

public class ServletResponseWrapper 
  implements ServletResponse
{
  private ServletResponse _impl;

  /**
   * Create a new ServletResponseWrapper by wrapping the supplied
   * ServletResponse.
   *
   * @since Servlet API 2.3
   *
   * @param wrappedResponse the object to be wrapped
   * @throws java.lang.IllegalArgumentException if wrappedResponse is null
   */
  public ServletResponseWrapper (ServletResponse wrappedResponse)
  {
    if (wrappedResponse == null)
    {
      throw new IllegalArgumentException ("constructor called with null argument");
    }
    _impl = wrappedResponse;
  }

  /**
   * fall through to the underlying object by default.
   */
  public String getCharacterEncoding ()
  {
    return _impl.getCharacterEncoding ();
  }

  /**
   * fall through to the underlying object by default.
   */
  public ServletOutputStream getOutputStream ()
    throws IOException
  {
    return _impl.getOutputStream ();
  }

  /**
   * fall through to the underlying object by default.
   * @throws IOException
   * @throws java.io.UnsupportedEncodingException
   * @throws IllegalStateException
   */
  public PrintWriter getWriter ()
    throws IOException
  {
    return _impl.getWriter ();
  }

  /**
   * fall through to the underlying object by default.
   */
  public void setContentLength (int length)
  {
    _impl.setContentLength (length);
  }

  /**
   * fall through to the underlying object by default.
   */
  public void setContentType (String type)
  {
    _impl.setContentType (type);
  }

  /**
   * fall through to the underlying object by default.
   */
  public void setBufferSize (int size)
  {
    _impl.setBufferSize (size);
  }

  /**
   * fall through to the underlying object by default.
   */
  public int getBufferSize ()
  {
    return _impl.getBufferSize ();
  }

  /**
   * fall through to the underlying object by default.
   */
  public void flushBuffer ()
    throws IOException
  {
    _impl.flushBuffer ();
  }

  /**
   * fall through to the underlying object by default.
   */
  public boolean isCommitted ()
  {
    return _impl.isCommitted ();
  }

  /**
   * fall through to the underlying object by default.
   * @throws IllegalStateException 
   */
  public void reset ()
  {
    _impl.reset ();
  }

  /**
   * fall through to the underlying object by default.
   */
  public void resetBuffer ()
  {
    _impl.resetBuffer ();
  }

  /**
   * fall through to the underlying object by default.
   */
  public void setLocale (Locale locale)
  {
    _impl.setLocale (locale);
  }

  /**
   * fall through to the underlying object by default.
   */
  public Locale getLocale ()
  {
    return _impl.getLocale ();
  }

  /**
   * get the response from the wrapper.
   * @return the servlet response from the wrapper.
   * 
   * @since Servlet API 2.3
   */
  public ServletResponse getResponse()
  {
    return _impl;
  }

  /**
   * set a new wrapper
   * 
   * @param wrappedResponse the object to be wrapped
   * @throws java.lang.IllegalArgumentException if wrappedResponse is null
   * 
   * @since Servlet API 2.3
   */
  public void setResponse(ServletResponse wrappedResponse)
  {
    if (wrappedResponse == null)
    {
      throw new IllegalArgumentException ("constructor called with null argument");
    }
    _impl = wrappedResponse;
  }

  /**
   * set the character encoding.
   * @since Servlet API 2.3
   * @param enc the character encoding
   */
  public void setCharacterEncoding (String enc)
  {
    _impl.setCharacterEncoding (enc);
  }

  /**
   * get the content type
   * @since Servlet API 2.3
   * @return the content type
   */
  public String getContentType ()
  {
    return _impl.getContentType ();
  }

}
