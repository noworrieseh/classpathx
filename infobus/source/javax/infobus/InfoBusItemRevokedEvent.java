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

/**
 * InfoBus Item Revoked Event.
 * @author Andrew Selkirk
 * @version $Revision: 1.2 $
 */
public final	class	InfoBusItemRevokedEvent
		extends	InfoBusEvent {

	//-------------------------------------------------------------
	// Initialization ---------------------------------------------
	//-------------------------------------------------------------

	/**
	 * Create InfoBusItemRevoked event.
	 * @param dataItemName Data item name
	 * @param producer InfoBus Data Producer
	 */
	protected InfoBusItemRevokedEvent(String				dataItemName, 
								  InfoBusDataProducer	producer) {
		super(dataItemName, producer);
	} // InfoBusItemRevokedEvent()


	//-------------------------------------------------------------
	// Public Accessor Methods ------------------------------------
	//-------------------------------------------------------------

	/**
	 * Get source as a Data Producer.  Convience method.
	 * @returns InfoBus Data Producer
	 */
	public InfoBusDataProducer getSourceAsProducer() {
		return (InfoBusDataProducer) getSource();
	} // getSourceAsProducer()


} // InfoBusItemRevokedEvent
