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

package javax.xml.transform;

// Imports
import java.io.IOException;
import java.io.InputStream;
import java.io.FileInputStream;
import java.io.File;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Properties;

/**
 * TransformerFactory
 * @author	Andrew Selkirk
 * @version	1.0
 */
public abstract class TransformerFactory {

	//-------------------------------------------------------------
	// Variables --------------------------------------------------
	//-------------------------------------------------------------

	private static final String	defaultPropName =
		"javax.xml.transform.TransformerFactory";

	private static final boolean	debug		= false;
	private static String		foundFactory	= null;


	//-------------------------------------------------------------
	// Initialization ---------------------------------------------
	//-------------------------------------------------------------

	protected TransformerFactory() {
	} // TransformerFactory()


	//-------------------------------------------------------------
	// Methods ----------------------------------------------------
	//-------------------------------------------------------------

	public static TransformerFactory newInstance() 
			throws TFactoryConfigurationError {

		// Variables
		Class			classObject;
		TransformerFactory	factory;

		// Locate Factory
		foundFactory = findFactory(defaultPropName, 
			"org.apache.xalan.processor.TransformerFactoryImpl");

		try {

			// Get Class
			classObject = Class.forName(foundFactory);

			// Instantiate Class
			factory = (TransformerFactory) classObject.newInstance();

			// Return Instance
			return factory;

		} catch (Exception e) {
			throw new TFactoryConfigurationError(e);
		} // try		

	} // newInstance()

	public abstract Transformer newTransformer(Source source) 
		throws TransformerConfigurationException;

	public abstract Transformer newTransformer() 
		throws TransformerConfigurationException;

	public abstract Templates newTemplates(Source source) 
		throws TransformerConfigurationException;

	public abstract Source getAssociatedStylesheet(Source source, 
		String media, String title, String charset) 
		throws TransformerConfigurationException;

	public abstract void setURIResolver(URIResolver resolver);

	public abstract URIResolver getURIResolver();

	public abstract boolean getFeature(String name);

	public abstract void setAttribute(String name, Object value)
		throws IllegalArgumentException;

	public abstract Object getAttribute(String name) 
		throws IllegalArgumentException;

	public abstract void setErrorListener(ErrorListener listener) 
		throws IllegalArgumentException;

	public abstract ErrorListener getErrorListener();

	private static String findFactory(String property, String defaultValue) {

		// Variables
		String		factory;
		String		javaHome;
		File		file;
		Properties	props;
		ClassLoader	loader;
		BufferedReader	br;
		InputStream	stream;

		// Check System Property
		factory = System.getProperty(property);

		// Check JAVA_HOME
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
				loader = TransformerFactory.class.getClassLoader();
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


} // TransformerFactory


