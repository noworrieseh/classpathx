/********************************************************************
 * Copyright (c) Open Java Extensions, Andrew Selkirk  LGPL License *
 ********************************************************************/

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
 * SAXParserFactory
 * @author	Andrew Selkirk
 * @version	1.0
 */
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
			"org.apache.crimson.jaxp.SAXParserFactoryImpl");

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

	public abstract void setFeature(String value1, boolean value2) 
		throws	ParserConfigurationException, 
			SAXNotRecognizedException, 
			SAXNotSupportedException;

	public abstract boolean getFeature(String value) 
		throws	ParserConfigurationException, 
			SAXNotRecognizedException, 
			SAXNotSupportedException;

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



