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
import java.util.Enumeration;
import java.util.Vector;

/**
 * Prioritized Data Controller List.  Organizes Data Controllers
 * based on priority.
 * @author Andrew Selkirk
 * @version $Revision: 1.3 $
 */
class PrioritizedDCList implements Cloneable {

	//-------------------------------------------------------------
	// Variables --------------------------------------------------
	//-------------------------------------------------------------

	/**
	 * Data Controller list.  List is prioritized.
	 */
	private	Vector				controllerList	= null;

	/**
	 * Priority List.  There is a 1-1 matching of
	 * priority vales and controller list.  List is
	 * in prioritized ordering.
	 */
	private	Vector				priorityList	= null;

	/**
	 * Parent Infobus
	 */
	private	InfoBus				parentIB		= null;

	/**
	 * Synchonization locking object
	 */
	private transient Object	syncLock		= null;


	//-------------------------------------------------------------
	// Initialization ---------------------------------------------
	//-------------------------------------------------------------

	/**
	 * Create default PrioritizedDCList.
	 */
	protected PrioritizedDCList() {
		controllerList = new Vector();
		priorityList = new Vector();
	} // PrioritizedDCList()

	/**
	 * Create PrioritizedDCList.
	 * @param value1 What is this for?
	 * @param value2 What is this for?
	 * @param infoBus Parent Infobus
	 */
	protected PrioritizedDCList(int value1, int value2, InfoBus infoBus) {
		parentIB = infoBus;
		controllerList = new Vector(value1, value2);
		priorityList = new Vector(value1, value2);
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
			value = (Integer) priorityList.elementAt(index);
			if (value.intValue() < priority) {
				controllerList.insertElementAt(controller, index);
				priorityList.insertElementAt(new Integer(priority),
												index);
				return;
			}
		} // for

		controllerList.addElement(controller);
		priorityList.addElement(new Integer(priority));

	} // addDataController()

	/**
	 * Remove data controller.
	 * @param controller Data controller to remove
	 */
	protected void removeDataController(InfoBusDataController controller) {

		// Variables
		int	index;

		// Get Index of Controller
		index = controllerList.indexOf(controller);

		// Remove Controller
		if (index != -1) {
			controllerList.removeElementAt(index);
			priorityList.removeElementAt(index);
		} // if

	} // removeDataController()

	/**
	 * Get clone of Data Controller list
	 * @return Vector of controllers (in prioritized order)
	 */
	protected Vector getDCclone() {
		return (Vector) controllerList.clone();
	} // getDCclone()

	/**
	 * Clone.
	 * @return Cloned PrioritizedDCList
	 */
	public Object clone() {
		return this; // TODO
	} // clone()

	/**
	 * Get size of Data Controller list.
	 * @return Size of Data Controller list
	 */
	protected int size() {
		return controllerList.size();
	} // size()

	/**
	 * Get Data Controller at a specified position.
	 * @param index Index of controller
	 * @return Data Controller at specified position
	 */
	protected InfoBusDataController controllerAt(int index) {
		return (InfoBusDataController) controllerList.get(index);
	} // controllerAt()

	/**
	 * Get priority at a specified position.
	 * @param index Index of controller
	 * @return Priority at specified position
	 */
	protected int priorityAt(int index) {
		return ((Integer) priorityList.get(index)).intValue();
	} // priorityAt()

	/**
	 * Get enumeration of Data Controllers.
	 * @return Enumeration of Data Controllers
	 */
	protected Enumeration elements() {
		return controllerList.elements();
	} // elements()


} // PrioritizedDCList
