/*
 * InternetHeaders.java
 * Copyright(C) 2002 The Free Software Foundation
 * 
 * This file is part of GNU JavaMail, a library.
 * 
 * GNU JavaMail is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 *(at your option) any later version.
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

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.NoSuchElementException;
import javax.mail.Header;
import javax.mail.MessagingException;

import gnu.inet.util.LineInputStream;

/**
 * InternetHeaders is a utility class that manages RFC822 style headers.
 * Given a rfc822 format message stream, it reads lines till the blank line 
 * that indicates end of header. The input stream is positioned at the start 
 * of the body. The lines are stored within the object and can be extracted 
 * as either Strings or Header objects.
 * <p>
 * This class is mostly intended for service providers.
 * MimeMessage and MimeBody use this class for holding their headers.
 * <p>
 * <hr>
 * A note on RFC822 and MIME headers
 * <p>
 * RFC822 and MIME header fields must contain only US-ASCII characters.
 * If a header contains non US-ASCII characters, it must be encoded as per
 * the rules in RFC 2047. The MimeUtility class provided in this package can 
 * be used to to achieve this. Callers of the <code>setHeader</code>,
 * <code>addHeader</code>, and <code>addHeaderLine</code> methods are 
 * responsible for enforcing the MIME requirements for the specified headers.
 * In addition, these header fields must be folded(wrapped) before being 
 * sent if they exceed the line length limitation for the transport 
 *(1000 bytes for SMTP). Received headers may have been folded.
 * The application is responsible for folding and unfolding headers 
 * as appropriate.
 *
 * @author <a href="mailto:dog@gnu.org">Chris Burdess</a>
 * @version 1.3
 */
public class InternetHeaders
{

  /*
   * The header class that stores raw header lines.
   */
  static class InternetHeader
    extends Header
  {
    
    String line;
    
    InternetHeader(String line)
    {
      super(null, null);
      int i = line.indexOf(':');
      name = (i < 0) ? line.trim() : line.substring(0, i).trim();
      this.line = line;
    }
    
    InternetHeader(String name, String value)
    {
      super(name, null);
      if (value != null)
        {
          StringBuffer buffer = new StringBuffer();
          buffer.append(name);
          buffer.append(':');
          buffer.append(' ');
          buffer.append(value);
          line = buffer.toString();
        }
    }

    public String getValue()
    {
      int i = line.indexOf(':');
      if (i < 0)
        {
          return line;
        }
      
      int pos, len = line.length();
      for (pos = i + 1; pos < len; pos++)
        {
          char c = line.charAt(pos);
          if (c != ' ' && c != '\t' && c != '\r' && c != '\n')
            {
              break;
            }
        }
      
      return line.substring(pos);
    }

    void setValue(String value)
    {
      StringBuffer buffer = new StringBuffer();
      buffer.append(name);
      buffer.append(':');
      buffer.append(' ');
      buffer.append(value);
      line = buffer.toString();
    }

    boolean nameEquals(String other)
    {
      return name.equalsIgnoreCase(other);
    }
    
  }

  /*
   * The enumeration used to filter headers for the InternetHeaders object.
   */
  static class HeaderEnumeration
    implements Iterator, Enumeration
  {

    private Iterator source;
    private String[] names;
    private boolean stringForm;
    private boolean matching;
    private InternetHeader nextHeader;
    
    HeaderEnumeration(Iterator source, String[] names,
                       boolean stringForm, boolean matching)
    {
      this.source = source;
      this.names = names;
      this.stringForm = stringForm;
      this.matching = matching;
    }
    
    /**
     * Enumeration syntax
     */
    public boolean hasMoreElements()
    {
      return hasNext();
    }

    /**
     * Iterator syntax
     */
    public boolean hasNext()
    {
      if (nextHeader == null)
        {
          nextHeader = getNext();
        }
      return (nextHeader != null);
    }
    
    /**
     * Enumeration syntax
     */
    public Object nextElement()
    {
      return next();
    }

    /**
     * Iterator syntax
     */
    public Object next()
    {
      if (nextHeader == null)
        {
          nextHeader = getNext();
        }
      if (nextHeader == null)
        {
          throw new NoSuchElementException();
        }
      
      InternetHeader header = nextHeader;
      nextHeader = null;
      
      if (stringForm)
        {
          return header.line;
        }
      return header;
    }

    public void remove()
    {
      throw new UnsupportedOperationException();
    }
    
    private InternetHeader getNext()
    {
      while (source.hasNext()) 
        {
          InternetHeader header = (InternetHeader) source.next();
          if (header.line == null)
            {
              continue;
            }
          
          if (names == null)
            {
              return (matching) ? null : header;
            }
          
          for (int i = 0; i < names.length; i++)
            {
              if (!header.nameEquals(names[i]))
                {
                  continue;
                }
              
              if (matching)
                {
                  return header;
                }
              
              return getNext();
            }
          
          if (!matching)
            {
              return header;
            }
        }
      return null;
    }
  
  }
  
