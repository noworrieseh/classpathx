/********************************************************************
 * Copyright (c) Open Java Extensions, Andrew Selkirk  LGPL License *
 ********************************************************************/

package javax.infobus;

// Imports
import java.util.EventObject;

/**
 * InfoBus Event.
 */
public	class	InfoBusEvent
	extends	EventObject {

	//-------------------------------------------------------------
	// Variables --------------------------------------------------
	//-------------------------------------------------------------

	/**
	 * Data Item name.
	 */
	private String m_dataItemName	= "";


	//-------------------------------------------------------------
	// Initialization ---------------------------------------------
	//-------------------------------------------------------------

	/**
	 * Create new InfoBusEvent.
	 * @param dataItemName Data Item name
	 * @param listener InfoBus event listener
	 */
	protected InfoBusEvent(String dataItemName, InfoBusEventListener listener) {
		super(listener);
		m_dataItemName = dataItemName;
	} // InfoBusEvent()


	//-------------------------------------------------------------
	// Public Accessor Methods ------------------------------------
	//-------------------------------------------------------------

	/**
	 * Get Data Item name.
	 * @returns Data Item name
	 */
	public String getDataItemName() {
		return m_dataItemName;
	} // getDataItemName()


} // InfoBusEvent
