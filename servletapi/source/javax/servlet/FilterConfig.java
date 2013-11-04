/*
 * Copyright (C) 1998, 1999, 2001, 2013 Free Software Foundation, Inc.
 *
 * This file is part of GNU Classpath Extensions (classpathx).
 * For more information please visit https://www.gnu.org/software/classpathx/
 *
 * classpathx is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * classpathx is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with classpathx.
 * If not, see <http://www.gnu.org/licenses/>.
 */
package javax.servlet;

import java.util.Enumeration;

/** configuration information for filters.
 * Whenever a server wants to pass initialization data to a filter
 * it creates a class which implements this interface.<BR>
 * The server then adds {String,String} pairs to the class, and the servlet
 * can read these using this interface.
 *
 * @version 3.0
 * @since 2.3
 * @author Nic Ferrier (nferrier@tapsellferrier.co.uk)
 */
public interface FilterConfig
{

    /** get the name of the filter.
     * As specified in a webapp deployment descriptor for example.
     *
     * @return the name of the filter.
     */
    String getFilterName();

    /** get the context that this filter belongs to.
     *
     * @return the context of the filter whose config this is
     */
    ServletContext getServletContext();

    /** get the value of this name's initparameter 
     *
     * @param name the name of the Parameter whose value we want
     * @return value of this parameter associated with the name
     *  or null if it doesn't exist
     */
    String getInitParameter(String name);

    /** get the names of all the init parameters.
     *
     * @return all the init parameter names or an empty empty enumeration
     *   when there are no init parameters
     */
    Enumeration getInitParameterNames();

}
