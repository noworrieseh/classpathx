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

import java.util.Collection;
import java.util.EnumSet;

/**
 * Interface for configuring filters.
 * @version 3.0
 * @since 3.0
 * @author Chris Burdess
 */
public interface FilterRegistration
    extends Registration
{

    /**
     * Dynamic registration support.
     */
    public static interface Dynamic
        extends FilterRegistration, Registration.Dynamic
    {
    }

    /**
     * Add a filter mapping with the given servlet names and dispatcher
     * types.
     * @param dispatcherTypes filter mapping dispatcher types, or null to
     * use {@link javax.servlet.DispatcherType#REQUEST}
     * @param isMatchAfter true to match this filter mapping after declared
     * ones in the servlet context, false to match it before
     * @param servletNames the servlet names in the mapping
     * @exception IllegalArgumentException if servletNames is null or empty
     * @exception IllegalStateException if the context is already initialized
     */
    void addMappingForServletNames(EnumSet<DispatcherType> dispatcherTypes,
            boolean isMatchAfter,
            String... servletNames);

    /**
     * Returns the servlet name mappings of the associated filter.
     */
    Collection<String> getServletNameMappings();

    /**
     * Add a filter mapping with the given url patterns and dispatcher
     * types.
     * @param dispatcherTypes filter mapping dispatcher types, or null to
     * use {@link javax.servlet.DispatcherType#REQUEST}
     * @param isMatchAfter true to match this filter mapping after declared
     * ones in the servlet context, false to match it before
     * @param urlPatterns the url patterns in the mapping
     * @exception IllegalArgumentException if urlPatterns is null or empty
     * @exception IllegalStateException if the context is already initialized
     */
    void addMappingForUrlPatterns(EnumSet<DispatcherType> dispatcherTypes,
            boolean isMatchAfter,
            String... urlPatterns);

    /**
     * Returns the url pattern mappings of the associated filter.
     */
    Collection getUrlPatternMappings();

}
