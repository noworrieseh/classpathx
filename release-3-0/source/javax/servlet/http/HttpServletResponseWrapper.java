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
import java.util.Collection;
import javax.servlet.ServletResponseWrapper;

/**
 * Provided as a convenience to a developer that wishes to write an adapter
 * to an HttpServletResponse Object. By Default it passes all methods that
 * are part of the HttpServletResponse interface to the underlying 
 * implementation
 *
 * @version 3.0
 * @since 2.3
 * @author Charles Lowell (cowboyd@pobox.com)
 * @author Chris Burdess
 */
public class HttpServletResponseWrapper
    extends ServletResponseWrapper 
    implements HttpServletResponse
{

    /**
     * Create a new HttpServletResponseWrapper to act as an adapter for the 
     * supplied HttpServletResponse. The Default behavior is to pass all methods
     * that are part of the HttpServletResponse interface through to the underlying 
     * object 
     * 
     * @param response the response to be wrapped
     *
     * @throws java.lang.IllegalArgumentException if response is null
     */
    public HttpServletResponseWrapper(HttpServletResponse response)
    {
        super(response);
    }

    public String getContentType()
    {
        return ((HttpServletResponse) super.getResponse()).getContentType();
    }

    /**
     * By default passes the call to the underlying HttpServletResponse
     */
    public void addCookie(Cookie cookie)
    {
        ((HttpServletResponse) super.getResponse()).addCookie(cookie);
    }

    /**
     * By default passes the call to the underlying HttpServletResponse
     */
    public boolean containsHeader(String name)
    {
        return ((HttpServletResponse) super.getResponse()).containsHeader(name);
    }

    /**
     * By default passes the call to the underlying HttpServletResponse
     */
    public String encodeURL(String url)
    {
        return ((HttpServletResponse) super.getResponse()).encodeURL(url);
    }

    /**
     * By default passes the call to the underlying HttpServletResponse
     */
    public String encodeRedirectURL(String url)
    {
        return ((HttpServletResponse) super.getResponse()).encodeRedirectURL(url);
    }

    /**
     * By default passes the call to the underlying HttpServletResponse
     * @deprecated use <code>encodeURL()</code>
     */
    public String encodeUrl(String url)
    {
        return ((HttpServletResponse) super.getResponse()).encodeURL(url);
    }

    /**
     * By default passes the call to the underlying HttpServletResponse
     * @deprecated use <code>encodeRedirectURL()</code>
     */
    public String encodeRedirectUrl(String url)
    {
        return ((HttpServletResponse) super.getResponse()).encodeRedirectURL(url);
    }

    /**
     * By default passes the call to the underlying HttpServletResponse
     */
    public void sendError(int status, String message)
        throws IOException
    {
        ((HttpServletResponse) super.getResponse()).sendError(status, message);
    }

    /**
     * By default passes the call to the underlying HttpServletResponse
     */
    public void sendError(int status)
        throws IOException
    {
        ((HttpServletResponse) super.getResponse()).sendError(status);
    }

    /**
     * By default passes the call to the underlying HttpServletResponse
     */
    public void sendRedirect(String location)
        throws IOException
    {
        ((HttpServletResponse) super.getResponse()).sendRedirect(location);
    }

    /**
     * By default passes the call to the underlying HttpServletResponse
     */
    public void setDateHeader(String name, long date)
    {
        ((HttpServletResponse) super.getResponse()).setDateHeader(name, date);
    }

    /**
     * By default passes the call to the underlying HttpServletResponse
     */
    public void addDateHeader(String name, long date)
    {
        ((HttpServletResponse) super.getResponse()).addDateHeader(name, date);
    }

    /**
     * By default passes the call to the underlying HttpServletResponse
     */
    public void setHeader(String name, String value)
    {
        ((HttpServletResponse) super.getResponse()).setHeader(name, value);
    }

    /**
     * By default passes the call to the underlying HttpServletResponse
     */
    public void addHeader(String name, String value)
    {
        ((HttpServletResponse) super.getResponse()).addHeader(name, value);
    }

    /**
     * By default passes the call to the underlying HttpServletResponse
     */
    public void setIntHeader(String name, int value)
    {
        ((HttpServletResponse) super.getResponse()).setIntHeader(name, value);
    }

    /**
     * By default passes the call to the underlying HttpServletResponse
     */
    public void addIntHeader(String name, int value)
    {
        ((HttpServletResponse) super.getResponse()).addIntHeader(name, value);
    }

    /**
     * By default passes the call to the underlying HttpServletResponse
     */
    public void setStatus(int status)
    {
        ((HttpServletResponse) super.getResponse()).setStatus(status);
    }

    /**
     * By default passes the call to the underlying HttpServletResponse
     * @deprecated only errors should give an extra error message,
     */
    public void setStatus(int status, String msg)
    {
        ((HttpServletResponse) super.getResponse()).setStatus(status, msg);
    }

    /**
     * By default passes the call to the underlying HttpServletResponse
     * @since 3.0
     */
    public int getStatus()
    {
        return ((HttpServletResponse) super.getResponse()).getStatus();
    }

    /**
     * By default passes the call to the underlying HttpServletResponse
     * @since 3.0
     */
    public String getHeader(String name)
    {
        return ((HttpServletResponse) super.getResponse()).getHeader(name);
    }

    /**
     * By default passes the call to the underlying HttpServletResponse
     * @since 3.0
     */
    public Collection<String> getHeaders(String name)
    {
        return ((HttpServletResponse) super.getResponse()).getHeaders(name);
    }

    /**
     * By default passes the call to the underlying HttpServletResponse
     * @since 3.0
     */
    public Collection<String> getHeaderNames()
    {
        return ((HttpServletResponse) super.getResponse()).getHeaderNames();
    }

}
