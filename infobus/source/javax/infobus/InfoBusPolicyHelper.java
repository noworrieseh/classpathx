/********************************************************************
 * Copyright (c) Open Java Extensions, Andrew Selkirk  LGPL License *
 ********************************************************************/

package javax.infobus;

// Imports
import java.beans.PropertyChangeEvent;

/**
 * InfoBus Policy Helper.
 */
public abstract interface InfoBusPolicyHelper {

	//-------------------------------------------------------------
	// Interface: InfoBusPolicyHelper -----------------------------
	//-------------------------------------------------------------

	/**
	 * Generate default InfoBus name from object.
	 * @param object Object to generate name from
	 * @returns Generated InfoBus name, or null
	 */
	public String generateDefaultName(Object object);

	/**
	 * Check permission to get InfoBus.
	 * @param busName Infobus name
	 */
	public void canGet(String busName);

	/**
	 * Check permission to join InfoBus.
	 * @param infobus InfoBus
	 * @param member InfoBus member
	 */
	public void canJoin(InfoBus infobus, InfoBusMember member);

	/**
	 * Check permission to register InfoBus member.
	 * @param infobus InfoBus
	 * @param member InfoBus member
	 */
	public void canRegister(InfoBus infobus, InfoBusMember member);

	/**
	 * Check permission to send PropertyChange to InfoBus.
	 * @param infobus InfoBus
	 * @param event Property Change event
	 */
	public void canPropertyChange(InfoBus				infobus, 
							  PropertyChangeEvent	event);

	/**
	 * Check permission to add Data Controller to InfoBus.
	 * @param infobus InfoBus
	 * @param controller Data controller
	 * @param priority Priority of controller
	 */
	public void canAddDataController(InfoBus				infobus, 
								 InfoBusDataController	controller, 
								 int					priority);

	/**
	 * Check permission to add Data Producer to InfoBus.
	 * @param infobus InfoBus
	 * @param producer Data Producer
	 */
	public void canAddDataProducer(InfoBus 				infobus, 
							   InfoBusDataProducer	producer);

	/**
	 * Check permission to add Data Consumer to InfoBus.
	 * @param infobus InfoBus
	 * @param consumer Data Consumer
	 */
	public void canAddDataConsumer(InfoBus				infobus, 
							   InfoBusDataConsumer	consumer);

	/**
	 * Check permission to fire available item on InfoBus.
	 * @param infobus InfoBus
	 * @param dataItemName Data item name
	 * @param producer Data Producer
	 */
	public void canFireItemAvailable(InfoBus				infobus, 
								 String					dataItemName, 
								 InfoBusDataProducer	producer);

	/**
	 * Check permission to fire revokoed item on InfoBus.
	 * @param infobus InfoBus
	 * @param dataItemName Data item name
	 * @param producer Data Producer
	 */
	public void canFireItemRevoked(InfoBus				infobus, 
							   String				dataItemName, 
							   InfoBusDataProducer	producer);

	/**
	 * Check permission to request item on InfoBus.
	 * @param infobus InfoBus
	 * @param dataItemName Data item name
	 * @param consumer Data Consumer
	 */
	public void canRequestItem(InfoBus				infobus, 
						   String				dataItemName, 
						   InfoBusDataConsumer	consumer);


} // InfoBusPolicyHelper
