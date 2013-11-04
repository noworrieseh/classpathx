/* MimeMessage.java */

/* License goes here. */

package javax.mail.internet;

import javax.mail.Message;
import javax.mail.Flags;
import javax.mail.Session;
import javax.mail.MessagingException;
import javax.mail.Address;
import javax.mail.Folder;
import javax.activation.DataHandler;
import java.io.InputStream;



/**
 * Modelling a MIME formatted message, this class provides ways
 * to retrieve, set, and format the data in a MIME message.
 *
 * A person creating a message will want to construct an empty instance and
 * then fill in the values as they desire. 
 *
 * @author Joey Lesh
 * @see javax.mail.Message
 * @see javax.mail.internet.MimePart
 */
public class MimeMessage extends Message implements MimePart {
	
  /* The <code>byte</code> array that holds this message's content.  */
  protected byte[] content;

  /* This holds source of the message's content.  */
  protected DataHandler dh;
  //	
  // but I can't put that in the constructor. Where does it go?

  /* The flags for this message.  */
  protected Flags flags;
	
  /* Holds the header for this message.  */
  protected InternetHeaders headers;

  /* Something for folders */
  private Folder folder;

  private Session session = null;


  // +-------------+--------------------------------------------------
  // | Inner Class |
  // +-------------+
	
  /**
   * Each instance represents a type of recipient such as the "Cc" or
   * carbon-copy recipients of an email message. Other types include "Bcc"
   * "To" and "Newsgroups".
   */
  public static class RecipientType extends Message.RecipientType { 

    /* The type representing the "Newsgroups" recipent. */
    public static final RecipientType NEWSGROUPS = new RecipientType("Newsgroups");
		
    // The MimeMessage.RecipientType.type field is inherited.
		
    /* Constructs a <code>MimeMessage.RecipientType</code> instance of the 
     * given type. 
     *
     * @param type A <code>String</code> specifying the type of recipient.
     */
    protected RecipientType(java.lang.String type) {
      super(type);
    }
  }// MimeMessage.RecipientType


  // +--------------+----------------------------------
  // | Constructors |
  // +--------------+

  /**
   * Constructs a new <code>MimeMessage</code>. The message is empty.
   *
   */
  public MimeMessage(Session session) {
    this.flags = new Flags();
    this.headers = new InternetHeaders();
    this.session = session;
    // TO DO: content and dh are unassigned.
  }// MimeMessage(Session)
	
  /**
   * Parses the <code>InputStream</code> into full <code>MimeMessage</code>
   * leaving the marker positioned at the end of the stream.
   *
   * @param session The session data.
   * @param is The stream of data to parse into a message.
   * @exception Thrown when data in the stream is in incorrect format.
   */
  public MimeMessage(Session session, InputStream is) throws MessagingException {
    headers = new InternetHeaders(is);
    this.session = session;
    // TO DO 
    // set up Flags, dh, and content.
  }// MimeMessage(Session, InputStream)

  /** 
   * Used to make an empty message with the given folder and message number.
   * This is mostly here for those who will subclass it.
   */
  protected MimeMessage(Folder folder, int msgnum) {
    // TO DO
  }// MimeMessage(Folder, int)

  /**
   * Used to make a message with the given folder and message number. The
   * message will be parsed from the <code>InputStream</code>.
   *
   * @param folder The folder that contains this message.
   * @param is The stream to parse this message from.
   * @param msgnum This message's ID number.
   * @exception Thrown when the stream data doesn't parse right.
   */
  protected MimeMessage(Folder folder, InputStream is, int msgnum) throws MessagingException {
    //TO DO
  }// MimeMessage(Folder, InputStream, int)
		
  /**
   * Used to make a message with the given folder and message number. The
   * message will be parsed from the <code>InputStream</code>.
   *
   * @param folder The folder that contains this message.
   * @param headers The headers for the message.
   * @param content A byte array holding the content of the message.
   * @param msgnum This message's ID number.
   * @exception Thrown when the stream data doesn't parse right.
   */
  protected MimeMessage(Folder folder, InternetHeaders headers, 
			byte[] content, int msgnum) throws MessagingException
  {
    this.headers = headers;
    this.content = content;
    // TO DO
  }// MimeMessage(Folder, InternetHeaders, byte[], int)


