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
 * DocumentBuilderFactory
 * @author	Andrew Selkirk
 * @version	1.0
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

	public static DocumentBuilderFactory newInstance() {

		// Variables
		Class			classObject;
		DocumentBuilderFactory	factory;

		// Locate Factory
		foundFactory = findFactory(defaultPropName, 
			"org.apache.crimson.jaxp.DocumentBuilderFactoryImpl");

		try {

			// Get Class
			classObject = Class.forName(foundFactory);

			// Instantiate Class
			factory = (DocumentBuilderFactory) classObject.newInstance();

			// Return Instance
			return factory;

		} catch (Exception e) {
			throw new FactoryConfigurationError(e);
		} // try		

	} // newInstance()

	public abstract DocumentBuilder newDocumentBuilder()
		throws ParserConfigurationException;

	public void setNamespaceAware(boolean value) {
		namespaceAware = value;
	} // setNamespaceAware()

	public void setValidating(boolean value) {
		validating = value;
	} // setValidating()
	
	public void setIgnoringElementContentWhitespace(boolean value) {
		whitespace = value;
	} // setIgnoringElementContentWhitespace()

	public void setExpandEntityReferences(boolean value) {
		expandEntityRef = value;
	} // setExpandEntityReferences()

	public void setIgnoringComments(boolean value) {
		ignoreComments = value;
	} // setIgnoringComments()

	public void setCoalescing(boolean value) {
		coalescing = value;
	} // setCoalescing()

	public boolean isNamespaceAware() {
		return namespaceAware;
	} // isNamespaceAware()

	public boolean isValidating() {
		return validating;
	} // isValidating()

	public boolean isIgnoringElementContentWhitespace() {
		return whitespace;
	} // isIgnoringElementContentWhitespace()

	public boolean isExpandEntityReferences() {
		return expandEntityRef;
	} // isExpandEntityReferences()

	public boolean isIgnoringComments() {
		return ignoreComments;
	} // isIgnoringComments()

	public boolean isCoalescing() {
		return coalescing;
	} // isCoalescing()

	public abstract void setAttribute(String value1, Object value2) 
		throws IllegalArgumentException;

	public abstract Object getAttribute(String value) 
		throws IllegalArgumentException;

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
				loader = DocumentBuilderFactory.class.getClassLoader();
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


} // DocumentBuilderFactory


