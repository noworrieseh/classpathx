/********************************************************************
 * Copyright (c) Open Java Extensions, Andrew Selkirk  LGPL License *
 ********************************************************************/

package javax.mail.search;

// Imports
import javax.mail.*;

/**
 * Recipient String Term.
 * @author	Andrew Selkirk
 * @version	1.0
 */
public final class RecipientStringTerm extends AddressStringTerm {

	//-------------------------------------------------------------
	// Variables --------------------------------------------------
	//-------------------------------------------------------------

	private	Message.RecipientType	type;


	//-------------------------------------------------------------
	// Initialization ---------------------------------------------
	//-------------------------------------------------------------

	/**
	 * Create a new Recipient String Term.
	 * @param pattern Search pattern
	 */
	public RecipientStringTerm(Message.RecipientType type, String pattern) {
		super(pattern);
		this.type = type;
	} // RecipientStringTerm()


	//-------------------------------------------------------------
	// Methods ----------------------------------------------------
	//-------------------------------------------------------------

	public Message.RecipientType getRecipientType() {
		return type;
	} // getRecipientType()

	/**
	 * Do string match.
	 * @param Address Address to check
	 * @returns true if match, false otherwise
	 */
	public boolean match(Message message) {

		// Variables
		Address[]	addressList;
		int			index;

		try {

			// Get List of Addresses
			addressList = message.getRecipients(type);

			// Process Each Address
			for (index = 0; index < addressList.length; index++) {
				if (match(addressList[index]) == true) {
					return true;
				} // if
			} // for

		} catch (MessagingException e) {
		} // try

		// Unable to Locate Pattern
		return false;

	} // match()


} // RecipientStringTerm
