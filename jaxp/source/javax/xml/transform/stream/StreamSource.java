/********************************************************************
 * Copyright (c) Open Java Extensions, Andrew Selkirk  LGPL License *
 ********************************************************************/

package javax.xml.transform.stream;

// Imports
import java.io.InputStream;
import java.io.Reader;
import java.io.File;
import javax.xml.transform.Source;

/**
 * Stream Source
 * @author	Andrew Selkirk
 * @version	1.0
 */
public class StreamSource implements Source {

	//-------------------------------------------------------------
	// Variables --------------------------------------------------
	//-------------------------------------------------------------

	public static final String FEATURE =
		"http://javax.xml.transform.stream.StreamSource/feature";

	private String		publicId	= null;
	private String		systemId	= null;
	private InputStream	inputStream	= null;
	private Reader		reader		= null;


	//-------------------------------------------------------------
	// Initialization ---------------------------------------------
	//-------------------------------------------------------------

	public StreamSource() {
	} // StreamSource()

	public StreamSource(InputStream stream) {
		this.inputStream = stream;
	} // StreamSource()

	public StreamSource(InputStream stream, String systemID) {
		this.inputStream = stream;
		this.systemId = systemID;
	} // StreamSource()

	public StreamSource(Reader reader) {
		this.reader = reader;
	} // StreamSource()

	public StreamSource(Reader reader, String systemID) {
		this.reader = reader;
		this.systemId = systemID;
	} // StreamSource()

	public StreamSource(String systemID) {
		this.systemId = systemID;
	} // StreamSource()

	public StreamSource(File file) {
		this(file.getName());
	} // StreamSource()


	//-------------------------------------------------------------
	// Methods ----------------------------------------------------
	//-------------------------------------------------------------

	public InputStream getInputStream() {
		return inputStream;
	} // getInputStream()

	public void setInputStream(InputStream stream) {
		this.inputStream = stream;
	} // setInputStream()

	public void setReader(Reader reader) {
		this.reader = reader;
	} // setReader()

	public Reader getReader() {
		return reader;
	} // getReader()

	public void setPublicId(String publicID) {
		this.publicId = publicID;
	} // setPublicId()

	public String getPublicId() {
		return publicId;
	} // getPublicId()

	public void setSystemId(String systemID) {
		this.systemId = systemID;
	} // setSystemId()

	public void setSystemId(File file) {
		this.systemId = file.getName();
	} // setSystemId()

	public String getSystemId() {
		return systemId;
	} // getSystemId()


} // StreamSource

