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
 * Data Item Change Listener support.
 * @author Andrew Selkirk
 * @version $Revision: 1.3 $
 */
public class DataItemChangeListenerSupport { // TODO

	//-------------------------------------------------------------
	// Initialization ---------------------------------------------
	//-------------------------------------------------------------

	/**
	 * Create data item change listener support.
	 */
	public DataItemChangeListenerSupport() {
	} // DataItemChangeListenerSupport()


	//-------------------------------------------------------------
	// Public Accessor Methods ------------------------------------
	//-------------------------------------------------------------

	/**
	 * Data item added.
	 * @param event Data item added event
	 */
	public void dataItemAdded(DataItemAddedEvent event) {
	} // dataItemAdded()

	/**
	 * Data item deleted.
	 * @param event Data item deleted event
	 */
	public void dataItemDeleted(DataItemDeletedEvent event) {
 	} // dataItemDeleted()

	/**
	 * Data item revoked.
	 * @param event Data item revoked event
	 */
	public void dataItemRevoked(DataItemRevokedEvent event) {
	} // dataItemRevoked()

	/**
	 * Data item shape changed.
	 * @param event Data item shape changed event
	 */
	public void dataItemShapedChanged(DataItemShapeChangedEvent event) {
	} // dataItemShapeChanged()

	/**
	 * Data item value changed.
	 * @param event Data item value changed event
	 */
	public void dataItemValueChanged(DataItemValueChangedEvent event) {
	} // dataItemValueChanged()

	/**
	 * Rowset cursor moved.
	 * @param event Rowset cursor moved event
	 */
	public void rowsetCursorMoved(RowsetCursorMovedEvent event) {
	} // rowsetCursorMoved()


} // DataItemChangeListenerSupport
