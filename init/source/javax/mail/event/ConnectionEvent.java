/********************************************************************
 * Copyright (c) Open Java Extensions, Andrew Selkirk  LGPL License *
 ********************************************************************/

package javax.mail.event;

/**
 * Connection Event
 * @author	Andrew Selkirk
 * @version	1.0
 */
public class ConnectionEvent extends MailEvent {

	//-------------------------------------------------------------
	// Variables --------------------------------------------------
	//-------------------------------------------------------------

	public static final int	OPENED			= 1;
	public static final int	DISCONNECTED	= 2;
	public static final int	CLOSED			= 3;

	/**
	 * Connection type of event.
	 */
	protected	int	type		= -1;


	//-------------------------------------------------------------
	// Initialization ---------------------------------------------
	//-------------------------------------------------------------

	public ConnectionEvent(Object source, int type) {
		super(source);
		this.type = type;
	} // ConnectionEvent()


	//-------------------------------------------------------------
	// Methods ----------------------------------------------------
	//-------------------------------------------------------------

	/**
	 * Dispatch event to listener
	 * @param listener Listener to notify
	 */
	public void dispatch(Object listener) {

		// Variables
		ConnectionListener cListener;

		// Get Connection Listener
		if (listener instanceof ConnectionListener) {
			cListener = (ConnectionListener) listener;
		} else {
			return;
		}

		// Check Type
		switch (type) {
			case OPENED:
				cListener.opened(this);
				break;
			case DISCONNECTED:
				cListener.disconnected(this);
				break;
			case CLOSED:
				cListener.closed(this);
				break;
		} // switch

	} // dispatch()

	/**
	 * Get connection event type.
	 * @returns Connection type
	 */
	public int getType() {
		return type;
	} // getType()


} // ConnectionEvent
