/********************************************************************
 * Copyright (c) Open Java Extensions, Andrew Selkirk  LGPL License *
 ********************************************************************/

package javax.mail.search;

// Imports
import javax.mail.Address;

/**
 * Address String Term.
 * @author	Andrew Selkirk
 * @version	1.0
 */
public abstract class AddressStringTerm extends StringTerm {

	//-------------------------------------------------------------
	// Initialization ---------------------------------------------
	//-------------------------------------------------------------

	/**
	 * Create a new Address String Term.
	 * @param pattern Search pattern
	 */
	protected AddressStringTerm(String pattern) {
		super(pattern);
	} // AddressStringTerm()


	//-------------------------------------------------------------
	// Methods ----------------------------------------------------
	//-------------------------------------------------------------

	/**
	 * Do string match.
	 * @param Address Address to check
	 * @returns true if match, false otherwise
	 */
	protected boolean match(Address address) {
		return super.match(address.toString());
	} // match()


} // AddressStringTerm
