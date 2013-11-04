/********************************************************************
 * Copyright (c) Open Java Extensions, Andrew Selkirk  LGPL License *
 ********************************************************************/

package javax.mail;

/**
 * No Such Provider Exception
 */
public class NoSuchProviderException extends MessagingException {

	//-------------------------------------------------------------
	// Initialization ---------------------------------------------
	//-------------------------------------------------------------

	/**
	 * Create new no such provider exception.
	 */
	public NoSuchProviderException() {
		super();
	} // NoSuchProviderException()

	/**
	 * Create new no such provider exception with description.
	 * @param message Description
	 */
	public NoSuchProviderException(String message) {
		super(message);
	} // NoSuchProviderException()


} // NoSuchProviderException
