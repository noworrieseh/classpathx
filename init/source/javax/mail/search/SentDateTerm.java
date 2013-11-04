/********************************************************************
 * Copyright (c) Open Java Extensions, Andrew Selkirk  LGPL License *
 ********************************************************************/

package javax.mail.search;

// Imports
import java.util.Date;
import javax.mail.*;

/**
 * Sent Date Term.
 * @author	Andrew Selkirk
 * @version	1.0
 */
public final class SentDateTerm extends DateTerm {

	//-------------------------------------------------------------
	// Initialization ---------------------------------------------
	//-------------------------------------------------------------

	/**
	 * Create a new Sent Date Term.
	 * @param comparison Comparison type
	 * @param date Date to check
	 */
	public SentDateTerm(int comparison, Date date) {
		super(comparison, date);
	} // SentDateTerm()


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
			return match(message.getSentDate());
		} catch (Exception e) {
		}
		return false;
	} // match()


} // SentDateTerm
