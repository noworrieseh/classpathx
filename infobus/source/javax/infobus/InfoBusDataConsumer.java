/********************************************************************
 * Copyright (c) Open Java Extensions, Andrew Selkirk  LGPL License *
 ********************************************************************/

package javax.infobus;

/**
 * InfoBus Data Consumer
 */
public abstract interface	InfoBusDataConsumer
		extends		InfoBusEventListener {

	//-------------------------------------------------------------
	// Interface: InfoBusDataConsumer -----------------------------
	//-------------------------------------------------------------

	/**
	 * Data item available.
	 * @param event Item available event
	 */
	public void dataItemAvailable(InfoBusItemAvailableEvent event);

	/**
	 * Data item revoked.
	 * @param event Item revoked event
	 */
	public void dataItemRevoked(InfoBusItemRevokedEvent event);


} // InfoBusDataConsumer
