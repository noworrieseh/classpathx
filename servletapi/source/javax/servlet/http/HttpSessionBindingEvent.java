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


/**
 * Send to an Object that implements <code>HttpSessionBindingListener</code>
 * when bound into a session or unbound from a session. Gives access to the
 * session and the name used to bind the Object to the session.
 *
 * @see javax.servlet.http.HttpSession
 * @see javax.servlet.http.HttpSession#putValue(java.lang.String, java.lang.Object)
 * @see javax.servlet.http.HttpSession#removeValue(java.lang.String)
 * @see javax.servlet.http.HttpSession#invalidate()
 * @see javax.servlet.http.HttpSessionBindingListener
 *
 * @version Servlet API 2.3
 * @since Servlet API 2.0
 */
public class HttpSessionBindingEvent
extends HttpSessionEvent
{

  private String myName;
  private Object myValue = null;

  /**
   * Creates a new <code>HttpSessionBindingEvent</code> given the session
   * and the name used.
   *
   * @since Servlet API 2.0
   *
   * @param session which the Object was bound to or unbound from
   * @param name which was used to refer to the object
   */
  public HttpSessionBindingEvent(HttpSession session, String name) 
  {
    super(session);
    myName = name;
  }


  /**
   * Creates a new <code>HttpSessionBindingEvent</code> given the session
   * and the name used.
   *
   * @since Servlet API 2.3
   *
   * @param session which the Object was bound to or unbound from
   * @param name which was used to refer to the object
   * @param value 
   */
  public HttpSessionBindingEvent(HttpSession session, String name, Object value) 
  {
    this(session,name);
    myValue = value;
  }


  /**
   * Returns the name used to refer to this Object.
   *
   * @since Servlet API 2.0
   */
  public String getName() 
  {
    return myName;
  }


  /**
   * Returns the value.
   *
   * @since Servlet API 2.3
   */
  public Object getValue() 
  {
    return myValue;
  }
}
