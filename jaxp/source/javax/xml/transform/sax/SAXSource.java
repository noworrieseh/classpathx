/********************************************************************
 * Copyright (c) Open Java Extensions, Andrew Selkirk  LGPL License *
 ********************************************************************/

package javax.xml.transform.sax;

// Imports
import org.xml.sax.XMLReader;
import org.xml.sax.InputSource;
import javax.xml.transform.Source;

/**
 * SAX Source
 * @author	Andrew Selkirk
 * @version	1.0
 */
public class SAXSource implements Source {

	//-------------------------------------------------------------
	// Variables --------------------------------------------------
	//-------------------------------------------------------------

	public static final String FEATURE =
		"http://javax.xml.transform.sax.SAXSource/feature";

	private XMLReader	reader		= null;
	private InputSource	inputSource	= null;


	//-------------------------------------------------------------
	// Initialization ---------------------------------------------
	//-------------------------------------------------------------

	public SAXSource() {
	} // SAXSource()

	public SAXSource(XMLReader reader, InputSource source) {
		this.reader = reader;
		this.inputSource = source;
	} // SAXSource()

	public SAXSource(InputSource source) {
		this.inputSource = source;
	} // SAXSource()


	//-------------------------------------------------------------
	// Methods ----------------------------------------------------
	//-------------------------------------------------------------

	public void setXMLReader(XMLReader reader) {
		this.reader = reader;
	} // setXMLReader()

	public XMLReader getXMLReader() {
		return reader;
	} // getXMLReader()

	public void setInputSource(InputSource source) {
		this.inputSource = source;
	} // setInputSource()

	public InputSource getInputSource() {
		return inputSource;
	} // inputSource()

	public void setSystemId(String systemID) {
		if (inputSource != null) {
			inputSource.setSystemId(systemID);
		}
	} // setSystemId()

	public String getSystemId() {
		if (inputSource != null) {
			return inputSource.getSystemId();
		} // if
		return null;
	} // getSystemId()

	public static InputSource sourceToInputSource(Source source) {
		return null; // TODO
	} // sourceToInputSource()


} // SAXSource


