/********************************************************************
 * Copyright (c) Open Java Extensions, Andrew Selkirk  LGPL License *
 ********************************************************************/

package javax.mail;

/**
 * The context that a particular part of a message is contained.
 * Generate from MessageAware objects.
 * @author	Andrew Selkirk
 * @version	1.0
 */
public class MessageContext {

	//-------------------------------------------------------------
	// Variables --------------------------------------------------
	//-------------------------------------------------------------

	/**
	 * Message Context Part.
	 */
	private	Part	part	= null;


	//-------------------------------------------------------------
	// Initialization ---------------------------------------------
	//-------------------------------------------------------------

	/**
	 * Create a new Message Context from part.
	 * @param part Part
	 */
	public MessageContext(Part part) {
		this.part = part;
	} // MessageContext()


	//-------------------------------------------------------------
	// Methods ----------------------------------------------------
	//-------------------------------------------------------------

	/**
	 * Get message.
	 * @returns Message
	 */
	public Message getMessage() {
		try {
			return getMessage(part);
		} catch (Exception e) {
		}
		return null;
	} // getMessage()

	/**
	 * Get message.
	 * @param part Part
	 * @returns Message
	 * @throws MessagingException Messaging exception occurred
	 */
	private static Message getMessage(Part part)
			throws MessagingException {

		// Check for Message part
		if (part instanceof Message) {
			return (Message) part;

		// Check for Multipart
		} else if (part instanceof Multipart &&
			   ((Multipart) part).getParent() != null) {
			return getMessage(((Multipart) part).getParent());
		}

		// Unable to locate message, return null
		return null;

	} // getMessage()

	/**
	 * Get part.
	 * @returns Part
	 */
	public Part getPart() {
		return part;
	} // getPart()

	/**
	 * Get session.
	 * @returns Session
	 */
	public Session getSession() {

		// Variables
		Message	message;

		// This technique of accessing the protected
		// session property of message is the only
		// way I can connect a Part object to a Session.

		// Locate Message
		message = getMessage();

		// If Exists, look for Session
		return message.session;

	} // getSession()


} // MessageContext
