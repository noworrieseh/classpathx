/**
 * This is part of the Open Java Extensions project. 
 */
package javax.mail.internet;

import java.lang.Exception;

/**
 * This subclass of <code>ParseException</code> is used when an incorrectly
 * formatted address is encountered.
 * 
 * @author Joey Lesh (joey@gnu.org)
 */
public class AddressException extends ParseException {
	
	/**
	 * The postion (index) in the <code>String</code> where the problem 
	 * occurred. Value is -1 if position not known. */
	protected int pos = -1;

	/*
	 * The <code>String</code> in which parsing failed.
	 */
	protected String ref;
	
	/**
	 * Constructs an <code>AddressException</code> without a detail
	 * message.
	 */
	public AddressException() {
		super();
	}

	/**
	 * Constructs an <code>AddressException</code> with a message
	 * giving more detail.
	 */
	public AddressException(String s) {
		super(s);
	}

	/**
	 * Constructs an <code>AddressException</code> with both a
	 * message giving details of the exception and  the <code>String</code>
	 * upon which the error ocurred.
	 *
	 * @param s the detail message
	 * @param reference the <code>String</code> which caused the error.
	 */ 
	public AddressException(String s, String reference) {
		super(s);
		ref= reference;
	}

	/**
	 * Constructs an <code>AddressException</code> with both a
	 * message giving details of the exception and  the <code>String</code>
	 * upon which the error ocurred. It also includes the postion at which
	 * the exception was thrown.
	 *
	 * @param s the detail message
	 * @param reference the <code>String</code> which caused the error.
	 * @param position the index at which the error was found.
	 */ 
	public AddressException(String s, String reference, int position) {
		super(s);
		ref= reference;
		pos= position;
	}

	/**
	 * Gets the String which was being operated on when this exception was 
	 * thrown.
	 *
	 * @return the reference String. 
	 */
	public String getRef() {
		return this.ref;
	}
	
	/**
	 * Gets the index in <code>this.ref</code> at which the processing was
	 * halted.
	 *
	 * @return the <code>int</code> index. 
	 */
	public int getPos() {
		return this.pos;
	}

	/**
	 * Creates a <code>String</code> that contains all of the information
	 * that this <code>AddressException</code> has, including the reference
	 * string and the position.
	 *
	 * @return the detail information.
	 */
	public String toString() {
	    return super.toString() + (ref != null ? " In address " + ref + (pos > -1 ? " at " + new Integer(pos).toString() : "") : "");
	}

}//public class AddressException

	



