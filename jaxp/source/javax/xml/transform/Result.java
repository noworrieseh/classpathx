/********************************************************************
 * Copyright (c) Open Java Extensions, Andrew Selkirk  LGPL License *
 ********************************************************************/

package javax.xml.transform;

/**
 * Result
 * @author	Andrew Selkirk
 * @version	1.0
 */
public interface Result {

	//-------------------------------------------------------------
	// Constants --------------------------------------------------
	//-------------------------------------------------------------

	public static final String PI_DISABLE_OUTPUT_ESCAPING =
			"javax.xml.transform.disable-output-escaping";

	public static final String PI_ENABLE_OUTPUT_ESCAPING =
			"javax.xml.transform.disable-output-escaping";


	//-------------------------------------------------------------
	// Interface: Result ------------------------------------------
	//-------------------------------------------------------------

	public String getSystemId();

	public void setSystemId(String systemID);


} // Result

