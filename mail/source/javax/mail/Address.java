/********************************************************************
 * Copyright (c) Open Java Extensions, Andrew Selkirk  LGPL License *
 ********************************************************************/

package javax.mail;

/**
 * Address
 */
public abstract class Address {

	//-------------------------------------------------------------
	// Initialization ---------------------------------------------
	//-------------------------------------------------------------

	/**
	 * Create new Address.
	 */
	public Address() {
	} // Address()


	//-------------------------------------------------------------
	// Public Accessor Methods ------------------------------------
	//-------------------------------------------------------------

	/**
	 * Check Equality.
	 * @param object Object to compare against
	 * @returns true if equal, false otherwise
	 */
	public abstract boolean equals(Object object);

	/**
	 * Get string representation of Address.
	 * @returns String
	 */
	public abstract String toString();

	/**
	 * Get address type.
	 * @returns Address type
	 */
	public abstract String getType();


} // Address
