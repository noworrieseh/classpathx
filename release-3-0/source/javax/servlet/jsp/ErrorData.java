/*
 * Copyright (C) 2003, 2013 Free Software Foundation, Inc.
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
package javax.servlet.jsp;

/**
 * Informations for error pages.
 *
 * @author Arnaud Vandyck - arnaud.vandyck@ulg.ac.be
 * @version 2.1
 * @since 2.0
 * @see PageContext#getErrorData()
 */
public final class ErrorData 
{

    private String requestURI = null;
    private String servletName = null;
    private int statusCode = -1;
    private Throwable throwable = null;

    /**
     * Constructor.
     * @param throwable
     * @param statusCode
     * @param uri
     * @param servletName
     */
    public ErrorData(Throwable throwable, int statusCode,
            String uri, String servletName)
    {
        this.throwable = throwable;
        this.statusCode = statusCode;
        this.requestURI = uri;
        this.servletName = servletName;
    }

    /**
     * Returns the request URI.
     */
    public String getRequestURI() {
        return requestURI;
    }

    /**
     * Returns the servlet name.
     */
    public String getServletName() {
        return servletName;
    }

    /**
     * Returns the status code of the error.
     */
    public int getStatusCode() {
        return statusCode;
    }

    /**
     * Returns the Throwable that caused the error.
     */
    public Throwable getThrowable() {
        return throwable;
    }

}
