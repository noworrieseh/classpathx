/********************************************************************
 * Copyright (c) Open Java Extensions, Andrew Selkirk  LGPL License *
 ********************************************************************/

package javax.mail.internet;

// Imports
import java.util.Vector;
import java.util.Enumeration;

/**
 * matchEnum that generates an enumeration of hdr objects/lines
 * based on the specified criteria.  Used by InternetHeaders.
 *
 * @author	Andrew Selkirk
 * @version	1.0
 */
class matchEnum implements Enumeration {

	//-------------------------------------------------------------
	// Variables --------------------------------------------------
	//-------------------------------------------------------------

	/**
	 * Enumeration of all header objects.
	 */
	private	Enumeration	e			= null;

	/**
	 * List of header names to match.
	 */
	private	String[]	names		= null;

	/**
	 * Vector form of names array.
	 */
	private	Vector		nameList	= null;

	/**
	 * Whether retsults should (or should not) match a header
	 * name in the header names list.
	 */
	private	boolean		match		= false;

	/**
	 * Whether the enumeration results should be header objects
	 * or header lines.
	 */
	private	boolean		want_line	= false;

	/**
	 * Push-back header object that is produced when determining
	 * if there are any more matching headers in the enumeration.
	 */
	private	hdr			next_header	= null;


	//-------------------------------------------------------------
	// Initialization ---------------------------------------------
	//-------------------------------------------------------------

	/**
	 * Construct matching enumeration.
	 * @param headers Vector of header objects to search
	 * @param names Array of names to match
	 * @param matchHeaders if true, checks for existence of header
	 * name in header names list, otherwise, check for non-existence
	 * @param headerLines Whether enumeration objects should be header
	 * objects or header lines.
	 */
	protected matchEnum(Vector headers, String[] names,
						boolean matchHeaders, boolean headerLines) {
		this.e = headers.elements();
		this.names = names;
		this.match = matchHeaders;
		this.want_line = headerLines;

		// Optimize Name List into Vector
		if (names != null) {
			nameList = new Vector();
			for (int index = 0; index < names.length; index++) {
				nameList.addElement(names[index]);
			} // for
		} // if

	} // matchEnum()


	//-------------------------------------------------------------
	// Method -----------------------------------------------------
	//-------------------------------------------------------------

	/**
	 * Get the next element in enumeration.
	 * @returns Next element
	 */
	public Object nextElement() {

		// Variables
		hdr		next;

		// Get Next Header
		next = nextMatch();

		// Check for end of enumeration
		if (next == null) {
			return null;
		} // if

		// Check Line mode
		if (want_line == true) {
			return next.getName() + ": " + next.getValue();
		} else {
			return next;
		} // if

	} // nextElement()

	/**
	 * Check if there are any further matches in the enumeration.
	 * @returns true if there are more elements, false otherwise
	 */
	public boolean hasMoreElements() {

		// Check for Next Header
		if (next_header != null) {
			return true;
		} // if

		// Check Enumeration for another match
		next_header = nextMatch();
		if (next_header != null) {
			return true;
		} else {
			return false;
		} // if

	} // hasMoreElements()

	/**
	 * Get next matching header.
	 * @returns Matching header
	 */
	private hdr nextMatch() {

		// Variables
		hdr		next;
		int		index;
		boolean	checkMatch;

		// Check for next_header
		if (next_header != null) {
			next = next_header;
			next_header = null;
			return next;
		} // if

		// Process Each element in Enumeration
		while (e.hasMoreElements() == true) {

			// Process Next Header
			next = (hdr) e.nextElement();

			// Check for Match All
			if (names == null) {
				return next;
			} // if

			// Check Match
			checkMatch = nameList.contains(next.getName());

			// Check Match Mode
			if ((checkMatch == true && match == true) ||
				(checkMatch == false && match == false)) {
				return next;
			} // if

		} // while

		// Unable to Locate Next Match
		return null;

	} // nextMatch()


} // matchEnum
