/********************************************************************
 * Copyright (c) Open Java Extensions, Andrew Selkirk  LGPL License *
 ********************************************************************/

package javax.infobus;

/**
 * Data Item Revoked event.
 */
public final	class	DataItemRevokedEvent
		extends	DataItemChangeEvent {

	//-------------------------------------------------------------
	// Initialization ---------------------------------------------
	//-------------------------------------------------------------

	/**
	 * Create Data Item revoked event.
	 * @param source Object source
	 * @param changedItem Item that changed
	 * @param propertyMap Property map
	 */
	public DataItemRevokedEvent(Object source, Object changedItem, 
							InfoBusPropertyMap propertyMap) {
		super(source, changedItem, propertyMap);
	} // DataItemRevokedEvent()


} // DataItemRevokedEvent
