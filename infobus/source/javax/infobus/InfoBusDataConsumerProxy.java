/********************************************************************
 * Copyright (c) Open Java Extensions, Andrew Selkirk  LGPL License *
 ********************************************************************/

package javax.infobus;

// Imports
import java.beans.PropertyChangeEvent;

/**
 * InfoBus Consumer Proxy.
 */
public	class		InfoBusDataConsumerProxy
		implements	InfoBusDataConsumer {

	//-------------------------------------------------------------
	// Variables --------------------------------------------------
	//-------------------------------------------------------------

	/**
	 * Proxied InfoBus Data Consumer
	 */
	private	InfoBusDataConsumer	m_parent	= null;


	//-------------------------------------------------------------
	// Initialization ---------------------------------------------
	//-------------------------------------------------------------

	/**
	 * Create InfoBusDataConsumer proxy.
	 * @param parent Proxied consumer
	 */
	public InfoBusDataConsumerProxy(InfoBusDataConsumer parent) {
		m_parent = parent;
	} // InfoBusDataConsumerProxy()


	//-------------------------------------------------------------
	// Public Accessor Methods ------------------------------------
	//-------------------------------------------------------------

	/**
	 * Data Item available event proxy.
	 * @param event Item available event
	 */
	public void dataItemAvailable(InfoBusItemAvailableEvent event) {
		m_parent.dataItemAvailable(event);
	} // dataItemAvailable()

	/**
	 * Data Item revoked event proxy.
	 * @param event Item revoked event
	 */
	public void dataItemRevoked(InfoBusItemRevokedEvent event) {
		m_parent.dataItemRevoked(event);
	} // dataItemRevoked()

	/**
	 * Property change event proxy.
	 * @param event Property change event
	 */
	public void propertyChange(PropertyChangeEvent event) {
		m_parent.propertyChange(event);
	} // propertyChange()


} // InfoBusDataConsumerProxy
