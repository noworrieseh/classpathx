/********************************************************************
 * Copyright (c) Open Java Extensions, Andrew Selkirk  LGPL License *
 ********************************************************************/

package javax.mail.search;

// Imports
import java.util.Date;
import javax.mail.*;

/**
 * Received Date Term.
 * @author	Andrew Selkirk
 * @version	1.0
 */
public final class ReceivedDateTerm extends DateTerm {

	//-------------------------------------------------------------
	// Initialization ---------------------------------------------
	//-------------------------------------------------------------

	/**
	 * Create a new Received Date Term.
	 * @param comparison Comparison type
	 * @param date Date to check
	 */
	public ReceivedDateTerm(int comparison, Date date) {
		super(comparison, date);
	} // ReceivedDateTerm()


	//-------------------------------------------------------------
	// Methods ----------------------------------------------------
	//-------------------------------------------------------------

	/**
	 * Match search term
	 * @param message Message to match
	 * @returns true if match, false otherwise
	 */
	public boolean match(Message message) {
		try {
			return match(message.getReceivedDate());
		} catch (Exception e) {
		}
		return false;
	} // match()


} // ReceivedDateTerm
