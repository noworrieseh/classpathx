/********************************************************************
 * Copyright (c) Open Java Extensions, Andrew Selkirk  LGPL License *
 ********************************************************************/

package javax.mail.event;

// Imports
import java.util.EventListener;

/**
 * Transport Listener interface.
 */
public interface TransportListener extends EventListener {

	//-------------------------------------------------------------
	// Interface: TransportListener ------------------------------
	//-------------------------------------------------------------

	/**
	 * Message delivered.
	 * @param event Transport event
	 */
	public abstract void messageDelivered(TransportEvent event);

	/**
	 * Message not delivered.
	 * @param event Transport event
	 */
	public abstract void messageNotDelivered(TransportEvent event);

	/**
	 * Message partially delivered
	 * @param event Transport event
	 */
	public abstract void messagePartiallyDelivered(TransportEvent event);


} // TransportListener
