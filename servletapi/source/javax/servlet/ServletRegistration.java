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
import java.util.Set;

/**
 * Configuration interface for a servlet.
 * @version 3.0
 * @since 3.0
 * @author Chris Burdess
 */
public interface ServletRegistration
    extends Registration
{

    /**
     * Dynamic support.
     */
    interface Dynamic
        extends ServletRegistration, Registration.Dynamic
    {

        /**
         * Set the <code>loadOnStartup</code> descriptor property.
         */
        void setLoadOnStartup(int loadOnStartup);

        /**
         * Set the servlet security element to be applied to the mappings in
         * the registration.
         */
        Set<String> setServletSecurity(ServletSecurityElement element);

        /**
         * Set the multipart config element to be applied to the mappings in
         * the registration.
         */
        void setMultipartConfig(MultipartConfigElement multipartConfig);

        /**
         * Set the <code>runAsRole</code> descriptor property.
         */
        void setRunAsRole(String roleName);

    }

    /**
     * Add a servlet mapping with the given URL patterns.
     * @exception IllegalStateException if the context is already
     * initialized
     */
    Set<String> addMapping(String... urlPatterns);

    /**
     * Returns the mappings for the servlet.
     */
    Collection<String> getMappings();

    /**
     * Returns the <code>runAsRole</code> descriptor property.
     */
    String getRunAsRole();

}
