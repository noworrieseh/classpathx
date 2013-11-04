/********************************************************************
 * Copyright (c) Open Java Extensions, LGPL License                 *
 ********************************************************************/

package javax.mail.internet;

// Imports
import java.util.Hashtable;
import java.util.Enumeration;
import java.util.StringTokenizer;

/**
 * Provides a holder for MIME parameters. Name-value pairs can be
 * stored and retrieved.
 *
 * @author Joey Lesh
 * @author Andrew Selkirk
 */
public class ParameterList {

	/* ============================================================= *
	 * Developer Notes:  Interesting that Sun chose to re-implement  *
	 * this class from JavaBeans Activation Framework.  The class is *
	 * almost an exact duplicate of MimeTypesParameterList.  This    *
	 * implementation has been copied from the OJE implementation of *
	 * JAF.  Sun did leave out the token checking which we may want  *
	 * to include in the future for proper error-checking.           *
	 * ============================================================= */

	//-------------------------------------------------------------
	// Variables --------------------------------------------------
	//-------------------------------------------------------------

	/**
	 * Internal parameter listing stored as a dictionary.
	 * Mapping is name->value.
	 */
	private Hashtable parameters = new Hashtable();


	//-------------------------------------------------------------
	// Initialization ---------------------------------------------
	//-------------------------------------------------------------

	/**
	 * Default Constructor. Creates an empty parameter list.
	 */
	public ParameterList() {
	}// ParameterList()

	/**
	 * Constructs a new <code>ParameterList</code> from name-value
	 * pairs parsed out of the string. The pairs should be in MIME
	 * format.
	 *
	 * @param mimeParameters The <code>String</code> containing MIME
	 *        formatted name-value pairs. Can be null.
	 * @exception An exception if the parsing fails.
	 */
	public ParameterList(String mimeParameters) throws ParseException {

		// Variables
		StringTokenizer	tokens;
		String			parameter;
		String			name;
		String			value;
		int				index;

		// Create Tokenizer
		tokens = new StringTokenizer(mimeParameters, ";");

		// Load Each Parameter
		while (tokens.hasMoreTokens() == true) {
			parameter = tokens.nextToken();
			index = parameter.indexOf("=");
			if (index != parameter.lastIndexOf("=")) {
				throw new ParseException("multiple =");
			}
			name = parameter.substring(0, index).trim();
			value = unquote(parameter.substring(index + 1).trim());

			// Add to Parameter List
			set(name, value);

		} // while())

	} // ParameterList()


	//-------------------------------------------------------------
	// Methods ----------------------------------------------------
	//-------------------------------------------------------------

	/**
	 * Gets the value associated with the name given.
	 *
	 * @return The <code>String</code> value.
	 */
	public String get(String name) {
		return (String) parameters.get(name);
	} // get()

	/**
	 * Gets an <code>java.util.Enumeration</code> of the names in
	 * the ParameterList. The Objects in the Enumeration are Strings.
	 *
	 * @return An <code>Enumeration</code> of the names in the list.
	 */
	public Enumeration getNames() {
		return parameters.keys();
	} // getNames()

	/**
	 * Removes a name-value pair from the <code>ParameterList</code>. Does
	 * nothing if the parameter is not present.
	 *
	 * @param name The name of the name-value pair to be removed from the list.
	 */
	public void remove(String name) {
		parameters.remove(name);
	} // remove()

	/**
	 * Adds a parameter to the list. If it already exists, the old value
	 * is replaced by the new one.
	 *
	 * @param name The name of the parameter.
	 * @param value The value of the parameter.
	 */
	public void set(String name, String value) {
		parameters.put(name, value);
	} // set()

	/**
	 * Gets the number of parameters in <code>this</code>.
	 *
	 * @return the number of parameters.
	 */
	public int size() {
		return parameters.size();
	} // size()

	/**
	 * Converts the list into a String of name-value pairs in MIME format.
	 * Like "name=value".
	 *
	 * @return a <code>String</code> representation of this list.
	 */
	public String toString() {

		// Variables
		Enumeration		enum;
		StringBuffer	buffer;
		String			name;
		String			value;

		// Get Names
		enum = getNames();

		// Process Each Name
		buffer = new StringBuffer();
		while (enum.hasMoreElements() == true) {

			// Get Parameter
			name = (String) enum.nextElement();
			value = get(name);

			// Append to Result
			buffer.append(name);
			buffer.append("=");
			buffer.append(quote(value));

			// Check for Seperator
			if (enum.hasMoreElements() == true) {
				buffer.append("; ");
			}

		} // while

		// Return Result
		return buffer.toString();

	} // toString()

	/**
	 * Quote value.
	 * @param value String to quote
	 * @returns Quoted string
	 */
	private String quote(String value) {

		// Check for starting quote
		if (value.startsWith("\"") == false) {
			value = "\"" + value;
		} // if

		// Check for ending quote
		if (value.endsWith("\"") == false) {
			value = value + "\"";
		} // if

		return value;

	} // quote()

	/**
	 * Remove quotes from string.
	 * @param value Quoted string
	 * @returns Unquoted string
	 */
	private String unquote(String value) {

		// Check for Starting quote
		if (value.startsWith("\"") == true) {
			value = value.substring(1);
		} // if

		// Check for Ending quote
		if (value.endsWith("\"") == true) {
			value = value.substring(0, value.length() - 1);
		} // if

		return value;

	} // unquote()


} // ParameterList
