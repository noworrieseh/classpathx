/********************************************************************
 * Copyright (c) Open Java Extensions, Andrew Selkirk  LGPL License *
 ********************************************************************/

package javax.infobus;

/**
 * Duplicate Column Exception.
 */
public	class	DuplicateColumnException
	extends	Exception {

	//-------------------------------------------------------------
	// Initialization ---------------------------------------------
	//-------------------------------------------------------------

	/**
	 * Create Duplicate column exception with description.
	 * @param s Description string
	 */
	public DuplicateColumnException(String s) {
		super(s);
	} // DuplicateColumnException()


} // DuplicateColumnException
