/*
 * Copyright (C) 2013 Free Software Foundation, Inc.
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
package javax.servlet;

import java.io.IOException;
import java.util.EventListener;

/**
 * Listener for asynchronous lifecycle operations.
 * @version 3.0
 * @since 3.0
 * @author Chris Burdess
 */
public interface AsyncListener
    extends EventListener
{

    public void onComplete(AsyncEvent event)
        throws IOException;

    public void onTimeout(AsyncEvent event)
        throws IOException;

    public void onError(AsyncEvent event)
        throws IOException;

    public void onStartAsync(AsyncEvent event)
        throws IOException;

}

