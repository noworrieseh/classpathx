/********************************************************************
 * Copyright (c) Open Java Extensions, Andrew Selkirk  LGPL License *
 ********************************************************************/

package javax.mail;

/**
 * Authentication Failed Exception.
 */
public class AuthenticationFailedException extends MessagingException {

	//-------------------------------------------------------------
	// Initialization ---------------------------------------------
	//-------------------------------------------------------------

	/**
	 * Create new authentication failed exception with description.
	 * @param message Exception message
	 */
	public AuthenticationFailedException(String message) {
		super(message);
	} // AuthenticationFailedException()

	/**
	 * Create new authentication failed exception.
	 */
	public AuthenticationFailedException() {
		super();
	} // AuthenticationFailedException()


} // AuthenticationFailException
