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
 * Data Item Shape Changed event.
 * @author Andrew Selkirk
 * @version $Revision: 1.3 $
 */
public final	class	DataItemShapeChangedEvent
				extends	DataItemChangeEvent {

	//-------------------------------------------------------------
	// Initialization ---------------------------------------------
	//-------------------------------------------------------------

	/**
	 * Create new DataItem shape changed event.
	 * @param source Event source
	 * @param changedItem Item that changed
	 * @param propertyMap Property map
	 */
	public DataItemShapeChangedEvent(Object			source, 
								 Object				changedItem, 
								 InfoBusPropertyMap propertyMap) {
		super(source, changedItem, propertyMap);
	} // DataItemShapeChangedEvent()


} // DataItemShapeChangedEvent