  // +=========+-----------------------------------------------------
  // | Methods |
  // +=========+

  /**
   * Gets the <code>Address</code> objects for the people this message is
   * from.  Attempts to use the RFC822 "From" header, if that is null, looks
   * at the "Sender" header.
   *
   * @return An array of <code>Address</code> objects.
   * @exception If the headers can't be correctly parsed.
   */
  public Address[] getFrom() throws MessagingException {
    String froms = headers.getHeader("From", ",");
    if (froms == null) {
      froms = headers.getHeader("Sender", ",");
    }
    return InternetAddress.parse(froms);
  }// getFrom()
	
  /**
   * Sets the "From" header to the <code>Address</code> value.
   * If other values are already set for the header, it will replace them. 
   * Using null for the <code>address</code> parameter will cause the "From"
   * header to be removed. 
   *
   * @param address An <code>Address</code> that contains the 
   *        address of the person this email is from.
   */
  public void setFrom(Address address) throws MessagingException {
    if (address == null) {
      headers.removeHeader("From");
    } else {
      headers.setHeader("From", address.toString());
    }
  }// setFrom(Address)
	
  /**
   * Sets the "From" header using the local address to get
   * an <code>Address</code> to extract the address from. This method
   * would be used when creating a message to be sent. It will set the
   * address to that of the local user. 
   *
   * If there are other values already for this header, the values will 
   * be replaced by the new one.
   *
   * @see javax.mail.internet.InternetAddress#getLocalAddress(Session)
   */
  public void setFrom() throws MessagingException {
    headers.setHeader("From", InternetAddress.getLocalAddress(session).toString());
  }// setFrom()

  /**
   * Adds all of the <code>Address</code>es to the "From" header. 
   *
   * @param addresses The email addresses to add to the "From" header.
   */
  public void addFrom(Address[] addresses) throws MessagingException {
    if (addresses != null) {
      headers.setHeader("From", InternetAddress.toString(this.getFrom())+", "+InternetAddress.toString(addresses));
    }
  }// addFrom(Address[])
	
  /**
   * Gets an array of <code>Address</code>es that are the recipients of the
   * type specified by the <code>RecipientType</code>
   * The RecipientType mapping looks like so:
   * <pre>
   * Message.RecipientType.TO		"To"
   * Message.RecipientType.CC		"Cc"
   * Message.RecipientType.BCC	"Bcc"
   * MimeMessage.RecipientType.NEWSGROUPS	"Newsgroups"
   * </pre>
   *
   * If the <code>type</code> isn't one of the above, null is returned.
   *
   * @return An <code>Address[]</code> of recipients. 
   */
  public Address[] getRecipients(Message.RecipientType type) throws MessagingException {
    if (type ==Message.RecipientType.TO)
    return InternetAddress.parse(headers.getHeader("To", ","));
    else if (type == Message.RecipientType.CC) 
    return InternetAddress.parse(headers.getHeader("Cc", ","));
    else if (type == Message.RecipientType.BCC) 
    return InternetAddress.parse(headers.getHeader("Bcc", ","));
    else if (type == MimeMessage.RecipientType.NEWSGROUPS) 
    return InternetAddress.parse(headers.getHeader("Newsgroups", ","));
    else
    return null;
  }// getRecipients(Message.RecipientType)
	
