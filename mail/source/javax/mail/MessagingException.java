/********************************************************************
 * Copyright (c) Open Java Extensions, Andrew Selkirk  LGPL License *
 ********************************************************************/

package javax.mail;

/**
 * Messaging Exception.
 * @author	Andrew Selkirk
 * @version	1.0
 */
public class MessagingException extends Exception {

	//-------------------------------------------------------------
	// Variables --------------------------------------------------
	//-------------------------------------------------------------

	/**
	 * Next exception
	 */
	private	Exception	next	= null;


	//-------------------------------------------------------------
	// Initialization ---------------------------------------------
	//-------------------------------------------------------------

	/**
	 * Create new messaging exception.
	 */
	public MessagingException() {
	} // MessagingException()

	/**
	 * Create new messaging exception with description.
	 * @param message Description
	 */
	public MessagingException(String message) {
		super(message);
	} // MessagingException()

	/**
	 * Create new messaging exception with description.
	 * @param message Description
	 * @param exception Next exception
	 */
	public MessagingException(String message, Exception exception) {
		super(message);
		next = exception;
	} // MessagingException()


	//-------------------------------------------------------------
	// Public Accessor Methods ------------------------------------
	//-------------------------------------------------------------

	/**
	 * Get message.
	 * @returns Message
	 */
	public String getMessage() {
		return super.getMessage();
	} // getMessage()

	/**
	 * Get next exception.
	 * @returns Next exception
	 */
	public Exception getNextException() {
		return next;
	} // getNextException()

	/**
	 * Set next exception.
	 * @param exception Exception to set
	 */
	public synchronized void setNextException(Exception exception) {
		next = exception;
	} // setNextException()


} // MessagingException
