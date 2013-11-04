/********************************************************************
 * Copyright (c) Open Java Extensions, Andrew Selkirk  LGPL License *
 ********************************************************************/

package javax.mail.search;

// Imports
import javax.mail.*;

/**
 * Body Term.
 * @author	Andrew Selkirk
 * @version	1.0
 */
public final class BodyTerm extends StringTerm {

	//-------------------------------------------------------------
	// Initialization ---------------------------------------------
	//-------------------------------------------------------------

	/**
	 * Create a new Body Term.
	 * @param pattern Search pattern
	 */
	public BodyTerm(String pattern) {
		super(pattern, true);
	} // BodyTerm()


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
		Object		content;
		String		text;
		Multipart	multi;

		try {

			// Get Message Content
			content = message.getContent();

			// Check Content
			if (content instanceof String) {
				text = (String) content;
			} else if (content instanceof Multipart) {
				multi = (Multipart) content;
				if (multi.getBodyPart(0).getContent() instanceof String) {
					text = (String) multi.getBodyPart(0).getContent();
				} else {
					return false;
				}
			} else {
				return false;
			} // if

		} catch (Exception e) {
			return false;
		} // try

		// Do Match
		return match(text);

	} // match()


} // BodyTerm
