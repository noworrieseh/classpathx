/*
 * $Id: SAXParser.java,v 1.3 2001-11-02 21:39:57 db Exp $
 * Copyright (C) 2001 Andrew Selkirk
 * Copyright (C) 2001 David Brownell
 * 
 * This file is part of GNU JAXP, a library.
 *
 * GNU JAXP is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 * GNU JAXP is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 * As a special exception, if you link this library with other files to
 * produce an executable, this library does not by itself cause the
 * resulting executable to be covered by the GNU General Public License.
 * This exception does not however invalidate any other reasons why the
 * executable file might be covered by the GNU General Public License. 
 */

package javax.xml.parsers;

// Imports
import java.io.*;
import java.net.*;
import org.w3c.dom.*;
import org.xml.sax.*;
import org.xml.sax.helpers.*;

/**
 * Wraps a SAX2 (or SAX1) parser.
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

	public abstract void setProperty (String id, Object value) 
		throws SAXNotRecognizedException, SAXNotSupportedException;

	public abstract Object getProperty (String id) 
		throws SAXNotRecognizedException, SAXNotSupportedException;

	public void parse(InputStream stream, HandlerBase handler) 
		throws SAXException, IOException {
		parse(new InputSource(stream), handler);
	} // parse()

	public void parse (
		InputStream stream,
		HandlerBase handler,
		String systemID
	) throws SAXException, IOException {

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

	public void parse (
		InputStream stream,
		DefaultHandler def,
		String systemID
	) throws SAXException, IOException {

		// Variables
		InputSource	source;

		// Prepare Source
		source = new InputSource(stream);
		source.setSystemId(systemID);

		parse(source, def);

	} // parse()

	public void parse(String uri, HandlerBase handler) 
		throws SAXException, IOException {
		parse(new InputSource(uri), handler);
	} // parse()

	public void parse(String uri, DefaultHandler def) 
		throws SAXException, IOException {
		parse(new InputSource(uri), def);
	} // parse()

	public void parse(File file, HandlerBase handler) 
		throws SAXException, IOException {
// FIXME:  never discard the uri!  fileToURI()
		parse(new InputSource(new FileInputStream(file)), handler);
	} // parse()

	public void parse(File file, DefaultHandler def) 
		throws SAXException, IOException {
// FIXME:  never discard the uri!  fileToURI()
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

		// Parse
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

		// FIXME: if "def" implements lexical or decl handler,
		// set those properties ...

		// Parse
		reader.parse(source);

	} // parse()

	public abstract Parser getParser() throws SAXException;

	public abstract XMLReader getXMLReader() throws SAXException;

	public abstract boolean isNamespaceAware();

	public abstract boolean isValidating();
}
