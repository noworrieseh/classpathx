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
 * Data Item Change Listener interface.
 * @author Andrew Selkirk
 * @version $Revision: 1.2 $
 */
public interface DataItemChangeListener {

	//-------------------------------------------------------------
	// Interface: DataItemChangeListener --------------------------
	//-------------------------------------------------------------

	/**
	 * Data item value changed.
	 * @param event Data item value changed event
	 */
	public void dataItemValueChanged(DataItemValueChangedEvent event);

	/**
	 * Data item added.
	 * @param event Data item added event
	 */
	public void dataItemAdded(DataItemAddedEvent event);

	/**
	 * Data item deleted.
	 * @param event Data item deleted event
	 */
	public void dataItemDeleted(DataItemDeletedEvent event);

	/**
	 * Data item revoked.
	 * @param event Data item revoked event
	 */
	public void dataItemRevoked(DataItemRevokedEvent event);

	/**
	 * Rowset cursor moved..
	 * @param event Rowset cursor moved event
	 */
	public void rowsetCursorMoved(RowsetCursorMovedEvent event);


} // DataItemChangeListener
