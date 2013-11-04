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
package javax.servlet.http;

import java.io.Serializable;
import java.text.MessageFormat;
import java.util.Collections;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.TreeSet;

/**
 * A cookie is basically a {String,String} name/value pair that the server tells
 * the client to remember and to send back to him attached to every future
 * request.<BR>
 * Using cookies a server can maintain a state in between client requests.
 * <P>
 * A formal specification of Cookies can be found in RFC 2109
 * ("HTTP State Management Mechanism")
 *
 * @version 3.0
 * @since 2.0
 * @author Paul Siegmann (pauls@euronet.nl)
 * @author Chris Burdess
 */
public class Cookie
    implements Cloneable, Serializable
{

    private String name;
    private String value;
    private String comment;
    private String domain;
    private int maxAge = -1;
    private String path;
    private boolean secure;
    private int version = 0;
    private boolean isHttpOnly;

    // Valid HTTP/1.1 token characters
    private static final String VALID_CHARS  = "!#$%&'*+-.0123456789" +
        "ABCDEFGHIJKLMNOPQRSTUVWXYZ" +
        "^_`abcdefghijklmnopqrstuvwxyz|~";

    private static final ResourceBundle L10N =
        ResourceBundle.getBundle("javax.servlet.http.L10N");
    private static final Set TOKENS;
    static
    {
        Set acc = new TreeSet();
        acc.add("comment");
        acc.add("discard");
        acc.add("domain");
        acc.add("expires");
        acc.add("max-age");
        acc.add("path");
        acc.add("secure");
        acc.add("version");
        TOKENS = Collections.unmodifiableSet(acc);
    }

    /**
     * Creates a cookie with a name and a value.
     * The name must be a valid HTTP/1.1 token and not start with $.
     *
     * @param name The name of the cookie
     * @param value The value of the cookie
     * @exception IllegalArgumentException if name is not a valid HTTP/1.1 token
     * or starts with $
     */
    public Cookie(String name, String value)
    {
        if (name == null || name.length() < 1) 
          {
            throw new IllegalArgumentException(L10N.getString("err.cookie_name_blank"));
          }
        if (!isValidName(name) || name.charAt(0) == '$' || TOKENS.contains(name.toLowerCase())) 
          {
            String message = L10N.getString("err.cookie_name_is_token");
            Object[] args = new Object[] { name };
            throw new IllegalArgumentException(MessageFormat.format(message, args));
          }
        this.name = name;
        this.value = value;
    }

    private static boolean isValidName(String name)
    {
        int len = name.length();
        for (int i = 0; i < len; i++) 
          {
            char c = name.charAt(i);
            if (VALID_CHARS.indexOf(c) == -1) 
              {
                return false;
              }
          }
        return true;
    }

    /**
     * Sets the comment of the cookie.
     * Not supported by version 0 cookies.
     *
     * @param comment the comment to be
     */
    public void setComment(String comment) 
    {
        this.comment = comment;
    }

    /**
     * Gets the comment of the cookie
     *
     * @return the comment or null if not defined
     */
    public String getComment() 
    {
        return comment;
    }

    /**
     * Sets the domain for which this Cookie will be used.
     * If the domain is for instance set to .foo_bar.com then the client
     * sends the cookie along with requests to all webservers whose domain
     * ends with ".foo_bar.com" (www.foo_bar.com, blah.foo_bar.com, etc).
     * If not set cookies are only returned to the domain from which the client
     * received the cookie.
     *
     * @param domain The cookie's domain
     */
    public void setDomain(String domain) 
    {
        this.domain = domain;
    }

    /**
     * Gets this cookie's domain
     *
     * @return The domain for which this cookie will be used or null if not
     * defined
     */
    public String getDomain() 
    {
        return domain;
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
     * @param maxAge The time-to-live for the cookie, in seconds
     */
    public void setMaxAge(int maxAge) 
    {
        this.maxAge = maxAge;
    }

    /**
     * Gets the time-to-live for this cookie, in seconds.<BR>
     * If it is 0 then the client will delete the cookie.<BR>
     * If it is -1 (which is the default) then the cookie will
     * be a non-persistent cookie.<BR>
     * This means that the cookie will live as long as the http
     * client lives, and will not be saved to disk.
     *
     * @return the number of seconds to live or -1
     */
    public int getMaxAge() 
    {
        return maxAge;
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
     * @param path the path
     */
    public void setPath(String path) 
    {
        this.path = path;
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
     * @return the path or null if not defined
     */
    public String getPath() 
    {
        return path;
    }

    /**
     * Whether only secure means (https) should be used when sending this
     * cookie to a server.
     *
     * @param secure whether this cookie should be secure or not.
     */
    public void setSecure(boolean secure) 
    {
        this.secure = secure;
    }

    /**
     * Whether only secure means (https) should be used when sending this
     * cookie to a server.
     *
     * @return whether this cookie should be secure or not
     */
    public boolean getSecure() 
    {
        return secure;
    }

    /**
     * Get the name
     *
     * @return the Name
     */
    public String getName() 
    {
        return name;
    }

    /**
     * Sets a new value.
     *
     * @param value The new value
     */
    public void setValue(String value) 
    {
        this.value = value;
    }

    /**
     * Gets the value
     *
     * @return the Value
     */
    public String getValue() 
    {
        return value;
    }

    /**
     * Gets the version of this cookie.
     * The current type of cookies have version = 1, according to rfc2109.
     * There have been slightly different (netscape only) types of cookies,
     * but these days everyone uses version 1.
     * Fresh cookies however get a default version of 0, to improve
     * interoperability.
     *
     * @return the version
     */
    public int getVersion() 
    {
        return this.version;
    }

    /**
     * Sets the version.
     * The current type of cookies have version = 1, according to rfc2109.
     * There have been slightly different (netscape only) types of cookies,
     * but these days everyone uses version 1.
     *
     * @param version the version
     */
    public void setVersion(int version) 
    {
        this.version = version;
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
            RuntimeException e2 = new RuntimeException();
            e2.initCause(e);
            throw e2;
          }
    }

    /**
     * Marks or unmarks this cookie as HTTP-only.
     * HTTP-only cookies are not supposed to be exposed to client side
     * JavaScript.
     * @see #isHttpOnly
     * @since 3.0
     */
    public void setHttpOnly(boolean isHttpOnly)
    {
        this.isHttpOnly = isHttpOnly;
    }

    /**
     * Indicates whether this cookie has been marked as HTTP-only.
     * @see #setHttpOnly
     * @since 3.0
     */
    public boolean isHttpOnly()
    {
        return isHttpOnly;
    }

}
