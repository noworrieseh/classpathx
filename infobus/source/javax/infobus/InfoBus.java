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
import java.awt.Component;
import java.awt.datatransfer.DataFlavor;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyVetoException;
import java.util.Enumeration;
import java.util.Vector;

/**
 * InfoBus.
 * @author Andrew Selkirk
 * @version $Revision: 1.3 $
 */
public final	class		InfoBus
				implements	PropertyChangeListener {

	//-------------------------------------------------------------
	// Variables --------------------------------------------------
	//-------------------------------------------------------------

	/**
	 * Static list of InfoBus instances
	 */
	private static	Vector				infoBusList		= new Vector();

	/**
	 * Security policy helper
	 */
	private	static	InfoBusPolicyHelper	ibPolicy		= new DefaultPolicy();

	/**
	 * Static synchronization locking object
	 */
	private	static	Object				syncLock		= new Object();

	/**
	 * Synchronization locking object
	 */
	private			Object				objectLock		= new Object();

	/**
	 * Debugging output flag
	 */
	private	static final	boolean		DEBUG_OUTPUT	= false;

	/**
	 * TODO
	 */
	public	static final	String		MIME_TYPE_IMMEDIATE_ACCESS			=
		"application/x-java-infobus;class=javax.infobus.ImmediateAccess";

	/**
	 * TODO
	 */
	public	static final	String		MIME_TYPE_ARRAY_ACCESS				=
		"application/x-java-infobus;class=javax.infobus.ArrayAccess";

	/**
	 * TODO
	 */
	public	static final	String		MIME_TYPE_RESHAPEABLEARRAY_ACCESS	=
		"application/x-java-infobus;class=javax.infobus.ReshapeableArrayAccess";

	/**
	 * TODO
	 */
	public	static final	String		MIME_TYPE_ROWSET_ACCESS				=
		"application/x-java-infobus;class=javax.infobus.RowsetAccess";

	/**
	 * TODO
	 */
	public	static final	String		MIME_TYPE_SCROLLABLEROWSET_ACCESS	=
		"application/x-java-infobus;class=javax.infobus.ScrollableRowsetAccess";

	/**
	 * TODO
	 */
	public	static final	String		MIME_TYPE_DB_ACCESS					=
		"application/x-java-infobus;class=javax.infobus.DbAccess";

	/**
	 * TODO
	 */
	public	static final	String		MIME_TYPE_COLLECTION				=
		"application/x-java-infobus;class=java.util.Collection";

	/**
	 * TODO
	 */
	public	static final	String		MIME_TYPE_MAP						=
		"application/x-java-infobus;class=java.util.Map";

	/**
	 * TODO
	 */
	public	static final	String		MIME_TYPE_SET						=
		"application/x-java-infobus;class=java.util.Set";

	/**
	 * TODO
	 */
	public	static final	String		MIME_TYPE_LIST						=
		"application/x-java-infobus;class=java.util.List";

	/**
	 * TODO
	 */
	public	static final	String		MIME_TYPE_ANY_ACCESS				=
		"application/x-java-infobus;class=javax.infobus.DataItem";

	/**
	 * Monitor Priority
	 */
	public	static final	int			MONITOR_PRIORITY	= 6;

	/**
	 * Very High Priority
	 */
	public	static final	int			VERY_HIGH_PRIORITY	= 5;

	/**
	 * High Priority
	 */
	public	static final	int			HIGH_PRIORITY		= 4;

	/**
	 * Medium Priority
	 */
	public	static final	int			MEDIUM_PRIORITY		= 3;

	/**
	 * Low Priority
	 */
	public	static final	int			LOW_PRIORITY		= 2;

	/**
	 * Very Low Priority
	 */
	public	static final	int			VERY_LOW_PRIORITY	= 1;

	/**
	 * Default Controller priority
	 */
	protected static final	int			DEFAULT_CONTROLLER_PRIORITY	= 0;	// TODO

	/**
	 * InfoBus name.
	 */
	private		String					busID				= null;

	/**
	 * List of registered members.
	 */
	private		Vector					memberList			= null;

	/**
	 * List of registered producers.
	 */
	private		Vector					producerList		= null;

	/**
	 * List of registered consumers.
	 */
	private		Vector					consumerList		= null;

	/**
	 * Open count // TODO
	 */
	private		int						openCount			= 0;

	/**
	 * Prioritized list of data controllers
	 */
	private		PrioritizedDCList		controllerList		= null;

	/**
	 * Flag added controllers // TODO
	 */
	private		boolean					addedControllers	= false;

	/**
	 * Special default data controller.
	 */
	private		InfoBusDataController	defaultControl		= null;


	//-------------------------------------------------------------
	// Initialization ---------------------------------------------
	//-------------------------------------------------------------

	/**
	 * Create new InfoBus.
	 * @param busName Name of InfoBus
	 */
	private InfoBus(String busName) {
		busID			= busName;
		memberList		= new Vector();
		producerList	= new Vector();
		consumerList	= new Vector();
		controllerList	= new PrioritizedDCList();
		defaultControl	= new DefaultController(this);
		controllerList.addDataController(defaultControl,
							DEFAULT_CONTROLLER_PRIORITY);
		
	} // InfoBus()


	//-------------------------------------------------------------
	// Public Accessor Methods ------------------------------------
	//-------------------------------------------------------------

	/**
	 * Check policy
	 */
	private static void checkPolicy() {
		// TODO
	} // checkPolicy()

	/**
	 * Check if InfoBus is stale.
	 */
	private void checkStale() {
		if (infoBusList.contains(this) == false) {
			throw new StaleInfoBusException("stale infobus");
		} // if
	} // checkStale()

	/**
	 * Get InfoBus name.
	 * @return InfoBus name
	 */
	public String getName() {
		return busID;
	} // getName()

	/**
	 * Get InfoBus instance based on applet component.
	 * @param component Component in an applet
	 * @return InfoBus instance
	 */
	public static InfoBus get(Component component) {
		return get(ibPolicy.generateDefaultName(component));
	} // get()

	/**
	 * Get InfoBus instance.
	 * @param busName Name of new InfoBus
	 * @return InfoBus instance
	 */
	public static InfoBus get(String busName) {

		// Variables
		InfoBus		current;
		Enumeration	enum;

		// Synchronize
		synchronized (syncLock) {

			// Check Policy
			ibPolicy.canGet(busName);

			// Check for Valid Bus Name
			if (busName.startsWith("-") == true ||
				busName.indexOf("*") != -1) {
				throw new IllegalArgumentException();
			} // if

			// Search for InfoBus representing busName
			enum = infoBusList.elements();
			while (enum.hasMoreElements() == true) {

				// Get InfoBus Object
				current = (InfoBus) enum.nextElement();

				// Check for BusName
				if (current.getName().equals(busName) == true) {
					return current;
				} // if

			} // while

			// Did not find InfoBus.  Create
			current = new InfoBus(busName);

			// Add to List
			infoBusList.addElement(current);
			
			// Return
			return current;

		} // synchronized

	} // get()

	/**
	 * Increment open count.
	 */
	private void incrementOpenCount() {
		openCount += 1;
	} // incrementOpenCount()

	/**
	 * Decrement open count.
	 */
	private void decrementOpenCount() {
		openCount -= 1;
	} // decrementOpenCount()

	/**
	 * Release InfoBus holder object
	 * TODO
	 */
	public void release() {
		// TODO

		synchronized (objectLock) {

			// Check for Stale
			checkStale();

		} // synchronized

	} // release()

	/**
	 * Join InfoBus.
	 * @param member InfoBus member to join InfoBus
	 * @throws PropertyVetoException Veto exception
	 */
	public void join(InfoBusMember member)
			throws PropertyVetoException {

		synchronized (objectLock) {

			// Check Policy
			ibPolicy.canJoin(this, member);

			// Check for Stale
			checkStale();

			// Register Infobus with member
			member.setInfoBus(this);

		} // synchronized

	} // join()

	/**
	 * Register on InfoBus.
	 * @param member InfoBus member
	 */
	public void register(InfoBusMember member) {

		synchronized (objectLock) {

			// Check Policy
			ibPolicy.canRegister(this, member);

			// Check for Stale
			checkStale();

			// Add Member to list
			memberList.addElement(member);

			// Register as Property Change Listener
			member.addInfoBusPropertyListener(this);

		} // synchronized

	} // register()

	/**
	 * Property change.
	 * @param event Property change event
	 */
	public void propertyChange(PropertyChangeEvent event) {

		// TODO
		//System.out.println("propertyChange...");

		// Check Policy
		ibPolicy.canPropertyChange(this, event);

	} // propertyChange()

	/**
	 * TODO
	 */
	private void freeUnused() {
		// TODO
	} // freeUnused()

	/**
	 * Leave InfoBus.
	 * @param member InfoBus member to leave InfoBus
	 * @throws PropertyVetoException Veto exception
	 */
	public void leave(InfoBusMember member)
			throws PropertyVetoException {

		synchronized (objectLock) {

			// Unregister InfoBus
			member.setInfoBus(null);

			// Remove Member from list
			memberList.removeElement(member);

			// Unregister as Listener
			member.removeInfoBusPropertyListener(this);

		} // synchronized

	} // leave()

	/**
	 * Add data controller.
	 * @param controller Data controller to add
	 * @param priority Priority of data controller
	 * @throws InfoBusMembershipException Eembership exception
	 */
	public void addDataController(InfoBusDataController controller, int priority)
			throws InfoBusMembershipException {

		synchronized (objectLock) {

			// Check Policy
			ibPolicy.canAddDataController(this, controller, priority);

			// Check for Stale
			checkStale();

			// Check Priority
			if (priority > MONITOR_PRIORITY) {
				priority = VERY_HIGH_PRIORITY;
			} else if (priority < VERY_LOW_PRIORITY) {
				priority = VERY_LOW_PRIORITY;
			} // if

			// Add Data Controller
			controllerList.addDataController(controller, priority);
			
			// Set Lists
			controller.setConsumerList(consumerList);
			controller.setProducerList(producerList);

		} // synchronized

	} // addDataController()

	/**
	 * Remove data controller.
	 * @param controller Data controller to add
	 */
	public void removeDataController(InfoBusDataController controller) {

		synchronized (objectLock) {

			// Remove Data Controller
			controllerList.removeDataController(controller);

		} // synchronized

	} // removeDataController()

	/**
	 * Add data producer to InfoBus
	 * @param producer InfoBus data producer to add
	 */
	public void addDataProducer(InfoBusDataProducer producer) {

		// Variables
		Enumeration				enum;
		InfoBusDataController	controller;

		synchronized (objectLock) {

			// Check Policy
			ibPolicy.canAddDataProducer(this, producer);

			// Check for Stale
			checkStale();

			// Add Producer to List
			if (producerList.contains(producer) == false) {
				producerList.addElement(producer);
				
				// Add to All Controllers
				enum = controllerList.elements();
				while (enum.hasMoreElements() == true) {
					controller = (InfoBusDataController) enum.nextElement();
					controller.addDataProducer(producer);
				} // while
				
			} // if

		} // synchronized

	} // addDataProducer()

	/**
	 * Remove data producer to InfoBus
	 * @param producer InfoBus data producer to remove
	 */
	public void removeDataProducer(InfoBusDataProducer producer) {
		
		// Variables
		Enumeration				enum;
		InfoBusDataController	controller;

		synchronized (objectLock) {

			// Remove Producer from List
			if (producerList.contains(producer) == true) {
				producerList.remove(producer);
				
				// Remove From All Controllers
				enum = controllerList.elements();
				while (enum.hasMoreElements() == true) {
					controller = (InfoBusDataController) enum.nextElement();
					controller.removeDataProducer(producer);
				} // while
				
			} // if

		} // synchronized

	} // removeDataProducer()

	/**
	 * Add data consumer to InfoBus
	 * @param consumer InfoBus data consumer to add
	 */
	public void addDataConsumer(InfoBusDataConsumer consumer) {

		// Variables
		Enumeration				enum;
		InfoBusDataController	controller;

		synchronized (objectLock) {

			// Check Policy
			ibPolicy.canAddDataConsumer(this, consumer);

			// Check for Stale
			checkStale();

			// Add Consumer to List
			if (consumerList.contains(consumer) == false) {
				consumerList.addElement(consumer);
				
				// Add to All Controllers
				enum = controllerList.elements();
				while (enum.hasMoreElements() == true) {
					controller = (InfoBusDataController) enum.nextElement();
					controller.addDataConsumer(consumer);
				} // while
				
			} // if

		} // synchronized

	} // addDataConsumer()

	/**
	 * Remove data consumer to InfoBus
	 * @param consumer InfoBus data consumer to remove
	 */
	public void removeDataConsumer(InfoBusDataConsumer consumer) {
	
		// Variables
		Enumeration				enum;
		InfoBusDataController	controller;

		synchronized (objectLock) {

			// Remove Consumer from List
			if (consumerList.contains(consumer) == true) {
				consumerList.remove(consumer);
				
				// Remove From All Controllers
				enum = controllerList.elements();
				while (enum.hasMoreElements() == true) {
					controller = (InfoBusDataController) enum.nextElement();
					controller.removeDataConsumer(consumer);
				} // while
				
			} // if

		} // synchronized

	} // removeDataConsumer()

	/**
	 * Fire data item available.
	 * @param dataItemName Data item name
	 * @param flavor Data flavors
	 * @param producer Infobus data producer
	 */
	public void fireItemAvailable(String dataItemName, DataFlavor[] flavor, 
								InfoBusDataProducer producer) {

		// Variables
		Enumeration					enum;
		InfoBusDataController		controller;

		// Check Policy
		ibPolicy.canFireItemAvailable(this, dataItemName, producer);

		// Notify All Controllers
		enum = controllerList.elements();
		while (enum.hasMoreElements() == true) {
			controller = (InfoBusDataController) enum.nextElement();
			if (controller.fireItemAvailable(dataItemName, flavor, producer) == true) {
				return;
			} // if
		} // while
		
	} // fireItemAvailable()

	/**
	 * Fire data item revoked.
	 * @param dataItemName Data item name
	 * @param producer Infobus data producer
	 */
	public void fireItemRevoked(String				dataItemName,
								InfoBusDataProducer	producer) {

		// Variables
		Enumeration				enum;
		InfoBusDataController	controller;

		// Check Policy
		ibPolicy.canFireItemRevoked(this, dataItemName, producer);
		
		// Notify All Controllers
		enum = controllerList.elements();
		while (enum.hasMoreElements() == true) {
			controller = (InfoBusDataController) enum.nextElement();
			if (controller.fireItemRevoked(dataItemName, producer) == true) {
				return;
			} // if
		} // while

	} // fireItemRevoked()

	/**
	 * Find data item.
	 * @param dataItemName Data item name
	 * @param flavor Data flavors
	 * @param consumer Infobus data consumer
	 * @return TODO
	 */
	public Object findDataItem(String dataItemName, DataFlavor[] flavor, 
							InfoBusDataConsumer consumer) {

		// Variables
		Enumeration					enum;
		Object						dataItem;
		InfoBusDataController		controller;
		Vector						resultSet;
		boolean						result;

		// Check Policy
		ibPolicy.canRequestItem(this, dataItemName, consumer);

		// Notify All Controllers
		enum = controllerList.elements();
		resultSet = new Vector();
		while (enum.hasMoreElements() == true) {
			controller = (InfoBusDataController) enum.nextElement();
			result = controller.findDataItem(dataItemName, flavor, consumer, resultSet);
			if (resultSet.size() > 0) {
				dataItem = resultSet.elementAt(0);
				return dataItem;
			} else if (result == true) {
				return null;
			} // if
		} // while
		
		return null;

	} // findDataItem()

	/**
	 * Find multiple data items.
	 * @param dataItemName Data item name
	 * @param flavor Data flavors
	 * @param consumer Infobus data consumer
	 * @return TODO
	 */
	public Object[] findMultipleDataItems(String				dataItemName,
										  DataFlavor[]			flavor,
										  InfoBusDataConsumer	consumer) {

		// Variables
		Enumeration					enum;
		Object						dataItem;
		Vector						dataItemList;
		Vector						resultSet;
		boolean						result;
		InfoBusDataController		controller;
		int							index;

		// Check Policy
		ibPolicy.canRequestItem(this, dataItemName, consumer);

		// Create Data Item List
		dataItemList = new Vector();
		
		// Notify All Controllers
		enum = controllerList.elements();
		resultSet = new Vector();
		while (enum.hasMoreElements() == true) {
			controller = (InfoBusDataController) enum.nextElement();
			result = controller.findMultipleDataItems(dataItemName,
									flavor, consumer, resultSet);
			if (resultSet.size() > 0) {
				for (index = 0; index < resultSet.size(); index++) {
					dataItem = resultSet.elementAt(0);
					if (dataItemList.contains(dataItem) == false) {
						dataItemList.addElement(dataItem);
					} //  if
				} // for
			} // if
			
			if (result == true) {
				if (dataItemList.size() == 0) {
					return null;
				} else {
					return dataItemList.toArray();
				} // if
			} // if
		} // while

		if (dataItemList.size() == 0) {
			return null;
		} else {
			return dataItemList.toArray();
		} // if

	} // findMultipleDataItems()

	/**
	 * Needed?
	 * @param list1 TODO
	 * @param list2 TODO
	 */
	private void catNoDups(Vector list1, Vector list2) {
		// TODO
	} // catNoDups()

	/**
	 * Find data item and notify producer.
	 * @param dataItemName Data item name
	 * @param flavor Data flavors
	 * @param consumer Infobus data consumer
	 * @param producer Infobus data producer
	 * @return TODO
	 */
	public Object findDataItem(String dataItemName, DataFlavor[] flavor, 
			InfoBusDataConsumer consumer, InfoBusDataProducer producer) {

		// Variables
		InfoBusItemRequestedEvent	event;
		Enumeration					enum;
		Object						dataItem;

		// Check Policy
		ibPolicy.canRequestItem(this, dataItemName, consumer);

		// Create Event
		event = new InfoBusItemRequestedEvent(dataItemName, flavor, consumer);

		// Notify Producer
		producer.dataItemRequested(event);

		// Check for Data Item
		return event.getDataItem();

	} // findDataItem()

	/**
	 * Find data item and notify producers.
	 * @param dataItemName Data item name
	 * @param flavor Data flavors
	 * @param consumer Infobus data consumer
	 * @param producers Infobus data producer list
	 * @return TODO
	 */
	public Object findDataItem(String dataItemName, DataFlavor[] flavor, 
						InfoBusDataConsumer consumer, Vector producers) {

		// Variables
		InfoBusItemRequestedEvent	event;
		InfoBusDataProducer			producer;
		Enumeration					enum;
		Object						dataItem;

		// Check Policy
		ibPolicy.canRequestItem(this, dataItemName, consumer);

		// Create Event
		event = new InfoBusItemRequestedEvent(dataItemName, flavor, consumer);

		// Notify All Producers
		enum = producers.elements();
		while (enum.hasMoreElements() == true) {

			// Get Producer
			producer = (InfoBusDataProducer) enum.nextElement();

			// Notify Producer
			producer.dataItemRequested(event);

			// Check for Data Item
			dataItem = event.getDataItem();
			if (dataItem != null) {
				return dataItem;
			} // if

		} // while

		// Unable to Locate DataItem
		return null;

	} // findDataItem()

	/**
	 * Fire data item available to specified consumer.
	 * @param dataItemName Data item name
	 * @param flavor Data flavors
	 * @param producer Infobus data producer
	 * @param consumer Infobus data consumer
	 */
	public void fireItemAvailable(String dataItemName, DataFlavor[] flavor, 
				InfoBusDataProducer producer, InfoBusDataConsumer consumer) {

		// Variables
		InfoBusItemAvailableEvent	event;
		Enumeration					enum;

		// Check Policy
		ibPolicy.canFireItemAvailable(this, dataItemName, producer);

		// Create Event
		event = new InfoBusItemAvailableEvent(dataItemName, flavor, producer);

		// Notify Consumer
		consumer.dataItemAvailable(event);

	} // fireItemAvailable()

	/**
	 * Fire data item available to list of specified consumers.
	 * @param dataItemName Data item name
	 * @param flavor Data flavors
	 * @param producer Infobus data producer
	 * @param consumers Infobus data consumer list
	 */
	public void fireItemAvailable(String dataItemName, DataFlavor[] flavor, 
				InfoBusDataProducer producer, Vector consumers) {

		// Variables
		InfoBusItemAvailableEvent	event;
		InfoBusDataConsumer			consumer;
		Enumeration					enum;

		// Check Policy
		ibPolicy.canFireItemAvailable(this, dataItemName, producer);

		// Create Event
		event = new InfoBusItemAvailableEvent(dataItemName, flavor, producer);

		// Notify All Consumers
		enum = consumers.elements();
		while (enum.hasMoreElements() == true) {

			// Get Consumer
			consumer = (InfoBusDataConsumer) enum.nextElement();

			// Notify Consumer
			consumer.dataItemAvailable(event);

		} // while

	} // fireItemAvailable()

	/**
	 * Fire data item revoked to specified consumer.
	 * @param dataItemName Data item name
	 * @param producer Infobus data producer
	 * @param consumer Infobus data consumer
	 */
	public void fireItemRevoked(String dataItemName, InfoBusDataProducer producer, 
							InfoBusDataConsumer consumer) {

		// Variables
		InfoBusItemRevokedEvent	event;
		Enumeration				enum;

		// Check Policy
		ibPolicy.canFireItemRevoked(this, dataItemName, producer);

		// Create Event
		event = new InfoBusItemRevokedEvent(dataItemName, producer);

		// Notify Consumer
		consumer.dataItemRevoked(event);

	} // fireItemRevoked()

	/**
	 * Fire data item revoked to specified list of consumers.
	 * @param dataItemName Data item name
	 * @param producer Infobus data producer
	 * @param consumers Infobus data consumer list
	 */
	public void fireItemRevoked(String dataItemName, InfoBusDataProducer producer, 
							Vector consumers) {

		// Variables
		InfoBusItemRevokedEvent	event;
		InfoBusDataConsumer		consumer;
		Enumeration				enum;

		// Check Policy
		ibPolicy.canFireItemRevoked(this, dataItemName, producer);

		// Create Event
		event = new InfoBusItemRevokedEvent(dataItemName, producer);

		// Notify All Consumers
		enum = consumers.elements();
		while (enum.hasMoreElements() == true) {

			// Get Consumer
			consumer = (InfoBusDataConsumer) enum.nextElement();

			// Notify Consumer
			consumer.dataItemRevoked(event);

		} // while

	} // fireItemRevoked()


} // InfoBus