  /**
   * Gets an array of <code>Address</code>es that are the recipients of the
   * message. It will get recipients from the "To", "Cc", "Bcc" and 
   * "Newsgroups" headers. 
   *
   * @return An array containing all recipients of this message.
   */
  public Address[] getAllRecipients() throws MessagingException {
    // There must be a "To" address. (TO DO double check that assertion,
    // it's true for email but for Newsgroups??)
    String addressString =  headers.getHeader("To", ",");
    //Get CC 
    addressString = addressString + ((headers.getHeader("Cc", ",") == null) ? "" : "," + headers.getHeader("Cc", ","));
    //Get BCC
    addressString = addressString + ((headers.getHeader("Cc", ",") == null) ? "" : "," + headers.getHeader("Bcc", ","));
    //Get Newsgroups
    addressString = addressString + ((headers.getHeader("Cc", ",") == null) ? "" : "," + headers.getHeader("Newsgroups", ","));

    // Make them into addresses
    Address[] addresses = InternetAddress.parse(addressString);
    return addresses;
  }// getAllRecipients()
	
  /**
   * Sets the recipients of the specified type of header to be those in the 
   * array of <code>Address</code>es.
   */
  public void setRecipients(Message.RecipientType type, Address[] addresses) throws MessagingException {
    if (type == Message.RecipientType.TO) 
    headers.setHeader("To", InternetAddress.toString(addresses));
    else if (type == Message.RecipientType.CC) 
    headers.setHeader("Cc", InternetAddress.toString(addresses));
    else if  (type == Message.RecipientType.BCC) 
    headers.setHeader("Bcc", InternetAddress.toString(addresses));
    else if (type == MimeMessage.RecipientType.NEWSGROUPS) 
    headers.setHeader("Newsgroups", InternetAddress.toString(addresses));
  }// setRecipients(Message.RecipientType, Address[])
	
  /**
   * Adds the given addresses to <code>this</code> as recipients of type
   * <code>type</code>. If the type doesn't already have recipients, the
   * header will be added and then the recipients will be added. 
   *
   * @param type The kind of recipient to add the extra recipients to.
   * @param addresses The <code>Address</code>es to add as recipients.
   */
  public void addRecipients(Message.RecipientType type, Address[] addresses) throws MessagingException {
    // You'll note that the use of arrays makes this code a little
    // annoying. Don't blame me, I like lists.
    int i;
    Address[] tmpAddresses = this.getRecipients(type);
    Address[] allAddresses = new Address[tmpAddresses.length + addresses.length]; 
    for (i = 0; i < tmpAddresses.length; i++) 
    allAddresses[i] = tmpAddresses[i];
    for (int j = 0; j < addresses.length ; i++, j++) 
    allAddresses[i] = addresses[j];
    this.setRecipients(type, allAddresses);
  }// addRecipients(Message.RecipientType, Address[])
	
  /**
   * Gets the value of the "Reply-To" header. If the header doesn't exist
   * or lacks a value, the value of <code>getFrom</code> will be returned.
   *
   * @return An array of <code>Address</code>es that has the address from 
   * The "Reply-To" or "From" header.
   */
  public Address[] getReplyTo() throws MessagingException  {
    Address[] addresses;
    addresses = InternetAddress.parse(headers.getHeader("Reply-To", ","));
    if (addresses == null) 
    addresses = getFrom();
    return addresses;
  }// getReplyTo()
	
  /**
   * Sets the RFC822 "Reply-To" header to the addresses specified in
   * the parameter <code>addresses</code>.
   *
   * @param addresses The address to set "Reply-To"'s header to.
   */
  public void setReplyTo(Address[] addresses) throws MessagingException {
    headers.setHeader("Reply-To", InternetAddress.toString(addresses));
  }// setReplyTo(Address[])

  /**
   * Gets the text associated with the "Subject" header. If the header is
   * encoded according to the rules in RFC2047 it is decoded. If the decoding
   * or the conversion to the Unicode charset fails, the text is returned
   * in its original form.
   *
   * @return The value associated with the "Subject" header.
   */
  public String getSubject() throws MessagingException {
    String subject = headers.getHeader("Subject", null);
    try {
      subject = MimeUtility.decodeText(subject);
    }
    catch (java.io.UnsupportedEncodingException e) {
      // We ignore the exception and return the data as is.
    }
    return subject;
  }// getSubject()

