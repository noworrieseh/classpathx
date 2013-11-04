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


/** if a developer wanna be notified when an attribute change in requests.
 *
 * @author Arnaud Vandyck - arnaud.vandyck@ulg.ac.be
 * @version Servlet API 2.4
 * @since Servlet API 2.4
 */
public interface ServletRequestAttributeListener
{
  public void attributeAdded(ServletRequestAttributeEvent evt);

  public void attributeReplaced(ServletRequestAttributeEvent evt);

  public void attributeRemoved(ServletRequestAttributeEvent evt);

}
