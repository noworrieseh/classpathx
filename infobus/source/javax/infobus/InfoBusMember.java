/********************************************************************
 * Copyright (c) Open Java Extensions, Andrew Selkirk  LGPL License *
 ********************************************************************/

package javax.infobus;

// Imports
import java.beans.PropertyVetoException;
import java.beans.VetoableChangeListener;
import java.beans.PropertyChangeListener;

/**
 * InfoBus Member interface.
 */
public abstract interface InfoBusMember {

	//-------------------------------------------------------------
	// Interface: InfoBusMember -----------------------------------
	//-------------------------------------------------------------

	/**
	 * Get Infobus
	 * @returns Infobus instance. Null is possible
	 */
	public InfoBus getInfoBus();

	/**
	 * Set InfoBus.  Do not call this directly.  Set by InfoBus.
	 * @param infoBus InfoBus
	 * @throws PropertyVetoException Veto exception
	 */
	public void setInfoBus(InfoBus newInfoBus)
		throws PropertyVetoException;

	/**
	 * Add Vetoable Change Listener
	 * @param listener Vetoable change listener
	 */
	public void addInfoBusVetoableListener(VetoableChangeListener listener);

	/**
	 * Remove Vetoable Change Listener
	 * @param listener Vetoable change listener
	 */
	public void removeInfoBusVetoableListener(VetoableChangeListener listener);

	/**
	 * Add Property Change Listener
	 * @param listener Property change listener
	 */
	public void addInfoBusPropertyListener(PropertyChangeListener listener);

	/**
	 * Remove Property Change Listener
	 * @param listener Property change listener
	 */
	public void removeInfoBusPropertyListener(PropertyChangeListener listener);


} // InfoBusMember
