/********************************************************************
 * Copyright (c) Open Java Extensions, Andrew Selkirk  LGPL License *
 ********************************************************************/

package javax.mail.event;

/**
 * Message Count Adapter.
 */
public abstract class MessageCountAdapter implements MessageCountListener {

	//-------------------------------------------------------------
	// Initialization ---------------------------------------------
	//-------------------------------------------------------------

	/**
	 * Create a new MessageCount Adapter.
	 */
	public MessageCountAdapter() {
	} // MessageCountAdapter()


	//-------------------------------------------------------------
	// Interface: MessageCountListener ----------------------------
	//-------------------------------------------------------------

	/**
	 * Message added.
	 * @param event Message count event
	 */
	public void messageAdded(MessageCountEvent event) {
	} // messageAdded()

	/**
	 * Message removed.
	 * @param event Message count event
	 */
	public void messageRemoved(MessageCountEvent event) {
	} // messageRemoved()


} // MessageCountAdapter
