/********************************************************************
 * Copyright (c) Open Java Extensions, Andrew Selkirk  LGPL License *
 ********************************************************************/

package javax.infobus;

// Imports
import java.util.EventListener;
import java.beans.PropertyChangeListener;

/**
 * InfoBus Event Listener.
 */
public abstract interface	InfoBusEventListener
		extends		EventListener,
					PropertyChangeListener {

} // InfoBusEventListener
