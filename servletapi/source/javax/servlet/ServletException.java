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

/**
 * This exception is thrown by a servlet when a servlet related problem occurs.
 * 
 * @version 3.0
 * @since 1.0
 * @author Paul Siegmann (pauls@euronet.nl)
 */
public class ServletException
    extends Exception 
{

    /**
     * Creates a new ServletException.
     *
     * @since 2.0
     */
    public ServletException() 
    {
        super();
    }


    /**
     * Creates a new ServletException with a message.
     *
     * @param message why this exception occured
     */
    public ServletException(String message) 
    {
        super(message);
    }

    /**
     * Creates a new ServletException with a message
     * and what caused the exception.
     *
     * @since 2.1
     *
     * @param message why this exception occured
     * @param cause what made this exception occur
     */
    public ServletException(String message, Throwable cause) 
    {
        super(message);
        initCause(cause);
    }

    /**
     * Creates a new ServletException with what caused the exception.
     *
     * @since 2.1
     *
     * @param message why this exception occured
     * @param cause what made this exception occur
     */
    public ServletException(Throwable cause) 
    {
        super(cause.getLocalizedMessage());
        initCause(cause);
    }


    /**
     * Gives the Throwable that caused this exception if known, otherwise null.
     *
     * @since 2.1
     *
     * @return Throwable that caused this exception
     */
    public Throwable getRootCause() 
    {
        return getCause();
    }

}