  /**
   * Sets the value associated with the "Subject" header to that 
   * of the parameter <code>subject</code>.  That <code>String</code>
   * is converted to mail-safe form using the rules in RFC2047 and the 
   * encoding specifed by the platform's default charset.
   *
   * @param subject If null, causes the "Subject" header to be removed, 
   *      otherwise it is the text to made mail-safe and used as the subject.
   */
  public void setSubject(String subject) throws MessagingException {
    if (subject == null)
    headers.removeHeader("Subject");
    else {
      try {
	headers.setHeader("Subject", MimeUtility.encodeText(subject));
      }
      catch (java.io.UnsupportedEncodingException e) {
	throw new MessagingException("Failed encoding subject", e);
      }
    }
  }// setSubject(String)
	
  /**
   *
   */
  public void setSubject(String subject, String charset) throws MessagingException {
    if (subject == null)
    headers.removeHeader("Subject");
    else {
      try {
	headers.setHeader("Subject", MimeUtility.encodeText(subject, charset, null));
      }
      catch (java.io.UnsupportedEncodingException e) {
	throw new MessagingException("Failed encoding subject", e);
      }
    }
  }// setSubject(String, String)

  /**
   *
   */
  public java.util.Date getSentDate() throws MessagingException {
    return new Date(Date.parse(headers.getHeader("Date", null)));
  }// getSentDate()

  /**
   *
   */
  public void setSentDate(java.util.Date date) throws MessagingException {
    // I hate Java dates.  Someone who undertands them better should
    // do this part.

    // TO DO
  }// setSentDate(java.util.Date)

  /**
   * Returns the date this message was received or <code>null<code> if
   * not specified. This is not a field defined by RFC822 and as such, 
   * can quite possibly return <code>null</code>. In fact, that's what
   * this implementation does. 
   *
   * Implementations that provide this field will need to implement this
   * method differently
   * 
   * @return A <code>Date</code> that represents the date this message
   *         was received. (Currently null)
   */
  public void getReceivedDate() throws MessagingException {
    // Returns null for compatibility with Sun's impl.
    return null;
  }// getReceivedDate()

  /**
   * Returns the size of the message in bytes. 
   *
   * Is not required to be exactly accurate and may or may not account
   * for the transfer-encoding of this message.
   *
   * Implementation returns -1.
   *
   * @return The size of the message in bytes or -1 if size can't be 
   *         determined.
   */
  public int getSize() throws MessagingException {
    if (dh == null) 
    return -1;
    else
    return dh.length;
  }// getSize()
	
  /**
   * Returns the number of lines in the message or -1 if the line count 
   * cannot be determined. 
   *
   * Is not required to be exactly accurate and may or may not account
   * for the transfer-encoding of this message.
   *
   * Implementation returns -1.
   *
   * @return number of lines in the message or -1.
   */
  public int getLineCount() throws MessagingException {
    // Done for Sun compatability.
    return -1;
  }// getLineCount() 
	
  /**
   * Returns the value for the RFC 822 "Content-Type" header. This 
   * must return some value so if there is a problem getting the header
   * for it, text/plain will be returned.
   *
   * @return The content-type of this message.
   */
  public String getContentType() throws MessagingException {
    String contenttype = headers.getHeader("Content-Type", null);
    if (contenttype == null) 
    contenttype = "text/plain";
    return contenttype;
  }// getContentType()

  /**
   * Determines if the <code>mimeType</code> parameter specifies the
   * same content-type as this message does. The comparison ignores all
   * parameters and compares only the primary and secondary types. Also
   * the "*" character will match any type. For an example, look at 
   * ContentType.match(String).
   *
   * @return <code>true</code> if they match.
   * @see javax.mail.internet.ContentType#match(String)
   */
  public boolean isMimeType(String mimeType) throws MessagingException {
    ContentType ctype = new ContentType(this.getContentType());
    return ctype.match(mimetype);
  }// isMimeType(String)

