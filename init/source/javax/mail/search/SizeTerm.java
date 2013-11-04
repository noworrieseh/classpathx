/********************************************************************
 * Copyright (c) Open Java Extensions, Andrew Selkirk  LGPL License *
 ********************************************************************/

package javax.mail.search;

// Imports
import javax.mail.*;

/**
 * Size Term.
 * @author	Andrew Selkirk
 * @version	1.0
 */
public final class SizeTerm extends IntegerComparisonTerm {

	//-------------------------------------------------------------
	// Initialization ---------------------------------------------
	//-------------------------------------------------------------

	/**
	 * Create a new Size Term.
	 * @param comparison Comparison type
	 * @param size Size to check
	 */
	public SizeTerm(int comparison, int size) {
		super(comparison, size);
	} // SizeTerm()


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
			return match(((Part) message).getSize());
		} catch (Exception e) {
		}
		return false;
	} // match()


} // SizeTerm
