/********************************************************************
 * Copyright (c) Open Java Extensions, Andrew Selkirk  LGPL License *
 ********************************************************************/

package javax.xml.transform;

/**
 * ErrorListener
 * @author	Andrew Selkirk
 * @version	1.0
 */
public interface ErrorListener {

	//-------------------------------------------------------------
	// Interface: ErrorListener -----------------------------------
	//-------------------------------------------------------------

	public void error(TransformerException exception)
		throws TransformerException;

	public void fatalError(TransformerException exception)
		throws TransformerException;

	public void warning(TransformerException exception)
		throws TransformerException;


} // ErrorListener

