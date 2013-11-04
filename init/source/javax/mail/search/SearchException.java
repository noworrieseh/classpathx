/********************************************************************
 * Copyright (c) Open Java Extensions, Andrew Selkirk  LGPL License *
 ********************************************************************/

package javax.mail;

// Imports
import javax.mail.MessagingException;

/**
 * Search Exception.
 * @author	Andrew Selkirk
 * @version	1.0
 */
public class SearchException extends MessagingException {

	//-------------------------------------------------------------
	// Initialization ---------------------------------------------
	//-------------------------------------------------------------

	/**
	 * Create new search exception.
	 */
	public SearchException() {
	} // SearchException()

	/**
	 * Create new search exception with description.
	 * @param message Description
	 */
	public SearchException(String message) {
		super(message);
	} // SearchException()


} // SearchException
