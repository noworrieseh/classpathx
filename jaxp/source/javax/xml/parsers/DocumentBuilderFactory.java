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
import java.io.IOException;
import java.io.InputStream;
import java.io.FileInputStream;
import java.io.File;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Properties;
import org.w3c.dom.*;
import org.xml.sax.*;

/**
 * DocumentBuilderFactory is used to resolve the problem that the
 * W3C DOM APIs don't include portable bootstrapping.
 *
 * @author	Andrew Selkirk, David Brownell
 * @version	$Id: DocumentBuilderFactory.java,v 1.4 2001-07-16 16:11:59 db Exp $
 */
public abstract class DocumentBuilderFactory {

	//-------------------------------------------------------------
	// Variables --------------------------------------------------
	//-------------------------------------------------------------

	private static final	String defaultPropName	= 
		"javax.xml.parsers.DocumentBuilderFactory";

	private static		String foundFactory	= null;
	private static final	boolean debug		= false;

	private 		boolean validating	= false;
	private 		boolean namespaceAware	= false;
	private 		boolean whitespace	= false;
	private 		boolean expandEntityRef	= false;
	private 		boolean ignoreComments	= false;
	private 		boolean coalescing	= false;


	//-------------------------------------------------------------
	// Initialization ---------------------------------------------
	//-------------------------------------------------------------

	protected DocumentBuilderFactory() {
	} // DocumentBuilderFactory()


	//-------------------------------------------------------------
	// Methods ----------------------------------------------------
	//-------------------------------------------------------------

	public abstract Object getAttribute(String name) 
		throws IllegalArgumentException;

	public boolean isCoalescing() {
		return coalescing;
	} // isCoalescing()

	public boolean isExpandEntityReferences() {
		return expandEntityRef;
	} // isExpandEntityReferences()

	public boolean isIgnoringComments() {
		return ignoreComments;
	} // isIgnoringComments()

	public boolean isIgnoringElementContentWhitespace() {
		return whitespace;
	} // isIgnoringElementContentWhitespace()

	public boolean isNamespaceAware() {
		return namespaceAware;
	} // isNamespaceAware()

	public boolean isValidating() {
		return validating;
	} // isValidating()

	public abstract DocumentBuilder newDocumentBuilder()
		throws ParserConfigurationException;

	public static DocumentBuilderFactory newInstance() {

		// Variables
		Class			classObject;
		DocumentBuilderFactory	factory;

		// Locate Factory
		foundFactory = findFactory(defaultPropName, 
			"gnu.xml.dom.JAXPFactory");

		try {

			// Get Class
			classObject = Class.forName(foundFactory);

			// Instantiate Class
			factory = (DocumentBuilderFactory)
				classObject.newInstance();

			// Return Instance
			return factory;

		} catch (Exception e) {
			throw new FactoryConfigurationError(e);
		} // try		

	} // newInstance()

	public abstract void setAttribute(String name, Object value) 
		throws IllegalArgumentException;

	public void setCoalescing(boolean value) {
		coalescing = value;
	} // setCoalescing()

	public void setExpandEntityReferences(boolean value) {
		expandEntityRef = value;
	} // setExpandEntityReferences()

	public void setIgnoringComments(boolean value) {
		ignoreComments = value;
	} // setIgnoringComments()

	public void setIgnoringElementContentWhitespace(boolean value) {
		whitespace = value;
	} // setIgnoringElementContentWhitespace()

	public void setNamespaceAware(boolean value) {
		namespaceAware = value;
	} // setNamespaceAware()

	public void setValidating(boolean value) {
		validating = value;
	} // setValidating()
	
	//
	// INTERNALS
	//
	private static String
	findFactory (String property, String defaultValue)
	{
		// Variables
		String		factory;
		String		javaHome;
		File		file;
		Properties	props;
		ClassLoader	loader;
		BufferedReader	br;
		InputStream	stream;

		// Check System Property
		try {
		    factory = System.getProperty(property);
		} catch (SecurityException e) {
		    factory = null;
		}

		// Check in $JAVA_HOME/lib/jaxp.properties
		try {
			if (factory == null) {
				javaHome = System.getProperty("java.home");
				file = new File(new File(javaHome, "lib"),
					"jaxp.properties");
				if (file.exists() == true) {
					props = new Properties();
					props.load(new FileInputStream(file));
					factory = props.getProperty(property);
				} // if
			} // if
		} catch (Exception e1) {
		} // try

		// Check Services API
		try {
			if (factory == null) {

				// Get Class Loader for Accessing resources
				loader = DocumentBuilderFactory.class
						.getClassLoader();
				if (loader == null) {
					loader = ClassLoader
						.getSystemClassLoader();
				} // if
			
				// Get Resource Stream
				stream = loader.getResourceAsStream(
					"META-INF/services/" + property);

				// Stream Found, Read Entry
				if (stream != null) {
					br = new BufferedReader(
					    new InputStreamReader(stream));
					factory = br.readLine();
				} // if

			} // if
		} catch (Exception e2) {
		} // try

		// Otherwise, Use default
		if (factory == null) {
			factory = defaultValue;
		} // if

		// Return Factory
		return factory;

	} // findFactory()
}
