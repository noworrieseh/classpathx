/********************************************************************
 * Copyright (c) Open Java Extensions, Andrew Selkirk  LGPL License *
 ********************************************************************/

package javax.mail;

// Imports
import java.io.*;
import java.util.*;
import javax.activation.DataHandler;

/**
 * Part
 */
public interface Part {

	//-------------------------------------------------------------
	// Variables --------------------------------------------------
	//-------------------------------------------------------------

	public static final	String	ATTACHMENT	= "attachement";
	public static final	String	INLINE		= "inline";


	//-------------------------------------------------------------
	// Interface: Part --------------------------------------------
	//-------------------------------------------------------------

	public abstract Object getContent()
		throws IOException, MessagingException;

	public abstract InputStream getInputStream()
		throws IOException, MessagingException;

	public abstract int getSize()
		throws MessagingException;

	public abstract void writeTo(OutputStream stream)
		throws IOException, MessagingException;

	public abstract String getContentType()
		throws MessagingException;

	public abstract void addHeader(String headerName, String headerValue)
		throws MessagingException;

	public abstract Enumeration getAllHeaders()
		throws MessagingException;

	public abstract DataHandler getDataHandler()
		throws MessagingException;

	public abstract String getDescription()
		throws MessagingException;

	public abstract String getDisposition()
		throws MessagingException;

	public abstract String getFileName()
		throws MessagingException;

	public abstract String[] getHeader(String headerName)
		throws MessagingException;

	public abstract int getLineCount()
		throws MessagingException;

	public abstract Enumeration getMatchingHeaders(String[] headerNames)
		throws MessagingException;

	public abstract Enumeration getNonMatchingHeaders(String[] headerNames)
		throws MessagingException;

	public abstract boolean isMimeType(String mimetype)
		throws MessagingException;

	public abstract void removeHeader(String headerName)
		throws MessagingException;

	public abstract void setContent(Object object, String type)
		throws MessagingException;

	public abstract void setContent(Multipart multipart)
		throws MessagingException;

	public abstract void setDataHandler(DataHandler handler)
		throws MessagingException;

	public abstract void setDescription(String desc)
		throws MessagingException;

	public abstract void setDisposition(String disposition)
		throws MessagingException;

	public abstract void setFileName(String filename)
		throws MessagingException;

	public abstract void setHeader(String headerName, String headerValue)
		throws MessagingException;

	public abstract void setText(String text)
		throws MessagingException;


} // Part
