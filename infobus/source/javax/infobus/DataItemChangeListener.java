/********************************************************************
 * Copyright (c) Open Java Extensions, Andrew Selkirk  LGPL License *
 ********************************************************************/

package javax.infobus;

/**
 * Data Item Change Listener interface.
 */
public abstract interface DataItemChangeListener {

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
