/********************************************************************
 * Copyright (c) Open Java Extensions, Andrew Selkirk  LGPL License *
 ********************************************************************/

package javax.mail;

/**
 * Message Removed Exception
 */
public class MessageRemovedException extends MessagingException {

	//-------------------------------------------------------------
	// Initialization ---------------------------------------------
	//-------------------------------------------------------------

	/**
	 * Create new method removed exception.
	 */
	public MessageRemovedException() {
		super();
	} // MessageRemovedException()

	/**
	 * Create new method removed exception with description.
	 * @param message Description
	 */
	public MessageRemovedException(String message) {
		super(message);
	} // MessageRemovedException()


} // MessageRemovedException
