/********************************************************************
 * Copyright (c) Open Java Extensions, Andrew Selkirk  LGPL License *
 ********************************************************************/

package javax.infobus;

// Imports
import java.util.Vector;
import java.util.Enumeration;

/**
 * Data Item Change Manager support.
 */
public class DataItemChangeManagerSupport {

	//-------------------------------------------------------------
	// Variables --------------------------------------------------
	//-------------------------------------------------------------

	/**
	 * Source object.
	 */
	protected	Object	m_source			= null;

	/**
	 * List of change listeners.
	 */
	protected	Vector	m_changeListeners	= new Vector();


	//-------------------------------------------------------------
	// Initialization ---------------------------------------------
	//-------------------------------------------------------------

	/**
	 * Create date item change manager support.
	 * @param source Source object
	 */
	public DataItemChangeManagerSupport(Object source) {
		m_source = source;
	} // DataItemChangeManagerSupport()


	//-------------------------------------------------------------
	// Public Accessor Methods ------------------------------------
	//-------------------------------------------------------------

	/**
	 * Add data item change listener.
	 * @param listener Data item change listener
	 */
	public synchronized void addDataItemChangeListener(DataItemChangeListener listener) {
		if (m_changeListeners.contains(listener) == false) {
			m_changeListeners.add(listener);
		}
	} // addDataItemChangeListener()

	/**
	 * Remove data item change listener.
	 * @param listener Data item change listener
	 */
	public synchronized void removeDataItemChangeListener(DataItemChangeListener listener) {
		if (m_changeListeners.contains(listener) == true) {
			m_changeListeners.remove(listener);
		}
	} // removeDataItemChangeListener()

	/**
	 * Remove all change listeners.
	 */
	public synchronized void removeAllListeners() {
		m_changeListeners.clear();
	} // removeAllListeners()

	/**
	 * Get enumerated list of change listeners
	 * @returns Enumeration of change listeners
	 */
	protected synchronized Enumeration enumerateListeners() {
		return m_changeListeners.elements();
	} // enumerateListeners()

	/**
	 * Fire item value changed event.
	 * @param changedItem Changed item
	 * @param propertyMap Property map
	 */
	public void fireItemValueChanged(Object changedItem, InfoBusPropertyMap propertyMap) {

		// Variables
		Enumeration					list;
		DataItemChangeListener		listener;
		DataItemValueChangedEvent	event;

		// Get Listeners
		list = enumerateListeners();

		// Create Event
		event = new DataItemValueChangedEvent(m_source, changedItem, propertyMap);

		// Loop through all Listeners
		while (list.hasMoreElements() == true) {

			// Get Listener
			listener = (DataItemChangeListener) list.nextElement();

			// Notify Listener
			listener.dataItemValueChanged(event);

		} // while

	} // fireItemValueChanged()

	/**
	 * Fire item added event.
	 * @param changedItem Changed item
	 * @param changedCollection Changed collection
	 * @param propertyMap Property map
	 */
	public void fireItemAdded(Object				changedItem, 
						  Object				changedCollection, 
						  InfoBusPropertyMap	propertyMap) {

		// Variables
		Enumeration					list;
		DataItemChangeListener		listener;
		DataItemAddedEvent			event;

		// Get Listeners
		list = enumerateListeners();

		// Create Event
		event = new DataItemAddedEvent(m_source, changedItem,
					changedCollection, propertyMap);

		// Loop through all Listeners
		while (list.hasMoreElements() == true) {

			// Get Listener
			listener = (DataItemChangeListener) list.nextElement();

			// Notify Listener
			listener.dataItemAdded(event);

		} // while

	} // fireItemAdded()

	/**
	 * Fire item deleted event.
	 * @param changedItem Changed item
	 * @param changedCollection Changed collection
	 * @param propertyMap Property map
	 */
	public void fireItemDeleted(Object				changedItem, 
							Object				changedCollection, 
							InfoBusPropertyMap	propertyMap) {

		// Variables
		Enumeration					list;
		DataItemChangeListener		listener;
		DataItemDeletedEvent		event;

		// Get Listeners
		list = enumerateListeners();

		// Create Event
		event = new DataItemDeletedEvent(m_source, changedItem,
					changedCollection, propertyMap);

		// Loop through all Listeners
		while (list.hasMoreElements() == true) {

			// Get Listener
			listener = (DataItemChangeListener) list.nextElement();

			// Notify Listener
			listener.dataItemDeleted(event);

		} // while

	} // fireItemDeleted()

	/**
	 * Fire item revoked event.
	 * @param changedItem Changed item
	 * @param propertyMap Property map
	 */
	public void fireItemRevoked(Object				changedItem, 
							InfoBusPropertyMap	propertyMap) {

		// Variables
		Enumeration					list;
		DataItemChangeListener		listener;
		DataItemRevokedEvent		event;

		// Get Listeners
		list = enumerateListeners();

		// Create Event
		event = new DataItemRevokedEvent(m_source, changedItem,
						propertyMap);

		// Loop through all Listeners
		while (list.hasMoreElements() == true) {

			// Get Listener
			listener = (DataItemChangeListener) list.nextElement();

			// Notify Listener
			listener.dataItemRevoked(event);

		} // while

	} // fireItemRevoked()

	/**
	 * Fire rowset cursor moved event.
	 * @param changedItem Changed item
	 * @param propertyMap Property map
	 */
	public void fireRowsetCursorMoved(Object				changedItem, 
								  InfoBusPropertyMap	propertyMap) {

		// Variables
		Enumeration					list;
		DataItemChangeListener		listener;
		RowsetCursorMovedEvent		event;

		// Get Listeners
		list = enumerateListeners();

		// Create Event
		event = new RowsetCursorMovedEvent(m_source, changedItem,
						propertyMap);

		// Loop through all Listeners
		while (list.hasMoreElements() == true) {

			// Get Listener
			listener = (DataItemChangeListener) list.nextElement();

			// Notify Listener
			listener.rowsetCursorMoved(event);

		} // while

	} // fireRowsetCursorMoved()

	/**
	 * Fire item shape changed event.
	 * @param changedItem Changed item
	 * @param propertyMap Property map
	 */
	public void fireItemShapeChanged(Object				changedItem, 
								 InfoBusPropertyMap	propertyMap) {

		// Variables
		Enumeration					list;
		DataItemChangeListener		listener;
		DataItemShapeChangedEvent	event;
		DataItemShapeChangeListener	shapeListener;

		// Get Listeners
		list = enumerateListeners();

		// Create Event
		event = new DataItemShapeChangedEvent(m_source, changedItem,
						propertyMap);

		// Loop through all Listeners
		while (list.hasMoreElements() == true) {

			// Get Listener
			listener = (DataItemChangeListener) list.nextElement();

			// Notify Listener
			if (listener instanceof DataItemShapeChangeListener) {
				shapeListener = (DataItemShapeChangeListener) listener;
				shapeListener.dataItemShapeChanged(event);
			}

		} // while

	} // fireItemItemShapeChanged()


} // DataItemChangeManagerSupport
