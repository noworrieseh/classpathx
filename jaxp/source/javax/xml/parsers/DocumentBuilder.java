/*
  GNU-Classpath Extensions:	jaxp
  Copyright (C) 2001 Andrew Selkirk
  Copyright (C) 2001 David Brownell

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
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.IOException;
// import java.net.*;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;


/**
 * DocumentBuilder
 * @author	Andrew Selkirk, David Brownell
 * @version	$Id: DocumentBuilder.java,v 1.4 2001-07-16 16:11:59 db Exp $
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

	public abstract DOMImplementation getDOMImplementation ();
	
	public abstract boolean isNamespaceAware();

	public abstract boolean isValidating();

	public abstract Document newDocument();

	public Document parse(File file) 
		throws SAXException, IOException {
// FIXME:  map the filename to a URI,
// without relying on the jdk 1.2 API for that 
		return parse(new InputSource(new FileInputStream(file)));
	} // parse()

	public abstract Document parse(InputSource source) 
		throws SAXException, IOException;

	/**
	 * Avoid using this call; provide the system ID wherever possible.
	 * System IDs are essential when parsers resolve relative URIs,
	 * or provide diagnostics.
	 */
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
		return parse(new InputSource(uri));
	} // parse()

	public abstract void setEntityResolver(EntityResolver resolver);

	public abstract void setErrorHandler(ErrorHandler handler);
}
