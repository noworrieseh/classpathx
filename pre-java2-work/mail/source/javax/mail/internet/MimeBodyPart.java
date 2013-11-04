/*
 * MimeBodyPart.java
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
import java.util.ArrayList;
import java.util.Enumeration;
import javax.activation.DataHandler;
import javax.mail.*;
import gnu.mail.util.CRLFOutputStream;

/**
 * This class represents a MIME body part.
 * It implements the BodyPart abstract class and the MimePart interface.
 * MimeBodyParts are contained in MimeMultipart objects.
 * <p>
 * MimeBodyPart uses the InternetHeaders class to parse and store 
 * the headers of that body part.
 * <p>
 * <hr>
 * A note on RFC 822 and MIME headers
 * <p>
 * RFC 822 header fields must contain only US-ASCII characters.
 * MIME allows non ASCII characters to be present in certain portions
 * of certain headers, by encoding those characters.
 * RFC 2047 specifies the rules for doing this.
 * The MimeUtility class provided in this package can be used to achieve this.
 * Callers of the <code>setHeader</code>, <code>addHeader</code>, and 
 * <code>addHeaderLine</code> methods are responsible for enforcing the 
 * MIME requirements for the specified headers. 
 * In addition, these header fields must be folded (wrapped) before being 
 * sent if they exceed the line length limitation for the transport
 * (1000 bytes for SMTP).
 * Received headers may have been folded.
 * The application is responsible for folding and unfolding headers as 
 * appropriate.
 */
