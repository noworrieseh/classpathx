/********************************************************************
 * Copyright (c) Open Java Extensions, Andrew Selkirk  LGPL License *
 ********************************************************************/

package javax.infobus;

/**
 * Data Item Shape Changed event.
 */
public final	class	DataItemShapeChangedEvent
		extends	DataItemChangeEvent {

	//-------------------------------------------------------------
	// Initialization ---------------------------------------------
	//-------------------------------------------------------------

	/**
	 * Create new DataItem shape changed event.
	 * @param source Event source
	 * @param changedItem Item that changed
	 * @param propertyMap Property map
	 */
	public DataItemShapeChangedEvent(Object				source, 
								 Object				changedItem, 
								 InfoBusPropertyMap propertyMap) {
		super(source, changedItem, propertyMap);
	} // DataItemShapeChangedEvent()


} // DataItemShapeChangedEvent