  /*
   * The list of headers.
   */
  private ArrayList headers = new ArrayList(20);

  /**
   * Create an empty InternetHeaders object.
   */
  public InternetHeaders()
  {
    headers.add(new InternetHeader("Return-Path", null));
    headers.add(new InternetHeader("Received", null));
    headers.add(new InternetHeader("Message-Id", null));
    headers.add(new InternetHeader("Resent-Date", null));
    headers.add(new InternetHeader("Date", null));
    headers.add(new InternetHeader("Resent-From", null));
    headers.add(new InternetHeader("From", null));
    headers.add(new InternetHeader("Reply-To", null));
    headers.add(new InternetHeader("To", null));
    headers.add(new InternetHeader("Subject", null));
    headers.add(new InternetHeader("Cc", null));
    headers.add(new InternetHeader("In-Reply-To", null));
    headers.add(new InternetHeader("Resent-Message-Id", null));
    headers.add(new InternetHeader("Errors-To", null));
    headers.add(new InternetHeader("Mime-Version", null));
    headers.add(new InternetHeader("Content-Type", null));
    headers.add(new InternetHeader("Content-Transfer-Encoding", null));
    headers.add(new InternetHeader("Content-MD5", null));
    headers.add(new InternetHeader("Content-Length", null));
    headers.add(new InternetHeader("Status", null));
  }

  /**
   * Read and parse the given rfc822 message stream till the blank line
   * separating the header from the body.
   * The input stream is left positioned at the start of the body.
   * The header lines are stored internally.
   * <p>
   * For efficiency, wrap a BufferedInputStream around the actual input 
   * stream and pass it as the parameter.
   * @param is an rfc822 input stream
   */
  public InternetHeaders(InputStream is)
    throws MessagingException
  {
    load(is);
  }

  /**
   * Read and parse the given rfc822 message stream till the blank line
   * separating the header from the body.
   * Store the header lines inside this InternetHeaders object.
   * <p>
   * Note that the header lines are added into this InternetHeaders object,
   * so any existing headers in this object will not be affected.
   * @param is an rfc822 input stream
   */
  public void load(InputStream is)
    throws MessagingException
  {
    LineInputStream in = new LineInputStream(is);
    try
      {
        for (String line = in.readLine(); line != null; line = in.readLine()) 
          {
            line = trim(line);
            if (line.length() == 0)
              {
                break;
              }
            addHeaderLine(line);
          }
      }
    catch (IOException e)
      {
        throw new MessagingException("I/O error", e);
      }
  }

  /**
   * Return all the values for the specified header.
   * The values are String objects.
   * @param name the header name
   */
  public String[] getHeader(String name)
  {
    ArrayList acc = new ArrayList(headers.size());
    for (Iterator i = headers.iterator(); i.hasNext(); ) 
      {
        InternetHeader header = (InternetHeader) i.next();
        if (header.nameEquals(name) && header.line != null)
          {
            acc.add(header.getValue());
          }
      }
    int size = acc.size();
    if (size == 0)
      {
        return null;
      }
    String[] h = new String[size];
    acc.toArray(h);
    return h;
  }

  /**
   * Get all the headers for this header name, returned as a single String,
   * with headers separated by the delimiter.
   * If the delimiter is null, only the first header is returned.
   * @param name the header name
   * @param delimiter the delimiter
   * @return the value fields for all headers with this name
   */
  public String getHeader(String name, String delimiter)
  {
    String[] h = getHeader(name);
    if (h == null)
      {
        return null;
      }
    
    if (delimiter == null || h.length == 1)
      {
        return h[0];
      }

    StringBuffer buffer = new StringBuffer();
    for(int i = 0; i < h.length; i++)
      {
        if (i > 0)
          {
            buffer.append(delimiter);
          }
        buffer.append(h[i]);
      }
    return buffer.toString();
  }

