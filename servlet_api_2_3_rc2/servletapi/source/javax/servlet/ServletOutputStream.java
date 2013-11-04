/*
  GNU-Classpath Extensions: Servlet API
  Copyright (C) 1998, 1999, 2001   Free Software Foundation, Inc.

  For more information on the classpathx please mail: classpathx-discuss@gnu.org

  This program is free software; you can redistribute it and/or
  modify it under the terms of the GNU Lesser General Public License
  as published by the Free Software Foundation; either version 2
  of the License, or (at your option) any later version.

  This program is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  GNU General Public License for more details.

  You should have received a copy of the GNU Lesser General Public License
  along with this program; if not, write to the Free Software
  Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
*/
package javax.servlet;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Used to write output from a Servlet to the client.
 * Servlet engines should provide a subclass of ServletOutputStream that
 * implements <code>OutputStream.write(int)</code>.
 * <p>
 * Note that I (MJW) do not understand how the <code>print</code> methods work
 * when the stream uses something else then a simple ASCII character encoding.
 * It seems saver to use <code>ServletResponse.getWriter()</code> for all
 * output that is not binary.
 *
 * @version Servlet API 2.2
 * @since Servlet API 1.0
 * @author Paul Siegmann (pauls@euronet.nl)
 */
public abstract class ServletOutputStream extends OutputStream 
{

  protected ServletOutputStream() 
  {
  }

  /**
   * Writes a String.
   *
   * @since Servlet API 1.0
   *
   * @param value the String to be printed
   * @exception IOException if an I/O exception occurs
   */
  public void print(String value)
    throws IOException 
  {
    byte[] byteArray = value.getBytes();
    write(byteArray, 0, byteArray.length);
  }


  /**
   * Writes a boolean.
   *
   * @since Servlet API 1.0
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
   * @since Servlet API 1.0
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
   * @since Servlet API 1.0
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
   * @since Servlet API 1.0
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
   * @since Servlet API 1.0
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
   * @since Servlet API 1.0
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
   * @since Servlet API 1.0
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
   * @since Servlet API 1.0
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
   * @since Servlet API 1.0
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
   * @since Servlet API 1.0
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
   * @since Servlet API 1.0
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
   * @since Servlet API 1.0
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
   * @since Servlet API 1.0
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
   * @since Servlet API 1.0
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
