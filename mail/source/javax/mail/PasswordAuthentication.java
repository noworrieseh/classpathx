/********************************************************************
 * Copyright (c) Open Java Extensions, Andrew Selkirk  LGPL License *
 ********************************************************************/

package javax.mail;

/**
 * Password Authentication.
 */
public final class PasswordAuthentication {

	//-------------------------------------------------------------
	// Variables --------------------------------------------------
	//-------------------------------------------------------------

	/**
	 * User name for authentication.
	 */
	private	String	userName	= null;

	/**
	 * Password for authentication.
	 */
	private	String	password	= null;


	//-------------------------------------------------------------
	// Initialization ---------------------------------------------
	//-------------------------------------------------------------

	/**
	 * Create new Password Authentication.
	 * @param userName User Name
	 * @param password Password
	 */
	public PasswordAuthentication(String userName, String password) {
		this.userName = userName;
		this.password = password;
	} // PasswordAuthentication()


	//-------------------------------------------------------------
	// Metods -----------------------------------------------------
	//-------------------------------------------------------------

	/**
	 * Get password.
	 * @returns Password
	 */
	public String getPassword() {
		return password;
	} // getPassword()

	/**
	 * Get user name.
	 * @returns User name
	 */
	public String getUserName() {
		return userName;
	} // getUserName()


} // PasswordAuthentication