/**
 * Default Controller.
 * @author Andrew Selkirk
 */
final class DefaultController implements InfoBusDataController {

	//-------------------------------------------------------------
	// Variables --------------------------------------------------
	//-------------------------------------------------------------

	/**
	 * InfoBus.
	 */
	private	InfoBus		parent		= null;

	/**
	 * List of data consumers.
	 */
	private	Vector		consumerList	= null;

	/**
	 * List of data producers.
	 */
	private	Vector		producerList	= null;
	

	//-------------------------------------------------------------
	// Initialization ---------------------------------------------
	//-------------------------------------------------------------

	/**
	 * Create default controller.
	 * @param infobus InfoBus reference
	 */
	DefaultController(InfoBus infobus) {
		parent = infobus;
		consumerList = new Vector();
		producerList = new Vector();
	} // DefaultController()
	

	//-------------------------------------------------------------
	// Public Accessor Methods ------------------------------------
	//-------------------------------------------------------------

	/**
	 * Fire data item available.
	 * @param dataItemName Data item name
	 * @param flavors Data flavors
	 * @param producer Infobus data producer
	 * @return TODO
	 */
	public boolean fireItemAvailable(String				dataItemName,
									 DataFlavor[]			flavors,
									 InfoBusDataProducer	producer) {

		// Check For Consumers to Notify			
		if (consumerList.size() > 0) {
			parent.fireItemAvailable(dataItemName, flavors, producer,
										consumerList);
		} // if
		
		return true;
		
	} // fireItemAvailable()
	
