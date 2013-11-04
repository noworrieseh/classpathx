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

import java.io.IOException;

/** a chain of filters which will be applied to a resource.
 * Constructed by the container when requests require filtering.
 * The chain is a representation of all the filters that apply
 * to a particular request uri. The chain is made available to the
 * filter developer and he is expected to call the single available
 * method to pass control from his filter to the next filter in
 * the chain.
 *
 * @version 3.0
 * @since 2.3
 * @author Nic Ferrier - Tapsell-Ferrier Limited, nferrier@tfltd.net
 */
public interface FilterChain 
{

    /** go to the next filter in the chain or the target resource.
     *
     * @param request the request
     * @param response the response
     */
    public void doFilter(ServletRequest request, ServletResponse response)
        throws ServletException, IOException;

}
