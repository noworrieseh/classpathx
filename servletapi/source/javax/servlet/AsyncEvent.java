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
 * Lifecycle event for asynchronous operations performed on a
 * {@link javax.servlet.ServletRequest}.
 * @version 3.0
 * @since 3.0
 * @author Chris Burdess
 */
public class AsyncEvent
{

    private AsyncContext context;
    private ServletRequest request;
    private ServletResponse response;
    private Throwable throwable;

    public AsyncEvent(AsyncContext context)
    {
        this(context, null, null, null);
    }

    public AsyncEvent(AsyncContext context, ServletRequest request, ServletResponse response)
    {
        this(context, request, response, null);
    }

    public AsyncEvent(AsyncContext context, Throwable throwable)
    {
        this(context, null, null, throwable);
    }

    public AsyncEvent(AsyncContext context, ServletRequest request, ServletResponse response, Throwable throwable)
    {
        this.context = context;
        this.request = request;
        this.response = response;
        this.throwable = throwable;
    }

    public AsyncContext getAsyncContext()
    {
        return context;
    }

    public ServletRequest getSuppliedRequest()
    {
        return request;
    }

    public ServletResponse getSuppliedResponse()
    {
        return response;
    }

    public Throwable getThrowable()
    {
        return throwable;
    }

}

