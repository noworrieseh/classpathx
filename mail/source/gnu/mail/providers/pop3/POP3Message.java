
/*
 * POP3Message.java
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

package dog.mail.pop3;

import java.io.*;
import java.util.*;
import javax.activation.DataHandler;
import javax.mail.*;
import javax.mail.internet.*;

/**
 * The message class implementing the POP3 mail protocol.
 *
 * @author dog@dog.net.uk
 * @author nferrier@tapsellferrier.co.uk
 * @version 1.1.1
 */
public class POP3Message 
extends MimeMessage 
{

  /*
   * a flag to tell us when message header has been read and when it hasn't.
   */
  boolean isHeaderRead=false;

  /*
   * a flag to tell us when message content has been read and when it hasn't.
   */
  boolean isContentRead=false;

  /*
   * a size that can be set by the object constructing this message.
   */
  int msgSize=-1;

  protected POP3Message(POP3Folder folder,InputStream in,int msgnum,int size) 
  throws MessagingException 
  {
    this(folder,in,msgnum);
    msgSize=size;
  }

  /** create a POP3Message.
   * This is called by the POP3Store.
   *
   * <p>The constructor is smart about what data it retrieves. If the stream
   * contains no data then the object is left in a state where the headers and the content
   * must be read. If the constructor manages to read the headers it will acknowledge
   * their reciept in the internal state of the object, the content will be read in
   * if the user causes it to be necessary. Finally, the constructor may see the whole
   * content on the stream in which case it reads in everything.</p>
   */
  protected POP3Message(POP3Folder folder, InputStream in, int msgnum) 
  throws MessagingException 
  {
    super(folder, msgnum);
    try 
    {
      if (!(in instanceof ByteArrayInputStream) && !(in instanceof BufferedInputStream))
      in = new BufferedInputStream(in);
      //are headers available?
      if(in.available()>0) 
      {
	headers = new InternetHeaders(in);
	isHeaderRead=true;
      }
      //this might contain the whole shebang so read it all in if it does
      if(in.available()>0) 
      {
	BufferedInputStream content=new BufferedInputStream(in);
	ByteArrayOutputStream bout=new ByteArrayOutputStream();
	byte[] readBuffer=new byte[POP3Store.fetchsize];
	int red=content.read(readBuffer,0,POP3Store.fetchsize);
	while(red>-1) 
	{
	  bout.write(readBuffer,0,red);
	  red=content.read(readBuffer,0,POP3Store.fetchsize);
	}
	super.content=bout.toByteArray();
	//if we've read the content without an error then set a flag to say
	//we don't have to read it again
	isContentRead=true;
      }
    } 
    catch(IOException e) 
    {
      throw new MessagingException("I/O error", e);
    }
  }


  //content handling methods

  /** retrive the content of the message.
   * This uses the POP3Store to do the retrieval.
   */
  public void retr() 
  throws MessagingException 
  {
    try 
    {
      POP3Store str=(POP3Store)folder.getStore();
      InputStream msgData=str.popRETR(msgnum);
      BufferedInputStream contentStr=new BufferedInputStream(msgData);
      //first read the headers again
      headers=new InternetHeaders(contentStr);
      isHeaderRead=true;
      //now store the content
      ByteArrayOutputStream bout=new ByteArrayOutputStream();
      byte[] readBuffer=new byte[POP3Store.fetchsize];
      int red=contentStr.read(readBuffer,0,POP3Store.fetchsize);
      while(red>-1) 
      {
	bout.write(readBuffer,0,red);
	red=contentStr.read(readBuffer,0,POP3Store.fetchsize);
      }
      super.content=bout.toByteArray();
      //if we've read the content without an error then set a flag to say
      //we don't have to read it again
      isContentRead=true;
    } 
    catch(IOException e) 
    {
      throw new MessagingException("can't get content",e);
    }
  }

  /** causes the content to be read in.
   */
  public Object getContent() 
  throws MessagingException, IOException 
  {
    if(!isContentRead)
    retr();
    return super.getContent();
  }

  /** causes the content to be read in.
   */
  public InputStream getInputStream() 
  throws MessagingException, IOException 
  {
    if(!isContentRead)
    retr();
    return super.getInputStream();
  }

  /** causes the content to be read in.
   */
  protected InputStream getContentStream() 
  throws MessagingException 
  {
    if(!isContentRead)
    retr();
    return super.getContentStream();
  }

  //header handling  operation methods

  /** causes the header to be read.
   */
  void top() 
  throws MessagingException 
  {
    try 
    {
      POP3Store str=(POP3Store)folder.getStore();
      InputStream headersInStr=str.popTOP(msgnum);
      BufferedInputStream headersIn=new BufferedInputStream(headersInStr);
      headers = new InternetHeaders(headersIn);
      isHeaderRead=true;
    } 
    catch(Exception e) 
    {
      throw new MessagingException("can't get content",e);
    }
  }

  /** gets the size of the message.
   * Uses the cached size if it's available to us.
   */
  public int getSize() 
  throws MessagingException 
  {
    if(msgSize>-1)
    return msgSize;
    return super.getSize();    
  }

  /**
   * Returns the from address.
   */
  public Address[] getFrom() 
  throws MessagingException 
  {
    if(!isHeaderRead)
    top();
    Address[] a = getAddressHeader("From");
    if (a==null) 
    a = getAddressHeader("Sender");
    return a;
  }

  /** get all the headers from the message.
   */
  public Enumeration getAllHeaders() 
  throws MessagingException 
  {
    if(!isHeaderRead)
    top();
    return super.getAllHeaders();
  }

  /** get all the headers from the message.
   */
  public Enumeration getAllHeaderLines() 
  throws MessagingException 
  {
    if(!isHeaderRead)
    top();
    return super.getAllHeaderLines();
  }

  /**
   * Returns the recipients' addresses.
   */
  public Address[] getRecipients(RecipientType type) 
  throws MessagingException 
  {
    if(!isHeaderRead)
    top();
    if (type!=RecipientType.NEWSGROUPS)
    return getAddressHeader(getHeaderKey(type));
    else 
    {
      String key = getHeader("Newsgroups", ",");
      if (key==null) 
      return null;
      return NewsAddress.parse(key);
    }
  }

  /**
   * Returns the reply-to address.
   */
  public Address[] getReplyTo() 
  throws MessagingException 
  {
    if(!isHeaderRead)
    top();
    Address[] a = getAddressHeader("Reply-To");
    if (a==null) 
    a = getFrom();
    return a;
  }

  /**
   * Returns an array of addresses for the specified header key.
   */
  protected Address[] getAddressHeader(String key) 
  throws MessagingException 
  {
    if(!isHeaderRead)
    top();
    String header = getHeader(key, ",");
    if (header==null) return null;
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
	  return parseAddress(header, ((POP3Store)folder.getStore()).getHostName());
	} 
	catch (AddressException e2) 
	{
	  throw new MessagingException("Invalid address: "+header, e);
	}
      }
      throw e;
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
	{
	  // name <address>
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
	  {
	    // address (name)
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

  //these are the message->message communications methods

  public void writeTo(OutputStream msgStream) 
  throws IOException, MessagingException 
  {
    if(!isContentRead)
    retr();
    super.writeTo(msgStream);
  }

  public void writeTo(OutputStream msgStream,String[] ignoreList) 
  throws IOException, MessagingException 
  {
    if(!isContentRead)
    retr();
    super.writeTo(msgStream,ignoreList);
  }

  // -- Need to override these since we are read-only --

  /**
   * POP3 messages are read-only.
   */
  public void setFrom(Address address) 
  throws MessagingException 
  {
    throw new IllegalWriteException("POP3Message is read-only");
  }

  /**
   * POP3 messages are read-only.
   */
  public void addFrom(Address aaddress[]) 
  throws MessagingException 
  {
    throw new IllegalWriteException("POP3Message is read-only");
  }

  /**
   * POP3 messages are read-only.
   */
  public void setRecipients(javax.mail.Message.RecipientType recipienttype, Address aaddress[]) 
  throws MessagingException 
  {
    throw new IllegalWriteException("POP3Message is read-only");
  }

  /**
   * POP3 messages are read-only.
   */
  public void addRecipients(javax.mail.Message.RecipientType recipienttype, Address aaddress[]) 
  throws MessagingException 
  {
    throw new IllegalWriteException("POP3Message is read-only");
  }

  /**
   * POP3 messages are read-only.
   */
  public void setReplyTo(Address aaddress[]) 
  throws MessagingException 
  {
    throw new IllegalWriteException("POP3Message is read-only");
  }

  /**
   * POP3 messages are read-only.
   */
  public void setSubject(String s, String s1) 
  throws MessagingException 
  {
    throw new IllegalWriteException("POP3Message is read-only");
  }

  /**
   * POP3 messages are read-only.
   */
  public void setSentDate(Date date) 
  throws MessagingException 
  {
    throw new IllegalWriteException("POP3Message is read-only");
  }

  /**
   * POP3 messages are read-only.
   */
  public void setDisposition(String s) 
  throws MessagingException 
  {
    throw new IllegalWriteException("POP3Message is read-only");
  }

  /**
   * POP3 messages are read-only.
   */
  public void setContentID(String s) 
  throws MessagingException 
  {
    throw new IllegalWriteException("POP3Message is read-only");
  }

  /**
   * POP3 messages are read-only.
   */
  public void setContentMD5(String s) 
  throws MessagingException 
  {
    throw new IllegalWriteException("POP3Message is read-only");
  }

  /**
   * POP3 messages are read-only.
   */
  public void setDescription(String s, String s1) 
  throws MessagingException 
  {
    throw new IllegalWriteException("POP3Message is read-only");
  }

  /**
   * POP3 messages are read-only.
   */
  public void setDataHandler(DataHandler datahandler) 
  throws MessagingException 
  {
    throw new IllegalWriteException("POP3Message is read-only");
  }

}
