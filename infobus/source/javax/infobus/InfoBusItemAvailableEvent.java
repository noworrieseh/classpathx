/*
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */
package javax.infobus;

// Imports
import java.awt.datatransfer.DataFlavor;

/**
 * InfoBus Item Available Event.
 * @author Andrew Selkirk
 * @version $Revision: 1.3 $
 */
public final	class	InfoBusItemAvailableEvent
				extends	InfoBusEvent {

	//-------------------------------------------------------------
	// Variables --------------------------------------------------
	//-------------------------------------------------------------

	/**
	 * Data flavors
	 */
	private	DataFlavor[]	flavors	= null;


	//-------------------------------------------------------------
	// Initialization ---------------------------------------------
	//-------------------------------------------------------------

	/**
	 * Create InfoBusItemAvailable event.
	 * @param dataItemName Data item name
	 * @param dataFlavor Data flavor
	 * @param producer InfoBus Data Producer
	 */
	protected InfoBusItemAvailableEvent(String			dataItemName,
									DataFlavor[]		dataFlavor, 
									InfoBusDataProducer	producer) {
		super(dataItemName, producer);
		this.flavors = dataFlavor;
	} // InfoBusItemAvailableEvent()


	//-------------------------------------------------------------
	// Public Accessor Methods ------------------------------------
	//-------------------------------------------------------------

	/**
	 * Get Data flavors.
	 * @return Data flavors
	 */
	public DataFlavor[] getDataFlavors() {
		return flavors;
	} // getDataFlavors()

	/**
	 * Request Data Item.
	 * @param consumer Data Consumer
	 * @param flavors Data flavors
	 * @return TODO
	 */
	public Object requestDataItem(InfoBusDataConsumer	consumer, 
							  	DataFlavor[]			flavors) {

		// Variables
		String						itemName;
		InfoBusItemRequestedEvent	event;
		InfoBusDataProducer			producer;

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
	 * @return InfoBus Data Producer
	 */
	public InfoBusDataProducer getSourceAsProducer() {
		return (InfoBusDataProducer) getSource();
	} // getSourceAsProducer()


} // InfoBusItemAvailableEvent
