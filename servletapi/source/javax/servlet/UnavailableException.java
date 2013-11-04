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
 * This is a special kind of exception telling the server that this particular
 * servlet is currently not available.
 * It has two kinds of unavailability:
 * <DL>
 * <DT>Permanent unavailable
 * <DD>This servlet is now and forever in the future unavailable.
 * If another class in spite of this fact asks this exception for how long it 
 * is unavailable it returns a negative number of seconds.
 * (-1 as a matter of fact)
 * <DT>Temporary unavailable
 * The servlet is currently unavailable, but will be available within
 * a certain number of seconds.
 * A class can ask the exception for that number of seconds.
 * </DL>
 *
 * @version 3.0
 * @since 1.0
 */
public class UnavailableException
    extends ServletException 
{

    private Servlet servlet;
    private int seconds;

    /**
     * Constructor for a permanent unavailable exception
     *
     * @since 2.2
     */
    public UnavailableException(String message) 
    {
        this(null, message);
    }


    /**
     * Constructor for a temporary unavailable exception
     *
     * @since 2.2
     */
    public UnavailableException(String message, int seconds) 
    {
        this(seconds, null, message);
    }

    /**
     * Constructor for a permanent unavailable exception
     *
     * @deprecated use {@link UnavailableException(String)}.
     */
    public UnavailableException(Servlet servlet, String message) 
    {
        super(message);
        this.servlet = servlet;
    }


    /**
     * Constructor for a temporary unavailable exception
     * @deprecated use {@link UnavailableException(String)}.
     */
    public UnavailableException(int seconds, Servlet servlet, String message) 
    {
        super(message);
        this.servlet = servlet;
        if (seconds <= 0)
          {
            seconds = -1;
          }
        this.seconds = seconds;
    }


    /**
     * Check whether the servlet is permanently unavailable
     *
     * @return whether the servlet is permanently unavailable
     */
    public boolean isPermanent() 
    {
        return seconds == 0;
    }


    /**
     * Gets the servlet that is unavailable
     *
     * @deprecated no replacement
     */
    public Servlet getServlet() 
    {
        return servlet;
    }

    /**
     * Gets the number of seconds the servlet is unavailable
     *
     * @return the number of seconds. Negative if permanently unavailable
     */
    public int getUnavailableSeconds() 
    {
        return (seconds == 0) ? -1 : seconds;
    }

}

