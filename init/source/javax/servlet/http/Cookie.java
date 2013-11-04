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


/**
 * A cookie is basically a {String,String} name/value pair that the server tells
 * the client to remember and to send back to him attached to every future
 * request.<BR>
 * Using cookies a server can maintain a state in between client requests.
 * <P>
 * A formal specification of Cookies can be found in RFC 2109
 * ("HTTP State Management Mechanism")
 *
 * @version Servlet API 2.2
 * @since Servlet API 2.0
 * @author Paul Siegmann (pauls@euronet.nl)
 */
public class Cookie
implements Cloneable 
{

  private String myName;
  private String myValue;
  private String myComment = null;
  private String myDomain = null;
  private int myMaxAge = -1;
  private String myPath = null;
  private boolean mySecure = false;
  private int myVersion = 0;

  // Valid HTTP/1.1 token characters
  private static String validChars  = "!#$%&'*+-.0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ"
  +"^_`abcdefghijklmnopqrstuvwxyz|~";

  /**
   * Creates a cookie with a name and a value.
   * The name must be a valid HTTP/1.1 token and not start with $.
   *
   * @since Servlet API 2.0
   *
   * @param name The name of the cookie
   * @param value The value of the cookie
   * @exception IllegalArgumentException if name is not a valid HTTP/1.1 token
   * or starts with $
   */
  public Cookie(String name, String value) throws IllegalArgumentException 
  {
    if(name.length() == 0) 
    throw new IllegalArgumentException("Empty names are not allowed");
    if(name.charAt(0) == '$') 
    throw new IllegalArgumentException("'$' not allowed as first char of Cookie name");
    //check the rest of the chars for validity
    for(int i = 0; i < name.length(); i++) 
    {
      if (validChars.indexOf(name.charAt(i)) == -1) 
      throw new IllegalArgumentException("Character '"+name.charAt(i)
					 +"' is not a valid HTTP/1.1 token");
    }
    myName = name;
    myValue = value;
  }

  /**
   * Gets the comment of the cookie
   *
   * @since Servlet API 2.0
   *
   * @return the comment or null if not defined
   */
  public String getComment() 
  {
    return myComment;
  }


  /**
   * Gets this cookie's domain
   *
   * @since Servlet API 2.0
   *
   * @return The domain for which this cookie will be used or null if not
   * defined
   */
  public String getDomain() 
  {
    return myDomain;
  }


  /**
   * Gets the time-to-live for this cookie, in seconds.<BR>
   * If it is 0 then the client will delete the cookie.<BR>
   * If it is -1 (which is the default) then the cookie will
   * be a non-persistent cookie.<BR>
   * This means that the cookie will live as long as the http
   * client lives, and will not be saved to disk.
   * 
   * @since Servlet API 2.0
   *
   * @return the number of seconds to live or -1
   */
  public int getMaxAge() 
  {
    return myMaxAge;
  }


  /**
   * Get the name
   *
   * @since Servlet API 2.0
   *
   * @return the Name
   */
  public String getName() 
  {
    return myName;
  }


  /**
   * Gets the path for which requests this cookie will be attached.
   * The domain/path pair determines with which requests the cookie
   * will be sent to the server.<BR>
   * Example:<BR>
   * When a client receives a Cookie on requesting "/products/" then
   * the path will be "/products/", and this Cookie will be attached
   * to every request for "/products/" and any of its subdirectories.
   *
   * @since Servlet API 2.0
   *
   * @return the path or null if not defined
   */
  public String getPath() 
  {
    return myPath;
  }


  /**
   * Whether only secure means (https) should be used when sending this
   * cookie to a server.
   *
   * @since Servlet API 2.0
   *
   * @return whether this cookie should be secure or not
   */
  public boolean getSecure() 
  {
    return mySecure;
  }


  /**
   * Gets the value
   *
   * @since Servlet API 2.0
   *
   * @return the Value
   */
  public String getValue() 
  {
    return myValue;
  }


  /**
   * Gets the version of this cookie.
   * The current type of cookies have version = 1, according to rfc2109.
   * There have been slightly different (netscape only) types of cookies,
   * but these days everyone uses version 1.
   * Fresh cookies however get a default version of 0, to improve
   * interoperability.
   *
   * @since Servlet API 2.0
   *
   * @return the version
   */
  public int getVersion() 
  {
    return myVersion;
  }


  /**
   * Sets the comment of the cookie.
   * Not supported by version 0 cookies.
   *
   * @since Servlet API 2.0
   *
   * @param comment the comment to be
   */
  public void setComment(String comment) 
  {
    myComment = comment;
  }



  /**
   * Sets the domain for which this Cookie will be used.
   * If the domain is for instance set to .foo_bar.com then the client
   * sends the cookie along with requests to all webservers whose domain
   * ends with ".foo_bar.com" (www.foo_bar.com, blah.foo_bar.com, etc).
   * If not set cookies are only returned to the domain from which the client
   * received the cookie.
   *
   * @since Servlet API 2.0
   *
   * @param domain The cookie's domain
   */
  public void setDomain(String domain) 
  {
    myDomain = domain;
  }


  /**
   * Sets the maximum lifetime of the cookie in seconds.<BR>
   * If set to 0 then the cookie will be deleted by the client.<BR>
   * If set to a negative value (such as -1 which is the default)
   * then the cookie will
   * be a non-persistent cookie.<BR>
   * This means that the cookie will live as long as the http
   * client lives, and will not be saved to disk.
   *
   * @since Servlet API 2.0
   *
   * @param maxAge The time-to-live for the cookie, in seconds
   */
  public void setMaxAge(int maxAge) 
  {
    myMaxAge = maxAge;
  }


  /**
   * Set the path with which requests this cookie will be sent back to
   * the server.
   * The domain/path pair determines with which requests the cookie
   * will be sent to the server.<BR>
   * Defaults to path the client requested when it got this cookie.<BR>
   * Example:<BR>
   * When a client receives a Cookie on requesting "/products/" then
   * the path will be "/products/", and this Cookie will be attached
   * to every request for "/products/" and any of its subdirectories.
   *
   * @since Servlet API 2.0
   *
   * @param path the path
   */
  public void setPath(String path) 
  {
    myPath = path;
  }


  /**
   * Whether only secure means (https) should be used when sending this
   * cookie to a server.
   *
   * @since Servlet API 2.0
   *
   * @param secure whether this cookie should be secure or not.
   */
  public void setSecure(boolean secure) 
  {
    mySecure = secure;
  }


  /**
   * Sets a new value.
   *
   * @since Servlet API 2.0
   *
   * @param value The new value
   */
  public void setValue(String value) 
  {
    myValue = value;
  }


  /**
   * Sets the version.
   * The current type of cookies have version = 1, according to rfc2109.
   * There have been slightly different (netscape only) types of cookies,
   * but these days everyone uses version 1.
   *
   * @since Servlet API 2.0
   *
   * @param version the version
   */
  public void setVersion(int version) 
  {
    myVersion = version;
  }

  /**
   * Clones the Cookie.
   */
  public Object clone() 
  {
    try 
    {
      return super.clone();
    }
    catch (CloneNotSupportedException e) 
    {
      return null; // This should never happen
    }
  }
}
