/********************************************************************
 * Copyright (c) Open Java Extensions, Andrew Selkirk  LGPL License *
 ********************************************************************/

package javax.mail;

// Imports
import java.util.Vector;
import javax.mail.event.*;

/**
 * A Transport Service is a Provider that implements a transport
 * protocol for sending out a message.
 * @author	Andrew Selkirk
 * @version	1.0
 */
public abstract class Transport extends Service {

	//-------------------------------------------------------------
	// Variables --------------------------------------------------
	//-------------------------------------------------------------

	/**
	 * Transport listeners
	 */
	private	Vector	transportListeners	= new Vector();


	//-------------------------------------------------------------
	// Initialization ---------------------------------------------
	//-------------------------------------------------------------

	/**
	 * Create new Transport.
	 * @param session Session that created this Transport
	 * @param urlName URLName representation of transport
	 */
	public Transport(Session session, URLName urlName) {
		super(session, urlName);
	} // Transport()


	//-------------------------------------------------------------
	// Public Accessor Methods ------------------------------------
	//-------------------------------------------------------------

	/**
	 * Add a listener for transport events.
	 * @param listener Transport listener
	 */
	public synchronized void addTransportListener(TransportListener listener) {
		if (transportListeners.contains(listener) == false) {
			transportListeners.addElement(listener);
		}
	} // addTransportListener()

	/**
	 * Remove a listener from being notified of transport events.
	 * @param listener Transport listener
	 */
	public synchronized void removeTransportListener(TransportListener listener) {
		if (transportListeners.contains(listener) == true) {
			transportListeners.remove(listener);
		}
	} // removeTransportListener()

	/**
	 * Notify Transport listeners of event.  The event is queued into a
	 * Service event queue.
	 * @param type Transport type
	 * @param validSent Valid sent addresses
	 * @param validUnsent Valid unsent addresses
	 * @param invalid Invalid addresses
	 * @param message Message that was sent
	 */
	protected void notifyTransportListeners(int type, Address[] validSent,
			Address[] validUnsent, Address[] invalid, Message message) {

		// Variables
		TransportEvent	event;

		// Create Event
		event = new TransportEvent(this, type, validSent,
									validUnsent, invalid, message);

		// Queue Event
		queueEvent(event, (Vector) transportListeners.clone());

	} // notifyTransportListeners()

	/**
	 * Send a message.  Recipients are determined from the message.
	 * Before the message is sent, saveChanges() is called.
	 * @param message Message to send
	 * @throws MessaginException Messaging exception occurred
	 */
	public static void send(Message message) throws MessagingException {

		// Variables
		Address[]	addresses;

		// Get Addresses
		addresses = message.getAllRecipients();

		// Save Changes
		message.saveChanges();

		// Send Message
		send0(message, addresses);

	} // send()

	/**
	 * Send a message.  Recipients are not determined from the message
	 * are are sent exclusively to the provided addresses.  This allows
	 * for the implementation of BCC type destinations.  Before the
	 * message is sent, saveChanges() is called.
	 * @param message Message to send
	 * @param addresses Addresses to send message to
	 * @throws MessaginException Messaging exception occurred
	 */
	public static void send(Message message, Address[] addresses)
			throws MessagingException {

		// Save Changes
		message.saveChanges();

		// Send Message
		send0(message, addresses);

	} // send()

	/**
	 * Determine the Transport necessary to send to each address and
	 * send the message.
	 * @param message Message to send
	 * @param addresses Addresses to send message to
	 * @throws MessaginException Messaging exception occurred
	 */
	private static void send0(Message message, Address[] addresses)
			throws MessagingException {

		// Variables
		Session		session;
		Transport	transport;
		int			index;

		// Get Session
		// OK, the problem is we need to get ourselves a valid
		// session object so that we can do the song and dance to
		// get the related Transport object.  Assuming that a
		// default session has already been created, the following
		// will return us a valid Session.  Is this the way to do it?
		session = Session.getDefaultInstance(null, null);

		// Process Each Address
		for (index = 0; index < addresses.length; index++) {

			// Get Transport for Address
			transport = session.getTransport(addresses[index]);

			// Send Message
			transport.sendMessage(message,
						new Address[]{addresses[index]});

		} // for: index

	} // send0()

	/**
	 * Implementation of sending message over the determined protocol.
	 * @param message Message to send
	 * @param addresses Addresses to send message to
	 * @throws MessaginException Messaging exception occurred
	 */
	public abstract void sendMessage(Message message, Address[] addresses)
			throws MessagingException;


} // Transport
