/********************************************************************
 * Copyright (c) Open Java Extensions, Andrew Selkirk  LGPL License *
 ********************************************************************/

package javax.xml.transform.dom;

// Imports
import org.w3c.dom.Node;
import javax.xml.transform.Source;

/**
 * DOM Source
 * @author	Andrew Selkirk
 * @version	1.0
 */
public class DOMSource implements Source {

	//-------------------------------------------------------------
	// Variables --------------------------------------------------
	//-------------------------------------------------------------

	public static final String FEATURE =
		"http://javax.xml.transform.dom.DOMSource/feature";

	private Node	node 	= null;
		String	baseID	= null;


	//-------------------------------------------------------------
	// Initialization ---------------------------------------------
	//-------------------------------------------------------------

	public DOMSource() {
	} // DOMSource()

	public DOMSource(Node node) {
		this.node = node;
	} // DOMSource()

	public DOMSource(Node node, String systemID) {
		this.node = node;
		this.baseID = systemID;
	} // DOMSource()


	//-------------------------------------------------------------
	// Methods ----------------------------------------------------
	//-------------------------------------------------------------

	public void setNode(Node node) {
		this.node = node;
	} // setNode()

	public Node getNode() {
		return node;
	} // getNode()

	public void setSystemId(String systemID) {
		baseID = systemID;
	} // setSystemId()

	public String getSystemId() {
		return baseID;
	} // getSystemId()

 
} // DOMSource

