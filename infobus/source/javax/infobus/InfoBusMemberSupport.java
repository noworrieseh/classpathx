/********************************************************************
 * Copyright (c) Open Java Extensions, Andrew Selkirk  LGPL License *
 ********************************************************************/

package javax.infobus;

// Imports
import java.awt.Component;
import java.beans.*;
import java.util.*;
import java.beans.*;

/**
 * InfoBus Member Support.
 */
public 	class		InfoBusMemberSupport
	implements	InfoBusMember {

	//-------------------------------------------------------------
	// Variables --------------------------------------------------
	//-------------------------------------------------------------

	/**
	 * Member of this Infobus.
	 */
	protected	InfoBus					m_infoBus		= null;

	/**
	 * Property change support.
	 */
	protected	PropertyChangeSupport	m_propSupport	= null;

	/**
	 * Vetoable change support.
	 */
	protected	VetoableChangeSupport	m_vetoSupport	= null;

	/**
	 * Proxy for this InfoBus member
	 */
	protected	InfoBusMember			m_sourceRef		= null;

	/**
	 * Synchronization object
	 */
	protected	Object					m_syncLock		= null;


	//-------------------------------------------------------------
	// Initialization ---------------------------------------------
	//-------------------------------------------------------------

	/**
	 * Create InfoBusMemberSupport without proxy.  Equivalent of
	 * InfoBusMemberSupport(null)
	 */
	public InfoBusMemberSupport() {
		this(null);
	} // InfoBusMemberSupport()

	/**
	 * Create InfoBusMemberSupport object.
	 * @param member Member to proxy for
	 */
	public InfoBusMemberSupport(InfoBusMember member) {
		if (member == null) {
			m_sourceRef = this;
		} else {
			m_sourceRef = member;
		}
		m_propSupport = new PropertyChangeSupport(m_sourceRef);
		m_vetoSupport = new VetoableChangeSupport(m_sourceRef);
	} // InfoBusMemberSupport()


	//-------------------------------------------------------------
	// Public Accessor Methods ------------------------------------
	//-------------------------------------------------------------

	/**
	 * Get Infobus
	 * @returns Infobus instance. Null is possible
	 */
	public InfoBus getInfoBus() {
		return m_infoBus;
	} // getInfoBus()

	/**
	 * Set InfoBus.  Do not call this directly.  Set by InfoBus.
	 * @param infoBus InfoBus
	 * @throws PropertyVetoException Veto exception
	 */
	public void setInfoBus(InfoBus infoBus) throws PropertyVetoException {

		// Variables
		PropertyChangeEvent		event;

		// Create Event
		event = new PropertyChangeEvent(m_sourceRef, "InfoBus", 
										m_infoBus, infoBus);

		// Notify Vetoable Change Listeners
		m_vetoSupport.fireVetoableChange(event);

		// Set InfoBus member
		m_infoBus = infoBus;

		// Notify Property Change Listeners
		m_propSupport.firePropertyChange(event);

		if (m_infoBus != null) {
			m_infoBus.register(m_sourceRef);
		}

	} // setInfoBus()

	/**
	 * Join named InfoBus.
	 * @param busName Name of bus to join
	 * @throws InfoBusMembershipException Membership exception
	 * @throws PropertyVetoException Veto exception
	 */
	public void joinInfoBus(String busName)
			throws InfoBusMembershipException, PropertyVetoException {

		// Variables
  		InfoBus	infoBus;

		// Check For Membership
		if (m_infoBus != null) {
			throw new InfoBusMembershipException("already member of" +
												" infobus");
		}

		// Get InfoBus instance
		infoBus = InfoBus.get(busName);

		// Join InfoBus
   		if (infoBus != null) {
			infoBus.join(m_sourceRef);
			infoBus.release();
   		}

	} // joinInfoBus()

	/**
	 * Join InfoBus based on applet context.
	 * @param component Component child of applet
	 * @throws InfoBusMembershipException Membership exception
	 * @throws PropertyVetoException Veto exception
	 */
	public void joinInfoBus(Component component)
			throws InfoBusMembershipException, PropertyVetoException {

		// Variables
  		InfoBus infoBus;

		// Check For Membership
		if (m_infoBus != null) {
			throw new InfoBusMembershipException("already member " +
												"of infobus");
		}

		// Get InfoBus instance
		infoBus = InfoBus.get(component);

		// Join InfoBus
		if (infoBus != null) {
			infoBus.join(m_sourceRef);
			infoBus.release();
		}

	} // joinInfoBus()

	/**
	 * Leave InfoBus.
	 * @throws InfoBusMembershipException Membership exception
	 * @throws PropertyVetoException Veto exception
	 */
	public void leaveInfoBus()
			throws InfoBusMembershipException, PropertyVetoException {
		m_infoBus.leave(m_sourceRef);
	} // leaveInfoBus()

	/**
	 * Add Vetoable Change Listener
	 * @param listener Vetoable change listener
	 */
	public void addInfoBusVetoableListener(VetoableChangeListener listener) {
		m_vetoSupport.addVetoableChangeListener(listener);
	} // addInfoBusVetoableListener()

	/**
	 * Remove Vetoable Change Listener
	 * @param listener Vetoable change listener
	 */
	public void removeInfoBusVetoableListener(VetoableChangeListener listener) {
		m_vetoSupport.removeVetoableChangeListener(listener);
	} // removeInfoBusVetoableListener()

	/**
	 * Add Property Change Listener
	 * @param listener Property change listener
	 */
	public void addInfoBusPropertyListener(PropertyChangeListener listener) {
		m_propSupport.addPropertyChangeListener(listener);
	} // addInfoBusPropertyListener()

	/**
	 * Remove Property Change Listener
	 * @param listener Property change listener
	 */
	public void removeInfoBusPropertyListener(PropertyChangeListener listener) {
		m_propSupport.removePropertyChangeListener(listener);
	} // removeInfoBusPropertyListener()


} // InfoBusMemberSupport
