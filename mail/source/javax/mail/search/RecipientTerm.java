/********************************************************************
 * Copyright (c) Open Java Extensions, Andrew Selkirk  LGPL License *
 ********************************************************************/

package javax.mail.search;

// Imports
import javax.mail.*;

/**
 * Recipient Term.
 * @author	Andrew Selkirk
 * @version	1.0
 */
public final class RecipientTerm extends AddressTerm {

	//-------------------------------------------------------------
	// Variables --------------------------------------------------
	//-------------------------------------------------------------

	/**
	 * Recipient Type.
	 */
	protected Message.RecipientType	type	= null;


	//-------------------------------------------------------------
	// Initialization ---------------------------------------------
	//-------------------------------------------------------------

	/**
	 * Create a new Recipient Term.
	 */
	public RecipientTerm(Message.RecipientType type, Address address) {
		super(address);
		this.type = type;
	} // RecipientTerm()


	//-------------------------------------------------------------
	// Methods ----------------------------------------------------
	//-------------------------------------------------------------

	/**
	 * Get recipient type.
	 * @returns Recipient type
	 */
	public Message.RecipientType getRecipientType() {
		return type;
	} // getRecipientType()

	/**
	 * Recipient Address comparison match.
	 * @param value Recipient address to check
	 * @returns true if match, false otherwise
	 */
	public boolean match(Message message) {

		// Variables
		Address[]	recipients;
		int		index;

		try {
			// Get From List
			recipients = message.getRecipients(type);

			// Check From List
			for (index = 0; index < recipients.length; index++) {
				if (match(recipients[index]) == true) {
					return true;
				} // if
			} // for

		} catch (Exception e) {
		} // try

		// Return Result
		return false;

	} // match()


} // RecipientTerm
