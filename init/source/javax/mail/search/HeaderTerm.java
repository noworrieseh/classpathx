/********************************************************************
 * Copyright (c) Open Java Extensions, Andrew Selkirk  LGPL License *
 ********************************************************************/

package javax.mail.search;

// Imports
import javax.mail.*;

/**
 * Header Term.
 * @author	Andrew Selkirk
 * @version	1.0
 */
public final class HeaderTerm extends StringTerm {

	//-------------------------------------------------------------
	// Variables --------------------------------------------------
	//-------------------------------------------------------------

	private	String	headerName;


	//-------------------------------------------------------------
	// Initialization ---------------------------------------------
	//-------------------------------------------------------------

	/**
	 * Create a new Header Term.
	 * @param headerName Header name
	 * @param pattern Search pattern
	 */
	public HeaderTerm(String headerName, String pattern) {
		super(pattern, true);
		this.headerName = headerName;
	} // HeaderTerm()


	//-------------------------------------------------------------
	// Methods ----------------------------------------------------
	//-------------------------------------------------------------

	public String getHeaderName() {
		return headerName;
	} // getHeaderName()

	/**
	 * Do Message match.
	 * @param Address Message to check
	 * @returns true if match, false otherwise
	 */
	public boolean match(Message message) {

		// Variables
		String[]	headerList;
		int			index;

		try {

			// Get List of Headers
			headerList = message.getHeader(headerName);

			// Process Each Header
			for (index = 0; index < headerList.length; index++) {
				if (match(headerList[index]) == true) {
					return true;
				} // if
			} // for

		} catch (MessagingException e) {
		} // try

		// Unable to Locate Pattern
		return false;

	} // match()


} // HeaderTerm
