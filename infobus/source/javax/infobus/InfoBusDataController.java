/********************************************************************
 * Copyright (c) Open Java Extensions, Andrew Selkirk  LGPL License *
 ********************************************************************/

package javax.infobus;

// Imports
import java.util.Vector;
import java.awt.datatransfer.DataFlavor;

/**
 * InfoBus Data Controller.
 */
public abstract interface InfoBusDataController {

	//-------------------------------------------------------------
	// Interface: InfoBusDataController ---------------------------
	//-------------------------------------------------------------

	/**
	 * Set consumer list.
	 * @param consumers List of consumers
	 */
	public void setConsumerList(Vector consumers);

	/**
	 * Set producer list.
	 * @param producers List of producers
	 */
	public void setProducerList(Vector producers);

	/**
	 * Add data consumer.
	 * @param consumer Data consumer
	 */
	public void addDataConsumer(InfoBusDataConsumer consumer);

	/**
	 * Add data producer.
	 * @param producer Data producer
	 */
	public void addDataProducer(InfoBusDataProducer producer);

	/**
	 * Remove data consumer.
	 * @param consumer Data consumer
	 */
	public void removeDataConsumer(InfoBusDataConsumer consumer);

	/**
	 * Remove data producer.
	 * @param producer Data producer
	 */
	public void removeDataProducer(InfoBusDataProducer producer);

	/**
	 * Fire item available.
	 * @param dataItemName Data item name
	 * @param flavors Data flavors
	 * @param source Data producer source
	 * @returns true if successful, false otherwise
	 */
	public boolean fireItemAvailable(String					dataItemName, 
								 DataFlavor[]			flavors, 
								 InfoBusDataProducer	source);

	/**
	 * Fire item revoked.
	 * @param dataItemName Data item name
	 * @param source Data producer source
	 * @returns true if successful, false otherwise
	 */
	public boolean fireItemRevoked(String				dataItemName, 
							   InfoBusDataProducer	source);

	/**
	 * Find data item.
	 * @param dataItemName Data item name
	 * @param flavors Data flavors
	 * @param consumer Data consumer source
	 * @param foundItem List of found items
	 * @returns true if successful, false otherwise
	 */
	public boolean findDataItem(String				dataItemName, 
							DataFlavor[]		flavors, 
							InfoBusDataConsumer	consumer, 
							Vector				foundItem);

	/**
	 * Find multiple data items.
	 * @param dataItemName Data item name
	 * @param flavors Data flavors
	 * @param consumer Data consumer source
	 * @param foundItem List of found items
	 * @returns true if successful, false otherwise
	 */
	public boolean findMultipleDataItems(String					dataItemName, 
									 DataFlavor[]			flavors, 
									 InfoBusDataConsumer	consumer, 
									 Vector					foundItem);


} // InfoBusDataController
