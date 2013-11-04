/* MimePart.java */

/* Liscense goes here. */

package javax.mail.internet;

import javax.mail.BodyPart;
import javax.mail.MessagingException;
import javax.activation.DataHandler;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;
import java.util.Enumeration;

/**
 * 
 *
 * @see javax.mail.MimePart
 * @see javax.mail.BodyPart
 * @author Joey Lesh
 */
public class MimeBodyPart extends BodyPart implements MimePart {
	// +--------+----------------------------------------------
	// | Fields |
	// +--------+
	
	/* The content of the part. */
	protected  byte[] content;
	
	/* The <code>DataHandler</code> that can be used to access data 
	 * in <code>this</code>. */
	protected DataHandler dh;
	
	/* The headers needed to correctly send <code>this</code>.  */
	protected InternetHeaders headers;
	

	// +--------------+----------------------------------------------
	// | Constructors |
	// +--------------+

	/**
	 * Constructs and empty <code>MimeBodyPart</code>.
	 */
	public MimeBodyPart() {
		// Nothing.
	}//MimeBodyPart

	/**
	 * Constructs a <code>MimeBodyPart</code> by parsing the 
	 * <code>InputStream</code>.  
	 *
	 * @param is The <code>InputStream</code> to parse the data from.
	 */
	public MimeBodyPart(InputStream is) {
		// TO DO: waiting on parsing.
	}// MimeBodyPart(InputStream)

	/**
	 * Constructs a <code>MimeBodyPart</code> using the <code>headers</code>
	 * parameters for the headers and content. They are assumed to be
	 * correctly formatted.
	 *
	 * @param headers The <code>InternetHeaders</code> object that 
	 *    contains the headers for the data in <code>content</code>.
	 * @param content A <code>byte</code> array containing the data
	 *    for this message.
	 */
	public MimeBodyPart(InternetHeaders headers, byte[] content) {
		this.headers = headers;
		this.content= content;
		// How should I deal with the DataHandler?
		// this.dh = 
	}// MimeBodyPart(InternetHeaders, byte[])


	// +----------+----------------------------------------------
	// | Methods  |
	// +----------+
	
	/**
	 * Gets the size, in bytes, of the content of this body part.
	 *
	 * @return The number of bytes in the body of this part.
	 */
	public int getSize() throws MessagingException {
		return content.length;
	}// getSize()

	/**
	 * Gets the line count for the part. 
	 *
	 * SPEC NOTE: is very unclear and loose about what this may 
	 * mean. Implementation returns -1.
	 */
	public int getLineCount() throws MessagingException {
		/*
		  int count = 0;
		  for (int i = 0 ; i< dh.length ; i++)
		  if (content[i] == '\n') 
		  count++;
		*/
		return -1;
	}// getLineCount()

	/**
	 * Get the "Content-Type" header for this part.
	 */
	public String getContentType() throws MessagingException {
		return getHeader("Content-Type")
	}// getContentType()
	
	/**
	 * Returns the <code>true</code> if this <code>MimeBodyPart</code> 
	 * is the same MIME type as the parameter specifies. 
	 *
	 * @param mimeType The name of the MIME type to compare to.
	 * @return A <code>boolean</code> that is <code>true</code>
	 *         if the specified type mathces <code>this</code>.
	 */
	public boolean isMimeType(String mimeType) {
		return false;
	}// isMimeType(String)

	/**
	 *
	 */
	public String getDisposition() {
		// use getHeader("Content-Disposition") if null
		return null;
	}// getDisposition()

	/**
	 *
	 */
	public void setDisposition(String disposition) {
		// TO DO 
	}// setDisposition(String)

	/**
	 *
	 */
	public String getEncoding() throws MessagingException {
		// use getHeader("Content-Transfer-Encoding") if null
		return null;
	}// getEncoding()

	/**
	 *
	 */
	public String getContentID() throws MessagingException {
		// To DO
		// use getHeader("Content-ID") if null
		return null;
	}// getContentID()
	
	/**
	 *
	 */
	public String getContentMD5() throws MessagingException {
		// use getHeader("Content-MD5") if null
		return null;
	}// getContentMD5()

	/**
	 *
	 */
	public void setContentMD5(String md5) throws MessagingException {
		//
	}// setContentMD5(String)
	
	/**
	 *
	 */
	public String[] getContentLanguage() throws MessagingException {
		// use getHeader("Content-Language") if null
		return null;
	}// getContentLanguage()
	
