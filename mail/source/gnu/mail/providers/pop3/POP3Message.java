/*
 * POP3Message.java
 * Copyright (C) 1999, 2003 Chris Burdess <dog@gnu.org>
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2 of the License, or (at your option) any later version.
 * 
 * You also have permission to link it with the Sun Microsystems, Inc. 
 * JavaMail(tm) extension and run that combination.
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

package gnu.mail.providers.pop3;

import java.io.InputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Enumeration;
import javax.activation.DataHandler;
import javax.mail.MessagingException;
import javax.mail.internet.InternetHeaders;

import gnu.mail.providers.ReadOnlyMessage;

/**
 * The message class implementing the POP3 mail protocol.
 *
 * @author <a href='mailto:dog@gnu.org'>Chris Burdess</a>
 * @author <a href='mailto:nferrier@tapsellferrier.co.uk'>Nic Ferrier</a>
 * @version 1.2
 */
public final class POP3Message 
  extends ReadOnlyMessage 
{

  /*
   * The size of this message, if predetermined.
   */
  int size;

  /**
   * Create a POP3Message.
   */
  POP3Message(POP3Folder folder, InputStream in, int msgnum) 
    throws MessagingException 
  {
    this(folder, in, msgnum, -1);
  }

  /** 
   * Create a POP3Message.
   * This is called by the POP3Store.
   *
   * <p>The constructor is smart about what data it retrieves. If the stream
   * contains no data then the object is left in a state where the headers and
   * the content must be read. If the constructor manages to read the headers
   * it will acknowledge their receipt in the internal state of the object,
   * the content will be read in if the user causes it to be necessary.
   * Finally, the constructor may see the whole content on the stream in which
   * case it reads in everything.</p>
   */
  POP3Message(POP3Folder folder, InputStream in, int msgnum, int size) 
    throws MessagingException 
  {
    super(folder, in, msgnum);
    this.size = size;
    POP3Headers h = (POP3Headers)headers;
    if (headers!=null && h.isEmpty())
      headers = null;
    if (content!=null && content.length==0)
      content = null;
  }

  // -- Content --

  /** 
   * Retrieves the content of the message.
   * This uses the POP3Store to do the retrieval.
   */
  void fetchContent() 
    throws MessagingException 
  {
    POP3Store str = (POP3Store)folder.getStore();
    parse(str.popRETR(msgnum));
  }

  /** 
   * Causes the content to be read in.
   */
  public DataHandler getDataHandler() 
    throws MessagingException
  {
    if (content==null)
      fetchContent();
    return super.getDataHandler();
  }

  /** 
   * Causes the content to be read in.
   */
  protected InputStream getContentStream() 
    throws MessagingException 
  {
    if (content==null)
      fetchContent();
    return super.getContentStream();
  }

  /** 
   * Gets the size of the message.
   * Uses the cached size if it's available to us.
   */
  public int getSize() 
    throws MessagingException 
  {
    if (size>-1)
      return size;
    if (content==null)
      fetchContent();
    return super.getSize();    
  }

  // -- Headers --

  /** 
   * Causes the headers to be read.
   */
  void fetchHeaders() 
    throws MessagingException 
  {
    POP3Store str = (POP3Store)folder.getStore();
    headers = createInternetHeaders(str.popTOP(msgnum));
  }

  /**
   * Causes the headers to be read.
   */
  public String[] getHeader(String name) 
    throws MessagingException 
  {
    if (headers==null)
      fetchHeaders();
    return super.getHeader(name);
  }
  
  /**
   * Causes the headers to be read.
   */
  public String getHeader(String name, String delimiter) 
    throws MessagingException 
  {
    if (headers==null)
      fetchHeaders();
    return super.getHeader(name, delimiter);
  }

  /** 
   * Causes the headers to be read.
   */
  public Enumeration getAllHeaders() 
    throws MessagingException 
  {
    if (headers==null)
      fetchHeaders();
    return super.getAllHeaders();
  }

  /** 
   * Causes the headers to be read.
   */
  public Enumeration getAllHeaderLines() 
    throws MessagingException 
  {
    if (headers==null)
      fetchHeaders();
    return super.getAllHeaderLines();
  }

  /** 
   * Causes the headers to be read.
   */
  public Enumeration getMatchingHeaders(String[] names) 
    throws MessagingException 
  {
    if (headers==null)
      fetchHeaders();
    return super.getMatchingHeaders(names);
  }

  /** 
   * Causes the headers to be read.
   */
  public Enumeration getMatchingHeaderLines(String[] names) 
    throws MessagingException 
  {
    if (headers==null)
      fetchHeaders();
    return super.getMatchingHeaderLines(names);
  }

  /** 
   * Causes the headers to be read.
   */
  public Enumeration getNonMatchingHeaders(String[] names) 
    throws MessagingException 
  {
    if (headers==null)
      fetchHeaders();
    return super.getNonMatchingHeaders(names);
  }

  /** 
   * Causes the headers to be read.
   */
  public Enumeration getNonMatchingHeaderLines(String[] names) 
    throws MessagingException 
  {
    if (headers==null)
      fetchHeaders();
    return super.getNonMatchingHeaderLines(names);
  }

  protected InternetHeaders createInternetHeaders(InputStream is)
    throws MessagingException
  {
    return new POP3Headers(is);
  }

  // -- Utility --

  public void writeTo(OutputStream msgStream) 
    throws IOException, MessagingException 
  {
    if (content==null)
      fetchContent();
    super.writeTo(msgStream);
  }

  public void writeTo(OutputStream msgStream, String[] ignoreList) 
    throws IOException, MessagingException 
  {
    if (content==null)
      fetchContent();
    super.writeTo(msgStream, ignoreList);
  }

}
