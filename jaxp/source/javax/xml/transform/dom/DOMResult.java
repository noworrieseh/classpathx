/********************************************************************
 * Copyright (c) Open Java Extensions, Andrew Selkirk  LGPL License *
 ********************************************************************/

package javax.xml.transform.dom;

// Imports
import org.w3c.dom.Node;
import javax.xml.transform.Result;

/**
 * DOM Result
 * @author	Andrew Selkirk
 * @version	1.0
 */
public class DOMResult implements Result {

	//-------------------------------------------------------------
	// Variables --------------------------------------------------
	//-------------------------------------------------------------

	public static final String FEATURE =
		"http://javax.xml.transform.dom.DOMResult/feature";

	private Node	node		= null;
	private String	systemId	= null;


	//-------------------------------------------------------------
	// Initialization ---------------------------------------------
	//-------------------------------------------------------------

	public DOMResult() {
	} // DOMResult()

	public DOMResult(Node node) {
		this.node = node;
	} // DOMResult()

	public DOMResult(Node node, String systemID) {
		this.node = node;
		this.systemId = systemID;
	} // DOMResult()


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
		this.systemId = systemID;
	} // systemID()

	public String getSystemId() {
		return systemId;
	} // getSystemId()


} // DOMResult

