/*
 * MimePart.java
 * Copyright (C) 2002 The Free Software Foundation
 * 
 * This file is part of GNU JavaMail, a library.
 * 
 * GNU JavaMail is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 * GNU JavaMail is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 * As a special exception, if you link this library with other files to
 * produce an executable, this library does not by itself cause the
 * resulting executable to be covered by the GNU General Public License.
 * This exception does not however invalidate any other reasons why the
 * executable file might be covered by the GNU General Public License.
 */

package javax.mail.internet;

import java.util.Enumeration;
import javax.mail.MessagingException;
import javax.mail.Part;

/**
 * The MimePart interface models an Entity as defined by MIME
 * (RFC2045, Section 2.4).
 * <p>
 * MimePart extends the Part interface to add additional RFC822 and MIME 
 * specific semantics and attributes. It provides the base interface for 
 * the MimeMessage and MimeBodyPart classes
 * <hr>
 * A note on RFC822 and MIME headers
 * <p>
 * RFC822 and MIME header fields must contain only US-ASCII characters. If a
 * header contains non US-ASCII characters, it must be encoded as per the 
 * rules in RFC 2047. The MimeUtility class provided in this package can be 
 * used to to achieve this. Callers of the <code>setHeader</code>, 
 * <code>addHeader</code>, and <code>addHeaderLine</code> methods are 
 * responsible for enforcing the MIME requirements for the specified headers.
 * In addition, these header fields must be folded (wrapped) before being 
 * sent if they exceed the line length limitation for the transport 
 * (1000 bytes for SMTP). Received headers may have been folded. 
 * The application is responsible for folding and unfolding headers as 
 * appropriate.
 *
 * @author <a href="mailto:dog@gnu.org">Chris Burdess</a>
 * @version 1.3
 */
public interface MimePart
  extends Part
{

  /**
   * Get the values of all header fields available for this header,
   * returned as a single String, with the values separated by the delimiter.
   * If the delimiter is null, only the first value is returned.
   * @param header_name the name of this header
   * @return the value fields for all headers with this name
   */
  String getHeader(String header_name, String delimiter)
    throws MessagingException;

  /**
   * Add a raw RFC822 header-line.
   * @exception IllegalWriteException if the underlying implementation does not
   * support modification
   * @exception IllegalStateException if this Part is obtained from a READ_ONLY
   * folder
   */
  void addHeaderLine(String line)
    throws MessagingException;

  /**
   * Get all header lines as an Enumeration of Strings.
   * A Header line is a raw RFC822 header-line,
   * containing both the "name" and "value" field.
   */
  Enumeration getAllHeaderLines()
    throws MessagingException;

  /**
   * Get matching header lines as an Enumeration of Strings.
   * A Header line is a raw RFC822 header-line,
   * containing both the "name" and "value" field.
   */
  Enumeration getMatchingHeaderLines(String[] names)
    throws MessagingException;

  /**
   * Get non-matching header lines as an Enumeration of Strings.
   * A Header line is a raw RFC822 header-line,
   * containing both the "name" and "value" field.
   */
  Enumeration getNonMatchingHeaderLines(String[] names)
    throws MessagingException;

  /**
   * Get the transfer encoding of this part.
   * @return content-transfer-encoding
   */
  String getEncoding()
    throws MessagingException;

  /**
   * Get the Content-ID of this part. 
   * Returns null if none present.
   * @return content-ID
   */
  String getContentID()
    throws MessagingException;

  /**
   * Get the Content-MD5 digest of this part.
   * Returns null if none present.
   * @return content-MD5
   */
  String getContentMD5()
    throws MessagingException;

  /**
   * Set the Content-MD5 of this part.
   * @param cid content-id
   * @exception IllegalWriteException if the underlying implementation does not
   * support modification
   * @param IllegalStateException if this Part is obtained from a READ_ONLY 
   * folder
   */
  void setContentMD5(String md5)
    throws MessagingException;

  /**
   * Get the language tags specified in the Content-Language header of this
   * MimePart. The Content-Language header is defined by RFC 1766.
   * Returns null if this header is not available.
   */
  String[] getContentLanguage()
    throws MessagingException;

  /**
   * Set the Content-Language header of this MimePart. The Content-Language
   * header is defined by RFC1766.
   * @param languages array of language tags
   * @exception IllegalWriteException if the underlying implementation does not
   * support modification
   * @exception IllegalStateException if this Part is obtained from a READ_ONLY
   * folder
   */
  void setContentLanguage(String[] languages)
    throws MessagingException;

  /**
   * Convenience method that sets the given String as this part's content, with
   * a MIME type of "text/plain". If the string contains non US-ASCII
   * characters. it will be encoded using the platform's default charset. The
   * charset is also used to set the "charset" parameter.
   * <p>
   * Note that there may be a performance penalty if text is large, since this
   * method may have to scan all the characters to determine what charset to
   * use.
   * <p>
   * If the charset is already known, use the <code>setText()</code> version 
   * that takes the <code>charset</code> parameter.
   */
  void setText(String text)
    throws MessagingException;

  /**
   * Convenience method that sets the given String as this part's content, with
   * a MIME type of "text/plain" and the specified charset. The given Unicode
   * string will be charset-encoded using the specified charset. The charset is
   * also used to set "charset" parameter.
   */
  void setText(String text, String charset)
    throws MessagingException;
  
}
