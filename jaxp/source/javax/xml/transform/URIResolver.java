/********************************************************************
 * Copyright (c) Open Java Extensions, Andrew Selkirk  LGPL License *
 ********************************************************************/

package javax.xml.transform;

/**
 * URIResolver
 * @author	Andrew Selkirk
 * @version	1.0
 */
public interface URIResolver {

	//-------------------------------------------------------------
	// Interface: URIResolver -------------------------------------
	//-------------------------------------------------------------

	public Source resolve(String href, String base)
		throws TransformerException;


} // URIResolver

