/********************************************************************
 * Copyright (c) Open Java Extensions, Andrew Selkirk  LGPL License *
 ********************************************************************/

package javax.infobus;

/**
 * InfoBus Bean.interface.
 */
public abstract interface	InfoBusBean
		extends		InfoBusMember {

	//-------------------------------------------------------------
	// Variables --------------------------------------------------
	//-------------------------------------------------------------

	public static final String USER_DEFAULT_INFOBUS	= "-default";


	//-------------------------------------------------------------
	// Interface: InfoBusBean -------------------------------------
	//-------------------------------------------------------------

	/**
	 * Get InfoBus name.
	 * @returns InfoBus name
	 */
	public String getInfoBusName();

	/**
	 * Set InfoBus name.
	 * @param newBusName New bus name
	 * @throws InfoBusMembershipException Membership exception
	 */
	public void setInfoBusName(String newBusName)
		throws InfoBusMembershipException;


} // InfoBusBean
