/********************************************************************
 * Copyright (c) Open Java Extensions, Andrew Selkirk  LGPL License *
 ********************************************************************/

package javax.infobus;

/**
 * InfoBus Item Revoked Event.
 */
public final	class	InfoBusItemRevokedEvent
		extends	InfoBusEvent {

	//-------------------------------------------------------------
	// Initialization ---------------------------------------------
	//-------------------------------------------------------------

	/**
	 * Create InfoBusItemRevoked event.
	 * @param dataItemName Data item name
	 * @param producer InfoBus Data Producer
	 */
	protected InfoBusItemRevokedEvent(String				dataItemName, 
								  InfoBusDataProducer	producer) {
		super(dataItemName, producer);
	} // InfoBusItemRevokedEvent()


	//-------------------------------------------------------------
	// Public Accessor Methods ------------------------------------
	//-------------------------------------------------------------

	/**
	 * Get source as a Data Producer.  Convience method.
	 * @returns InfoBus Data Producer
	 */
	public InfoBusDataProducer getSourceAsProducer() {
		return (InfoBusDataProducer) getSource();
	} // getSourceAsProducer()


} // InfoBusItemRevokedEvent
