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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Map;

/**
 * Whenever the server receives a request it creates a ServletRequest object,
 * puts all the request information in it and passes this along with
 * a ServletResponse object to the approriate servlet.
 *
 * @version 3.0
 * @since 1.0
 * @author Paul Siegmann (pauls@euronet.nl)
 * @author Charles Lowell (cowboyd@pobox.com)
 * @author Chris Burdess
 */
public interface ServletRequest
{

    /**
     * Gets a named attribute's value.
     * This gives one of the initialization attribute values.
     * <p>
     * Note that the Servlet 2.1 API Documentation mentions some predefined
     * attribute names, but the Servlet Spec does not mention them.
     * I (MJW) am not sure if they are platform specific (JWS) or not.
     *
     * @param name the attribute name
     * @return The value of the attribute, null if not found.
     */
    Object getAttribute(String name);

    /**
     * Gets an Enumeration of all the attribute names.
     *
     * @since 2.1
     *
     * @return The Enumeration of all attribute names set in this request.
     */
    Enumeration getAttributeNames();
    
    /**
     * Gets the character encoding of the request data.
     *
     * @since 2.0
     *
     * @return Character encoding or null if the encoding is unavailable
     */
    String getCharacterEncoding();

    /**
     * Sets the name of the character encoding used for the body
     * of this request
     *
     * @since 2.3
     *
     * @param enc a String containing the name of the character encoding
     * @throws java.io.UnsupportedEncodingException if the provided name is not 
     * a valid encoding scheme
     */
    void setCharacterEncoding(String enc)
        throws UnsupportedEncodingException;

    /**
     * Gets the size in bytes of the request
     *
     * @return the number of bytes in the request or -1 if not known
     */
    int getContentLength();

    /**
     * Gets the mime type of the request
     *
     * @return a String containing the mime type of the request
     * or null if not known
     */
    String getContentType();

    /**
     * Creates an inputstream for servlets to read client request data from.
     * @see javax.servlet.ServletRequest#getReader()
     *
     * @return The created InputStreams
     * @throws IOException if an i/o related error occured
     * @throws IllegalStateException if <code>getReader</code> was already
     * called on this request.
     */
    ServletInputStream getInputStream()
        throws IOException;

    /**
     * Gets the value of a named requestparameter.
     * If the parameter can have more than one value
     * <code>getParameterValues</code> should be used.
     * If there are more than one values associated with the parameter this
     * method will only return the first value as return by
     * <code>getParameterValues</code> is returned.
     * see javax.servlet.ServletRequest.getParameterValues()
     *
     * @param name the name of the parameter whose value we want
     * @return the (first) value of the parameter or null if not present
     */
    String getParameter(String name);

    /**
     * Gets all parameter names.
     * <p>
     * Note that the Servlet API 2.1 documentation says that this returns
     * an empty Enumeration if the input stream is empty, but this is not
     * mandated by the Servlet Spec.
     *
     * @return an enumeration containing all parameter names
     */
    Enumeration getParameterNames();

    /**
     * Gets an array of Strings containing all the request parameter's
     * values whose name matches <CODE>name</CODE>.
     *
     * @param name the parameter name
     * @return the array containing all the values or null if not present
     */
    String[] getParameterValues(String name);

    /**
     * Gets a Map of all the parameters contained within this request
     *
     * @since 2.3
     *
     * @return java.util.Map containing key value pairs of the request parameters,
     * where the key is a String containing the parameter name, and the
     * value is an array of Strings containing the parameter values
     */

    Map getParameterMap();

    /**
     * Gets the protocol of the request as Proto/Major.Minor
     * ("HTTP/1.1").
     *
     * @return A string containing the protocol name
     */
    String getProtocol();

    /**
     * Gets the scheme of the request as defined by RFC 1783
     * ("ftp", "http", "gopher", "news").
     *
     * @return A String containing the scheme
     */
    String getScheme();

    /**
     * Get the name of the server receiving the request
     *
     * @return The name of the server.
     */
    String getServerName();

    /**
     * Gets the portnumber the server reveiving the request is running on.
     *
     * @return the portnumber
     */
    int getServerPort();

