/********************************************************************
 * Copyright (c) Open Java Extensions, Andrew Selkirk  LGPL License *
 ********************************************************************/

package oje.mail.util;

// Imports
import java.io.*;

/**
 * Line input stream
 * @author	Andrew Selkirk
 * @version	1.0
 */
public class LineInputStream extends FilterInputStream {

	//-------------------------------------------------------------
	// Variables --------------------------------------------------
	//-------------------------------------------------------------

	private char[] lineBuffer	= null; // TODO


	//-------------------------------------------------------------
	// Initialization ---------------------------------------------
	//-------------------------------------------------------------

	public LineInputStream(InputStream stream) {
		super(stream);
	} // LineInputStream()


	//-------------------------------------------------------------
	// Methods ----------------------------------------------------
	//-------------------------------------------------------------

	public String readLine() throws IOException {
		return null; // TODO
	} // readLine()

} // LineInputStream
