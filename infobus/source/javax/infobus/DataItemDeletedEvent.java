/********************************************************************
 * Copyright (c) Open Java Extensions, Andrew Selkirk  LGPL License *
 ********************************************************************/

package javax.infobus;

/**
 * Data Item Deleted event.
 */
public final	class	DataItemDeletedEvent
		extends	DataItemChangeEvent {

	//-------------------------------------------------------------
	// Variables --------------------------------------------------
	//-------------------------------------------------------------

	/**
	 * Changed collection.
	 */
	private	Object	changedCollection	= null;


	//-------------------------------------------------------------
	// Initialization ---------------------------------------------
	//-------------------------------------------------------------

	/**
	 * Create data item deleted event.
	 * @param source Object source
	 * @param changedItem Item that changed
	 * @param changedCollection Collection that changed
	 * @param propertyMap Property map
	 */
	public DataItemDeletedEvent(Object source, Object changedItem, 
							Object changedCollection, 
							InfoBusPropertyMap propertyMap) {
		super(source, changedItem, propertyMap);
		this.changedCollection = changedCollection;
	} // DataItemDeletedEvent()


	//-------------------------------------------------------------
	// Public Accessor Methods ------------------------------------
	//-------------------------------------------------------------

	/**
	 * Get changed collection.
	 * @returns Changed collection
	 */
	public Object getChangedCollection() {
		return changedCollection;
	} // getChangedCollection()


} // DataItemDeletedEvent
