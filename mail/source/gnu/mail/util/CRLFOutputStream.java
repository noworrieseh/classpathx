/********************************************************************
 * Copyright (c) Open Java Extensions, Andrew Selkirk  LGPL License *
 ********************************************************************/

package oje.mail.util;

// Imports
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/** ensures that all end-of-lines consist of the CR-LF character sequence.
 * This is an SMTP requirement and also is required by protocols like POP.
 *
 * @author	Andrew Selkirk
 * @version	1.0
 */
public class CRLFOutputStream
extends FilterOutputStream 
{

  /** last byte to be written.
   * -1 represents no bytes have been written yet.
   */
  protected int lastb = -1;

  /** end-of-line character sequence.
   */
  protected static byte[] newline = {13, 10};


  public CRLFOutputStream(OutputStream stream) 
  {
    super(stream);
  }

  /** write end-of-line character sequence.
   * @throws IOException IO Exception occurred
   */
  public void writeln()
  throws IOException 
  {
    out.write(newline);
  }

  /** write byte to filtered output stream.
   * Checks if CR/LF being written and ensures that all
   * end-of-lines on this output stream are CR-LF.
   * @param b Byte to write
   * @throws IOException IO Exception occurred
   */
  public void write(int b)
  throws IOException 
  {
    // Check for CR
    if (b == '\r') 
    out.write(newline);
    else if (b == '\n') 
    {
      // Only output if NOT preceeded by CR
      if (lastb != '\r') 
      out.write(newline);
    }
    else 
    {
      // Otherwise, it's safe to write the byte
      out.write(b);
    } 
    // Track the last byte
    lastb = b;
  }

  /**
   * Write byte array to filtered output stream.
   * @param bytes Byte array to write
   * @exception IOException IO Exception occurred
   */
  public void write(byte[] bytes)
  throws IOException 
  {
    write(bytes, 0, bytes.length);
  }

  /**
   * Write byte array to filtered output stream.
   * @param bytes Byte array to write
   * @param offset Offset of byte array to start
   * @param length Number of bytes to output
   * @exception IOException IO Exception occurred
   */
  public void write(byte[] bytes, int offset, int length)
  throws IOException 
  {
    // Variables
    int index;
    int writeStart;
    // Process each byte
    writeStart = offset;
    for (index = offset; index < length; index++) 
    {
      // Check for CR
      if (bytes[index] == '\r') 
      {
	out.write(bytes, writeStart, index - writeStart);
	out.write(newline);
	writeStart = index + 1;
	// Check for LF
      }
      else if (bytes[index] == '\n') 
      {
	if (lastb != '\r') 
	{
	  out.write(bytes, writeStart, index - writeStart);
	  out.write(newline);
	}
	writeStart = index + 1;
      }
      // Track Last Byte
      lastb = bytes[index];
    }
    // Check for unwritten bytes
    if (writeStart < length) 
    {
      out.write(bytes, writeStart, length - writeStart);
    }
  }


}
