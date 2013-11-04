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

/** a request/response filter.
 * Filters are classes that allow you to change the request and
 * response that a target servlet receives.
 *
 * <h4>Lifecycle</h4>
 * <p>Developers must implement this interface for filtering code and
 * then cause their <code>Filter</code>s to be mapped to servlets or
 * url-patterns (normally by using a webapp's deployment descriptor).</p>
 *
 * <p>When the server loads the filter it causes the <code>init()</code>
 * method to be called with the necessary config information (including
 * init parameters, specified in the webapp's DD perhaps).</p>
 *
 * <p>When the server recieves requests that match the filter conditions
 * it calls the <code>doFilter()</code> method of the first matching
 * filter. The remaining filters (and the target servlet) are passed in
 * the <code>FilterChain</code> argument. The developer must call the
 * <code>FilterChain.doFilter()</code> method to pass the request on to
 * the next filter.</p>
 *
 * <p>When the server has finished with the filter it calls the
 * <code>destory()</code> method <em>after</em> the filter has been removed
 * from use.</p>
 *
 * <p>For more information see section 6 of the Servlet API 2.3
 * specification.</p>
 *
 * @see FilterChain for more information on how the chains work
 * @version 3.0
 * @since 2.3
 * @author Nic Ferrier - Tapsell-Ferrier Limited, nferrier@tfltd.net
 * @author Charles Lowell - cowboyd@pobox.com
*/
public interface Filter
{

    /** initialize a filter.
     *
     * @param init the filter's configuration information (including init params)
     */
    public void init(FilterConfig init)
        throws ServletException;

    /** filter the request/response.
     *
     * @param request the request to be filtered
     * @param response the response to be filtered
     * @param chain the remaining filters (and the target servlet) in the request chain
     * @throws ServletException
     * @throws IOException
     */
    public void doFilter(ServletRequest request,
            ServletResponse response,
            FilterChain chain)
        throws ServletException, IOException;

    /** destroy a filter.
     */
    public void destroy();

}
