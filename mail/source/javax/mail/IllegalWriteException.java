/********************************************************************
 * Copyright (c) Open Java Extensions, Andrew Selkirk  LGPL License *
 ********************************************************************/

package javax.mail;

/**
 * Illegal Write Exception
 */
public class IllegalWriteException extends MessagingException {

	//-------------------------------------------------------------
	// Initialization ---------------------------------------------
	//-------------------------------------------------------------

	/**
	 * Create new illegal write exception.
	 */
	public IllegalWriteException() {
		super();
	} // IllegalWriteException()

	/**
	 * Create new illegal write exception with description.
	 * @param message Description
	 */
	public IllegalWriteException(String message) {
		super(message);
	} // IllegalWriteException()


} // IllegalWriteException
