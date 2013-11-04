/********************************************************************
 * Copyright (c) Open Java Extensions, Andrew Selkirk  LGPL License *
 ********************************************************************/

package javax.mail;

/**
 * A Provider object describes the information regarding a protocol
 * implementation.
 * @author	Andrew Selkirk
 * @version	1.0
 */
public class Provider {

	//-------------------------------------------------------------
	// Classes ----------------------------------------------------
	//-------------------------------------------------------------

	/**
	 * Type of Provider.  Currently, STORE and TRANSPORT are the
	 * only two defined types of providers.
	 */
	public static class Type {

		public static final Type STORE     = new Type("STORE");
		public static final Type TRANSPORT = new Type("TRANSPORT");

		/**
		 * Type identifier
		 */
		private	String	type	= null;

		/**
		 * Create new Type.
		 * @param value String representation of Type
		 */
		private Type(String value) {
			type = value;
		} // Type()


	} // Type


	//-------------------------------------------------------------
	// Variables --------------------------------------------------
	//-------------------------------------------------------------

	/**
	 * Provider type.
	 */
	private	Type	type		= null;

	/**
	 * Provider protocol.
	 */
	private	String	protocol	= null;

	/**
	 * Provider class name.
	 */
	private String	className	= null;

	/**
	 * Provider vendor information.
	 */
	private	String	vendor		= null;

	/**
	 * Provider version information.
	 */
	private	String	version		= null;


	//-------------------------------------------------------------
	// Initialization ---------------------------------------------
	//-------------------------------------------------------------

	/**
	 * Create new Provider
	 */
	Provider(Type type, String protocol, String className,
			String vendor, String version) {
		this.type = type;
		this.protocol = protocol;
		this.className = className;
		this.vendor = vendor;
		this.version = version;
	} // Provider()


	//-------------------------------------------------------------
	// Methods ----------------------------------------------------
	//-------------------------------------------------------------

	/**
	 * Get string representation of Provider.
	 * @returns String representation
	 */
	public String toString() {

		// Variables
		StringBuffer	output;

		// Add Provider class to output
		output = new StringBuffer(getClass().getName() + "[");

		// Add Type to Output
		if (type == Type.STORE) {
			output.append("STORE");
		} else {
			output.append("TRANSPORT");
		}

		// Add Protocol
		if (protocol != null) {
			output.append("," + protocol);
		}

		// Add Class Name
		if (className != null) {
			output.append("," + className);
		}

		// Add Vendor
		if (vendor != null) {
			output.append("," + vendor);
		}

		// Add Version
		if (version != null) {
			output.append("," + version);
		}

		// Close off output
		output.append("]");

		// Return Result
		return output.toString();

	} // toString()

	/**
	 * Get provider type.
	 * @returns Provider type
	 */
	public Type getType() {
		return type;
	} // getType()

	/**
	 * Get provider protocol.
	 * @returns Protocol
	 */
	public String getProtocol() {
		return protocol;
	} // getProtocol()

	/**
	 * Get provider version.
	 * @returns Version information
	 */
	public String getVersion() {
		return version;
	} // getVersion()

	/**
	 * Get provider class name.
	 * @returns Class name
	 */
	public String getClassName() {
		return className;
	} // getClassName()

	/**
	 * Get vendor information.
	 * @returns Vendor information
	 */
	public String getVendor() {
		return vendor;
	} // getVendor()


} // Provider
