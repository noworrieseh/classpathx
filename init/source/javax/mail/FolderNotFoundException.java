/********************************************************************
 * Copyright (c) Open Java Extensions, Andrew Selkirk  LGPL License *
 ********************************************************************/

package javax.mail;

/**
 * Folder Not Found Exception
 */
public class FolderNotFoundException extends MessagingException {

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
	 * Create new folder not found exception.
	 */
	public FolderNotFoundException() {
		super();
	} // FolderNotFoundException()

	/**
	 * Create new folder not found exception with description.
	 * @param message Description
	 * @param folder Folder
	 */
	public FolderNotFoundException(String message, Folder folder) {
		super(message);
		this.folder = folder;
	} // FolderNotFoundException()


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


} // FolderNotFoundException
