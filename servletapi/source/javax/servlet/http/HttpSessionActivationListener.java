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
 * Objects that are bound to a session as an attribute and implementing
 * this interface will be notified whenever the session to which they
 * are bound is activated or passivated. It is the responsibility of the
 * container to notify all such objects implementing this interface
 * upon activation and passivation
 *
 * @version Servlet API 2.3
 * @since Servlet API 2.3
 * @author Charles Lowell (cowboyd@pobox.com)
 */

public interface HttpSessionActivationListener
extends EventListener
{
  /**
   * This method is called to notify that the session has just been activated
   * 
   * @since Servlet API 2.3
   * 
   * @param event the event representing the session activation
   */
  void sessionDidActivate (HttpSessionEvent event);

  /**
   * This method is called to notify that the session will soon be passivated
   *
   * @since Servlet API 2.3
   *
   * @param event the event representing the upcoming passivation
   */
  void sessionWillPassivate (HttpSessionEvent event);
}
