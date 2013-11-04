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

import java.io.InputStream;
import java.io.IOException;


/**
 * This class serves as a stream where servlets can read data supplied by
 * the client from.
 *
 * @version Servlet API 2.2
 * @since Servlet API 1.0
 * @author Paul Siegmann (pauls@euronet.nl)
 */
public abstract class ServletInputStream
extends InputStream 
{

  /**
   * Does nothing.
   *
   * @since Servlet API 1.0
   */
  protected ServletInputStream() 
  {
  }

  /**
   * This method read bytes from a stream and stores them into a caller
   * supplied buffer.  It starts storing the data at index
   * <code>offset</code> into the buffer and attempts to read until a 
   * end of line ('\n') is encountered or <code>length</code> bytes are
   * read.
   * This method can return before reading the number of bytes requested.
   * The actual number of bytes read is returned as an int.
   * A -1 is returned to indicate the end of the stream.
   * 
   * This method will block until some data can be read.
   *
   * This method operates by calling the single byte <code>read()</code>
   * method in a loop until the desired number of bytes are read.
   * The read loop stops short if the end of the stream is encountered
   * or if an IOException is encountered on any read operation except
   * the first.  If the first attempt to read a bytes fails, the
   * IOException is allowed to propagate upward. And subsequent
   * IOException is caught and treated identically to an end of stream
   * condition.  Subclasses can (and should if possible)
   * override this method to provide a more efficient implementation.
   *
   * @since Servlet API 1.0
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
    /* Note:
       Both the code and the javadocs are originally from Aaron
       M. Renn's (arenn@urbanophile.com) java.io.InputStream code.
       I have adapted it to my coding style and added the '\n'
       detection. I like free software!
    */
    if (length == 0) 
    {
      return(0);
    }

    // Read the first byte here in order to allow IOException's to 
    // propagate up
    int readChar = read();
    if (readChar == -1) 
    {
      return(-1);
    }
    buffer[offset] = (byte)readChar;

    int totalRead = 1;

    // Read the rest of the bytes
    try 
    {
      for (int i = 1; i < length; i++) 
      {
	if(readChar == '\n') 
	{

	  return(totalRead);
	}
	readChar = read();
	if (readChar == -1) 
	{
	  return(totalRead);
	}
	buffer[offset + i] = (byte)readChar;
	totalRead++;
      }
    }
    catch (IOException e) 
    {
      return(totalRead);
    }

    return(totalRead);
  }
}
