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
 * InfoBus Producer Proxy.
 * @author Andrew Selkirk
 * @version $Revision: 1.3 $
 */
public	class		InfoBusDataProducerProxy
		implements	InfoBusDataProducer {

	//-------------------------------------------------------------
	// Variables --------------------------------------------------
	//-------------------------------------------------------------

	/**
	 * Proxied InfoBus Data Producer
	 */
	private	InfoBusDataProducer	parent	= null;


	//-------------------------------------------------------------
	// Initialization ---------------------------------------------
	//-------------------------------------------------------------

	/**
	 * Create InfoBusDataProducer proxy.
	 * @param parent Proxied producer
	 */
	public InfoBusDataProducerProxy(InfoBusDataProducer parent) {
		this.parent = parent;
	} // InfoBusDataProducerProxy()


	//-------------------------------------------------------------
	// Public Accessor Methods ------------------------------------
	//-------------------------------------------------------------

	/**
	 * Request Data Item event proxy.
	 * @param event Item requested event
	 */
	public void dataItemRequested(InfoBusItemRequestedEvent event) {
		parent.dataItemRequested(event);
	} // dataItemRequested()

	/**
	 * Property change proxy.
	 * @param event Property change event
	 */
	public void propertyChange(PropertyChangeEvent event) {
		parent.propertyChange(event);
	} // propertyChange()


} // InfoBusDataProducerProxy