	/**
	 *
	 */
	public void setContentLanguage(String[] languages) throws MessagingException {
		// TO DO 
	}// setContentLanguage(String[])

	/**
	 *
	 */
	public String getDescription() throws MessagingException {
		// TO DO
		return "";
	}// getDescription()
	
	/**
	 *
	 */
	public void setDescription(String description) throws MessagingException {
		// TO DO
	}// setDescription(String)

	/**
	 *
	 */
	public void setDescription(String description, String charset) throws MessagingException {
		// TO DO
	}// setDescription(String, String)

	/**
	 *
	 */
	public String getFileName() throws MessagingException {
		// TO DO
		return null;
	}// getFileName()

	/**
	 *
	 */
	public void setFileName(String filename) throws MessagingException {
		// TO DO
	}// setFileName(String)

	/**
	 *
	 */
	public InputStream getInputStream() throws IOException, MessagingException {
		// TO DO
		return null;
	}// getInputStream()

	/**
	 *
	 */
	public InputStream getContentStream()  throws MessagingException {
		// TO DO 
		return null;
	}// getContentStream()

	/**
	 *
	 */
	public DataHandler getDataHandler()  throws MessagingException {
		return this.dh;
	}// getDataHandler()

	/**
	 *
	 */
	public Object getContent() throws MessagingException {
		// TO DO
		return null;
	}// getContent()

	/**
	 *
	 */
	public void setDataHandler(DataHandler dh) throws MessagingException {
		this.dh = dh;
	}// setDataHandler(DataHandler)

	/**
	 *
	 */
	public void setContent(Object o, String type)throws MessagingException {
		// TO DO 
	}// setContent(Object, String)

	/**
	 *
	 */
	public void setText(String text) throws MessagingException {
		// TO DO
	}// setText(String)
	
	/**
	 *
	 */
	public void setText(String text, String charset) throws MessagingException {
		// TO DO
	}// setText(String, String)
	
	/**
	 *
	 */
	public void setContent(Multipart mp) throws MessagingException {
		// TO DO 
	}// setContent(Multipart)

	/**
	 *
	 */
	public void writeTo(OutputStream os) throws MessagingException {
		// TO DO 
	}// writeTo(OutputStream)

	/**
	 *
	 */
	public String[] getHeader(String name) throws MessagingException {
		// TO DO
		return null;
	}// getHeader(String)

	/**
	 *
	 */
	public String getHeader(String name, String delimiter)throws MessagingException {
		// TO DO
		return null;
	}// getHeader (String, String)

	/**
	 *
	 */
	public void setHeader(String name, String value) throws MessagingException {
		//TO DO 
	}// setHeader(String, String)

	/**
	 *
	 */
	public void addHeader(String name, String value) throws MessagingException {
		// TO DO 
	}// addHeader(String, String)

	/**
	 *
	 */
	public void removeHeader(String name) throws MessagingException {
		// TO DO
	}// removeHeader(String)
	
	/**
	 *
	 */
	public Enumeration getAllHeaders() throws MessagingException {
		// TO DO
		return null;
	}// getAllHeaders()

	/**
	 *
	 */
	public Enumeration getMatchingHeaders(String[] names) throws MessagingException {
		//TO DO 
		return null;
	}// getMatchingHeaders(String[])
	
	/**
	 *
	 */
	public Enumeration getNonMatchingHeaders(String[] names) throws MessagingException {
		//TO DO 
		return null;
	}// getNonMatchingHeaders(String[])
	
	/**
	 *
	 */
	public void addHeaderLIne(String line) throws MessagingException {
		//To DO 
	}// addHEaderLine(String)

	/**
	 *
	 */
	public Enumeration getAllheaderLines() throws MessagingException {
		// to do 
		return null;
	}// getAllHeaderLines()

	/**
	 *
	 */
	public Enumeration getMatchingHeaderLines()  throws MessagingException{
		// to do
		return null;
	}// getMatchingHeaderLines()
 
	/**
	 *
	 */
	public Enumeration getNonMatchingHeaderLines() throws MessagingException {
		// to do
		return null;
	}// getNonMatchingHeaderLines()

	/**
	 *
	 */
	protected void updateHeaders()  throws MessagingException{
		//TO DO
	}// updateHeaders()
	
}// MimeBodyPart