  /**
   * Change the first header line that matches <code>name</code> to have 
   * <code>value</code>, adding a new header if no existing header matches.
   * Remove all matching headers but the first.
   * <p>
   * Note that RFC822 headers can only contain US-ASCII characters
   * @param name the header name
   * @param value the header value
   */
  public void setHeader(String name, String value)
  {
    boolean first = true;
    for (int i = 0; i < headers.size(); i++)
    {
      InternetHeader header = (InternetHeader) headers.get(i);
      if (header.nameEquals(name))
        {
          if (first)
            {
              header.setValue(value);
              first = false;
            }
          else
            {
              headers.remove(i);
              i--;
            }
        }
    }
    if (first)
      {
        addHeader(name, value);
      }
  }

  /**
   * Add a header with the specified name and value to the header list.
   * <p>
   * Note that RFC822 headers can only contain US-ASCII characters.
   * @param name the header name
   * @param value the header value
   */
  public void addHeader(String name, String value)
  {
    synchronized (headers)
      {
        int len = headers.size();
        for (int i = len - 1; i >= 0; i--)
          {
            InternetHeader header = (InternetHeader) headers.get(i);
            if (header.nameEquals(name))
              {
                headers.add(i + 1, new InternetHeader(name, value));
                return;
              }
            if (header.nameEquals(":"))
              {
                len = i;
              }
          }
        headers.add(len, new InternetHeader(name, value));
      }
  }

  /**
   * Remove all header entries that match the given name
   * @param name header name
   */
  public void removeHeader(String name)
  {
    synchronized (headers)
      {
        int len = headers.size();
        for (int i = 0; i < len; i++)
          {
            InternetHeader header = (InternetHeader) headers.get(i);
            if (header.nameEquals(name))
              {
                header.line = null;
              }
          }
      }
  }

  /**
   * Return all the headers as an Enumeration of Header objects
   */
  public Enumeration getAllHeaders()
  {
    return new HeaderEnumeration(headers.iterator(), null, false, false);
  }

  /**
   * Return all matching Header objects
   * @param names the names to match
   */
  public Enumeration getMatchingHeaders(String[] names)
  {
    return new HeaderEnumeration(headers.iterator(), names, false, true);
  }

  /**
   * Return all non-matching Header objects
   * @param names the names not to match
   */
  public Enumeration getNonMatchingHeaders(String[] names)
  {
    return new HeaderEnumeration(headers.iterator(), names, false, false);
  }

  /**
   * Add an RFC822 header line to the header store.
   * If the line starts with a space or tab(a continuation line),
   * add it to the last header line in the list.
   * <p>
   * Note that RFC822 headers can only contain US-ASCII characters
   * @param line raw rfc822 header line
   */
  public void addHeaderLine(String line)
  {
    try
      {
        char c = line.charAt(0);
        if (c == ' ' || c == '\t') // continuation character
          {
            int len = headers.size();
            InternetHeader header = (InternetHeader) headers.get(len - 1);
            StringBuffer buffer = new StringBuffer();
            buffer.append(header.line);
            buffer.append("\r\n");
            buffer.append(line);
            header.line = buffer.toString();
          }
        else
          {
            synchronized (headers)
              {
                headers.add(new InternetHeader(line));
              }
          }
      }
    catch (StringIndexOutOfBoundsException e)
      {
      }
    catch (NoSuchElementException e)
      {
      }
  }

  /**
   * Return all the header lines as an Enumeration of Strings.
   */
  public Enumeration getAllHeaderLines()
  {
    return new HeaderEnumeration(headers.iterator(), null, true, false);
  }

  /**
   * Return all matching header lines as an Enumeration of Strings.
   */
  public Enumeration getMatchingHeaderLines(String[] names)
  {
    return new HeaderEnumeration(headers.iterator(), names, true, true);
  }

  /**
   * Return all non-matching header lines
   */
  public Enumeration getNonMatchingHeaderLines(String[] names)
  {
    return new HeaderEnumeration(headers.iterator(), names, true, false);
  }

  private static String trim(String line)
  {
    int len = line.length();
    if (len > 0 && line.charAt(len - 1) == '\r')
      {
        line = line.substring(0, len - 1);
      }
    return line;
  }
  
}
