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
import java.io.UnsupportedEncodingException;
import java.io.BufferedReader;
import java.text.MessageFormat;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * This class provides an adapter that makes it easy to wrap a request
 * The default behavior of this class is to pass all method calls in 
 * the {@link ServletRequest} interface through to the underlying request
 * object
 * @version 3.0
 * @since 2.3
 * @author Charles Lowell (cowboyd@pobox.com)
 * @author Chris Burdess
 */
public class ServletRequestWrapper
    implements ServletRequest
{

    private static final ResourceBundle L10N =
        ResourceBundle.getBundle("javax.servlet.L10N");
    
    private ServletRequest request;

    /**
     * Create a new wrapper which will wrap the given request
     * the default behaviour is to pass all method calls in
     * the ServletRequest interface to the 
     *
     * @param wrappedRequest the request that will be wrapped
     * by this object 
     * @throws java.lang.IllegalArgumentException if wrappedRequest is null
     */
    public ServletRequestWrapper(ServletRequest wrappedRequest)
    {
        if (wrappedRequest == null) 
        {
            throw new IllegalArgumentException("Constructor called with " +
                    "null argument");
        }
        request = wrappedRequest;
    }

    /**
     * Get the request wrapped by this object
     *
     * @return the wrapped request object
     */

    public ServletRequest getRequest()
    {
        return request;
    }

    /**
     * Sets a new wrapper
     *
     * @param wrappedRequest the request that will be wrapped
     * by this object 
     * @throws java.lang.IllegalArgumentException if wrappedRequest is null
     */
    public void setRequest(ServletRequest wrappedRequest)
    {
        if (wrappedRequest == null) 
        {
            throw new IllegalArgumentException("setRequest called with " +
                    "null argument");
        }
        request  = wrappedRequest;
    }

    /**
     * Calls the wrapped request object's method
     */
    public Object getAttribute(String name)
    {
        return request.getAttribute(name);
    }

    /**
     * Calls the wrapped request object's method
     */
    public Enumeration getAttributeNames()
    {
        return request.getAttributeNames();
    }

    /**
     * Calls the wrapped request object's method
     */
    public String getCharacterEncoding()
    {
        return request.getCharacterEncoding();
    }

    /**
     * Calls the wrapped request object's method
     */
    public void setCharacterEncoding(String enc)
        throws UnsupportedEncodingException
    {
        request.setCharacterEncoding(enc);
    }

    /**
     * Calls the wrapped request object's method
     */
    public int getContentLength()
    {
        return request.getContentLength();
    }

    /**
     * Calls the wrapped request object's method
     */
    public String getContentType()
    {
        return request.getContentType();
    }

    /**
     * Calls the underlying request object's method
     * @throws IOException
     * @throws IllegalStateException
     */
    public ServletInputStream getInputStream()
        throws IOException
    {
        return request.getInputStream();
    }

    /**
     * Calls the underlying request object's method
     */
    public String getParameter(String name)
    {
        return request.getParameter(name);
    }

    /**
     * Calls the underlying request object's method
     */
    public Map getParameterMap()
    {
        return request.getParameterMap();
    }

    /**
     * Calls the underlying request object's method
     */
    public Enumeration getParameterNames()
    {
        return request.getParameterNames();
    }

    /**
     * Calls the underlying request object's method
     */
    public String[] getParameterValues(String name)
    {
        return request.getParameterValues(name);
    }

    /**
     * Calls the underlying request object's method
     */
    public String getProtocol()
    {
        return request.getProtocol();
    }

    /**
     * Calls the underlying request object's method
     */
    public String getScheme()
    {
        return request.getScheme();
    }

    /**
     * Calls the underlying request object's method
     */
    public String getServerName()
    {
        return request.getServerName();
    }

    /**
     * Calls the underlying request object's method
     */
    public int getServerPort()
    {
        return request.getServerPort();
    }

    /**
     * Calls the underlying request object's method
     * @throws IOException
     * @throws java.io.UnsupportedEncodingException
     * @throws IllegalStateException
     */
    public BufferedReader getReader()
        throws IOException
    {
        return request.getReader();
    }

    /**
     * Calls the underlying request object's method
     */
    public String getRemoteAddr()
    {
        return request.getRemoteAddr();
    }

    /**
     * Calls the underlying request object's method
     */
    public String getRemoteHost()
    {
        return request.getRemoteHost();
    }

    /**
     * Calls the underlying request object's method
     */
    public void setAttribute(String name, Object value)
    {
        request.setAttribute(name, value);
    }

    /**
     * Calls the underlying request object's method
     */
    public void removeAttribute(String name)
    {
        request.removeAttribute(name);
    }

    /**
     * Calls the underlying request object's method
     */
    public Locale getLocale()
    {
        return request.getLocale();
    }

    /**
     * Calls the underlying request object's method
     */
    public Enumeration getLocales()
    {
        return request.getLocales();
    }

    /**
     * Calls the underlying request object's method
     */
    public boolean isSecure()
    {
        return request.isSecure();
    }

    /**
     * Calls the underlying request object's method
     */
    public RequestDispatcher getRequestDispatcher(String path)
    {
        return request.getRequestDispatcher(path);
    }

    /**
     * Calls the underlying request object's method
     * @deprecated Should use getRealPath from the current ServletContext.
     * @see javax.servlet.ServletContext#getRealPath(java.lang.String)
     */
    public String getRealPath(String path)
    {
        return request.getRealPath(path);
    }

    /**
     * Calls the underlying request object's method
     * @since 2.4
     */
    public int getRemotePort()
    {
        return request.getRemotePort();
    }

    /**
     * Calls the underlying request object's method
     * @since 2.4
     */
    public String getLocalName()
        throws IOException
    {
        return request.getLocalName();
    }

    /**
     * Calls the underlying request object's method
     * @since 2.4
     */
    public String getLocalAddr()
        throws IOException
    {
        return request.getLocalAddr();
    }

    /**
     * Calls the underlying request object's method
     * @since 2.4
     */
    public int getLocalPort()
        throws IOException
    {
        return request.getLocalPort();
    }

    /**
     * Calls the underlying request object's method
     * @since 3.0
     */
    public ServletContext getServletContext()
    {
        return request.getServletContext();
    }

    /**
     * Calls the underlying request object's method
     * @since 3.0
     */
    public AsyncContext startAsync()
        throws IllegalStateException
    {
        return request.startAsync();
    }

    /**
     * Calls the underlying request object's method
     * @since 3.0
     */
    public AsyncContext startAsync(ServletRequest request, ServletResponse response)
        throws IllegalStateException
    {
        return request.startAsync(request, response);
    }

    /**
     * Calls the underlying request object's method
     * @since 3.0
     */
    public boolean isAsyncStarted()
    {
        return request.isAsyncStarted();
    }

    /**
     * Calls the underlying request object's method
     * @since 3.0
     */
    public boolean isAsyncSupported()
    {
        return request.isAsyncSupported();
    }

    /**
     * Calls the underlying request object's method
     * @since 3.0
     */
    public AsyncContext getAsyncContext()
    {
        return request.getAsyncContext();
    }

    /**
     * Indicates if this wrapper wraps the specified request.
     * @since 3.0
     */
    public boolean isWrapperFor(ServletRequest wrapped)
    {
        if (request == wrapped)
          {
            return true;
          }
        if (request instanceof ServletRequestWrapper)
          {
            return ((ServletRequestWrapper) request).isWrapperFor(wrapped);
          }
        return false;
    }

    /**
     * Indicates if this wrapper wraps a request of the specified class.
     * @since 3.0
     */
    public boolean isWrapperFor(Class wrappedType)
    {
        if (!ServletRequest.class.isAssignableFrom(wrappedType))
          {
            String message = L10N.getString("err.not_subinterface");
            Object[] args = new Object[] { wrappedType.getName(), "javax.servlet.ServletRequest" };
            throw new IllegalArgumentException(MessageFormat.format(message, args));
          }
        if (wrappedType.isAssignableFrom(request.getClass()))
          {
            return true;
          }
        if (request instanceof ServletRequestWrapper)
          {
            return ((ServletRequestWrapper) request).isWrapperFor(wrappedType);
          }
        return false;
    }

    /**
     * Calls the underlying request object's method
     * @since 3.0
     */
    public DispatcherType getDispatcherType()
    {
        return request.getDispatcherType();
    }

}
