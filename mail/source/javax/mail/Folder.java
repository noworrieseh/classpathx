/********************************************************************
 * Copyright (c) Open Java Extensions, Andrew Selkirk  LGPL License *
 ********************************************************************/

package javax.mail;

// Imports
import java.util.*;
import javax.mail.event.*;
import javax.mail.search.*;

/**
 * Object that represents a folder for mail messages and sub folders.
 * @author	Andrew Selkirk
 * @version	1.0
 */
public abstract class Folder {

	//-------------------------------------------------------------
	// Variables --------------------------------------------------
	//-------------------------------------------------------------

	public	static final	int	HOLDS_MESSAGES		= 1;
	public	static final	int	HOLDS_FOLDERS		= 2;
	public	static final	int	READ_ONLY			= 1;
	public	static final	int	READ_WRITE			= 2;

	protected		Store	store					= null;
	protected		int		mode					= -1;
	private			Vector	connectionListeners		= new Vector();
	private			Vector	folderListeners			= new Vector();
	private			Vector	messageListeners		= new Vector();
	private			Vector	messageChangedListeners	= new Vector();
	private			EventQueue	q					= new EventQueue();


	//-------------------------------------------------------------
	// Initialization ---------------------------------------------
	//-------------------------------------------------------------

	/**
	 * Create a new Folder.
	 * @param store Store for folder
	 */
	protected Folder(Store store) {
		this.store = store;
	} // Folder()


	//-------------------------------------------------------------
	// Methods ----------------------------------------------------
	//-------------------------------------------------------------

	/**
	 * Finalize.
	 */
	protected void finalize() throws Throwable {
		terminateQueue();
	} // finalize()

	/**
	 * Get string representation of folder.
	 * @returns String representation
	 */
	public String toString() {

		// Variables
		String value;

		// Get Full Name
		value = getFullName();

		// Check Value
		if (value == null) {
			return super.toString();
		}

		// Return Value
		return value;

	} // toString()

	/**
	 * Get name of folder.
	 * @returns Folder name
	 */
	public abstract String getName();

	/**
	 * Get parent folder of this folder.  Null is returned if this
	 * folder is the root of the hierarchy.
	 * @returns Parent folder, or null
	 * @throws MessagingException Messaging exception occurred
	 */
	public abstract Folder getParent()
		throws MessagingException;

	/**
	 * Get message that corresponds to the specified message number.
	 * @param messageNum Message number
	 * @returns Message
	 * @throws MessagingException Messaging exception occurred
	 */
	public abstract Message getMessage(int messageNum)
		throws MessagingException;

	/**
	 * List of folders belonging to this Folders namespace area
	 * that correspond to the specified pattern.  '*' for the
	 * entire hierarchy and '%' for folders in this folder.
	 * @param pattern Search pattern
	 * @returns Array list of folders
	 * @throws MessagingException Messaging exception occurred
	 */
	public abstract Folder[] list(String pattern)
		throws MessagingException;

	/**
	 * Convenience methods that returns all of the folders contained
	 * in this folder.  Equivalent of calling list("%")
	 * @returns Array list of folders
	 */
	public Folder[] list() throws MessagingException {
		return list("%");
	} // list()

	/**
	 * Delete the folder.  Only valid on a closed folder.
	 * @param recurse Recursively delete hierarchy rooted
	 * @returns true if successful, false otherwise
	 */
	public abstract boolean delete(boolean recurse)
		throws MessagingException;

	/**
	 * Get folder type.
	 * @returns Integer bit-field of folder type
	 * @throws MessagingException Messaging exception occurred
	 */
	public abstract int getType()
		throws MessagingException;

	/**
	 * Convenience method to do search on all messages.  Equivalent to
	 * seach(term, getMessages()).
	 * @param term Search term
	 * @returns Array list of messages
	 * @throws MessagingException Messaging exception occurred
	 */
	public Message[] search(SearchTerm term)
			throws MessagingException {
		return search(term, getMessages());
	} // search()

