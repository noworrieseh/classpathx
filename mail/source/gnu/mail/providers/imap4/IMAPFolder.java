/*
 * IMAPFolder.java
 * Copyright (C) 2003 Chris Burdess <dog@gnu.org>
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

package gnu.mail.providers.imap4;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import javax.mail.FetchProfile;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.FolderNotFoundException;
import javax.mail.IllegalWriteException;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Store;
import javax.mail.event.ConnectionEvent;
import javax.mail.event.FolderEvent;
import javax.mail.internet.MimeMessage;

/**
 * The folder class implementing the IMAP4rev1 mail protocol.
 *
 * @author <a href='mailto:dog@gnu.org'>Chris Burdess</a>
 * @version 0.1
 */
public class IMAPFolder 
  extends Folder
  implements IMAPConstants
{

  /**
   * The folder path.
   */
  protected String path;

  /**
   * The type of this folder (HOLDS_MESSAGES or HOLDS_FOLDERS).
   */
  protected int type;

  /**
   * The open state of this folder (READ_ONLY, READ_WRITE, or -1).
   */
  protected int mode;

  protected Flags permanentFlags = new Flags();

  protected char delimiter = '\u0000';

  protected int messageCount = -1;

  protected int newMessageCount = -1;

  /**
   * Constructor.
   */
  protected IMAPFolder(Store store, String path) 
  {
    super(store);
    this.path = path;
  }

  /*
   * Updates this folder from the specified mailbox status object.
   */
  void update(MailboxStatus status, boolean fireEvents)
    throws MessagingException
  {
    if (status==null)
      throw new FolderNotFoundException(this);
    mode = status.readWrite ? Folder.READ_WRITE : Folder.READ_ONLY;
    if (status.permanentFlags!=null)
      permanentFlags = readFlags(status.permanentFlags);
    // message counts
    int oldMessageCount = messageCount;
    messageCount = status.messageCount;
    newMessageCount = status.newMessageCount;
    // fire events if necessary
    if (fireEvents)
    {
      if (messageCount>oldMessageCount)
      {
        Message[] m = new Message[messageCount-oldMessageCount];
        for (int i=oldMessageCount; i<messageCount; i++)
          m[i-oldMessageCount] = getMessage(i);
        notifyMessageAddedListeners(m);
      }
      else if (messageCount<oldMessageCount)
      {
        Message[] m = new Message[oldMessageCount-messageCount];
        for (int i=messageCount; i<oldMessageCount; i++)
          m[i-messageCount] = getMessage(i);
        notifyMessageRemovedListeners(false, m);
      }
    }
  }

  Flags readFlags(List sflags)
  {
    Flags flags = new Flags();
    int len = sflags.size();
    for (int i=0; i<len; i++)
    {
      String flag = (String)sflags.get(i);
      if (flag==FLAG_ANSWERED)
        flags.add(Flags.Flag.ANSWERED);
      else if (flag==FLAG_DELETED)
        flags.add(Flags.Flag.DELETED);
      else if (flag==FLAG_DRAFT)
        flags.add(Flags.Flag.DRAFT);
      else if (flag==FLAG_FLAGGED)
        flags.add(Flags.Flag.FLAGGED);
      else if (flag==FLAG_RECENT)
        flags.add(Flags.Flag.RECENT);
      else if (flag==FLAG_SEEN)
        flags.add(Flags.Flag.SEEN);
      // user flags?
    }
    return flags;
  }

  /**
   * Returns the name of this folder.
   */
  public String getName() 
  {
    int di = path.lastIndexOf(delimiter);
    return (di==-1) ? path : path.substring(di+1);
  }

  /**
   * Returns the full path of this folder.
   */
  public String getFullName()
  {
    return path;
  }

  /**
   * Returns the type of this folder.
   * @exception MessagingException if a messaging error occurred
   */
  public int getType() 
    throws MessagingException 
  {
    // TODO
    return type;
  }

  /**
   * Indicates whether this folder exists.
   * @exception MessagingException if a messaging error occurred
   */
  public boolean exists() 
    throws MessagingException 
  {
    return false; // TODO
  }

  /**
   * Indicates whether this folder contains new messages.
   * @exception MessagingException if a messaging error occurred
   */
  public boolean hasNewMessages() 
    throws MessagingException 
  {
    return getNewMessageCount()>0; // TODO
  }

  /**
   * Opens this folder.
   * @exception MessagingException if a messaging error occurred
   */
  public void open(int mode) 
    throws MessagingException 
  {
    IMAPConnection connection = ((IMAPStore)store).connection;
    try
    {
      MailboxStatus status = null;
      switch (mode)
      {
        case Folder.READ_WRITE:
          status = connection.select(getFullName());
          break;
        case Folder.READ_ONLY:
          status = connection.examine(getFullName());
          break;
        default:
          throw new MessagingException("No such mode: "+mode);
      }
      update(status, false);
      notifyConnectionListeners(ConnectionEvent.OPENED);
    }
    catch (IOException e)
    {
      throw new MessagingException(e.getMessage(), e);
    }
  }

  /**
   * Create this folder.
   */
  public boolean create(int type) 
    throws MessagingException 
  {
    IMAPConnection connection = ((IMAPStore)store).connection;
    try
    {
      String path = this.path;
      if (type==HOLDS_FOLDERS)
        path = new StringBuffer(path)
          .append(getSeparator())
          .toString();
      if (connection.create(path))
      {
        notifyFolderListeners(FolderEvent.CREATED);
        return true;
      }
      else
        return false;
    }
    catch (IOException e)
    {
      throw new MessagingException(e.getMessage(), e);
    }
  }

  /**
   * Delete this folder.
   */
  public boolean delete(boolean flag) 
    throws MessagingException 
  {
    IMAPConnection connection = ((IMAPStore)store).connection;
    try
    {
      if (connection.delete(path))
      {
        notifyFolderListeners(FolderEvent.DELETED);
        return true;
      }
      else
        return false;
    }
    catch (IOException e)
    {
      throw new MessagingException(e.getMessage(), e);
    }
  }

  /**
   * Rename this folder.
   */
  public boolean renameTo(Folder folder) 
    throws MessagingException 
  {
    IMAPConnection connection = ((IMAPStore)store).connection;
    try
    {
      if (connection.rename(path, folder.getFullName()))
      {
        // do we have to close?
        notifyFolderRenamedListeners(folder);
        return true;
      }
      else
        return false;
    }
    catch (IOException e)
    {
      throw new MessagingException(e.getMessage(), e);
    }
  }

  /**
   * Closes this folder.
   * @param expunge if the folder is to be expunged before it is closed
   * @exception MessagingException if a messaging error occurred
   */
  public void close(boolean expunge) 
    throws MessagingException 
  {
    if (!isOpen())
      throw new MessagingException("Folder is not open");
    if (expunge)
      expunge();
    IMAPConnection connection = ((IMAPStore)store).connection;
    try
    {
      connection.close();
    }
    catch (IOException e)
    {
      throw new MessagingException(e.getMessage(), e);
    }
    mode = -1;
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
    if (!isOpen())
      throw new MessagingException("Folder is not open");
    if (mode==Folder.READ_ONLY)
      throw new MessagingException("Folder was opened read-only");
    IMAPConnection connection = ((IMAPStore)store).connection;
    try
    {
      int[] messageNumbers = connection.expunge();
      // construct empty IMAPMessages for the messageNumbers
      IMAPMessage[] messages = new IMAPMessage[messageNumbers.length];
      for (int i=0; i<messages.length; i++)
        messages[i] = new IMAPMessage(this, messageNumbers[i]);
      // do we need to do this?
      notifyMessageRemovedListeners(true, messages);
      return messages;
    }
    catch (IOException e)
    {
      throw new MessagingException(e.getMessage(), e);
    }
  }

  /**
   * Indicates whether this folder is open.
   */
  public boolean isOpen() 
  {
    return (mode!=-1);
  }

  /**
   * Returns the permanent flags for this folder.
   */
  public Flags getPermanentFlags() 
  {
    return permanentFlags;
  }

  /**
   * Returns the number of messages in this folder.
   * @exception MessagingException if a messaging error occurred
   */
  public int getMessageCount() 
    throws MessagingException 
  {
    if (mode==-1 || messageCount<0)
    {
      IMAPConnection connection = ((IMAPStore)store).connection;
      try
      {
        String[] items = new String[1];
        items[0] = IMAPConnection.MESSAGES;
        MailboxStatus ms = connection.status(path, items);
        update(ms, true);
      }
      catch (IOException e)
      {
        throw new MessagingException(e.getMessage(), e);
      }
    }
    // TODO else NOOP
    return messageCount;
  }

  /**
   * Returns the number of new messages in this folder.
   * @exception MessagingException if a messaging error occurred
   */
  public int getNewMessageCount() 
    throws MessagingException 
  {
    if (mode==-1 || newMessageCount<0)
    {
      IMAPConnection connection = ((IMAPStore)store).connection;
      try
      {
        String[] items = new String[1];
        items[0] = IMAPConnection.RECENT;
        MailboxStatus ms = connection.status(path, items);
        update(ms, true);
      }
      catch (IOException e)
      {
        throw new MessagingException(e.getMessage(), e);
      }
    }
    // TODO else NOOP
    return newMessageCount;
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
    if (mode==-1)
      throw new MessagingException("Folder is not open");
    return new IMAPMessage(this, msgnum);
  }

  /**
   * Appends the specified set of messages to this folder.
   * Only <code>MimeMessage</code>s are accepted.
   */
  public void appendMessages(Message[] messages) 
    throws MessagingException 
  {
    if (mode==Folder.READ_ONLY)
      throw new IllegalWriteException("Folder is read-only");
    MimeMessage[] m = new MimeMessage[messages.length];
    try
    {
      for (int i=0; i<messages.length; i++)
        m[i] = (MimeMessage)messages[i];
    }
    catch (ClassCastException e)
    {
      throw new MessagingException("Only MimeMessages can be appended to "+
          "this folder");
    }
    try
    {
      IMAPStore s = (IMAPStore)store;
      for (int i=0; i<m.length; i++)
      {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        m[i].writeTo(out);
        byte[] content = out.toByteArray();
        out = null;
        s.connection.append(path, null, content);
      }
    }
    catch (IOException e)
    {
      throw new MessagingException(e.getMessage(), e);
    }
    notifyMessageAddedListeners(m);
  }

  public void fetch(Message[] messages, FetchProfile fetchprofile) 
    throws MessagingException 
  {
    // TODO
  }

  /**
   * Returns the subfolders for this folder.
   */
  public Folder[] list(String pattern) 
    throws MessagingException 
  {
    IMAPConnection connection = ((IMAPStore)store).connection;
    try
    {
      ListEntry[] entries = connection.list(path, pattern);
      Folder[] folders = new Folder[entries.length];
      for (int i=0; i<folders.length; i++)
        folders[i] = getFolder(entries[i].mailbox);
      return folders;
    }
    catch (IOException e)
    {
      throw new MessagingException(e.getMessage(), e);
    }
  }
  
  /**
   * Returns the subscribed subfolders for this folder.
   */
  public Folder[] listSubscribed(String pattern) 
    throws MessagingException 
  {
    IMAPConnection connection = ((IMAPStore)store).connection;
    try
    {
      ListEntry[] entries = connection.lsub(path, pattern);
      Folder[] folders = new Folder[entries.length];
      for (int i=0; i<folders.length; i++)
        folders[i] = getFolder(entries[i].mailbox);
      return folders;
    }
    catch (IOException e)
    {
      throw new MessagingException(e.getMessage(), e);
    }
  }

  /**
   * Returns the parent folder of this folder.
   */
  public Folder getParent() 
    throws MessagingException 
  {
    IMAPConnection connection = ((IMAPStore)store).connection;
    int di = path.lastIndexOf(getSeparator());
    if (di==-1)
      return null;
    return store.getFolder(path.substring(0, di));
  }

  /**
   * Returns a subfolder with the specified name.
   */
  public Folder getFolder(String name) 
    throws MessagingException 
  {
    IMAPConnection connection = ((IMAPStore)store).connection;
    return store.getFolder(new StringBuffer(path)
        .append(getSeparator())
        .append(name)
        .toString());
  }

  /**
   * Returns the path separator charcter.
   */
  public char getSeparator() 
    throws MessagingException 
  {
    if (delimiter=='\u0000')
    {
      try
      {
        IMAPConnection connection = ((IMAPStore)store).connection;
        ListEntry[] entries = connection.list(path, null);
        if (entries.length>0)
          delimiter = entries[0].delimiter;
      }
      catch (IOException e)
      {
        throw new MessagingException(e.getMessage(), e);
      }
    }
    return delimiter;
  }


}
