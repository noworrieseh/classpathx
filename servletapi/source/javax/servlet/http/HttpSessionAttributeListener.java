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
 * Implementers of this interface can receive notifications when
 * the attributes of the sessions in a web application change
 * either through addittion, deletion, or replacement
 *
 * @version 3.0
 * @since 2.3
 * @author Charles Lowell (cowboyd@pobox.com)
 */
public interface HttpSessionAttributeListener
    extends EventListener
{

    /**
     * Indicates that an attribute has been added to a session
     */
    void attributeAdded(HttpSessionBindingEvent event);

    /**
     * Indicates that an attribute has been removed from a session
     */
    void attributeRemoved(HttpSessionBindingEvent event);

    /**
     * Indicates that an attribute has been replaced in a session
     */
    void attributeReplaced(HttpSessionBindingEvent event);

}

