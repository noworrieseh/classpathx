/* ContentType.java */    

/* Liscense goes here. */

package javax.mail.internet;

import javax.mail.internet.ParameterList;

/**
 * This class represents a MIME content type value.
 *
 * @author Joey Lesh
 */
public class ContentType {
	
	// +--------+-------------------------------------------
	// | Fields | of Dreams
	// +--------+

	private ParameterList params = null;

	private String primaryType = "text";
	
	private String subType = "plain";

	// +--------------+----------------------------------------
	// | Constructors |
	// +--------------+

	/**
	 * Standard, no-argument constructor. Defaults with type "text/plain"
	 * and no parameters.
	 */
	public ContentType() {

	}// ContentType()
	
	/**
	 * Creates a "Content-Type" header by parsing the string.
	 *
	 * @param s The string to parse into a content-type.
	 */
	public ContentType(String s) throws ParseException {

		// TO DO:  Waiting for Dave to do the HeaderTokenizer
	}// ContentType(String)
	
	/**
	 * Constructs a <code>ContentType</code> object with the specified
	 * types and parameters.
	 *
	 * @param primaryType The primary type of the content.
	 * @param subType The subtype of the content.
	 * @param list The parameters associated with the value.
	 */
	public ContentType(String primaryType, String subType, ParameterList list) {
		this.primaryType = primaryType;
		this.subType = subType;
		this.params = list;
	}// ContentType(String, String, ParameterList)

	// +----------------+--------------------------------------
	// | Public Methods |
	// +----------------+

	/**
	 * Returns a MIME <code>String</code> without the parameters.
	 *
	 * @return A string of the type.
	 */
	public String getBaseType() {
		return this.primaryType + "/" + this.subType;
	}// getBaseType()
	
	/**
	 * Gets the value of the parameter named by <code>name</code>.
	 *
	 * @return The parameter's value.
	 */
	public String getParameter(String name) {
		return params.get(name);
	}// getParameter(String)
	

	/**
	 * Returns the <code>ParameterList</code> containing
	 * the values of the parameters keyed by the parameter names.
	 *
	 * @return The parameters.
	 */
	public ParameterList getParameterList() {
		return this.params;
	}// getParameterList()
	
	/**
	 * Returns the primary type of this "Content-Type" header.
	 *
	 * @return The primary type. 
	 */
	public String getPrimaryType() {
		return this.primaryType;
	}// getPrimaryType()
	
	/**
	 * Returns the subtype of this "Content-Type" header.
	 *
	 * @return The subtype. 
	 */
	public String getSubType() {
		return this.subType;
	}// getSubType()
	
	/**
	 * Matches the <code>ContentType</code> to <code>this</code>.  A match
	 * occurs when the base and sub types of each are the same (ignoring
	 * case). Note that the subtype to match can be the special "*" which
	 * any subtype will match.
	 *
	 * @param cType The object to match against.
	 * @return true if they match.  
	 */
	public  boolean match(ContentType cType) {
		if (cType.getBaseType().equalsIgnoreCase(this.getBaseType()))
			if (cType.getSubType().equalsIgnoreCase(this.getSubType())
				|| cType.getSubType().equals("*"))
				return true;
		return false;
	}// match(ContentType)
	
	/**
	 * Matches by comparing the type part only, ignoring case. Also, a 
	 * subtype of the special star, "*", will match any subtype. For example:
	 * <pre>
	 * text/plain; foo=bar
	 *      and
	 * TEXT/*
     * </pre>
	 * are matches.
	 *
	 * @param s The <code>String</code> to match against.
	 * @return <code>true</code> if they match.  
	 */
 	public boolean match(String s) {
		return (new ContentType(s)).match(this);
	} //match(boolean)

	/**
	 * Adds or replaces the parameter named <code>name</code>
	 * with associated value <code>value</code>
	 *
	 * @param name The parameter's name.
	 * @param value The parameter's value.
	 */
	public void setParameter(String name, String value) {
		this.params.set(name, value);
	}// setParameter(String, String)
	
	/**
	 * Replaces the old <code>ParameterList</code> with a new
	 * one.
	 *
	 * @param list The new list.
	 */
	public void setParameterList(ParameterList list) {
		this.params = list;
	}// setParameterList(ParameterList)
	
	/**
	 *
	 */
	public void setPrimaryType(String primaryType) {
		this.primaryType = primaryType;
	}// setPrimaryType(String)
	
	/**
	 * Sets the subtype to the parameter.
	 */
	public void setSubType(String subType) {
		this.subType = subType;
	}// setSubType(String)
	
	/**
	 * Returns a MIME compliant "Content-Type" header with the parameters
	 * and type.
	 */
	public String toString() {
		return "Content-Type:" + primaryType+ "/" + subType + (params == null ? null : ";" + params.toString());
	}// toString(String)
	
}// ContentType



