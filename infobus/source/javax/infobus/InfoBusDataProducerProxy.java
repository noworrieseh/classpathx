/********************************************************************
 * Copyright (c) Open Java Extensions, Andrew Selkirk  LGPL License *
 ********************************************************************/

package javax.infobus;

// Imports
import java.beans.PropertyChangeEvent;

/**
 * InfoBus Producer Proxy.
 */
public	class		InfoBusDataProducerProxy
	implements	InfoBusDataProducer {

	//-------------------------------------------------------------
	// Variables --------------------------------------------------
	//-------------------------------------------------------------

	/**
	 * Proxied InfoBus Data Producer
	 */
	private	InfoBusDataProducer	m_parent	= null;


	//-------------------------------------------------------------
	// Initialization ---------------------------------------------
	//-------------------------------------------------------------

	/**
	 * Create InfoBusDataProducer proxy.
	 * @param parent Proxied producer
	 */
	public InfoBusDataProducerProxy(InfoBusDataProducer parent) {
		m_parent = parent;
	} // InfoBusDataProducerProxy()


	//-------------------------------------------------------------
	// Public Accessor Methods ------------------------------------
	//-------------------------------------------------------------

	/**
	 * Request Data Item event proxy.
	 * @param event Item requested event
	 */
	public void dataItemRequested(InfoBusItemRequestedEvent event) {
		m_parent.dataItemRequested(event);
	} // dataItemRequested()

	/**
	 * Property change proxy.
	 * @param event Property change event
	 */
	public void propertyChange(PropertyChangeEvent event) {
		m_parent.propertyChange(event);
	} // propertyChange()


} // InfoBusDataProducerProxy
