/********************************************************************
 * Copyright (c) Open Java Extensions, Andrew Selkirk  LGPL License *
 ********************************************************************/

package javax.mail.event;

/**
 * Folder Adapter
 */
public abstract class FolderAdapter implements FolderListener {

	//-------------------------------------------------------------
	// Initialization ---------------------------------------------
	//-------------------------------------------------------------

	/**
	 * Create a new Folder Adapter.
	 */
	public FolderAdapter() {
	} // FolderAdapter()


	//-------------------------------------------------------------
	// Interface: FolderListener ------------------------------
	//-------------------------------------------------------------

	/**
	 * Folder created.
	 * @param event Folder event
	 */
	public void folderCreated(FolderEvent event) {
	} // folderCreated()

	/**
	 * Folder deleted.
	 * @param event Folder event
	 */
	public void folderDeleted(FolderEvent event) {
	} // folderDeleted()

	/**
	 * Folder renamed.
	 * @param event Folder event
	 */
	public void folderRenamed(FolderEvent event) {
	} // folderRenamed()


} // FolderAdapter
