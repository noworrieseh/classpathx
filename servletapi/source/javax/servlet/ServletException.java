/*
  GNU-Classpath Extensions: Servlet API
  Copyright (C) 1998, 1999, 2001   Free Software Foundation, Inc.

  For more information on the classpathx please mail: classpathx-discuss@gnu.org

  This program is free software; you can redistribute it and/or
  modify it under the terms of the GNU Lesser General Public License
  as published by the Free Software Foundation; either version 2
  of the License, or (at your option) any later version.

  This program is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  GNU General Public License for more details.

  You should have received a copy of the GNU Lesser General Public License
  along with this program; if not, write to the Free Software
  Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
*/
package javax.servlet;


/**
 * This exception is thrown by a servlet when a servlet related problem occurs.
 * 
 * @version Servlet API 2.4
 * @since Servlet API 1.0
 * @author Paul Siegmann (pauls@euronet.nl)
 */
public class ServletException
  extends Exception 
{

  /**
   * Creates a new ServletException.
   *
   * @since Servlet API 2.0
   */
  public ServletException() 
  {
    super();
  }


  /**
   * Creates a new ServletException with a message.
   *
   * @since Servlet API 1.0
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
   * @since Servlet API 2.1
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
   * @since Servlet API 2.1
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
   * @since Servlet API 2.1
   *
   * @return Throwable that caused this exception
   */
  public Throwable getRootCause() 
  {
    return getCause();
  }

}

