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

import java.lang.Exception;
import java.io.InputStream;
import java.net.URL;
import java.net.MalformedURLException;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Set;


/**
 * A class created by the server to give servlets access to certain
 * environment related objects and methods.<BR>
 * It contains standard information like the names of all the servlets,
 * two kinds of log methods, the server name, etc.<BR>
 * The server can also store extra information here in the form
 * of {String, Object} pairs.
 * Different servlets can live in different ServletContexts,
 * but a servlet engine can group related Servlets in the same
 * ServletContext.<BR>
 * Servlet specific information can be transferred to a servlet using
 * a class implementing the ServletConfig interface.<BR>
 *
 * @version Servlet API 2.2
 * @since Servlet API 1.0
 * @author Paul Siegmann (pauls@euronet.nl)
 * @author Charles Lowell (cowboyd@pobox.com)
 */
public interface ServletContext
{
  
  /**
   * Gets the value of a named attribute
   *
   * @since Servlet API 1.0
   *
   * @param name the name of the attribute
   * @return the value of the attribute or null if there is no attribute with
   * this name
   */
  Object getAttribute (String name);


  /**
   * Gets an enumeration containing all the attribute names
   *
   * @since Servlet API 2.1
   *
   * @return The enumeration containing all the attribute names
   */
  Enumeration getAttributeNames ();


  /**
   * Gives the <code>ServletContext</code> of another servlet indicated by
   * the <code>UriPath</code> on the same server. For security reasons this
   * can return null even if there is an active servlet at that location.
   * This can then be used to set attributes in the context of another
   * servlet.
   * @see javax.servlet.ServletContext#setAttribute(java.lang.String, java.lang.Object)
   *
   * @since Servlet API 2.1
   *
   * @param UriPath The path to the servlet,
   * such as <code>/servlet/ShowBook</code>
   * @return ServletContext of the requested servlet or null if there is no
   * servlet at the requested <code>UriPath</code> or when it is unavailable
   * due to security restrictions
   */
  ServletContext getContext (String UriPath);


  /**
   * Major version number of the Servlet API the servlet engine supports.
   *
   * @since Servlet API 2.1
   *
   * @return 2 if the 2.1 Servlet API is supported
   */
  int getMajorVersion ();

  /**
   * Minor version number of the Servlet API the servlet engine supports.
   *
   * @since Servlet API 2.1
   *
   * @return 1 if the 2.1 Servlet API is supported
   */
  int getMinorVersion ();


  /**
   * Gives the mimetype of the requested file
   *
   * @since Servlet API 1.0
   *
   * @param filename the file
   * @return a String containing the mime type
   * or null if the mime type cannot be determined
   */
  String getMimeType(String filename);
	

  /**
   * Translates the requested virtual path to the real filesystem path
   * using the servers knowledge of the document root.
   * Use the <code>getResource</code> and <code>getResourceAsStream</code>
   * methods to access the original object in a more abstract way not tied to
   * the filesystem.
   * @see javax.servlet.ServletContext#getResource(java.lang.String)
   * @see javax.servlet.ServletContext#getResourceAsStream(java.lang.String)
   *
   * @since Servlet API 1.0
   *
   * @param virtualPath the path to be translated
   * (e.g. <code>/graphics/baby-gnu.png</code>)
   * @return the translated real filesystem path
   * or null if the real system path cannot be found
   */
  String getRealPath(String virtualPath);

  /**
   * Returns a listing of all the resources that are accessible inside 
   * this web application, whose paths start with the path provided
   * specified. This path must start with a "/", and all paths in the
   * resulting set will be relative to the root of the web application
   * So a call of <code>getResourcePaths ("/images/jpegs/")</code>
   * might yield the set {"/images/jpegs/a.jpg","/images/jpegs/b.jpg,..."}
   * 
   * @since Servlet API 2.3
   *
   * @param pathPrefix The path starting with "/" used to match against the resources
   *
   * @return a Set containing the paths of the matching resources
   * or null if no resources with the specified prefix can be found 
   */
  Set getResourcePaths (String pathPrefix);
 

  /**
   * Translates the requested virtual path to an URL object that can be
   * accesed by the servlet. This is more generic than the
   * <code>getRealPath</code> method since it is not tied to the local
   * filesystem. This means that a servlet can access the resource even when
   * loaded in a servlet engine on a different machine. The servlet engine
   * should make sure that the appropriate <code>URLStreamHandlers</code> and
   * <code>URLConnection</code> classes are implemented to acces to resource.
   * <p>
   * This can also be used to write to a resource if the resource
   * (<code>URLConnection</code>) supports it. The following example gives
   * you an <code>OutputStream</code>:<br>
   * <p>
   * <code>
   * URLConnection con = getResource("/logs/mylog.txt").openConnection();<br>
   * con.setDoOutput(true);<br>
   * OutputStream out = con.getOutputStream();<br>
   * </code>
   * <p>
   * Note that a <code>ServerContext</code> does not have to have access to
   * the complete servers document space and is allowed to return null even
   * for valid virtual paths.
   * <p>
   * Note that according to the 2.1 API documentation this method can throw
   * a MalformedURLException. But according to the official spec it does not
   * throw any exceptions.
   *
   * @since Servlet API 2.1
   *
   * @see java.net.URL#openConnection()
   * @see java.net.URLConnection#setDoOutput(boolean)
   * @see java.net.URLConnection#getOutputStream()
   *
   * @param virtualPath the path to the requested resource
   * (e.g. <code>/philosophy/words-to-avoid.html</code>)
   * @return the URL that can be used to access the resource
   * or null if the resource cannot be found
   */
  URL getResource(String virtualPath)
  throws MalformedURLException;

      

