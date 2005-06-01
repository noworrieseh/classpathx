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


import java.util.Enumeration;


/** configuration information for filters.
 * Whenever a server wants to pass initialization data to a filter
 * it creates a class which implements this interface.<BR>
 * The server then adds {String,String} pairs to the class, and the servlet
 * can read these using this interface.
 *
 * @version Servlet API 2.4
 * @since Servlet API 2.3
 * @author Nic Ferrier (nferrier@tapsellferrier.co.uk)
 */
public interface FilterConfig
{
  /** get the value of this name's initparameter 
   *
   * @param name the name of the Parameter whose value we want
   * @return value of this parameter associated with the name
   *  or null if it doesn't exist
   * @since Servlet API 2.3
   */
  String getInitParameter(String name);

  /** get the names of all the init parameters.
   *
   * @return all the init parameter names or an empty empty enumeration
   *   when there are no init parameters
   * @since Servlet API 1.0
   */
  Enumeration getInitParameterNames();

  /** get the context that this filter belongs to.
   *
   * @return the context of the filter whose config this is
   * @since Servlet API 1.0
   */
  ServletContext getServletContext();

  /** get the name of the filter.
   * As specified in a webapp deployment descriptor for example.
   *
   * @return the name of the filter.
   */
  String getFilterName();

}
