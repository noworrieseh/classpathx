/********************************************************************
 * Copyright (c) Open Java Extensions, Andrew Selkirk  LGPL License *
 ********************************************************************/

package javax.xml.transform.stream;

// Imports
import java.io.OutputStream;
import java.io.Writer;
import java.io.File;
import javax.xml.transform.Result;

/**
 * Stream Result
 * @author	Andrew Selkirk
 * @version	1.0
 */
public class StreamResult implements Result {

	//-------------------------------------------------------------
	// Variables --------------------------------------------------
	//-------------------------------------------------------------

	public static final String FEATURE =
		"http://javax.xml.transform.stream.StreamResult/feature";

	private String		systemId	= null;
	private OutputStream	outputStream	= null;
	private Writer		writer		= null;


	//-------------------------------------------------------------
	// Initialization ---------------------------------------------
	//-------------------------------------------------------------

	public StreamResult() {
	} // StreamResult()

	public StreamResult(OutputStream stream) {
		this.outputStream = stream;
	} // StreamResult()

	public StreamResult(Writer writer) {
		this.writer = writer;
	} // StreamResult()

	public StreamResult(String systemID) {
		this.systemId = systemID;
	} // StreamResult()

	public StreamResult(File file) {
		this(file.getName());
	} // StreamResult()


	//-------------------------------------------------------------
	// Methods ----------------------------------------------------
	//-------------------------------------------------------------

	public OutputStream getOutputStream() {
		return outputStream;
	} // getOutputStream()

	public void setOutputStream(OutputStream stream) {
		this.outputStream = stream;
	} // setOutputStream()

	public void setWriter(Writer writer) {
		this.writer = writer;
	} // setWriter()

	public Writer getWriter() {
		return writer;
	} // getWriter()

	public void setSystemId(String systemID) {
		this.systemId = systemID;
	} // setSystemId()

	public void setSystemId(File file) {
		this.systemId = file.getName();
	} // setSystemId()

	public String getSystemId() {
		return systemId;
	} // getSystemId()


} // StreamResult


