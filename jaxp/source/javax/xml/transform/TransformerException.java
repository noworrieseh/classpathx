/********************************************************************
 * Copyright (c) Open Java Extensions, Andrew Selkirk  LGPL License *
 ********************************************************************/

package javax.xml.transform;

// Imports
import java.io.*;

/**
 * TransformerException
 * @author	Andrew Selkirk
 * @version	1.0
 */
public class TransformerException extends Exception {

	//-------------------------------------------------------------
	// Variables --------------------------------------------------
	//-------------------------------------------------------------

	private	SourceLocator	locator			= null;
	private Exception	containedException	= null;


	//-------------------------------------------------------------
	// Initialization ---------------------------------------------
	//-------------------------------------------------------------

	public TransformerException(String msg) {
		super(msg);
	} // TransformerException()

	public TransformerException(Exception ex) {
		super();
		containedException = ex;
	} // TransformerException()

	public TransformerException(String msg, Exception ex) {
		super(msg);
		containedException = ex;
	} // TransformerException()

	public TransformerException(String msg, SourceLocator locator) {
		super(msg);
		this.locator = locator;
	} // TransformerException()

	public TransformerException(String msg, SourceLocator locator, 
				Exception ex) {
		super(msg);
		this.locator = locator;
		containedException = ex;
	} // TransformerException()


	//-------------------------------------------------------------
	// Methods ----------------------------------------------------
	//-------------------------------------------------------------

	public Exception getException() {
		return containedException;
	} // getException()

	public SourceLocator getLocator() {
		return locator;
	} // getLocator()

	public void printStackTrace() {
		printStackTrace(System.out);
	} // printStackTrace()

	public void printStackTrace(PrintStream stream) {
		printStackTrace(new PrintWriter(
			new OutputStreamWriter(stream)));
	} // printStackTrace()

	public void printStackTrace(PrintWriter writer) {
		if (containedException != null) {
			containedException.printStackTrace(writer);
		} // if
		super.printStackTrace(writer);
	} // printStackTrace()

	public Throwable getCause() {
		return containedException;
	} // getCause()

	public synchronized Throwable initCause(Throwable cause) {
		return null; // TODO
	} // initCause()

	public String getMessageAndLocation() {
		return null; // TODO
	} // getMessageAndLocation()

	public String getLocationAsString() {
		return null; // TODO
	} // getLocationAsString()


} // TranformerException


