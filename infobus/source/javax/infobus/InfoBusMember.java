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
import java.beans.PropertyVetoException;
import java.beans.VetoableChangeListener;
import java.beans.PropertyChangeListener;

/**
 * InfoBus Member interface.
 * @author Andrew Selkirk
 * @version $Revision: 1.3 $
 */
public interface InfoBusMember {

	//-------------------------------------------------------------
	// Interface: InfoBusMember -----------------------------------
	//-------------------------------------------------------------

	/**
	 * Get Infobus
	 * @return Infobus instance. Null is possible
	 */
	public InfoBus getInfoBus();

	/**
	 * Set InfoBus.  Do not call this directly.  Set by InfoBus.
	 * @param newInfoBus InfoBus
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
