/********************************************************************
 * Copyright (c) Open Java Extensions, Andrew Selkirk  LGPL License *
 ********************************************************************/

package javax.xml.transform.sax;

// Imports
import org.xml.sax.ContentHandler;
import org.xml.sax.ext.LexicalHandler;
import javax.xml.transform.Result;

/**
 * SAX Result
 * @author	Andrew Selkirk
 * @version	1.0
 */
public class SAXResult implements Result {

	//-------------------------------------------------------------
	// Variables --------------------------------------------------
	//-------------------------------------------------------------

	public static final String FEATURE =
		"http://javax.xml.transform.sax.SAXResult/feature";

	private ContentHandler	handler		= null;
	private LexicalHandler	lexhandler	= null;
	private String		systemId	= null;


	//-------------------------------------------------------------
	// Initialization ---------------------------------------------
	//-------------------------------------------------------------

	public SAXResult() {
	} // SAXResult()

	public SAXResult(ContentHandler handler) {
		this.handler = handler;
	} // SAXResult()


	//-------------------------------------------------------------
	// Methods ----------------------------------------------------
	//-------------------------------------------------------------

	public void setHandler(ContentHandler handler) {
		this.handler = handler;
	} // setHandler()

	public ContentHandler getHandler() {
		return handler;
	} // getHandler()

	void setLexicalHandler(LexicalHandler lexHandler) {
		this.lexhandler = lexHandler;
	} // setLexicalHandler()

	LexicalHandler getLexicalHandler() {
		return lexhandler;
	} // getLexicalHandler()

	public void setSystemId(String systemID) {
		this.systemId = systemID;
	} // setSystemId()

	public String getSystemId() {
		return systemId;
	} // getSystemId()


} // SAXResult

