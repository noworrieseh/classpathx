/*
 * MimeUtility.java
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
import java.util.HashMap;
import java.util.StringTokenizer;
import java.util.NoSuchElementException;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.MessagingException;
import javax.mail.Session;
import gnu.mail.util.*;

/**
 * This is a utility class that provides various MIME related functionality.
 * <p>
 * There are a set of methods to encode and decode MIME headers as per 
 * RFC 2047. A brief description on handling such headers is given below:
 * <p>
 * RFC 822 mail headers must contain only US-ASCII characters. Headers that
 * contain non US-ASCII characters must be encoded so that they contain only
 * US-ASCII characters. Basically, this process involves using either BASE64 
 * or QP to encode certain characters. RFC 2047 describes this in detail.
 * <p>
 * In Java, Strings contain (16 bit) Unicode characters. ASCII is a subset of
 * Unicode (and occupies the range 0 - 127). A String that contains only ASCII
 * characters is already mail-safe. If the String contains non US-ASCII
 * characters, it must be encoded. An additional complexity in this step is that
 * since Unicode is not yet a widely used charset, one might want to first
 * charset-encode the String into another charset and then do the
 * transfer-encoding.
 * <p>
 * Note that to get the actual bytes of a mail-safe String (say, for sending 
 * over SMTP), one must do
 * <pre>

        byte[] bytes = string.getBytes("iso-8859-1");

   <pre>
 * The <code>setHeader()</code> and <code>addHeader()</code> methods on
 * MimeMessage and MimeBodyPart assume that the given header values are 
 * Unicode strings that contain only US-ASCII characters. Hence the callers 
 * of those methods must insure that the values they pass do not contain non 
 * US-ASCII characters. The methods in this class help do this.
 * <p>
 * The <code>getHeader()</code> family of methods on MimeMessage and 
 * MimeBodyPart return the raw header value. These might be encoded as per 
 * RFC 2047, and if so, must be decoded into Unicode Strings.
 * The methods in this class help to do this.
 */
public class MimeUtility
{

  /*
   * Uninstantiable.
   */
  private MimeUtility()
  {
  }
  
  /**
   * Get the content-transfer-encoding that should be applied to the input
   * stream of this datasource, to make it mailsafe.
   * <p>
   * The algorithm used here is:
   * <ul>
   * <li>If the primary type of this datasource is "text" and if all the bytes
   * in its input stream are US-ASCII, then the encoding is "7bit". If more
   * than half of the bytes are non-US-ASCII, then the encoding is "base64".
   * If less than half of the bytes are non-US-ASCII, then the encoding is
   * "quoted-printable".
   * <li>If the primary type of this datasource is not "text", then if all the
   * bytes of its input stream are US-ASCII, the encoding is "7bit". If
   * there is even one non-US-ASCII character, the encoding is "base64".
   * @param ds DataSource
   * @return the encoding.
   * This is either "7bit", "quoted-printable" or "base64"
   */
  public static String getEncoding(DataSource ds)
  {
    String encoding = "base64";
    InputStream is = null;
    try
    {
      is = ds.getInputStream();
      ContentType ct = new ContentType(ds.getContentType());
      boolean text = ct.match("text/*");
      switch (asciiStatus(is, ALL, text))
      {
        case ALL_ASCII:
          encoding = "7bit";
          break;
        case MAJORITY_ASCII:
          if (text)
            encoding = "quoted-printable";
          break;
      }
    }
    catch (Exception e)
    {
      
    }
    try
    {
      is.close();
    }
    catch (IOException e)
    {
    }
    return encoding;
  }
  
  /**
   * Same as getEncoding(DataSource) except that instead of reading the data
   * from an InputStream it uses the writeTo method to examine the data.
   * This is more efficient in the common case of a DataHandler created 
   * with an object and a MIME type (for example, a "text/plain" String)
   * because all the I/O is done in this thread.
   * In the case requiring an InputStream the DataHandler uses a thread,
   * a pair of pipe streams, and the writeTo method to produce the data.
   */
  public static String getEncoding(DataHandler dh)
  {
    String encoding = "base64";
    if (dh.getName()!=null)
      return getEncoding(dh.getDataSource());
    try
    {
      ContentType ct = new ContentType(dh.getContentType());
      boolean text = ct.match("text/*");
      
      AsciiOutputStream aos = new AsciiOutputStream(!text);
      try
      {
        dh.writeTo(aos);
      }
      catch (IOException e)
      {
      }
      switch (aos.status())
      {
        case ALL_ASCII:
          encoding = "7bit";
          break;
        case MAJORITY_ASCII:
          if (text)
            encoding = "quoted-printable";
          break;
      }
    }
    catch (Exception e)
    {
    }
    return encoding;
  }