	/**
	 * Search the specified messages with the provided search criteria.
	 * @param term Search term
	 * @param messages Array list of messages to search
	 * @returns Array list of messages
	 * @throws MessagingException Messaging exception occurred
	 */
	public Message[] search(SearchTerm term, Message[] messages)
			throws MessagingException {

		// Variables
		int		index;
		Vector		list;
		Message[]	msgList;

		// Check all messages
		list = new Vector();
		for (index = 0; index < messages.length; index++) {
			if (term.match(messages[index]) == true) {
				list.addElement(messages[index]);
			} // if
		} // for: index

		// Create Array List of results
		msgList = new Message[list.size()];
		for (index = 0; index < list.size(); index++) {
			msgList[index] = (Message) list.elementAt(index);
		} // for: index

		// Return results
		return msgList;

	} // search()

	/**
	 * Open this folder.
	 * @param mode Mode to open folder with
	 * @throws MessagingException Messaging exception occurred
	 */
	public abstract void open(int mode)
		throws MessagingException;

	/**
	 * Close this folder.
	 * @param expunge Permanently remove deleted messages
	 * @throws MessagingException Messaging exception occurred
	 */
	public abstract void close(boolean expunge)
		throws MessagingException;

	/**
	 * Determine if this folder actually exists on the Store.
	 * @returns true if it does, false otherwise
	 * @throws MessagingException Messaging exception occurred
	 */
	public abstract boolean exists() throws MessagingException;

	/**
	 * Rename this folder. Valid only on a closed folder.
	 * @param folder Folder to rename this folder to
	 * @returns true if successful, false otherwise
	 * @throws MessagingException Messaging exception occurred
	 */
	public abstract boolean renameTo(Folder folder)
		throws MessagingException;

	/**
	 * Get separator character that separates this folder's pathname
	 * from it's subfolders.
	 * @returns Separator char
	 * @throws MessagingException Messaging exception occurred
	 */
	public abstract char getSeparator()
		throws MessagingException;

	/**
	 * Add connection listener to folder.
	 * @param listener Connection listener
	 */
	public synchronized void addConnectionListener(ConnectionListener listener) {
		if (connectionListeners.contains(listener) == false) {
			connectionListeners.addElement(listener);
		}
	} // addConnectionListener()

	/**
	 * Add folder listener to folder.
	 * @param listener Folder listener
	 */
	public synchronized void addFolderListener(FolderListener listener) {
		if (folderListeners.contains(listener) == false) {
			folderListeners.addElement(listener);
		}
	} // addFolderListener()

	/**
	 * Add message changed listener to folder.
	 * @param listener Message changed listener
	 */
	public synchronized void addMessageChangedListener(MessageChangedListener listener) {
		if (messageChangedListeners.contains(listener) == false) {
			messageChangedListeners.addElement(listener);
		}
	} // addMessageChangedListener()

	/**
	 * Add message count listener to folder.
	 * @param listener Message count listener
	 */
	public synchronized void addMessageCountListener(MessageCountListener listener) {
		if (messageListeners.contains(listener) == false) {
			messageListeners.addElement(listener);
		}
	} // addMessageCountListener()

	/**
	 * Append specified messages to this folder.
	 * @param messages Messages to append to folder
	 * @throws MessagingException Messaging exception occurred
	 */
	public abstract void appendMessages(Message[] messages)
		throws MessagingException;

	/**
	 * Copy the specified messages to the folder provided.  Note that
	 * all the messages must exist in this folder.
	 * @param messages Messages to copy
	 * @param folder Destination folder to copy messages to
	 * @throws MessagingException Messaging exception occurred
	 */
	public void copyMessages(Message[] messages, Folder folder)
			throws MessagingException {
		folder.appendMessages(messages);
	} // copyMessages()

