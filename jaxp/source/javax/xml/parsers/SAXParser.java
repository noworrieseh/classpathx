/*
  GNU-Classpath Extensions:	jaxp
  Copyright (C) 2001 Andrew Selkirk

  For more information on the classpathx please mail: classpathx-discuss@gnu.org

  This program is free software; you can redistribute it and/or
  modify it under the terms of the GNU General Public License
  as published by the Free Software Foundation; either version 2
  of the License, or (at your option) any later version.

  This program is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  GNU General Public License for more details.

  You should have received a copy of the GNU General Public License
  along with this program; if not, write to the Free Software
  Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
*/

package javax.xml.parsers;

// Imports
import java.io.*;
import java.net.*;
import org.w3c.dom.*;
import org.xml.sax.*;
import org.xml.sax.helpers.*;

/**
 * SAXParser
 * @author	Andrew Selkirk
 * @version	1.0
 */
public abstract class SAXParser {

	//-------------------------------------------------------------
	// Initialization ---------------------------------------------
	//-------------------------------------------------------------

	protected SAXParser() {
	} // SAXParser()


	//-------------------------------------------------------------
	// Methods ----------------------------------------------------
	//-------------------------------------------------------------

	public abstract void setProperty(String value1, Object value2) 
		throws SAXNotRecognizedException, SAXNotSupportedException;

	public abstract Object getProperty(String value) 
		throws SAXNotRecognizedException, SAXNotSupportedException;

	public void parse(InputStream stream, HandlerBase handler) 
		throws SAXException, IOException {
		parse(new InputSource(stream), handler);
	} // parse()

	public void parse(InputStream stream, HandlerBase handler, String systemID)
		throws SAXException, IOException {

		// Variables
		InputSource	source;

		// Prepare Source
		source = new InputSource(stream);
		source.setSystemId(systemID);

		parse(source, handler);

	} // parse()

	public void parse(InputStream stream, DefaultHandler def) 
		throws SAXException, IOException {
		parse(new InputSource(stream), def);
	} // parse()

	public void parse(InputStream stream, DefaultHandler def, String systemID) 
		throws SAXException, IOException {

		// Variables
		InputSource	source;

		// Prepare Source
		source = new InputSource(stream);
		source.setSystemId(systemID);

		parse(source, def);

	} // parse()

	public void parse(String uri, HandlerBase handler) 
		throws SAXException, IOException {
		parse(new InputSource(new URL(uri).openStream()), handler);
	} // parse()

	public void parse(String uri, DefaultHandler def) 
		throws SAXException, IOException {
		parse(new InputSource(new URL(uri).openStream()), def);
	} // parse()

	public void parse(File file, HandlerBase handler) 
		throws SAXException, IOException {
		parse(new InputSource(new FileInputStream(file)), handler);
	} // parse()

	public void parse(File file, DefaultHandler def) 
		throws SAXException, IOException {
		parse(new InputSource(new FileInputStream(file)), def);
	} // parse()

	public void parse(InputSource source, HandlerBase handler) 
		throws SAXException, IOException {

		// Variables
		Parser	parser;

		// Prepare Parser
		parser = getParser();
		parser.setDocumentHandler(handler);
		parser.setDTDHandler(handler);
		parser.setEntityResolver(handler);
		parser.setErrorHandler(handler);

		// Parse stream
		parser.parse(source);

	} // parse()

	public void parse(InputSource source, DefaultHandler def) 
		throws SAXException, IOException {

		// Variables
		XMLReader	reader;

		// Prepare XML Reader
		reader = getXMLReader();
		reader.setContentHandler(def);
		reader.setDTDHandler(def);
		reader.setEntityResolver(def);
		reader.setErrorHandler(def);

		// Parse stream
		reader.parse(source);

	} // parse()

	public abstract Parser getParser() throws SAXException;

	public abstract XMLReader getXMLReader() throws SAXException;

	public abstract boolean isNamespaceAware();

	public abstract boolean isValidating();


} // SAXParser



