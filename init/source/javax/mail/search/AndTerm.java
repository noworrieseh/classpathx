/********************************************************************
 * Copyright (c) Open Java Extensions, Andrew Selkirk  LGPL License *
 ********************************************************************/

package javax.mail.search;

// Imports
import javax.mail.Message;

/**
 * And Term.
 * @author	Andrew Selkirk
 * @version	1.0
 */
public final class AndTerm extends SearchTerm {

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
	public AndTerm(SearchTerm term1, SearchTerm term2) {
		terms = new SearchTerm[2];
		terms[0] = term1;
		terms[1] = term2;
	} // AndTerm()

	/**
	 * Create a new Search Term.
	 * @param terms List of terms
	 */
	public AndTerm(SearchTerm[] terms) {
		this.terms = terms;
	} // AndTerm()


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
			if (terms[index].match(message) == false) {
				return false;
			} // if
		} // for

		// Valid
		return true;

	} // match()


} // AndTerm