	/**
	 * Fire data item revoked.
	 * @param dataItemName Data item name
	 * @param producer Infobus data producer
	 * @return TODO
	 */
	public boolean fireItemRevoked(String			dataItemName,
								   InfoBusDataProducer	producer) {
	
		// Check For Consumers to Notify			
		if (consumerList.size() > 0) {
			parent.fireItemRevoked(dataItemName, producer, consumerList);
		} // if
		
		return true;
		
	} // fireItemRevoked()
	
	/**
	 * Find data item.
	 * @param dataItemName Data item name
	 * @param flavors Data flavors
	 * @param consumer Infobus data consumer
	 * @param foundItem Item list
	 * @return TODO
	 */
	public boolean findDataItem(String				dataItemName,
								DataFlavor[]		flavors,
								InfoBusDataConsumer consumer,
								Vector				foundItem) {
			
		// Variables
		Object result;
		
		// Check For Producers to Notify
		if (producerList.size() > 0) {
			result = parent.findDataItem(dataItemName, flavors,
											consumer, producerList);
			if (result != null) {
				foundItem.addElement(result);
			} // if: result
		} // if: m_producerList
		
		return true;
		
	} // findDataItem()
	
	/**
	 * Find multiple data items.
	 * @param dataItemName Data item name
	 * @param flavors Data flavors
	 * @param consumer Infobus data consumer
	 * @param foundItems Item list
	 * @return TODO
	 */
	public boolean findMultipleDataItems(String					dataItemName, 
										 DataFlavor[]			flavors,
										 InfoBusDataConsumer	consumer,
										 Vector					foundItems) {
		
		// Variables
		Object 				result;
		Enumeration			enum;
		InfoBusDataProducer	producer;
		
		// Check For Producers to Notify
		enum = producerList.elements();
		while (enum.hasMoreElements() == true) {
			producer = (InfoBusDataProducer) enum.nextElement();
			result = parent.findDataItem(dataItemName, flavors, consumer, producer);
			if (result != null) {
				foundItems.addElement(result);
			} // if: result
		} // while
		
		return true;

	} // findMultipleDataItems()

	/**
	 * Set list of consumers.
	 * @param consumers Consumer list
	 */
	public void setConsumerList(Vector consumers) {
		consumerList = consumers;
	} // setConsumerList()
	
	/**
	 * Set list of producers.
	 * @param producers Producer list
	 */
	public void setProducerList(Vector producers) {
		producerList = producers;
	} // setProducerList()

	/**
	 * Add data consumer.
	 * @param consumer Data consumer
	 */
	public void addDataConsumer(InfoBusDataConsumer consumer) {
		consumerList.addElement(consumer);
	} // addDataConsumer()

	/**
	 * Add data producer.
	 * @param producer Data producer
	 */
	public void addDataProducer(InfoBusDataProducer producer) {
		producerList.addElement(producer);
	} // addDataProducer()

	/**
	 * Remove data consumer.
	 * @param consumer Data consumer
	 */
	public void removeDataConsumer(InfoBusDataConsumer consumer) {
		consumerList.removeElement(consumer);
	} // removeDataConsumer()

	/**
	 * Remove data producer.
	 * @param producer Data producer
	 */
	public void removeDataProducer(InfoBusDataProducer producer) {
		producerList.removeElement(producer);
	} // removeDataProducer()
	
	
} // DefaultController
