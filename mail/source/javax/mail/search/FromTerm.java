/********************************************************************
 * Copyright (c) Open Java Extensions, Andrew Selkirk  LGPL License *
 ********************************************************************/

package javax.mail.search;

// Imports
import javax.mail.*;

/**
 * From Term.
 * @author	Andrew Selkirk
 * @version	1.0
 */
public final class FromTerm extends AddressTerm {

	//-------------------------------------------------------------
	// Initialization ---------------------------------------------
	//-------------------------------------------------------------

	/**
	 * Create a new From Term.
	 */
	public FromTerm(Address address) {
		super(address);
	} // FromTerm()


	//-------------------------------------------------------------
	// Methods ----------------------------------------------------
	//-------------------------------------------------------------

	/**
	 * Address comparison match.
	 * @param value Address to check
	 * @returns true if match, false otherwise
	 */
	public boolean match(Message message) {

		// Variables
		Address[]	from;
		int		index;

		try {
			// Get From List
			from = message.getFrom();

			// Check From List
			for (index = 0; index < from.length; index++) {
				if (match(from[index]) == true) {
					return true;
				} // if
			} // for

		} catch (Exception e) {
		} // try

		// Return Result
		return false;

	} // match()


} // FromTerm