	/**
	 * Create this folder on the store.
	 * @param type Type of this folder
	 * @returns true if successful, false otherwise
	 * @throws MessagingException Messaging exception occurred
	 */
	public abstract boolean create(int type)
		throws MessagingException;

	/**
	 * Permanently removed deleted messages in this folder.
	 * @returns Array list of expunged messages
	 * @throws MessagingException Messaging exception occurred
	 */
	public abstract Message[] expunge() throws MessagingException;

	/**
	 * Fetch Messages based on a particular profile.  By default,
	 * this implementation doesn't do anything useful.
	 * @param message Fetch items for these messages
	 * @param profile Fetch profile
	 */
	public void fetch(Message[] messages, FetchProfile profile)
			throws MessagingException {
		// Do nothing for now
	} // fetch()

	/**
	 * Get the folder that corresponds to the specified name.
	 * @param name Name of folder
	 * @returns Folder
	 * @throws MessagingException Messaging exception occurred
	 */
	public abstract Folder getFolder(String name)
		throws MessagingException;

	/**
	 * Get the full name of this folder.f
	 * @returns Full name of folder
	 */
	public abstract String getFullName();

	/**
	 * Get the number of messages in this folder.
	 * @returns Number of messages
	 * @throws MessagingException Messaging exception occurred
	 */
	public abstract int getMessageCount() throws MessagingException;

	/**
	 * Get the messages that correspond to the specified start-end
	 * range.  Note that messages numbers are indexed from 1 and
	 * both the start and end are inclusive.
	 * @param start Starting message number
	 * @param end Ending message number
	 * @returns Array list of messages
	 * @throws MessagingException Messaging exception occurred
	 */
	public synchronized Message[] getMessages(int start, int end)
			throws MessagingException {

		// Variables
		Message[]	msgList;
		int		index;

		// Create Message list
		msgList = new Message[end - start + 1];

		// Get Messages
		for (index = 0; index < (end - start + 1); index++) {
			msgList[index] = getMessage(index);
		} // for: index

		// Return List
		return msgList;

	} // getMessages()

	/**
	 * Get the messages that correspond to the specified index
	 * range.  Note that messages numbers are indexed from 1.
	 * @param messageNums Message numbers to get
	 * @returns Array list of messages
	 * @throws MessagingException Messaging exception occurred
	 */
	public synchronized Message[] getMessages(int[] messageNums)
			throws MessagingException {

		// Variables
		Message[]	msgList;
		int		index;

		// Create Message list
		msgList = new Message[messageNums.length];

		// Get Messages
		for (index = 0; index < messageNums.length; index++) {
			msgList[index] = getMessage(messageNums[index]);
		} // for: index

		// Return List
		return msgList;

	} // getMessages()

	/**
	 * Get all the messages in this folder.
	 * @returns Array list of messages
	 * @throws MessagingException Messaging exception occurred
	 */
	public synchronized Message[] getMessages()
			throws MessagingException {

		// Variables
		Message[]	msgList;
		int		index;
		int		count;

		// Create Message list
		count = getMessageCount();
		msgList = new Message[count];

		// Get Messages
		for (index = 0; index < count; index++) {
			msgList[index] = getMessage(index + 1);
		} // for: index

		// Return List
		return msgList;

	} // getMessages()

	/**
	 * Get the open mode for this folder.
	 * @returns Open mode
	 */
	public int getMode() {
		return mode;
	} // getMode()

	/**
	 * Get count of new messages.
	 * @returns Number of new messages
	 * @throws MessagingException Messaging exception occurred
	 */
	public synchronized int getNewMessageCount()
			throws MessagingException {

		// Variables
		Message[]	messages;
		int		index;
		int		count;

		// Check Opened state
		if (isOpen() == false) {
			return -1;
		}

		// Get Messages
		messages = getMessages();

		// Check Messages
		count = 0;
		for (index = 0; index < messages.length; index++) {
			if (messages[index].isSet(Flags.Flag.RECENT) == true) {
				count += 1;
			} // if
		} // for: index

		// Return Count
		return count;

	} // getNewMessageCount()

