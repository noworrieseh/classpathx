/********************************************************************
 * Copyright (c) Open Java Extensions, Andrew Selkirk  LGPL License *
 ********************************************************************/

package javax.mail.search;

// Imports
import javax.mail.Message;

/**
 * Or Term.
 * @author	Andrew Selkirk
 * @version	1.0
 */
public final class OrTerm extends SearchTerm {

	//-------------------------------------------------------------
	// Variables --------------------------------------------------
	//-------------------------------------------------------------

	/**
	 * List of terms.
	 */
	protected	SearchTerm[]	terms	= null;


	//-------------------------------------------------------------
	// Initialization ---------------------------------------------
	//-------------------------------------------------------------

	/**
	 * Create a new Search Term.
	 * @param term1 Term 1
	 * @param term2 Term 2
	 */
	public OrTerm(SearchTerm term1, SearchTerm term2) {
		terms = new SearchTerm[2];
		terms[0] = term1;
		terms[1] = term2;
	} // OrTerm()

	/**
	 * Create a new Search Term.
	 * @param terms List of terms
	 */
	public OrTerm(SearchTerm[] terms) {
		this.terms = terms;
	} // OrTerm()


	//-------------------------------------------------------------
	// Methods ----------------------------------------------------
	//-------------------------------------------------------------

	/**
	 * Get terms.
	 * @returns List of search terms
	 */
	public SearchTerm[] getTerms() {
		return terms;
	} // getTerms()

	/**
	 * Match Search Term.
	 * @param message Message to match
	 */
	public boolean match(Message message) {

		// Variables
		int	index;

		// Check each term
		for (index = 0; index < terms.length; index++) {
			if (terms[index].match(message) == true) {
				return true;
			} // if
		} // for

		// Return Result
		return false;

	} // match()


} // OrTerm
