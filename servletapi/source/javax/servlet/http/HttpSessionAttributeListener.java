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
 * Implementers of this interface can receive notifications when
 * the attributes of the sessions in a web application change
 * either through addittion, deletion, or replacement
 *
 * @version Servlet API 2.3
 * @since Servlet API 2.3
 * @author Charles Lowell (cowboyd@pobox.com)
 */

public interface HttpSessionAttributeListener
{
  /**
   * Indicates that an attribute has been added to a session
   *
   * @since Servlet API 2.3
   */
  void attributeAdded (HttpSessionBindingEvent event);

  /**
   * Indicates that an attribute has been removed from a session
   *
   * @since Servlet API 2.3
   */
  void attributeRemoved (HttpSessionBindingEvent event);

  /**
   * Indicates that an attribute has been replaced in a session
   *
   * @since Servlet API 2.3
   */
  void attributeReplaced (HttpSessionBindingEvent event);
}
