/********************************************************************
 * Copyright (c) Open Java Extensions, Andrew Selkirk  LGPL License *
 ********************************************************************/

package javax.mail;

/**
 * Message Not Supported Exception
 */
public class MethodNotSupportedException extends MessagingException {

	//-------------------------------------------------------------
	// Initialization ---------------------------------------------
	//-------------------------------------------------------------

	/**
	 * Create new method not supported exception.
	 */
	public MethodNotSupportedException() {
		super();
	} // MethodNotSupportedException()

	/**
	 * Create new method not supported exception with description.
	 * @param message Description
	 */
	public MethodNotSupportedException(String message) {
		super(message);
	} // MethodNotSupportedException()


} // MethodNotSupportedException
