/********************************************************************
 * Copyright (c) Open Java Extensions, Andrew Selkirk  LGPL License *
 ********************************************************************/

package javax.infobus;

/**
 * Rowset Cursor Moved Event.
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
