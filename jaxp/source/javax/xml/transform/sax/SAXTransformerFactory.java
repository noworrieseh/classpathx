/********************************************************************
 * Copyright (c) Open Java Extensions, Andrew Selkirk  LGPL License *
 ********************************************************************/

package javax.xml.transform.sax;

// Imports
import org.xml.sax.XMLFilter;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.Source;
import javax.xml.transform.Templates;
import javax.xml.transform.TransformerConfigurationException;

/**
 * SAX Transformer Factory
 * @author	Andrew Selkirk
 * @version	1.0
 */
public abstract class SAXTransformerFactory extends TransformerFactory {

	//-------------------------------------------------------------
	// Constants --------------------------------------------------
	//-------------------------------------------------------------

	public static final String FEATURE =
		"http://javax.xml.transform.sax.SAXTransformerFactory/feature";

	public static final String FEATURE_XMLFILTER =
		"http://javax.xml.transform.sax.SAXTransformerFactory/feature/xmlfilter";


	//-------------------------------------------------------------
	// Initialization ---------------------------------------------
	//-------------------------------------------------------------
	protected SAXTransformerFactory() {
	} // SAXTransformerFactory()


	//-------------------------------------------------------------
	// Methods ----------------------------------------------------
	//-------------------------------------------------------------

	public abstract TransformerHandler newTransformerHandler(Source source)
		throws TransformerConfigurationException;

	public abstract TransformerHandler newTransformerHandler(Templates templates)
		throws TransformerConfigurationException;

	public abstract TransformerHandler newTransformerHandler()
		throws TransformerConfigurationException;

	public abstract TemplatesHandler newTemplatesHandler()
		throws TransformerConfigurationException;

	public abstract XMLFilter newXMLFilter(Source source)
		throws TransformerConfigurationException;

	public abstract XMLFilter newXMLFilter(Templates templates)
		throws TransformerConfigurationException;


} // SAXTransformerFactory

