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
package javax.servlet;

import java.io.InputStream;
import java.io.IOException;

/**
 * This class serves as a stream where servlets can read data supplied by
 * the client from.
 * @version 3.0
 * @since 1.0
 * @author Chris Burdess
 */
public abstract class ServletInputStream
    extends InputStream 
{

    /**
     * Does nothing.
     */
    protected ServletInputStream() 
    {
    }

    /**
     * Reads bytes using the {@link #read} method until it reaches the
     * limit, EOF or an LF character (which will also be placed in the
     * array). Returns -1 if EOF is reached.
     *
     * @param buffer The array into which the bytes read should be stored
     * @param offset The offset into the array to start storing bytes
     * @param length The maximum number of bytes to read
     *
     * @return The actual number of bytes read, or -1 if end of stream.
     *
     * @exception IOException If an error occurs.
     */
    public int readLine(byte[] buffer, int offset, int length)
        throws IOException 
    {
        // NB replaced Paul Siegmann's extremely complicated code here
        // with simpler algorithm.
        if (length < 1)
          {
            return 0;
          }
        int count = 0;
        int c = read();
        do
          {
            if (c < 0)
              {
                break;
              }
            buffer[offset++] = (byte) (c & 0xff);
            count++;
            c = read();
          }
        while (c != 10 && count < length);
        return (count < 1) ? -1 : count;
    }

}

