/*
 * IMAPMessage.java
 * Copyright (C) 2003 Chris Burdess <dog@gnu.org>
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

package gnu.mail.providers.imap4;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import javax.activation.DataHandler;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.InternetHeaders;
import javax.mail.internet.MailDateFormat;
import javax.mail.internet.MimeMessage;

import gnu.mail.providers.ReadOnlyMessage;

/**
 * The message class implementing the IMAP4 mail protocol.
 *
 * @author <a href='mailto:dog@gnu.org'>Chris Burdess</a>
 * @version 1.0
 */
public final class IMAPMessage extends ReadOnlyMessage implements IMAPConstants
{

  static final String FETCH_HEADERS = "BODY.PEEK[HEADER]";
  static final String FETCH_CONTENT = "BODY.PEEK[]";

  /**
   * If set, this contains the RFC822-formatted value of the received date.
   */
  protected String internalDate;

  IMAPMessage(IMAPFolder folder, InputStream in, int msgnum) 
    throws MessagingException 
  {
    super(folder, in, msgnum);
    flags = null;
  }

  IMAPMessage(IMAPFolder folder, int msgnum) 
    throws MessagingException 
  {
    super(folder, msgnum);
    flags = null;
  }

  /**
   * Fetches the flags fo this message.
   */
  void fetchFlags()
    throws MessagingException
  {
    String[] commands = new String[] { FLAGS };
    fetch(commands);
  }

  /**
   * Fetches the message header.
   */
  void fetchHeaders()
    throws MessagingException
  {
    String[] commands = new String[] { FETCH_HEADERS, INTERNALDATE };
    fetch(commands);
  }

  /**
   * Fetches the message body.
   */
  void fetchContent()
    throws MessagingException
  {
    String[] commands = new String[] { FETCH_CONTENT, INTERNALDATE };
    fetch(commands);
  }

  /**
   * Generic fetch routine.
   */
  void fetch(String[] commands)
    throws MessagingException
  {
    try
    {
      IMAPConnection connection = ((IMAPStore)folder.getStore()).connection;
      // Select folder
      if (!folder.isOpen())
        folder.open(Folder.READ_WRITE);
      int[] messages = new int[] { msgnum };
      synchronized (connection)
      {
        MessageStatus[] ms = connection.fetch(messages, commands);
        for (int i=0; i<ms.length; i++)
        {
          if (ms[i].getMessageNumber()==msgnum)
            update(ms[i]);
        }
      }
    }
    catch (IOException e)
    {
      throw new MessagingException(e.getMessage(), e);
    }
  }

  /**
   * Updates this message using the specified message status object.
   */
  void update(MessageStatus status)
    throws MessagingException
  {
    for (Iterator i = status.keySet().iterator(); i.hasNext(); )
    {
      String key = (String)i.next();
      if (key==FLAGS)
      {
        List fl = (List)status.get(key);
        flags = new Flags();
        for (Iterator j = fl.iterator(); j.hasNext(); )
        {
          Object f = j.next();
          if (f==FLAG_ANSWERED)
            flags.add(Flags.Flag.ANSWERED);
          else if (f==FLAG_DELETED)
            flags.add(Flags.Flag.DELETED);
          else if (f==FLAG_DRAFT)
            flags.add(Flags.Flag.DRAFT);
          else if (f==FLAG_FLAGGED)
            flags.add(Flags.Flag.FLAGGED);
          else if (f==FLAG_RECENT)
            flags.add(Flags.Flag.RECENT);
          else if (f==FLAG_SEEN)
            flags.add(Flags.Flag.SEEN);
          else if (f instanceof String)
            flags.add((String)f);
        }
      }
      else if (key==BODYHEADER)
      {
        InputStream in = new ByteArrayInputStream(status.getContent());
        headers = createInternetHeaders(in);
      }
      else if (key==BODY)
      {
        InputStream in = new ByteArrayInputStream(status.getContent());
        parse(in);
      }
      else if (key==INTERNALDATE)
      {
        internalDate = (String)status.get(key);
      }
      else
        throw new MessagingException("Unknown message status key: "+key);
    }
    
  }

  /**
   * Returns the date on which this message was received.
   */
  public Date getReceivedDate()
    throws MessagingException
  {
    if (headers==null)
      fetchHeaders(); // seems reasonable
    if (internalDate==null)
      return null;
    try
    {
      return new MailDateFormat().parse(internalDate);
    }
    catch (ParseException e)
    {
      throw new MessagingException(e.getMessage(), e);
    }
  }

