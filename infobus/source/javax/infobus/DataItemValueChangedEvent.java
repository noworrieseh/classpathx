/********************************************************************
 * Copyright (c) Open Java Extensions, Andrew Selkirk  LGPL License *
 ********************************************************************/

package javax.infobus;

/**
 * Data Item Value Changed event.
 */
public final	class	DataItemValueChangedEvent
		extends	DataItemChangeEvent {

	//-------------------------------------------------------------
	// Initialization ---------------------------------------------
	//-------------------------------------------------------------

	/**
	 * Create new DataItem value changed event.
	 * @param source Event source
	 * @param changedItem Item that changed
	 * @param propertyMap Property map
	 */
	public DataItemValueChangedEvent(Object				source, 
								 Object				changedItem, 
								 InfoBusPropertyMap	propertyMap) {
		super(source, changedItem, propertyMap);
	} // DataItemValueChangedEvent()


} // DataItemValueChangedEvent
