/********************************************************************
 * Copyright (c) Open Java Extensions, Andrew Selkirk  LGPL License *
 ********************************************************************/

package javax.xml.parsers;

/**
 * FactoryConfigurationError
 * @author	Andrew Selkirk
 * @version	1.0
 */
public class FactoryConfigurationError extends Error {

	//-------------------------------------------------------------
	// Variables --------------------------------------------------
	//-------------------------------------------------------------

	private Exception	exception	= null;


	//-------------------------------------------------------------
	// Initialization ---------------------------------------------
	//-------------------------------------------------------------
	public FactoryConfigurationError() {
		super();
	} // FactoryConfigurationError()

	public FactoryConfigurationError(String msg) {
		super(msg);
	} // FactoryConfigurationError()

	public FactoryConfigurationError(Exception ex) {
		super();
		exception = ex;
	} // FactoryConfigurationError()

	public FactoryConfigurationError(Exception ex, String msg) {
		super(msg);
		exception = ex;
	} // FactoryConfigurationError()


	//-------------------------------------------------------------
	// Methods ----------------------------------------------------
	//-------------------------------------------------------------

	public String getMessage() {
		return super.getMessage();
	} // getMessage()

	public Exception getException() {
		return exception;
	} // getException()


} // FactoryConfigurationError


