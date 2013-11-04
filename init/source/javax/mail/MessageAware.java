/********************************************************************
 * Copyright (c) Open Java Extensions, Andrew Selkirk  LGPL License *
 ********************************************************************/

package javax.mail;

/**
 * Message Aware.
 */
public interface MessageAware {

	//-------------------------------------------------------------
	// Interface: MessageAware ------------------------------------
	//-------------------------------------------------------------

	/**
	 * Get message context.
	 * @returns Message context
	 */
	public MessageContext getMessageContext();


} // MessageAware
