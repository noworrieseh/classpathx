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


import java.util.EventObject;


/** something happened to the specified <code>ServletContext</code>.
 * The event source is defined as the source <code>ServletContext</code>.
 *
 * @author Nic Ferrier - Tapsell-Ferrier Limited, nferrier@tfltd.net
 * @version Servlet API 2.3
 * @since Servlet API 2.3
 */
public class ServletContextEvent
extends EventObject
{

  ServletContext sc;

  /** create the event.
   */
  public ServletContextEvent(ServletContext source)
  {
    super(source);
    sc=source;
  }

  public ServletContext getServletContext()
  {
    return sc;
  }
}
