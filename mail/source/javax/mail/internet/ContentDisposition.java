/********************************************************************
 * Copyright (c) Open Java Extensions, Andrew Selkirk  LGPL License *
 ********************************************************************/

package javax.mail.internet;

// Imports
import java.util.Vector;
import java.util.Enumeration;

/**
 * Content Disposition.  Used by MimeBodyPart.  Implementation of
 * RFC 2183.
 *
 * @author	Andrew Selkirk
 * @version	1.0
 */
class ContentDisposition {

	//-------------------------------------------------------------
	// Variables --------------------------------------------------
	//-------------------------------------------------------------

	/**
	 * Disposition type.  RFC describes inline and attachment.
	 */
	private	String			disposition	= "";

	/**
	 * List of associated parameters to the disposition.
	 */
	private ParameterList	list		= new ParameterList();


	//-------------------------------------------------------------
	// Initialization ---------------------------------------------
	//-------------------------------------------------------------

	/**
	 * Default Constructor.  Create an empty content disposition.
	 */
	public ContentDisposition() {
	} // ContentDisposition()

	/**
	 * Create a Content Disposition.
	 * @param disposition Disposition type
	 * @param list List of parameters
	 */
	public ContentDisposition(String disposition, ParameterList list) {
		this.disposition = disposition;
		this.list = list;
	} // ContentDisposition()

	/**
	 * Create Content Disposition object from a raw header.
	 * @param rawValue Raw header line
	 * @exception ParseException Error occurred in processing of header
	 */
	public ContentDisposition(String rawValue) throws ParseException {

		// Variables
		int				index;

		// Check for Mail Header (Do we need to check for this??)
		if (rawValue.startsWith("Content-Disposition:") == true) {
			rawValue = rawValue.substring(20);
		} // if

		// Check for seperator
		index = rawValue.indexOf(";");
		if (index != -1) {
			setDisposition(rawValue.substring(0, index));
			setParameterList(new ParameterList(rawValue.substring(index + 1)));
		} else {
			setDisposition(rawValue);
		} // if

	} // ContentDisposition()


	//-------------------------------------------------------------
	// Methods ----------------------------------------------------
	//-------------------------------------------------------------

	/**
	 * Get string representation of content disposition.
	 * @returns String
	 */
	public String toString() {
		return null; // TODO
	} // toString()

	/**
	 * Get content disposition type.
	 * @returns Content disposition type
	 */
	public String getDisposition() {
		return disposition;
	} // getDisposition()

	/**
	 * Get parameter value for the specified name.
	 * @param name Parameter name
	 * @returns Parameter value
	 */
	public String getParameter(String name) {
		return list.get(name);
	} // getParameter()

	/**
	 * Get list of parameters.
	 * @returns Parameter list
	 */
	public ParameterList getParameterList() {
		return list;
	} // getParameterList()

	/**
	 * Set the content disposition type.
	 * @param value Type of disposition
	 */
	public void setDisposition(String value) {
		disposition = value;
	} // setDisposition()

	/**
	 * Set a parameter name and value for the disposition.
	 * @param name Name of parameter
	 * @param value Value of parameter
	 */
	public void setParameter(String name, String value) {
		list.set(name, value);
	} // setParameter()

	/**
	 * Set the parameters of the disposition.
	 * @param list Parameter list
	 */
	public void setParameterList(ParameterList list) {
		this.list = list;
	} // setParameterList()


} // ContentDisposition