  /**
   * Decode the given input stream.
   * The Input stream returned is the decoded input stream.
   * All the encodings defined in RFC 2045 are supported here.
   * They include "base64", "quoted-printable", "7bit", "8bit", and
   * "binary". In addition, "uuencode" is also supported.
   * @param is input stream
   * @param encoding the encoding of the stream.
   * @return decoded input stream.
   */
  public static InputStream decode(InputStream is, String encoding)
    throws MessagingException
  {
    if (encoding.equalsIgnoreCase("base64"))
      return new Base64InputStream(is);
    if (encoding.equalsIgnoreCase("quoted-printable"))
      return new QPInputStream(is);
    if (encoding.equalsIgnoreCase("uuencode") || 
        encoding.equalsIgnoreCase("x-uuencode"))
      return new UUDecoderStream(is);
    if (encoding.equalsIgnoreCase("binary") ||
        encoding.equalsIgnoreCase("7bit") ||
        encoding.equalsIgnoreCase("8bit"))
      return is;
    throw new MessagingException("Unknown encoding: "+encoding);
  }

  /**
   * Wrap an encoder around the given output stream.
   * All the encodings defined in RFC 2045 are supported here.
   * They include "base64", "quoted-printable", "7bit", "8bit" and "binary".
   * In addition, "uuencode" is also supported.
   * @param os output stream
   * @param encoding the encoding of the stream.
   * @return output stream that applies the specified encoding.
   */
  public static OutputStream encode(OutputStream os, String encoding)
    throws MessagingException
  {
    if (encoding==null)
      return os;
    if (encoding.equalsIgnoreCase("base64"))
      return new Base64OutputStream(os);
    if (encoding.equalsIgnoreCase("quoted-printable"))
      return new QPOutputStream(os);
    if (encoding.equalsIgnoreCase("uuencode") ||
        encoding.equalsIgnoreCase("x-uuencode"))
      return new UUEncoderStream(os);
    if (encoding.equalsIgnoreCase("binary") || 
        encoding.equalsIgnoreCase("7bit") || 
        encoding.equalsIgnoreCase("8bit"))
      return os;
    else
      throw new MessagingException("Unknown encoding: "+encoding);
  }

  /**
   * Wrap an encoder around the given output stream.
   * All the encodings defined in RFC 2045 are supported here.
   * They include "base64", "quoted-printable", "7bit", "8bit" and "binary".
   * In addition, "uuencode" is also supported. The <code>filename</code>
   * parameter is used with the "uuencode" encoding and is included in the 
   * encoded output.
   * @param os output stream
   * @param encoding the encoding of the stream.
   * @param filename name for the file being encoded (only used with uuencode)
   * @return output stream that applies the specified encoding.
   */
  public static OutputStream encode(OutputStream os, String encoding,
      String filename)
    throws MessagingException
  {
    if (encoding==null)
      return os;
    if (encoding.equalsIgnoreCase("base64"))
      return new Base64OutputStream(os);
    if (encoding.equalsIgnoreCase("quoted-printable"))
      return new QPOutputStream(os);
    if (encoding.equalsIgnoreCase("uuencode") ||
        encoding.equalsIgnoreCase("x-uuencode"))
      return new UUEncoderStream(os, filename);
    if (encoding.equalsIgnoreCase("binary") || 
        encoding.equalsIgnoreCase("7bit") || 
        encoding.equalsIgnoreCase("8bit"))
      return os;
    else
      throw new MessagingException("Unknown encoding: "+encoding);
  }

