/********************************************************************
 * Copyright (c) Open Java Extensions, Andrew Selkirk  LGPL License *
 ********************************************************************/

package javax.mail.event;

// Imports
import javax.mail.*;

/**
 * Message Count Event.
 */
public class MessageCountEvent extends MailEvent {

	//-------------------------------------------------------------
	// Variables --------------------------------------------------
	//-------------------------------------------------------------

	/**
	 * Message added.
	 */
	public static final int	ADDED		= 1;

	/**
	 * Message removed.
	 */
	public static final int	REMOVED		= 2;

	/**
	 * Message count type.
	 */
	protected		int		type		= -1;

	/**
	 * Message removed?
	 */
	protected		boolean		removed		= false;

	/**
	 * Messages
	 */
	protected transient	Message[]	msgs	= null;


	//-------------------------------------------------------------
	// Initialization ---------------------------------------------
	//-------------------------------------------------------------

	/**
	 * Create a new message count event.
	 * @param folder Source
	 * @param type Message count type
	 * @param removed Message removed
	 * @param msgs Messages
	 */
	public MessageCountEvent(Folder folder, int type,
				boolean removed, Message[] msgs) {
		super(folder);
		this.type = type;
		this.removed = removed;
		this.msgs = msgs;
	} // MessageCountEvent()


	//-------------------------------------------------------------
	// Methods ----------------------------------------------------
	//-------------------------------------------------------------

	/**
	 * Dispatch event to listener
	 * @param listener Listener to notify
	 */
	public void dispatch(Object listener) {

		// Variables
		MessageCountListener mcListener;

		// Get Message Count Listener
		if (listener instanceof MessageCountListener) {
			mcListener = (MessageCountListener) listener;
		} else {
			return;
		}

		// Check Type
		switch (type) {
			case ADDED:
				mcListener.messageAdded(this);
				break;
			case REMOVED:
				mcListener.messageRemoved(this);
				break;
		} // switch

	} // dispatch()

	/**
	 * Get message count type.
	 * @returns Message count type
	 */
	public int getType() {
		return type;
	} // getType()

	/**
	 * Get messages.
	 * @returns List of messages
	 */
	public Message[] getMessages() {
		return msgs;
	} // getMessages()

	/**
	 * Determine if removed.
	 * @returns true is removed, false otherwise
	 */
	public boolean isRemoved() {
		return removed;
	} // isRemoved()


} // MessageCountEvent
