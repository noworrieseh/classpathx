/* javax.mail.internet.ParseException
 *        Exception thrown during parsing headers.
 *
 *  Covered by the LGPL. Copyright Open Java Extensions, et al.
 */

package javax.mail.internet;

import javax.mail.MessagingException;

/**
 * This <code>ParseException</code> should be thrown to indicate an error
 * parsing RFC822 or MIME formatted headers.
 *
 * @author Joey Lesh (joey@gnu.org)
 */
public class ParseException extends MessagingException {
	
	/**
	 * Constructs a new <code>ParseException</code> without a detail message.
	 */
	public ParseException() {
		super();
	}// ParseException()

	/**
	 * Constructs a new <code>ParseException</code> with a detail message.
	 *
	 * @param s the detail message.
	 */
	public ParseException(String s) {
		super(s);
	}// ParseException(String)
}// ParseException