public class MimeBodyPart
  extends BodyPart
  implements MimePart
{

  /**
   * The DataHandler object representing this Part's content.
   */
  protected DataHandler dh;

  /**
   * Byte array that holds the bytes of the content of this Part.
   */
  protected byte[] content;

  /**
   * If the data for this body part was supplied by an InputStream that
   * implements the SharedInputStream interface, contentStream is another 
   * such stream representing the content of this body part.
   * In this case, content will be null.
   */
  protected InputStream contentStream;

  /**
   * The InternetHeaders object that stores all the headers of this body part.
   */
  protected InternetHeaders headers;

  /*
   * These constants are also referenced by MimeMessage.
   */
  static final String CONTENT_TYPE_NAME = "Content-Type";
  static final String CONTENT_DISPOSITION_NAME = "Content-Disposition";
  static final String CONTENT_TRANSFER_ENCODING_NAME =
    "Content-Transfer-Encoding";
  static final String CONTENT_ID_NAME = "Content-ID";
  static final String CONTENT_MD5_NAME = "Content-MD5";
  static final String CONTENT_LANGUAGE_NAME = "Content-Language";
  static final String CONTENT_DESCRIPTION_NAME = "Content-Description";
  
  static final String TEXT_PLAIN = "text/plain";

  /**
   * An empty MimeBodyPart object is created.
   * This body part maybe filled in by a client 
   * constructing a multipart message.
   */
  public MimeBodyPart()
  {
    headers = new InternetHeaders();
  }

  /**
   * Constructs a MimeBodyPart by reading and parsing the data from the
   * specified input stream.
   * The parser consumes data till the end of the given input stream.
   * The input stream must start at the beginning of a valid MIME body part 
   * and must terminate at the end of that body part.
   * <p>
   * Note that the "boundary" string that delimits body parts must not be
   * included in the input stream. The intention is that the MimeMultipart
   * parser will extract each body part's bytes from a multipart stream and feed
   * them into this constructor, without the delimiter strings.
   * @param is the body part Input Stream
   */
  public MimeBodyPart(InputStream is)
    throws MessagingException
  {
    // Buffer the stream if necessary
    if (!(is instanceof ByteArrayInputStream) &&
        !(is instanceof BufferedInputStream))
      is = new BufferedInputStream(is);
    // Read the headers
    headers = new InternetHeaders(is);
    
    if (is instanceof SharedInputStream)
    {
      SharedInputStream sis = (SharedInputStream)is;
      contentStream = sis.newStream(sis.getPosition(), -1L);
      return;
    }
    
    // Read stream into byte array (see MimeMessage.parse())
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

  /**
   * Constructs a MimeBodyPart using the given header and content bytes.
   * <p>
   * Used by providers.
   * @param headers The header of this part
   * @param content bytes representing the body of this part.
   */
  public MimeBodyPart(InternetHeaders headers, byte[] content)
    throws MessagingException
  {
    this.headers = headers;
    this.content = content;
  }

  /**
   * Return the size of the content of this body part in bytes.
   * Return -1 if the size cannot be determined.
   * <p>
   * Note that this number may not be an exact measure of the content size and
   * may or may not account for any transfer encoding of the content.
   * <p>
   * This implementation returns the size of the content array (if not null),
   * or, if contentStream is not null, and the available method returns a
   * positive number, it returns that number as the size. Otherwise, it 
   * returns -1.
   * @return size in bytes, or -1 if not known
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
        int len = contentStream.available();
        if (len>0)
          return len;
      }
      catch (IOException e)
      {
      }
    }
    return -1;
  }

  /**
   * Return the number of lines for the content of this Part.
   * Return -1 if this number cannot be determined.
   * <p>
   * Note that this number may not be an exact measure of the content length 
   * and may or may not account for any transfer encoding of the content.
   * <p>
   * This implementation returns -1.
   * @return number of lines, or -1 if not known
   */
  public int getLineCount()
    throws MessagingException
  {
    return -1;
  }

  /**
   * Returns the value of the RFC 822 "Content-Type" header field.
   * This represents the content type of the content of this body part.
   * This value must not be null.
   * If this field is unavailable, "text/plain" should be returned.
   * <p>
   * This implementation uses <code>getHeader(name)</code> to obtain 
   * the requisite header field.
   * @return Content-Type of this body part
   */
  public String getContentType()
    throws MessagingException
  {
    String contentType = getHeader(CONTENT_TYPE_NAME, null);
    if (contentType==null)
      contentType = TEXT_PLAIN;
    return contentType;
  }

  /**
   * Is this Part of the specified MIME type?
   * This method compares only the primaryType and subType.
   * The parameters of the content types are ignored.
   * <p>
   * For example, this method will return true when comparing a Part 
   * of content type "text/plain" with "text/plain; charset=foobar".
   * <p>
   * If the subType of <code>mimeType</code> is the special character '*',
   * then the subtype is ignored during the comparison.
   */
  public boolean isMimeType(String mimeType)
    throws MessagingException
  {
    String contentType = getContentType();
    try
    {
      return (new ContentType(contentType).match(mimeType));
    }
    catch (ParseException e)
    {
      return (getContentType().equalsIgnoreCase(mimeType));
    }
  }

  /**
   * Returns the value of the "Content-Disposition" header field.
   * This represents the disposition of this part.
   * The disposition describes how the part should be presented to the user.
   * <p>
   * If the Content-Disposition field is unavailable, null is returned.
   * <p>
   * This implementation uses <code>getHeader(name)</code> to obtain the 
   * requisite header field.
   */
  public String getDisposition()
    throws MessagingException
  {
    String disposition = getHeader(CONTENT_DISPOSITION_NAME, null);
    if (disposition!=null)
      return new ContentDisposition(disposition).getDisposition();
    return null;
  }

  /**
   * Set the "Content-Disposition" header field of this body part.
   * If the disposition is null, any existing "Content-Disposition" 
   * header field is removed.
   * @exception IllegalWriteException if the underlying implementation 
   * does not support modification
   * @exception IllegalStateException if this body part is obtained 
   * from a READ_ONLY folder.
   */
  public void setDisposition(String disposition)
    throws MessagingException
  {
    if (disposition==null)
      removeHeader(CONTENT_DISPOSITION_NAME);
    else
    {
      String value = getHeader(CONTENT_DISPOSITION_NAME, null);
      if (value!=null)
      {
        ContentDisposition cd = new ContentDisposition(value);
        cd.setDisposition(disposition);
        disposition = cd.toString();
      }
      setHeader(CONTENT_DISPOSITION_NAME, disposition);
    }
  }

  /**
   * Returns the content transfer encoding from the 
   * "Content-Transfer-Encoding" header field.
   * Returns null if the header is unavailable or its value is absent.
   * <p>
   * This implementation uses <code>getHeader(name)</code> to obtain 
   * the requisite header field.
   */
  public String getEncoding()
    throws MessagingException
  {
    String encoding = getHeader(CONTENT_TRANSFER_ENCODING_NAME, null);
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
   * This implementation uses <code>getHeader(name)</code> to obtain 
   * the requisite header field.
   */
  public String getContentID()
    throws MessagingException
  {
    return getHeader(CONTENT_ID_NAME, null);
  }

  /**
   * Returns the value of the "Content-MD5" header field.
   * Returns null if the field is unavailable or its value is absent.
   * <p>
   * This implementation uses <code>getHeader(name)</code> to obtain 
   * the requisite header field.
   */
  public String getContentMD5()
    throws MessagingException
  {
    return getHeader(CONTENT_MD5_NAME, null);
  }

  /**
   * Set the "Content-MD5" header field of this body part.
   * @exception IllegalWriteException if the underlying implementation 
   * does not support modification
   * @exception IllegalStateException if this body part is obtained 
   * from a READ_ONLY folder.
   */
  public void setContentMD5(String md5)
    throws MessagingException
  {
    setHeader(CONTENT_MD5_NAME, md5);
  }

  /**
   * Get the languages specified in the Content-Language header of this
   * MimePart.
   * The Content-Language header is defined by RFC 1766. Returns null if 
   * this header is not available or its value is absent.
   * <p>
   * This implementation uses <code>getHeader(name)</code> to obtain 
   * the requisite header field.
   */
  public String[] getContentLanguage()
    throws MessagingException
  {
    String header = getHeader(CONTENT_LANGUAGE_NAME, null);
    if (header!=null)
    {
      HeaderTokenizer ht = new HeaderTokenizer(header, HeaderTokenizer.MIME);
      ArrayList acc = new ArrayList();
      for (boolean done = false; !done; )
      {
        HeaderTokenizer.Token token = ht.next();
        switch (token.getType())
        {
          case HeaderTokenizer.Token.EOF:
            done = true;
            break;
          case HeaderTokenizer.Token.ATOM:
            acc.add(token.getValue());
            break;
        }
      } 
      if (acc.size()>0)
      {
        String[] languages = new String[acc.size()];
        acc.toArray(languages);
        return languages;
      }
    }
    return null;
  }

  /**
   * Set the Content-Language header of this MimePart.
   * The Content-Language header is defined by RFC 1766.
   * @param languages array of language tags
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
      setHeader(CONTENT_LANGUAGE_NAME, buffer.toString());
    }
    else
      setHeader(CONTENT_LANGUAGE_NAME, null);
  }

  /**
   * Returns the "Content-Description" header field of this body part.
   * This typically associates some descriptive information with this part.
   * Returns null if this field is unavailable or its value is absent.
   * <p>
   * If the Content-Description field is encoded as per RFC 2047,
   * it is decoded and converted into Unicode.
   * If the decoding or conversion fails, the raw data is returned as is.
   * <p>
   * This implementation uses <code>getHeader(name)</code> to obtain 
   * the requisite header field.
   */
  public String getDescription()
    throws MessagingException
  {
    String header = getHeader(CONTENT_DESCRIPTION_NAME, null);
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
   * Set the "Content-Description" header field for this body part.
   * If the description parameter is null, then any existing
   * "Content-Description" fields are removed.
   * <p>
   * If the description contains non US-ASCII characters, it will be encoded
   * using the platform's default charset. If the description contains only
   * US-ASCII characters, no encoding is done and it is used as is.
   * <p>
   * Note that if the charset encoding process fails, a MessagingException is
   * thrown, and an UnsupportedEncodingException is included in the chain of
   * nested exceptions within the MessagingException.
   * @param description content description
   * @exception IllegalWriteException if the underlying implementation 
   * does not support modification
   * @exception IllegalStateException if this body part is obtained 
   * from a READ_ONLY folder.
   */
  public void setDescription(String description)
    throws MessagingException
  {
    setDescription(description, null);
  }

  /**
   * Set the "Content-Description" header field for this body part.
   * If the description parameter is null, then any existing
   * "Content-Description" fields are removed.
   * <p>
   * If the description contains non US-ASCII characters, it will be encoded
   * using the specified charset. If the description contains only
   * US-ASCII characters, no encoding is done and it is used as is.
   * <p>
   * Note that if the charset encoding process fails, a MessagingException is
   * thrown, and an UnsupportedEncodingException is included in the chain of
   * nested exceptions within the MessagingException.
   * @param description content description
   * @param charset Charset for encoding
   * @exception IllegalWriteException if the underlying implementation 
   * does not support modification
   * @exception IllegalStateException if this body part is obtained 
   * from a READ_ONLY folder.
   */
  public void setDescription(String description, String charset)
    throws MessagingException
  {
    if (description!=null)
    {
      try
      {
        setHeader(CONTENT_DESCRIPTION_NAME,
            MimeUtility.encodeText(description, charset, null));
      }
      catch (UnsupportedEncodingException e)
      {
        throw new MessagingException("Encode error", e);
      }
    }
    else
      removeHeader(CONTENT_DESCRIPTION_NAME);
  }

  /**
   * Get the filename associated with this body part.
   * <p>
   * Returns the value of the "filename" parameter from the
   * "Content-Disposition" header field of this body part.
   * If it's not available, returns the value of the "name" parameter 
   * from the "Content-Type" header field of this body part.
   * Returns null if both are absent.
   */
  public String getFileName()
    throws MessagingException
  {
    String filename = null;
    String header = getHeader(CONTENT_DISPOSITION_NAME, null);
    if (header!=null)
    {
      ContentDisposition cd = new ContentDisposition(header);
      filename = cd.getParameter("filename");
    }
    if (filename==null)
    {
      header = getHeader(CONTENT_TYPE_NAME, null);
      if (header!=null)
      {
        try
        {
          ContentType contentType = new ContentType(header);
          filename = contentType.getParameter("name");
        }
        catch (ParseException e)
        {
        }
      }
    }
    return filename;
  }

  /**
   * Set the filename associated with this body part, if possible.
   * <p>
   * Sets the "filename" parameter of the "Content-Disposition"
   * header field of this body part.
   * @exception IllegalWriteException if the underlying implementation 
   * does not support modification
   * @exception IllegalStateException if this body part is obtained 
   * from a READ_ONLY folder.
   */
  public void setFileName(String filename)
    throws MessagingException
  {
    String header = getHeader(CONTENT_DISPOSITION_NAME, null);
    if (header==null)
      header = "attachment";
    ContentDisposition cd = new ContentDisposition(header);
    cd.setParameter("filename", filename);
    setHeader(CONTENT_DISPOSITION_NAME, cd.toString());

    // We will also set the "name" parameter of the Content-Type field
    // to preserve compatibility with nonconformant MUAs
    header = getContentType(); // not valid for this to be null
    try
    {
      ContentType contentType = new ContentType(header);
      contentType.setParameter("name", filename);
      setHeader(CONTENT_TYPE_NAME, contentType.toString());
    }
    catch (ParseException e)
    {
    }
  }

  /**
   * Return a decoded input stream for this body part's "content".
   * <p>
   * This implementation obtains the input stream from the DataHandler.
   * That is, it invokes getDataHandler().getInputStream();
   * @exception IOException this is typically thrown by the DataHandler.
   * Refer to the documentation for javax.activation.DataHandler for more
   * details.
   */
  public InputStream getInputStream()
    throws IOException, MessagingException
  {
    return getDataHandler().getInputStream();
  }

  /**
   * Produce the raw bytes of the content. 
   * This method is used when creating a DataHandler object for the content.
   * Subclasses that can provide a separate input stream for just the Part 
   * content might want to override this method.
   */
  protected InputStream getContentStream()
    throws MessagingException
  {
    if (contentStream!=null)
      return ((SharedInputStream)contentStream).newStream(0L, -1L);
    if (content!=null)
      return new ByteArrayInputStream(content);
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
   * This implementation simply calls the <code>getContentStream</code> method.
   */
  public InputStream getRawInputStream()
    throws MessagingException
  {
    return getContentStream();
  }

  /**
   * Return a DataHandler for this body part's content.
   * <p>
   * The implementation provided here works just like the the implementation
   * in MimeMessage.
   */
  public DataHandler getDataHandler()
    throws MessagingException
  {
    if (dh==null)
      dh = new DataHandler(new MimePartDataSource(this));
    return dh;
  }

  /**
   * Return the content as a java object.
   * The type of the object returned is of course dependent on the content
   * itself. For example, the native format of a text/plain content is 
   * usually a String object. The native format for a "multipart" content is 
   * always a Multipart subclass. For content types that are unknown to the 
   * DataHandler system, an input stream is returned as the content.
   * <p>
   * This implementation obtains the content from the DataHandler.
   * That is, it invokes <code>getDataHandler().getContent();</code>
   * @exception IOException - this is typically thrown by the DataHandler.
   * Refer to the documentation for javax.activation.DataHandler for more 
   * details.
   */
  public Object getContent()
    throws IOException, MessagingException
  {
    return getDataHandler().getContent();
  }

  /**
   * This method provides the mechanism to set this body part's content.
   * The given DataHandler object should wrap the actual content.
   * @exception IllegalWriteException if the underlying implementation 
   * does not support modification
   * @exception IllegalStateException if this body part is obtained 
   * from a READ_ONLY folder.
   */
  public void setDataHandler(DataHandler dh)
    throws MessagingException
  {
    this.dh = dh;
    // The Content-Type and Content-Transfer-Encoding headers may need to be
    // recalculated by the new DataHandler - see updateHeaders()
    removeHeader(CONTENT_TYPE_NAME);
    removeHeader(CONTENT_TRANSFER_ENCODING_NAME);
  }

  /**
   * A convenience method for setting this body part's content.
   * <p>
   * The content is wrapped in a DataHandler object. Note that a
   * DataContentHandler class for the specified type should be available 
   * to the JavaMail implementation for this to work right.
   * That is, to do <code>setContent(foobar, "application/x-foobar")</code>,
   * a DataContentHandler for "application/x-foobar" should be installed.
   * Refer to the Java Activation Framework for more information.
   * @param o the content object
   * @param type Mime type of the object
   * @exception IllegalWriteException if the underlying implementation 
   * does not support modification
   * @exception IllegalStateException if this body part is obtained 
   * from a READ_ONLY folder.
   */
  public void setContent(Object o, String type)
    throws MessagingException
  {
    if (o instanceof Multipart)
      setContent((Multipart)o);
    else
      setDataHandler(new DataHandler(o, type));
  }

  /**
   * Convenience method that sets the given String as this part's content,
   * with a MIME type of "text/plain".
   * If the string contains non US-ASCII characters, it will be encoded 
   * using the platform's default charset. The charset is also used to set 
   * the "charset" parameter.
   * <p>
   * Note that there may be a performance penalty if text is large,
   * since this method may have to scan all the characters to determine what 
   * charset to use.
   * <p>
   * If the charset is already known, use the <code>setText()</code> version
   * that takes the <code>charset</code> parameter.
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
   * This method sets the body part's content to a Multipart object.
   * @param mp The multipart object that is the Message's content
   * @exception IllegalWriteException if the underlying implementation 
   * does not support modification
   * @exception IllegalStateException if this body part is obtained 
   * from a READ_ONLY folder.
   */
  public void setContent(Multipart mp)
    throws MessagingException
  {
    setDataHandler(new DataHandler(mp, mp.getContentType()));
    // Ensure component hierarchy
    mp.setParent(this);
  }

  /**
   * Output the body part as an RFC 822 format stream.
   * @exception IOException if an error occurs writing to the stream or if an
   * error is generated by the javax.activation layer.
   */
  public void writeTo(OutputStream os)
    throws IOException, MessagingException
  {
    // Wrap in a CRLFOutputStream
    CRLFOutputStream crlfos = null;
    if (os instanceof CRLFOutputStream)
      crlfos = (CRLFOutputStream)os;
    else
      crlfos = new CRLFOutputStream(os);
    
    // Write the headers
    for (Enumeration e = getAllHeaderLines();
        e.hasMoreElements(); )
    {
      crlfos.write((String)e.nextElement());
      crlfos.writeln();
    }
    crlfos.writeln();
    crlfos.flush();

    // Write the content
    os = MimeUtility.encode(os, getEncoding());
    getDataHandler().writeTo(os);
    os.flush();
  }

  /**
   * Get all the headers for this header_name.
   * Note that certain headers may be encoded as per RFC 2047
   * if they contain non US-ASCII characters and these should be decoded.
   * @param name name of header
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
   * @param name the name of this header
   * @param delimiter the delimiter to use
   */
  public String getHeader(String name, String delimiter)
    throws MessagingException
  {
    return headers.getHeader(name, delimiter);
  }

  /**
   * Add this value to the existing values for this name.
   * Note that RFC 822 headers must contain only US-ASCII characters,
   * so a header that contains non US-ASCII characters must be encoded 
   * as per the rules of RFC 2047.
   * @param name the header name
   * @param value the header value
   */
  public void setHeader(String name, String value)
    throws MessagingException
  {
    headers.setHeader(name, value);
  }

  /**
   * Add this value to the existing values for this name.
   * Note that RFC 822 headers must contain only US-ASCII characters,
   * so a header that contains non US-ASCII characters must be encoded 
   * as per the rules of RFC 2047.
   * @param name the header name
   * @param value the header value
   */
  public void addHeader(String name, String value)
    throws MessagingException
  {
    headers.addHeader(name, value);
  }

  /**
   * Remove all headers with this name.
   * @param name the name of this header
   * @exception IllegalWriteException if the underlying implementation 
   * does not support modification
   * @exception IllegalStateException if this body part is obtained 
   * from a READ_ONLY folder.
   */
  public void removeHeader(String name)
    throws MessagingException
  {
    headers.removeHeader(name);
  }

  /**
   * Return all the headers from this Message as an Enumeration of Header
   * objects.
   */
  public Enumeration getAllHeaders()
    throws MessagingException
  {
    return headers.getAllHeaders();
  }

  /**
   * Return matching headers from this Message as an Enumeration of Header
   * objects.
   */
  public Enumeration getMatchingHeaders(String[] names)
    throws MessagingException
  {
    return headers.getMatchingHeaders(names);
  }

  /**
   * Return non-matching headers from this Message as an Enumeration of Header
   * objects.
   */
  public Enumeration getNonMatchingHeaders(String[] names)
    throws MessagingException
  {
    return headers.getNonMatchingHeaders(names);
  }

  /**
   * Add a header line to this body part.
   * @exception IllegalWriteException if the underlying implementation 
   * does not support modification
   * @exception IllegalStateException if this body part is obtained 
   * from a READ_ONLY folder.
   */
  public void addHeaderLine(String line)
    throws MessagingException
  {
    headers.addHeaderLine(line);
  }

  /**
   * Get all header lines as an Enumeration of Strings.
   * A Header line is a raw RFC 822 header line,
   * containing both the "name" and "value" field.
   */
  public Enumeration getAllHeaderLines()
    throws MessagingException
  {
    return headers.getAllHeaderLines();
  }

  /**
   * Get matching header lines as an Enumeration of Strings.
   * A Header line is a raw RFC 822 header line,
   * containing both the "name" and "value" field.
   */
  public Enumeration getMatchingHeaderLines(String[] names)
    throws MessagingException
  {
    return headers.getMatchingHeaderLines(names);
  }

  /**
   * Get non-matching header lines as an Enumeration of Strings.
   * A Header line is a raw RFC 822 header line,
   * containing both the "name" and "value" field.
   */
  public Enumeration getNonMatchingHeaderLines(String[] names)
    throws MessagingException
  {
    return headers.getNonMatchingHeaderLines(names);
  }

  /**
   * Examine the content of this body part and update the appropriate MIME
   * headers.
   * Typical headers that get set here are Content-Type and
   * Content-Transfer-Encoding. Headers might need to be updated in two cases:
   * <ul>
   * <li>A message being crafted by a mail application will certainly need to
   * activate this method at some point to fill up its internal headers.
   * <li>A message read in from a Store will have obtained all its headers 
   * from the store, and so doesn't need this.
   * However, if this message is editable and if any edits have been made 
   * to either the content or message structure, we might need to resync our 
   * headers.
   * </ul>
   * In both cases this method is typically called by the 
   * <code>Message.saveChanges</code> method.
   */
  protected void updateHeaders()
    throws MessagingException
  {
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
          if (getHeader(CONTENT_TRANSFER_ENCODING_NAME)==null)
          {
            setHeader(CONTENT_TRANSFER_ENCODING_NAME,
                MimeUtility.getEncoding(dh));
          }
        }

        // Update Content-Type if nonexistent,
        // and Content-Type "name" with Content-Disposition "filename"
        // parameter (see setFilename())
        if (getHeader(CONTENT_TYPE_NAME)==null)
        {
          String disposition = getHeader(CONTENT_DISPOSITION_NAME, null);
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
          setHeader(CONTENT_TYPE_NAME, contentType);
        }
      }
      catch (IOException e)
      {
        throw new MessagingException("I/O error", e);
      }
    }
  }

}
