/********************************************************************
 * Copyright (c) Open Java Extensions, Andrew Selkirk  LGPL License *
 ********************************************************************/

package javax.infobus;

/**
 * Column Not Found exception.
 */
public	class	ColumnNotFoundException
	extends	Exception {

	//-------------------------------------------------------------
	// Initialization ---------------------------------------------
	//-------------------------------------------------------------

	/**
	 * Create columns not found exception with description.
	 * @param s Description string
	 */
	public ColumnNotFoundException(String s) {
		super(s);
	} // ColumnNotFoundException()


} // ColumnNotFoundException
