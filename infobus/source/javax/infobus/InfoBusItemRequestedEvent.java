/********************************************************************
 * Copyright (c) Open Java Extensions, Andrew Selkirk  LGPL License *
 ********************************************************************/

package javax.infobus;

// Imports
import java.awt.datatransfer.DataFlavor;

/**
 * InfoBus Item Requested Event.
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
