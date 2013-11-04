/********************************************************************
 * Copyright (c) Open Java Extensions, Andrew Selkirk  LGPL License *
 ********************************************************************/

package javax.mail.event;

// Imports
import java.util.EventListener;

/**
 * Connection Interface.
 */
public interface ConnectionListener extends EventListener {

	//-------------------------------------------------------------
	// Interface: ConnectionListener ------------------------------
	//-------------------------------------------------------------

	/**
	 * Connection closed.
	 * @param event Connection event
	 */
	public abstract void closed(ConnectionEvent event);

	/**
	 * Connection disconnected.
	 * @param event Connection event
	 */
	public abstract void disconnected(ConnectionEvent event);

	/**
	 * Connection opened.
	 * @param event Connection event
	 */
	public abstract void opened(ConnectionEvent event);


} // ConnectionListener

