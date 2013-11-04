/********************************************************************
 * Copyright (c) Open Java Extensions, Andrew Selkirk  LGPL License *
 ********************************************************************/

package javax.mail;

// Imports
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import javax.activation.DataHandler;

/**
 * Body Part.
 */
public abstract class BodyPart implements Part {

	//-------------------------------------------------------------
	// Variables --------------------------------------------------
	//-------------------------------------------------------------

	/**
	 * Get parent multipart.
	 */
	protected	Multipart	parent	= null;


	//-------------------------------------------------------------
	// Initialization ---------------------------------------------
	//-------------------------------------------------------------

	/**
	 * Create new body part.
	 */
	public BodyPart() {
	} // BodyPart()


	//-------------------------------------------------------------
	// Public Accessor Methods ------------------------------------
	//-------------------------------------------------------------

	/**
	 * Get parent multipart.
	 * @returns Parent multipart
	 */
	public Multipart getParent() {
		return parent;
	} // getParent()

	/**
	 * Get content object.
	 * @returns Connect
	 */
	public abstract Object getContent();

	/**
	 * Get input stream of part.
	 * @returns Input stream
	 * @throws IOException IO exception occurred
	 */
	public abstract InputStream getInputStream() throws IOException;

	/**
	 * Get size of part.
	 * @returns Size of part
	 */
	public abstract int getSize();

	/**
	 * Write body part to output stream.
	 * @param stream Output stream to write to
	 * @throws IOException IO exception occurred
	 * @throws MessagingException Messaging exception occurred
	 */
	public abstract void writeTo(OutputStream stream)
			throws IOException, MessagingException;

	/**
	 * Get content type of body part.
	 * @returns Content type
	 * @throws MessagingException Messaging exception occurred
	 */
	public abstract String getContentType()
		throws MessagingException;

	/**
	 * Add header.
	 * @param headerName Header name
	 * @param headerValue Header value
	 * @throws MessagingException Messaging exception occurred
	 */
	public abstract void addHeader(String headerName, String headerValue)
			throws MessagingException;

	/**
	 * Get all headers.
	 * @returns Enumeration of headers
	 * @throws MessagingException Messaging exception occurred
	 */
	public abstract Enumeration getAllHeaders()
		throws MessagingException;

	/**
	 * Get data handler.
	 * @returns Data handler
	 * @throws MessagingException Messaging exception occurred
	 */
	public abstract DataHandler getDataHandler()
		throws MessagingException;

	/**
	 * Get part description.
	 * @returns Description
	 * @throws MessagingException Messaging exception occurred
	 */
	public abstract String getDescription()
		throws MessagingException;

	/**
	 * Get part disposition.
	 * @returns Disposition
	 * @throws MessagingException Messaging exception occurred
	 */
	public abstract String getDisposition()
		throws MessagingException;

	/**
	 * Get file name of part.
	 * @returns File name
	 * @throws MessagingException Messaging exception occurred
	 */
	public abstract String getFileName()
		throws MessagingException;

	/**
	 * Get header of part.
	 * @param headerName Header name
	 * @returns List of values
	 * @throws MessagingException Messaging exception occurred
	 */
	public abstract String[] getHeader(String headerName)
		throws MessagingException;

	/**
	 * Get line count of part.
	 * @returns Line count
	 * @throws MessagingException Messaging exception occurred
	 */
	public abstract int getLineCount()
		throws MessagingException;

	/**
	 * Get matching headers.
	 * @param headerNames Names of headers to match
	 * @returns Enumeration of headers
	 * @throws MessagingException Messaging exception occurred
	 */
	public abstract Enumeration getMatchingHeaders(String[] headerNames)
		throws MessagingException;

	/**
	 * Get non-matching headers.
	 * @param headerNames Names of headers not to match
	 * @returns Enumeration of headers
	 * @throws MessagingException Messaging exception occurred
	 */
	public abstract Enumeration getNonMatchingHeaders(String[] headerNames)
		throws MessagingException;

	/**
	 * Check if part is a particular MIME type.
	 * @param mimeType MIME Type to check
	 * @returns true if matching MIME type, false otherwise
	 * @throws MessagingException Messaging exception occurred
	 */
	public abstract boolean isMimeType(String mimeType)
		throws MessagingException;

	/**
	 * Remove header from part.
	 * @param headerName Name of header
	 * @throws MessagingException Messaging exception occurred
	 */
	public abstract void removeHeader(String headerName)
		throws MessagingException;

	/**
	 * Set the content of body part.
	 * @param object Object to set
	 * @param mimeType MIME type of object
	 * @throws MessagingException Messaging exception occurred
	 */
	public abstract void setContent(Object object, String mimeType)
		throws MessagingException;

	/**
	 * Set the content of body part.
	 * @param content Multipart content
	 * @throws MessagingException Messaging exception occurred
	 */
	public abstract void setContent(Multipart content)
		throws MessagingException;

	/**
	 * Set the data handler.
	 * @param handler Data handler
	 * @throws MessagingException Messaging exception occurred
	 */
	public abstract void setDataHandler(DataHandler handler)
		throws MessagingException;

	/**
	 * Set the description of the body part.
	 * @param desc Description
	 * @throws MessagingException Messaging exception occurred
	 */
	public abstract void setDescription(String desc)
		throws MessagingException;

	/**
	 * Set the disposition of the body part.
	 * @param desc Disposition
	 * @throws MessagingException Messaging exception occurred
	 */
	public abstract void setDisposition(String disposition)
		throws MessagingException;

	/**
	 * Set the file name of body part.
	 * @param filename File name of part
	 * @throws MessagingException Messaging exception occurred
	 */
	public abstract void setFileName(String filename)
		throws MessagingException;

	/**
	 * Set the value of this header name.
	 * @param headerName Name of header
	 * @param headerValue Value of header
	 * @throws MessagingException Messaging exception occurred
	 */
	public abstract void setHeader(String headerName, String headerValue)
		throws MessagingException;

	/**
	 * Set the parent of the body part.
	 * @param parentPart Parent part
	 */
	void setParent(Multipart parentPart) {
		parent = parentPart;
	} // setParent()

	/**
	 * A convienence method for setting the body part as
	 * text with the MIME type text/plain.
	 * @param text Text of part
	 * @throws MessagingException Messaging exception occurred
	 */
	public abstract void setText(String text)
		throws MessagingException;


} // BodyPart
