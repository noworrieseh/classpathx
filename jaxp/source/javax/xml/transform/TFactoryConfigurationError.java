/********************************************************************
 * Copyright (c) Open Java Extensions, Andrew Selkirk  LGPL License *
 ********************************************************************/

package javax.xml.transform;

/**
 * TFactoryConfigurationError
 * @author	Andrew Selkirk
 * @version	1.0
 */
public class TFactoryConfigurationError extends Error {

	//-------------------------------------------------------------
	// Variables --------------------------------------------------
	//-------------------------------------------------------------

	private Exception	exception	= null;


	//-------------------------------------------------------------
	// Initialization ---------------------------------------------
	//-------------------------------------------------------------

	public TFactoryConfigurationError() {
		super();
	} // FactoryConfigurationError()

	public TFactoryConfigurationError(String msg) {
		super(msg);
	} // TFactoryConfigurationError()

	public TFactoryConfigurationError(Exception ex) {
		super();
		exception = ex;
	} // TFactoryConfigurationError()

	public TFactoryConfigurationError(Exception ex, String msg) {
		super(msg);
		exception = ex;
	} // TFactoryConfigurationError()


	//-------------------------------------------------------------
	// Methods ----------------------------------------------------
	//-------------------------------------------------------------

	public String getMessage() {
		if (super.getMessage() == null &&
			exception != null) {
			return exception.getMessage();
		}
		return super.getMessage();
	} // getMessage()

	public Exception getException() {
		return exception;
	} // getException()


} // TFactoryConfigurationError