  /**
   * Encode a RFC 822 "text" token into mail-safe form as per RFC 2047.
   * <p>
   * The given Unicode string is examined for non US-ASCII characters. If the
   * string contains only US-ASCII characters, it is returned as-is. If the
   * string contains non US-ASCII characters, it is first character-encoded
   * using the platform's default charset, then transfer-encoded using either
   * the B or Q encoding. The resulting bytes are then returned as a Unicode
   * string containing only ASCII characters.
   * <p>
   * Note that this method should be used to encode only "unstructured" 
   * RFC 822 headers.
   * <p>
   * Example of usage:
   * <pre>
    MimePart part = ...
    String rawvalue = "FooBar Mailer, Japanese version 1.1"
    try {
      // If we know for sure that rawvalue contains only US-ASCII
      // characters, we can skip the encoding part
      part.setHeader("X-mailer", MimeUtility.encodeText(rawvalue));
    } catch (UnsupportedEncodingException e) {
      // encoding failure
    } catch (MessagingException me) {
      // setHeader() failure
    }
    </pre>
   * @param text unicode string
   * @return Unicode string containing only US-ASCII characters
   * @param UnsupportedEncodingException if the encoding fails
   */
  public static String encodeText(String text)
    throws UnsupportedEncodingException
  {
    return encodeText(text, null, null);
  }

  /**
   * Encode a RFC 822 "text" token into mail-safe form as per RFC 2047.
   * <p>
   * The given Unicode string is examined for non US-ASCII characters. If the
   * string contains only US-ASCII characters, it is returned as-is. If the
   * string contains non US-ASCII characters, it is first character-encoded
   * using the platform's default charset, then transfer-encoded using either
   * the B or Q encoding. The resulting bytes are then returned as a Unicode
   * string containing only ASCII characters.
   * <p>
   * Note that this method should be used to encode only "unstructured" 
   * RFC 822 headers.
   * <p>
   * @param text the header value
   * @param charset the charset. If this parameter is null, the platform's
   * default chatset is used.
   * @param encoding the encoding to be used. 
   * Currently supported values are "B" and "Q".
   * If this parameter is null, then the "Q" encoding is used if most of the
   * characters to be encoded are in the ASCII charset, otherwise "B"
   * encoding is used.
   * @return Unicode string containing only US-ASCII characters
   */
  public static String encodeText(String text, String charset, String encoding)
    throws UnsupportedEncodingException
  {
    return encodeWord(text, charset, encoding, false);
  }

  /**
   * Decode "unstructured" headers, that is, headers that are defined as '*text'
   * as per RFC 822.
   * <p>
   * The string is decoded using the algorithm specified in RFC 2047, Section
   * 6.1.1. If the charset-conversion fails for any sequence, an
   * UnsupportedEncodingException is thrown. If the String is not an RFC 2047
   * style encoded header, it is returned as-is
   * <p>
   * Example of usage:
   * <pre>
    MimePart part = ...
    String rawvalue = null;
    String  value = null;
    try {
      if ((rawvalue = part.getHeader("X-mailer")[0]) != null)
        value = MimeUtility.decodeText(rawvalue);
    } catch (UnsupportedEncodingException e) {
        // Don't care
        value = rawvalue;
    } catch (MessagingException me) { }
    return value;
    <pre>
   * @param etext the possibly encoded value
   * @exception UnsupportedEncodingException if the charset conversion failed.
   */
  public static String decodeText(String etext)
    throws UnsupportedEncodingException
  {
    String delimiters = "\t\n\r ";
    if (etext.indexOf("=?")<0)
      return etext;
    StringTokenizer st = new StringTokenizer(etext, delimiters, true);
    StringBuffer buffer = new StringBuffer();
    StringBuffer extra = new StringBuffer();
    boolean decoded = false;
    while (st.hasMoreTokens()) 
    {
      String token = st.nextToken();
      char c = token.charAt(0);
      if (delimiters.indexOf(c)>-1)
        extra.append(c);
      else
      {
        try
        {
          token = decodeWord(token);
          if (!decoded && extra.length()>0)
            buffer.append(extra);
          decoded = true;
        }
        catch (ParseException e)
        {
          if (extra.length()>0)
            buffer.append(extra);
          decoded = false;
        }
        buffer.append(token);
        extra.setLength(0);
      }
    }
    return buffer.toString();
  }

