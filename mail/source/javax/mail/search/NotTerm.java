/********************************************************************
 * Copyright (c) Open Java Extensions, Andrew Selkirk  LGPL License *
 ********************************************************************/

package javax.mail.search;

// Imports
import javax.mail.Message;

/**
 * Not Term.
 * @author	Andrew Selkirk
 * @version	1.0
 */
public final class NotTerm extends SearchTerm {

	//-------------------------------------------------------------
	// Variables --------------------------------------------------
	//-------------------------------------------------------------

	/**
	 * Term to negate.
	 */
	protected	SearchTerm	term	= null;


	//-------------------------------------------------------------
	// Initialization ---------------------------------------------
	//-------------------------------------------------------------

	/**
	 * Create a new Search Term.
	 * @param term Term to negate
	 */
	public NotTerm(SearchTerm term) {
		this.term = term;
	} // NotTerm()


	//-------------------------------------------------------------
	// Methods ----------------------------------------------------
	//-------------------------------------------------------------

	/**
	 * Get term to negate.
	 * @returns Search term
	 */
	public SearchTerm getTerm() {
		return term;
	} // getTerm()

	/**
	 * Match Search Term.
	 * @param message Message to match
	 */
	public boolean match(Message message) {
		if (term.match(message) == true) {
			return false;
		}
		return true;
	} // match()


} // NotTerm
