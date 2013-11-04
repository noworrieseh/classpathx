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

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Enumeration;

/**
 * Whenever the server receives a request it creates a ServletRequest object,
 * puts all the request information in it and passes this along with
 * a ServletResponse object to the approriate servlet.
 *
 * @version Servlet API 2.2
 * @since Servlet API 1.0
 * @author Paul Siegmann (pauls@euronet.nl)
 */
public interface ServletRequest
{

  /**
   * Gets the size in bytes of the request
   *
   * @since Servlet API 1.0
   *
   * @return the number of bytes in the request
   * or -1 if not known
   */
  int getContentLength();

  /**
   * Gets the mime type of the request
   *
   * @since Servlet API 1.0
   *
   * @return a String containing the mime type of the request
   * or null if not known
   */
  String getContentType();

  /**
   * Gets the protocol of the request as Proto/Major.Minor
   * ("HTTP/1.1").
   *
   * @since Servlet API 1.0
   *
   * @return A string containing the protocol name
   */
  String getProtocol();


  /**
   * Gets the scheme of the request as defined by RFC 1783
   * ("ftp", "http", "gopher", "news").
   *
   * @since Servlet API 1.0
   *
   * @return A String containing the scheme
   */
  String getScheme();

  /**
   * Get the name of the server receiving the request
   *
   * @since Servlet API 1.0
   *
   * @return The name of the server.
   */
  String getServerName();

  /**
   * Gets the portnumber the server reveiving the request is running on.
   *
   * @since Servlet API 1.0
   *
   * @return the portnumber
   */
  int getServerPort();

  /**
   * Gets the ip address of the client that sent the request
   *
   * @since Servlet API 1.0
   *
   * @return the client's ip address
   */
  String getRemoteAddr();

  /**
   * Gets the hostname of the client that sent the request.
   * This is either a fully qualified host name or a string representing
   * the remote IP address.
   *
   * @since Servlet API 1.0
   *
   * @return the client's hostname
   */
  String getRemoteHost();


  /**
   * Translates the given path to the real path on the servers
   * filesystem, using the servers documentroot.
   *
   * @deprecated Should use getRealPath from the current ServletContext.
   * @see javax.servlet.ServletContext#getRealPath(java.lang.String)
   *
   * @since Servlet API 1.0
   *
   * @param path the path which requires translating
   * @return the translated path
   */
  String getRealPath(String path);

  /**
   * Creates an inputstream for servlets to read client request data from.
   * @see javax.servlet.ServletRequest#getReader()
   *
   * @since Servlet API 1.0
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
   * @since Servlet API 1.0
   *
   * @param name the name of the parameter whose value we want
   * @return the (first) value of the parameter or null if not present
   */
  String getParameter(String name);

  /**
   * Gets an array of Strings containing all the request parameter's
   * values whose name matches <CODE>name</CODE>.
   *
   * @since Servlet API 1.0
   *
   * @return the array containing all the values or null if not present
   */
  String[] getParameterValues(String name);

  /**
   * Gets all parameter names.
   * <p>
   * Note that the Servlet API 2.1 documentation says that this returns
   * an empty Enumeration if the input stream is empty, but this is not
   * mandated by the Servlet Spec.
   *
   * @since Servlet API 1.0
   *
   * @return an enumeration containing all parameter names
   */
  Enumeration getParameterNames();


  /**
   * Gets a named attribute's value.
   * This gives one of the initialization attribute values.
   * <p>
   * Note that the Servlet 2.1 API Documentation mentions some predefined
   * attribute names, but the Servlet Spec does not mention them.
   * I (MJW) am not sure if they are platform specific (JWS) or not.
   *
   * @since Servlet API 1.0
   *
   * @return The value of the attribute, null if not found.
   */
  Object getAttribute(String name);


  /**
   * Puts a named object into the <code>ServletRequest</code>.
   * Can be used to communicate with other servlets if this
   * <code>ServletRequest</code> is passed to another servlet through a
   * <code>RequestDispatcher</code>.
   * The names used must follow the conventions used for naming java
   * packages.
   *
   * @since Servlet API 2.1
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
   * Gets an Enumeration of all the attribute names.
   *
   * @since Servlet API 2.1
   *
   * @return The Enumeration of all attribute names set in this request.
   */
  Enumeration getAttributeNames();

	
  /**
   * remove the specified attribute.
   *
   * @param name the name of the attribute to be removed
   *
   * @since Servlet API 2.1
   */
  void removeAttribute(String name);


  /**
   * get the locale associated with the request.
   *
   * @return the international locale of the request
   *
   * @since Servlet API 2.2
   */
  java.util.Locale getLocale();


  /**
   * get all the locales associated with the request.
   *
   * @since Servlet API 2.2
   */
  Enumeration getLocales();

  /**
   * Creates an BufferedReader for servlets to read client request
   * data from.
   * @see javax.servlet.ServletRequest#getInputStream()
   *
   * @since Servlet API 2.0
   *
   * @return The created BufferedReader
   * @exception IOException if an i/o related error occured
   * @exception IllegalStateException if <code>getInputStream</code> was
   * already called on this request.
   */
  BufferedReader getReader() throws IOException;


  /**
   * Gets the character encoding of the request data.
   *
   * @since Servlet API 2.0
   *
   * @return Character encoding or null if the encoding is unavailable
   */
  String getCharacterEncoding();


  /**
   * has the request arrived under secure conditions?
   * A secure condition might be an encrypted channel, for
   * example SSL.
   *
   * @since Servlet API 2.2
   */
  boolean isSecure();


  /**
   * get a means of dispatching to the specified path.
   * Relative path resolution is performed.
   *
   * @param path if relative it will be resolved
   * @return the means if dispatching to the path
   *
   * @since Servlet API 2.2
   */
  RequestDispatcher getRequestDispatcher(String path);

}


