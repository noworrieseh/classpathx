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
 * Data Item Added event.
 * @author Andrew Selkirk
 * @version $Revision: 1.2 $
 */
public final	class	DataItemAddedEvent
		extends	DataItemChangeEvent {

	//-------------------------------------------------------------
	// Variables --------------------------------------------------
	//-------------------------------------------------------------

	/**
	 * Changed collection.
	 */
	private	Object	changedCollection	= null;


	//-------------------------------------------------------------
	// Initialization ---------------------------------------------
	//-------------------------------------------------------------

	/**
	 * Create data item added event.
	 * @param source Source object
	 * @param changedItem Changed item
	 * @param changedCollection Changed collection
	 * @param propertyMap Property map
	 */
	public DataItemAddedEvent(Object source, Object changedItem, 
						  Object				changedCollection, 
						  InfoBusPropertyMap	propertyMap) {
		super(source, changedItem, propertyMap);
		this.changedCollection = changedCollection;
	} // DataItemAddedEvent()


	//-------------------------------------------------------------
	// Public Accessor Methods ------------------------------------
	//-------------------------------------------------------------

	/**
	 * Get changed collection.
	 * @returns Changed collection
	 */
	public Object getChangedCollection() {
		return changedCollection;
	} // getChangedCollection()


} // DataItemAddedEvent