  /**
   * Encode a RFC 822 "word" token into mail-safe form as per RFC 2047.
   * <p>
   * The given Unicode string is examined for non US-ASCII characters.
   * If the string contains only US-ASCII characters, it is returned as-is.
   * If the string contains non US-ASCII characters, it is first 
   * character-encoded using the platform's default charset, then 
   * transfer-encoded using either the B or Q encoding.
   * The resulting bytes are then returned as a Unicode string containing 
   * only ASCII characters.
   * <p>
   * This method is meant to be used when creating RFC 822 "phrases". The
   * InternetAddress class, for example, uses this to encode it's 'phrase'
   * component.
   * @param text unicode string
   * @return Unicode string containing only US-ASCII characters.
   * @exception UnsupportedEncodingException if the encoding fails
   */
  public static String encodeWord(String text)
    throws UnsupportedEncodingException
  {
    return encodeWord(text, null, null);
  }

  /**
   * Encode a RFC 822 "word" token into mail-safe form as per RFC 2047.
   * <p>
   * The given Unicode string is examined for non US-ASCII characters.
   * If the string contains only US-ASCII characters, it is returned as-is.
   * If the string contains non US-ASCII characters, it is first 
   * character-encoded using the platform's default charset, then 
   * transfer-encoded using either the B or Q encoding.
   * The resulting bytes are then returned as a Unicode string containing 
   * only ASCII characters.
   * <p>
   * @param text unicode string
   * @param charset the MIME charset
   * @param encoding the encoding to be used.
   * Currently supported values are "B" and "Q".
   * If this parameter is null, then the "Q" encoding is used if most of the
   * characters to be encoded are in the ASCII charset, otherwise "B"
   * encoding is used.
   * @return Unicode string containing only US-ASCII characters
   * @exception UnsupportedEncodingException if the encoding fails
   */
  public static String encodeWord(String text, String charset, String encoding)
    throws UnsupportedEncodingException
  {
    return encodeWord(text, charset, encoding, true);
  }

  private static String encodeWord(String text, String charset, 
      String encoding, boolean word)
    throws UnsupportedEncodingException
  {
    if (asciiStatus(text.getBytes())==1)
      return text;
    String javaCharset;
    if (charset==null)
    {
      javaCharset = getDefaultJavaCharset();
      charset = mimeCharset(charset);
    }
    else
      javaCharset = javaCharset(charset);
    if (encoding==null)
    {
      byte[] bytes = text.getBytes(javaCharset);
      if (asciiStatus(bytes)!=MINORITY_ASCII)
        encoding = "Q";
      else
        encoding = "B";
    }
    boolean bEncoding;
    if (encoding.equalsIgnoreCase("B"))
      bEncoding = true;
    else if (encoding.equalsIgnoreCase("Q"))
      bEncoding = false;
    else
      throw new UnsupportedEncodingException("Unknown transfer encoding: "+
          encoding);
    
    StringBuffer encodingBuffer = new StringBuffer();
    encodingBuffer.append("=?");
    encodingBuffer.append(charset);
    encodingBuffer.append("?");
    encodingBuffer.append(encoding);
    encodingBuffer.append("?");
    
    StringBuffer buffer = new StringBuffer();
    encodeBuffer(buffer,
        text, 
        javaCharset, 
        bEncoding, 
        68 - charset.length(), 
        encodingBuffer.toString(), 
        true,
        word);
    return buffer.toString();
  }

  private static void encodeBuffer(StringBuffer buffer,
      String text, 
      String charset, 
      boolean bEncoding, 
      int max, 
      String encoding,
      boolean keepTogether, 
      boolean word)
    throws UnsupportedEncodingException
  {
    byte[] bytes = text.getBytes(charset);
    int elen;
    if (bEncoding)
      elen = BOutputStream.encodedLength(bytes);
    else
      elen = QOutputStream.encodedLength(bytes, word);
    int len = text.length();
    if (elen>max && len>1)
    {
      encodeBuffer(buffer,
          text.substring(0, len/2), 
          charset, 
          bEncoding, 
          max, 
          encoding, 
          keepTogether, 
          word);
      encodeBuffer(buffer,
          text.substring(len/2, len),
          charset,
          bEncoding,
          max,
          encoding,
          false,
          word);
    }
    else
    {
      ByteArrayOutputStream bos = new ByteArrayOutputStream();
      OutputStream os = null;
      if (bEncoding)
        os = new BOutputStream(bos);
      else
        os = new QOutputStream(bos, word);
      try
      {
        os.write(bytes);
        os.close();
      }
      catch (IOException e)
      {
      }
      bytes = bos.toByteArray();
      if (!keepTogether)
        buffer.append("\r\n ");
      buffer.append(encoding);
      for (int i = 0; i<bytes.length; i++)
        buffer.append((char)bytes[i]);
      
      buffer.append("?=");
    }
  }

