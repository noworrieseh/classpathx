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
import java.applet.Applet;
import java.awt.Component;
import java.beans.PropertyChangeEvent;

/**
 * Default Policy.
 * @author Andrew Selkirk
 * @version $Revision: 1.3 $
 */
public	class		DefaultPolicy
		implements	InfoBusPolicyHelper {

	//-------------------------------------------------------------
	// Public Accessor Methods ------------------------------------
	//-------------------------------------------------------------

	/**
	 * Generate default InfoBus name from object.
	 * @param object Object to generate name from
	 * @return Generated InfoBus name, or null
	 */
	public String generateDefaultName(Object object) {
		if (object instanceof Component) {
			return retrieveID((Component) object);
		} // if

		// Unable to process
		return null;

	} // generateDefaultName()

	/**
	 * Generate applet context InfoBus name.
	 * @param component Component in applet
	 * @return InfoBus name, or null
	 */
	private static String retrieveID(Component component) {

		// Variables
		Component	current;

		// Search For Applet Parent
		current = component;
		while (current != null && (current instanceof Applet) == false) {
			current = current.getParent();
		} // while

		// Check For Applet
		if (current instanceof Applet) {
			return ((Applet) current).getDocumentBase().toString();
		} // if

		// Applet not found, return null
		return null;

	} // retrieveID()

	/**
	 * Check permission to get InfoBus.
	 * @param busName Infobus name
	 */
	public void canGet(String busName) {
	} // canGet()

	/**
	 * Check permission to join InfoBus.
	 * @param infobus InfoBus
	 * @param member InfoBus member
	 */
	public void canJoin(InfoBus infobus, InfoBusMember member) {
	} // canJoin()

	/**
	 * Check permission to register InfoBus member.
	 * @param infobus InfoBus
	 * @param member InfoBus member
	 */
	public void canRegister(InfoBus infobus, InfoBusMember member) {
	} // canRegister()

	/**
	 * Check permission to send PropertyChange to InfoBus.
	 * @param infobus InfoBus
	 * @param event Property Change event
	 */
	public void canPropertyChange(InfoBus				infobus,
								  PropertyChangeEvent	event) {
	} // canPropertyChange()

	/**
	 * Check permission to add Data Controller to InfoBus.
	 * @param infobus InfoBus
	 * @param controller Data controller
	 * @param priority Priority of controller
	 */
	public void canAddDataController(InfoBus				infobus,
									 InfoBusDataController	controller,
									 int					priority) {
	} // canAddDataController()

	/**
	 * Check permission to add Data Producer to InfoBus.
	 * @param infobus InfoBus
	 * @param producer Data Producer
	 */
	public void canAddDataProducer(InfoBus				infobus,
								   InfoBusDataProducer	producer) {
	} // canAddDataProducer()

	/**
	 * Check permission to add Data Consumer to InfoBus.
	 * @param infobus InfoBus
	 * @param consumer Data Consumer
	 */
	public void canAddDataConsumer(InfoBus				infobus,
								   InfoBusDataConsumer	consumer) {
	} // canAddDataConsumer()

	/**
	 * Check permission to fire available item on InfoBus.
	 * @param infobus InfoBus
	 * @param dataItemName Data item name
	 * @param producer Data Producer
	 */
	public void canFireItemAvailable(InfoBus				infobus,
									 String					dataItemName,
									 InfoBusDataProducer	producer) {
	} // canFireItemAvailable()

	/**
	 * Check permission to fire revokoed item on InfoBus.
	 * @param infobus InfoBus
	 * @param dataItemName Data item name
	 * @param producer Data Producer
	 */
	public void canFireItemRevoked(InfoBus				infobus,
								   String				dataItemName,
								   InfoBusDataProducer	producer) {
	} // canFireItemRevoked()

	/**
	 * Check permission to request item on InfoBus.
	 * @param infobus InfoBus
	 * @param dataItemName Data item name
	 * @param consumer Data Consumer
	 */
	public void canRequestItem(InfoBus				infobus,
							   String				dataItemName,
							   InfoBusDataConsumer	consumer) {
	} // canRequestItem()


} // DefaultPolicy
