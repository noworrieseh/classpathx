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
 * SAXParserFactory is used to bootstrap JAXP wrappers for
 * SAX parsers.
 *
 * <para> Note that the JAXP 1.1 spec does not specify how
 * the <em>isValidating()</em> or <em>isNamespaceAware()</em>
 * flags relate to the SAX2 feature flags controlling those
 * same features.
 *
 * @author	Andrew Selkirk
 * @version	1.0
 */

// some javadoc added by David Brownell

public abstract class SAXParserFactory {

	//-------------------------------------------------------------
	// Variables --------------------------------------------------
	//-------------------------------------------------------------

	private static final	String defaultPropName	=
		"javax.xml.parsers.SAXParserFactory";

	private static		String foundFactory	= null;
	private static final	boolean debug		= false;

	private 		boolean validating	= false;
	private 		boolean namespaceAware	= false;


	//-------------------------------------------------------------
	// Initialization ---------------------------------------------
	//-------------------------------------------------------------

	protected SAXParserFactory() {
	} // SAXParserFactory()


	//-------------------------------------------------------------
	// Methods ----------------------------------------------------
	//-------------------------------------------------------------

	public static SAXParserFactory newInstance() {

		// Variables
		Class			classObject;
		SAXParserFactory	factory;

		// Locate Factory
		foundFactory = findFactory(defaultPropName,
			"gnu.xml.aelfred2.JAXPFactory");

		try {

			// Get Class
			classObject = Class.forName(foundFactory);

			// Instantiate Class
			factory = (SAXParserFactory) classObject.newInstance();

			// Return Instance
			return factory;

		} catch (Exception e) {
			throw new FactoryConfigurationError(e);
		} // try		

	} // newInstance()

	/**
	 * Returns a new instance of a SAXParser using the platform
	 * default implementation and the currently specified factory
	 * feature flag settings.
	 *
	 * @exception ParserConfigurationException
	 *	when the parameter combination is not supported
	 * @exception SAXNotRecognizedException
	 *	if one of the specified SAX2 feature flags is not recognized
	 * @exception SAXNotSupportedException
	 *	if one of the specified SAX2 feature flags values can
	 *	not be set, perhaps because of sequencing requirements
	 *	(which could be met by using SAX2 directly)
	 */
	public abstract SAXParser newSAXParser()
		throws ParserConfigurationException, SAXException;

	public void setNamespaceAware(boolean value) {
		namespaceAware = value;
	} // setNamespaceAware()

	public void setValidating(boolean value) {
		validating = value;
	} // setValidating()
	
	public boolean isNamespaceAware() {
		return namespaceAware;
	} // isNamespaceAware()

	public boolean isValidating() {
		return validating;
	} // isValidating()

	/**
	 * Establishes a factory parameter corresponding to the
	 * specified feature flag.
	 *
	 * @param name identifies the feature flag
	 * @param value specifies the desired flag value
	 *
	 * @exception SAXNotRecognizedException
	 *	if the specified SAX2 feature flag is not recognized
	 * @exception SAXNotSupportedException
	 *	if the specified SAX2 feature flag values can not be set,
	 *	perhaps because of sequencing requirements (which could
	 *	be met by using SAX2 directly)
	 */
	public abstract void setFeature (String name, boolean value) 
		throws	ParserConfigurationException, 
			SAXNotRecognizedException, 
			SAXNotSupportedException;

	/**
	 * Retrieves a current factory feature flag setting.
	 *
	 * @param name identifies the feature flag
	 *
	 * @exception SAXNotRecognizedException
	 *	if the specified SAX2 feature flag is not recognized
	 * @exception SAXNotSupportedException
	 *	if the specified SAX2 feature flag values can not be
	 *	accessed before parsing begins.
	 */
	public abstract boolean getFeature (String name) 
		throws	ParserConfigurationException, 
			SAXNotRecognizedException, 
			SAXNotSupportedException;

	private static String
	findFactory(String property, String defaultValue)
	{
		// Variables
		String		factory;
		String		javaHome;
		File		file;
		Properties	props;
		ClassLoader	loader;
		BufferedReader	br;
		InputStream	stream;

		// Four ordered steps, as listed in the
		// JAXP spec for the "pluggability".

		// Check System Property
		// ... normally fails in applet environments
		try {
			factory = System.getProperty(property);
		} catch (SecurityException se) {
			factory = null;
		}

		// Check $JAVA_HOME/lib/jaxp.properties
		try {
			if (factory == null) {
				javaHome = System.getProperty("java.home");
				file = new File(new File(javaHome, "lib"), "jaxp.properties");
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
				loader = SAXParserFactory.class.getClassLoader();
				if (loader == null) {
					loader = ClassLoader.getSystemClassLoader();
				} // if
			
				// Get Resource Stream
				stream = loader.getResourceAsStream(
					"META-INF/services/" + property);

				// Stream Found, Read Entry
				if (stream != null) {
					br = new BufferedReader(new InputStreamReader(stream));
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


} // SAXParserFactory

