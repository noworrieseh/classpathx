/* InternetAddress.java */

/* Liscense goes here. */


package javax.mail.internet;

import javax.mail.Address;
import javax.mail.Session;
import javax.mail.internet.AddressException;
import java.io.UnsupportedEncodingException;

/**
 * This class models an RFC822 address consisting of a personal name
 * plus some sort of host address.
 *
 * @author Joey Lesh
 */
public class InternetAddress extends Address {
	
	// +--------+------------------------------------
	// | Fields |
	// +--------+

	/* The actual address. */	
	protected String address;

	/* The RFC 2047 encoded personal name. If you change this field or the
	   personal name field, the other should be set to null so that the other 
	   can be recomputed. */
	protected String encodedPersonal;

	/* The personal name. If you change this field or the
	   personal name field, the other should be set to null so that the other 
	   can be recomputed. */ 
	protected String personal;

	// +--------------+------------------------------
	// | Constructors |
	// +--------------+

	public InternetAddress() {
		this.personal = "Joey Lesh";
		this.address = "gnu.org";
		this.encodedPersonal = "Joey Lesh";
	}// InternetAddress()

	/**
	 * Constructs a new <code>InternetAddress</code> by parsing the 
	 * parameter address.
	 *
	 * @param address the RFC822 formatted address
	 * @exception Thrown when the <code>address</code> is in a wrong
	 *            format.
	 */
	public InternetAddress(String address) throws AddressException {
		// TO DO 
	}// InternetAddress(String)
	
	/**
	 * Constructs a new <code>InternetAddress</code> with the address and 
	 * personal name as parameters. The address is assumed to be a valid
	 * address in RFC822 format.
	 *
	 * @param address A valid RFC822 address.
	 * @param personal The personal name associated with the address.
	 * @exception Thrown when the personal name can not be converted to 
	 *            US-ASCII using the RFC2047 guidelines. (I assume)
	 */
	public InternetAddress(String address, String personal) throws UnsupportedEncodingException {
		this.personal = personal;
		this.address = address;
		this.encodedPersonal = MimeUtility.quote(MimeUtility.encodeWord(this.personal), HeaderTokenizer.RFC822);
	}// InternetAddress(String, String)
	
	// +---------+------------------------------
	// | Methods |
	// +---------+
	
	/**
	 * The equality operater. Equality is judged by equivalent 
	 * <code>address</code>'s and <code>personal</code>'s. Objects of other
	 * type than <code>InternetAddress</code> are not equal.
	 *
	 * @param a The <code>Object</code> to compare with.
	 * @return true if equal; false otherwise.
	 */
	public boolean equals(Object a) {
		if (a instanceof InternetAddress) {
			if (((InternetAddress) a).getAddress().equalsIgnoreCase(this.address) && ((InternetAddress) a).getPersonal().equals(this.personal))
				return true;
		}
		return false;
	}// equals(Object)

	/**
	 * Gets the RFC822 email address.
	 *
	 * @return the address.
	 */
	public String getAddress() {
		return this.address;
	}// getAddress()
	
	/**
	 * Gets the personal name for this address.
	 *
	 * @return the personal name.
	 */
	public String getPersonal() { 
		//Oh, I'll get personal don't you worry.
		return this.personal;
		// Is this.personal enough for you sweetheart?
	}// getPersonal()

	/**
	 * Gets the type of this address. Type is "rfc822"
	 *
	 * @return the type of address (rfc822)
	 */
	public String getType() {
		//I told you, you were my type :)
		return "rfc822";
	}// getType()
	
	/**
	 * Computes a hash code for this.
	 *
	 * @return The <code>int</code> hash code. 
	 */ 
	public int hashCode() {
		// TO DO
		return 0;
	}// hashCode()

	/**
	 * Sets the address to the new <code>String</code>.
	 *
	 * @param address the RFC822 formatted address.
	 */
	public void setAddress(String address) {
		this.address = address;
	}// setAddress(String)
	
	/**
	 * Sets the personal name to the new <code>String</code>
	 * and attempts to encode it into US-ASCII using the platform's default
	 * encoding if it contains non-US-ASCII characters.
	 *
	 * @param name The personal name.
	 * @exception If the encoding attempt fails.
	 */
	public void setPersonalName(String name) throws UnsupportedEncodingException {
		// encodedPsersonal should be done first otherwise the
		// state could be messed up if an exception is thrown
	    this.encodedPersonal = MimeUtility.encodeWord(name);
		this.personal = name;
	}// setPersonalName(String)

	/** 
	 * Sets the personal name to the new <code>String</code>
	 * and attempts to encode it into US-ASCII using the given
	 * encoding if it contains non-US-ASCII characters.
	 *
	 * @param name The personal name.
	 * @param charset The name of the encoding to use to encode the personal
	 *        name into RFC2047 format.
	 * @exception If the encoding attempt fails.
	 */
	public void setPersonalName(String name, String charset) throws UnsupportedEncodingException {
		this.encodedPersonal = MimeUtility.encodeWord(name, charset, null);
		this.personal = name;
	}// setPersonalName(String, String)
	
