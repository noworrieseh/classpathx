/********************************************************************
 * Copyright (c) Open Java Extensions, Andrew Selkirk  LGPL License *
 ********************************************************************/

package javax.mail.event;

/**
 * Connection Adapter
 */
public abstract class ConnectionAdapter implements ConnectionListener {

	//-------------------------------------------------------------
	// Initialization ---------------------------------------------
	//-------------------------------------------------------------

	/**
	 * Create a new Connection Adapter.
	 */
	public ConnectionAdapter() {
	} // ConnectionAdapter()


	//-------------------------------------------------------------
	// Interface: ConnectionListener ------------------------------
	//-------------------------------------------------------------

	/**
	 * Connection closed.
	 * @param event Connection event
	 */
	public void closed(ConnectionEvent event) {
	} // closed()

	/**
	 * Connection disconnected.
	 * @param event Connection event
	 */
	public void disconnected(ConnectionEvent event) {
	} // disconnected()

	/**
	 * Connection opened.
	 * @param event Connection event
	 */
	public void opened(ConnectionEvent event) {
	} // opened()


} // ConnectionAdapter
