/********************************************************************
 * Copyright (c) Open Java Extensions, Andrew Selkirk  LGPL License *
 ********************************************************************/

package javax.xml.transform;

// Imports
import java.util.Properties;

/**
 * Templates
 * @author	Andrew Selkirk
 * @version	1.0
 */
public interface Templates {

	//-------------------------------------------------------------
	// Interface: Templates ---------------------------------------
	//-------------------------------------------------------------

	public Transformer newTransformer()
		throws TransformerConfigurationException;

	public Properties getOutputProperties();


} // Templates

