/********************************************************************
 * Copyright (c) Open Java Extensions, Andrew Selkirk  LGPL License *
 ********************************************************************/

package javax.infobus;

// Imports
import java.util.EventObject;

/**
 * Data Item Change Event.
 */
public 	class	DataItemChangeEvent
	extends	EventObject {

	//-------------------------------------------------------------
	// Variables --------------------------------------------------
	//-------------------------------------------------------------

	/**
	 * Source object.
	 */
	private	Object				source		= null;

	/**
	 * Changed item.
	 */
	private	Object				changedItem	= null;

	/**
	 * Property map.
	 */
	private	InfoBusPropertyMap	map			= null;


	//-------------------------------------------------------------
	// Initialization ---------------------------------------------
	//-------------------------------------------------------------

	/**
	 * Create data item change event.
	 * @param source Source object
	 * @param changedItem Changed item
	 * @param propertyMap Property map
	 */
	protected DataItemChangeEvent(Object source, Object changedItem, 
								InfoBusPropertyMap propertyMap) {
		super(source);
		this.source = source;
		this.changedItem = changedItem;
		this.map = propertyMap;
	} // DataItemChangeEvent()


	//-------------------------------------------------------------
	// Public Accessor Methods ------------------------------------
	//-------------------------------------------------------------

	/**
	 * Get source object.
	 * @returns Source object
	 */
	public Object getSource() {
		return source;
	} // getSource()

	/**
	 * Get changed item.
	 * @returns Changed item object
	 */
	public Object getChangedItem() {
		return changedItem;
	} // getChangedItem()

	/**
	 * Get property value
	 * @param propertyName Property name key
	 */
	public Object getProperty(String propertyName) {
		return map.get(propertyName);
	} // getProperty()

		
} // DataItemChangeEvent
