/********************************************************************
 * Copyright (c) Open Java Extensions, Andrew Selkirk  LGPL License *
 ********************************************************************/

package javax.mail.event;

// Imports
import javax.mail.*;

/**
 * Message Changed Event.
 */
public class MessageChangedEvent extends MailEvent {

	//-------------------------------------------------------------
	// Variables --------------------------------------------------
	//-------------------------------------------------------------

	/**
	 * Flags changed type.
	 */
	public static final int	FLAGS_CHANGED		= 1;

	/**
	 * Envelope changed type.
	 */
	public static final int	ENVELOPE_CHANGED	= 2;

	/**
	 * Connection type of event.
	 */
	protected		int	type		= -1;

	/**
	 * Message.
	 */
	protected transient	Message	msg		= null;


	//-------------------------------------------------------------
	// Initialization ---------------------------------------------
	//-------------------------------------------------------------

	/**
	 * Create new message changed event.
	 * @param source Source of event
	 * @param type Message changed type
	 * @param message Message
	 */
	public MessageChangedEvent(Object source, int type, Message message) {
		super(source);
		this.type = type;
		this.msg = message;
	} // MessageChangedEvent()


	//-------------------------------------------------------------
	// Methods ----------------------------------------------------
	//-------------------------------------------------------------

	/**
	 * Dispatch event to listener
	 * @param listener Listener to notify
	 */
	public void dispatch(Object listener) {

		// Check for MessageChanged Listener
		if (listener instanceof MessageChangedListener) {
			((MessageChangedListener) listener).messageChanged(this);
		} // if

	} // dispatch()

	/**
	 * Get message change type.
	 * @returns MessageChange type
	 */
	public int getMessageChangeType() {
		return type;
	} // getMessageChangeType()

	/**
	 * Get message.
	 * @returns Message
	 */
	public Message getMessage() {
		return msg;
	} // getMessage()


} // MessageChangedEvent
