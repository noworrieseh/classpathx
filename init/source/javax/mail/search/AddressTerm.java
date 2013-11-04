/********************************************************************
 * Copyright (c) Open Java Extensions, Andrew Selkirk  LGPL License *
 ********************************************************************/

package javax.mail.search;

// Imports
import javax.mail.*;

/**
 * Address Term.
 * @author	Andrew Selkirk
 * @version	1.0
 */
public abstract class AddressTerm extends SearchTerm {

	//-------------------------------------------------------------
	// Variables --------------------------------------------------
	//-------------------------------------------------------------

	/**
	 * Comparison address.
	 */
	protected	Address	address	= null;


	//-------------------------------------------------------------
	// Initialization ---------------------------------------------
	//-------------------------------------------------------------

	/**
	 * Create a new Address Term.
	 */
	public AddressTerm(Address address) {
		this.address = address;
	} // AddressTerm()


	//-------------------------------------------------------------
	// Methods ----------------------------------------------------
	//-------------------------------------------------------------

	/**
	 * Get address value.
	 * @returns Address value
	 */
	public Address getAddress() {
		return address;
	} // getAddress()

	/**
	 * Address comparison match.
	 * @param value Address to check
	 * @returns true if match, false otherwise
	 */
	protected boolean match(Address value) {
		if (address.equals(value) == true) {
			return true;
		}
		return false;
	} // match()


} // AddressTerm
