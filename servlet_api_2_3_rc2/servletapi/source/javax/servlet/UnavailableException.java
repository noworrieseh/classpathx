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
 * @version Servlet API 2.2
 * @since Servlet API 1.0
 */
public class UnavailableException
extends ServletException 
{

  private Servlet servlet;
  private int seconds;


  /**
   * Constructor for a permanent unavailable exception
   *
   * @since Servlet API 2.2
   */
  public UnavailableException(String message) 
  {
    super(message);
    seconds = -1;
  }


  /**
   * Constructor for a temporary unavailable exception
   *
   * @since Servlet API 2.2
   */
  public UnavailableException(String message, int seconds) 
  {
    super(message);
    this.seconds = seconds;
  }

  /**
   * Constructor for a permanent unavailable exception
   *
   * @deprecated use {@link UnavailableException(String)}.
   * @since Servlet API 1.0
   */
  public UnavailableException(Servlet servlet, String message) 
  {
    super(message);
    this.servlet = servlet;
    seconds = -1;
  }


  /**
   * Constructor for a temporary unavailable exception
   * @deprecated use {@link UnavailableException(String)}.
   *
   * @since Servlet API 1.0
   */
  public UnavailableException(int seconds, Servlet servlet, String message) 
  {
    super(message);
    this.servlet = servlet;
    this.seconds = seconds;
  }


  /**
   * Check whether the servlet is permanently unavailable
   *
   * @since Servlet API 1.0
   *
   * @return whether the servlet is permanently unavailable
   */
  public boolean isPermanent() 
  {
    return seconds < 0;
  }


  /**
   * Gets the servlet that is unavailable
   *
   * @deprecated no replacement
   * @since Servlet API 1.0
   */
  public Servlet getServlet() 
  {
    return servlet;
  }

  /**
   * Gets the number of seconds the servlet is unavailable
   *
   * @since Servlet API 1.0
   *
   * @return the number of seconds. Negative if permanently unavailable
   */
  public int getUnavailableSeconds() 
  {
    return seconds;
  }
}
