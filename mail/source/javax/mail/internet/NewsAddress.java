/* NewsAddress.java */

/*
 * Liscense
 */

package javax.mail.internet;

import javax.mail.Address;

/**
 * Models a newsgroup address. 
 */
public class NewsAddress extends Address {

	// +--------+-----------------------------------------
	// | Fields |
	// +--------+

	/* The host used to access the newsgroup. Can be a domian name, not
	   just an IP address. (IS this true??)  */
	protected String host;
	
	/* The newsgroup. */
	protected String newsgroup;


	// +--------------+-------------------------------------
	// | Constructors |
	// +--------------+

	/** 
	 * Constructs the default object.
	 */
	public NewsAddress() {
	}// NewsAddress()

	/**
	 * Constructs a <code>NewsAddress</code> with the given newsgroup.
	 *	 
	 * @param newsgroup the (?fully qualified?) newgroup name. 
	 */
	public NewsAddress(String newsgroup) {
		this.newsgroup = newsgroup;
	}// NewsAddress(String)

	/**
	 * Constructs a <code>NewsAddress</code> with the given newsgroup and
	 * hostname.
	 *
	 * @param newsgroup The newsgroup's name
	 * @param host The hostname
	 */
	public NewsAddress(String newsgroup, String host) {
		this.newsgroup = newsgroup;
		this.host = host;
	}// NewsAddress(String, String)


	// +---------+-------------------------------------
	// | Methods |
	// +---------+
	
	/**
	 * Gets the type of this address. The type is "news"
	 *
	 * @return "news" (without the quotes)
	 */
	public String getType() {
		return "news";
	}// getType()

	/**
	 * Sets the newsgroup to the new value.
	 * 
	 * @param newsgroup the new newsgroup value.
	 */
	public void setNewsgroup(String newsgroup) {
		this.newsgroup = newsgroup;
	}// setNewsgroup(String)

	/**
	 * Gets the newsgroup. Is it fully qualified??
	 *
	 * @return the newsgroup.
	 */
	public String getNewsgroup() {
		return this.newsgroup;
	}// getNewsgroup()

	/**
	 * Sets the hostname.
	 *
	 * @param the hostname.
	 */
	public void setHost(String host) {
		this.host = host;
	}// setHost(String)

	/**
	 * Gets the hostname.
	 * 
	 * @return A <code>String</code> hostname.
	 */
	public String getHost() {
		return this.host;
	}// getHost()
	
	/**
	 * Converts this address into an RFC 1036-type <code>String</code>.
	 *
	 * @return The address as a string.
	 */
	public String toString() {
		// TO DO See RFC 1036 Although I can't quite get which address
		// they're referring to.
		return "";
	}// toString()

	/**
	 * This is the equality operator.  What?  You say it looks like a method
	 * to you?  Shhhh.  Java doesn't have operator overloading yet so I guess
	 * Sun just pretends.
	 *
	 * @param a The <code>Object</code> to compare to.
	 * @return true if equal, false otherwise.
	 */
	public boolean equals(Object a) {
		return false;
	}// equals(Object)

	/**
	 * The hashcode for this.
	 *
	 * @return the hashcode <code>int</code>
	 */
	public int hashcode() {
		return 0;
	}// hashcode()

	/**
	 *
	 *
	 */
	public static String toString(Address[] addresses) {
		String addressString = "";
		int i;

		// Add all but last 
		for (i = 0; i < addresses.length -1 ; i++)
			addressString.concat(addresses[i].toString() + ",");
		addressString.concat(addresses[i+1].toString());
		return addressString;
	}// toString(Address[])

	/**
	 *
	 */
	public static NewsAddress[] parse(String newsgroups) throws AddressException {
		//TO DO
		return null;
	}// parse(String)
	
}// NewsAddress

	
