/*
 * POP3Folder.java
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
import javax.mail.*;
import javax.mail.event.*;

/**
 * The folder class implementing the POP3 mail protocol.
 *
 * @author dog@dog.net.uk
 * @author nferrier@tapsellferrier.co.uk
 * @version 1.1.1
 */
public class POP3Folder 
extends Folder 
{

  Hashtable messages = new Hashtable();
  boolean readonly = false, open = false;
  int type;

  Folder inbox;

  /**
   * Constructor.
   */
  protected POP3Folder(Store store, int type) 
  {
    super(store);
    this.type = type;
  }

  /**
   * Returns the name of this folder.
   */
  public String getName() 
  {
    switch (type) 
    {
      case HOLDS_FOLDERS:
	return "/";
      case HOLDS_MESSAGES:
	return "INBOX";
      default:
	return "(Unknown)";
    }
  }

  /**
   * Returns the full name of this folder.
   */
  public String getFullName() 
  {
    return getName();
  }

  /**
   * Returns the type of this folder.
   * @exception MessagingException if a messaging error occurred
   */
  public int getType() 
  throws MessagingException 
  {
    return type;
  }

  /**
   * Indicates whether this folder exists.
   * @exception MessagingException if a messaging error occurred
   */
  public boolean exists() 
  throws MessagingException 
  {
    return (type==HOLDS_MESSAGES);
  }

  /**
   * Indicates whether this folder contains new messages.
   * @exception MessagingException if a messaging error occurred
   */
  public boolean hasNewMessages() 
  throws MessagingException 
  {
    return getNewMessageCount()>0;
  }

  /**
   * Opens this folder.
   * @exception MessagingException if a messaging error occurred
   */
  public void open(int mode) 
  throws MessagingException 
  {
    switch (mode)
    {
      case READ_WRITE:
	readonly = false;
	break;
      case READ_ONLY:
	readonly = true;
	break;
    }
    open = true;
    notifyConnectionListeners(ConnectionEvent.OPENED);
  }

  /**
   * Closes this folder.
   * @param expunge if the folder is to be expunged before it is closed
   * @exception MessagingException if a messaging error occurred
   */
  public void close(boolean expunge) 
  throws MessagingException 
  {
    if (!open)
    throw new MessagingException("Folder is not open");
    if (expunge)
    expunge();
    open = false;
    notifyConnectionListeners(ConnectionEvent.CLOSED);
  }

  /**
   * Expunges this folder.
   * This deletes all the messages marked as deleted.
   * @exception MessagingException if a messaging error occurred
   */
  public Message[] expunge() 
  throws MessagingException 
  {
    if (!open)
    throw new MessagingException("Folder is not open");
    if (readonly)
    throw new MessagingException("Folder was opened read-only");
    Vector vector = new Vector();
    for (Enumeration enum = messages.elements(); enum.hasMoreElements(); )
    {
      Message message = (Message)enum.nextElement();
      Flags flags = message.getFlags();
      if (flags.contains(Flags.Flag.DELETED))
      vector.addElement(message);
    }
    Message[] delete = new Message[vector.size()]; vector.copyInto(delete);
    for (int i=0; i<delete.length; i++)
    ((POP3Store)store).delete(delete[i].getMessageNumber());
    messages.clear();
    return delete;
  }

  /**
   * Indicates whether this folder is open.
   */
  public boolean isOpen() 
  {
    return open;
  }

  /**
   * Returns the permanent flags for this folder.
   */
  public Flags getPermanentFlags() 
  {
    return new Flags();
  }

  /**
   * Returns the number of messages in this folder.
   * This results in a STAT call to the POP3 server, so the latest
   * count is always delivered.
   * @exception MessagingException if a messaging error occurred
   */
  public int getMessageCount() 
  throws MessagingException 
  {
    return ((POP3Store)store).getMessageCount();
  }

  /**
   * Returns the specified message number from this folder.
   * The message is only retrieved once from the server.
   * Subsequent getMessage() calls to the same message are cached.
   * Since POP3 does not provide a mechanism for retrieving only part of
   * the message (headers, etc), the entire message is retrieved.
   * @exception MessagingException if a messaging error occurred
   */
  public Message getMessage(int msgnum) 
  throws MessagingException 
  {
    if (!open)
    throw new MessagingException("Folder is not open");
    Message message = (Message)messages.get(new Integer(msgnum));
    if (message==null) {
      message = ((POP3Store)store).getMessage(this, msgnum);
      messages.put(new Integer(msgnum), message);
    }
    return message;
  }

  /**
   * You can't append messages to a POP3 folder.
   */
  public void appendMessages(Message amessage[]) 
  throws MessagingException 
  {
    throw new MessagingException("Can't append messages to a POP3 folder");
  }

  /**
   * Does nothing.
   * The messages <i>must</i> be fetched in their entirety by getMessage(int) -
   * this is the nature of the POP3 protocol.
   * @exception MessagingException ignore
   */
  public void fetch(Message amessage[], FetchProfile fetchprofile) 
  throws MessagingException 
  {
  }

  /**
   * Returns the subfolders for this folder.
   */
  public Folder[] list() 
  throws MessagingException 
  {
    switch (type)
    {
      case HOLDS_FOLDERS:
	if (inbox==null) inbox = new POP3Folder(store, HOLDS_MESSAGES);
	Folder[] folders = { inbox };
	return folders;
      default:
	throw new MessagingException("This folder can't contain subfolders");
    }
  }

  /**
   * Returns the subfolders for this folder.
   */
  public Folder[] list(String pattern) 
  throws MessagingException 
  {
    return list();
  }

  /**
   * POP3 folders can't have parents.
   */
  public Folder getParent() 
  throws MessagingException 
  {
    switch (type)
    {
      case HOLDS_MESSAGES:
	return ((POP3Store)store).root;
      default:
	throw new MessagingException("Root folders can't have a parent");
    }
  }

  /**
   * POP3 folders can't contain subfolders.
   */
  public Folder getFolder(String s) 
  throws MessagingException 
  {
    switch (type)
    {
      case HOLDS_FOLDERS:
	if (inbox==null) inbox = new POP3Folder(store, HOLDS_MESSAGES);
	return inbox;
      default:
	throw new MessagingException("This folder can't contain subfolders");
    }
  }

  /**
   * Returns the path separator charcter.
   */
  public char getSeparator() 
  throws MessagingException 
  {
    return '\u0000';
  }


  // -- These must be overridden to throw exceptions --

  /**
   * POP3 folders can't be created, deleted, or renamed.
   */
  public boolean create(int i) 
  throws MessagingException 
  {
    throw new MessagingException("Folder can't be created");
  }

  /**
   * POP3 folders can't be created, deleted, or renamed.
   */
  public boolean delete(boolean flag) 
  throws MessagingException 
  {
    throw new MessagingException("Folder can't be deleted");
  }

  /**
   * POP3 folders can't be created, deleted, or renamed.
   */
  public boolean renameTo(Folder folder) 
  throws MessagingException 
  {
    throw new MessagingException("Folder can't be renamed");
  }

}
