/********************************************************************
 * Copyright (c) Open Java Extensions, Andrew Selkirk  LGPL License *
 ********************************************************************/

package javax.infobus;

/**
 * InfoBus Data Producer.
 */
public abstract interface	InfoBusDataProducer
		extends		InfoBusEventListener {

	//-------------------------------------------------------------
	// Interface: InfoBusDataProducer -----------------------------
	//-------------------------------------------------------------

	/**
	 * Data item requested.
	 * @param event Item requested event
	 */
	public void dataItemRequested(InfoBusItemRequestedEvent event);


} // InfoBusDataProducer
