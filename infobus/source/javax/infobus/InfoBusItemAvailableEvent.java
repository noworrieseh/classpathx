/********************************************************************
 * Copyright (c) Open Java Extensions, Andrew Selkirk  LGPL License *
 ********************************************************************/

package javax.infobus;

// Imports
import java.awt.datatransfer.DataFlavor;

/**
 * InfoBus Item Available Event.
 */
public final	class	InfoBusItemAvailableEvent
		extends	InfoBusEvent {

	//-------------------------------------------------------------
	// Variables --------------------------------------------------
	//-------------------------------------------------------------

	/**
	 * Data flavors
	 */
	private	DataFlavor[]	m_flavors	= null;


	//-------------------------------------------------------------
	// Initialization ---------------------------------------------
	//-------------------------------------------------------------

	/**
	 * Create InfoBusItemAvailable event.
	 * @param dataItemName Data item name
	 * @param dataFlavor Data flavor
	 * @param producer InfoBus Data Producer
	 */
	protected InfoBusItemAvailableEvent(String				dataItemName, 
									DataFlavor[]		dataFlavor, 
									InfoBusDataProducer	producer) {
		super(dataItemName, producer);
		m_flavors = dataFlavor;
	} // InfoBusItemAvailableEvent()


	//-------------------------------------------------------------
	// Public Accessor Methods ------------------------------------
	//-------------------------------------------------------------

	/**
	 * Get Data flavors.
	 * @returns Data flavors
	 */
	public DataFlavor[] getDataFlavors() {
		return m_flavors;
	} // getDataFlavors()

	/**
	 * Request Data Item.
	 * @param consumer Data Consumer
	 * @param flavors Data flavors
	 */
	public Object requestDataItem(InfoBusDataConsumer	consumer, 
							  DataFlavor[]			flavors) {

		// Variables
  		String				itemName;
  		InfoBusItemRequestedEvent	event;
    		InfoBusDataProducer		producer;

    		// Create Event
      		itemName = getDataItemName();
      		event = new InfoBusItemRequestedEvent(itemName, flavors, consumer);

        	// Send Event
         	producer = getSourceAsProducer();
          	producer.dataItemRequested(event);

          	// Return Data Item
		return event.getDataItem();

	} // requestDataItem()

	/**
	 * Get source as a Data Producer.  Convience method.
	 * @returns InfoBus Data Producer
	 */
	public InfoBusDataProducer getSourceAsProducer() {
		return (InfoBusDataProducer) getSource();
	} // getSourceAsProducer()


} // InfoBusItemAvailableEvent
