/********************************************************************
 * Copyright (c) Open Java Extensions, Andrew Selkirk  LGPL License *
 ********************************************************************/

package javax.mail.search;

// Imports
import javax.mail.Message;

/**
 * String Term.
 * @author	Andrew Selkirk
 * @version	1.0
 */
public abstract class StringTerm extends SearchTerm {

	//-------------------------------------------------------------
	// Variables --------------------------------------------------
	//-------------------------------------------------------------

	/**
	 * Search pattern.
	 */
	protected	String	pattern		= null;

	/**
	 * Case should be ignored in match.
	 */
	protected	boolean	ignoreCase	= false;


	//-------------------------------------------------------------
	// Initialization ---------------------------------------------
	//-------------------------------------------------------------

	/**
	 * Create a new String Term.
	 * @param pattern Search pattern
	 */
	public StringTerm(String pattern) {
		this.pattern = pattern;
	} // StringTerm()

	/**
	 * Create a new String Term.
	 * @param pattern Search pattern
	 * @param ignoreCase Ignore case
	 */
	public StringTerm(String pattern, boolean ignoreCase) {
		this.pattern = pattern;
		this.ignoreCase = ignoreCase;
	} // StringTerm()


	//-------------------------------------------------------------
	// Methods ----------------------------------------------------
	//-------------------------------------------------------------

	/**
	 * Get ignore case.
	 * @returns true if case ignored, false otherwise
	 */
	public boolean getIgnoreCase() {
		return ignoreCase;
	} // getIgnoreCase()

	/**
	 * Get search pattern.
	 * @returns Search pattern
	 */
	public String getPattern() {
		return pattern;
	} // getPattern()

	/**
	 * Do string match.
	 * @param str String to check
	 * @returns true if match, false otherwise
	 */
	protected boolean match(String str) {
		if (ignoreCase == true) {
			if (str.toLowerCase().indexOf(pattern.toLowerCase()) != -1) {
				return true;
			}
		} else {
			if (str.indexOf(pattern) != -1) {
				return true;
			}
		}
		return false;
	} // match()


} // StringTerm
