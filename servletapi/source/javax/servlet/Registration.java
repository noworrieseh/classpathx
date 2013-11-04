/*
 * Copyright (C) 2013 Free Software Foundation, Inc.
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

import java.util.Map;
import java.util.Set;

/**
 * Base interface for configuring a servlet or filter.
 * @version 3.0
 * @since 3.0
 * @author Chris Burdess
 */
public interface Registration
{

    /**
     * Dynamic registration support.
     */
    public interface Dynamic
        extends Registration
    {

        void setAsyncSupported(boolean flag);

    }

    /**
     * Returns the servlet or filter name.
     */
    String getName();

    /**
     * Returns the class name of the servlet or filter.
     */
    String getClassName();

    /**
     * Sets the specified initialization parameter.
     * @exception IllegalArgumentException if name or value is null
     * @exception IllegalStateException if the context is already
     * initialized
     */
    boolean setInitParameter(String name, String value);

    /**
     * Returns the specified initialization parameter.
     */
    String getInitParameter(String name);

    /**
     * Bulk set operation.
     * @exception IllegalArgumentException if any name or value is null
     * @exception IllegalStateException if the context is already
     * initialized
     */
    Set<String> setInitParameters(Map<String,String> params);

    /**
     * Returns the initialization parameters set for this registration.
     * The returned map will be immutable.
     */
    Map<String,String> getInitParameters();

}
