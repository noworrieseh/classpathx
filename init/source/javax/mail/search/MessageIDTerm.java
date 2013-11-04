/********************************************************************
 * Copyright (c) Open Java Extensions, Andrew Selkirk  LGPL License *
 ********************************************************************/

package javax.mail.search;

// Imports
import javax.mail.*;

/**
 * Message ID Term.
 * @author	Andrew Selkirk
 * @version	1.0
 */
public final class MessageIDTerm extends StringTerm {

	//-------------------------------------------------------------
	// Initialization ---------------------------------------------
	//-------------------------------------------------------------

	/**
	 * Create a new Message ID Term.
	 * @param headerName Header name
	 * @param pattern Search pattern
	 */
	public MessageIDTerm(String messageID) {
		super(messageID);
	} // MessageIDTerm()


	//-------------------------------------------------------------
	// Methods ----------------------------------------------------
	//-------------------------------------------------------------

	/**
	 * Do Message match.
	 * @param Address Message to check
	 * @returns true if match, false otherwise
	 */
	public boolean match(Message message) {

		// Variables
		String[]	headerList;
		int			index;

		try {

			// Get List of Headers
			headerList = message.getHeader("Message-ID");

			// Process Each Header
			for (index = 0; index < headerList.length; index++) {
				if (match(headerList[index]) == true) {
					return true;
				} // if
			} // for

		} catch (MessagingException e) {
		} // try

		// Unable to Locate Pattern
		return false;

	} // match()


} // MessageIDTerm
