/********************************************************************
 * Copyright (c) Open Java Extensions, Andrew Selkirk  LGPL License *
 ********************************************************************/

package javax.mail.search;

// Imports
import javax.mail.*;

/**
 * Subject Term.
 * @author	Andrew Selkirk
 * @version	1.0
 */
public final class SubjectTerm extends StringTerm {

	//-------------------------------------------------------------
	// Initialization ---------------------------------------------
	//-------------------------------------------------------------

	/**
	 * Create a new Subject Term.
	 * @param pattern Search pattern
	 */
	public SubjectTerm(String pattern) {
		super(pattern);
	} // SubjectTerm()


	//-------------------------------------------------------------
	// Methods ----------------------------------------------------
	//-------------------------------------------------------------

	/**
	 * Do string match.
	 * @param Message Message to check
	 * @returns true if match, false otherwise
	 */
	public boolean match(Message message) {
		try {
			return super.match(message.getSubject());
		} catch (MessagingException e) {
			return false;
		} // try
	} // match()


} // SubjectTerm