	/**
	 * Get the flags that are supported by this folder.  The flag
	 * Flags.USER indicates that user-defined flags are supported.
	 * @returns Flags object
	 */
	public abstract Flags getPermanentFlags();

	/**
	 * Get store for this folder.
	 * @returns Folder store
	 */
	public Store getStore() {
		return store;
	} // getStore()

	/**
	 * Get a URL name that represents this folder.
	 * @returns URL Name
	 * @throws MessagingException Messaging exception occurred
	 */
	public URLName getURLName() throws MessagingException {
		return null; // TODO
	} // getURLName()

	/**
	 * Get count of unread messages.
	 * @returns Number of unread messages
	 * @throws MessagingException Messaging exception occurred
	 */
	public synchronized int getUnreadMessageCount()
			throws MessagingException {

		// Variables
		Message[]	messages;
		int		index;
		int		count;

		// Check Opened state
		if (isOpen() == false) {
			return -1;
		}

		// Get Messages
		messages = getMessages();

		// Check Messages
		count = 0;
		for (index = 0; index < messages.length; index++) {
			if (messages[index].isSet(Flags.Flag.SEEN) == false) {
				count += 1;
			} // if
		} // for: index

		// Return Count
		return count;

	} // getUnreadMessageCount()

	/**
	 * Light0weight check to determine if new messages exist.
	 * @returns true if they exist, false otherwise
	 * @throws MessagingException Messaging exception occurred
	 */
	public abstract boolean hasNewMessages()
		throws MessagingException;

	/**
	 * Determine if this folder is in the open state.
	 * @returns true if open, false otherwise
	 */
	public abstract boolean isOpen();

	/**
	 * Determine if this folder is subscribed. Defaults to true.
	 * @returns true if subscribed, false otherwise
	 */
	public boolean isSubscribed() {
		return true;
	} // isSubscribed()

	/**
	 * List subscribed sub folders.  This implementation performs
	 * a simple list().
	 * @param pattern Pattern to search with
	 * @returns Array list of folders that are subscribed to
	 * @throws MessagingException Messaging exception occurred
	 */
	public Folder[] listSubscribed(String pattern)
			throws MessagingException {
		return list(pattern);
	} // listSubscribed()

	/**
	 * A convenience method to list all subscribed folders of
	 * this folder.  Equivalent to listSubscribed("%")
	 * @returns Array list of folders that are subscribed to
	 * @throws MessagingException Messaging exception occurred
	 */
	public Folder[] listSubscribed() throws MessagingException {
		return listSubscribed("%");
	} // listSubscribed()

	protected void notifyConnectionListeners(int type) {

		// Variables
		ConnectionEvent event;

		// Check Listener List
		if (connectionListeners.size() == 0) {
			return;
		} // if

		// Create Connection Event
		event = new ConnectionEvent(this, type);

		// Queue Notification
		queueEvent(event, (Vector) connectionListeners.clone());

	} // notifyConnectionListeners()

	protected void notifyFolderListeners(int type) {

		// Variables
		FolderEvent     event;

		// Create Event
		event = new FolderEvent(this, this, type);

		// Queue Event
		queueEvent(event, (Vector) folderListeners.clone());

	} // notifyFolderListeners()

	protected void notifyFolderRenamedListeners(Folder folder) {

		// Variables
		FolderEvent     event;

		// Create Event
		event = new FolderEvent(this, this, folder, FolderEvent.RENAMED);

		// Queue Event
		queueEvent(event, (Vector) folderListeners.clone());

	} // notifyFolderRenamedListeners()

	protected void notifyMessageAddedListeners(Message[] messages) {

		// Variables
		MessageCountEvent	event;

		// Create Event
		event = new MessageCountEvent(this, MessageCountEvent.ADDED,
										false, messages);

		// Queue Event
		queueEvent(event, (Vector) messageListeners.clone());

	} // notifyMessageAddedListeners()

