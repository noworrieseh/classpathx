/*
 * ErrorData.java -- XXX
 *
 * Copyright (c) 2003 by Free Software Foundation, Inc.
 * Written by Arnaud Vandyck (arnaud.vandyck@ulg.ac.be)
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Library General Public License as published
 * by the Free Software Foundation, version 2. (see COPYING.LIB)
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software Foundation
 * Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307 USA
 */

package javax.servlet.jsp;

/**
 * Informations for error pages.
 *
 * @author Arnaud Vandyck - arnaud.vandyck@ulg.ac.be
 * @version JSP 2.0
 * @since JSP 2.0
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
   * Get the RequestURI value.
   * @return the RequestURI value.
   */
  public String getRequestURI() {
    return requestURI;
  }

  /**
   * Get the ServletName value.
   * @return the ServletName value.
   */
  public String getServletName() {
    return servletName;
  }

  /**
   * Get the StatusCode value.
   * @return the StatusCode value.
   */
  public int getStatusCode() {
    return statusCode;
  }

  /**
   * Get the Throwable value.
   * @return the Throwable value.
   */
  public Throwable getThrowable() {
    return throwable;
  }

}
