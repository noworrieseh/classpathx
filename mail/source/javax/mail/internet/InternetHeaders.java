/*
 * InternetHeaders.java
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

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import javax.mail.Header;
import javax.mail.MessagingException;
import gnu.mail.util.LineInputStream;

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
 * In addition, these header fields must be folded (wrapped) before being 
 * sent if they exceed the line length limitation for the transport 
 * (1000 bytes for SMTP). Received headers may have been folded.
 * The application is responsible for folding and unfolding headers 
 * as appropriate.
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
      name = (i<0) ? line.trim() : line.substring(0, i).trim();
      this.line = line;
    }
    
    InternetHeader(String name, String value)
    {
      super(name, null);
      if (value!=null)
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
      if (i<0)
        return line;
      
      int pos;
      for (pos = i+1; pos<line.length(); pos++)
      {
        char c = line.charAt(pos);
        if (c!=' ' && c!='\t' && c!='\r' && c!='\n')
          break;
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
  class HeaderEnumeration
    implements Enumeration
  {

    private Enumeration source;
    private String[] names;
    private boolean stringForm;
    private boolean matching;
    private InternetHeader next;
    
    HeaderEnumeration(Vector vector, String[] names,
        boolean stringForm, boolean matching)
    {
      source = vector.elements();
      this.names = names;
      this.stringForm = stringForm;
      this.matching = matching;
    }
    
    public boolean hasMoreElements()
    {
      if (next==null)
        next = getNext();
      return (next!=null);
    }
    
    public Object nextElement()
    {
      if (next==null)
        next = getNext();
      if (next==null)
        throw new NoSuchElementException();
      
      InternetHeader header = next;
      next = null;
      
      if (stringForm)
        return header.line;
      else
        return header;
    }
    
    private InternetHeader getNext()
    {
      while (source.hasMoreElements()) 
      {
        InternetHeader header = (InternetHeader)source.nextElement();
        if (header.line==null)
          continue;
        
        if (names==null)
          return (matching) ? null : header;

        for (int i = 0; i<names.length; i++)
        {
          if (!header.nameEquals(names[i]))
            continue;
          
          if (matching)
            return header;
          
          return getNext();
        }
        
        if (!matching)
          return header;
      }
      return null;
    }
  
  }
  
  /*
   * The list of headers.
   */
  private Vector headers = new Vector(20);

  /**
   * Create an empty InternetHeaders object.
   */
  public InternetHeaders()
  {
    headers.addElement(new InternetHeader("Return-Path", null));
    headers.addElement(new InternetHeader("Received", null));
    headers.addElement(new InternetHeader("Message-Id", null));
    headers.addElement(new InternetHeader("Resent-Date", null));
    headers.addElement(new InternetHeader("Date", null));
    headers.addElement(new InternetHeader("Resent-From", null));
    headers.addElement(new InternetHeader("From", null));
    headers.addElement(new InternetHeader("Reply-To", null));
    headers.addElement(new InternetHeader("To", null));
    headers.addElement(new InternetHeader("Subject", null));
    headers.addElement(new InternetHeader("Cc", null));
    headers.addElement(new InternetHeader("In-Reply-To", null));
    headers.addElement(new InternetHeader("Resent-Message-Id", null));
    headers.addElement(new InternetHeader("Errors-To", null));
    headers.addElement(new InternetHeader("Mime-Version", null));
    headers.addElement(new InternetHeader("Content-Type", null));
    headers.addElement(new InternetHeader("Content-Transfer-Encoding", null));
    headers.addElement(new InternetHeader("Content-MD5", null));
    headers.addElement(new InternetHeader("Content-Length", null));
    headers.addElement(new InternetHeader("Status", null));
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
      for (String line = in.readLine(); line!=null; line = in.readLine()) 
      {
        if (line.length()==0)
          break;
        addHeaderLine(line);
      }
      return;
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
    Vector acc = new Vector();
    for (Enumeration e = headers.elements(); e.hasMoreElements(); ) 
    {
      InternetHeader header = (InternetHeader)e.nextElement();
      if (header.nameEquals(name) && header.line!=null)
        acc.addElement(header.getValue());
    }
    if (acc.size()==0)
      return null;
    else
    {
      String[] h = new String[acc.size()];
      acc.copyInto(h);
      return h;
    }
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
    if (h==null)
      return null;
    
    if (delimiter==null || h.length==1)
      return h[0];

    StringBuffer buffer = new StringBuffer();
    for(int i = 0; i<h.length; i++)
    {
      if (i>0)
        buffer.append(delimiter);
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
    for (int i = 0; i<headers.size(); i++)
    {
      InternetHeader header = (InternetHeader)headers.elementAt(i);
      if (header.nameEquals(name))
      {
        if (first)
        {
          header.setValue(value);
          first = false;
        }
        else
        {
          headers.removeElementAt(i);
          i--;
        }
      }
    }
    if (first)
      addHeader(name, value);
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
    int len = headers.size();
    for (int i = len-1; i>=0; i--)
    {
      InternetHeader header = (InternetHeader)headers.elementAt(i);
      if (header.nameEquals(name))
      {
        headers.insertElementAt(new InternetHeader(name, value), i+1);
        return;
      }
      if (header.nameEquals(":"))
        len = i;
    }
    headers.insertElementAt(new InternetHeader(name, value), len);
  }

  /**
   * Remove all header entries that match the given name
   * @param name header name
   */
  public void removeHeader(String name)
  {
    for (int i = 0; i<headers.size(); i++)
    {
      InternetHeader header = (InternetHeader)headers.elementAt(i);
      if (header.nameEquals(name))
        header.line = null;
    }
  }

  /**
   * Return all the headers as an Enumeration of Header objects
   */
  public Enumeration getAllHeaders()
  {
    return new HeaderEnumeration(headers, null, false, false);
  }

  /**
   * Return all matching Header objects
   * @param names the names to match
   */
  public Enumeration getMatchingHeaders(String[] names)
  {
    return new HeaderEnumeration(headers, names, false, true);
  }

  /**
   * Return all non-matching Header objects
   * @param names the names not to match
   */
  public Enumeration getNonMatchingHeaders(String[] names)
  {
    return new HeaderEnumeration(headers, names, false, false);
  }

  /**
   * Add an RFC822 header line to the header store.
   * If the line starts with a space or tab (a continuation line),
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
      if (c==' ' || c=='\t') // continuation character
      {
        InternetHeader header = (InternetHeader)headers.lastElement();
        StringBuffer buffer = new StringBuffer();
        buffer.append(header.line);
        buffer.append("\r\n");
        buffer.append(line);
        header.line = buffer.toString();
      }
      else
        headers.addElement(new InternetHeader(line));
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
    return new HeaderEnumeration(headers, null, true, false);
  }

  /**
   * Return all matching header lines as an Enumeration of Strings.
   */
  public Enumeration getMatchingHeaderLines(String[] names)
  {
    return new HeaderEnumeration(headers, names, true, true);
  }

  /**
   * Return all non-matching header lines
   */
  public Enumeration getNonMatchingHeaderLines(String[] names)
  {
    return new HeaderEnumeration(headers, names, true, false);
  }
  
}
