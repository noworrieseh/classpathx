/********************************************************************
 * Copyright (c) Open Java Extensions, Andrew Selkirk  LGPL License *
 ********************************************************************/

package javax.infobus;

/**
 * Unsupported operation exception
 */
public	class	UnsupportedOperationException
	extends	RuntimeException {

	//-------------------------------------------------------------
	// Initialization ---------------------------------------------
	//-------------------------------------------------------------

	/**
	 * Create exception with description.
	 * @param s Description string
	 */
	public UnsupportedOperationException(String s) {
		super(s);
	} // UnsupportedOperationException()


} // UnsupportedOperationException
