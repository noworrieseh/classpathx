/********************************************************************
 * Copyright (c) Open Java Extensions, Andrew Selkirk  LGPL License *
 ********************************************************************/

package javax.infobus;

// Imports
import java.beans.PropertyVetoException;
import java.io.Serializable;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.io.IOException;

/**
 * InfoBus Bean Support.
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
	protected	String	m_infoBusName	= null;


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
		m_infoBusName = initialBusName;
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
	 */
	public void setInfoBus(InfoBus newInfoBus) 
			throws PropertyVetoException {
		super.setInfoBus(newInfoBus);
		if (newInfoBus != null) {
			m_infoBusName = newInfoBus.getName();
		}
	} // setInfoBus()

	/**
	 * Get InfoBus name.
	 * @returns InfoBus name
	 */
	public String getInfoBusName() {
		if (m_infoBusName == null) {
			return "";
		}
		return m_infoBusName;
	} // getInfoBusName()

	/**
	 * Set InfoBus name.
	 * @param newBusName New bus name
	 */
	public void setInfoBusName(String newBusName) 
			throws InfoBusMembershipException {
		if (newBusName.equals("-default") == true) {
			//TODO
		} else {
			m_infoBusName = newBusName;
		}
	} // setInfoBusName()

	/**
	 * Leave InfoBus.
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
				joinInfoBus(m_infoBusName);
			} catch (PropertyVetoException e) {
			}
		}
	} // rejoinInfoBus()


} // InfoBusBeanSupport
