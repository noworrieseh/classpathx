/********************************************************************
 * Copyright (c) Open Java Extensions, Andrew Selkirk  LGPL License *
 ********************************************************************/

package javax.mail;

// Imports
import java.net.InetAddress;

/**
 * Authenticator.
 * @author	Andrew Selkirk
 * @version	1.0
 */
public abstract class Authenticator {

	//-------------------------------------------------------------
	// Variables --------------------------------------------------
	//-------------------------------------------------------------

	private	InetAddress		requestingSite		= null;
	private	int				requestingPort		= -1;
	private	String			requestingProtocol	= null;
	private String			requestingPrompt	= null;
	private	String			requestingUserName	= null;


	//-------------------------------------------------------------
	// Initialization ---------------------------------------------
	//-------------------------------------------------------------

	public Authenticator() {
	} // Authenticator()


	//-------------------------------------------------------------
	// Public Accessor Methods ------------------------------------
	//-------------------------------------------------------------

	/**
	 * Reset entries.
	 */
	private void reset() {
		requestingSite     = null;
		requestingPort     = -1;
		requestingProtocol = null;
		requestingPrompt   = null;
		requestingUserName = null;
	} // reset()

	/**
	 * Get password authentication object.
	 * @returns default null
	 */
	protected PasswordAuthentication getPasswordAuthentication() {
		return null;
	} // getPasswordAuthentication()

	/**
	 * Get default user name.
	 * @returns Default username
	 */
	protected final String getDefaultUserName() {
		return requestingUserName;
	} // getDefaultUserName()

	/**
	 * Get requesting port.
	 * @returns Port number
	 */
	protected final int getRequestingPort() {
		return requestingPort;
	} // getRequestingPort()

	/**
	 * Get requesting prompt.
	 * @returns Prompt
	 */
	protected final String getRequestingPrompt() {
		return requestingPrompt;
	} // getRequestingPrompt()

	/**
	 * Get requesting protocol.
	 * @returns Protocol
	 */
	protected final String getRequestingProtocol() {
		return requestingProtocol;
	} // getRequestingProtocol()

	/**
	 * Get requesting site.
	 * @returns InetAddress
	 */
	protected final InetAddress getRequestingSite() {
		return requestingSite;
	} // getRequestingSite()

	/**
	 * Get password authentication.
	 */
	final PasswordAuthentication requestPasswordAuthentication(
					InetAddress	address,
					int			port,
					String		protocol,
					String		prompt,
					String		userName) {

		// Reset Entries
		reset();

		// Set Properties
		requestingSite		= address;
		requestingPort		= port;
		requestingProtocol	= protocol;
		requestingPrompt	= prompt;
		requestingUserName	= userName;

		// Generate Password Authentication
		return getPasswordAuthentication();

	} // requestPasswordAuthentication()


} // Authenticator
