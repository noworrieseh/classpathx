/*
  GNU-Classpath Extensions: javamail
  Copyright (C) 2000 Andrew Selkirk

  For more information on the classpathx please mail: nferrier@tapsellferrier.co.uk

  This program is free software; you can redistribute it and/or
  modify it under the terms of the GNU Lesser General Public License
  as published by the Free Software Foundation; either version 2
  of the License, or (at your option) any later version.

  This program is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  GNU General Public License for more details.

  You should have received a copy of the GNU General Public License
  along with this program; if not, write to the Free Software
  Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
*/
package gnu.mail.util;

// Imports
import java.io.OutputStream;
import java.io.FilterOutputStream;
import java.io.IOException;

/**
 * Quoted Printable Encoding stream.
 *
 * @author Andrew Selkirk
 * @version 1.0
 * @see java.io.FilterOutputStream
 **/
public class QPEncoderStream
extends FilterOutputStream
{

  /**
   * Char array used in decimal to hexidecimal conversion.
   */
  private static final char[] hex = {'0','1','2','3','4','5','6',
				     '7','8','9','A','B','C','D',
				     'E','F'};

  /**
   * Current byte position in output.
   */
  private int count;

  /**
   * Number of bytes per line.
   */
  private int bytesPerLine;

  /**
   * Flag when a space is seen.
   */
  private boolean gotSpace;

  /**
   * Flag when a CR is seen.
   */
  private boolean gotCR;


  //-------------------------------------------------------------
  // Initialization ---------------------------------------------
  //-------------------------------------------------------------

  /**
   * Create a new Quoted PrintableEncoding stream.
   * @param stream Output stream
   * @param length Number of bytes per line
   */
  public QPEncoderStream(OutputStream stream, int length) 
  {
    super(stream);
    this.bytesPerLine = length;
    this.count = 0;
    this.gotSpace = false;
    this.gotCR = false;
  } // QPEncoderStream()

  /**
   * Create a new Quoted PrintableEncoding stream with
   * the default 76 bytes per line.
   * @param stream Output stream
   */
  public QPEncoderStream(OutputStream stream) 
  {
    this(stream, 76);
  } // QPEWncoderStream()


  //-------------------------------------------------------------
  // Methods ----------------------------------------------------
  //-------------------------------------------------------------

  /**
   * Flush encoding buffer.
   * @exception IOException IO Exception occurred
   */
  public void flush() throws IOException 
  {
    if (gotSpace == true) 
    {
      out.write('=');
      out.write(hex[32 >> 4]);
      out.write(hex[32 & 0xF]);
    } // if
    if (gotCR == true) 
    {
      outputCRLF();
    }
  } // flush()

  /**
   * Write bytes to encoding stream.
   * @param bytes Byte array to read values from
   * @param offset Offset to start reading bytes from
   * @param length Number of bytes to read
   * @exception IOException IO Exception occurred
   */
  public void write(byte[] bytes, int offset, int length)
  throws IOException 
  {

    // Variables
    int index;

    // Process Each Byte
    for (index = offset; index < length; index++) 
    {
      write(bytes[index]);
    } // for

  } // write()

  /**
   * Write bytes to stream.
   * @param bytes Byte array to write to stream
   * @exception IOException IO Exception occurred
   */
  public void write(byte[] bytes) throws IOException 
  {
    write(bytes, 0, bytes.length);
  } // write()

  /**
   * Write a byte to the stream.
   * @param b Byte to write to the stream
   * @exception IOException IO Exception occurred
   */
  public void write(int b) throws IOException 
  {

    // NOTE: WARNING!! This is ugly!!!!  The design needs
    // to be redone.  Does work though...

    // Check For printable characters
    if (b >= 33 && b <= 126 && b != 61) 
    {

      // Check for CR
      if (gotCR == true) 
      {
	outputCRLF();
	gotCR = false;
	count = 0;
      } // if

      // Check for space
      if (gotSpace == true) 
      {
	if (count == bytesPerLine - 1) 
	{
	  out.write('=');
	  outputCRLF();
	} // if
	out.write(' ');
	count += 1;
	gotSpace = false;
      } // if

      // Check Byte position
      if (count == bytesPerLine - 1) 
      {
	out.write('=');
	outputCRLF();
      } // if

      // Write Byte
      out.write(b);
      count += 1;

      // Check for SPACE
    } else if (b == 32) 
    {
      // Check for space
      if (gotSpace == true) 
      {
	if (count == bytesPerLine - 1) 
	{
	  out.write('=');
	  outputCRLF();
	} // if
	out.write(' ');
	count += 1;
      } // if
      gotSpace = true;

      // Check for CR
    } else if (b == 13) 
    {
      if (gotSpace == true) 
      {

	// Check Bytes Position
	if (count >= bytesPerLine - 3) 
	{
	  out.write('=');
	  outputCRLF();
	} // if

	// Write Encoded Byte
	out.write('=');
	out.write(hex[32 >> 4]);
	out.write(hex[32 & 0xF]);
	count += 3;
	gotSpace = false;

      } // if

      gotCR = true;

      // Check for LF
    } else if (b == 10) 
    {
      if (gotSpace == true) 
      {

	// Check Bytes Position
	if (count >= bytesPerLine - 3) 
	{
	  out.write('=');
	  outputCRLF();
	} // if

	// Write Encoded Byte
	out.write('=');
	out.write(hex[32 >> 4]);
	out.write(hex[32 & 0xF]);
	count += 3;
	gotSpace = false;

      } // if
      outputCRLF();

      // Write Encoded Character
    } else 
    {

      // Check for CR
      if (gotCR == true) 
      {
	outputCRLF();
	gotCR = false;
      } // if

      // Check for space
      if (gotSpace == true) 
      {
	if (count == bytesPerLine - 1) 
	{
	  out.write('=');
	  outputCRLF();
	} // if
	out.write(' ');
	count += 1;
	gotSpace = false;
      } // if

      // Check Bytes Position
      if (count >= bytesPerLine - 3) 
      {
	out.write('=');
	outputCRLF();
      } // if

      // Write Encoded Byte
      out.write('=');
      out.write(hex[b >> 4]);
      out.write(hex[b & 0xF]);
      count += 3;

    } // if

  } // write()

  /**
   * Close stream.
   * @exception IOException IO Exception occurred
   */
  public void close() throws IOException 
  {

    // Flush Stream
    flush();

    // Close
    out.close();

  } // close()

  /**
   * ????
   * @param b ??
   * @param value ??
   * @exception IOException IO Exception occurred
   */
  protected void output(int b, boolean value)
  throws IOException 
  {
    // TODO
  } // output()

  /**
   * Write CRLF byte series to stream.
   * @exception IOException IO Exception occurred
   */
  private void outputCRLF() throws IOException 
  {
    out.write('\r');
    out.write('\n');
    count = 0;
  } // outputCRLF()


} // QPEncoderStream
