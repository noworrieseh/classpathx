/********************************************************************
 * Copyright (c) Open Java Extensions, Andrew Selkirk  LGPL License *
 ********************************************************************/

package javax.xml.transform.dom;

// Imports
import org.w3c.dom.Node;
import javax.xml.transform.SourceLocator;

/**
 * DOM Locator
 * @author	Andrew Selkirk
 * @version	1.0
 */
public interface DOMLocator extends SourceLocator {

	//-------------------------------------------------------------
	// Interface: DOMLocator --------------------------------------
	//-------------------------------------------------------------

	public Node getOriginatingNode();


} // DOMLocator