  /**
   * The string is parsed using the rules in RFC 2047 for parsing an
   * "encoded-word".
   * If the parse fails, a ParseException is thrown. Otherwise, it is 
   * transfer-decoded, and then charset-converted into Unicode. If the
   * charset-conversion fails, an UnsupportedEncodingException is thrown.
   * @param eword the possibly encoded value
   * @exception ParseException if the string is not an encoded-word as per 
   * RFC 2047.
   * @exception UnsupportedEncodingException if the charset conversion
   * failed.
   */
  public static String decodeWord(String text)
    throws ParseException, UnsupportedEncodingException
  {
    if (!text.startsWith("=?"))
      throw new ParseException();
    int start = 2;
    int end = text.indexOf('?', start);
    if (end<0)
      throw new ParseException();
    String charset = javaCharset(text.substring(start, end));
    start = end + 1;
    end = text.indexOf('?', start);
    if (end<0)
      throw new ParseException();
    String encoding = text.substring(start, end);
    start = end + 1;
    end = text.indexOf("?=", start);
    if (end<0)
      throw new ParseException();
    text = text.substring(start, end);
    try
    {
      // The characters in the remaining string must all be 7-bit clean.
      // Therefore it is safe just to copy them verbatim into a byte array.
      char[] chars = text.toCharArray();
      int len = chars.length;
      byte[] bytes = new byte[len];
      for (int i = 0; i<len; i++)
        bytes[i] = (byte)chars[i];

      ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
      InputStream is;
      if (encoding.equalsIgnoreCase("B"))
        is = new Base64InputStream(bis);
      else
      if (encoding.equalsIgnoreCase("Q"))
        is = new QInputStream(bis);
      else
        throw new UnsupportedEncodingException("Unknown encoding: "+encoding);
      len = bis.available();
      bytes = new byte[len];
      len = is.read(bytes, 0, len);
      return new String(bytes, 0, len, charset);
    }
    catch (IOException e)
    {
      throw new ParseException();
    }
    catch (IllegalArgumentException e)
    {
      throw new UnsupportedEncodingException();
    }
  }

  /**
   * A utility method to quote a word, if the word contains any characters 
   * from the specified 'specials' list.
   * <p>
   * The HeaderTokenizer class defines two special sets of delimiters - 
   * MIME and RFC 822.
   * <p>
   * This method is typically used during the generation of RFC 822 and MIME
   * header fields.
   * @param word word to be quoted
   * @param specials the set of special characters
   * @return the possibly quoted word
   */
  public static String quote(String text, String specials)
  {
    int len = text.length();
    boolean needsQuotes = false;
    for (int i = 0; i<len; i++)
    {
      char c = text.charAt(i);
      if (c=='\n' || c=='\r' || c=='"' || c=='\\')
      {
        StringBuffer buffer = new StringBuffer(len+3);
        buffer.append('"');
        for (int j = 0; j<len; j++)
        {
          char c2 = text.charAt(j);
          if (c2=='"' || c2=='\\' || c2=='\r' || c2=='\n')
            buffer.append('\\');
          buffer.append(c2);
        }

        buffer.append('"');
        return buffer.toString();
      }
      if (c<' ' || c>'\177' || specials.indexOf(c)>0)
        needsQuotes = true;
    }

    if (needsQuotes)
    {
      StringBuffer buffer = new StringBuffer(len+2);
      buffer.append('"');
      buffer.append(text);
      buffer.append('"');
      return buffer.toString();
    }
    else
      return text;
  }

  // -- Java and MIME charset conversions --

