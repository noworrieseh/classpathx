/*
  GNU Javamail IMAP provider
  Copyright (C) N.J.Ferrier, Tapsell-Ferrier Limited 2000,2001 for the OJE project

  For more information on this please mail: nferrier@tapsellferrier.co.uk

  This program is free software; you can redistribute it and/or
  modify it under the terms of the GNU Lesser General Public License
  as published by the Free Software Foundation; either version 2
  of the License, or (at your option) any later version.

  This program is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  GNU General Public License for more details.

  You should have received a copy of the GNU Lesser General Public License
  along with this program; if not, write to the Free Software
  Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
*/
package gnu.mail.providers.imap;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.StringReader;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Enumeration;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.activation.DataHandler;
import javax.mail.Address;
import javax.mail.IllegalWriteException;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.InternetHeaders;
import javax.mail.internet.NewsAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.AddressException;


import gnu.mapping.InPort;
import gnu.kawa.lispexpr.ScmRead;
import gnu.lists.LList;
import gnu.text.SyntaxException;


/** a representation of a message coming from an IMAP store.
 *
 * @author Nic Ferrier - Tapsell-Ferrier Limited
 */
public class IMAPMessage
extends MimeMessage
{

  /** the correctly cast IMAPStore.
   */
  private IMAPStore imapStore;

  /** the folder cast to the convieniant type.
   */
  IMAPFolder imapFolder;


  //status data about the state of this message container

  /** is the header cached?
   */
  boolean isHeaderCached=false;

  /** is the content cached?
   */
  boolean isContentCached=false;

  /** is the body structure read?
   */
  boolean isStructureRead=false;


  //data about the message

  /** the s-expression containing the body structure.
   */
  LList bodyStructure=null;

  /** the message size.
   */
  int msgSize=-1;


  /** create a representation of an IMAP message.
   *
   * @param store the store which holds this message
   * @param folder the folder creating this message
   * @param msgNumber the number of this message
   */
  IMAPMessage(IMAPStore store,IMAPFolder folder,int msgNumber)
  throws MessagingException
  {
    super(folder,msgNumber);
    imapStore=store;
    imapFolder=folder;
    if(false)
    throw new MessagingException("dummy stuff");
  }


  //content handling

  /** buffer the content of the mail message.
   * This is the method that actually retrieves the message
   * content and stores it in the content buffer.
   *
   * <p>A side effect of this method is that it reads the
   * header as well... it does that because one can either
   * read the whole message (including the header) or just
   * particular parts. There's no way to read all the parts
   * without the header.</p>
   *
   * <p>I might be able to fix that by using the body structure
   * to read all the parts one after the other.</p>
   *
   * @see javax.mail.internet.MimeMessage#content  the content buffer
   */
  void readContentBuffer()
  throws MessagingException
  {
    try 
    {
      InputStream in=readRawStream(-1,-1,-1);
      //we must synch access to stop wierd things happening
      synchronized(imapStore)
      {
	//are headers available?
	if(in.available()>0) 
	{
	  headers = new InternetHeaders(in);
	  isHeaderCached=true;
	}
	//this might contain the whole shebang so read it all in if it does
	if(in.available()>0) 
	{
	  ByteArrayOutputStream bout=new ByteArrayOutputStream();
	  byte[] readBuffer=new byte[imapStore.fetchSize];
	  int red=in.read(readBuffer,0,imapStore.fetchSize);
	  while(red>-1) 
	  {
	    bout.write(readBuffer,0,red);
	    red=in.read(readBuffer,0,imapStore.fetchSize);
	  }
	  content=bout.toByteArray();
	  //if we've read the content without an error then set a flag to say
	  //we don't have to read it again
	  isContentCached=true;
	}
      }
    } 
    catch(IOException e) 
    {
      throw new MessagingException("I/O error", e);
    }
  }

  /** get the raw (ie: possibly encoded) content from the specified part.
   * There is a catch to doing this. The stream returned is just a wrapper
   * stream on the udnerlying connection stream. You can do bizzare things
   * to the underlying IMAP if you are in a multi-thread setup using the
   * IMAPConnection. For example, if you call this and then another thread
   * performs another op[ the other thread may well read your input. You
   * can get round this by synchronizing access to the IMAPMessage object
   * or by not calling this method yourself unless you know you're in a
   * single thread state.
   *
   * <p><h4>What is this method for if it's so dangerous?</h4>
   * Sometimes you want to inline the return of message data and not cause
   * it to be cached. For example in a webmail application you don't want
   * to cache a 1Mb JPG file attachment that you're just going to send
   * straight back to the browser. You want to inline the stream, reading
   * small chunks out of javamail and writing them to the destination.
   * This method allows you to do that. Remember in a webmail app there
   * is probably only one thread accessing each IMAPConnection (because
   * users don't have multiple web sessions for webmail tools) so it's
   * okay to use this.</p>
   *
   * @param part if this is multipart then part refers to the part inside the multipart
   * @param offset the byte offset to start reading
   * @param len the number of bytes to read
   * @throws IllegalStateException if part>1 and message is not a multipart
   * @throws MessagingException if something goes wrong with IMAP
   * @see IMAPConnection#fetchBody for more details about part and offset/len numbers
   */
  public InputStream readRawStream(int part,int offset,int len)
  throws MessagingException
  {
    //test part against body structure first...
    try
    {
      InputStream in=null;
      synchronized(imapStore.connectionLock)
      {
	IMAPConnection con=imapStore.getConnection(imapFolder.folderName);
	in=con.fetchBody(msgnum,part,offset,len);
      }
      return in;
    }
    catch(IMAPException imap)
    {
      throw new MessagingException("IMAP failure: "+imap.getMessage());
    }
  }

  /** read the body structure into the local var.
   */
  public void readBodyStructure()
  throws MessagingException
  {
    try
    {
      LList bs=null;
      synchronized(imapStore.connectionLock)
      {
	IMAPConnection con=imapStore.getConnection(imapFolder.folderName);
	bs=con.fetchBodyStructure(msgnum);
      }
      bodyStructure=bs;
      if(bodyStructure==null)
      throw new MessagingException("couldn't read the structure of message "+msgnum);
    }
    catch(IMAPException imap)
    {
      throw new MessagingException("IMAP failure: "+imap.getMessage());
    }
  }


  //content methods overridden from MIMEMessage

  /** javamail:: gets the size of the message.
   * Uses the cached size if it's available to us.
   */
  public int getSize() 
  throws MessagingException 
  {
    if(msgSize>-1)
    return msgSize;
    //we have to read the size from the bodystructure
    if(bodyStructure==null)
    readBodyStructure();
    return msgSize;
  }

  /** javamail:: get the raw stream from the server.
   * The content of the entire message is returned. If the content is
   * buffered then a stream onto the content is returned. Otherwise
   * the content is returned directly from the server.
   *
   * @see IMAPFETCHInputStream which is returned if the content buffer is empty
   */
  public InputStream getRawInputStream()
  throws MessagingException
  {
    if(isContentCached)
    return new ByteArrayInputStream(content);
    /***
	if we want this to optional buffer content we have to
	do wrap this with another stream
    ***/
    //specify the part as -1 as this means: the whole message
    return new IMAPFETCHInputStream(this,-1);
  }

  /** javamail:: get the content associated with this message.
   * This has to follow the rules of Sun's provider (more or less).
   */
  public Object getContent()
  throws MessagingException,IOException
  {
    if(!isContentCached)
    readContentBuffer();
    return super.getContent();
  }

  /** javamail:: get the raw (ie: encoded) stream.
   * The data is left on the stream and not buffered.
   * This means that the application should read the data
   * before it does anything else.
   *
   * @return the data inline in the stream.
   */
  protected InputStream getContentStream()
  throws MessagingException
  {
    if(!isContentCached)
    readContentBuffer();
    return new ByteArrayInputStream(content);
  }


  //header methods

  /** read the header of the mail message.
   * @see javax.mail.internet.MimeMessage#headers which is created by this
   */
  protected void readHeader()
  throws MessagingException
  {
    try 
    {
      InputStream in=readRawStream(0,-1,-1);
      //we must synch access to stop wierd things happening
      synchronized(imapStore)
      {
	//are headers available?
	if(in.available()>0) 
	{
	  headers = new InternetHeaders(in);
	  //we might have to force the end of stream
	  isHeaderCached=true;
	}
      }
    } 
    catch(IOException e) 
    {
      throw new MessagingException("I/O error", e);
    }
  }

  /** returns the from address.
   */
  public Address[] getFrom() 
  throws MessagingException 
  {
    if(!isHeaderCached)
    readHeader();
    return super.getFrom();
  }

  /** get all the headers from the message.
   */
  public Enumeration getAllHeaders() 
  throws MessagingException 
  {
    if(!isHeaderCached)
    readHeader();
    return super.getAllHeaders();
  }

  /** get all the headers from the message.
   */
  public Enumeration getAllHeaderLines() 
  throws MessagingException 
  {
    if(!isHeaderCached)
    readHeader();
    return super.getAllHeaderLines();
  }

  /** returns the recipients' addresses.
   */
  public Address[] getRecipients(RecipientType type) 
  throws MessagingException 
  {
    if(!isHeaderCached)
    readHeader();
    return super.getRecipients(type);
  }

  /** returns the reply-to address.
   */
  public Address[] getReplyTo() 
  throws MessagingException 
  {
    if(!isHeaderCached)
    readHeader();
    return super.getReplyTo();
  }


  //these are the message->message communications methods

  public void writeTo(OutputStream msgStream) 
  throws IOException, MessagingException 
  {
    if(!isContentCached)
    readContentBuffer();
    super.writeTo(msgStream);
  }

  public void writeTo(OutputStream msgStream,String[] ignoreList) 
  throws IOException, MessagingException 
  {
    if(!isContentCached)
    readContentBuffer();
    super.writeTo(msgStream,ignoreList);
  }


  //add methods go here: should all throw exceptions - we don't allow alteration

  /** message are read-only.
   */
  public void setFrom(Address address) 
  throws MessagingException 
  {
    throw new IllegalWriteException("this is read-only");
  }

  /** messages are read-only.
   */
  public void addFrom(Address address[]) 
  throws MessagingException 
  {
    throw new IllegalWriteException("this is read-only");
  }

  /** messages are read-only.
   */
  public void setRecipients(javax.mail.Message.RecipientType recipienttype, Address aaddress[]) 
  throws MessagingException 
  {
    throw new IllegalWriteException("this is read-only");
  }

  /** messages are read-only.
   */
  public void addRecipients(javax.mail.Message.RecipientType recipienttype, Address aaddress[]) 
  throws MessagingException 
  {
    throw new IllegalWriteException("this is read-only");
  }

  /** messages are read-only.
   */
  public void setReplyTo(Address aaddress[]) 
  throws MessagingException 
  {
    throw new IllegalWriteException("this is read-only");
  }

  /** messages are read-only.
   */
  public void setSubject(String s, String s1) 
  throws MessagingException 
  {
    throw new IllegalWriteException("this is read-only");
  }

  /** messages are read-only.
   */
  public void setSentDate(Date date) 
  throws MessagingException 
  {
    throw new IllegalWriteException("this is read-only");
  }

  /** messages are read-only.
   */
  public void setDisposition(String s) 
  throws MessagingException 
  {
    throw new IllegalWriteException("this is read-only");
  }

  /** messages are read-only.
   */
  public void setContentID(String s) 
  throws MessagingException 
  {
    throw new IllegalWriteException("this is read-only");
  }

  /** messages are read-only.
   */
  public void setContentMD5(String s) 
  throws MessagingException 
  {
    throw new IllegalWriteException("this is read-only");
  }

  /** messages are read-only.
   */
  public void setDescription(String s, String s1) 
  throws MessagingException 
  {
    throw new IllegalWriteException("this is read-only");
  }

  /** messages are read-only.
   */
  public void setDataHandler(DataHandler datahandler) 
  throws MessagingException 
  {
    throw new IllegalWriteException("this is read-only");
  }

}
