/********************************************************************
 * Copyright (c) Open Java Extensions, Andrew Selkirk  LGPL License *
 ********************************************************************/

package gnu.mail.providers.smtp;

// Imports
import java.io.IOException;
import java.io.OutputStream;
import gnu.mail.util.CRLFOutputStream;


/** a stream to handle SMTP's concept of full stop.
 * This stream wraps the Output stream that is generated in
 * SMTPTransport for data transmission of the message contents.
 * Since it is possible to include a '.' on a single line in a
 * message (which in the SMTP protocol refers to the end of the
 * data stream), this stream protects the out bound data by
 * prepending another '.' according to the SMTP rfc.
 *
 * @author	Andrew Selkirk
 * @version	1.0
 */
public class SMTPOutputStream
extends CRLFOutputStream
{

  /** make the SMTP stream, pointing to another stream.
   * @param stream the stream connected to the SMTP layer
   */
  public SMTPOutputStream(OutputStream stream) 
  {
    super(stream);
  }

  /** write byte to SMTP output stream.
   * If '.' is found according to the end-of-data guidelines,
   * then the byte is doubled.
   * @param b Byte to write
   * @throws IOException IO exception occurred
   */
  public void write(int b) throws IOException 
  {
    //check for '.' that was preceeded by eol character
    if (b == '.' && (lastb == '\r' || lastb == '\n')) 
    {
      out.write(new byte[]{46, 46}, 0, 2);
      lastb = b;
    }
    else 
    {
      super.write(b);
    }
  }

  /** write byte array to SMTP output stream.
   * If '.' is found according to the end-of-data guidelines,
   * then the byte is doubled.
   * @param bytes Byte array to write
   * @param offset Offset of bytes to write
   * @param length Number of bytes to write
   * @throws IOException IO exception occurred
   */
  public void write(byte[] bytes, int offset, int length)
  throws IOException 
  {
    int index;
    int writeStart;
    //process Each Byte
    writeStart = offset;
    for (index = offset; index < length; index++) 
    {
      if (bytes[index] == '.') 
      {
	// Output Previous bytes to CRLF layer
	super.write(bytes, writeStart, index - writeStart);
	// Write '.'
	write('.');
	// Track new Start
	writeStart = index + 1;
      }
    }
    // Check for Remaining bytes
    if (writeStart < length) 
    super.write(bytes, writeStart, length - writeStart);
  }

}
