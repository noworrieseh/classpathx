/********************************************************************
 * Copyright (c) Open Java Extensions, Andrew Selkirk  LGPL License *
 ********************************************************************/

package javax.mail.search;

// Imports
import javax.mail.Message;

/**
 * Search Term.
 * @author	Andrew Selkirk
 * @version	1.0
 */
public abstract class SearchTerm {

	//-------------------------------------------------------------
	// Initialization ---------------------------------------------
	//-------------------------------------------------------------

	/**
	 * Create a new Search Term.
	 */
	public SearchTerm() {
	} // SearchTerm()


	//-------------------------------------------------------------
	// Methods ----------------------------------------------------
	//-------------------------------------------------------------

	/**
	 * Match Search Term.
	 * @param message Message to match
	 */
	public abstract boolean match(Message message);


} // SearchTerm
