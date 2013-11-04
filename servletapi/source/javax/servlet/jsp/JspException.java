/*
 * Copyright (C) 1999, 2013 Free Software Foundation, Inc.
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
 * A JSP specific exception.
 * @version 2.1
 * @author Nic Ferrier (nferrier@tapsellferrier.co.uk)
 * @author Chris Burdess
 */
public class JspException
    extends Exception
{

    /**
     * New exception with no message.
     */
    public JspException()
    {
    }

    /** 
     * New exception with the specified message.
     */
    public JspException(String message)
    {
        super(message);
    }

    /**
     * New exception with the specified message and cause.
     */
    public JspException(String message, Throwable cause)
    {
        super(message);
        initCause(cause);
    }

    /**
     * New exception with the specified cause.
     */
    public JspException(Throwable cause)
    {
        initCause(cause);
    }

    /**
     * @deprecated use {@link java.lang.Throwable#getCause}.
     */
    public Throwable getRootCause()
    {
        return getCause();
    }

}
