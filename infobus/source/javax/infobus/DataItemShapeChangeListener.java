/********************************************************************
 * Copyright (c) Open Java Extensions, Andrew Selkirk  LGPL License *
 ********************************************************************/

package javax.infobus;

/**
 * Data Item Shape Change listener interface.
 */
public abstract interface	DataItemShapeChangeListener
		extends		DataItemChangeListener {

	//-------------------------------------------------------------
	// Interface: DataItemShapeChangeListener ---------------------
	//-------------------------------------------------------------

	/**
	 * Process a data item shape changed event.
	 * @param event Event
	 */
	public void dataItemShapeChanged(DataItemShapeChangedEvent event);


} // DataItemShapeChangeListener
