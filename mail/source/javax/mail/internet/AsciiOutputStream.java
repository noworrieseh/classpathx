/* AsciiOutputStream.java */

/* Liscense goes here. */

package javax.mail.internet;

import java.io.OutputStream;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * A <code>FilterOutputStream</code> subclass that provides methods to
 * write data in the internet standard US-ASCII format. All special
 * characters should be encoded using the MIME standard. 
 *
 * @author Joseph Carter Lesh (joey@gnu.org)
 * @see java.io.FilterOutputStream
 */
public class AsciiOutputStream extends FilterOutputStream {
 
 /**
   * Constructs a new ASCIIOutputStream.
   * @param out The stream to which data is written
   */
  public AsciiOutputStream(OutputStream out) {
    super (out);
  }

  /**
   * Writes a <code>String</code> in ASCII format, discarding the top byte of
   * every character.
   *
   * @param s The <code>String</code> to be written
   * @exception An <code>IOException</code> signalling trouble
   *            performing an i/o operation. 
   */
  public void write(String toBeWritten) throws IOException {
	  try {
		  write(toBeWritten.getBytes("iso-8859-1"));
	  }
	  catch (UnsupportedEncodingException e) {
		  //We're using US-ASCII, so this should essentially]
		  //never happen. If it does, we can't use the 'net anyway
		  //so we're pretty much screwed. Anyway, ignore this.
	  }
  }// write(String)
}// AsciiOutputStream
