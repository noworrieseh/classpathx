/********************************************************************
 * Copyright (c) Open Java Extensions, Andrew Selkirk  LGPL License *
 ********************************************************************/

package javax.mail.event;

// Imports
import java.util.EventObject;

/**
 * Mail Event.
 */
public abstract class MailEvent extends EventObject {

	//-------------------------------------------------------------
	// Initialization ---------------------------------------------
	//-------------------------------------------------------------

	public MailEvent(Object source) {
		super(source);
	} // MailEvent()


	//-------------------------------------------------------------
	// Methods ----------------------------------------------------
	//-------------------------------------------------------------

	/**
	 * Dispatch event to listener
	 * @param listener Listener to notify
	 */
	public abstract void dispatch(Object listener);


} // MailEvent
