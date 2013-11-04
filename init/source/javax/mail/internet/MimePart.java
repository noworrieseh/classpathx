/* javax.mail.internet.MimePart */

/**
 * Liscense  
 */


package javax.mail.internet;

import javax.mail.Part;
import javax.mail.MessagingException;
import java.util.Enumeration;

/**
 *
 * @author Joey Lesh
 */
public abstract interface MimePart extends Part {

	/**
	 *
	 */
	public String getHeader(String headerName, String delimiter) throws MessagingException;
	// In the JavaMail API the first param is called header_name which
	// is totally incorrect naming form so I'm calling it headerName.


	/**
	 *
	 * @exception Throws an <code>IllegalWriteException</code> if read-only.
	 */
	public void addHeaderLine(String line) throws MessagingException; 
	/**
	 *
	 */
	public Enumeration getAllHeaderLines(String[] names) throws MessagingException;

	/**
	 *
	 */
	public Enumeration getMatchingHeaderLines(String[] names) throws MessagingException;

	/**
	 *
	 */
	public Enumeration getNonMatchingHeaderLines(String[] names) throws MessagingException ;

	/**
	 *
	 */
	public String getEncoding() throws MessagingException ;

	/**
	 *
	 */
	public String getContentID() throws MessagingException ;

	/**
	 *
	 */
	public String getContentMD5() throws MessagingException ;

	/**
	 *
	 */
	public void setContentMD5(String md5) throws MessagingException ;

	/**
	 *
	 */
	public String[] getContentLanguage() throws MessagingException ;
	
	/**
	 *
	 */
	public void setContentLanguage(String[] languages) throws MessagingException;
	
	/**
	 *
	 */
	public void setText(String text) throws MessagingException ;
	/**
	 *
	 */
	public void setText(String text, String charset) throws MessagingException;

}// MimePart

