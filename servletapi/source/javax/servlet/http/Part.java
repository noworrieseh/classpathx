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
package javax.servlet.http;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;

/**
 * A MIME body part received as part of a multipart/form-data request.
 * @version 3.0
 * @since 3.0
 * @author Chris Burdess
 */
public interface Part
{

    /**
     * Returns the part content.
     */
    InputStream getInputStream()
        throws IOException;

    /**
     * Returns the MIME Content-Type of the part.
     */
    String getContentType();

    /**
     * Returns the name of this part.
     */
    String getName();

    /**
     * Returns the size of the part.
     */
    long getSize();

    /**
     * Convenience method to write this part to disk.
     * @param fileName the name of the file to write to, relative to the
     * location specified in MultipartConfig.
     */
    void write(String fileName)
        throws IOException;

    /**
     * Delete any underlying storage for this part.
     */
    void delete()
        throws IOException;

    /**
     * Returns the specified MIME header value, or null if no such header
     * exists.
     */
    String getHeader(String name);

    /**
     * Returns the specified MIME header values, or an empty collection if
     * no such header exists.
     */
    Collection<String> getHeaders(String name);

    /**
     * Returns the header names for this part.
     * NB some containers are broken and this method may return null.
     */
    Collection<String> getHeaderNames();

}
