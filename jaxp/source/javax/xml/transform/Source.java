/********************************************************************
 * Copyright (c) Open Java Extensions, Andrew Selkirk  LGPL License *
 ********************************************************************/

package javax.xml.transform;

/**
 * Source
 * @author	Andrew Selkirk
 * @version	1.0
 */
public interface Source {

	//-------------------------------------------------------------
	// Interface: Source ------------------------------------------
	//-------------------------------------------------------------

	public String getSystemId();

	public void setSystemId(String systemID);


} // Source

