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
 * InfoBus Item Requested Event.
 * @author Andrew Selkirk
 * @version $Revision: 1.2 $
 */
public final	class	InfoBusItemRequestedEvent
		extends	InfoBusEvent {

	//-------------------------------------------------------------
	// Variables --------------------------------------------------
	//-------------------------------------------------------------

	/**
	 * Data Item object
	 */
	private	Object			m_dataItem	= null;

	/**
	 * Data flavors
	 */
	private	DataFlavor[]	m_flavors	= null;


	//-------------------------------------------------------------
	// Initialization ---------------------------------------------
	//-------------------------------------------------------------

	/**
	 * Create InfoBusItemRequested event.
	 * @param dataItemName Data item name
	 * @param dataFlavor Data flavor
	 * @param consumer InfoBus Data Consumer
	 */
	protected InfoBusItemRequestedEvent(String				dataItemName, 
									DataFlavor[]		dataFlavor, 
									InfoBusDataConsumer	consumer) {
		super(dataItemName, consumer);
		m_flavors = dataFlavor;
	} // InfoBusItemRequestedEvent()


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
	 * Get Data Item.
	 * @returns Data item
	 */
	public Object getDataItem() {
		return m_dataItem;
	} // getDataItem()

	/**
	 * Set Data Item.  Value is write once.
	 * @param item Data item object
	 */
	public void setDataItem(Object item) {

		// Write-once property
		if (m_dataItem == null) {
			m_dataItem = item;
		}

	} // setDataItem()

	/**
	 * Reset Data Item.
	 */
	protected void resetDataItem() {
		// TODO
	} // resetDataItem()

	/**
	 * Get source as a Data Consumer.  Convience method.
	 * @returns InfoBus Data Consumer
	 */
	public InfoBusDataConsumer getSourceAsConsumer() {
		return (InfoBusDataConsumer) getSource();
	} // getSourceAsComsumer()


} // InfoBusItemRequestedEvent
