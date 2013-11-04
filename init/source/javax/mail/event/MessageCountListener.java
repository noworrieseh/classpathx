/********************************************************************
 * Copyright (c) Open Java Extensions, Andrew Selkirk  LGPL License *
 ********************************************************************/

package javax.mail.event;

// Imports
import java.util.EventListener;

/**
 * Message Count Listener interface.
 */
public interface MessageCountListener extends EventListener {

	//-------------------------------------------------------------
	// Interface: MessageCountListener ----------------------------
	//-------------------------------------------------------------

	/**
	 * Message added.
	 * @param event Message count event
	 */
	public abstract void messageAdded(MessageCountEvent event);

	/**
	 * Message removed.
	 * @param event Message count event
	 */
	public abstract void messageRemoved(MessageCountEvent event);


} // MessageCountListener
