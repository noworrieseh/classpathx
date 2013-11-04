/********************************************************************
 * Copyright (c) Open Java Extensions, Andrew Selkirk  LGPL License *
 ********************************************************************/

package javax.mail;

// Imports
import java.net.*;
import java.util.BitSet;

/**
 * URL Name
 */
public class URLName {

	//-------------------------------------------------------------
	// Variables --------------------------------------------------
	//-------------------------------------------------------------

	protected	String		fullURL				= null;
	private		String		protocol			= null;
	private		String		username			= null;
	private		String		password			= null;
	private		String		host				= null;
	private		InetAddress	hostAddress			= null;
	private		boolean		hostAddressKnown	= false;
	private		int			port				= 0;
	private		String		file				= null;
	private		String		ref					= null;
	private		int			hashCode			= 0;
	private static	boolean	doEncode			= false;
	static		BitSet		dontNeedEncoding	= null;
	static	final	int		caseDiff			= 0;


	//-------------------------------------------------------------
	// Initialization ---------------------------------------------
	//-------------------------------------------------------------

	/**
	 * Create new URL Name.
	 */
	public URLName(String protocol, String host, int port,
			String file, String username, String password) {
		this.protocol = protocol;
		this.host = host;
		this.port = port;
		this.file = file;
		this.username = username;
		this.password = password;
	} // URLName()

	public URLName(URL url) {
		this.protocol = url.getProtocol();
		this.host = url.getHost();
		this.port = url.getPort();
		this.file = url.getFile();
	} // URLName()

	public URLName(String url) {
		parseString(url);
	} // URLName()


	//-------------------------------------------------------------
	// Public Accessor Methods ------------------------------------
	//-------------------------------------------------------------

	public int hashCode() {
		return 0; // TODO
	} // hashCode()

	public boolean equals(Object object) {

		// Variables
		URLName	urlName;

		if (object != null) {
			urlName = (URLName) object;

			// Check Protocol
			if (protocol != null && urlName.getProtocol() != null &&
				protocol.equals(urlName.getProtocol()) == false) {
					return false;
			}

			// Check Host
			if (host != null && urlName.getHost() != null &&
				host.equals(urlName.getHost()) == false) {
					return false;
			}

			// Check Port
			if (port != urlName.getPort()) {
				return false;
			}

			// Check Username
			if (username != null && urlName.getUsername() != null &&
				username.equals(urlName.getUsername()) == false) {
					return false;
			}

			// Check Password
			if (password != null && urlName.getPassword() != null &&
				password.equals(urlName.getPassword()) == false) {
					return false;
			}

			// Check File
			if (file != null && urlName.getFile() != null &&
				file.equals(urlName.getFile()) == false) {
					return false;
			}

			return true;

		} // if

		return false;

	} // equals()

	public String toString() {

		// Variables
		String	result;

		// Add Protocol to String
		result = getProtocol() + "://";

		// Add Username
		if (getUsername() != null) {
			result += getUsername();
		}

		// Add Password
		if (getPassword() != null) {
			result += ":" + getPassword();
		}

		// Add Username/Hostname separator
		if (getUsername() != null) {
			result += "@";
		}

		// Add Host
		result += getHost();

		// Add Port
		if (getPort() != -1) {
			result += ":" + getPort();
		}

		// Add Hostname ender
		result += "/";

		// Add File
		if (getFile() != null) {
			result += getFile();
		}

		// Return Result
		return result;

	} // toString()

	public URL getURL() throws MalformedURLException {
		return new URL(protocol, host, port, file);
	} // getURL()

	static String decode(String value) {
		return null; // TODO
	} // decode()

	public String getProtocol() {
		return protocol;
	} // getProtocol()

	public String getFile() {
		return file;
	} // getFile()

	public String getHost() {
		return host;
	} // getHost()

	public int getPort() {
		return port;
	} // getPort()

	public String getRef() {
		return null; // TODO
	} // getRef()

	private synchronized InetAddress getHostAddress() {
		return null; // TODO
	} // getHostAddress()

	public static String _encode(String value) {
		return null; // TODO
	} // _encode()

	static String encode(String value) {
		return null; // TODO
	} // encode()

	public String getPassword() {
		return password;
	} // getPassword()

	public String getUsername() {
		return username;
	} // getUsername()

	private static int indexOfAny(String value1, String value2) {
		return 0; // TODO
	} // indexOfAny()

	private static int indexOfAny(String value1, String value2, int value3) {
		return 0; // TODO
	} // indexOfAny()

	protected void parseString(String url) {
	} // parseString()


} // URLName