	/**
	 * Converts this address into a String in RFC2047 and RFC822 format.
	 *
	 * @return An encoded, mail-safe address <code>String</code>
	 */
	public String toString() {
		// TO DO
		return encodedPersonal + address;
	}// toString()
	

	// +----------------+-----------------------------------------
	// | Static Methods |
	// +----------------+

	/**
	 * Gets the email address for the local user. The address is attempted
	 * to be found in the following places (in order): in the "mail.from"
	 * property, the "mail.user" and "mail.host" properties are tried, and
	 * finally the "user.name" property and InetAddress.getLocalHost()
	 * methods are tried. All SecurityExceptions are ignored. If none of
	 * the above can produce a valid address, <code>null</code> is returned.
	 *
	 * @param session The current session.
	 */
	public static InternetAddress getLocalAddress(Session session) {
		String address = null;
		try {
			address = session.getProperty("mail.from");
		} catch (SecurityException e) {
			// IGNORE. The spec says so, that's why.
		}
		
		try {
			if (null == address)
				address = session.getProperty("mail.user") + session.getProperty("mail.host");
		} catch (SecurityException e) {
			// IGNORE. The spec says so, that's why.
		}
		// TO DO:I'm leaving out the InetAddress.getLocalHost() b/c I don't 
		// know where to find such an object.
		InternetAddress inetAddress = new InternetAddress();
		inetAddress.setAddress(address);
		return inetAddress;
	}// getLocalAddress(Session)

	/**
	 * Converts the array containing <code>InternetAddress</code>es into 
	 * a <code>String</code> in RFC822 and RFC2047 format. The resulting
	 * string is mail-safe and has LWSP inserted to ensure no line is longer
	 * than 76 characters long. Commas delimit each address from the other.
	 *
	 * @param addresses The <code>InternetAddress</code>es to be encoded.
	 * @exception Throws a <code>ClassCastException</code> if any of the 
	 * addresses in the list are not instances of <code>InternetAddress.</code>
	 *  Note that this is a <code>RuntimeException</code>.
	 */
	public static String toString(Address[] addresses) {
		return InternetAddress.toString(addresses, 0);
	}// toString(Address[])

	/**
	 * Converts the array containing <code>InternetAddress</code>es into 
	 * a <code>String</code> in RFC822 and RFC2047 format. The resulting
	 * string is mail-safe and has LWSP inserted in (***SPECIFY***)
	 * Commas delimit each address from the other. The <code>used</code>
	 * parameter is used to tell how many characters have already been 
	 * used in order to determine the correct place to break lines.
	 * Regardless of the value of used, this will return a string like:
	 * <code>"Franklin D. Roosevelt" <fdr@whitehouse.gov>, "Bill Clinton" 
	 * <bubba@whitehouse.com>, "Al Gore" <al@theinternet.com> </code>
	 *
	 * @param addresses The <code>InternetAddress</code>es to be encoded.
	 * @param used The numbers of characters already used. 
	 * @exception Throws a <code>ClassCastException</code> if any of the 
	 * addresses in the list are not instances of <code>InternetAddress.</code>
	 * Note that this is a <code>RuntimeException</code>. 
	 */
	public static String toString(Address[] addresses, int used) {
		String addressString;
		String tmpAddressString;
		int lineLength = 76;
		
		// by the way, we might make a mistake once in a while breaking
		// the lines where the last address is makes the last line exactly
		// lineLength long. I think that the code is clearer and cleaner 
		// without dealing with that (trivial) condition as it only adds 
		// one line and doesn't break anything. -Joey
		
		// We don't want to add a comma for the first address.
		addressString = addresses[0].toString();
		used = addressString.length();
		
		for (int i = 0; i < addresses.length; i++) {
			tmpAddressString = addresses[i].toString();
			// It's three greater because of the ", "address"," addition.
			if (used + tmpAddressString.length() + 3 > lineLength) {
				addressString = addressString + "," + '\n' + tmpAddressString;
				used = tmpAddressString.length();
			} else {
				addressString = addressString + ", " + tmpAddressString;
				used = used + tmpAddressString.length() + 2;
			}
		}
		return addressString;
	}// toString(Address[], int)

	/**
	 * NYI
	 */
	public static InternetAddress[] parse(String addresslist)
		throws AddressException {
		// TO DO: Waiting on Tokenizer.
		return null;
	}// parse(String)
	
	/**
	 * NYI
	 */
	public static InternetAddress[] parse(String s, boolean strict)
		throws AddressException {
			// TO DO: 

		return null;
	}// parse(String, boolean)

}// InternetAddress
