/********************************************************************
 * Copyright (c) Open Java Extensions, Andrew Selkirk  LGPL License *
 ********************************************************************/

package javax.mail;

/**
 * UID Folder
 */
public interface UIDFolder {

	//-------------------------------------------------------------
	// Variables --------------------------------------------------
	//-------------------------------------------------------------

	/**
	 * Last UID
	 */
	public static final long	LASTUID	= -1;


	//-------------------------------------------------------------
	// Public Accessor Methods ------------------------------------
	//-------------------------------------------------------------

	/**
	 * Get message by UID.
	 * @param uid UID of message
	 * @returns Message
	 * @throws MessagingException Messaging exception
	 */
	public abstract Message getMessageByUID(long uid)
		throws MessagingException;

	/**
	 * Get messages.
	 * @param value1 ??? min maybe?
	 * @param value2 ??? max maybe?
	 * @returns Message array
	 * @throws MessagingException Messaging exception
	 */
	public abstract Message[] getMessagesByUID(long value1, long value2)
		throws MessagingException;

	/**
	 * Get messages by UID.
	 * @param uid UID list
	 * @returns Message array
	 * @throws MessagingException Messaging exception
	 */
	public abstract Message[] getMessagesByUID(long[] uid)
		throws MessagingException;

	/**
	 * Get UID for messaging.
	 * @param message Message
	 * @returns UID value
	 * @throws MessagingException Messaging exception
	 */
	public abstract long getUID(Message message)
		throws MessagingException;

	/**
	 * Get UID validity.
	 * @returns UID value
	 * @throws MessagingException Messaging exception
	 */
	public abstract long getUIDValidity()
		throws MessagingException;


} // UIDFolder
