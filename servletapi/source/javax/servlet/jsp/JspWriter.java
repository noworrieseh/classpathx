/*
 * Copyright (C) 1999, 2013 Free Software Foundation, Inc.
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
package javax.servlet.jsp;

import java.io.Writer;
import java.io.IOException;

/**
 * The output stream used by JSP engines.
 * @version 2.1
 * @author Nic Ferrier (nferrier@tapsellferrier.co.uk)
 * @author Mark Wielaard (mark@klomp.org)
 * @author Chris Burdess
 */
public abstract class JspWriter
    extends Writer
{

    /**
     * The writer will not buffer output.
     */
    public static final int NO_BUFFER = 0;

    /**
     * The writer will buffer output using the default buffer size.
     */
    public static final int DEFAULT_BUFFER = -1;

    /**
     * The writer is buffered and unbounded.
     */
    public static final int UNBOUNDED_BUFFER = -2;

    /**
     * Size of the buffer.
     */
    protected int bufferSize;

    /**
     * Whether the buffer is automatically flushed.
     */
    protected boolean autoFlush;

    /**
     * Make a JSP io stream.
     */
    protected JspWriter(int bufferSize, boolean autoFlush)
    {
        this.autoFlush = autoFlush;
        this.bufferSize = bufferSize;
    }

    /**
     * Write a line separator.
     */
    public abstract void newLine()
        throws IOException;

    /**
     * Print a boolean value using
     * {@link java.lang.String#valueOf(boolean)}.
     */
    public abstract void print(boolean b)
        throws IOException;

    /**
     * Print a character.
     */
    public abstract void print(char c)
        throws IOException;

    /**
     * Print an int using
     * {@link java.lang.String#valueOf(int)}.
     */
    public abstract void print(int i) throws IOException;

    /**
     * Print a long using
     * {@link java.lang.String#valueOf(long)}.
     */
    public abstract void print(long l) throws IOException;

    /**
     * Print a float using
     * {@link java.lang.String#valueOf(float)}.
     */
    public abstract void print(float f) throws IOException;

    /**
     * Print a double using
     * {@link java.lang.String#valueOf(double)}.
     */
    public abstract void print(double d)
        throws IOException;

    /**
     * Print the characters in the specified array.
     * @throws NullPointerException if the array is null
     */
    public abstract void print(char[] s)
        throws IOException;

    /**
     * Print a string.
     * If the string is null then the string "null" is printed.
     */
    public abstract void print(String s)
        throws IOException;

    /**
     * Print an object using
     * {@link java.lang.String#valueOf(java.lang.Object)}.
     */
    public abstract void print(Object o)
        throws IOException;

    /**
     * Print a line separator.
     */
    public abstract void println()
        throws IOException;

    /**
     * Print a boolean value using
     * {@link java.lang.String#valueOf(boolean)},
     * followed by a line separator.
     */
    public abstract void println(boolean b)
        throws IOException;

    /**
     * Print a character,
     * followed by a line separator.
     */
    public abstract void println(char c)
        throws IOException;

    /**
     * Print an int value using
     * {@link java.lang.String#valueOf(int)},
     * followed by a line separator.
     */
    public abstract void println(int i)
        throws IOException;

    /**
     * Print a long value using
     * {@link java.lang.String#valueOf(long)},
     * followed by a line separator.
     */
    public abstract void println(long l)
        throws IOException;

    /**
     * Print a float value using
     * {@link java.lang.String#valueOf(float)},
     * followed by a line separator.
     */
    public abstract void println(float f)
        throws IOException;

    /**
     * Print a double value using
     * {@link java.lang.String#valueOf(double)},
     * followed by a line separator.
     */
    public abstract void println(double d)
        throws IOException;

    /**
     * Print the characters in the specified array,
     * followed by a line separator.
     * @throws NullPointerException if the array is null
     */
    public abstract void println(char[] cb)
        throws IOException;

    /**
     * Print a string, or the string "null" if the string is null,
     * followed by a line separator.
     */
    public abstract void println(String s)
        throws IOException;

    /**
     * Print an object using
     * {@link java.lang.String#valueOf(java.lang.Object)},
     * followed by a line separator.
     */
    public abstract void println(Object o)
        throws IOException;

    /**
     * Clears the contents of the buffer. If data has been flushed already,
     * throws an IOException.
     */
    public abstract void clear()
        throws IOException;

    /**
     * Clears the contents of the buffer. This will not throw IOException if
     * the buffer has been flushed.
     */
    public abstract void clearBuffer()
        throws IOException;

    /**
     * Flush the buffer.
     */
    public abstract void flush()
        throws IOException;

    /**
     * Flush and close the stream.
     * The JSP container will automatically generate a call to this method
     * to terminate the page.
     */
    public abstract void close() 
        throws IOException;

    /**
     * Returns the buffer size.
     */
    public int getBufferSize()
    {
        return bufferSize;
    }

    /**
     * Returns the number of unused bytes in the buffer.
     */
    public abstract int getRemaining();

    /**
     * Indicates whether the buffer will be flushed automatically.
     */
    public boolean isAutoFlush()
    {
        return autoFlush;
    }

}