  /**
   * A convenience method for
   * <code>getResource(virtualPath).openStream()</code>.
   * But the servlet engine is allowed to implement is in a more efficient
   * way.
   *
   * @see javax.servlet.ServletContext#getResource(java.lang.String)
   * @see java.net.URL#openStream()
   *
   * @since Servlet API 2.1
   *
   * @param virtualPath the path to the requested resource
   * (e.g. <code>/philosophy/words-to-avoid.html</code>)
   * @return the InputStream that can be used to read the resource
   * or null if the resource cannot be found
   */
  InputStream getResourceAsStream(String virtualPath);




  /**
   * Returns a <code>RequestDispatcher</code> to forward requests or
   * include responses from another (active) resource. Some resources can also
   * be accessed by the <code>getResource</code> method.
   *
   * @see javax.servlet.ServletContext#getResource(java.lang.String)
   *
   * @since Servlet API 2.1
   *
   * @param UriPath the path to another (active) resource
   * (e.g. <code>/servlet/OtherServlet</code>)
   * @return an <code>RequestDispatcher</code> for the (active) resource
   * found at <code>UriPath</code>
   */
  RequestDispatcher getRequestDispatcher (String UriPath);


  /**
   * get a dispatcher which wraps the servlet specified by name.
   * Servlet names can be set by the class or within the webapp
   * deployment descriptor or by some other implementation dependant
   * system.
   *
   * @param name the name of the servlet to wrap
   * @return the means of dispatching to the servlet
   *
   * @since Servlet API 2.2
   */
  RequestDispatcher getNamedDispatcher(String name);


  /**
   * A server supplied string containing the server name, version number, etc
   *
   * @since Servlet API 1.0
   *
   * @return the string
   */
  String getServerInfo ();


  /**
   * get the application initialization parameter associated with the name.
   * The application init parameters are set in the webapp deployment
   * descriptor or in some other implementation defined way.
   *
   * @param name the name of the init parameter
   * @return value the value of the init parameter
   *
   * @since Servlet API 2.2
   */
  String getInitParameter(String name);

  /**
   * get a list of the init parameter names.
   *
   * @return the list of the init parameter names as defined in the webapp DD.
   *
   * @since Servlet API 2.2
   */
  Enumeration getInitParameterNames();


  /**
   * Writes a message to the log
   *
   * @since Servlet API 1.0
   *
   * @param message the message to write
   */
  void log (String message);


  /**
   * Writes an exception + message to the log
   *
   * @since Servlet API 2.1
   *
   * @param t the exception
   * @param message the message
   */
  void log (String message, Throwable t);


  /**
   * Writes an exception + message to the log
   *
   * @deprecated Use <code>log(String, Throwable)</code> which is more
   * general.
   *
   * @see javax.servlet.ServletContext#log(java.lang.String, java.lang.Throwable)
   * @since Servlet API 2.0
   *
   * @param exception the exception
   * @param message the message
   */
  void log (Exception exception, String message);


  /**
   * Puts a named object into the <code>ServletContext</code>.
   * Can be used to communicate with other servlets in this
   * <code>ServletContext</code>. The names used must follow the conventions
   * used for naming java packages.
   *
   * @since Servlet API 2.1
   *
   * @param name - which is used to refer to this object
   * @param object - which should be returned when somebody calls
   * <code>getAttribute(name)</code>
   * @see javax.servlet.ServletContext#getAttribute(java.lang.String)
   */
  void setAttribute(String name, Object o);


  /**
   * Removes the named object from the <code>ServletContext</code>
   *
   * @since Servlet API 2.1
   *
   * @param name The name which was used to set the object with
   * <code>setObject</code>
   */
  void removeAttribute(String name);


  /**
   * Gets a specific servlet by name. The Servlet is guaranteed to accept
   * service requests.
   *
   * @param name the name of the wanted servlet
   * @return null, used to return the servlet or null if not loaded.
   * @exception ServletException if a servlet related error occured
   *
   * @since Servlet API 1.0
   * @deprecated as of Servlet API 2.1, use {@link RequestDispatcher}s
   *   instead; this now always returns <code>null</code>
   */
  Servlet getServlet(String name)
  throws ServletException;


  /**
   * Gets all servlets
   *
   *
   * @return Empty Enumeration, used to return an enumeration containing all
   * loaded servlets including the calling servlet.
   *
   * @since Servlet API 1.0
   * @deprecated as of Servlet API 2.1, there is no equivalent now.
   *  this Always returns an empty <code>Enumeration</code>.
   */
  Enumeration getServlets();


  /**
   * Gets all servlet names
   *
   * @return Empty Enumeration, used to return an enumeration containing all
   * loaded servlet names including the calling servlet name
   *
   * @since Servlet API 2.0
   * @deprecated since Servlet API 2.1, use a {@link RequestDispatcher}
   *   instead; now this always returns an empty <code>Enumeration</code>.
   */
  Enumeration getServletNames();

  /**
   * Get the Servlet Context Name
   *
   * @return Name of the web-app or null if the context has no name.
   * 
   * @since Servlet API 2.3
   */
  String getServletContextName();
}
