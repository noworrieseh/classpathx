/*
  GNU-Classpath Extensions: GNU Javamail - SMTP Service Provider
  Copyright (C) 2001 Benjamin A. Speakmon

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

package gnu.mail.providers.smtp;

import java.io.IOException;
import java.io.OutputStream;
import gnu.mail.util.CRLFOutputStream;

/** 
 * A stream to handle SMTP's concept of full stop.
 * This stream wraps the Output stream that is generated in
 * SMTPTransport for data transmission of the message contents.
 * Since it is possible to include a '.' on a single line in a
 * message (which in the SMTP protocol refers to the end of the
 * data stream), this stream protects the out bound data by
 * prepending another '.' according to the SMTP rfc.
 *
 * @author      Andrew Selkirk
 * @author      Ben Speakmon
 * @version     1.0
 */
public class SMTPOutputStream
  extends CRLFOutputStream
{

  private int lastb;

  /** make the SMTP stream, pointing to another stream.
   * @param stream the stream connected to the SMTP layer
   */
  public SMTPOutputStream(OutputStream stream) 
  {
    super(stream);
  }

  /** 
   * Write a byte to the SMTP output stream.
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

  /** 
   * Write a byte array to the SMTP output stream.
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
