/*
 * Article.java
 * Copyright (C) 1999 dog <dog@dog.net.uk>
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
 * 
 * You may retrieve the latest version of this library from
 * http://www.dog.net.uk/knife/
 */

package gnu.mail.providers.nntp;

import java.io.*;
import java.util.*;
import javax.activation.DataHandler;
import javax.mail.*;
import javax.mail.internet.*;

/**
 * The message class implementing the NNTP mail protocol.
 *
 * @author dog <dog@dog.net.uk>
 * @author Torgeir Veimo <tv@sevenmountains.no>
 * @version 1.4.1
 */
public class Article 
extends MimeMessage 
{

  /**
   * The unique message-id of this message.
   */
  protected String messageId;

  // Whether the headers for this article were retrieved using xover.
  boolean xoverHeaders = false;

  /**
   * Creates an NNTP message with the specified article number.
   */
  protected Article(Newsgroup folder, int msgnum) 
  throws MessagingException 
  {
    super(folder, msgnum);
  }

  /**
   * Creates an NNTP message with the specified message-id.
   */
  protected Article(Newsgroup folder, String messageId) 
  throws MessagingException 
  {
    super(folder, 0);
    this.messageId = messageId;
  }

  // Adds headers retrieved by an xover call to this article.
  void addXoverHeaders(InternetHeaders headers) 
  {
    this.headers = headers;
    xoverHeaders = true;
    headers.addHeader("Newsgroups", folder.getName());
  }
	
  /**
   * Set the specified flag for this message.
   * Articles only support the SEEN flag. Other flag types will be ignored.
   */
  public void setFlag(Flags.Flag flag, boolean set) 
  throws MessagingException 
  {
    if (Flags.Flag.SEEN.equals(flag))
    ((Newsgroup)folder).setSeen(getArticleNumber(), set);
  }
	
  /**
   * Set the flags for this message.
   * Articles only support the SEEN flag. Other flag types will be ignored.
   */
  public void setFlags(Flags flags, boolean set) 
  throws MessagingException 
  {
    if (flags!=null && flags.contains(Flags.Flag.SEEN))
    ((Newsgroup)folder).setSeen(getArticleNumber(), set);
  }
	
  /**
   * Return a Flags object containing the flags for this message.
   */
  public Flags getFlags() 
  {
    if (((Newsgroup)folder).isSeen(getArticleNumber()))
    return new Flags(Flags.Flag.SEEN);
    else
    return new Flags();
  }

  int getArticleNumber() 
  {
    return getMessageNumber();
  }

  /**
   * Produce the raw bytes of the content.
   */
  protected InputStream getContentStream() 
  throws MessagingException 
  {
    if (content==null) 
    content = ((NNTPStore)folder.getStore()).getContent(this);
    return super.getContentStream();
  }

  /**
   * Many news article addresses don't conform to spec.
   */
  public Address[] getFrom() 
  throws MessagingException 
  {
    Address a[] = getAddressHeader("From");
    if (a==null)
    a = getAddressHeader("Sender");
    return a;
  }
  /**
   * Returns the recipients' addresses for the specified RecipientType.
   */
  public Address[] getRecipients(RecipientType type) 
  throws MessagingException 
  {
    if (type==RecipientType.NEWSGROUPS) 
    {
      String key = getHeader("Newsgroups", ",");
      if (key==null) return null;
      return NewsAddress.parse(key);
    }
    else
    {
      return getAddressHeader(getHeaderKey(type));
    }
  }
	
  private void headerCheck(String name) 
  throws MessagingException 
  {
    NNTPStore s = (NNTPStore)folder.getStore();
    if (name!=null && xoverHeaders) 
    {
      boolean valid = s.validateOverviewHeader(name);
      if (!valid) 
      {
	if (s.getSession().getDebug())
	System.err.println("DEBUG: nntp: "+name+" is not in the overview headers, reloading all headers");
	headers = null;
      }
    }
    if (headers==null) 
    {
      headers = s.getHeaders(this);
      xoverHeaders = false;
    }
  }

  /**
   * Get all the headers for this header_name. Note that certain headers may
   * be encoded as per RFC 2047 if they contain non US-ASCII characters and
   * these should be decoded.
   */
  public String[] getHeader(String name) throws MessagingException 
  {
    headerCheck(name);
    return super.getHeader(name);
  }

  /**
   * Get all the headers for this header name, returned as a single String,
   * with headers separated by the delimiter. If the delimiter is null, only
   * the first header is returned.
   */
  public String getHeader(String name, String delimiter) 
  throws MessagingException 
  {
    headerCheck(name);
    return super.getHeader(name, delimiter);
  }
	
  /**
   * Returns an enumeration of all the headers.
   */
  public Enumeration getAllHeaders() 
  throws MessagingException 
  {
    headerCheck(null);
    return super.getAllHeaders();
  }

  /**
   * Returns an enumeration of all the headers matching the specified expression.
   */
  public Enumeration getMatchingHeaders(String[] expr) 
  throws MessagingException 
  {
    headerCheck(null);
    return super.getMatchingHeaders(expr);
  }

  /**
   * Returns an enumeration of all the headers not matching the specified expression.
   */
  public Enumeration getNonMatchingHeaders(String[] expr) 
  throws MessagingException 
  {
    headerCheck(null);
    return super.getNonMatchingHeaders(expr);
  }

  public Enumeration getAllHeaderLines() 
  throws MessagingException 
  {
    headerCheck(null);
    return headers.getAllHeaderLines();
  }

  public Enumeration getMatchingHeaderLines(String[] expr) 
  throws MessagingException 
  {
    headerCheck(null);
    return headers.getMatchingHeaderLines(expr);
  }

  public Enumeration getNonMatchingHeaderLines(String[] expr) 
  throws MessagingException 
  {
    headerCheck(null);
    return headers.getNonMatchingHeaderLines(expr);
  }

  /**
   * Returns an array of addresses for the specified header key.
   */
  protected Address[] getAddressHeader(String key) 
  throws MessagingException 
  {
    String header = getHeader(key, ",");
    if (header==null) 
    return null;
    try 
    {
      return InternetAddress.parse(header);
    }
    catch (AddressException e) 
    {
      String message = e.getMessage();
      if (message!=null && message.indexOf("@domain")>-1) 
      {
	try 
	{
	  return parseAddress(header, ((NNTPStore)folder.getStore()).getHostName());
	}
	catch (AddressException e2) 
	{
	  throw new MessagingException("Invalid address: "+header, e);
	}
      }
      try 
      {
	InternetAddress[] a = new InternetAddress[1];
	a[0] = new InternetAddress();
	a[0].setPersonal(header);
	return a;
      }
      catch (UnsupportedEncodingException e2) 
      {
	return null;
      }
    }
  }

  /**
   * Makes a pass at parsing internet addresses.
   */
  protected Address[] parseAddress(String in, String defhost) 
  throws AddressException 
  {
    Vector v = new Vector();
    for (StringTokenizer st = new StringTokenizer(in, ","); st.hasMoreTokens(); ) 
    {
      String s = st.nextToken().trim();
      try 
      {
	v.addElement(new InternetAddress(s));
      }
      catch (AddressException e) 
      {
	int index = s.indexOf('>');
	if (index>-1)
	{ // name <address>
	  StringBuffer buffer = new StringBuffer();
	  buffer.append(s.substring(0, index));
	  buffer.append('@');
	  buffer.append(defhost);
	  buffer.append(s.substring(index));
	  v.addElement(new InternetAddress(buffer.toString()));
	}
	else 
	{
	  index = s.indexOf(" (");
	  if (index>-1)
	  { // address (name)
	    StringBuffer buffer = new StringBuffer();
	    buffer.append(s.substring(0, index));
	    buffer.append('@');
	    buffer.append(defhost);
	    buffer.append(s.substring(index));
	    v.addElement(new InternetAddress(buffer.toString()));
	  }
	  else // address
	  v.addElement(new InternetAddress(s+"@"+defhost));
	}

      }
    }
    Address[] a = new Address[v.size()]; v.copyInto(a);
    return a;
  }

  /**
   * Returns the header key for the specified RecipientType.
   */
  protected String getHeaderKey(RecipientType type) 
  throws MessagingException 
  {
    if (type==RecipientType.TO)
    return "To";
    if (type==RecipientType.CC)
    return "Cc";
    if (type==RecipientType.BCC)
    return "Bcc";
    if (type==RecipientType.NEWSGROUPS)
    return "Newsgroups";
    throw new MessagingException("Invalid recipient type: "+type);
  }

  // -- Need to override these since we are read-only --

  /**
   * NNTP messages are read-only.
   */
  public void setFrom(Address address) 
  throws MessagingException
  {
    throw new IllegalWriteException("Article is read-only");
  }

  /**
   * NNTP messages are read-only.
   */
  public void addFrom(Address a[]) 
  throws MessagingException 
  {
    throw new IllegalWriteException("Article is read-only");
  }

  /**
   * NNTP messages are read-only.
   */
  public void setRecipients(javax.mail.Message.RecipientType recipienttype, Address a[]) 
  throws MessagingException 
  {
    throw new IllegalWriteException("Article is read-only");
  }

  /**
   * NNTP messages are read-only.
   */
  public void addRecipients(javax.mail.Message.RecipientType recipienttype, Address a[]) 
  throws MessagingException 
  {
    throw new IllegalWriteException("Article is read-only");
  }

  /**
   * NNTP messages are read-only.
   */
  public void setReplyTo(Address a[]) 
  throws MessagingException 
  {
    throw new IllegalWriteException("Article is read-only");
  }

  /**
   * NNTP messages are read-only.
   */
  public void setSubject(String s, String s1) 
  throws MessagingException 
  {
    throw new IllegalWriteException("Article is read-only");
  }

  /**
   * NNTP messages are read-only.
   */
  public void setSentDate(Date date) 
  throws MessagingException 
  {
    throw new IllegalWriteException("Article is read-only");
  }

  /**
   * NNTP messages are read-only.
   */
  public void setDisposition(String s) 
  throws MessagingException 
  {
    throw new IllegalWriteException("Article is read-only");
  }

  /**
   * NNTP messages are read-only.
   */
  public void setContentID(String s) 
  throws MessagingException 
  {
    throw new IllegalWriteException("Article is read-only");
  }

  /**
   * NNTP messages are read-only.
   */
  public void setContentMD5(String s) 
  throws MessagingException 
  {
    throw new IllegalWriteException("Article is read-only");
  }

  /**
   * NNTP messages are read-only.
   */
  public void setDescription(String s, String s1) 
  throws MessagingException 
  {
    throw new IllegalWriteException("Article is read-only");
  }

  /**
   * NNTP messages are read-only.
   */
  public void setDataHandler(DataHandler datahandler) 
  throws MessagingException 
  {
    throw new IllegalWriteException("Article is read-only");
  }

}