    /**
     * Creates an BufferedReader for servlets to read client request
     * data from.
     * @see javax.servlet.ServletRequest#getInputStream()
     *
     * @since 2.0
     *
     * @return The created BufferedReader
     * @exception IOException if an i/o related error occured
     * @exception IllegalStateException if <code>getInputStream</code> was
     * already called on this request.
     * @exception java.io.UnsupportedEncodingException if the character
     * encoding cannot be decoded.
     */
    BufferedReader getReader()
        throws IOException;

    /**
     * Gets the ip address of the client that sent the request
     *
     * @return the client's ip address
     */
    String getRemoteAddr();

    /**
     * Gets the hostname of the client that sent the request.
     * This is either a fully qualified host name or a string representing
     * the remote IP address.
     *
     * @return the client's hostname
     */
    String getRemoteHost();

    /**
     * Puts a named object into the <code>ServletRequest</code>.
     * Can be used to communicate with other servlets if this
     * <code>ServletRequest</code> is passed to another servlet through a
     * <code>RequestDispatcher</code>.
     * The names used must follow the conventions used for naming java
     * packages.
     *
     * @since 2.1
     *
     * @see javax.servlet.ServletRequest#getAttribute(java.lang.String)
     * @see javax.servlet.RequestDispatcher
     *
     * @param name - which is used to refer to this object
     * @param object - which should be returned when somebody calls
     * <code>getAttribute(name)</code>
     */
    void setAttribute(String name, Object o);

    /**
     * remove the specified attribute.
     *
     * @param name the name of the attribute to be removed
     *
     * @since 2.1
     */
    void removeAttribute(String name);

    /**
     * get the locale associated with the request.
     *
     * @return the international locale of the request
     *
     * @since 2.2
     */
    Locale getLocale();

    /**
     * get all the locales associated with the request.
     *
     * @since 2.2
     */
    Enumeration getLocales();

    /**
     * has the request arrived under secure conditions?
     * A secure condition might be an encrypted channel, for
     * example SSL.
     *
     * @since 2.2
     */
    boolean isSecure();

    /**
     * get a means of dispatching to the specified path.
     * Relative path resolution is performed.
     *
     * @param path if relative it will be resolved
     * @return the means if dispatching to the path
     *
     * @since 2.2
     */
    RequestDispatcher getRequestDispatcher(String path);

    /**
     * Translates the given path to the real path on the servers
     * filesystem, using the servers documentroot.
     *
     * @deprecated Should use getRealPath from the current ServletContext.
     * @see javax.servlet.ServletContext#getRealPath(java.lang.String)
     *
     * @param path the path which requires translating
     * @return the translated path
     */
    String getRealPath(String path);

    /**
     * Returns the IP source port of the client that sent the request.
     * @since 2.4
     */
    int getRemotePort();

    /**
     * Returns the DNS hostname of the IP interface on which the request was
     * received.
     * @since 2.4
     */
    String getLocalName()
        throws IOException;

    /**
     * Returns the IP address of the interface on which the request was
     * received.
     * @since 2.4
     */
    String getLocalAddr()
        throws IOException;

    /**
     * Returns the port number of the interface on which the request was
     * received.
     * @since 2.4
     */
    int getLocalPort()
        throws IOException;

    /**
     * Returns the context to which this request was last dispatched.
     * @since 3.0
     */
    ServletContext getServletContext();

    /**
     * Put this request into asynchronous mode.
     * The associated response will not be committed until
     * {@link javax.servlet.AsyncContext#complete()} has been called.
     * @return the context for the asynchronous operation
     * @since 3.0
     */
    AsyncContext startAsync()
        throws IllegalStateException;

    /**
     * Put this request into asynchronous mode using the specified
     * request/response pair.
     * The associated response will not be committed until
     * {@link javax.servlet.AsyncContext#complete()} has been called.
     * @return the context for the asynchronous operation
     * @since 3.0
     */
    AsyncContext startAsync(ServletRequest request, ServletResponse response)
        throws IllegalStateException;

    /**
     * Indicates whether this request has been put into asynchronous mode.
     * @since 3.0
     */
    boolean isAsyncStarted();

    /**
     * Indicates whether asynchronous mode is supported for this request.
     * @since 3.0
     */
    boolean isAsyncSupported();

    /**
     * Returns the current context for asynchronous operation.
     * @exception IllegalStateException if this request has not been put into
     * asynchronous mode.
     * @since 3.0
     */
    AsyncContext getAsyncContext();

    /**
     * Returns the dispatcher type of this request.
     * @since 3.0
     */
    DispatcherType getDispatcherType();

}
