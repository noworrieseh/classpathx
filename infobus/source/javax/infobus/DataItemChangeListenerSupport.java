/********************************************************************
 * Copyright (c) Open Java Extensions, Andrew Selkirk  LGPL License *
 ********************************************************************/

package javax.infobus;

/**
 * Data Item Change Listener support.
 */
public class DataItemChangeListenerSupport {

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
