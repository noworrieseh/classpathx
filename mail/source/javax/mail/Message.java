/********************************************************************
 * Copyright (c) Open Java Extensions, Andrew Selkirk  LGPL License *
 ********************************************************************/

package javax.mail;

// Imports
import java.io.*;
import java.util.*;
import javax.activation.*;
import javax.mail.search.*;

/**
 * Message.
 */
public abstract class Message implements Part {

	//-------------------------------------------------------------
	// Classes ----------------------------------------------------
	//-------------------------------------------------------------

	public static final class RecipientType {

		public static final RecipientType TO  = new RecipientType("TO");
		public static final RecipientType CC  = new RecipientType("CC");
		public static final RecipientType BCC = new RecipientType("BCC");

		/**
		 * Recipient type string.
		 */
		protected	String	type	= null;

		/**
		 * Create a new recipient type.
		 * @param type Recipient type
		 */
		protected RecipientType(String type) {
			this.type = type;
		} // RecipientType()

	} // RecipientType


	//-------------------------------------------------------------
	// Variables --------------------------------------------------
	//-------------------------------------------------------------

	protected	int		msgnum		= 0;
	protected	boolean	expunged	= false;
	protected	Folder	folder		= null;
	protected	Session	session		= null;

	//-------------------------------------------------------------
	// Initialization ---------------------------------------------
	//-------------------------------------------------------------

	public Message() {
	} // Message()

	public Message(Folder folder, int messageNum) {
		this.folder = folder;
		msgnum = messageNum;
	} // Message()

	public Message(Session session) {
		this.session = session;
	} // Message()


	//-------------------------------------------------------------
	// Interface: Part --------------------------------------------
	//-------------------------------------------------------------

	public boolean isSet(Flags.Flag flag)
			throws MessagingException {
		return getFlags().contains(flag);
	} // isSet()

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

	public abstract void addFrom(Address[] addresses)
		throws MessagingException;

	public abstract void addHeader(String headerName, String headerValue)
		throws MessagingException;

	public void addRecipient(RecipientType type, Address address)
			throws MessagingException {
		addRecipients(type, address);
	} // addRecipient()

	public abstract void addRecipients(RecipientType type, Address address)
		throws MessagingException;

	public abstract Enumeration getAllHeaders()
		throws MessagingException;

	public Address[] getAllRecipients() throws MessagingException {

		// Variables
		Address[]	to;
		Address[]	cc;
		Address[]	bcc;
		Address[]	all;
		int		index;

		// Get All Recipients
		to = getRecipients(RecipientType.TO);
		cc = getRecipients(RecipientType.CC);
		bcc = getRecipients(RecipientType.BCC);

		// Create recipients
		all = new Address[to.length + cc.length + bcc.length];

		// Add TO Recipients
		for (index = 0; index < to.length; index++) {
			all[index] = to[index];
		} // for: index

		// Add CC Recipients
		for (index = 0; index < cc.length; index++) {
			all[to.length + index] = cc[index];
		} // for: index

		// Add BCC Recipients
		for (index = 0; index < bcc.length; index++) {
			all[to.length + cc.length + index] = bcc[index];
		} // for: index

		// Return All Recipients
		return all;

	} // getAllRecipients()

	public abstract DataHandler getDataHandler()
		throws MessagingException;

	public abstract String getDescription()
		throws MessagingException;

	public abstract String getDisposition()
		throws MessagingException;

	public abstract String getFileName()
		throws MessagingException;

	public abstract Flags getFlags()
		throws MessagingException;

	public Folder getFolder() {
		return folder;
	} // getFolder()

	public abstract Address[] getFrom()
		throws MessagingException;

	public abstract String[] getHeader(String headerName)
		throws MessagingException;

	public abstract int getLineCount()
		throws MessagingException;

	public abstract Enumeration getMatchingHeaders(String[] headerNames)
		throws MessagingException;

	public int getMessageNumber() {
		return msgnum;
	} // getMessageNumber()

	public abstract Enumeration getNonMatchingHeaders(String[] headerNames)
		throws MessagingException;

	public abstract Date getReceivedDate()
		throws MessagingException;

	public abstract Address[] getRecipients(RecipientType type)
		throws MessagingException;

	public Address[] getReplyTo() throws MessagingException {
		return getFrom();
	} // getReplyTo()

	public abstract Date getSentDate() throws MessagingException;

	public abstract String getSubject() throws MessagingException;

	public boolean isExpunged() {
		return expunged;
	} // isExpunged()

	public abstract boolean isMimeType(String mimetype)
		throws MessagingException;

	public boolean match(SearchTerm term) throws MessagingException {
		return term.match(this);
	} // match()

	public abstract void removeHeader(String headerName)
		throws MessagingException;

	public abstract Message reply(boolean value)
		throws MessagingException;

	public abstract void saveChanges()
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

	protected void setExpunged(boolean value) {
		expunged = true;
	} // setExpunged()

	public abstract void setFileName(String filename)
		throws MessagingException;

	public void setFlag(Flags.Flag flag, boolean value)
			throws MessagingException {
		setFlags(new Flags(flag), value);
	} // setFlag()

	public abstract void setFlags(Flags flags, boolean value)
		throws MessagingException;

	public abstract void setFrom()
		throws MessagingException;

	public abstract void setFrom(Address address)
		throws MessagingException;

	public abstract void setHeader(String headerName, String headerValue)
		throws MessagingException;

	protected void setMessageNumber(int messageNum) {
		msgnum = messageNum;
	} // setMessageNumber()

	public void setRecipient(RecipientType type, Address address)
			throws MessagingException {

		// Variables
		Address[]	recipients;
		Address[]	list;
		int		index;

		// Get Current Recipients
		recipients = getRecipients(type);

		// Create New Array
		list = new Address[recipients.length + 1];

		// Copy
		for (index = 0; index < recipients.length; index++) {
			list[index] = recipients[index];
		} // for: index
		list[recipients.length] = address;

		// Set Recipients
		setRecipients(type, list);

	} // setRecipient()

	public abstract void setRecipients(RecipientType type, Address[] addresses)
		throws MessagingException;

	public void setReplyTo(Address[] addresses)
			throws MessagingException {
		throw new MethodNotSupportedException();
	} // setReplyTo()

	public abstract void setSentDate(Date date)
		throws MessagingException;

	public abstract void setSubject(String subject)
		throws MessagingException;

	public abstract void setText(String text)
		throws MessagingException;


} // Message
