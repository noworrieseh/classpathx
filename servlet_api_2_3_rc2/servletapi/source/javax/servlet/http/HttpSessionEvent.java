/*
  GNU-Classpath Extensions: Servlet API
  Copyright (C) 2001   Free Software Foundation, Inc.

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


import java.util.EventObject;


/** event which represents a session lifecycle event.
 * This is the event which is passed to {@link #HttpSessionListener}.
 *
 * @author Nic Ferrier - nferrier@tapsellferrier.co.uk
 * @version servlet API 2.3
 * @since Servlet API 2.3
 */
public class HttpSessionEvent
extends EventObject
{

  /** construct the event with the session the event is about.
   */
  public HttpSessionEvent(HttpSession source)
  {
    super(source);
  }

  /** @return the event source cast to a session
   */
  public HttpSession getSession()
  {
    return (HttpSession)super.getSource();
  }

}