  /**
   * Gets the "Content-Disposition" header from this message. If it 
   * doesn't exist, <code>null</code> is returned.
   *
   * @return A <code>String</code> detailing the disposition or <code>
   *         null</code> if it doesn't exist.
   */
  public String getDisposition() throws MessagingException {
    return headers.getHeader("Content-Disposition", null);
  }// getDisposition()

  /**
   *
   */
  public void setDisposition(String disposistion) throws MessagingException {
    // TO DO 
  }// setDisposition(String)

  /**
   * Gets the value of the "Content-Transfer-Encoding" and returns it.
   * If the header doesn't exist or there is no value associated with it,
   * null is returned.
   *
   * @return The content-transfer-encoding or null.
   */
  public String getEncoding() throws MessagingException {
    return headers.getHeader("Content-Transfer-Encoding", null);		
  }// getEncoding()

  /**
   * Gets the value of the "Content-ID" header and returns it.
   * If the header doesn't exist or there is no value associated with it,
   * null is returned.
   *
   * @return The content-id or null.
   *
   */
  public String getContentID() throws MessagingException {
    return headers.getHeader("Content-ID", null);
  }// getContentID()
                              
  /**
   * Sets the value of the "Content-ID" header to <code>cid</code>, or
   * adds the header and sets the value. If <code>cid</code> is 
   * <code>null</code> then the header is removed.
   *
   * @param cid The content-id (for example, TO DO)
   */
  public void setContentID(String cid) throws MessagingException {
    if (cid == null) 
    headers.removeHeader("Content-ID");
    else 
    headers.setHeader("Content-ID", cid);
  }// setContentID(String)

  /**
   * Gets the value of the "Content-MD5" header and returns it.
   * If the header doesn't exist or there is no value associated with it,
   * null is returned.
   *
   * @return the MD5 sum or null.
   */
  public String getContentMD5() throws MessagingException {
    return headers.getHeader("Content-MD5", null);
  }// getContentMD5()

  /**
   * Sets the value of the "Content-MD5" header to <code>md5</code>, or
   * adds the header and sets the value. If <code>md5</code> is 
   * <code>null</code> then the header is removed.
   **
   */
  public void setContentMD5(String md5) throws MessagingException {
    if (md5 == null) 
    headers.removeHeader("Content-MD5");
    else 
    headers.setHeader("Content-MD5", md5);
  }// setContentMD5(String)

  /**
   * Gets the value of the "Content-Description" header. If the header is
   * not there or the value is absent then <code>null</code> is returned.
   *
   *  My 
   * "Content-Description" right now should be "Joey Lesh, a sleepy,
   * disinterested intern."
   *
   * @return A string containing the value of the "Content-Description"
   *         header or null.
   */
  public String getDescription() throws MessagingException {
    return MimeUtility.decodeText(headers.getHeader("Content-Description", null));
  }// getDescription()

  /**
   *
   */
  public void setDescription(String description) throws MessagingException {
    if (description == null) 
    headers.remove("Content-Description");
    else {
      try {
	headers.setHeader("Content-Description", MimeUtility.encodeText(description));
      }
      catch (UnsupportedEncodingException e) {
	throw new MessagingException(e, "Encoding of the \"Content-Description\" header value failed.");
      }
    }
  }// setDescription(String)

  /**
   *
   */
  public void setDescription(String description, String charset) throws MessagingException {
    if (description == null) 
    headers.remove("Content-Description");
    else {
      try {
	headers.setHeader("Content-Description", MimeUtility.encodeText(description), charset);
      }
      catch (UnsupportedEncodingException e) {
	throw new MessagingException(e, "Encoding of the \"Content-Description\" header value failed.");
      }
    }
  }// setDescription(String)

  /**
   * Gets the value of the "Content-Language" header. For more info on this
   * header take a look at RFC 1766. 
   *
   * It will return null if the header is not present or doesn't have a 
   * value.
   *
   * @return The "Content-Language" header value.
   */
  public String[] getContentLanguage() throws MessagingException {
    return headers.getHeader("Content-Language");
  }// getContentLanguage()

