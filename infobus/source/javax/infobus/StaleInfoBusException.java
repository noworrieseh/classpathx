/********************************************************************
 * Copyright (c) Open Java Extensions, Andrew Selkirk  LGPL License *
 ********************************************************************/

package javax.infobus;

/**
 * Stale Infobus Exception.  Thrown when action performed on an
 * Infobus instance that is no longer active.
 */
public	class	StaleInfoBusException
	extends	RuntimeException {

	//-------------------------------------------------------------
	// Initialization ---------------------------------------------
	//-------------------------------------------------------------

	/**
	 * Create exception with description
	 * @param s Description string
	 */
	public StaleInfoBusException(String s) {
		super(s);
	} // StaleInfoBusException()


} // StaleInfoBusException
