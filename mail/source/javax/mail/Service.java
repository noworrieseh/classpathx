/********************************************************************
 * Copyright (c) Open Java Extensions, Andrew Selkirk  LGPL License *
 ********************************************************************/

package javax.mail;

// Imports
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.*;
import javax.mail.event.*;

/**
 * Service is an abstract class that represents the common
 * functionality among messaging services.
 * @author	Andrew Selkirk
 * @version	1.0
 */
public abstract class Service {

	//-------------------------------------------------------------
	// Variables --------------------------------------------------
	//-------------------------------------------------------------

	/**
	 * Session context of service.
	 */
	protected	Session		session				= null;

	/**
	 * URLName of service.
	 */
	protected	URLName		url					= null;

	/**
	 * Whether service is in debug mode.
	 */
	protected	boolean		debug				= false;

	/**
	 * Connection state of service.
	 */
	private		boolean		connected			= false;

	/**
	 * List of connection listeners.
	 */
	private		Vector		connectionListeners	= new Vector();

	/**
	 * Event Queue for event notification to listeners.
	 */
	private		EventQueue	q					= null;


	//-------------------------------------------------------------
	// Initialization ---------------------------------------------
	//-------------------------------------------------------------

	/**
	 * Service contructor.
	 * @param session Session context of service
	 * @param urlname URLName of service
	 */
	protected Service(Session session, URLName urlName) {
		this.session = session;
		this.url = urlName;
		this.debug = session.getDebug();
		q = new EventQueue();
	} // Service()


	//-------------------------------------------------------------
	// Methods ----------------------------------------------------
	//-------------------------------------------------------------

	/**
	 * Ensure event queue is properly terminated when finalizing.
	 */
	protected void finalize() throws Throwable {
		terminateQueue();
		super.finalize();
	} // finalize()

	/**
	 * String representation of service.
	 * @returns String representation
	 */
	public String toString() {
		if (url != null) {
			return getURLName().toString();
		}
		return super.toString();
	} // toString()

	/**
	 * Close service connection and notify listeners.
	 * @throws MessagingException Messaging exception has occurred.
	 */
	public synchronized void close() throws MessagingException {

		// Close Connection
		setConnected(false);

		// Send Event
		notifyConnectionListeners(ConnectionEvent.CLOSED);

	} // close()

	/**
	 * Default connect method.  Equivalent of connect(null, null, null).
	 * @throws MessagingException Messaging exception has occurred.
	 */
	public void connect() throws MessagingException {
		connect(null, null, null);
	} // connect()

	/**
	 * Connect method.  Equivalent of connect(null, -1, null, null).
	 * @param host Host to connect to
	 * @param user User to connect as
	 * @param password Password to use in connection
	 * @throws MessagingException Messaging exception has occurred.
	 */
	public void connect(String host, String user, String password)
			throws MessagingException {
		connect(host, -1, user, password);
	} // connect()

	/**
	 * Connect method.
	 * @param host Host to connect to
	 * @param port Port on host to connect to
	 * @param user User to connect as
	 * @param password Password to use in connection
	 * @throws MessagingException Messaging exception has occurred.
	 */
	public void connect(String host, int port, String user,
				String password) throws MessagingException {

		// Variables
		String					protocol;
		String					file;
		PasswordAuthentication	authentication;
		InetAddress				address;

		// Check for current Connection
		if (isConnected() == true) {
			throw new MessagingException("Already connected");
		} // if

		// Initialize Variables
		protocol = null;
		file = null;

		// Extract Information from URLName
		if (url != null) {

			// Get Protocol and File
			protocol = url.getProtocol();
			file = url.getFile();

			// Check Hostname
			if (host == null) {
				host = url.getHost();
    		} // if

			// Check Port
			if (port == -1) {
				port = url.getPort();
    		} // if

			// Check Username
			if (user == null) {
				user = url.getUsername();
    		} // if

			// Check Password
			if (password == null) {
				password = url.getPassword();
    		} // if

		} // if: url

		// Check for Protocol Specific Username
		if ((protocol != null) && (user == null)) {
			user = session.getProperty("mail." + protocol + ".user");
		} // if

		// Check for Protocol Specific Password
		if ((protocol != null) && (password == null)) {
			password = session.getProperty("mail." + protocol + ".password");
		} // if

		// Check for Default Username
		if (user == null) {
			user = session.getProperty("mail.user");
		} // if

		// Check for Generic Password
		if (password == null) {
			password = session.getProperty("mail.password");
		} // if

		// Attempt to Connect with Protocol
		while (protocolConnect(host, port, user, password) == false) {

			// Determine InetAddress of Host
			try {
				address = InetAddress.getByName(host);
			} catch (UnknownHostException e) {
				address = null;
			} // try

			// Call back to Application for authentication information
			authentication = session.requestPasswordAuthentication(
								address, port, protocol, null, user);

   			// Check for Results
			if (authentication != null) {
				user = authentication.getUserName();
				password = authentication.getPassword();
    		} // if

		} // while

		// Set URLName with functional information
		setURLName(new URLName(protocol, host, port, file, user, password));

		// Send Event
		notifyConnectionListeners(ConnectionEvent.OPENED);

	} // connect()