	protected void notifyMessageRemovedListeners(boolean removed,
						Message[] messages) {

		// Variables
		MessageCountEvent	event;

		// Create Event
		event = new MessageCountEvent(this, MessageCountEvent.REMOVED,
										removed, messages);

		// Queue Event
		queueEvent(event, (Vector) messageListeners.clone());

	} // notifyMessageRemovedListeners()

	protected void notifyMessageChangedListeners(int type, Message message) {

		// Variables
		MessageChangedEvent	event;

		// Create Event
		event = new MessageChangedEvent(this, type, message);

		// Queue Event
		queueEvent(event, (Vector) messageChangedListeners.clone());

	} // notifyMessageChangedListeners()

	private synchronized void queueEvent(MailEvent event, Vector list) {

		// Queue Event
		q.enqueue(event, list);

	} // queueEvent()

	/**
	 * Remove connection listener from the folder.
	 * @param listener Connection listener
	 */
	public synchronized void removeConnectionListener(ConnectionListener listener) {
		if (connectionListeners.contains(listener) == true) {
			connectionListeners.remove(listener);
		}
	} // removeConnectionListener()

	/**
	 * Remove folder listener from the folder.
	 * @param listener Connection listener
	 */
	public synchronized void removeFolderListener(FolderListener listener) {
		if (folderListeners.contains(listener) == true) {
			folderListeners.remove(listener);
		}
	} // removeFolderListener()

	/**
	 * Remove message changed listener from the folder.
	 * @param listener Connection listener
	 */
	public synchronized void removeMessageChangedListener(MessageChangedListener listener) {
		if (messageChangedListeners.contains(listener) == true) {
			messageChangedListeners.remove(listener);
		}
	} // removeMessageChangedListener()

	/**
	 * Remove message count listener from the folder.
	 * @param listener Connection listener
	 */
	public synchronized void removeMessageCountListener(MessageCountListener listener) {
		if (messageListeners.contains(listener) == true) {
			messageListeners.remove(listener);
		}
	} // removeMessageCountListener()

	public synchronized void setFlags(Message[] messages, Flags flag, boolean value)
			throws MessagingException {

		// Variables
		int	index;

		// Process Each Message
		for (index = 0; index < messages.length; index++) {
			messages[index].setFlags(flag, value);
		} // for

	} // setFlags()

	public synchronized void setFlags(int start, int end, Flags flag, boolean value)
			throws MessagingException {

		// Variables
		int	index;

		// Process Each Message
		for (index = start; index <= end; index++) {

			// Ensure Message # is greater than 0
			if (index > 0) {
				getMessage(index).setFlags(flag, value);
			} // if

		} // for

	} // setFlags()

	public synchronized void setFlags(int[] messageNums, Flags flag, boolean value)
			throws MessagingException {

		// Variables
		int	index;
		int	msgnum;

		// Process Each Message
		for (index = 0; index < messageNums.length; index++) {

			// Ensure Message # is greater than 0
			msgnum = messageNums[index];
			if (msgnum > 0) {
				getMessage(msgnum).setFlags(flag, value);
			} // if

		} // for

	} // setFlags()

	/**
	 * Set the subscribed state of this folder.  Note that not all
	 * stores support this.  Default implementation throws
	 * MethodNotSupportedException.
	 * @param subscribe Subscribed state
	 * @throws MessagingException Messaging exception occurred
	 */
	public void setSubscribed(boolean subscribe) throws MessagingException {
		throw new MethodNotSupportedException();
	} // setSubscribed()

	private synchronized void terminateQueue() {

		// Variables
		MailEvent       event;
		Vector          list;

		// Create Service Completion event
		event = new MailEvent(this) {
			public void dispatch(Object listener) {
			} // dispatch()
		};

		// Create Listeners
		list = new Vector();
		list.addElement(null);

		// Queue Event
		queueEvent(event, list);

	} // terminateQueue()


} // Folder
