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

// Imports
import java.awt.Component;
import java.beans.VetoableChangeListener;
import java.beans.PropertyChangeListener;
import java.beans.PropertyVetoException;
import java.beans.VetoableChangeSupport;
import java.beans.PropertyChangeSupport;
import java.beans.PropertyChangeEvent;

/**
 * InfoBus Member Support.
 * @author Andrew Selkirk
 * @version $Revision: 1.3 $
 */
public 	class		InfoBusMemberSupport
		implements	InfoBusMember {

	//-------------------------------------------------------------
	// Variables --------------------------------------------------
	//-------------------------------------------------------------

	/**
	 * Member of this Infobus.
	 */
	protected	InfoBus					infoBus		= null;

	/**
	 * Property change support.
	 */
	protected	PropertyChangeSupport	propSupport	= null;

	/**
	 * Vetoable change support.
	 */
	protected	VetoableChangeSupport	vetoSupport	= null;

	/**
	 * Proxy for this InfoBus member
	 */
	protected	InfoBusMember			sourceRef		= null;

	/**
	 * Synchronization object
	 */
	protected	Object					syncLock		= null;


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
			sourceRef = this;
		} else {
			sourceRef = member;
		} // if
		propSupport = new PropertyChangeSupport(sourceRef);
		vetoSupport = new VetoableChangeSupport(sourceRef);
	} // InfoBusMemberSupport()


	//-------------------------------------------------------------
	// Public Accessor Methods ------------------------------------
	//-------------------------------------------------------------

	/**
	 * Get Infobus
	 * @return Infobus instance. Null is possible
	 */
	public InfoBus getInfoBus() {
		return infoBus;
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
		event = new PropertyChangeEvent(sourceRef, "InfoBus",
										infoBus, infoBus);

		// Notify Vetoable Change Listeners
		vetoSupport.fireVetoableChange(event);

		// Set InfoBus member
		infoBus = infoBus;

		// Notify Property Change Listeners
		propSupport.firePropertyChange(event);

		if (infoBus != null) {
			infoBus.register(sourceRef);
		} // if

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
		if (this.infoBus != null) {
			throw new InfoBusMembershipException("already member of" +
												" infobus");
		} // if

		// Get InfoBus instance
		infoBus = InfoBus.get(busName);

		// Join InfoBus
   		if (infoBus != null) {
			infoBus.join(sourceRef);
			infoBus.release();
   		} // if

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
		if (this.infoBus != null) {
			throw new InfoBusMembershipException("already member " +
												"of infobus");
		} // if

		// Get InfoBus instance
		infoBus = InfoBus.get(component);

		// Join InfoBus
		if (infoBus != null) {
			infoBus.join(sourceRef);
			infoBus.release();
		} // if

	} // joinInfoBus()

	/**
	 * Leave InfoBus.
	 * @throws InfoBusMembershipException Membership exception
	 * @throws PropertyVetoException Veto exception
	 */
	public void leaveInfoBus()
			throws InfoBusMembershipException, PropertyVetoException {
		infoBus.leave(sourceRef);
	} // leaveInfoBus()

	/**
	 * Add Vetoable Change Listener
	 * @param listener Vetoable change listener
	 */
	public void addInfoBusVetoableListener(VetoableChangeListener listener) {
		vetoSupport.addVetoableChangeListener(listener);
	} // addInfoBusVetoableListener()

	/**
	 * Remove Vetoable Change Listener
	 * @param listener Vetoable change listener
	 */
	public void removeInfoBusVetoableListener(VetoableChangeListener listener) {
		vetoSupport.removeVetoableChangeListener(listener);
	} // removeInfoBusVetoableListener()

	/**
	 * Add Property Change Listener
	 * @param listener Property change listener
	 */
	public void addInfoBusPropertyListener(PropertyChangeListener listener) {
		propSupport.addPropertyChangeListener(listener);
	} // addInfoBusPropertyListener()

	/**
	 * Remove Property Change Listener
	 * @param listener Property change listener
	 */
	public void removeInfoBusPropertyListener(PropertyChangeListener listener) {
		propSupport.removePropertyChangeListener(listener);
	} // removeInfoBusPropertyListener()


} // InfoBusMemberSupport
