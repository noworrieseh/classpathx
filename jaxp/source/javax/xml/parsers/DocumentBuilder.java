/********************************************************************
 * Copyright (c) Open Java Extensions, Andrew Selkirk  LGPL License *
 ********************************************************************/

package javax.xml.parsers;

// Imports
import java.io.*;
import java.net.*;
import org.w3c.dom.*;
import org.xml.sax.*;

/**
 * DocumentBuilder
 * @author	Andrew Selkirk
 * @version	1.0
 */
public abstract class DocumentBuilder {

	//-------------------------------------------------------------
	// Initialization ---------------------------------------------
	//-------------------------------------------------------------

	protected DocumentBuilder() {
	} // DocumentBuilder()


	//-------------------------------------------------------------
	// Methods ----------------------------------------------------
	//-------------------------------------------------------------

	public Document parse(InputStream stream) 
		throws SAXException, IOException {
		return parse(new InputSource(stream));
	} // parse()

	public Document parse(InputStream stream, String systemID) 
		throws SAXException, IOException {

		// Variables
		InputSource	source;

		// Create Source
		source = new InputSource(stream);
		source.setSystemId(systemID);

		// Parse Input Source
		return parse(source);

	} // parse()

	public Document parse(String uri) 
		throws SAXException, IOException {
		return parse(new InputSource(new URL(uri).openStream()));
	} // parse()

	public Document parse(File file) 
		throws SAXException, IOException {
		return parse(new InputSource(new FileInputStream(file)));
	} // parse()

	public abstract Document parse(InputSource source) 
		throws SAXException, IOException;

	public abstract boolean isNamespaceAware();
	public abstract boolean isValidating();
	public abstract void setEntityResolver(EntityResolver resolver);
	public abstract void setErrorHandler(ErrorHandler handler);
	public abstract Document newDocument();


} // DocumentBuilder


