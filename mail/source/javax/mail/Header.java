/********************************************************************
 * Copyright (c) Open Java Extensions, Andrew Selkirk  LGPL License *
 ********************************************************************/

package javax.mail;

/**
 * Header
 */
public abstract class Header {

	//-------------------------------------------------------------
	// Variables --------------------------------------------------
	//-------------------------------------------------------------

	/**
	 * Name
	 */
	private	String	name	= null;

	/**
	 * Value
	 */
	private	String	value	= null;


	//-------------------------------------------------------------
	// Initialization ---------------------------------------------
	//-------------------------------------------------------------

	/**
	 * Create new Header.
	 * @param name Header name
	 * @param value Header value
	 */
	public Header(String name, String value) {
		this.name = name;
		this.value = value;
	} // Header()


	//-------------------------------------------------------------
	// Public Accessor Methods ------------------------------------
	//-------------------------------------------------------------

	/**
	 * Get name.
	 * @returns Header name
	 */
	public String getName() {
		return name;
	} // getName()

	/**
	 * Get value.
	 * @returns Header value
	 */
	public String getValue() {
		return value;
	} // getValue()


} // Header
