/********************************************************************
 * Copyright (c) Open Java Extensions, Andrew Selkirk  LGPL License *
 ********************************************************************/

package javax.mail.search;

// Imports
import javax.mail.*;

/**
 * From String Term.
 * @author	Andrew Selkirk
 * @version	1.0
 */
public final class FromStringTerm extends AddressStringTerm {

	//-------------------------------------------------------------
	// Initialization ---------------------------------------------
	//-------------------------------------------------------------

	/**
	 * Create a new From String Term.
	 * @param pattern Search pattern
	 */
	public FromStringTerm(String pattern) {
		super(pattern);
	} // FromStringTerm()


	//-------------------------------------------------------------
	// Methods ----------------------------------------------------
	//-------------------------------------------------------------

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
			addressList = message.getFrom();

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


} // FromStringTerm
