/********************************************************************
 * Copyright (c) Open Java Extensions, Andrew Selkirk  LGPL License *
 ********************************************************************/

package javax.mail.event;

// Imports
import java.util.EventListener;

/**
 * Folder Listener Interface.
 */
public interface FolderListener extends EventListener {

	//-------------------------------------------------------------
	// Interface: FolderListener ------------------------------
	//-------------------------------------------------------------

	/**
	 * Folder created.
	 * @param event Folder event
	 */
	public abstract void folderCreated(FolderEvent event);

	/**
	 * Folder deleted.
	 * @param event Folder event
	 */
	public abstract void folderDeleted(FolderEvent event);

	/**
	 * Folder renamed.
	 * @param event Folder event
	 */
	public abstract void folderRenamed(FolderEvent event);


} // FolderListener