  /**
   * Sets the value of the "Content-Language" header. Look at 
   * RFC 1766 for the definition of this header.
   *
   * 
   */
  public String[] setContentLanguage(String[] languages) throws MessagingException {
    for (int i =0; i < languages.length; i++) 
    headers.setHeader("Content-Language", languages[i]);
  }// setContentLanguage(String[])

  /**
   * Returns the value of the "Message-ID" header.
   *
   * @return The "Message-ID" <code>String</code> 
   */
  public String getMessageID() throws MessagingException {
    return headers.getHeader("Message-ID", null);
  }// getMessageID()

  /**
   *
   */
  public String getFileName() throws MessagingException {
    //TO DO
  }// getFileName()
		
  /**
   *
   */
  public void setFileName(String fileName) throws MessagingException {
		
  }// setFileName(String)
	
  /**
   * Returns a fully decoded stream for this <code>MimeMessage</code>'s 
   * content. 
   *
   * @return A decoded stream of the content
   */
  public InputStream getInputStream() throws MessagingException {
    return this.getDataHandler.getInputStream();
  }// getInputStream()
	
  /**
   * Returns a <code>InputStream</code> of the raw content. This can be
   * used for the creation of the <code>DataHandler</code>.
   *
   * @return A <code>ByteArrayInputStream</code> made from the <code>
   *         content</code> array.
   */
  public InputStream getContentStream() throws MessagingException {
    return new ByteArrayInputStream(content);
  }
	
  /**
   * Returns a <code>DataHandler</code> that will allow access to this
   * message's content.
   *
   * @return The <code>DataHandler</code>
   */
  public DataHandler getDataHandler() throws MessagingException {
    if (this.dh == null) 
    this.dh = new DataHandler(new MimeDataSource(this));
    return this.dh;
  }// getDataHandler()

  /**
   *
   */
  public Object getContent() throws MessagingException {
    return this.getDataHandler.getContent();
  }

  /**
   *
   */
  public void setDataHandler(DataHandler dh) throws MessagingException {
    this.dh = dh;
    InputStream is = dh.getInputStream();
    byte[] tmpContent;
    is.read(tmpContent, 0, is.available);
    this.content = tmpContent;
  }// setDataHandler(DataHandler)

  /**
   * This mehod is a convience to the user, as it creates 
   * <code>DataHandler</code> with the same paramaters. There must be
   * a <code>DataContentHandler</code> around that understands the MIME
   * type.
   *
   * @param o The <code>Object</code> that is to be the content.
   * @param type The MIME-type of the <code>Object</code>. Will be used
   *        to find  <code>DataContentHandler</code> to interpret 
   *        <code>o</code>.
   */
  public void setContent(Object o, String type) throws MessagingException {
    setDataHandler(new DataHandler(o, type));
  }// setContent(Object, String)

  /**
   *
   */
  public void setText(String text) throws MessagingException {
    text = MimeUtility.encode(text);
    setContent(text, "text/plain");
  }// setText(String)

  /**
   *
   */
  public void setText(String text, String charset) throws MessagingException {
    text = MimeUtility.encode(text, charset);
    setContent(text, "text/plain");
  }// setText(String)

  /**
   *
   */
  public void setContent(Multipart mp) throws MessagingException {
    setDataHandler(new DataHandler(mp, mp.getContentType()));
    // TO DO: I don't really know if this code will work.
  }// setContent(Multipart)

  /**
   *
   */
  public Message reply(boolean replyToAll) throws MessagingException {
    // TO DO
  }// reply(boolean)

  /**
   *
   */
  public void writeTo(java.io.OutputStream os) throws MessagingException {
    this.getDataHandler().writeTo(os);
  }// writeTo(OutputStream)

  /**
   *
   */
  public void writeTo(java.io.OutputStream os, String[] ignorelist) throws MessagingException {
		
  }
	
}// MimeMessage










