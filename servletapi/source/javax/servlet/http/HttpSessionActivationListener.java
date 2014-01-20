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

import java.util.EventListener;

/**
 * Objects that are bound to a session as an attribute and implementing
 * this interface will be notified whenever the session to which they
 * are bound is activated or passivated. It is the responsibility of the
 * container to notify all such objects implementing this interface
 * upon activation and passivation
 *
 * @version 3.0
 * @since 2.3
 * @author Charles Lowell (cowboyd@pobox.com)
 */
public interface HttpSessionActivationListener
    extends EventListener
{

    /**
     * This method is called to notify that the session will soon be passivated
     *
     * @param event the event representing the upcoming passivation
     */
    void sessionWillPassivate(HttpSessionEvent event);

    /**
     * This method is called to notify that the session has just been activated
     * 
     * @param event the event representing the session activation
     */
    void sessionDidActivate(HttpSessionEvent event);

}
