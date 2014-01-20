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
package javax.servlet.http;

import java.io.IOException;
import java.security.Principal;
import java.util.Collection;
import java.util.Enumeration;
import javax.servlet.ServletException;
import javax.servlet.ServletRequestWrapper;

/**
 * Serves a convenience class for developers to adapt to HttpServletRequests
 * by default, passes all method calls which are part of the {@link HttpServletRequest}
 * interface through to the wrapped object
 *
 * @version 3.0
 * @since 2.3
 * @author Charles Lowell (cowboyd@pobox.com)
 * @author Chris Burdess
 */
public class HttpServletRequestWrapper
    extends ServletRequestWrapper 
    implements HttpServletRequest
{

    /**
     * Creates a new HttpServletRequestWrapper which wraps the passed
     * HttpServletRequest
     *
     * @param wrappedObject the object to be wrapped
     *
     * @throws java.lang.IllegalArgumentException if wrappedObject is null
     */
    public HttpServletRequestWrapper(HttpServletRequest wrappedRequest)
    {
        super(wrappedRequest);
    }

    /**
     * By default passes the call to the underlying HttpServletRequest
     */
    public String getAuthType()
    {
        return ((HttpServletRequest) super.getRequest()).getAuthType();
    }

    /**
     * By default passes the call to the underlying HttpServletRequest
     */
    public Cookie[] getCookies()
    {
        return ((HttpServletRequest) super.getRequest()).getCookies();
    }

    /**
     * By default passes the call to the underlying HttpServletRequest
     */
    public long getDateHeader(String name)
    {
        return ((HttpServletRequest) super.getRequest()).getDateHeader(name);
    }

    /**
     * By default passes the call to the underlying HttpServletRequest
     */
    public String getHeader(String name)
    {
        return ((HttpServletRequest) super.getRequest()).getHeader(name);
    }

    /**
     * By default passes the call to the underlying HttpServletRequest
     */
    public Enumeration getHeaders(String name)
    {
        return ((HttpServletRequest) super.getRequest()).getHeaders(name);
    }

    /**
     * By default passes the call to the underlying HttpServletRequest
     */
    public Enumeration getHeaderNames()
    {
        return ((HttpServletRequest) super.getRequest()).getHeaderNames();
    }

    /**
     * By default passes the call to the underlying HttpServletRequest
     */
    public int getIntHeader(String name)
    {
        return ((HttpServletRequest) super.getRequest()).getIntHeader(name);
    }

    /**
     * By default passes the call to the underlying HttpServletRequest
     */
    public String getMethod()
    {
        return ((HttpServletRequest) super.getRequest()).getMethod();
    }

    /**
     * By default passes the call to the underlying HttpServletRequest
     */
    public String getPathInfo()
    {
        return ((HttpServletRequest) super.getRequest()).getPathInfo();
    }

    /**
     * By default passes the call to the underlying HttpServletRequest
     */
    public String getPathTranslated()
    {
        return ((HttpServletRequest) super.getRequest()).getPathTranslated();
    }

    /**
     * By default passes the call to the underlying HttpServletRequest
     */
    public String getContextPath()
    {
        return ((HttpServletRequest) super.getRequest()).getContextPath();
    }

    /**
     * By default passes the call to the underlying HttpServletRequest
     */
    public String getQueryString()
    {
        return ((HttpServletRequest) super.getRequest()).getQueryString();
    }

    /**
     * By default passes the call to the underlying HttpServletRequest
     */
    public String getRemoteUser()
    {
        return ((HttpServletRequest) super.getRequest()).getRemoteUser();
    }

    /**
     * By default passes the call to the underlying HttpServletRequest
     */
    public boolean isUserInRole(String role)
    {
        return ((HttpServletRequest) super.getRequest()).isUserInRole(role);
    }

    /**
     * By default passes the call to the underlying HttpServletRequest
     */
    public Principal getUserPrincipal()
    {
        return ((HttpServletRequest) super.getRequest()).getUserPrincipal();
    }

    /**
     * By default passes the call to the underlying HttpServletRequest
     */
    public String getRequestedSessionId()
    {
        return ((HttpServletRequest) super.getRequest()).getRequestedSessionId();
    }

    /**
     * By default passes the call to the underlying HttpServletRequest
     */
    public String getRequestURI()
    {
        return ((HttpServletRequest) super.getRequest()).getRequestURI();
    }

    /**
     * By default passes the call to the underlying HttpServletRequest
     */
    public StringBuffer getRequestURL()
    {
        return ((HttpServletRequest) super.getRequest()).getRequestURL();
    }

    /**
     * By default passes the call to the underlying HttpServletRequest
     */
    public String getServletPath()
    {
        return ((HttpServletRequest) super.getRequest()).getServletPath();
    }

    /**
     * By default passes the call to the underlying HttpServletRequest
     */
    public HttpSession getSession(boolean create)
    {
        return ((HttpServletRequest) super.getRequest()).getSession(create);
    }

    /**
     * By default passes the call to the underlying HttpServletRequest
     */
    public HttpSession getSession()
    {
        return ((HttpServletRequest) super.getRequest()).getSession();
    }

    /**
     * By default passes the call to the underlying HttpServletRequest
     */
    public boolean isRequestedSessionIdValid()
    {
        return ((HttpServletRequest) super.getRequest()).isRequestedSessionIdValid();
    }

    /**
     * By default passes the call to the underlying HttpServletRequest
     */
    public boolean isRequestedSessionIdFromCookie()
    {
        return ((HttpServletRequest) super.getRequest()).isRequestedSessionIdFromCookie();
    }

    /**
     * By default passes the call to the underlying HttpServletRequest
     */
    public boolean isRequestedSessionIdFromURL()
    {
        return ((HttpServletRequest) super.getRequest()).isRequestedSessionIdFromURL();
    }

    /**
     * By default passes the call to the underlying HttpServletRequest
     * @deprecated Use {@link #isRequestedSessionIdFromURL}
     * @since 2.0
     */
    public boolean isRequestedSessionIdFromUrl()
    {
        return ((HttpServletRequest) super.getRequest()).isRequestedSessionIdFromURL();
    }

    /**
     * By default passes the call to the underlying HttpServletRequest
     * @since 3.0
     */
    public boolean authenticate(HttpServletResponse response)
        throws IOException, ServletException
    {
        return ((HttpServletRequest) super.getRequest()).authenticate(response);
    }

    /**
     * By default passes the call to the underlying HttpServletRequest
     * @since 3.0
     */
    public void login(String username, String password)
        throws ServletException
    {
        ((HttpServletRequest) super.getRequest()).login(username, password);
    }

    /**
     * By default passes the call to the underlying HttpServletRequest
     * @since 3.0
     */
    public void logout()
        throws ServletException
    {
        ((HttpServletRequest) super.getRequest()).logout();
    }

    /**
     * By default passes the call to the underlying HttpServletRequest
     * @since 3.0
     */
    public Collection<Part> getParts()
        throws IOException, ServletException
    {
        return ((HttpServletRequest) super.getRequest()).getParts();
    }

    /**
     * By default passes the call to the underlying HttpServletRequest
     * @since 3.0
     */
    public Part getPart(String name)
        throws IOException, ServletException
    {
        return ((HttpServletRequest) super.getRequest()).getPart(name);
    }

}
