/********************************************************************
 * Copyright (c) Open Java Extensions, Andrew Selkirk  LGPL License *
 ********************************************************************/

package javax.mail.search;

// Imports
import javax.mail.*;

/**
 * Message Number Term.
 * @author	Andrew Selkirk
 * @version	1.0
 */
public final class MessageNumberTerm extends IntegerComparisonTerm {

	//-------------------------------------------------------------
	// Initialization ---------------------------------------------
	//-------------------------------------------------------------

	/**
	 * Create a new Message Number Term.
	 * @param number Message number to check
	 */
	public MessageNumberTerm(int number) {
		super(EQ, number);
	} // MessageNumberTerm()


	//-------------------------------------------------------------
	// Methods ----------------------------------------------------
	//-------------------------------------------------------------

	/**
	 * Match search term
	 * @param message Message to match
	 * @returns true if match, false otherwise
	 */
	public boolean match(Message message) {
		return match(message.getMessageNumber());
	} // match()


} // MessageNumberTerm
