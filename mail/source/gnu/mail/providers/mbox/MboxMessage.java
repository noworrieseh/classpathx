/*
 * MboxMessage.java
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
 */

package gnu.mail.providers.mbox;

import java.io.*;
import java.util.*;
import javax.activation.DataHandler;
import javax.mail.*;
import javax.mail.internet.*;

/**
 * The message class implementing the Mbox mail protocol.
 *
 * @author dog@dog.net.uk
 * @version 2.0
 */
public class MboxMessage 
  extends MimeMessage 
{

  /**
   * Status header key.
   * This keeps the mbox flags.
   */
  protected static final String STATUS = "Status";
  
  /**
   * The From_ line associated with this message.
   * We will preserve this if possible.
   */
  protected String fromLine;

  /**
   * Creates a Mbox message.
   * This is called by the MboxStore.
   */
  protected MboxMessage(MboxFolder folder, 
      String fromLine,
      InputStream in,
      int msgnum)
    throws MessagingException 
  {
    super(folder, in, msgnum);
    this.fromLine = fromLine;
    readStatusHeader();
  }

  /**
   * Creates a Mbox message.
   * This is called by the MboxFolder when appending.
   * It creates a copy of the specified message for the new folder.
   */
  protected MboxMessage(MboxFolder folder,
      MimeMessage message,
      int msgnum) 
    throws MessagingException 
  {
    super(message);
    this.folder = folder;
    this.msgnum = msgnum;
    readStatusHeader();
  }
	
  /**
   * Mbox messages are read-only.
   */
  public void setFrom(Address address) 
    throws MessagingException 
  {
    throw new IllegalWriteException("MboxMessage is read-only");
  }

  /**
   * Mbox messages are read-only.
   */
  public void addFrom(Address[] address) 
    throws MessagingException 
  {
    throw new IllegalWriteException("MboxMessage is read-only");
  }

  /**
   * Mbox messages are read-only.
   */
  public void setRecipients(Message.RecipientType recipienttype,
      Address[] addresses) 
    throws MessagingException 
  {
    throw new IllegalWriteException("MboxMessage is read-only");
  }

  /**
   * Mbox messages are read-only.
   */
  public void addRecipients(Message.RecipientType recipienttype,
      Address[] addresses) 
    throws MessagingException 
  {
    throw new IllegalWriteException("MboxMessage is read-only");
  }

  /**
   * Mbox messages are read-only.
   */
  public void setReplyTo(Address[] addresses) 
    throws MessagingException 
  {
    throw new IllegalWriteException("MboxMessage is read-only");
  }

  /**
   * Mbox messages are read-only.
   */
  public void setSubject(String subject, String charset) 
    throws MessagingException 
  {
    throw new IllegalWriteException("MboxMessage is read-only");
  }

  /**
   * Mbox messages are read-only.
   */
  public void setSentDate(Date date) 
    throws MessagingException 
  {
    throw new IllegalWriteException("MboxMessage is read-only");
  }

  /**
   * Mbox messages are read-only.
   */
  public void setDisposition(String disposition) 
    throws MessagingException 
  {
    throw new IllegalWriteException("MboxMessage is read-only");
  }

  /**
   * Mbox messages are read-only.
   */
  public void setContentID(String cid) 
    throws MessagingException 
  {
    throw new IllegalWriteException("MboxMessage is read-only");
  }

  /**
   * Mbox messages are read-only.
   */
  public void setContentMD5(String md5) 
    throws MessagingException 
  {
    throw new IllegalWriteException("MboxMessage is read-only");
  }

  /**
   * Mbox messages are read-only.
   */
  public void setDescription(String description, String charset) 
    throws MessagingException 
  {
    throw new IllegalWriteException("MboxMessage is read-only");
  }

  /**
   * Mbox messages are read-only.
   */
  public void setDataHandler(DataHandler datahandler) 
    throws MessagingException 
  {
    throw new IllegalWriteException("MboxMessage is read-only");
  }

  /** 
   * Ok, Mbox messages aren't entirely read-only.
   */
  public synchronized void setFlags(Flags flag, boolean set)
    throws MessagingException 
  {
    if (set)
      flags.add(flag);
    else
      flags.remove(flag);
  }
    
  /**
   * Updates the status header from the current flags.
   */
  protected void updateHeaders() 
    throws MessagingException 
  {
    super.updateHeaders();

    String old = getHeader(STATUS, "\n");
    StringBuffer buffer = new StringBuffer();
    boolean seen = flags.contains(Flags.Flag.SEEN);
    boolean recent = flags.contains(Flags.Flag.RECENT);
    boolean answered = flags.contains(Flags.Flag.ANSWERED);
    boolean deleted = flags.contains(Flags.Flag.DELETED);
    if (seen)
      buffer.append('R');
    if (!recent)
      buffer.append('O');
    if (answered)
      buffer.append('A');
    if (deleted)
      buffer.append('D');
    String status = buffer.toString();
    if (!(status.equals(old)))
      setHeader(STATUS, status);
  }


  /**
   * Reads the associated flags from the status header.
   */
  private void readStatusHeader() 
    throws MessagingException 
  {
    String[] currentStatus = this.getHeader(STATUS);
    if (currentStatus != null && currentStatus.length > 0) 
    {
      flags = new Flags();
      if (currentStatus[0].indexOf('R') >= 0)
        flags.add(Flags.Flag.SEEN);
      if (currentStatus[0].indexOf('O') < 0)
        flags.add(Flags.Flag.RECENT);
      if (currentStatus[0].indexOf('A') >= 0)
        flags.add(Flags.Flag.ANSWERED);
      if (currentStatus[0].indexOf('D') >= 0)
        flags.add(Flags.Flag.DELETED);
    }
  }

  // -- Utility methods --

  public boolean equals(Object other) 
  {
    if (other instanceof MimeMessage) 
    {
      MimeMessage message = (MimeMessage)other;
      return (message.getFolder()==getFolder() &&
          message.getMessageNumber()==getMessageNumber());
    }
    return false;
  }

}
