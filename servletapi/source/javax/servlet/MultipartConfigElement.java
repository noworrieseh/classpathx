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

import javax.servlet.annotation.MultipartConfig;

/**
 * A MultipartConfig annotation value.
 * @version 3.0
 * @since 3.0
 * @author Chris Burdess
 */
public class MultipartConfigElement
{

    private String location;
    private long maxFileSize;
    private long maxRequestSize;
    private int fileSizeThreshold;

    /**
     * Constructs a new element.
     */
    public MultipartConfigElement(String location)
    {
        if (location == null)
          {
            this.location = "";
          }
        maxFileSize = -1L;
        maxRequestSize = -1L;
        fileSizeThreshold = 0;
    }

    /**
     * Constructs a new element with the specified values.
     */
    public MultipartConfigElement(String location, long maxFileSize, long maxRequestSize, int fileSizeThreshold)
    {
        if (location == null)
          {
            this.location = "";
          }
        this.maxFileSize = maxFileSize;
        this.maxRequestSize = maxRequestSize;
        this.fileSizeThreshold = fileSizeThreshold;
    }

    /**
     * Constructs a new element based on the specified config object.
     */
    public MultipartConfigElement(MultipartConfig annotation)
    {
        location = annotation.location();
        fileSizeThreshold = annotation.fileSizeThreshold();
        maxFileSize = annotation.maxFileSize();
        maxRequestSize = annotation.maxRequestSize();
    }

    /**
     * Returns the directory location for file storage.
     */
    public String getLocation()
    {
        return location;
    }

    /**
     * Returns the maximum file size for uploaded files.
     */
    public long getMaxFileSize()
    {
        return maxFileSize;
    }

    /**
     * Returns the maximum size allowed for multipart/form-data requests.
     */
    public long getMaxRequestSize()
    {
        return maxRequestSize;
    }

    /**
     * Returns the size threshold above which files will be written to disk.
     */
    public int getFileSizeThreshold()
    {
        return fileSizeThreshold;
    }

}
