/********************************************************************
 * Copyright (c) Open Java Extensions, Andrew Selkirk  LGPL License *
 ********************************************************************/

package javax.xml.transform.sax;

// Imports
import org.xml.sax.ContentHandler;
import org.xml.sax.ext.LexicalHandler;
import javax.xml.transform.Result;
import javax.xml.transform.Transformer;

/**
 * Transformer Handler
 * @author	Andrew Selkirk
 * @version	1.0
 */
public interface TransformerHandler extends	ContentHandler, 
						LexicalHandler {

	//-------------------------------------------------------------
	// Interface: TransformerHandler ------------------------------
	//-------------------------------------------------------------

	public void setResult(Result result) 
		throws IllegalArgumentException;

	public void setSystemId(String systemID);

	public String getSystemId();

	public Transformer getTransformer();


} // TransformerHandler

