/********************************************************************
 * Copyright (c) Open Java Extensions, Andrew Selkirk  LGPL License *
 ********************************************************************/

package javax.infobus;

/**
 * InfoBus Property Map.
 */
public abstract interface InfoBusPropertyMap {

	//-------------------------------------------------------------
	// Interface: InfoBusPropertyMap ------------------------------
	//-------------------------------------------------------------

	/**
	 * Get value mapped from specified key.
	 * @param key Property key
	 * @returns Mapped value
	 */
	public Object get(Object key);


} // InfoBusPropertyMap
