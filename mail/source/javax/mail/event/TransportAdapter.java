/********************************************************************
 * Copyright (c) Open Java Extensions, Andrew Selkirk  LGPL License *
 ********************************************************************/

package javax.mail.event;

/**
 * Transport Adapter.
 */
public abstract class TransportAdapter implements TransportListener {

	//-------------------------------------------------------------
	// Initialization ---------------------------------------------
	//-------------------------------------------------------------

	/**
	 * Create a new Transport Adapter.
	 */
	public TransportAdapter() {
	} // TransportAdapter()


	//-------------------------------------------------------------
	// Interface: TransportListener ------------------------------
	//-------------------------------------------------------------

	/**
	 * Message delivered.
	 * @param event Transport event
	 */
	public void messageDelivered(TransportEvent event) {
	} // messageDelivered()

	/**
	 * Message not delivered.
	 * @param event Transport event
	 */
	public void messageNotDelivered(TransportEvent event) {
	} // messageNotDelivered()

	/**
	 * Message partially delivered
	 * @param event Transport event
	 */
	public void messagePartiallyDelivered(TransportEvent event) {
	} // messagePartiallyDelivered()


} // TransportAdapter
