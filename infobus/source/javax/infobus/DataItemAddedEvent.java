/********************************************************************
 * Copyright (c) Open Java Extensions, Andrew Selkirk  LGPL License *
 ********************************************************************/

package javax.infobus;

/**
 * Data Item Added event.
 */
public final	class	DataItemAddedEvent
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
	 * Create data item added event.
	 * @param source Source object
	 * @param changedItem Changed item
	 * @param changedCollection Changed collection
	 * @param propertyMap Property map
	 */
	public DataItemAddedEvent(Object source, Object changedItem, 
						  Object				changedCollection, 
						  InfoBusPropertyMap	propertyMap) {
		super(source, changedItem, propertyMap);
		this.changedCollection = changedCollection;
	} // DataItemAddedEvent()


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


} // DataItemAddedEvent
