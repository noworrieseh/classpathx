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
 * Data Item Change Event.
 * @author Andrew Selkirk
 * @version $Revision: 1.3 $
 */
public class DataItemChangeEvent extends EventObject {

	//-------------------------------------------------------------
	// Variables --------------------------------------------------
	//-------------------------------------------------------------

	/**
	 * Source object.
	 */
	private	Object				source		= null;

	/**
	 * Changed item.
	 */
	private	Object				changedItem	= null;

	/**
	 * Property map.
	 */
	private	InfoBusPropertyMap	map			= null;


	//-------------------------------------------------------------
	// Initialization ---------------------------------------------
	//-------------------------------------------------------------

	/**
	 * Create data item change event.
	 * @param source Source object
	 * @param changedItem Changed item
	 * @param propertyMap Property map
	 */
	protected DataItemChangeEvent(Object source, Object changedItem, 
								InfoBusPropertyMap propertyMap) {
		super(source);
		this.source = source;
		this.changedItem = changedItem;
		this.map = propertyMap;
	} // DataItemChangeEvent()


	//-------------------------------------------------------------
	// Public Accessor Methods ------------------------------------
	//-------------------------------------------------------------

	/**
	 * Get source object.
	 * @return Source object
	 */
	public Object getSource() {
		return source;
	} // getSource()

	/**
	 * Get changed item.
	 * @return Changed item object
	 */
	public Object getChangedItem() {
		return changedItem;
	} // getChangedItem()

	/**
	 * Get property value
	 * @param propertyName Property name key
	 * @return TODO
	 */
	public Object getProperty(String propertyName) {
		return map.get(propertyName);
	} // getProperty()

		
} // DataItemChangeEvent
