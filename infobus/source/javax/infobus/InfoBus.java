/********************************************************************
 * Copyright (c) Open Java Extensions, Andrew Selkirk  LGPL License *
 ********************************************************************/

package javax.infobus;

// Imports
import java.applet.Applet;
import java.awt.Component;
import java.awt.datatransfer.DataFlavor;
import java.beans.*;
import java.util.*;

/**
 * InfoBus.
 */
public final	class		InfoBus
		implements	PropertyChangeListener {

	//-------------------------------------------------------------
	// Variables --------------------------------------------------
	//-------------------------------------------------------------

	/**
	 * Static list of InfoBus instances
	 */
	private static	Vector				sm_infoBusList	= new Vector();

	/**
	 * Security policy helper
	 */
	private	static	InfoBusPolicyHelper	sm_IBPolicy		= new DefaultPolicy();

	/**
	 * Static synchronization locking object
	 */
	private	static	Object				sm_syncLock		= new Object();

	/**
	 * Synchronization locking object
	 */
	private			Object				m_syncLock		= new Object();

	/**
	 * Debugging output flag
	 */
	private	static final	boolean		DEBUG_OUTPUT	= false;

	public	static final	String		MIME_TYPE_IMMEDIATE_ACCESS			= 
		"application/x-java-infobus;class=javax.infobus.ImmediateAccess";
	public	static final	String		MIME_TYPE_ARRAY_ACCESS				= 
		"application/x-java-infobus;class=javax.infobus.ArrayAccess";
	public	static final	String		MIME_TYPE_RESHAPEABLEARRAY_ACCESS	= 
		"application/x-java-infobus;class=javax.infobus.ReshapeableArrayAccess";
	public	static final	String		MIME_TYPE_ROWSET_ACCESS				= 
		"application/x-java-infobus;class=javax.infobus.RowsetAccess";
	public	static final	String		MIME_TYPE_SCROLLABLEROWSET_ACCESS	= 
		"application/x-java-infobus;class=javax.infobus.ScrollableRowsetAccess";
	public	static final	String		MIME_TYPE_DB_ACCESS					= 
		"application/x-java-infobus;class=javax.infobus.DbAccess";
	public	static final	String		MIME_TYPE_COLLECTION				= 
		"application/x-java-infobus;class=java.util.Collection";
	public	static final	String		MIME_TYPE_MAP						= 
		"application/x-java-infobus;class=java.util.Map";
	public	static final	String		MIME_TYPE_SET						= 
		"application/x-java-infobus;class=java.util.Set";
	public	static final	String		MIME_TYPE_LIST						= 
		"application/x-java-infobus;class=java.util.List";
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
	private		String					m_busID				= null;

	/**
	 * List of registered members.
	 */
	private		Vector					m_memberList		= null;

	/**
	 * List of registered producers.
	 */
	private		Vector					m_producerList		= null;

	/**
	 * List of registered consumers.
	 */
	private		Vector					m_consumerList		= null;

	/**
	 * Open count // TODO
	 */
	private		int						m_openCount			= 0;

	/**
	 * Prioritized list of data controllers
	 */
	private		PrioritizedDCList		m_controllerList	= null;

	/**
	 * Flag added controllers // TODO
	 */
	private		boolean					m_addedControllers	= false;

	/**
	 * Special default data controller.
	 */
	private		InfoBusDataController	m_defaultControl	= null;


	//-------------------------------------------------------------
	// Initialization ---------------------------------------------
	//-------------------------------------------------------------

	/**
	 * Create new InfoBus.
	 * @param busName Name of InfoBus
	 */
	private InfoBus(String busName) {
		m_busID			 = busName;
		m_memberList	 = new Vector();
		m_producerList	 = new Vector();
		m_consumerList	 = new Vector();
		m_controllerList = new PrioritizedDCList();
		m_defaultControl = new DefaultController(this);
		m_controllerList.addDataController(m_defaultControl,
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
		if (sm_infoBusList.contains(this) == false) {
			throw new StaleInfoBusException("stale infobus");
		}
	} // checkStale()

	/**
	 * Get InfoBus name.
	 * @returns InfoBus name
	 */
	public String getName() {
		return m_busID;
	} // getName()

	/**
	 * Get InfoBus instance based on applet component.
	 * @param component Component in an applet
	 * @returns InfoBus instance
	 */
	public static InfoBus get(Component component) {
		return get(sm_IBPolicy.generateDefaultName(component));
	} // get()

	/**
	 * Get InfoBus instance.
	 * @param busName Name of new InfoBus
	 * @returns InfoBus instance
	 */
	public static InfoBus get(String busName) {

		// Variables
		InfoBus		current;
		Enumeration	enum;

		// Synchronize
		synchronized (sm_syncLock) {

			// Check Policy
			sm_IBPolicy.canGet(busName);

			// Check for Valid Bus Name
			if (busName.startsWith("-") == true ||
				busName.indexOf("*") != -1) {
				throw new IllegalArgumentException();
			}

			// Search for InfoBus representing busName
			enum = sm_infoBusList.elements();
			while (enum.hasMoreElements() == true) {

				// Get InfoBus Object
				current = (InfoBus) enum.nextElement();

				// Check for BusName
				if (current.getName().equals(busName) == true) {
					return current;
				}

			} // while

			// Did not find InfoBus.  Create
			current = new InfoBus(busName);

			// Add to List
			sm_infoBusList.addElement(current);
			
			// Return
			return current;

		} // synchronized

	} // get()

	/**
	 * Increment open count.
	 */
	private void incrementOpenCount() {
		m_openCount += 1;
	} // incrementOpenCount()

	/**
	 * Decrement open count.
	 */
	private void decrementOpenCount() {
		m_openCount -= 1;
	} // decrementOpenCount()

	/**
	 * Release InfoBus holder object
	 * TODO
	 */
	public void release() {
		// TODO

		synchronized (m_syncLock) {

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

		synchronized (m_syncLock) {

			// Check Policy
			sm_IBPolicy.canJoin(this, member);

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

		synchronized (m_syncLock) {

			// Check Policy
			sm_IBPolicy.canRegister(this, member);

			// Check for Stale
			checkStale();

			// Add Member to list
			m_memberList.addElement(member);

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
		sm_IBPolicy.canPropertyChange(this, event);

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

		synchronized (m_syncLock) {

			// Unregister InfoBus
			member.setInfoBus(null);

			// Remove Member from list
			m_memberList.removeElement(member);

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

		synchronized (m_syncLock) {

			// Check Policy
			sm_IBPolicy.canAddDataController(this, controller, priority);

			// Check for Stale
			checkStale();

			// Check Priority
			if (priority > MONITOR_PRIORITY) {
				priority = VERY_HIGH_PRIORITY;
			} else if (priority < VERY_LOW_PRIORITY) {
				priority = VERY_LOW_PRIORITY;
			}

			// Add Data Controller
			m_controllerList.addDataController(controller, priority);
			
			// Set Lists
			controller.setConsumerList(m_consumerList);
			controller.setProducerList(m_producerList);

		} // synchronized

	} // addDataController()

	/**
	 * Remove data controller.
	 * @param controller Data controller to add
	 */
	public void removeDataController(InfoBusDataController controller) {

		synchronized (m_syncLock) {

			// Remove Data Controller
			m_controllerList.removeDataController(controller);

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

		synchronized (m_syncLock) {

			// Check Policy
			sm_IBPolicy.canAddDataProducer(this, producer);

			// Check for Stale
			checkStale();

			// Add Producer to List
			if (m_producerList.contains(producer) == false) {
				m_producerList.addElement(producer);
				
				// Add to All Controllers
				enum = m_controllerList.elements();
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

		synchronized (m_syncLock) {

			// Remove Producer from List
			if (m_producerList.contains(producer) == true) {
				m_producerList.remove(producer);
				
				// Remove From All Controllers
				enum = m_controllerList.elements();
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

		synchronized (m_syncLock) {

			// Check Policy
			sm_IBPolicy.canAddDataConsumer(this, consumer);

			// Check for Stale
			checkStale();

			// Add Consumer to List
			if (m_consumerList.contains(consumer) == false) {
				m_consumerList.addElement(consumer);
				
				// Add to All Controllers
				enum = m_controllerList.elements();
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

		synchronized (m_syncLock) {

			// Remove Consumer from List
			if (m_consumerList.contains(consumer) == true) {
				m_consumerList.remove(consumer);
				
				// Remove From All Controllers
				enum = m_controllerList.elements();
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
		sm_IBPolicy.canFireItemAvailable(this, dataItemName, producer);

		// Notify All Controllers
		enum = m_controllerList.elements();
		while (enum.hasMoreElements() == true) {
			controller = (InfoBusDataController) enum.nextElement();
			if (controller.fireItemAvailable(dataItemName, flavor, producer) == true) {
				return;
			}
		} // while
		
	} // fireItemAvailable()

	/**
	 * Fire data item revoked.
	 * @param dataItemName Data item name
	 * @param producer Infobus data producer
	 */
	public void fireItemRevoked(String dataItemName, InfoBusDataProducer producer) {

		// Variables
		Enumeration				enum;
		InfoBusDataController	controller;

		// Check Policy
		sm_IBPolicy.canFireItemRevoked(this, dataItemName, producer);
		
		// Notify All Controllers
		enum = m_controllerList.elements();
		while (enum.hasMoreElements() == true) {
			controller = (InfoBusDataController) enum.nextElement();
			if (controller.fireItemRevoked(dataItemName, producer) == true) {
				return;
			}
		} // while

	} // fireItemRevoked()

	/**
	 * Find data item.
	 * @param dataItemName Data item name
	 * @param flavor Data flavors
	 * @param consumer Infobus data consumer
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
		sm_IBPolicy.canRequestItem(this, dataItemName, consumer);

		// Notify All Controllers
		enum = m_controllerList.elements();
		resultSet = new Vector();
		while (enum.hasMoreElements() == true) {
			controller = (InfoBusDataController) enum.nextElement();
			result = controller.findDataItem(dataItemName, flavor, consumer, resultSet);
			if (resultSet.size() > 0) {
				dataItem = resultSet.elementAt(0);
				return dataItem;
			} else if (result == true) {
				return null;
			}
		} // while
		
		return null;

	} // findDataItem()

	/**
	 * Find multiple data items.
	 * @param dataItemName Data item name
	 * @param flavor Data flavors
	 * @param consumer Infobus data consumer
	 */
	public Object[] findMultipleDataItems(String dataItemName, DataFlavor[] flavor, 
										InfoBusDataConsumer consumer) {

		// Variables
		Enumeration					enum;
		Object						dataItem;
		Vector						dataItemList;
		Vector						resultSet;
		boolean						result;
		InfoBusDataController		controller;
		int							index;

		// Check Policy
		sm_IBPolicy.canRequestItem(this, dataItemName, consumer);

		// Create Data Item List
		dataItemList = new Vector();
		
		// Notify All Controllers
		enum = m_controllerList.elements();
		resultSet = new Vector();
		while (enum.hasMoreElements() == true) {
			controller = (InfoBusDataController) enum.nextElement();
			result = controller.findMultipleDataItems(dataItemName, flavor, consumer, resultSet);
			if (resultSet.size() > 0) {
				for (index = 0; index < resultSet.size(); index++) {
					dataItem = resultSet.elementAt(0);
					if (dataItemList.contains(dataItem) == false) {
						dataItemList.addElement(dataItem);
					}
				}
			}
			
			if (result == true) {
				if (dataItemList.size() == 0) {
					return null;
				} else {
					return dataItemList.toArray();
				}
			}
		} // while
		
		if (dataItemList.size() == 0) {
			return null;
		} else {
			return dataItemList.toArray();
		}

	} // findMultipleDataItems()

	private void catNoDups(Vector list1, Vector list2) {
		// TODO
	} // catNoDups()

	/**
	 * Find data item and notify producer.
	 * @param dataItemName Data item name
	 * @param flavor Data flavors
	 * @param consumer Infobus data consumer
	 * @param producer Infobus data producer
	 */
	public Object findDataItem(String dataItemName, DataFlavor[] flavor, 
			InfoBusDataConsumer consumer, InfoBusDataProducer producer) {

		// Variables
		InfoBusItemRequestedEvent	event;
		Enumeration					enum;
		Object						dataItem;

		// Check Policy
		sm_IBPolicy.canRequestItem(this, dataItemName, consumer);

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
	 */
	public Object findDataItem(String dataItemName, DataFlavor[] flavor, 
						InfoBusDataConsumer consumer, Vector producers) {

		// Variables
		InfoBusItemRequestedEvent	event;
		InfoBusDataProducer			producer;
		Enumeration					enum;
		Object						dataItem;

		// Check Policy
		sm_IBPolicy.canRequestItem(this, dataItemName, consumer);

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
			}

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
		sm_IBPolicy.canFireItemAvailable(this, dataItemName, producer);

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
		sm_IBPolicy.canFireItemAvailable(this, dataItemName, producer);

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
		sm_IBPolicy.canFireItemRevoked(this, dataItemName, producer);

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
		sm_IBPolicy.canFireItemRevoked(this, dataItemName, producer);

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
 */
final class DefaultController implements InfoBusDataController {

	//-------------------------------------------------------------
	// Variables --------------------------------------------------
	//-------------------------------------------------------------

	/**
	 * InfoBus.
	 */
	private	InfoBus		m_parent		= null;

	/**
	 * List of data consumers.
	 */
	private	Vector		m_consumerList	= null;

	/**
	 * List of data producers.
	 */
	private	Vector		m_producerList	= null;
	

	//-------------------------------------------------------------
	// Initialization ---------------------------------------------
	//-------------------------------------------------------------

	/**
	 * Create default controller.
	 * @param infobus InfoBus reference
	 */
	DefaultController(InfoBus infobus) {
		m_parent = infobus;
		m_consumerList = new Vector();
		m_producerList = new Vector();
	} // DefaultController()
	

	//-------------------------------------------------------------
	// Public Accessor Methods ------------------------------------
	//-------------------------------------------------------------

	/**
	 * Fire data item available.
	 * @param dataItemName Data item name
	 * @param flavor Data flavors
	 * @param producer Infobus data producer
	 */
	public boolean fireItemAvailable(String					dataItemName, 
								 DataFlavor[]			flavors,
								 InfoBusDataProducer	producer) {

		// Check For Consumers to Notify			
		if (m_consumerList.size() > 0) {
			m_parent.fireItemAvailable(dataItemName, flavors, producer, 
										m_consumerList);
		}
		
		return true;
		
	} // fireItemAvailable()
	
	/**
	 * Fire data item revoked.
	 * @param dataItemName Data item name
	 * @param producer Infobus data producer
	 */
	public boolean fireItemRevoked(String				dataItemName, 
							   InfoBusDataProducer	producer) {
	
		// Check For Consumers to Notify			
		if (m_consumerList.size() > 0) {
			m_parent.fireItemRevoked(dataItemName, producer, m_consumerList);
		}
		
		return true;
		
	} // fireItemRevoked()
	
	/**
	 * Find data item.
	 * @param dataItemName Data item name
	 * @param flavor Data flavors
	 * @param consumer Infobus data consumer
	 * @param foundItem Item list
	 */
	public boolean findDataItem(String				dataItemName, 
							DataFlavor[]		flavors,
							InfoBusDataConsumer consumer, 
							Vector				foundItem) {
			
		// Variables
		Object result;
		
		// Check For Producers to Notify
		if (m_producerList.size() > 0) {
			result = m_parent.findDataItem(dataItemName, flavors, 
											consumer, m_producerList);
			if (result != null) {
				foundItem.addElement(result);
			} // if: result
		} // if: m_producerList
		
		return true;
		
	} // findDataItem()
	
	/**
	 * Find multiple data items.
	 * @param dataItemName Data item name
	 * @param flavor Data flavors
	 * @param consumer Infobus data consumer
	 * @param foundItem Item list
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
		enum = m_producerList.elements();
		while (enum.hasMoreElements() == true) {
			producer = (InfoBusDataProducer) enum.nextElement();
			result = m_parent.findDataItem(dataItemName, flavors, consumer, producer);
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
		m_consumerList = consumers;
	} // setConsumerList()
	
	/**
	 * Set list of producers.
	 * @param producers Producer list
	 */
	public void setProducerList(Vector producers) {
		m_producerList = producers;
	} // setProducerList()

	/**
	 * Add data consumer.
	 * @param consumer Data consumer
	 */
	public void addDataConsumer(InfoBusDataConsumer consumer) {
		m_consumerList.addElement(consumer);
	} // addDataConsumer()

	/**
	 * Add data producer.
	 * @param producer Data producer
	 */
	public void addDataProducer(InfoBusDataProducer producer) {
		m_producerList.addElement(producer);
	} // addDataProducer()

	/**
	 * Remove data consumer.
	 * @param consumer Data consumer
	 */
	public void removeDataConsumer(InfoBusDataConsumer consumer) {
		m_consumerList.removeElement(consumer);
	} // removeDataConsumer()

	/**
	 * Remove data producer.
	 * @param producer Data producer
	 */
	public void removeDataProducer(InfoBusDataProducer producer) {
		m_producerList.removeElement(producer);
	} // removeDataProducer()
	
	
} // DefaultController
