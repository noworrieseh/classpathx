/* MimeUtility.java */

/* Liscense goes here */

package javax.mail.internet;

import javax.mail.MessagingException;
import javax.activation.DataSource;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.DataInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * Acts as a utility to encode and decode charsets into US-ASCII as
 * that is all that is allowed in RFC822 headers.
 *
 *
 * @author Joey Lesh
 */
public class MimeUtility {
	
	/* I have no idea what this is for yet. Great documentation Sun. */
	public static final int ALL = 0;

	// No constructors.

	// +---------+---------------------------------------------
	// | Methods | 
	// +---------+

	/**
	 * Gets the content-transfer-encoding that the <code>DataSource</code>
	 * should be encoded with. Using this encoding will make it mailsafe.
	 *
	 * @param ds This is the source to be encoded.
	 * @return Will be "7-bit", "quoted-printable", or "base64"
	 */
	public static String getEncoding(DataSource ds) {
		String encoding = "";
		
		// Count of non-US-ASCII bytes in the InputStream.
		int count = 0;
		// Total number of bytes in the InputStream.
		int total;

		try {
			DataInputStream data = new DataInputStream(ds.getInputStream());
			total = data.available();
			if (ds.getContentType().equals("text")) {
				count = getNumberNonAscii(data); 
				if (count >= total/2)
					encoding = "base64";
				else if (0 < count)
					encoding = "quoted-printable";
				else
					encoding = "7-bit";
			}// if (contentType is "text")
			else { 
				if (containsNonAscii(data))
					encoding = "base64";
			}// else
		}
		catch (IOException e) {
			// This will occur if the DataSource can't get an InputStream
			// or if there is an error in reading from it.
			// I guess we'll return null.
			// TO DO
			return null;
		}
		return encoding;
	}// getEncoding(ds)
   
	/**
	 *
	 *
	 */
	public static InputStream decode(InputStream is, String encoding) throws MessagingException {
		// TO DO
		// I suppose the way to do this is to use piped streams connected
		// together. Maybe another thread?  See my ThreadWriter in FLIP
		// if you're looking at this Dave. 
		return null;
	}// decode(InputStream, String)

	/**
	 *
	 */
	public static OutputStream encode(OutputStream os, String encoding) throws MessagingException {
		return null;
	}// encode(OutputStream, String)

	/**
	 *
	 */
	public static String encodeText(String text) throws UnsupportedEncodingException {
		if (containsNonAscii(new ByteArrayInputStream(text.getBytes()))) {
			// encode it.
		}// if 
		return text;
	}// encodeText(String)

	/**
	 *
	 */
	public static String encodeText(String text,String charset, String encoding) throws UnsupportedEncodingException {
	if (containsNonAscii(new ByteArrayInputStream(text.getBytes(charset)))) {
			// encode it using encoding.
		}// if
	return text;
	}// encodeText(String, String, String)

	/**
	 *
	 */
	public static String decodeText(String etext) throws UnsupportedEncodingException {
		// TO DO
		return null;
	}// decodeText(String)

	public static String encodeWord(String word) throws UnsupportedEncodingException {
		// TO DO 
		return null;
	}// encodedWord(String)
	
	/**
	 *
	 */
	public static String encodeWord(String word,String charset, String encoding)  throws UnsupportedEncodingException  {
		if (containsNonAscii(new ByteArrayInputStream(word.getBytes(charset))))
			{
				// encode it using encoding.
				//TO DO 
			}// if
		return word;
	}// encodeWord(String, String, String)
	
	/**
	 *
	 */
	public static String decodeWord(String eword) throws ParseException {
		// TO DO
		return null;
	}// decodeWord(String)
		
	/**
	 *
	 */
	public static String quote(String word, String specials) {
		// to DO
		return null;
	}// quote(String, String)

	/**
	 *
	 */
	public static String javaCharset(String charset) {
		// TO DO
		return null;
	}// javaCharset(String)

	/**
	 *
	 */
	public static String mimeCharset(String charset) {
		// TO DO
		return null;
	}// mimeCharset(String)

	/**
	 *
	 */
	public static String getDefaultJavaCharset() {
		// TO DO 
		// this is a system property thang.
		return null;
	}// getDefaultJavaCharset()


	/*********************************************
	 *  Private Stuff                            *
	 *********************************************/

	private static int getNumberNonAscii(InputStream is) {
		int count = 0;
		try {		
			DataInputStream data = new DataInputStream(is);
			int total = data.available();
			for (int i = 0; i < total; i++) {
				// an ASCII byte is between 0 and 127.
				// Other UNICODE characters have a 1 at the beginning of
				// the byte, making them "negative" in the integer world.
				if (data.readByte() < 0)
					count = count + 1;
			}
		} catch (IOException e) {
			// Ignore, count will be what it is now.
		}
		return count;
	}// getNumberNonAscii(InputStream)
	
	private static boolean containsNonAscii(InputStream is) {
		try {
			DataInputStream data = new DataInputStream(is);
			int total = data.available();
			for (int i = 0; i < total; i++) {
				// an ASCII byte is between 0 and 127.
				// Other UNICODE characters have a 1 at the beginning of
				// the byte, making them "negative" in the integer world.
				if (data.readByte() < 0)
					return true;
			}
		} catch (IOException e) {
			// Same as above, ignore.
		}
		return false;
	}// containsNonAscii(InputStream)

}// MimeUtility
