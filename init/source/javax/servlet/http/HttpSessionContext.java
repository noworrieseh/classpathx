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

import java.util.Enumeration;

/**
 * Contains information shared by all the HttpSessions.
 *
 * @see javax.servlet.http.HttpSessionContext
 * @deprecated
 * This class has been deprecated for security reasons.<BR>
 * We don't want serlvets messing around in other sessions.<BR>
 * However convenient that might be.<BR>
 * @version Servlet API 2.2
 * @since Servlet API 2.0
 * @author Paul Siegmann (pauls@euronet.nl)
 */
public interface HttpSessionContext
{
  /**
   * Get the session with the given id.
   * @deprecated This method should always return null
   *
   * @since Servlet API 2.0
   *
   * @param id the id of the HttpSession we're looking for.
   * @return The HttpSession we're looking for, null if not present.
   */
  HttpSession getSession(String id);


  /**
   * Get all sessions ids.
   * @deprecated This method should always return an empty enumeration
   *
   * @since Servlet API 2.0
   *
   * @return an Enumeration containing all session id's.
   */
  Enumeration getIds();
}
