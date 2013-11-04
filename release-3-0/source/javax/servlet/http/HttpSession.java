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

import java.util.Enumeration;
import javax.servlet.ServletContext;

/**
 * A HttpSession holds session-dependant data on the server side.
 * A servlet can request the servlet by using HttpServletRequest.getSession(...)
 * <P>
 * The handling of the Session objects is a job done by the server and
 * servlets together.
 * <DL>
 * <DT>As follows:
 * <DD>
 * The server maintains the set of HttpSessions.<BR>
 * The server creates HttpSession on request by a servlet<BR>
 * The server removes all invalidated HttpSessions<BR>
 * The server connects an incoming request with its HttpSession
 * (usually done using cookies)<BR>
 * Servlets manipulate the contents of the HttpSession by adding and removing
 * items.<BR>
 * Servlets ask the server to remove HttpSessions by invalidating them<BR>
 * </DD>
 * </DL>
 *
 * @version 3.0
 * @since 2.0
 * @author Paul Siegmann (pauls@euronet.nl)
 * @author Charles Lowell (cowboyd@pobox.com)
 */
public interface HttpSession
{

    /**
     * Gets this session's creation time in seconds since january 1st 1970.
     *
     * @return a number of seconds
     * @exception IllegalStateException if the session has been invalidated.
     */
    long getCreationTime();

    /**
     * Gets the unique session id.
     * Every HttpSession has a Id that is unique for this (virtual) http
     * server.
     *
     * @return The Id
     * @exception IllegalStateException if the session has been invalidated.
     */
    String getId();

    /**
     * Gets the number of seconds since the previous access of this session.
     * Every time a client's request comes in the server checks (usually by
     * using cookies) which HttpSession object corresponds with this
     * particular client.
     * The server then sets the lastAccessedTime to the current time.
     * (And the isNew flag to false.) If the client has never requested anything
     * with this Session then this method returns -1
     *
     * @return number of seconds since last access or -1
     * @exception IllegalStateException if the session has been invalidated.
     */
    long getLastAccessedTime();

    /**
     * Get the ServletContext to which this session belongs
     *
     * @since 2.3
     *
     * @return the ServletContext representing the web application
     * of which this session is a part.
     */
    ServletContext getServletContext();

    /**
     * Sets the minimum time this session will be kept alive by the
     * server when it doesn't get accessed by a client.<BR>
     * <B>Note:</B> hmmm, should an interval of -1 mean that it should live forever?
     * @since 2.1
     *
     * @param interval Probably seconds or -1 if never
     *
     * @throws IllegalStateException if this session has been invalidated
     */
    void setMaxInactiveInterval(int interval);

    /**
     * Returns the minimum time this session will be kept alive by the
     * server when it doesn't get accessed by a client.
     *
     * @since 2.1
     *
     * @returns the time in seconds, or -1 when this session will live forever
     */
    int getMaxInactiveInterval();

    /**
     * Gets this HttpSession's context.
     * The context contains information that is the same for all HttpSessions
     * for this (virtual) host.
     *
     * @deprecated <code>HttpSessionContext</code> has been deprecated for
     *	security reasons. 
     *
     * @return The context
     * @exception IllegalStateException if the session has been invalidated.
     */
    HttpSessionContext getSessionContext();

    /**
     * get the attribute value associated with the name
     *
     * @since 2.2
     *
     * @param name the name of the attribute
     * @return the Object associated with this name
     * @throws IllegalStateException if this session has been invalidated
     */
    Object getAttribute(String name);

    /**
     * Gets a object from the set of name/value pairs in the session.
     *
     * @param name the name of the item required
     * @return the value of the item. null if not present.
     * @exception IllegalStateException if the session has been invalidated.
     *
     * @deprecated use {@link #getAttribute} instead
     */
    Object getValue(String name);

    /**
     * get the list of attribute names
     *
     * @since 2.2
     * @throws IllegalStateException if this session has been invalidated
     */
    Enumeration getAttributeNames();

    /**
     * Get a list of all item names in the session.
     *
     * @return An array of Strings containing all item names.
     * @exception IllegalStateException if the session has been invalidated.
     * @deprecated use {@link #getAttributeNames} instead
     */
    String[] getValueNames();

    /**
     * set the attribute value to be associated with the name.
     *
     * @since 2.2
     * @throws IllegalStateException if this session has been invalidated
     */
    void setAttribute(String name, Object value);

    /**
     * Puts a name and value in the HttpSession.
     * If the Object implements <code>HttpSessionBindindListener</code> then
     * the <code>valueBound()</code> method of the Object will be called.
     *
     * @param name the name of the item
     * @param value the value of the item
     * @exception IllegalStateException if the session has been invalidated.
     * @deprecated Servlet API 2.2 use {@link #setAttribute instead}
     */
    void putValue(String name, Object value);

    /**
     * remove the specified attribute.
     *
     * @since 2.2
     * @throws IllegalStateException if this session has been invalidated
     */
    void removeAttribute(String name);

    /**
     * Removes an item from the session.
     * If the Object implements <code>HttpSessionBindindListener</code> then
     * the <code>valueUnBound()</code> method of the Object will be called.
     *
     * @exception IllegalStateException if the session has been invalidated.
     * @param name the name of the item.
     * @deprecated use {@link #removeAttribute} instead
     */
    void removeValue(String name);

    /**
     * Make this HttpSession unavailable for use by other servlets and tell
     * the server to remove this session. All values bound to this session
     * with <code>putValue()</code> that implement
     * <code>HttpSessionBindingListener</code> will be called with
     * <code>valueUnbound()</code>.
     * Also: make it throw an IllegalStateException when a
     * servlet tries to execute one of its methods.
     *
     * @exception IllegalStateException if the session has been invalidated.
     */
    void invalidate();

    /**
     * Returns whether this session has been freshly created.
     * A servlet can ask the server to give the HttpSession connected with
     * this request/client.
     * The Servlet can use this method to check whether the HttpSession has
     * been newly created or if a HttpSession had already been created for a
     * previous request.
     *
     * @return Whether this is a new HttpSession
     * @exception IllegalStateException if the session has been invalidated.
     */
    boolean isNew();

}
