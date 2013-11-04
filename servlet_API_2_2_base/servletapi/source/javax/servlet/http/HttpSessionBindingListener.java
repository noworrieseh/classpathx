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

import java.util.EventListener;

/**
 * Objects that implement this interface will be called when they are bound or
 * unbound into a <code>HttpSession</code> with a
 * <code>HttpSessionBindingEvent</code>.
 *
 * @see javax.servlet.http.HttpSession
 * @see javax.servlet.http.HttpSession#putValue(java.lang.String, java.lang.Object)
 * @see javax.servlet.http.HttpSession#removeValue(java.lang.String)
 * @see javax.servlet.http.HttpSession#invalidate()
 * @see javax.servlet.http.HttpSessionBindingEvent
 *
 * @version Servlet API 2.2
 * @since Servlet API 2.0
 */
public interface HttpSessionBindingListener
extends EventListener
{
  /**
   * Called when the object is bound to a session.
   *
   * @since Servlet API 2.0
   *
   * @param event The event object containing the name and session
   */
  void valueBound (HttpSessionBindingEvent event);

  /**
   * Called when the object is unbound from a session.
   *
   * @since Servlet API 2.0
   *
   * @param event The event object containing the name and session
   */
  void valueUnbound (HttpSessionBindingEvent event);
}
