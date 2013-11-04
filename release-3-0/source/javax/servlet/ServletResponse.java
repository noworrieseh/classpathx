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

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Locale;

/**
 * A servlet can use this class to pass information to the client.
 *
 * @version 3.0
 * @since 1.0
 * @author Paul Siegmann (pauls@euronet.nl)
 * @author Charles Lowell (cowboyd@pobox.com)
 */
public interface ServletResponse
{

    /**
     * Returns the character encoding in use by this Response
     *
     * @since 2.0
     * @return the characterset encoding
     */
    String getCharacterEncoding();

    /**
     * get the content type if it has been set.
     * 
     * @since 2.4
     * 
     * @return the content type if set or null if not.
     */
    String getContentType();

    /**
     * Creates a ServletOutputStream for the servlet to write the data to.
     * The stream might have a buffer attached to it, the size of which the
     * user might be able to alter.
     *
     * <p>
     * No encoding of data written to this stream is done by the container.
     * </p>
     *
     * <p>
     * It is only possible to call <code>getWriter</code> or
     * <code>getOutputStream</code> on a response, but not both.
     * </p>
     *
     * @return ServletOutputStream to write binary data
     * @throws IOException if a i/o exception occurs
     * @throws IllegalStateException if <code>getWriter</code> was already
     *   called on this response
     * @see #setContentLength which the container might use to provide a buffer
     *   for this stream.
     */
    ServletOutputStream getOutputStream()
        throws IOException;

    /**
     * Creates a PrintWriter for the servlet to print text to.
     * The writer will have the charset associated with any previously
     * specified content type IF the content type has already been set.
     * If the default char set is acceptable them content type need not be
     * set before this method is called.
     *
     * <p>
     * It is only possible to call <code>getWriter</code> or
     * <code>getOutputStream</code> on a response, but not both.
     * </p>
     *
     * @since 2.0
     *
     * @return the created PrintWriter
     * @throws IOException if a i/o exception occurs
     * @throws IllegalStateException if <code>getOutputStream</code> was
     *   already called on this response
     * @throws java.io.UnsupportedEncodingException if no suitable character
     *   encoding can be used
     * @see #setContentType which must be called before this if you want to
     *   specify a charset to affect this writer.
     */
    PrintWriter getWriter()
        throws IOException;

    /**
     * sets the character encoding.
     * 
     * @since 2.4
     *
     * @parameter a IANA Character Sets
     * {@link (http://www.iana.org/assignments/character-sets)} string.
     */
    void setCharacterEncoding(String encoding);

    /**
     * Tells the client how many bytes to expect.
     *
     * @param length the number of bytes in the reply
     */
    void setContentLength(int length);

    /**
     * Tells the client what mime type to expect
     *
     * @param type the mime type of the content
     */
    void setContentType(String type);

    /**
     * set the size of the buffer where caching is used.
     * Caching is sometimes used on responses to ensure that content length
     * can be written properly.
     *
     * @param size the size in bytes of the buffer
     *
     * @since 2.2
     * @throws IllegalStateException if content has already been sent.
     */
    void setBufferSize(int size);

    /**
     * get the current size of the response cache buffer.
     *
     * @since 2.2
     */
    int getBufferSize();

    /**
     * flush away any extant response cache.
     *
     * @since 2.2
     */
    void flushBuffer()
        throws IOException;

    /**
     * Resets the underlying response buffer, but does not clear the response code
     * or headers.
     *
     * @since 2.3
     *
     * @throws IllegalStateException if the response has already been committed
     */

    void resetBuffer();

    /**
     * has the response cache been written to the client?
     *
     * @since 2.2
     */
    boolean isCommitted();

    /**
     * reset the current response cache buffer.
     *
     * @since 2.2
     * @throws IllegalStateException if content has already been sent.
     */
    void reset();

    /**
     * set the locale for the response.
     *
     * @since 2.2
     */
    void setLocale(Locale locale);

    /**
     * get the locale for the response.
     *
     * @since 2.2
     */
    Locale getLocale();

}