  /*
   * Map of MIME charset names to Java charset names.
   */
  private static HashMap mimeCharsets;

  /*
   * Map of Java charset names to MIME charset names.
   */
  private static HashMap javaCharsets;

  /*
   * Load the charset conversion tables.
   */
  static 
  {
    String mappings = "/META-INF/javamail.charset.map";
    InputStream in = (MimeUtility.class).getResourceAsStream(mappings);
    if (in!=null)
    {
      javaCharsets = new HashMap(20);
      mimeCharsets = new HashMap(10);
      LineInputStream lin = new LineInputStream(in);
      parse(javaCharsets, lin);
      parse(mimeCharsets, lin);
    }
  }

  /*
   * Parse a charset map stream.
   */
  private static void parse(HashMap mappings, LineInputStream lin)
  {
    try
    {
      while (true)
      {
        String line = lin.readLine();
        if (line==null || (line.startsWith("--") && line.endsWith("--")))
          return;
        
        if (line.trim().length()!=0 && !line.startsWith("#"))
        {
          StringTokenizer st = new StringTokenizer(line, "\t ");
          try
          {
            String key = st.nextToken();
            String value = st.nextToken();
            mappings.put(key.toLowerCase(), value);
          }
          catch (NoSuchElementException e2)
          {
          }
        }
      }
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
  }

  /**
   * Convert a MIME charset name into a valid Java charset name.
   * @param charset the MIME charset name
   * @return the Java charset equivalent.
   * If a suitable mapping is not available, the passed in charset is 
   * itself returned.
   */
  public static String javaCharset(String charset)
  {
    if (mimeCharsets==null || charset==null)
      return charset;
    String jc = (String)mimeCharsets.get(charset.toLowerCase());
    return (jc!=null) ? jc : charset;
  }

  /**
   * Convert a java charset into its MIME charset name.
   * <p>
   * Note that a future version of JDK (post 1.2) might provide this
   * functionality, in which case, we may deprecate this method then.
   * @param charset the JDK charset
   * @return the MIME/IANA equivalent.
   * If a mapping is not possible, the passed in charset itself is returned.
   */
  public static String mimeCharset(String charset)
  {
    if (javaCharsets==null || charset==null)
      return charset;
    String mc = (String)javaCharsets.get(charset.toLowerCase());
    return (mc!=null) ? mc : charset;
  }

  // -- Java default charset --
  
  /*
   * Local cache for the system default Java charset.
   * @see #getDefaultJavaCharset
   */
  private static String defaultJavaCharset;

  /**
   * Get the default charset corresponding to the system's current default
   * locale.
   * @return the default charset of the system's default locale,
   * as a Java charset. (NOT a MIME charset)
   */
  public static String getDefaultJavaCharset()
  {
    if (defaultJavaCharset==null)
    {
      try
      {
        defaultJavaCharset = System.getProperty("file.encoding", "8859_1");
      }
      catch (SecurityException e)
      {
        // InputStreamReader has access to the platform default encoding.
        // We create a dummy input stream to feed it with, just to get
        // this encoding value.
        InputStreamReader isr = 
          new InputStreamReader(new InputStream()
              {
                public int read()
                {
                  return 0;
                }
              }
          );
        defaultJavaCharset = isr.getEncoding();
        
        // If all else fails use 8859_1
        if (defaultJavaCharset==null)
          defaultJavaCharset = "8859_1";
      }
    }
    return defaultJavaCharset;
  }

  // -- Calculating multipart boundaries --
  
  private static int part = 0;

  /*
   * Returns a suitably unique boundary value.
   */
  static String getUniqueBoundaryValue()
  {
    StringBuffer buffer = new StringBuffer();
    buffer.append("----=_Part_");
    buffer.append(part++);
    buffer.append("_");
    buffer.append(buffer.hashCode());
    buffer.append('.');
    buffer.append(System.currentTimeMillis());
    return buffer.toString();
  }

  /*
   * Returns a suitably unique Message-ID value.
   */
  static String getUniqueMessageIDValue(Session session)
  {
    InternetAddress localAddress = InternetAddress.getLocalAddress(session);
    String address = (localAddress!=null) ? localAddress.getAddress() :
      "javamailuser@localhost";

    StringBuffer buffer = new StringBuffer();
    buffer.append(buffer.hashCode());
    buffer.append('.');
    buffer.append(System.currentTimeMillis());
    buffer.append('.');
    buffer.append("JavaMail.");
    buffer.append(address);
    return buffer.toString();
  }

  // These methods provide checks on whether collections of bytes contain
  // all-ASCII, majority-ASCII, or minority-ASCII bytes.
  
  // Constants
  static final int ALL = -1;
  static final int ALL_ASCII = 1;
  static final int MAJORITY_ASCII = 2;
  static final int MINORITY_ASCII = 3;

  static int asciiStatus(byte[] bytes)
  {
    int asciiCount = 0;
    int nonAsciiCount = 0;
    for (int i = 0; i<bytes.length; i++)
      if (isAscii(bytes[i] & 0xff))
        asciiCount++;
      else
        nonAsciiCount++;

    if (nonAsciiCount==0)
      return ALL_ASCII;
    return asciiCount<=nonAsciiCount ? MINORITY_ASCII : MAJORITY_ASCII;
  }

  static int asciiStatus(InputStream is, int len, boolean text)
  {
    int asciiCount = 0;
    int nonAsciiCount = 0;
    int blockLen = 4096;
    int lineLen = 0;
    boolean islong = false;
    byte[] bytes = null;
    if (len!=0)
    {
      blockLen = (len!=ALL) ? Math.min(len, 4096) : 4096;
      bytes = new byte[blockLen];
    }
    while (len!=0) 
    {
      int readLen;
      try
      {
        readLen = is.read(bytes, 0, blockLen);
        if (readLen<0)
          break;
        for (int i = 0; i<readLen; i++)
        {
          int c = bytes[i] & 0xff;
          if (c==13 || c==10)
            lineLen = 0;
          else
          {
            lineLen++;
            if (lineLen>998)
              islong = true;
          }
          if (isAscii(c))
            asciiCount++;
          else
          {
            if (text)
              return MINORITY_ASCII;
            nonAsciiCount++;
          }
        }

      }
      catch (IOException e)
      {
        break;
      }
      if (len!=-1)
        len -= readLen;
    }
    if (len==0 && text)
      return MINORITY_ASCII;
    if (nonAsciiCount==0)
      return !islong ? ALL_ASCII : MAJORITY_ASCII;
    return asciiCount<=nonAsciiCount ? MINORITY_ASCII : MAJORITY_ASCII;
  }

  private static final boolean isAscii(int c)
  {
    return (c<128 && c>31) || c==13 || c==10 || c==9;
  }

  /*
   * This is used by the getEncoding(DataHandler) method to ascertain which
   * encoding scheme to use. It embodies the same algorithm as the
   * asciiStatus methods above.
   */
  static class AsciiOutputStream extends OutputStream
  {
    
    private boolean strict;
    private int asciiCount = 0;
    private int nonAsciiCount = 0;
    private int ret;
    private int len;
    private boolean islong = false;
    
    public AsciiOutputStream(boolean strict)
    {
      this.strict = strict;
    }
    
    public void write(int c)
      throws IOException
    {
      check(c);
    }
    
    public void write(byte[] bytes)
      throws IOException
    {
      write(bytes, 0, bytes.length);
    }
    
    public void write(byte[] bytes, int offset, int length)
      throws IOException
    {
      length += offset;
      for (int i = offset; i<length; i++)
        check(bytes[i]);
      
    }
    
    private final void check(int c)
      throws IOException
    {
      c &= 0xff;
      if (c==13 || c==10)
        len = 0;
      else
      {
        len++;
        if (len>998)
          islong = true;
      }
      if (c>127)
      {
        nonAsciiCount++;
        if (strict)
        {
          ret = MINORITY_ASCII;
          throw new EOFException();
        }
      }
      else
        asciiCount++;
    }
    
    int status()
    {
      if (ret!=0)
        return ret;
      if (nonAsciiCount==0)
        return !islong ? ALL_ASCII : MAJORITY_ASCII;
      return asciiCount<=nonAsciiCount ? MAJORITY_ASCII : MINORITY_ASCII;
    }
    
  }

}
