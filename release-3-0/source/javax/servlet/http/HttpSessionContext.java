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

/**
 * Contains information shared by all the HttpSessions.
 *
 * @see javax.servlet.http.HttpSessionContext
 * @deprecated This class has been deprecated for security reasons.
 * We don't want servlets messing around in other sessions,
 * however convenient that might be.
 * @version 3.0
 * @since 2.0
 * @author Paul Siegmann (pauls@euronet.nl)
 */
public interface HttpSessionContext
{

    /**
     * Get the session with the given id.
     * @deprecated This method should always return null
     * @param id the id of the HttpSession we're looking for.
     * @return The HttpSession we're looking for, null if not present.
     */
    HttpSession getSession(String id);

    /**
     * Get all sessions ids.
     * @deprecated This method should always return an empty enumeration
     * @return an Enumeration containing all session id's.
     */
    Enumeration<String> getIds();

}
