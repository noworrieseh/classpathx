/********************************************************************
 * Copyright (c) Open Java Extensions, Andrew Selkirk  LGPL License *
 ********************************************************************/

package javax.infobus;

/**
 * Data Item interface.
 */
public abstract interface DataItem {

	//-------------------------------------------------------------
	// Interface: DataItem ----------------------------------------
	//-------------------------------------------------------------

	/**
	 * Get property value.
	 * @param propertyName Property key name
	 */
	public Object getProperty(String propertyName);

	/**
	 * Get event listener source.
	 * @returns InfoBus event listener source
	 */
	public InfoBusEventListener getSource();

	/**
	 * Release data item.
	 */
	public void release();


} // DataItem
