/*
 * MimeMessage.java
 * Copyright (C) 2001 dog <dog@dog.net.uk>
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

package javax.mail.internet;

import java.io.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.*;
import javax.activation.DataHandler;
import javax.mail.*;
import gnu.mail.util.CRLFOutputStream;

/**
 * This class represents a MIME style email message.
 * It implements the Message abstract class and the MimePart interface.
 * <p>
 * Clients wanting to create new MIME style messages will instantiate an empty
 * MimeMessage object and then fill it with appropriate attributes and content.
 * <p>
 * Service providers that implement MIME compliant backend stores may want to
 * subclass MimeMessage and override certain methods to provide specific
 * implementations. The simplest case is probably a provider that generates 
 * a MIME style input stream and leaves the parsing of the stream to this 
 * class.
 * <p>
 * MimeMessage uses the InternetHeaders class to parse and store the toplevel 
 * RFC 822 headers of a message.
 * <p>
 * <hr>
 * A note on RFC 822 and MIME headers
 * <p>
 * RFC 822 header fields must contain only US-ASCII characters. MIME allows 
 * non ASCII characters to be present in certain portions of certain headers,
 * by encoding those characters. RFC 2047 specifies the rules for doing this.
 * The MimeUtility class provided in this package can be used to to achieve 
 * this.
 * Callers of the <code>setHeader</code>, <code>addHeader</code>, and 
 * <code>addHeaderLine</code> methods are responsible for enforcing the MIME
 * requirements for the specified headers. In addition, these header fields
 * must be folded (wrapped) before being sent if they exceed the line length
 * limitation for the transport (1000 bytes for SMTP). Received headers may
 * have been folded. The application is responsible for folding and unfolding
 * headers as appropriate.
 */