	/**
	 * Perform protocol connection and return whether the action was
	 * successful.  This method should be sub-classed for each of the
	 * individual providers.  By default, this metod returns false.
	 * @param host Host to connect to
	 * @param port Port on host to connect to
	 * @param user User to connect as
	 * @param password Password to use in connection
	 * @returns true if protocol connect successful, false otherwise
	 * @throws MessagingException Messaging exception has occurred.
	 */
	protected boolean protocolConnect(String host, int port,
			String user, String password) throws MessagingException {
		return false;
	} // protocolConnect()

	/**
	 * Get URLName of service.
	 * @returns URLName of service
	 */
	public URLName getURLName() {
		return new URLName(url.getProtocol(), url.getHost(), url.getPort(),
							null, url.getUsername(), null);
	} // getURLName()

	/**
	 * Set the URLName of the service.
	 * @param urlName URLName of the service
	 */
	protected void setURLName(URLName urlName) {
		url = urlName;
	} // setURLName()

	/**
	 * Determine if service is connected.
	 * @returns true if connected, false otherwise
	 */
	public boolean isConnected() {
		return connected;
	} // isConnected()

	/**
	 * Set connected state.
	 * @param connected Connection state
	 */
	protected void setConnected(boolean connected) {
		this.connected = connected;
	} // setConnected()

	/**
	 * Queue event.
	 * @param event Event to queue
	 * @param vector List of listeners to notify
	 */
	protected synchronized void queueEvent(MailEvent event, Vector vector) {

		// Queue Event
		q.enqueue(event, (Vector) vector.clone());

	} // queueEvent()

	/**
	 * Add listener to connection listeners.
	 * @param listener Connection listener to add
	 */
	public synchronized void addConnectionListener(ConnectionListener listener) {

		// Add Connection Listener
		if (connectionListeners.contains(listener) == false) {
			connectionListeners.addElement(listener);
		} // if

	} // addConnectionListeners()

	/**
	 * Remove listener from connection listeners.
	 * @param listener Connection listener
	 */
	public synchronized void removeConnectionListeners(ConnectionListener listener) {

		// Remove Connection Listener
		if (connectionListeners.contains(listener) == true) {
			connectionListeners.removeElement(listener);
		} // if

	} // removeConnectionListeners()

	/**
	 * Notify Connection listeners of connect event that has
	 * occurred.
	 * @param type Type of connection event that has happened
	 */
	protected void notifyConnectionListeners(int type) {

		// Variables
		ConnectionEvent	event;

		// Check Listener List
		if (connectionListeners.size() == 0) {
			return;
		} // if

		// Create Connection Event
		event = new ConnectionEvent(this, type);

		// Queue Notification
		queueEvent(event, connectionListeners);

	} // notifyConnectionListeners()

	/**
	 * Send notification to event queue to terminate.
	 */
	private synchronized void terminateQueue() {

		// Variables
		MailEvent	event;
		Vector		list;

		// Create Service Completion event
		event = new MailEvent(this) {
			public void dispatch(Object listener) {
			} // dispatch()
		};

		// Create Listeners
		list = new Vector();
		list.addElement(null);

		// Queue Event
		queueEvent(event, list);

	} // terminateQueue()


} // Service()
