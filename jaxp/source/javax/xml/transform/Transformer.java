/********************************************************************
 * Copyright (c) Open Java Extensions, Andrew Selkirk  LGPL License *
 ********************************************************************/

package javax.xml.transform;

// Imports
import java.util.Properties;

/**
 * Transformer
 * @author	Andrew Selkirk
 * @version	1.0
 */
public abstract class Transformer {

	//-------------------------------------------------------------
	// Initialization ---------------------------------------------
	//-------------------------------------------------------------

	protected Transformer() {
	} // Transformer()


	//-------------------------------------------------------------
	// Methods ----------------------------------------------------
	//-------------------------------------------------------------

	public abstract void transform(Source source, Result result) 
		throws TransformerException;

	public abstract void setParameter(String name, Object value);

	public abstract Object getParameter(String name);

	public abstract void clearParameters();

	public abstract void setURIResolver(URIResolver resolver);

	public abstract URIResolver getURIResolver();

	public abstract void setOutputProperties(Properties outputformat) 
		throws IllegalArgumentException;

	public abstract Properties getOutputProperties();

	public abstract void setOutputProperty(String name, String value) 
		throws IllegalArgumentException;

	public abstract String getOutputProperty(String name) 
		throws IllegalArgumentException;

	public abstract void setErrorListener(ErrorListener listener) 
		throws IllegalArgumentException;

	public abstract ErrorListener getErrorListener();


} // Transformer

