/********************************************************************
 * Copyright (c) Open Java Extensions, Andrew Selkirk  LGPL License *
 ********************************************************************/

package javax.mail.event;

// Imports
import java.util.EventListener;

/**
 * Store Listener Interface.
 */
public interface StoreListener extends EventListener {

	//-------------------------------------------------------------
	// Interface: StoreListener ------------------------------
	//-------------------------------------------------------------

	/**
	 * Notification of store event.
	 * @param event Store event
	 */
	public abstract void notification(StoreEvent event);


} // StoreListener
