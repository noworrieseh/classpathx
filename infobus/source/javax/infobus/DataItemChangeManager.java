/********************************************************************
 * Copyright (c) Open Java Extensions, Andrew Selkirk  LGPL License *
 ********************************************************************/

package javax.infobus;

/**
 * Data Item Change Manager
 */
public abstract interface DataItemChangeManager {

	//-------------------------------------------------------------
	// Interface: DataItemChangeManager ---------------------------
	//-------------------------------------------------------------

	/**
	 * Add data item change listener.
	 * @param listener Data item change listener
	 */
	public void addDataItemChangeListener(DataItemChangeListener listener);

	/**
	 * Remove data item change listener.
	 * @param listener Data item change listener
	 */
	public void removeDataItemChangeListener(DataItemChangeListener listener);


} // DataItemChangeManager
