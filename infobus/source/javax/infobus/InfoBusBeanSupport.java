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
import java.io.Serializable;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.io.IOException;

/**
 * InfoBus Bean Support.
 * @author Andrew Selkirk
 * @version $Revision: 1.3 $
 */
public	class		InfoBusBeanSupport
		extends		InfoBusMemberSupport
		implements	InfoBusBean,
					Serializable {

	//-------------------------------------------------------------
	// Variables --------------------------------------------------
	//-------------------------------------------------------------

	/**
	 * InfoBus name.
	 */
	protected	String	infoBusName	= null;


	//-------------------------------------------------------------
	// Initialization ---------------------------------------------
	//-------------------------------------------------------------

	/**
	 * Default Constructor.
	 */
	public InfoBusBeanSupport() {
		this(null, null);
	} // InfoBusBeanSupport()

	/**
	 * Create new InfoBusBeanSupport.
	 * @param parent InfoBus member
	 */
	public InfoBusBeanSupport(InfoBusMember parent) {
		this(parent, null);
	} // InfoBusBeanSupport()

	/**
	 * Create new InfoBusBeanSupport.
	 * @param parent InfoBus member
	 * @param initialBusName InfoBus name
	 */
	public InfoBusBeanSupport(InfoBusMember parent, String initialBusName) {
		super(parent);
		infoBusName = initialBusName;
	} // InfoBusBeanSupport()


	//-------------------------------------------------------------
	// Public Accessor Methods ------------------------------------
	//-------------------------------------------------------------

	/**
	 * Write object to stream.
	 * @param stream Object Output stream
	 * @throws IOException IO exception
	 */
	private void writeObject(ObjectOutputStream stream) throws IOException {
		stream.defaultWriteObject();
	} // writeObject()

	/**
	 * Read object from stream.
	 * @param stream Object Input stream
	 * @throws IOException IO exception
	 * @throws ClassNotFoundException Class not found
	 */
	private void readObject(ObjectInputStream stream) 
			throws IOException, ClassNotFoundException {
		stream.defaultReadObject();
	} // readObject()

	/**
	 * Get InfoBus.
	 * @return InfoBus object
	 */
	public InfoBus getInfoBus() {
		return super.getInfoBus();
	} // getInfoBus()

	/**
	 * Set InfoBus.
	 * @param newInfoBus New InfoBus reference
	 * @throws PropertyVetoException TODO
	 */
	public void setInfoBus(InfoBus newInfoBus) 
			throws PropertyVetoException {
		super.setInfoBus(newInfoBus);
		if (newInfoBus != null) {
			infoBusName = newInfoBus.getName();
		}
	} // setInfoBus()

	/**
	 * Get InfoBus name.
	 * @return InfoBus name
	 */
	public String getInfoBusName() {
		if (infoBusName == null) {
			return "";
		} // if
		return infoBusName;
	} // getInfoBusName()

	/**
	 * Set InfoBus name.
	 * @param newBusName New bus name
	 * @throws InfoBusMembershipException TODO
	 */
	public void setInfoBusName(String newBusName) 
			throws InfoBusMembershipException {
		if (newBusName.equals("-default") == true) {
			//TODO
		} else {
			infoBusName = newBusName;
		} // if
	} // setInfoBusName()

	/**
	 * Leave InfoBus.
	 * @throws InfoBusMembershipException TODO
	 * @throws PropertyVetoException TODO
	 */
	public void leaveInfoBus() 
			throws InfoBusMembershipException, PropertyVetoException {
		super.leaveInfoBus();
	} // leaveInfoBus()

	/**
	 * Rejoing InfoBus.
	 * @throws InfoBusMembershipException Membership exception
	 */
	public void rejoinInfoBus() throws InfoBusMembershipException {
		if (getInfoBusName().equals("") == false) {
			try {
				joinInfoBus(infoBusName);
			} catch (PropertyVetoException e) {
			} // try
		}
	} // rejoinInfoBus()


} // InfoBusBeanSupport
