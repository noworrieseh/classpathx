/********************************************************************
 * Copyright (c) Open Java Extensions, Andrew Selkirk  LGPL License *
 ********************************************************************/

package javax.xml.transform;

/**
 * TransformerConfigurationException
 * @author	Andrew Selkirk
 * @version	1.0
 */
public class TransformerConfigurationException extends TransformerException {

	//-------------------------------------------------------------
	// Initialization ---------------------------------------------
	//-------------------------------------------------------------

	public TransformerConfigurationException() {
		super((String)null);
	} // TransformerConfigurationException()

	public TransformerConfigurationException(String msg) {
		super(msg);
	} // TransformerConfigurationException()

	public TransformerConfigurationException(Exception ex) {
		super(ex);
	} // TransformerConfigurationException()

	public TransformerConfigurationException(String msg, Exception ex) {
		super(msg, ex);
	} // TransformerConfigurationException()


} // TranformerConfigurationException


