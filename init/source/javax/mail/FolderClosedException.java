/********************************************************************
 * Copyright (c) Open Java Extensions, Andrew Selkirk  LGPL License *
 ********************************************************************/

package javax.mail;

/**
 * Folder Closed Exception
 */
public class FolderClosedException extends MessagingException {

	//-------------------------------------------------------------
	// Variables --------------------------------------------------
	//-------------------------------------------------------------

	/**
	 * Folder
	 */
	private	transient	Folder	folder	= null;


	//-------------------------------------------------------------
	// Initialization ---------------------------------------------
	//-------------------------------------------------------------

	/**
	 * Create new folder closed exception.
	 */
	public FolderClosedException() {
		super();
	} // FolderClosedException()

	/**
	 * Create new folder closed exception with description.
	 * @param message Description
	 * @param folder Folder
	 */
	public FolderClosedException(String message, Folder folder) {
		super(message);
		this.folder = folder;
	} // FolderClosedException()


	//-------------------------------------------------------------
	// Public Accessor Methods ------------------------------------
	//-------------------------------------------------------------

	/**
	 * Get folder.
	 * @returns Folder
	 */
	public Folder getFolder() {
		return folder;
	} // getFolder()


} // FolderClosedException

