/********************************************************************
 * Copyright (c) Open Java Extensions, Andrew Selkirk  LGPL License *
 ********************************************************************/

package javax.mail.event;

// Imports
import javax.mail.*;

/**
 * Store Event.
 */
public class StoreEvent extends MailEvent {

	//-------------------------------------------------------------
	// Variables --------------------------------------------------
	//-------------------------------------------------------------

	/**
	 * ALERT store type.
	 */
	public static final int	ALERT		= 1;

	/**
	 * NOTICE store type.
	 */
	public static final int	NOTICE		= 2;

	/**
	 * Store type of event.
	 */
	protected		int	type		= -1;

	/**
	 * Store message.
	 */
	protected		String	message		= null;


	//-------------------------------------------------------------
	// Initialization ---------------------------------------------
	//-------------------------------------------------------------

	/**
	 * Create a new store event.
	 */
	public StoreEvent(Store store, int type, String message) {
		super(store);
		this.type = type;
		this.message = message;
	} // StoreEvent()


	//-------------------------------------------------------------
	// Methods ----------------------------------------------------
	//-------------------------------------------------------------

	/**
	 * Dispatch event to listener
	 * @param listener Listener to notify
	 */
	public void dispatch(Object listener) {

		// Check for Store Listener
		if (listener instanceof StoreListener) {
			((StoreListener) listener).notification(this);
		} // if

	} // dispatch()

	/**
	 * Get message.
	 * @returns Message
	 */
	public String getMessage() {
		return message;
	} // getMessage()

	/**
	 * Get message type.
	 * @returns Message type
	 */
	public int getMessageType() {
		return type;
	} // getMessageType()
                    

} // StoreEvent
