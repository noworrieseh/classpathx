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

import java.io.CharConversionException;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.text.MessageFormat;
import java.util.ResourceBundle;

/**
 * Used to write output from a Servlet to the client.
 * Servlet engines should provide a subclass of ServletOutputStream that
 * implements <code>OutputStream.write(int)</code>.
 *
 * @version 3.0
 * @since 1.0
 * @author Chris Burdess
 */
public abstract class ServletOutputStream
    extends OutputStream 
{

    private static final ResourceBundle L10N =
        ResourceBundle.getBundle("javax.servlet.L10N");
    private static final Charset ISO_8859_1 = Charset.forName("ISO-8859-1");
    
    private final CharsetEncoder encoder;

    protected ServletOutputStream() 
    {
        encoder = ISO_8859_1.newEncoder();
    }

    /**
     * Writes a string in the ISO-8859-1 encoding.
     *
     * @param value the String to be printed
     * @exception IOException if an I/O exception occurs or the string is
     * not ISO-8859-1
     */
    public void print(String value)
        throws IOException 
    {
        if (value == null)
          {
            value = "null";
          }
        int len = value.length();
        for (int i = 0; i < len; i++)
          {
            char c = value.charAt(i);
            if (!encoder.canEncode(c))
              {
                String message = L10N.getString("err.not_iso_8859_1");
                Object[] args = new Object[] { Character.valueOf(c) };
                throw new CharConversionException(MessageFormat.format(message, args));
              }
          }
        byte[] byteArray = value.getBytes(ISO_8859_1);
        write(byteArray, 0, byteArray.length);
    }

    /**
     * Writes a boolean.
     *
     * @param value the boolean to be printed
     * @exception IOException if an I/O exception occurs
     */
    public void print(boolean value)
        throws IOException 
    {
        print(String.valueOf(value));
    }

    /**
     * Writes a single char.
     *
     * @param value the char to be printed
     * @exception IOException if an I/O exception occurs
     */
    public void print(char value)
        throws IOException 
    {
        print(String.valueOf(value));
    }

    /**
     * Writes an int.
     *
     * @param value the int to be printed
     * @exception IOException if an I/O exception occurs
     */
    public void print(int value)
        throws IOException 
    {
        print(String.valueOf(value));
    }

    /**
     * Writes a long.
     *
     * @param value the long to be printed
     * @exception IOException if an I/O exception occurs
     */
    public void print(long value)
        throws IOException 
    {
        print(String.valueOf(value));
    }

    /**
     * Writes a float.
     *
     * @param value the float to be printed
     * @exception IOException if an I/O exception occurs
     */
    public void print(float value)
        throws IOException 
    {
        print(String.valueOf(value));
    }

    /**
     * Writes a double.
     *
     * @param value the double to be printed
     * @exception IOException if an I/O exception occurs
     */
    public void print(double value)
        throws IOException 
    {
        print(String.valueOf(value));
    }

    /**
     * Writes a CRLF.
     *
     * @exception IOException if an I/O exception occurs
     */
    public void println()
        throws IOException 
    {
        print("\r\n");
    }

    /**
     * Writes a String followed by a CRLF.
     *
     * @param value the String to be printed
     * @exception IOException if an I/O exception occurs
     */
    public void println(String value)
        throws IOException 
    {
        print(value);
        println();
    }

    /**
     * Writes a boolean followed by a CRLF.
     *
     * @param value the boolean to be printed
     * @exception IOException if an I/O exception occurs
     */
    public void println(boolean value)
        throws IOException 
    {
        print(value);
        println();
    }

    /**
     * Writes a single char followed by a CRLF.
     *
     * @param value the char to be printed
     * @exception IOException if an I/O exception occurs
     */
    public void println(char value)
        throws IOException 
    {
        print(value);
        println();
    }

    /**
     * Writes an int followed by a CRLF.
     *
     * @param value the int to be printed
     * @exception IOException if an I/O exception occurs
     */
    public void println(int value)
        throws IOException 
    {
        print(value);
        println();
    }

    /**
     * Writes a long followed by a CRLF.
     *
     * @param value the long to be printed
     * @exception IOException if an I/O exception occurs
     */
    public void println(long value)
        throws IOException 
    {
        print(value);
        println();
    }

    /**
     * Writes a float followed by a CRLF.
     *
     * @param value the float to be printed
     * @exception IOException if an I/O exception occurs
     */
    public void println(float value)
        throws IOException 
    {
        print(value);
        println();
    }

    /**
     * Writes a double followed by a CRLF.
     *
     * @param value the double to be printed
     * @exception IOException if an I/O exception occurs
     */
    public void println(double value)
        throws IOException 
    {
        print(value);
        println();
    }

}