public class MimeMessage
  extends Message
  implements MimePart
{

  /**
   * This inner class extends the javax.mail.Message.RecipientType class 
   * to add additional RecipientTypes.
   * The one additional RecipientType currently defined here is NEWSGROUPS.
   */
  public static class RecipientType
    extends Message.RecipientType
  {

    /**
     * The "Newsgroup" (Usenet news) recipients.
     */
    public static final RecipientType NEWSGROUPS =
      new RecipientType("Newsgroups");

    /**
     * When deserializing a RecipientType, we need to make sure to return
     * only one of the known static final instances defined in this class.
     * Subclasses must implement their own readResolve method that checks
     * for their known instances before calling this super method.
     */
    protected Object readResolve()
      throws ObjectStreamException
    {
      if (type.equals("Newsgroups"))
        return NEWSGROUPS;
      else
        return super.readResolve();
    }

    // super :-)
    protected RecipientType(String type)
    {
      super(type);
    }
    
  }

  /**
   * The DataHandler object representing this Message's content.
   */
  protected DataHandler dh;

  /**
   * Byte array that holds the bytes of this Message's content.
   */
  protected byte content[];

  /**
   * If the data for this message was supplied by an InputStream 
   * that implements the SharedInputStream interface, contentStream is 
   * another such stream representing the content of this message.
   * In this case, content will be null.
   */
  protected InputStream contentStream;

  /**
   * The InternetHeaders object that stores the header of this message.
   */
  protected InternetHeaders headers;

  /**
   * The Flags for this message.
   */
  protected Flags flags;

  /**
   * A flag indicating whether the message has been modified.
   * If the message has not been modified, any data in the content array
   * is assumed to be valid and is used directly in the <code>writeTo</code>
   * method. This flag is set to true when an empty message is created 
   * or when the <code>saveChanges</code> method is called.
   */
  protected boolean modified;

  /**
   * Does the saveChanges method need to be called on this message?
   * This flag is set to false by the public constructor and set to true 
   * by the <code>saveChanges</code> method.
   * The <code>writeTo</code> method checks this flag and calls the 
   * <code>saveChanges</code> method as necessary.
   * This avoids the common mistake of forgetting to call the 
   * <code>saveChanges</code> method on a newly constructed message.
   */
  protected boolean saved;

  /*
   * This is used to parse and format values for the RFC822 Date header.
   */
  private static MailDateFormat dateFormat = new MailDateFormat();

  // Header constants.
  static final String TO_NAME = "To";
  static final String CC_NAME = "To";
  static final String BCC_NAME = "To";
  static final String NEWSGROUPS_NAME = "Newsgroups";
  static final String FROM_NAME = "From";
  static final String SENDER_NAME = "Sender";
  static final String REPLY_TO_NAME = "Reply-To";
  static final String SUBJECT_NAME = "Subject";
  static final String DATE_NAME = "Date";
  static final String MESSAGE_ID_NAME = "Message-ID";
  
  /**
   * Default constructor.
   * An empty message object is created.
   * The headers field is set to an empty InternetHeaders object.
   * The flags field is set to an empty Flags object.
   * The <code>modified</code> flag is set to true.
   */
  public MimeMessage(Session session)
  {
    super(session);
    headers = new InternetHeaders();
    flags = new Flags();
    modified = true;
  }

  /**
   * Constructs a MimeMessage by reading and parsing the data 
   * from the specified MIME InputStream.
   * The InputStream will be left positioned at the end of the data 
   * for the message. Note that the input stream parse is done within this
   * constructor itself.
   * @param session Session object for this message
   * @param is the message input stream
   */
  public MimeMessage(Session session, InputStream is)
    throws MessagingException
  {
    super(session);
    flags = new Flags();
    parse(is);
    saved = true;
  }

  /**
   * Constructs a new MimeMessage with content initialized from the source
   * MimeMessage.
   * The new message is independent of the original.
   * <p>
   * Note: The current implementation is rather inefficient, copying the data
   * more times than strictly necessary.
   * @param source the message to copy content from
   */
  public MimeMessage(MimeMessage source)
    throws MessagingException
  {
    super(source.session);
    // Don't know how Sun are copying the data more times than necessary:
    // here I am copying it exactly once!
    try
    {
      ByteArrayOutputStream bos = new ByteArrayOutputStream();
      source.writeTo(bos);
      bos.close();
      ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
      parse(bis);
      bis.close();
      saved = true;
    }
    catch (IOException e)
    {
      throw new MessagingException("I/O error", e);
    }
  }

  /**
   * Constructs an empty MimeMessage object with the given Folder and message
   * number.
   * <p>
   * This method is for providers subclassing MimeMessage.
   */
  protected MimeMessage(Folder folder, int msgnum)
  {
    super(folder, msgnum);
    flags = new Flags();
    saved = true;
  }

  /**
   * Constructs a MimeMessage by reading and parsing the data from the 
   * specified MIME InputStream.
   * The InputStream will be left positioned at the end of the data
   * for the message. Note that the input stream parse is done within this
   * constructor itself.
   * <p>
   * This method is for providers subclassing MimeMessage.
   * @param folder The containing folder.
   * @param is the message input stream
   * @param msgnum Message number of this message within its folder
   */
  protected MimeMessage(Folder folder, InputStream is, int msgnum)
    throws MessagingException
  {
    this(folder, msgnum);
    parse(is);
  }

  /**
   * Constructs a MimeMessage from the given InternetHeaders object and 
   * content.
   * This method is for providers subclassing MimeMessage.
   * @param folder The containing folder.
   * @param headers The message headers.
   * @param content the content as an array of bytes
   * @param msgnum Message number of this message within its folder
   */
  protected MimeMessage(Folder folder, InternetHeaders headers, 
      byte[] content, int msgnum)
    throws MessagingException
  {
    this(folder, msgnum);
    this.headers = headers;
    this.content = content;
  }

  /**
   * Parse the InputStream setting the headers and content fields 
   * appropriately.
   * Also resets the modified flag.
   * <p>
   * This method is intended for use by subclasses that need to control 
   * when the InputStream is parsed.
   * @param is The message input stream
   */
  protected void parse(InputStream is)
    throws MessagingException
  {
    // buffer it
    if (!(is instanceof ByteArrayInputStream) && 
        !(is instanceof BufferedInputStream))
      is = new BufferedInputStream(is);
    // headers
    headers = createInternetHeaders(is);
    // content
    if (is instanceof SharedInputStream)
    {
      SharedInputStream sis = (SharedInputStream)is;
      contentStream = sis.newStream(sis.getPosition(), -1L);
    }
    else
    {
      // Read stream into byte array
      try
      {
        // TODO Make buffer size configurable
        int len = 1024;
        if (is instanceof ByteArrayInputStream)
        {
          len = is.available();
          content = new byte[len];
          is.read(content, 0, len);
        }
        else
        {
          ByteArrayOutputStream bos = new ByteArrayOutputStream(len);
          content = new byte[len]; // it's just a buffer!
          for (int l = is.read(content, 0, len); 
              l!=-1;
              l = is.read(content, 0, len)) 
            bos.write(content, 0, l);
          content = bos.toByteArray();
        }
      }
      catch (IOException e)
      {
        throw new MessagingException("I/O error", e);
      }
    }
    modified = false;
  }

  // -- From --
  
  /**
   * Returns the value of the RFC 822 "From" header fields.
   * If this header field is absent, the "Sender" header field is used.
   * If the "Sender" header field is also absent, null is returned.
   * <p>
   * This implementation uses the <code>getHeader</code> method 
   * to obtain the requisite header field.
   */
  public Address[] getFrom()
    throws MessagingException
  {
    Address[] from = getInternetAddresses(FROM_NAME);
    if (from==null)
      from = getInternetAddresses(SENDER_NAME);
    return from;
  }

  /**
   * Set the RFC 822 "From" header field.
   * Any existing values are replaced with the given address.
   * If address is null, this header is removed.
   * @param address the sender of this message
   * @exception IllegalWriteException if the underlying implementation 
   * does not support modification of existing values
   * @exception IllegalStateException if this message is obtained from 
   * a READ_ONLY folder.
   */
  public void setFrom(Address address)
    throws MessagingException
  {
    if (address==null)
      removeHeader(FROM_NAME);
    else
      setHeader(FROM_NAME, address.toString());
  }

  /**
   * Set the RFC 822 "From" header field using the value of the
   * <code>InternetAddress.getLocalAddress</code> method.
   * @exception IllegalWriteException if the underlying implementation 
   * does not support modification of existing values
   * @exception IllegalStateException if this message is obtained from 
   * a READ_ONLY folder.
   */
  public void setFrom()
    throws MessagingException
  {
    InternetAddress localAddress = 
      InternetAddress.getLocalAddress(session);
    if (localAddress!=null)
      setFrom(localAddress);
    else
      throw new MessagingException("No local address");
  }

  /**
   * Add the specified addresses to the existing "From" field.
   * If the "From" field does not already exist, it is created.
   * @param addresses the senders of this message
   * @exception IllegalWriteException if the underlying implementation 
   * does not support modification of existing values
   * @exception IllegalStateException if this message is obtained from 
   * a READ_ONLY folder.
   */
  public void addFrom(Address[] addresses)
    throws MessagingException
  {
    addInternetAddresses(FROM_NAME, addresses);
  }

  // -- To --
  
  /**
   * Returns the recepients specified by the type.
   * The mapping between the type and the corresponding RFC 822 header 
   * is as follows:
   * <dl>
   * <dt>Message.RecipientType.TO<dd>"To"
   * <dt>Message.RecipientType.CC<dd>"Cc"
   * <dt>Message.RecipientType.BCC<dd>"Bcc"
   * <dt>MimeMessage.RecipientType.NEWSGROUPS<dd>"Newsgroups"
   * </dl>
   * <p>
   * Returns null if the header specified by the type is not found or if its
   * value is empty.
   * <p>
   * This implementation uses the <code>getHeader</code> method 
   * to obtain the requisite header field.
   * @param type the type of recipient
   */
  public Address[] getRecipients(Message.RecipientType type)
    throws MessagingException
  {
    if (type==RecipientType.NEWSGROUPS)
    {
      // Can't use getInternetAddresses here
      // and it's not worth a getNewsAddresses method
      String header = getHeader(NEWSGROUPS_NAME, ",");
      return (header!=null) ? NewsAddress.parse(header) : null;
    }
    return getInternetAddresses(getHeader(type));
  }

  /**
   * Get all the recipient addresses for the message.
   * Extracts the TO, CC, BCC, and NEWSGROUPS recipients.
   */
  public Address[] getAllRecipients()
    throws MessagingException
  {
    Address[] recipients = super.getAllRecipients();
    Address[] newsgroups = getRecipients(RecipientType.NEWSGROUPS);
    if (newsgroups==null)
      return recipients;
    else if (recipients==null)
      return newsgroups;
    else
    {
      Address[] both = new Address[recipients.length+newsgroups.length];
      System.arraycopy(recipients, 0, both, 0, recipients.length);
      System.arraycopy(newsgroups, 0, both, recipients.length, 
          newsgroups.length);
      return both;
    }
  }

  /**
   * Set the specified recipient type to the given addresses.
   * If the address parameter is null, the corresponding recipient field 
   * is removed.
   * @param type Recipient type
   * @param addresses Addresses
   * @exception IllegalWriteException if the underlying implementation 
   * does not support modification of existing values
   * @exception IllegalStateException if this message is obtained from 
   * a READ_ONLY folder.
   */
  public void setRecipients(Message.RecipientType type, Address[] addresses)
    throws MessagingException
  {
    if (type==RecipientType.NEWSGROUPS)
    {
      if (addresses==null || addresses.length==0)
        removeHeader(NEWSGROUPS_NAME);
      else
        setHeader(NEWSGROUPS_NAME, NewsAddress.toString(addresses));
    }
    else
      setInternetAddresses(getHeader(type), addresses);
  }

  /**
   * Set the specified recipient type to the given addresses.
   * If the address parameter is null, the corresponding recipient field
   * is removed.
   * @param type Recipient type
   * @param addresses Addresses
   * @exception IllegalWriteException if the underlying implementation 
   * does not support modification of existing values
   * @exception IllegalStateException if this message is obtained from 
   * a READ_ONLY folder.
   */
  public void setRecipients(Message.RecipientType type, String addresses)
    throws MessagingException
  {
    if (type==RecipientType.NEWSGROUPS)
    {
      if (addresses==null || addresses.length()==0)
        removeHeader(NEWSGROUPS_NAME);
      else
        setHeader(NEWSGROUPS_NAME, addresses);
    }
    else
      setInternetAddresses(getHeader(type),
          InternetAddress.parse(addresses));
  }

  /**
   * Add the given addresses to the specified recipient type.
   * @param type Recipient type
   * @param addresses Addresses
   * @exception IllegalWriteException if the underlying implementation 
   * does not support modification of existing values
   * @exception IllegalStateException if this message is obtained from 
   * a READ_ONLY folder.
   */
  public void addRecipients(Message.RecipientType type, Address[] addresses)
    throws MessagingException
  {
    if (type==RecipientType.NEWSGROUPS)
    {
      String value = NewsAddress.toString(addresses);
      if (value!=null)
        addHeader(NEWSGROUPS_NAME, value);
    }
    else
      addInternetAddresses(getHeader(type), addresses);
  }

  /**
   * Add the given addresses to the specified recipient type.
   * @param type Recipient type
   * @param addresses Addresses
   * @exception IllegalWriteException if the underlying implementation 
   * does not support modification of existing values
   * @exception IllegalStateException if this message is obtained from 
   * a READ_ONLY folder.
   */
  public void addRecipients(Message.RecipientType type, String addresses)
    throws MessagingException
  {
    if (type==RecipientType.NEWSGROUPS)
    {
      if (addresses!=null && addresses.length()!=0)
        addHeader(NEWSGROUPS_NAME, addresses);
    }
    else
      addInternetAddresses(getHeader(type),
          InternetAddress.parse(addresses));
  }

  /**
   * Return the value of the RFC 822 "Reply-To" header field.
   * If this header is unavailable or its value is absent,
   * then the <code>getFrom</code> method is called and its value is returned.
   * This implementation uses the <code>getHeader</code> method
   * to obtain the requisite header field.
   */
  public Address[] getReplyTo()
    throws MessagingException
  {
    Address[] replyTo = getInternetAddresses(REPLY_TO_NAME);
    if (replyTo==null)
      replyTo = getFrom();
    return replyTo;
  }

  /**
   * Set the RFC 822 "Reply-To" header field.
   * If the address parameter is null, this header is removed.
   * @param addresses Addresses
   * @exception IllegalWriteException if the underlying implementation 
   * does not support modification of existing values
   * @exception IllegalStateException if this message is obtained from 
   * a READ_ONLY folder.
   */
  public void setReplyTo(Address[] addresses)
    throws MessagingException
  {
    setInternetAddresses(REPLY_TO_NAME, addresses);
  }

  // convenience method
  private Address[] getInternetAddresses(String name)
    throws MessagingException
  {
    String value = getHeader(name, ",");
    return (value!=null) ? InternetAddress.parse(value) : null;
  }

  // convenience method
  private void setInternetAddresses(String name, Address[] addresses)
    throws MessagingException
  {
    String line = InternetAddress.toString(addresses);
    if (line==null)
      removeHeader(line);
    else
      setHeader(name, line);
  }

  // convenience method
  private void addInternetAddresses(String name, Address[] addresses)
    throws MessagingException
  {
    String line = InternetAddress.toString(addresses);
    if (line!=null)
      addHeader(name, line);
  }

  /*
   * Convenience method to return the header name for a given recipient
   * type. This should be faster than keeping a hash of recipient types to
   * names.
   */
  private String getHeader(Message.RecipientType type)
    throws MessagingException
  {
    if (type==Message.RecipientType.TO)
      return TO_NAME;
    if (type==Message.RecipientType.CC)
      return CC_NAME;
    if (type==Message.RecipientType.BCC)
      return BCC_NAME;
    if (type==RecipientType.NEWSGROUPS)
      return NEWSGROUPS_NAME;
    throw new MessagingException("Invalid recipient type");
  }

  /**
   * Returns the value of the "Subject" header field.
   * Returns null if the subject field is unavailable or its value is absent.
   * <p>
   * If the subject is encoded as per RFC 2047, it is decoded and converted 
   * into Unicode. If the decoding or conversion fails, 
   * the raw data is returned as is.
   * <p>
   * This implementation uses the <code>getHeader</code> method
   * to obtain the requisite header field.
   */
  public String getSubject()
    throws MessagingException
  {
    String subject = getHeader(SUBJECT_NAME, null);
    if (subject==null)
      return null;
    try
    {
      subject = MimeUtility.decodeText(subject);
    }
    catch (UnsupportedEncodingException e)
    {
    }
    return subject;
  }

  /**
   * Set the "Subject" header field. 
   * If the subject contains non US-ASCII characters, it will be encoded 
   * using the platform's default charset. If the subject contains only 
   * US-ASCII characters, no encoding is done and it is used as-is.
   * If the subject is null, the existing "Subject" field is removed.
   * <p>
   * Note that if the charset encoding process fails, a MessagingException is
   * thrown, and an UnsupportedEncodingException is included in the chain of
   * nested exceptions within the MessagingException.
   * @param subject the subject
   * @exception IllegalWriteException if the underlying implementation 
   * does not support modification of existing values
   * @exception IllegalStateException if this message is obtained from 
   * a READ_ONLY folder.
   */
  public void setSubject(String subject)
    throws MessagingException
  {
    setSubject(subject, null);
  }

  /**
   * Set the "Subject" header field. 
   * If the subject contains non US-ASCII characters, it will be encoded 
   * using the specified charset. If the subject contains only 
   * US-ASCII characters, no encoding is done and it is used as-is.
   * If the subject is null, the existing "Subject" field is removed.
   * <p>
   * Note that if the charset encoding process fails, a MessagingException is
   * thrown, and an UnsupportedEncodingException is included in the chain of
   * nested exceptions within the MessagingException.
   * @param subject the subject
   * @param charset the charset
   * @exception IllegalWriteException if the underlying implementation 
   * does not support modification of existing values
   * @exception IllegalStateException if this message is obtained from 
   * a READ_ONLY folder.
   */
  public void setSubject(String subject, String charset)
    throws MessagingException
  {
    if (subject==null)
      removeHeader(SUBJECT_NAME);
    try
    {
      setHeader(SUBJECT_NAME, MimeUtility.encodeText(subject, charset, null));
    }
    catch (UnsupportedEncodingException e)
    {
      throw new MessagingException("Encoding error", e);
    }
  }

  /**
   * Returns the value of the RFC 822 "Date" field. This is the date on which
   * this message was sent. Returns null if this field is unavailable or its
   * value is absent.
   * <p>
   * This implementation uses the <code>getHeader</code> method 
   * to obtain the requisite header field.
   */
  public Date getSentDate()
    throws MessagingException
  {
    String value = getHeader(DATE_NAME, null);
    if (value!=null)
    {
      try
      {
        return dateFormat.parse(value);
      }
      catch (ParseException e)
      {
      }
    }
    return null;
  }

  /**
   * Set the RFC 822 "Date" header field.
   * This is the date on which the creator of the message indicates that 
   * the message is complete and ready for delivery.
   * If the <code>date</code> parameter is null, the existing "Date" field 
   * is removed.
   * @param date the date value to set, or null to remove
   * @exception IllegalWriteException if the underlying implementation 
   * does not support modification of existing values
   * @exception IllegalStateException if this message is obtained from 
   * a READ_ONLY folder.
   */
  public void setSentDate(Date date)
    throws MessagingException
  {
    if (date==null)
      removeHeader(DATE_NAME);
    else
      setHeader(DATE_NAME, dateFormat.format(date));
  }

  /**
   * Returns the Date on this message was received.
   * Returns null if this date cannot be obtained.
   * <p>
   * Note that RFC 822 does not define a field for the received date.
   * Hence only implementations that can provide this date need return 
   * a valid value.
   */
  public Date getReceivedDate()
    throws MessagingException
  {
    // hence...
    return null;
  }

  /**
   * Return the size of the content of this message in bytes.
   * Return -1 if the size cannot be determined.
   * <p>
   * Note that this number may not be an exact measure of the content size 
   * and may or may not account for any transfer encoding of the content.
   * <p>
   * This implementation returns the size of the <code>content</code> array
   * (if not null), or, if <code>contentStream</code> is not null, and the 
   * <code>available</code> method returns a positive number, it returns 
   * that number as the size. Otherwise, it returns -1.
   */
  public int getSize()
    throws MessagingException
  {
    if (content!=null)
      return content.length;
    if (contentStream!=null)
    {
      try
      {
        int available = contentStream.available();
        if (available>0)
          return available;
      }
      catch (IOException e)
      {
      }
    }
    return -1;
  }

  /**
   * Return the number of lines for the content of this message.
   * Return -1 if this number cannot be determined.
   * <p>
   * Note that this number may not be an exact measure of the content length 
   * and may or may not account for any transfer encoding of the content.
   * <p>
   * This implementation returns -1.
   */
  public int getLineCount()
    throws MessagingException
  {
    return -1;
  }

  /**
   * Returns the value of the RFC 822 "Content-Type" header field.
   * This represents the content-type of the content of this message.
   * This value must not be null. If this field is unavailable, 
   * "text/plain" should be returned.
   * <p>
   * This implementation uses the <code>getHeader</code> method 
   * to obtain the requisite header field.
   */
  public String getContentType()
    throws MessagingException
  {
    String contentType = getHeader(MimeBodyPart.CONTENT_TYPE_NAME, null);
    if (contentType==null)
      return MimeBodyPart.TEXT_PLAIN;
    return contentType;
  }

  /**
   * Is this Part of the specified MIME type? This method compares only the
   * primaryType and subType.
   * The parameters of the content types are ignored.
   * <p>
   * For example, this method will return true when comparing a Part 
   * of content type "text/plain" with "text/plain; charset=foobar".
   * <p>
   * If the subType of mimeType is the special character '*', then 
   * the subtype is ignored during the comparison.
   * @see MimeBodyPart#isMimeType
   */
  public boolean isMimeType(String mimeType)
    throws MessagingException
  {
    return (new ContentType(getContentType()).match(mimeType));
  }

  /**
   * Returns the value of the "Content-Disposition" header field.
   * This represents the disposition of this part.
   * The disposition describes how the part should be presented to the user.
   * <p>
   * If the Content-Disposition field is unavailable, null is returned.
   * <p>
   * This implementation uses the <code>getHeader</code> method
   * to obtain the requisite header field.
   * @see MimeBodyPart#getDisposition
   */
  public String getDisposition()
    throws MessagingException
  {
    String disposition = 
      getHeader(MimeBodyPart.CONTENT_DISPOSITION_NAME, null);
    if (disposition!=null)
      return new ContentDisposition(disposition).getDisposition();
    return null;
  }

  /**
   * Set the "Content-Disposition" header field of this Message.
   * If <code>disposition</code> is null,
   * any existing "Content-Disposition" header field is removed.
   * @param disposition the disposition value to set, or null to remove
   * @exception IllegalWriteException if the underlying implementation 
   * does not support modification of existing values
   * @exception IllegalStateException if this message is obtained from 
   * a READ_ONLY folder.
   * @see MimeBodyPart#setDisposition
   */
  public void setDisposition(String disposition)
    throws MessagingException
  {
    if (disposition==null)
      removeHeader(MimeBodyPart.CONTENT_DISPOSITION_NAME);
    else
    {
      String value = getHeader(MimeBodyPart.CONTENT_DISPOSITION_NAME, null);
      if (value!=null)
      {
        ContentDisposition cd = new ContentDisposition(value);
        cd.setDisposition(disposition);
        disposition = cd.toString();
      }
      setHeader(MimeBodyPart.CONTENT_DISPOSITION_NAME, disposition);
    }
  }

  /**
   * Returns the content transfer encoding from the "Content-Transfer-Encoding"
   * header field.
   * Returns null if the header is unavailable or its value is absent.
   * <p>
   * This implementation uses the <code>getHeader</code> method 
   * to obtain the requisite header field.
   * @see MimeBodyPart#getEncoding
   */
  public String getEncoding()
    throws MessagingException
  {
    String encoding = 
      getHeader(MimeBodyPart.CONTENT_TRANSFER_ENCODING_NAME, null);
    if (encoding!=null)
    {
      encoding = encoding.trim();
      if (encoding.equalsIgnoreCase("7bit") || 
          encoding.equalsIgnoreCase("8bit") || 
          encoding.equalsIgnoreCase("quoted-printable") ||
          encoding.equalsIgnoreCase("base64"))
        return encoding;
      HeaderTokenizer ht = new HeaderTokenizer(encoding, HeaderTokenizer.MIME);
      for (boolean done = false; !done; )
      {
        HeaderTokenizer.Token token = ht.next();
        switch (token.getType())
        {
          case HeaderTokenizer.Token.EOF:
            done = true;
            break;
          case HeaderTokenizer.Token.ATOM:
            return token.getValue();
        }
      }
      return encoding;
    }
    return null;
  }

  /**
   * Returns the value of the "Content-ID" header field.
   * Returns null if the field is unavailable or its value is absent.
   * <p>
   * This implementation uses the <code>getHeader</code> method 
   * to obtain the requisite header field.
   * @see MimeBodyPart#getContentID
   */
  public String getContentID()
    throws MessagingException
  {
    return getHeader(MimeBodyPart.CONTENT_ID_NAME, null);
  }

  /**
   * Set the "Content-ID" header field of this Message.
   * If the cid parameter is null, any existing "Content-ID" is removed.
   * @param cid the content-id value to set, or null to remove
   * @exception IllegalWriteException if the underlying implementation 
   * does not support modification of existing values
   * @exception IllegalStateException if this message is obtained from 
   * a READ_ONLY folder.
   */
  public void setContentID(String cid)
    throws MessagingException
  {
    if (cid==null)
      removeHeader(MimeBodyPart.CONTENT_ID_NAME);
    else
      setHeader(MimeBodyPart.CONTENT_ID_NAME, cid);
  }

  /**
   * Return the value of the "Content-MD5" header field.
   * Returns null if this field is unavailable or its value is absent.
   * <p>
   * This implementation uses the <code>getHeader</code> method 
   * to obtain the requisite header field.
   * @see MimeBodyPart#getContentMD5
   */
  public String getContentMD5()
    throws MessagingException
  {
    return getHeader(MimeBodyPart.CONTENT_MD5_NAME, null);
  }

  /**
   * Set the "Content-MD5" header field of this Message.
   * @param md5 the content-md5 value to set
   * @exception IllegalWriteException if the underlying implementation 
   * does not support modification of existing values
   * @exception IllegalStateException if this message is obtained from 
   * a READ_ONLY folder.
   * @see MimeBodyPart#setContentMD5
   */
  public void setContentMD5(String md5)
    throws MessagingException
  {
    setHeader(MimeBodyPart.CONTENT_MD5_NAME, md5);
  }

  /**
   * Returns the "Content-Description" header field of this Message.
   * This typically associates some descriptive information with this part.
   * Returns null if this field is unavailable or its value is absent.
   * <p>
   * If the Content-Description field is encoded as per RFC 2047,
   * it is decoded and converted into Unicode.
   * If the decoding or conversion fails, the raw data is returned as-is.
   * <p>
   * This implementation uses the <code>getHeader</code> method 
   * to obtain the requisite header field.
   * @see MimeBodyPart#getDescription
   */
  public String getDescription()
    throws MessagingException
  {
    String header = getHeader(MimeBodyPart.CONTENT_DESCRIPTION_NAME, null);
    if (header!=null)
    {
      try
      {
        return MimeUtility.decodeText(header);
      }
      catch (UnsupportedEncodingException e)
      {
        return header;
      }
    }
    return null;
  }

  /**
   * Set the "Content-Description" header field for this Message.
   * If the <code>description</code> parameter is null,
   * then any existing "Content-Description" fields are removed.
   * <p>
   * If the description contains non US-ASCII characters, it will be encoded
   * using the platform's default charset. If the description contains only
   * US-ASCII characters, no encoding is done and it is used as-is.
   * <p>
   * Note that if the charset encoding process fails, a MessagingException is
   * thrown, and an UnsupportedEncodingException is included in the chain of
   * nested exceptions within the MessagingException.
   * @param description content-description
   * @exception IllegalWriteException if the underlying implementation 
   * does not support modification of existing values
   * @exception IllegalStateException if this message is obtained from 
   * a READ_ONLY folder.
   * @see MimeBodyPart#setDescription
   */
  public void setDescription(String description)
    throws MessagingException
  {
    setDescription(description, null);
  }

  /**
   * Set the "Content-Description" header field for this Message.
   * If the <code>description</code> parameter is null,
   * then any existing "Content-Description" fields are removed.
   * <p>
   * If the description contains non US-ASCII characters, it will be encoded
   * using the specified charset. If the description contains only
   * US-ASCII characters, no encoding is done and it is used as-is.
   * <p>
   * Note that if the charset encoding process fails, a MessagingException is
   * thrown, and an UnsupportedEncodingException is included in the chain of
   * nested exceptions within the MessagingException.
   * @param description content-description
   * @param charset the charset to use
   * @exception IllegalWriteException if the underlying implementation 
   * does not support modification of existing values
   * @exception IllegalStateException if this message is obtained from 
   * a READ_ONLY folder.
   * @see MimeBodyPart#setDescription
   */
  public void setDescription(String description, String charset)
    throws MessagingException
  {
    if (description!=null)
    {
      try
      {
        setHeader(MimeBodyPart.CONTENT_DESCRIPTION_NAME,
            MimeUtility.encodeText(description, charset, null));
      }
      catch (UnsupportedEncodingException e)
      {
        throw new MessagingException("Encode error", e);
      }
    }
    else
      removeHeader(MimeBodyPart.CONTENT_DESCRIPTION_NAME);
  }

  /**
   * Get the languages specified in the "Content-Language" header field 
   * of this message.
   * The Content-Language header is defined by RFC 1766.
   * Returns null if this field is unavailable or its value is absent.
   * <p>
   * This implementation uses the <code>getHeader</code> method 
   * to obtain the requisite header field.
   * @see MimeBodyPart#getContentLanguage
   */
  public String[] getContentLanguage()
    throws MessagingException
  {
    String header = getHeader(MimeBodyPart.CONTENT_LANGUAGE_NAME, null);
    if (header!=null)
    {
      HeaderTokenizer ht = new HeaderTokenizer(header, HeaderTokenizer.MIME);
      Vector acc = new Vector();
      for (boolean done = false; !done; )
      {
        HeaderTokenizer.Token token = ht.next();
        switch (token.getType())
        {
          case HeaderTokenizer.Token.EOF:
            done = true;
            break;
          case HeaderTokenizer.Token.ATOM:
            acc.addElement(token.getValue());
            break;
        }
      } 
      if (acc.size()>0)
      {
        String[] languages = new String[acc.size()];
        acc.copyInto(languages);
        return languages;
      }
    }
    return null;
  }

  /**
   * Set the "Content-Language" header of this MimePart.
   * The Content-Language header is defined by RFC 1766.
   * @param languages array of language tags
   * @exception IllegalWriteException if the underlying implementation 
   * does not support modification of existing values
   * @exception IllegalStateException if this message is obtained from 
   * a READ_ONLY folder.
   * @see MimeBodyPart#setContentLanguage
   */
  public void setContentLanguage(String[] languages)
    throws MessagingException
  {
    if (languages!=null && languages.length>0)
    {
      StringBuffer buffer = new StringBuffer();
      buffer.append(languages[0]);
      for (int i = 1; i<languages.length; i++)
      {
        buffer.append(',');
        buffer.append(languages[i]);
      }
      setHeader(MimeBodyPart.CONTENT_LANGUAGE_NAME, buffer.toString());
    }
    else
      setHeader(MimeBodyPart.CONTENT_LANGUAGE_NAME, null);
  }

  /**
   * Returns the value of the "Message-ID" header field.
   * Returns null if this field is unavailable or its value is absent.
   * <p>
   * This implementation uses the <code>getHeader</code> method 
   * to obtain the requisite header field.
   */
  public String getMessageID()
    throws MessagingException
  {
    return getHeader(MESSAGE_ID_NAME, null);
  }

  /**
   * Get the filename associated with this Message.
   * <p>
   * Returns the value of the "filename" parameter from the
   * "Content-Disposition" header field of this message.
   * If it's not available, returns the value of the "name" parameter 
   * from the "Content-Type" header field of this BodyPart.
   * Returns null if both are absent.
   * @see MimeBodyPart#getFileName
   */
  public String getFileName()
    throws MessagingException
  {
    String filename = null;
    String header = getHeader(MimeBodyPart.CONTENT_DISPOSITION_NAME, null);
    if (header!=null)
    {
      ContentDisposition cd = new ContentDisposition(header);
      filename = cd.getParameter("filename");
    }
    if (filename==null)
    {
      header = getHeader(MimeBodyPart.CONTENT_TYPE_NAME, null);
      if (header!=null)
      {
        ContentType contentType = new ContentType(header);
        filename = contentType.getParameter("name");
      }
    }
    return filename;
  }

  /**
   * Set the filename associated with this part, if possible.
   * <p>
   * Sets the "filename" parameter of the "Content-Disposition" 
   * header field of this message.
   * @exception IllegalWriteException if the underlying implementation 
   * does not support modification of existing values
   * @exception IllegalStateException if this message is obtained from 
   * a READ_ONLY folder.
   * @see MimeBodyPart#setFileName
   */
  public void setFileName(String filename)
    throws MessagingException
  {
    String header = getHeader(MimeBodyPart.CONTENT_DISPOSITION_NAME, null);
    if (header==null)
      header = "attachment";
    ContentDisposition cd = new ContentDisposition(header);
    cd.setParameter("filename", filename);
    setHeader(MimeBodyPart.CONTENT_DISPOSITION_NAME, cd.toString());

    // We will also set the "name" parameter of the Content-Type field
    // to preserve compatibility with nonconformant MUAs
    header = getContentType(); // not valid for this to be null
    ContentType contentType = new ContentType(header);
    contentType.setParameter("name", filename);
    setHeader(MimeBodyPart.CONTENT_TYPE_NAME, contentType.toString());
  }

  /**
   * Return a decoded input stream for this Message's "content".
   * <p>
   * This implementation obtains the input stream from the DataHandler,
   * that is, it invokes <code>getDataHandler().getInputStream()</code>.
   * @exception IOException this is typically thrown by the DataHandler.
   * Refer to the documentation for javax.activation.DataHandler for more
   * details.
   * @see MimeBodyPart#getInputStream
   */
  public InputStream getInputStream()
    throws IOException, MessagingException
  {
    return getDataHandler().getInputStream();
  }

  /**
   * Produce the raw bytes of the content.
   * This method is used during parsing, to create a DataHandler object 
   * for the content. Subclasses that can provide a separate input stream 
   * for just the message content might want to override this method.
   * <p>
   * This implementation just returns a ByteArrayInputStream constructed 
   * out of the content byte array.
   * @see MimeBodyPart#getContentStream
   */
  protected InputStream getContentStream()
    throws MessagingException
  {
    if (contentStream!=null)
      return ((SharedInputStream)contentStream).newStream(0L, -1L);
    if (content!=null)
      return new ByteArrayInputStream(content);
    else
      throw new MessagingException("No content");
  }

  /**
   * Return an InputStream to the raw data with any Content-Transfer-Encoding
   * intact.
   * This method is useful if the "Content-Transfer-Encoding" header is
   * incorrect or corrupt, which would prevent the <code>getInputStream</code>
   * method or <code>getContent</code> method from returning the correct data.
   * In such a case the application may use this method and attempt to decode
   * the raw data itself.
   * <p>
   * This implementation simply calls the <code>getContentStream</code>
   * method.
   * @see MimeBodyPart#getRawInputStream
   */
  public InputStream getRawInputStream()
    throws MessagingException
  {
    return getContentStream();
  }

  /**
   * Return a DataHandler for this Message's content.
   * <p>
   * The implementation provided here works as follows. Note the use of the
   * <code>getContentStream</code> method to generate the byte stream for 
   * the content. Also note that any transfer-decoding is done automatically
   * within this method.
   * <pre>
    getDataHandler() {
        if (dh == null) {
            dh = new DataHandler(new MimePartDataSource(this));
        }
        return dh;
    }

    class MimePartDataSource implements DataSource {
        public getInputStream() {
            return MimeUtility.decode(
               getContentStream(), getEncoding());
        }

          ....
    }
    </pre>
   */
  public synchronized DataHandler getDataHandler()
    throws MessagingException
  {
    if (dh==null)
      dh = new DataHandler(new MimePartDataSource(this));
    return dh;
  }

  /**
   * Return the content as a Java object.
   * The type of this object is dependent on the content itself.
   * For example, the native format of a "text/plain" content is usually 
   * a String object. The native format for a "multipart" message is always 
   * a Multipart subclass. For content types that are unknown to the 
   * DataHandler system, an input stream is returned as the content.
   * <p>
   * This implementation obtains the content from the DataHandler,
   * that is, it invokes <code>getDataHandler().getContent()</code>.
   * @exception IOException this is typically thrown by the DataHandler.
   * Refer to the documentation for javax.activation.DataHandler for more
   * details.
   */
  public Object getContent()
    throws IOException, MessagingException
  {
    return getDataHandler().getContent();
  }

  /**
   * This method provides the mechanism to set this part's content.
   * The given DataHandler object should wrap the actual content.
   * @param dh The DataHandler for the content.
   * @exception IllegalWriteException if the underlying implementation 
   * does not support modification of existing values
   * @exception IllegalStateException if this message is obtained from 
   * a READ_ONLY folder.
   * @see MimeBodyPart#setDataHandler
   */
  public void setDataHandler(DataHandler datahandler)
    throws MessagingException
  {
    dh = datahandler;
    // The Content-Type and Content-Transfer-Encoding headers may need to be
    // recalculated by the new DataHandler - see updateHeaders()
    removeHeader(MimeBodyPart.CONTENT_TYPE_NAME);
    removeHeader(MimeBodyPart.CONTENT_TRANSFER_ENCODING_NAME);
  }

  /**
   * A convenience method for setting this Message's content.
   * <p>
   * The content is wrapped in a DataHandler object. Note that a
   * DataContentHandler class for the specified type should be available 
   * to the JavaMail implementation for this to work right. i.e., to do
   * <code>setContent(foobar, "application/x-foobar")</code>, a 
   * DataContentHandler for "application/x-foobar" should be installed.
   * Refer to the Java Activation Framework for more information.
   * @param o the content object
   * @param type Mime type of the object
   * @exception IllegalWriteException if the underlying implementation 
   * does not support modification of existing values
   * @exception IllegalStateException if this message is obtained from 
   * a READ_ONLY folder.
   * @see MimeBodyPart#setContent
   */
  public void setContent(Object o, String type)
    throws MessagingException
  {
    setDataHandler(new DataHandler(o, type));
  }

  /**
   * Convenience method that sets the given String as this part's content,
   * with a MIME type of "text/plain".
   * If the string contains non US-ASCII characters, it will be encoded 
   * using the platform's default charset. The charset is also used to set 
   * the "charset" parameter.
   * <p>
   * Note that there may be a performance penalty if text is large, since 
   * this method may have to scan all the characters to determine what 
   * charset to use.
   * <p>
   * If the charset is already known, use the <code>setText</code> method
   * that takes the <code>charset</code> parameter.
   * @see MimeBodyPart#setText
   */
  public void setText(String text)
    throws MessagingException
  {
    setText(text, null);
  }

  /**
   * Convenience method that sets the given String as this part's content,
   * with a MIME type of "text/plain" and the specified charset.
   * The given Unicode string will be charset-encoded using the specified 
   * charset. The charset is also used to set the "charset" parameter.
   */
  public void setText(String text, String charset)
    throws MessagingException
  {
    if (charset==null)
    {
      // According to the API doc for getText(String), we may have to scan
      // the characters to determine the charset.
      // However this should work just as well and is hopefully relatively
      // cheap.
      charset = MimeUtility.mimeCharset(MimeUtility.getDefaultJavaCharset());
    }
    StringBuffer buffer = new StringBuffer();
    buffer.append("text/plain; charset=");
    buffer.append(MimeUtility.quote(charset, HeaderTokenizer.MIME));
    setContent(text, buffer.toString());
  }

  /**
   * This method sets the Message's content to a Multipart object.
   * @param mp The multipart object that is the Message's content
   * @exception IllegalWriteException if the underlying implementation 
   * does not support modification of existing values
   * @exception IllegalStateException if this message is obtained from 
   * a READ_ONLY folder.
   * @see MimeBodyPart#setContent(Multipart)
   */
  public void setContent(Multipart mp)
    throws MessagingException
  {
    setDataHandler(new DataHandler(mp, mp.getContentType()));
    // Ensure component hierarchy
    mp.setParent(this);
  }

  /**
   * Get a new Message suitable for a reply to this message.
   * The new Message will have its attributes and headers set up 
   * appropriately. Note that this new message object will be empty, 
   * i.e., it will not have a "content". These will have to be suitably 
   * filled in by the client.
   * <p>
   * If <code>replyToAll</code> is set, the new Message will be addressed 
   * to all recipients of this message. Otherwise, the reply will be 
   * addressed to only the sender of this message (using the value of 
   * the <code>getReplyTo</code> method).
   * <p>
   * The "Subject" field is filled in with the original subject prefixed 
   * with "Re:" (unless it already starts with "Re:"). The "In-Reply-To"
   * header is set in the new message if this message has a "Message-Id"
   * header. The ANSWERED flag is set in this message.
   * @param replyToAll reply should be sent to all recipients of this message
   * @return the reply Message
   */
  public Message reply(boolean replyToAll)
    throws MessagingException
  {
    MimeMessage message = new MimeMessage(session);
    String subject = getHeader(SUBJECT_NAME, null);
    if (subject!=null)
    {
      if (!subject.startsWith("Re: "))
        subject = "Re: "+subject;
      message.setHeader(SUBJECT_NAME, subject);
    }
    Address[] addresses = getReplyTo();
    message.setRecipients(Message.RecipientType.TO, addresses);
    if (replyToAll)
    {
      Vector vector = new Vector();
      addToSet(vector, addresses);

      InternetAddress localAddress = InternetAddress.getLocalAddress(session);
      if (localAddress!=null)
        vector.addElement(localAddress);
      String alternates = session.getProperty("mail.alternates");
      if (alternates!=null)
        addToSet(vector, InternetAddress.parse(alternates, false));
      
      addToSet(vector, getRecipients(Message.RecipientType.TO));
      addresses = new Address[vector.size()];
      vector.copyInto(addresses);
      boolean replyAllCC = 
        new Boolean(session.getProperty("mail.replyallcc")).booleanValue();
      if (addresses.length>0)
        if (replyAllCC)
          message.addRecipients(Message.RecipientType.CC, addresses);
        else
          message.addRecipients(Message.RecipientType.TO, addresses);
      
      vector.clear();
      addToSet(vector, getRecipients(Message.RecipientType.CC));
      addresses = new Address[vector.size()];
      vector.copyInto(addresses);
      if (addresses!=null && addresses.length>0)
        message.addRecipients(Message.RecipientType.CC, addresses);
      
      addresses = getRecipients(RecipientType.NEWSGROUPS);
      if (addresses!=null && addresses.length>0)
        message.setRecipients(RecipientType.NEWSGROUPS, addresses);
    }

    // Set In-Reply-To (will be replaced by References for NNTP)
    String mid = getHeader(MESSAGE_ID_NAME, null);
    if (mid!=null)
      message.setHeader("In-Reply-To", mid);
    try
    {
      setFlag(Flags.Flag.ANSWERED, true);
    }
    catch (MessagingException e)
    {
    }
    return message;
  }

  /*
   * Convenience method to add an array of addresses to a set.
   * Since we are targeting Java 1.1 and do not have access to the
   * Collections framework, we cannot simply use a Set object.
   */
  private static void addToSet(Vector vector, Address[] addresses)
  {
    if (addresses!=null)
    {
      for (int i = 0; i<addresses.length; i++)
      {
        if (!vector.contains(addresses[i]))
          vector.addElement(addresses[i]);
      }
    }
  }

  /**
   * Output the message as an RFC 822 format stream.
   * <p>
   * Note that, depending on how the message was constructed, it may use a
   * variety of line termination conventions. Generally the output should be
   * sent through an appropriate FilterOutputStream that converts the line
   * terminators to the desired form, either CRLF for MIME compatibility and 
   * for use in Internet protocols, or the local platform's line terminator 
   * for storage in a local text file.
   * <p>
   * This implementation calls the 
   * <code>writeTo(OutputStream, String[])</code> method with a null ignore 
   * list.
   * @exception IOException if an error occurs writing to the stream or if an
   * error is generated by the javax.activation layer.
   */
  public void writeTo(OutputStream os)
    throws IOException, MessagingException
  {
    writeTo(os, null);
  }

  /**
   * Output the message as an RFC 822 format stream, without specified 
   * headers. If the saved flag is not set, the <code>saveChanges</code>
   * method is called. If the <code>modified</code> flag is not set and 
   * the <code>content</code> array is not null, the <code>content</code>
   * array is written directly, after writing the appropriate message headers.
   * @exception IOException if an error occurs writing to the stream or if an
   * error is generated by the javax.activation layer.
   */
  public void writeTo(OutputStream os, String[] ignoreList)
    throws IOException, MessagingException
  {
    if (!saved)
      saveChanges();

    // Wrap in a CRLFOutputStream
    CRLFOutputStream crlfos = null;
    if (os instanceof CRLFOutputStream)
      crlfos = (CRLFOutputStream)os;
    else
      crlfos = new CRLFOutputStream(os);

    // Write the headers
    for (Enumeration e = getNonMatchingHeaderLines(ignoreList);
        e.hasMoreElements(); )
    {
      crlfos.write((String)e.nextElement());
      crlfos.writeln();
    }
    crlfos.writeln();
    crlfos.flush();

    if (modified || content==null && contentStream==null)
    {
      // use datahandler
      os = MimeUtility.encode(os, getEncoding());
      getDataHandler().writeTo(os);
    }
    else
    {
      // write content directly
      if (contentStream!=null)
      {
        InputStream is = ((SharedInputStream)contentStream).newStream(0L, -1L);
        // TODO make buffer size configurable
        int len = 8192;
        byte[] bytes = new byte[len];
        while ((len = is.read(bytes))>-1) 
          os.write(bytes, 0, len);
        is.close();
      }
      else
        os.write(content);
    }
    os.flush();
  }

  /**
   * Get all the headers for this header_name.
   * Note that certain headers may be encoded as per RFC 2047 if they 
   * contain non US-ASCII characters and these should be decoded.
   * <p>
   * This implementation obtains the headers from the <code>headers</code>
   * InternetHeaders object.
   * @param name name of header
   * @return array of headers
   */
  public String[] getHeader(String name)
    throws MessagingException
  {
    return headers.getHeader(name);
  }

  /**
   * Get all the headers for this header name, returned as a single String,
   * with headers separated by the delimiter.
   * If the delimiter is null, only the first header is returned.
   * @param name the name of the header
   * @param delimiter the delimiter
   * @return the value fields for all headers with this name
   */
  public String getHeader(String name, String delimiter)
    throws MessagingException
  {
    return headers.getHeader(name, delimiter);
  }

  /**
   * Set the value for this header_name.
   * Replaces all existing header values with this new value.
   * Note that RFC 822 headers must contain only US-ASCII characters,
   * so a header that contains non US-ASCII characters must have been 
   * encoded by the caller as per the rules of RFC 2047.
   * @param name header name
   * @param value header value
   * @exception IllegalWriteException if the underlying implementation 
   * does not support modification of existing values
   * @exception IllegalStateException if this message is obtained from 
   * a READ_ONLY folder.
   */
  public void setHeader(String name, String value)
    throws MessagingException
  {
    headers.setHeader(name, value);
  }

  /**
   * Add this value to the existing values for this header_name.
   * Note that RFC 822 headers must contain only US-ASCII characters,
   * so a header that contains non US-ASCII characters must have been
   * encoded as per the rules of RFC 2047.
   * @param name header name
   * @param value header value
   * @exception IllegalWriteException if the underlying implementation 
   * does not support modification of existing values
   * @exception IllegalStateException if this message is obtained from 
   * a READ_ONLY folder.
   */
  public void addHeader(String name, String value)
    throws MessagingException
  {
    headers.addHeader(name, value);
  }

  /**
   * Remove all headers with this name.
   * @param name header name
   * @exception IllegalWriteException if the underlying implementation 
   * does not support modification of existing values
   * @exception IllegalStateException if this message is obtained from 
   * a READ_ONLY folder.
   */
  public void removeHeader(String name)
    throws MessagingException
  {
    headers.removeHeader(name);
  }

  /**
   * Return all the headers from this Message as an enumeration of Header
   * objects.
   * <p>
   * Note that certain headers may be encoded as per RFC 2047 if they contain
   * non US-ASCII characters and these should be decoded.
   * <p>
   * This implementation obtains the headers from the <code>headers</code>
   * InternetHeaders object.
   * @return array of header objects
   */
  public Enumeration getAllHeaders()
    throws MessagingException
  {
    return headers.getAllHeaders();
  }

  /**
   * Return matching headers from this Message as an Enumeration of Header
   * objects.
   * <p>
   * This implementation obtains the headers from the <code>headers</code>
   * InternetHeaders object.
   */
  public Enumeration getMatchingHeaders(String[] names)
    throws MessagingException
  {
    return headers.getMatchingHeaders(names);
  }

  /**
   * Return non-matching headers from this Message as an Enumeration of Header
   * objects.
   * <p>
   * This implementation obtains the headers from the <code>headers</code>
   * InternetHeaders object.
   */
  public Enumeration getNonMatchingHeaders(String[] names)
    throws MessagingException
  {
    return headers.getNonMatchingHeaders(names);
  }

  /**
   * Add a raw RFC 822 header-line.
   * @param line the line to add
   * @exception IllegalWriteException if the underlying implementation 
   * does not support modification of existing values
   * @exception IllegalStateException if this message is obtained from 
   * a READ_ONLY folder.
   */
  public void addHeaderLine(String line)
    throws MessagingException
  {
    headers.addHeaderLine(line);
  }

  /**
   * Get all header lines as an Enumeration of Strings.
   * A Header line is a raw RFC 822 header-line, containing both the "name"
   * and "value" field.
   */
  public Enumeration getAllHeaderLines()
    throws MessagingException
  {
    return headers.getAllHeaderLines();
  }

  /**
   * Get matching header lines as an Enumeration of Strings.
   * A Header line is a raw RFC 822 header-line, containing both the "name"
   * and "value" field.
   */
  public Enumeration getMatchingHeaderLines(String[] names)
    throws MessagingException
  {
    return headers.getMatchingHeaderLines(names);
  }

  /**
   * Get non-matching header lines as an Enumeration of Strings.
   * A Header line is a raw RFC 822 header-line, containing both the "name"
   * and "value" field.
   */
  public Enumeration getNonMatchingHeaderLines(String[] names)
    throws MessagingException
  {
    return headers.getNonMatchingHeaderLines(names);
  }

  /**
   * Return a Flags object containing the flags for this message.
   * <p>
   * Note that a clone of the internal Flags object is returned, so modifying
   * the returned Flags object will not affect the flags of this message.
   * @return Flags object containing the flags for this message
   */
  public Flags getFlags()
    throws MessagingException
  {
    return (Flags)flags.clone();
  }

  /**
   * Check whether the flag specified in the flag argument is set in this
   * message.
   * <p>
   * This implementation checks this message's internal flags object.
   * @param flag - the flag
   * @return value of the specified flag for this message
   */
  public boolean isSet(Flags.Flag flag)
    throws MessagingException
  {
    return flags.contains(flag);
  }

  /**
   * Set the flags for this message.
   * <p>
   * This implementation modifies the flags field.
   * @exception IllegalWriteException if the underlying implementation 
   * does not support modification of existing values
   * @exception IllegalStateException if this message is obtained from 
   * a READ_ONLY folder.
   */
  public void setFlags(Flags flag, boolean set)
    throws MessagingException
  {
    if (set)
      flags.add(flag);
    else
      flags.remove(flag);
  }

  /**
   * Updates the appropriate header fields of this message to be consistent
   * with the message's contents.
   * If this message is contained in a Folder, any changes made to this 
   * message are committed to the containing folder.
   * <p>
   * If any part of a message's headers or contents are changed, 
   * <code>saveChanges</code> must be called to ensure that those changes 
   * are permanent. Otherwise, any such modifications may or may not be 
   * saved, depending on the folder implementation.
   * <p>
   * Messages obtained from folders opened READ_ONLY should not be modified 
   * and <code>saveChanges</code> should not be called on such messages.
   * <p>
   * This method sets the <code>modified</code> flag to true,
   * the <code>save</code> flag to true, and then calls the 
   * <code>updateHeaders</code> method.
   * @exception IllegalWriteException if the underlying implementation 
   * does not support modification of existing values
   * @exception IllegalStateException if this message is obtained from 
   * a READ_ONLY folder.
   */
  public void saveChanges()
    throws MessagingException
  {
    modified = true;
    saved = true;
    updateHeaders();
  }

  /**
   * Called by the <code>saveChanges</code> method to actually update 
   * the MIME headers.
   * The implementation here sets the Content-Transfer-Encoding header
   * (if needed and not already set), the Mime-Version header
   * and the Message-ID header.
   * Also, if the content of this message is a MimeMultipart,
   * its <code>updateHeaders</code> method is called.
   * @exception IllegalWriteException if the underlying implementation 
   * does not support modification of existing values
   * @exception IllegalStateException if this message is obtained from 
   * a READ_ONLY folder.
   * @see MimeBodyPart#updateHeaders
   */
  protected void updateHeaders()
    throws MessagingException
  {
    // This code is from MimeBodyPart
    if (getDataHandler()!=null)
    {
      try
      {
        String contentType = dh.getContentType();
        ContentType ct = new ContentType(contentType);
        if (ct.match("multipart/*"))
        {
          MimeMultipart mmp = (MimeMultipart)dh.getContent();
          mmp.updateHeaders();
        } 
        else if (ct.match("message/rfc822"))
        {
        }
        else
        {
          // Update Content-Transfer-Encoding
          if (getHeader(MimeBodyPart.CONTENT_TRANSFER_ENCODING_NAME)==null)
          {
            setHeader(MimeBodyPart.CONTENT_TRANSFER_ENCODING_NAME,
                MimeUtility.getEncoding(dh));
          }
        }

        // Update Content-Type if nonexistent,
        // and Content-Type "name" with Content-Disposition "filename"
        // parameter (see setFilename())
        if (getHeader(MimeBodyPart.CONTENT_TYPE_NAME)==null)
        {
          String disposition =
            getHeader(MimeBodyPart.CONTENT_DISPOSITION_NAME, null);
          if (disposition!=null)
          {
            ContentDisposition cd = new ContentDisposition(disposition);
            String filename = cd.getParameter("filename");
            if (filename!=null)
            {
              ct.setParameter("name", filename);
              contentType = ct.toString();
            }
          }
          setHeader(MimeBodyPart.CONTENT_TYPE_NAME, contentType);
        }
      }
      catch (IOException e)
      {
        throw new MessagingException("I/O error", e);
      }
    }
    
    // Below is MimeMessage-specific.
    // set mime version
    setHeader("Mime-Version", "1.0");
    // set new message-id if necessary
    String mid = getHeader(MESSAGE_ID_NAME, null);
    if (mid==null)
    {
      StringBuffer buffer = new StringBuffer();
      buffer.append('<');
      buffer.append(MimeUtility.getUniqueMessageIDValue(session));
      buffer.append('>');
      mid = buffer.toString();
      setHeader(MESSAGE_ID_NAME, mid);
    }
  }

  /**
   * Create and return an InternetHeaders object that loads the headers 
   * from the given InputStream.
   * Subclasses can override this method to return a subclass of
   * InternetHeaders, if necessary.
   * This implementation simply constructs and returns an InternetHeaders
   * object.
   * @param is the InputStream to read the headers from
   */
  protected InternetHeaders createInternetHeaders(InputStream is)
    throws MessagingException
  {
    return new InternetHeaders(is);
  }

}
