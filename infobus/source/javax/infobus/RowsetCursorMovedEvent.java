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
 * Rowset Cursor Moved Event.
 * @author Andrew Selkirk
 * @version $Revision: 1.2 $
 */
public final	class	RowsetCursorMovedEvent
		extends	DataItemChangeEvent {

	//-------------------------------------------------------------
	// Initialization ---------------------------------------------
	//-------------------------------------------------------------

	/**
	 * Create Rowset Cursor Moved event.
	 * @param source Source of event
	 * @Param changedItem Item that changed
	 * @param propertyMap Property map
	 */
	public RowsetCursorMovedEvent(Object 				source, 
							  Object 				changedItem,
							  InfoBusPropertyMap 	propertyMap) {
		super(source, changedItem, propertyMap);
	} // RowsetCursorMovedEvent()


} // RowsetCursorMovedEvent
