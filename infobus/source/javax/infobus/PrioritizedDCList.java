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
import java.util.*;

/**
 * Prioritized Data Controller List.  Organizes Data Controllers
 * based on priority.
 * @author Andrew Selkirk
 * @version $Revision: 1.2 $
 */
class PrioritizedDCList implements Cloneable {

	//-------------------------------------------------------------
	// Variables --------------------------------------------------
	//-------------------------------------------------------------

	/**
	 * Data Controller list.  List is prioritized.
	 */
	private	Vector	m_controllerList	= null;

	/**
	 * Priority List.  There is a 1-1 matching of
	 * priority vales and controller list.  List is
	 * in prioritized ordering.
	 */
	private	Vector	m_priorityList		= null;

	/**
	 * Parent Infobus
	 */
	private	InfoBus	m_parentIB			= null;

	/**
	 * Synchonization locking object
	 */
	private transient Object m_syncLock	= null;


	//-------------------------------------------------------------
	// Initialization ---------------------------------------------
	//-------------------------------------------------------------

	/**
	 * Create default PrioritizedDCList.
	 */
	protected PrioritizedDCList() {
		m_controllerList = new Vector();
		m_priorityList = new Vector();
	} // PrioritizedDCList()

	/**
	 * Create PrioritizedDCList.
	 * @param value1 What is this for?
	 * @param value2 What is this for?
	 * @param infoBus Parent Infobus
	 */
	protected PrioritizedDCList(int value1, int value2, InfoBus infoBus) {
		m_parentIB = infoBus;
		m_controllerList = new Vector(value1, value2);
		m_priorityList = new Vector(value1, value2);
	} // PrioritizedDCList()


	//-------------------------------------------------------------
	// Public Accessor Methods ------------------------------------
	//-------------------------------------------------------------

	/**
	 * Add data controller.
	 * @param controller Data controller to add
	 * @param priority Priority of controller
	 */
	protected void addDataController(InfoBusDataController controller, 
								 int priority) {

		// Variables
		int		index;
		Integer	value;

		for (index = 0; index < size(); index++) {
			value = (Integer) m_priorityList.elementAt(index);
			if (value.intValue() < priority) {
				m_controllerList.insertElementAt(controller, index);
				m_priorityList.insertElementAt(new Integer(priority), 
												index);
				return;
			}
		} // for

		m_controllerList.addElement(controller);
		m_priorityList.addElement(new Integer(priority));

	} // addDataController()

	/**
	 * Remove data controller.
	 * @param controller Data controller to remove
	 */
	protected void removeDataController(InfoBusDataController controller) {

		// Variables
		int	index;

		// Get Index of Controller
		index = m_controllerList.indexOf(controller);

		// Remove Controller
		if (index != -1) {
			m_controllerList.removeElementAt(index);
			m_priorityList.removeElementAt(index);
		}

	} // removeDataController()

	/**
	 * Get clone of Data Controller list
	 * @returns Vector of controllers (in prioritized order)
	 */
	protected Vector getDCclone() {
		return (Vector) m_controllerList.clone();
	} // getDCclone()

	/**
	 * Clone.
	 * @returns Cloned PrioritizedDCList
	 */
	public Object clone() {
		return this; // TODO
	} // clone()

	/**
	 * Get size of Data Controller list.
	 * @returns Size of Data Controller list
	 */
	protected int size() {
		return m_controllerList.size();
	} // size()

	/**
	 * Get Data Controller at a specified position.
	 * @param index Index of controller
	 * @returns Data Controller at specified position
	 */
	protected InfoBusDataController controllerAt(int index) {
		return (InfoBusDataController) m_controllerList.get(index);
	} // controllerAt()

	/**
	 * Get priority at a specified position.
	 * @param index Index of controller
	 * @returns Priority at specified position
	 */
	protected int priorityAt(int index) {
		return ((Integer) m_priorityList.get(index)).intValue();
	} // priorityAt()

	/**
	 * Get enumeration of Data Controllers.
	 * @returns Enumeration of Data Controllers
	 */
	protected Enumeration elements() {
		return m_controllerList.elements();
	} // elements()


} // PrioritizedDCList
