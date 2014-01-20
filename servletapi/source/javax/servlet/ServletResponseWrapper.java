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
import java.io.PrintWriter;
import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * A convenience class for developers to adapt to a ServletResponse. All
 * methods which are part of the {@link ServletResponse} interface fall
 * through to the underlying wrapped object.
 *
 * @version 3.0
 * @since 2.3
 * @author Charles Lowell (cowboyd@pobox.com)
 * @author Chris Burdess
 */

public class ServletResponseWrapper 
    implements ServletResponse
{

    private static final ResourceBundle L10N =
        ResourceBundle.getBundle("javax.servlet.L10N");

    private ServletResponse response;

    /**
     * Create a new ServletResponseWrapper by wrapping the supplied
     * ServletResponse.
     *
     * @param response the object to be wrapped
     * @throws java.lang.IllegalArgumentException if wrappedResponse is null
     */
    public ServletResponseWrapper(ServletResponse response)
    {
        if (response == null)
          {
            throw new IllegalArgumentException ("constructor called with " +
                    "null argument");
          }
        this.response = response;
    }

    /**
     * get the response from the wrapper.
     * @return the servlet response from the wrapper.
     */
    public ServletResponse getResponse()
    {
        return response;
    }

    /**
     * set a new wrapper
     * 
     * @param response the object to be wrapped
     * @throws java.lang.IllegalArgumentException if wrappedResponse is null
     */
    public void setResponse(ServletResponse response)
    {
        if (response == null)
          {
            throw new IllegalArgumentException("setResponse called with " +
                    "null argument");
          }
        this.response = response;
    }

    /**
     * set the character encoding.
     * @param enc the character encoding
     * @since 2.4
     */
    public void setCharacterEncoding(String enc)
    {
        response.setCharacterEncoding(enc);
    }

    /**
     * fall through to the underlying object by default.
     */
    public String getCharacterEncoding()
    {
        return response.getCharacterEncoding();
    }

    /**
     * fall through to the underlying object by default.
     */
    public ServletOutputStream getOutputStream()
        throws IOException
    {
        return response.getOutputStream();
    }

    /**
     * fall through to the underlying object by default.
     * @throws IOException
     * @throws java.io.UnsupportedEncodingException
     * @throws IllegalStateException
     */
    public PrintWriter getWriter()
        throws IOException
    {
        return response.getWriter();
    }

    /**
     * fall through to the underlying object by default.
     */
    public void setContentLength(int length)
    {
        response.setContentLength(length);
    }

    /**
     * fall through to the underlying object by default.
     */
    public void setContentType(String type)
    {
        response.setContentType(type);
    }

    /**
     * get the content type
     * @return the content type
     * @since 2.4
     */
    public String getContentType()
    {
        return response.getContentType();
    }

    /**
     * fall through to the underlying object by default.
     */
    public void setBufferSize(int size)
    {
        response.setBufferSize(size);
    }

    /**
     * fall through to the underlying object by default.
     */
    public int getBufferSize()
    {
        return response.getBufferSize();
    }

    /**
     * fall through to the underlying object by default.
     */
    public void flushBuffer()
        throws IOException
    {
        response.flushBuffer();
    }

    /**
     * fall through to the underlying object by default.
     */
    public boolean isCommitted()
    {
        return response.isCommitted();
    }

    /**
     * fall through to the underlying object by default.
     * @throws IllegalStateException 
     */
    public void reset()
    {
        response.reset();
    }

    /**
     * fall through to the underlying object by default.
     */
    public void resetBuffer()
    {
        response.resetBuffer();
    }

    /**
     * fall through to the underlying object by default.
     */
    public void setLocale(Locale locale)
    {
        response.setLocale(locale);
    }

    /**
     * fall through to the underlying object by default.
     */
    public Locale getLocale()
    {
        return response.getLocale();
    }

    /**
     * Indicates if this wrapper wraps the specified response.
     * @since 3.0
     */
    public boolean isWrapperFor(ServletResponse wrapped)
    {
        if (response == wrapped)
          {
            return true;
          }
        if (response instanceof ServletResponseWrapper)
          {
            return ((ServletResponseWrapper) response).isWrapperFor(wrapped);
          } 
        return false;
    }

    /**
     * Indicates if this wrapper wraps a response of the specified class.
     * @since 3.0
     */
    public boolean isWrapperFor(Class wrappedType)
    {
        if (!ServletResponse.class.isAssignableFrom(wrappedType))
          {
            String message = L10N.getString("err.not_subinterface");
            Object[] args = new Object[] { wrappedType.getName(), "javax.servlet.ServletResponse" };
            throw new IllegalArgumentException(MessageFormat.format(message, args));
          }
        if (wrappedType.isAssignableFrom(response.getClass()))
          {
            return true;
          }
        if (response instanceof ServletResponseWrapper)
          {
            return ((ServletResponseWrapper) response).isWrapperFor(wrappedType);
          }
        return false;
    }

}
