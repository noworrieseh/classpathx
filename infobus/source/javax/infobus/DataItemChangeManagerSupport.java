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
import java.util.Vector;
import java.util.Enumeration;

/**
 * Data Item Change Manager support.
 * @author Andrew Selkirk
 * @version $Revision: 1.3 $
 */
public class DataItemChangeManagerSupport {

	//-------------------------------------------------------------
	// Variables --------------------------------------------------
	//-------------------------------------------------------------

	/**
	 * Source object.
	 */
	protected	Object	source			= null;

	/**
	 * List of change listeners.
	 */
	protected	Vector	changeListeners	= new Vector();


	//-------------------------------------------------------------
	// Initialization ---------------------------------------------
	//-------------------------------------------------------------

	/**
	 * Create date item change manager support.
	 * @param source Source object
	 */
	public DataItemChangeManagerSupport(Object source) {
		this.source = source;
	} // DataItemChangeManagerSupport()


	//-------------------------------------------------------------
	// Public Accessor Methods ------------------------------------
	//-------------------------------------------------------------

	/**
	 * Add data item change listener.
	 * @param listener Data item change listener
	 */
	public synchronized void addDataItemChangeListener(
			DataItemChangeListener listener) {
		if (changeListeners.contains(listener) == false) {
			changeListeners.add(listener);
		} // if
	} // addDataItemChangeListener()

	/**
	 * Remove data item change listener.
	 * @param listener Data item change listener
	 */
	public synchronized void removeDataItemChangeListener(
			DataItemChangeListener listener) {
		if (changeListeners.contains(listener) == true) {
			changeListeners.remove(listener);
		} // if
	} // removeDataItemChangeListener()

	/**
	 * Remove all change listeners.
	 */
	public synchronized void removeAllListeners() {
		changeListeners.clear();
	} // removeAllListeners()

	/**
	 * Get enumerated list of change listeners
	 * @return Enumeration of change listeners
	 */
	protected synchronized Enumeration enumerateListeners() {
		return changeListeners.elements();
	} // enumerateListeners()

	/**
	 * Fire item value changed event.
	 * @param changedItem Changed item
	 * @param propertyMap Property map
	 */
	public void fireItemValueChanged(Object changedItem,
			InfoBusPropertyMap propertyMap) {

		// Variables
		Enumeration					list;
		DataItemChangeListener		listener;
		DataItemValueChangedEvent	event;

		// Get Listeners
		list = enumerateListeners();

		// Create Event
		event = new DataItemValueChangedEvent(source,
						changedItem, propertyMap);

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
		event = new DataItemAddedEvent(source, changedItem,
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
		event = new DataItemDeletedEvent(source, changedItem,
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
		event = new DataItemRevokedEvent(source, changedItem,
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
		event = new RowsetCursorMovedEvent(source, changedItem,
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
		event = new DataItemShapeChangedEvent(source, changedItem,
						propertyMap);

		// Loop through all Listeners
		while (list.hasMoreElements() == true) {

			// Get Listener
			listener = (DataItemChangeListener) list.nextElement();

			// Notify Listener
			if (listener instanceof DataItemShapeChangeListener) {
				shapeListener = (DataItemShapeChangeListener) listener;
				shapeListener.dataItemShapeChanged(event);
			} // if

		} // while

	} // fireItemItemShapeChanged()


} // DataItemChangeManagerSupport
