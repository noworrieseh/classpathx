/********************************************************************
 * Copyright (c) Open Java Extensions, Andrew Selkirk  LGPL License *
 ********************************************************************/

package javax.mail.event;

// Imports
import java.util.EventListener;

/**
 * Message Changed Listener Interface.
 */
public interface MessageChangedListener extends EventListener {

	//-------------------------------------------------------------
	// Interface: MessageChangedListener --------------------------
	//-------------------------------------------------------------

	/**
	 * Message changed event.
	 * @param event MessageChanged event
	 */
	public abstract void messageChanged(MessageChangedEvent event);


} // MessageChangedListener
