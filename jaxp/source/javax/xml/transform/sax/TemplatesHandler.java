/********************************************************************
 * Copyright (c) Open Java Extensions, Andrew Selkirk  LGPL License *
 ********************************************************************/

package javax.xml.transform.sax;

// Imports
import org.xml.sax.ContentHandler;
import javax.xml.transform.Templates;

/**
 * Templates Handler
 * @author	Andrew Selkirk
 * @version	1.0
 */
public interface TemplatesHandler extends ContentHandler {

	//-------------------------------------------------------------
	// Interface: TemplatesHandler --------------------------------
	//-------------------------------------------------------------

	public Templates getTemplates();

	public void setSystemId(String systemID);

	public String getSystemId();


} // TemplatesHandler
 
