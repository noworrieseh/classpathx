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
import java.beans.PropertyChangeEvent;

/**
 * InfoBus Consumer Proxy.
 * @author Andrew Selkirk
 * @version $Revision: 1.2 $
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
