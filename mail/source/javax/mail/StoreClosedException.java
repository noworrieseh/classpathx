/********************************************************************
 * Copyright (c) Open Java Extensions, Andrew Selkirk  LGPL License *
 ********************************************************************/

package javax.mail;

/**
 * Store Closed Exception
 */
public class StoreClosedException extends MessagingException {

	//-------------------------------------------------------------
	// Variables --------------------------------------------------
	//-------------------------------------------------------------

	private transient	Store	store	= null;


	//-------------------------------------------------------------
	// Initialization ---------------------------------------------
	//-------------------------------------------------------------

	/**
	 * Create new store closed exception.
	 * @param store Store
	 */
	public StoreClosedException(Store store) {
		super();
		this.store = store;
	} // StoreClosedException()

	/**
	 * Create new store closed exception with description.
	 * @param store Store
	 * @param message Exception message
	 */
	public StoreClosedException(Store store, String message) {
		super(message);
		this.store = store;
	} // StoreClosedException()


	//-------------------------------------------------------------
	// Public Accessor Methods ------------------------------------
	//-------------------------------------------------------------

	/**
	 * Get Store.
	 * @returns Store
	 */
	public Store getStore() {
		return store;
	} // getStore()


} // StoreClosedException
