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
import java.util.EventObject;

/**
 * InfoBus Event.
 * @author Andrew Selkirk
 * @version $Revision: 1.3 $
 */
public	class	InfoBusEvent
		extends	EventObject {

	//-------------------------------------------------------------
	// Variables --------------------------------------------------
	//-------------------------------------------------------------

	/**
	 * Data Item name.
	 */
	private String dataItemName	= "";


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
		this.dataItemName = dataItemName;
	} // InfoBusEvent()


	//-------------------------------------------------------------
	// Public Accessor Methods ------------------------------------
	//-------------------------------------------------------------

	/**
	 * Get Data Item name.
	 * @return Data Item name
	 */
	public String getDataItemName() {
		return dataItemName;
	} // getDataItemName()


} // InfoBusEvent
