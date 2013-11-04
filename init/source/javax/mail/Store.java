/********************************************************************
 * Copyright (c) Open Java Extensions, Andrew Selkirk  LGPL License *
 ********************************************************************/

package javax.mail;

// Imports
import java.util.Vector;
import javax.mail.event.*;

/**
 * Store.
 */
public abstract class Store extends Service {

	//-------------------------------------------------------------
	// Variables --------------------------------------------------
	//-------------------------------------------------------------

	/**
	 * Store Listeners.
	 */
	private	Vector	storeListeners	= null;

	/**
	 * Folder Listeners.
	 */
	private	Vector	folderListeners	= null;


	//-------------------------------------------------------------
	// Initialization ---------------------------------------------
	//-------------------------------------------------------------

	/**
	 * Create new Store.
	 * @param session Session
	 * @param urlName URL Name
	 */
	protected Store(Session session, URLName urlName) {
		super(session, urlName);
		storeListeners = new Vector();
		folderListeners = new Vector();
	} // Store()


	//-------------------------------------------------------------
	// Public Accessor Methods ------------------------------------
	//-------------------------------------------------------------

	/**
	 * Add folder listener.
	 * @param listener Folder listener
	 */
	public synchronized void addFolderListener(FolderListener listener) {
		if (folderListeners.contains(listener) == false) {
			folderListeners.addElement(listener);
		}
	} // addFolderListener()

	/**
	 * Add store listener.
	 * @param listener Store listener
	 */
	public synchronized void addStoreListener(StoreListener listener) {
		if (storeListeners.contains(listener) == false) {
			storeListeners.addElement(listener);
		}
	} // addStoreListener()

	/**
	 * Get default folder.
	 * @returns Default folder
	 */
	public abstract Folder getDefaultFolder()
		throws MessagingException;

	/**
	 * Get folder
	 * @param folder Folder
	 * @returns Folder
	 */
	public abstract Folder getFolder(String folder)
		throws MessagingException;

	/**
	 * Get folder
	 * @param urlName Folder
	 * @returns Folder
	 */
	public abstract Folder getFolder(URLName urlName)
		throws MessagingException;

	/**
	 * Notify folder listeners.
	 * @param value1 Value
	 * @param folder Folder
	 */
	protected void notifyFolderListeners(int type, Folder folder) {

		// Variables
		FolderEvent	event;

		// Create Event
		event = new FolderEvent(this, folder, type);

		// Queue Event
		queueEvent(event, (Vector) folderListeners.clone());

	} // notifyFolderListeners()

	/**
	 * Notify folder renamed listeners.
	 * @param folder1 Folder
	 * @param folder2 Folder
	 */
	protected void notifyFolderRenamedListeners(Folder oldFolder, Folder newFolder) {

		// Variables
		FolderEvent	event;

		// Create Event
		event = new FolderEvent(this, oldFolder, newFolder, FolderEvent.RENAMED);

		// Queue Event
		queueEvent(event, (Vector) folderListeners.clone());

	} // notifyFolderRenamedListeners()

	/**
	 * Notify store listeners.
	 * @param value1 Value ?
	 * @param value2 Value2?
	 */
	protected void notifyStoreListeners(int type, String message) {

		// Variables
		StoreEvent	event;

		// Create Event
		event = new StoreEvent(this, type, message);

		// Queue Event
		queueEvent(event, (Vector) storeListeners.clone());

	} // notifyStoreListeners()

	/**
	 * Remove folder listener.
	 * @param listener Folder listener
	 */
	public synchronized void removeFolderListener(FolderListener listener) {
		if (folderListeners.contains(listener) == true) {
			folderListeners.removeElement(listener);
		}
	} // removeFolderListener()

	/**
	 * Remove store listener.
	 * @param listener Store listener
	 */
	public synchronized void removeStoreListener(StoreListener listener) {
		if (storeListeners.contains(listener) == true) {
			storeListeners.removeElement(listener);
		}
	} // removeStoreListener()


} // Store