  // -- Content access --

  /**
   * Returns a data handler for this message's content.
   */
  public DataHandler getDataHandler() 
    throws MessagingException
  {
    if (content==null)
      fetchContent();
    return super.getDataHandler();
  }

  /**
   * Returns the raw content stream.
   */
  protected InputStream getContentStream() 
    throws MessagingException 
  {
    if (content==null)
      fetchContent();
    return super.getContentStream();
  }

  // -- Header access --
  
  /**
   * Returns the specified header field.
   */
  public String[] getHeader(String name) 
    throws MessagingException 
  {
    if (headers==null)
      fetchHeaders();
    return super.getHeader(name);
  }

  /**
   * Returns the specified header field.
   */
  public String getHeader(String name, String delimiter) 
    throws MessagingException 
  {
    if (headers==null)
      fetchHeaders();
    return super.getHeader(name, delimiter);
  }

  public Enumeration getAllHeaders() 
    throws MessagingException 
  {
    if (headers==null)
      fetchHeaders();
    return super.getAllHeaders();
  }

  public Enumeration getAllHeaderLines() 
    throws MessagingException 
  {
    if (headers==null)
      fetchHeaders();
    return super.getAllHeaderLines();
  }

  public Enumeration getMatchingHeaders(String[] names) 
    throws MessagingException 
  {
    if (headers==null)
      fetchHeaders();
    return super.getMatchingHeaders(names);
  }

  public Enumeration getMatchingHeaderLines(String[] names) 
    throws MessagingException 
  {
    if (headers==null)
      fetchHeaders();
    return super.getMatchingHeaderLines(names);
  }

  public Enumeration getNonMatchingHeaders(String[] names) 
    throws MessagingException 
  {
    if (headers==null)
      fetchHeaders();
    return super.getNonMatchingHeaders(names);
  }

  public Enumeration getNonMatchingHeaderLines(String[] names) 
    throws MessagingException 
  {
    if (headers==null)
      fetchHeaders();
    return super.getNonMatchingHeaderLines(names);
  }

  // -- Flags access --

  public Flags getFlags()
    throws MessagingException
  {
    if (flags==null)
      fetchFlags();
    return super.getFlags();
  }

  public boolean isSet(Flags.Flag flag)
    throws MessagingException
  {
    if (flags==null)
      fetchFlags();
    return super.isSet(flag);
  }

  /**
   * Set the specified flags.
   */
  public void setFlags(Flags flag, boolean set)
    throws MessagingException
  {
    if (flags==null)
      fetchFlags();
    try
    {
      if (set)
        flags.add(flag);
      else
        flags.remove(flag);
      // Create a list of flags to send to the server
      Flags.Flag[] sflags = flags.getSystemFlags();
      String[] uflags = flags.getUserFlags();
      List iflags = new ArrayList(sflags.length+uflags.length);
      for (int i=0; i<sflags.length; i++)
      {
        Flags.Flag f = sflags[i];
        if (f==Flags.Flag.ANSWERED)
          iflags.add(FLAG_ANSWERED);
        else if (f==Flags.Flag.DELETED)
          iflags.add(FLAG_DELETED);
        else if (f==Flags.Flag.DRAFT)
          iflags.add(FLAG_DRAFT);
        else if (f==Flags.Flag.FLAGGED)
          iflags.add(FLAG_FLAGGED);
        else if (f==Flags.Flag.RECENT)
          iflags.add(FLAG_RECENT);
        else if (f==Flags.Flag.SEEN)
          iflags.add(FLAG_SEEN);
      }
      iflags.addAll(Arrays.asList(uflags));
      String[] aflags = new String[iflags.size()];
      iflags.toArray(aflags);
      // Perform store
      IMAPStore store = (IMAPStore)folder.getStore();
      int[] messages = new int[] { msgnum };
      MessageStatus[] ms = store.connection.store(messages, FLAGS, aflags);
      for (int i=0; i<ms.length; i++)
      {
        if (ms[i].getMessageNumber()==msgnum)
          update(ms[i]);
      }
    }
    catch (IOException e)
    {
      flags = null; // will be re-read next time
      throw new MessagingException(e.getMessage(), e);
    }
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
