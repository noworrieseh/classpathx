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

/**
 * The execution context of an asynchronous operation on a servlet request.
 * The context is created by calling
 * {@link javax.servlet.ServletRequest#startAsync()} or
 * {@link javax.servlet.ServletRequest#startAsync(javax.servlet.ServletRequest,javax.servlet.ServletResponse)}.
 * Subsequent call to these methods return the same context instance.
 *
 * @version 3.0
 * @since 3.0
 * @author Chris Burdess
 */
public interface AsyncContext
{

    /**
     * Name of the attribute for the request URI.
     * @see #dispatch(java.lang.String)
     * @see #dispatch(javax.servlet.ServletContext,java.lang.String)
     */
    static final String ASYNC_REQUEST_URI = "javax.servlet.async.request_uri";

    /**
     * Name of the attribute for the context path.
     * @see #dispatch(java.lang.String)
     * @see #dispatch(javax.servlet.ServletContext,java.lang.String)
     */
    static final String ASYNC_CONTEXT_PATH = "javax.servlet.async.context_path";

    /**
     * Name of the attribute for the path info.
     * @see #dispatch(java.lang.String)
     * @see #dispatch(javax.servlet.ServletContext,java.lang.String)
     */
    static final String ASYNC_PATH_INFO = "javax.servlet.async.path_info";

    /**
     * Name of the attribute for the servlet path.
     * @see #dispatch(java.lang.String)
     * @see #dispatch(javax.servlet.ServletContext,java.lang.String)
     */
    static final String ASYNC_SERVLET_PATH = "javax.servlet.async.servlet_path";

    /**
     * Name of the attribute for the query string.
     * @see #dispatch(java.lang.String)
     * @see #dispatch(javax.servlet.ServletContext,java.lang.String)
     */
    static final String ASYNC_QUERY_STRING = "javax.servlet.async.query_string";

    /**
     * Returns the original request.
     */
    ServletRequest getRequest();

    /**
     * Returns the original response.
     */
    ServletResponse getResponse();

    /**
     * Indicates whether this context was initialized with the original
     * request/response pair.
     */
    boolean hasOriginalRequestAndResponse();

    /**
     * Dispatches the request/response pair to the servlet container.
     * If {@link javax.servlet.ServletRequest#startAsync(javax.servlet.ServletRequest,javax.servlet.ServletResponse)}
     * was called with a {@link javax.servlet.http.HttpServletRequest}, then
     * the URI for dispatch will be its request-URI, otherwise the URI will
     * be that of the request when it was last dispatched.
     * The dispatcher type of the request is
     * {@link javax.servlet.DispatcherType#ASYNC}.
     * This method returns immediately.
     * @see javax.servlet.ServletRequest#getDispatcherType()
     * @exception IllegalStateException if {@link #complete()} was called,
     * or a dispatch method was called but the startAsync method was not
     * called
     */
    void dispatch();

    /**
     * Dispatches the request/response pair to the specified path.
     * The dispatcher type of the request is
     * {@link javax.servlet.DispatcherType#ASYNC}.
     * This method returns immediately.
     * @see javax.servlet.ServletRequest#getDispatcherType()
     * @exception IllegalStateException if {@link #complete()} was called,
     * or a dispatch method was called but the startAsync method was not
     * called
     * @param path the dispatch target within the servlet context of this
     * async context
     */
    void dispatch(String path);

    /**
     * Dispatches the request/response pair to the specified path and
     * context.
     * The dispatcher type of the request is
     * {@link javax.servlet.DispatcherType#ASYNC}.
     * This method returns immediately.
     * @see javax.servlet.ServletRequest#getDispatcherType()
     * @exception IllegalStateException if {@link #complete()} was called,
     * or a dispatch method was called but the startAsync method was not
     * called
     * @param path the dispatch target within the specified servlet context
     * @param context the servlet context
     */
    void dispatch(ServletContext context, String path);

    /**
     * Completes this asynchronous operation.
     * The associated response is closed.
     */
    void complete();

    /**
     * Requests the servlet container to execute the specified runnable.
     */
    void start(Runnable runnable);

    /**
     * Adds a listener to receive
     * {@link javax.servlet.AsyncEvent}s for this context.
     */
    void addListener(AsyncListener listener);

    /**
     * Adds a listener to receive
     * {@link javax.servlet.AsyncEvent}s for this context.
     * The specified request and response will be passed in the event.
     */
    void addListener(AsyncListener listener, ServletRequest request,
            ServletResponse response);

    /**
     * Instantiates an
     * {@link javax.servlet.AsyncListener} from the given class.
     */
    <T extends AsyncListener> T createListener(java.lang.Class<T> t)
        throws ServletException;

    /**
     * Sets the timeout in milliseconds for this context.
     */
    void setTimeout(long timeout);

    /**
     * Returns the timeout in milliseconds for this context.
     */
    long getTimeout();

}
