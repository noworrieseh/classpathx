/********************************************************************
 * Copyright (c) Open Java Extensions, Andrew Selkirk  LGPL License *
 ********************************************************************/

package javax.xml.transform;

/**
 * Source Locator
 * @author	Andrew Selkirk
 * @version	1.0
 */
public interface SourceLocator {

	//-------------------------------------------------------------
	// Interface: SourceLocator -----------------------------------
	//-------------------------------------------------------------

	public String getPublicId();

	public String getSystemId();

	public int getLineNumber();

	public int getColumnNumber();


} // SourceLocator


