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
package javax.servlet.http;

import java.security.Principal;
import java.util.Enumeration;
import javax.servlet.ServletRequest;

/**
 * Contains all the client's request information.
 * <B>Implementation note:</B> all the headername matching in this class should be case<B>in</B>sensitive.
 *
 * @version Servlet API 2.4
 * @since Servlet API 1.0
 * @author Charles Lowell (cowboyd@pobox.com)
 */
public interface HttpServletRequest
  extends ServletRequest
{

  /**
   * Identifier for a request using basic authentication.
   * Value is "BASIC"
   */
  
  public static final String BASIC_AUTH = "BASIC";

  /**
   * Identifier for a request using form authentication.
   * Value is "FORM"
   */

  public static final String FORM_AUTH = "FORM";

  /**
   * Indentifier for a request using client certificate authentication
   * value is "CLIENT_CERT"
   */
  
  public static final String CLIENT_CERT_AUTH = "CLIENT_CERT";

  /**
   * Indentifier for a request using digest authentication
   * value is "DIGEST"
   */

  public static final String DIGEST_AUTH = "DIGEST";

  /**
   * Gets the authorization scheme of this request.
   * This is the same as the CGI request metadata <code>AUTH_TYPE</code>.
   * See also section 11 of the HTTP/1.1 specification (RFC 2068).
   *
   * @since Servlet API 1.0
   *
   * @return Authorization scheme or null if not set    
   */
  String getAuthType();


  /**
   * Gets all the Cookies present in the request.
   *
   * @since Servlet API 2.0
   *
   * @return an array containing all the Cookies or an empty array if there
   * are no cookies    
   */
  Cookie[] getCookies();


  /** 
   * Converts a given header parameter name to a date in the form of
   * the number of milliseconds since 1 january 1970 midnight GMT.
   * If the headername doesn't exist it returns -1;
   * If the header can not be converted to a date it throws
   * an IllegalArgumentException.
   *
   * @since Servlet API 1.0
   *
   * @param name the name of the header field (case insensitive)
   * @return milliseconds since January 1, 1970, 00:00:00 GMT or -1 if the
   * header does not exist.
   * @exception IllegalArgumentException if the value is not a date
   */
  long getDateHeader(String name);


  /**
   * Gets a named header.
   * returns null if the headername doesn't exist.
   *
   * @since Servlet API 1.0
   *
   * @param name the name of the header field (case insensitive)
   * @return The value of the header or null if the header does not exist
   */
  String getHeader(String name);


  /**
   * Returns the values of the specified header as an enumeration of String.
   *
   * @since Servlet API 2.2
   */
  Enumeration getHeaders(String name);


  /**
   * Gets an Enumeration with all the headernames.
   * Note that the Servlet API 2.1 Specification says that if an
   * implementation does not support this operation an empty enumeration
   * should be returned, but the Servlet API documentation says that the
   * implementation will return null.
   *
   * @since Servlet API 1.0
   *
   * @return Enumeration of all the header names or when this operation is
   * not supported an empty Enumeration or null.
   */
  Enumeration getHeaderNames();


  /**
   * Gets a named header and returns it in the shape of an int.
   * returns -1 if the headername doesn't exist.<BR>
   * [MJW] What if the value is -1?
   *
   * @since Servlet API 1.0
   *
   * @param name the name of the header field (case insensitive)
   * @return the value of the header field or -1 if the header does not exist
   * @exception NumberFormatException if the headervalue can't be converted
   * to an int.
   */
  int getIntHeader(String name);


  /**
   * Gets the method the client used.
   * This is the same as the CGI request metadata <code>REQUEST_METHOD</code>.
   * Possible return values are "GET", "HEAD", "POST", "PUT", "DELETE",
   * "OPTIONS", "TRACE".
   *
   * @since Servlet API 1.0
   *
   * @return The method in question
   */
  String getMethod();


  /**
   * Extra path info. Everything after the actual Servlet except the query
   * data. This is the same as the CGI request metadata <code>PATH_INFO</code>
   * and identifies the source or sub-resource to be returned by the Servlet.
   * <P>
   * The function of this method could best be explained using an example.
   * Client requests: www.foo_bar.com/servlets/myServlet/more/path?id=paul
   * (where myServlet is a servlet)<BR>
   * In this case this method would return "/more/path".
   * <P>
   * [MJW] Note that the Servlet 2.1 Spec says that the path info must be URL
   * decoded although this was not required before 2.1 and I am not sure if
   * that is the behaviour of the CGI request metadata <code>PATH_INFO</code>.
   *
   * @since Servlet API 1.0
   *
   * @return The path info or null when there is no path information.
   */
  String getPathInfo();


  /**
   * The filesystem path to the path info.
   * Does the same as getPathInfo, but translates the result to a real path.
   * This is the same as the CGI request metadata
   * <code>PATH_TRANSLATED</code>.
   * <P>
   * [MJW] Can this be different from calling
   * <code>ServletContext.getRealPath()</code> on the urldecoded result of
   * <code>getPathInfo()</code>?
   *
   * @since Servlet API 1.0
   *
   * @return The filesystem path to the file indicated by the path info
   *      or null if there is no path info
   */
  String getPathTranslated();


  /**
   * Returns the part of the request path used to identify the servlet context.
   *
   * @since Servlet API 2.2
   */
  String getContextPath();


  /**
   * Gets the request's query string.
   * The query string is the part of the request that follows the '?'.<BR>
   * This is the same as the CGI request metadata <code>QUERY_STRING</code>.
   *
   * @since Servlet API 1.0
   *
   * @return the query string or null if there is no such part
   */
  String getQueryString();


  /**
   * Gets the username of the person sending the request.
   * This is the same as the CGI request metadata <code>REMOTE_USER</code>.
   *
   * @since Servlet API 1.0
   *
   * @return User name
   *      or null if the username wasn't in the HTTP authentication.
   */
  String getRemoteUser();



  /**
   * is the user in the specified HTTP role?
   *
   * @since Servlet API 2.2
   */
  boolean isUserInRole(String role);

  /**
   * get the principal associated with the user tied to this request.
   *
   * @since Servlet API 2.2
   */
  Principal getUserPrincipal();


  /**
   * Gets the session Id of this request that the client wanted.
   * This id can differ from the id in the current session if the client
   * recently had gotten a new session id for whatver reason.
   *
   * @since Servlet API 2.0
   *
   * @return The requested session id
   */
  String getRequestedSessionId();


  /**
   * Gets the requested URI.
   * This includes both the path to the servlet and everything after
   * that except the '?' and the query_string.
   * <P>
   * Note that the Servlet 2.1 Spec says that the URI must be decoded before
   * being returned, but this was not required before the Servlet 2.1 API and
   * normally all URIs are encoded.
   *
   * @since Servlet API 1.0
   *
   * @return The requested URI
   */
  String getRequestURI();


  /**
   * Contains the URL that the client used to make the request without 
   * the query string. This includes protocol,server,port, and path 
   * information.
   *
   * @since Servlet API 2.3
   *
   * @return a StringBuffer containing the request URL.
   * this Stringbuffer can be easily appended to, to add the query
   * string if needed.
   */
  StringBuffer getRequestURL();

  /**
   * Gets the part of the URI up to and including the servlet name.
   * No path info or query string segments are included.
   * This is the same as the CGI request metadata <code>SCRIPT_NAME</code>.
   *
   * @since Servlet API 1.0
   */
  String getServletPath();


  /**
   * Gets the HttpSession connected with the client sending the request.
   * If the client didn't have a session connected with him
   * then a new HttpSession will be created. To maintain a session this
   * method must be called before the connection is flushed or closed.
   * Same as calling <code>getSession(true)</code>.
   *
   * @since Servlet API 2.1
   *
   * @return The HttpSession connected with the client sending the request.
   */
  HttpSession getSession();


  /**
   * Gets the HttpSession connected with the client sending the request.
   * If the client didn't have a session connected with him,
   * and <CODE>create</CODE> is true then a new HttpSession will be
   * created. If <CODE>create</CODE> is false then <CODE>null</CODE>
   * is returned. To maintain a session this
   * method must be called before the connection is flushed or closed.
   *
   * @since Servlet API 2.0
   *
   * @return The HttpSession connected with the client sending the request.
   */
  HttpSession getSession(boolean create);


  /**
   * is the session connected with the id in the request valid?
   * A session id is valid if it came in with the current request and
   * the session associated with it exists and is valid.
   *
   * <p>Note: there is no other way to <em>test</em> session validity
   * than using this method.
   * </p>
   *
   * @since Servlet API 2.0
   */
  boolean isRequestedSessionIdValid();


  /**
   * was the session id in the request passed via a Cookie.
   *
   * @since Servlet API 2.0
   */
  boolean isRequestedSessionIdFromCookie();


  /**
   * was the session id in the request passed via encoding of the request URI.
   * @since Servlet API 2.1
   */
  boolean isRequestedSessionIdFromURL();


  /**
   * Returns whether the session id in the request was encoded in the request URI.
   * @deprecated Use {@link #isRequestedSessionIdFromURL}
   * @since Servlet API 2.0
   */
  boolean isRequestedSessionIdFromUrl();

}

