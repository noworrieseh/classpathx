/********************************************************************
 * Copyright (c) Open Java Extensions, Andrew Selkirk  LGPL License *
 ********************************************************************/

package javax.mail.event;

// Imports
import javax.mail.*;

/**
 * Folder Event.
 */
public class FolderEvent extends MailEvent {

	//-------------------------------------------------------------
	// Variables --------------------------------------------------
	//-------------------------------------------------------------

	public static final int	CREATED		= 1;
	public static final int	DELETED		= 2;
	public static final int	RENAMED		= 3;

	/**
	 * Connection type of event.
	 */
	protected		int	type		= -1;

	/**
	 * Folder source of event.
	 */
	protected transient	Folder	folder		= null;

	/**
	 * New folder.
	 */
	protected transient	Folder	newFolder	= null;


	//-------------------------------------------------------------
	// Initialization ---------------------------------------------
	//-------------------------------------------------------------

	public FolderEvent(Object source, Folder folder, int type) {
		this(source, folder, null, type);
	} // FolderEvent()

	public FolderEvent(Object source, Folder oldFolder,
				Folder newFolder, int type) {
		super(source);
		this.folder = oldFolder;
		this.newFolder = newFolder;
		this.type = type;
	} // FolderEvent()


	//-------------------------------------------------------------
	// Methods ----------------------------------------------------
	//-------------------------------------------------------------

	/**
	 * Dispatch event to listener
	 * @param listener Listener to notify
	 */
	public void dispatch(Object listener) {

		// Variables
		FolderListener fListener;

		// Get Folder Listener
		if (listener instanceof FolderListener) {
			fListener = (FolderListener) listener;
		} else {
			return;
		}

		// Check Type
		switch (type) {
			case CREATED:
				fListener.folderCreated(this);
				break;
			case DELETED:
				fListener.folderDeleted(this);
				break;
			case RENAMED:
				fListener.folderRenamed(this);
				break;
		} // switch

	} // dispatch()

	/**
	 * Get folder event type.
	 * @returns Folder type
	 */
	public int getType() {
		return type;
	} // getType()

	/**
	 * Get folder.
	 * @returns Folder
	 */
	public Folder getFolder() {
		return folder;
	} // getFolder()

	/**
	 * Get new folder.
	 * @returns New folder
	 */
	public Folder getNewFolder() {
		return newFolder;
	} // getNewFolder()


} // FolderEvent
