/********************************************************************
 * Copyright (c) Open Java Extensions, Andrew Selkirk  LGPL License *
 ********************************************************************/

package javax.mail.event;

// Imports
import javax.mail.*;

/**
 * Transport Event.
 */
public class TransportEvent extends MailEvent {

	//-------------------------------------------------------------
	// Variables --------------------------------------------------
	//-------------------------------------------------------------

	public static final int	MESSAGE_DELIVERED			= 1;
	public static final int	MESSAGE_NOT_DELIVERED		= 2;
	public static final int	MESSAGE_PARTIALLY_DELIVERED	= 3;

	/**
	 * Transport type of event.
	 */
	protected		int		type		= -1;

	/**
	 * Sent addresses.
	 */
	protected transient	Address[]	validSent	= null;

	/**
	 * Unsent addresses.
	 */
	protected transient	Address[]	validUnsent	= null;

	/**
	 * Invalid addresses.
	 */
	protected transient	Address[]	invalid		= null;

	/**
	 * Message.
	 */
	protected transient	Message		msg			= null;


	//-------------------------------------------------------------
	// Initialization ---------------------------------------------
	//-------------------------------------------------------------

	/**
	 * Create a new transport event.
	 * @param transport Source
	 * @param type Transport type
	 * @param validSent Sent addresses
	 * @param validUnsent Unsent addresses
	 * @param invalid Invalid addresses
	 * @param msg Message sent
	 */
	public TransportEvent(Transport transport, int type, Address[] validSent,
			Address[] validUnsent, Address[] invalid, Message msg) {
		super(transport);
		this.type = type;
		this.validSent = validSent;
		this.validUnsent = validUnsent;
		this.invalid = invalid;
		this.msg = msg;
	} // TransportEvent()


	//-------------------------------------------------------------
	// Methods ----------------------------------------------------
	//-------------------------------------------------------------

	/**
	 * Dispatch event to listener
	 * @param listener Listener to notify
	 */
	public void dispatch(Object listener) {

		// Variables
		TransportListener tListener;

		// Get Transport Listener
		if (listener instanceof TransportListener) {
			tListener = (TransportListener) listener;
		} else {
			return;
		}

		// Check Type
		switch (type) {
			case MESSAGE_DELIVERED:
				tListener.messageDelivered(this);
				break;
			case MESSAGE_NOT_DELIVERED:
				tListener.messageNotDelivered(this);
				break;
			case MESSAGE_PARTIALLY_DELIVERED:
				tListener.messagePartiallyDelivered(this);
				break;
		} // switch

	} // dispatch()

	/**
	 * Get transport event type.
	 * @returns Transport type
	 */
	public int getType() {
		return type;
	} // getType()

	/**
	 * Get invalid addresses.
	 * @returns Invalid addresses
	 */
	public Address[] getInvalidAddresses() {
		return invalid;
	} // getInvalidAddresses()

	/**
	 * Get sent addresses.
	 * @returns Sent addresses
	 */
	public Address[] getValidSentAddresses() {
		return validSent;
	} // getValidSentAddresses()

	/**
	 * Get unsent addresses.
	 * @returns Unsent addresses
	 */
	public Address[] getValidUnsentAddresses() {
		return validUnsent;
	} // getValidUnsentAddresses()


} // TransportEvent
