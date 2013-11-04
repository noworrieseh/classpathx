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

/**
 * Whenever a server wants to pass initialization data to a servlet, it
 * creates a class which implements this interface.<BR>
 * The server then adds {String,String} pairs to the class, and the servlet
 * can read these using this interface.
 *
 * @version 3.0
 * @since 1.0
 * @author Paul Siegmann (pauls@euronet.nl)
 */
public interface ServletConfig
{

    /**
     * Returns the name of the servlet definition, as specified in the
     * deployment descriptor.
     */
    String getServletName();
    
    /**
     * Get the context of this ServletConfig
     *
     * @return The context of the servlet whose Config this is
     */
    ServletContext getServletContext();

    /**
     * Get the value of this name's initparameter 
     *
     * @param name the name of the Parameter whose value we want
     * @return The value of this name's initparameter or null if it doesn't
     * exist
     */
    String getInitParameter(String name);

    /**
     * Get all InitParameterNames
     *
     * @return An enumeration consisting of all the init parameter names
     * or an empty empty enumeration when there are no init parameters
     */
    Enumeration getInitParameterNames();

}
