/*
 * IMAPFolder.java
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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.mail.FetchProfile;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.FolderClosedException;
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
  protected int mode = -1;

  protected Flags permanentFlags = new Flags();

  protected char delimiter;

  protected int messageCount = -1;

  protected int newMessageCount = -1;

  /**
   * Constructor.
   */
  protected IMAPFolder(Store store, String path) 
  {
    this(store, path, -1, '\u0000');
  }
  
  /**
   * Constructor.
   */
  protected IMAPFolder(Store store, String path, char delimiter) 
  {
    this(store, path, -1, delimiter);
  }
  
  /**
   * Constructor.
   */
  protected IMAPFolder(Store store, String path, int type, char delimiter) 
  {
    super(store);
    this.path = path;
    this.type = type;
    this.delimiter = delimiter;
  }

  /*
   * Updates this folder from the specified mailbox status object.
   */
  void update(MailboxStatus status, boolean fireEvents)
    throws MessagingException
  {
    if (status==null)
    {
      System.err.println("update: status is null");
      throw new FolderNotFoundException(this);
    }
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
    if (type==-1)
    {
      int lsi = path.lastIndexOf(getSeparator());
      String parent = (lsi==-1) ? "" : path.substring(0, lsi);
      String name = (lsi==-1) ? path : path.substring(lsi+1);
      IMAPConnection connection = ((IMAPStore)store).connection;
      try
      {
        ListEntry[] entries;
        synchronized (connection)
        {
          entries = connection.list(parent, name);
        }
        if (entries.length>0)
        {
          List attributes = entries[0].attributes;
          if (attributes!=null)
          {
            if (attributes.contains(LIST_NOINFERIORS))
              type = Folder.HOLDS_MESSAGES;
            else
              type = Folder.HOLDS_FOLDERS;
          }
        }
        else
        {
          System.err.println("getType: no entries on list: "+parent+" "+name);
          throw new FolderNotFoundException(this);
        }
      }
      catch (IOException e)
      {
        throw new MessagingException(e.getMessage(), e);
      }
    }
    return type;
  }

  /**
   * Indicates whether this folder exists.
   * @exception MessagingException if a messaging error occurred
   */
  public boolean exists() 
    throws MessagingException 
  {
    return (getType()!=-1);
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
    IMAPStore s = (IMAPStore)store;
    IMAPConnection connection = s.connection;
    try
    {
      MailboxStatus status = null;
      synchronized (connection)
      {
        System.out.println("Opening "+path+" with mode "+mode);
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
      }
      s.setSelected(this);
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
    getSeparator();
    if (delimiter=='\u0000') // this folder cannot be created
    {
      System.err.println("create: delimiter is NIL");
      throw new FolderNotFoundException(this);
    }
    IMAPConnection connection = ((IMAPStore)store).connection;
    try
    {
      String path = this.path;
      if (type==HOLDS_FOLDERS)
        path = new StringBuffer(path)
          .append(delimiter)
          .toString();
      boolean ret;
      synchronized (connection)
      {
        ret = connection.create(path);
      }
      if (ret)
        notifyFolderListeners(FolderEvent.CREATED);
      return ret;
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
      boolean ret;
      synchronized (connection)
      {
        ret = connection.delete(path);
      }
      if (ret)
        notifyFolderListeners(FolderEvent.DELETED);
      return ret;
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
      boolean ret;
      synchronized (connection)
      {
        ret = connection.rename(path, folder.getFullName());
      }
      if (ret)
        notifyFolderRenamedListeners(folder); // do we have to close?
      return ret;
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
    if (mode==-1)
      return;
    IMAPStore s = (IMAPStore)store;
    boolean selected = s.isSelected(this);
    if (selected)
      s.setSelected(null);
    mode = -1;
    notifyConnectionListeners(ConnectionEvent.CLOSED);
    if (expunge)
    {
      if (!selected)
        throw new FolderClosedException(this);
      IMAPConnection connection = s.connection;
      try
      {
        boolean success;
        synchronized (connection)
        {
          success = connection.close();
        }
        if (!success)
          throw new IllegalWriteException();
      }
      catch (IOException e)
      {
        throw new MessagingException(e.getMessage(), e);
      }
    }
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
      int[] messageNumbers;
      synchronized (connection)
      {
        messageNumbers = connection.expunge();
      }
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
        synchronized (connection)
        {
          MailboxStatus ms = connection.status(path, items);
          update(ms, true);
        }
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
        synchronized (connection)
        {
          MailboxStatus ms = connection.status(path, items);
          update(ms, true);
        }
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
      throw new FolderClosedException(this);
    return new IMAPMessage(this, msgnum);
  }

  /**
   * Appends the specified set of messages to this folder.
   * Only <code>MimeMessage</code>s are accepted.
   */
  public void appendMessages(Message[] messages) 
    throws MessagingException 
  {
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
      IMAPConnection connection = ((IMAPStore)store).connection;
      for (int i=0; i<m.length; i++)
      {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        m[i].writeTo(out);
        byte[] content = out.toByteArray();
        out = null;
        synchronized (connection)
        {
          connection.append(path, null, content);
        }
      }
    }
    catch (IOException e)
    {
      throw new MessagingException(e.getMessage(), e);
    }
    notifyMessageAddedListeners(m);
  }

  /**
   * IMAP fetch routine.
   * This executes the fetch for the specified message numbers
   * and updates the messages according to the message statuses returned.
   */
  public void fetch(Message[] messages, FetchProfile fp) 
    throws MessagingException 
  {
    if (!isOpen())
      throw new FolderClosedException(this);
    // decide which commands to send
    List l = new ArrayList();
    if (fp.contains(FetchProfile.Item.CONTENT_INFO))
      l.add(IMAPMessage.FETCH_CONTENT);
    else if (fp.contains(FetchProfile.Item.ENVELOPE))
      l.add(IMAPMessage.FETCH_HEADERS);
    if (fp.contains(FetchProfile.Item.FLAGS))
      l.add(FLAGS);
    // TODO generic headers
    String[] commands = new String[l.size()];
    l.toArray(commands);
    l = null;
    // get casted imapmessages and message numbers
    IMAPMessage[] m = new IMAPMessage[messages.length];
    int[] msgnums = new int[messages.length];
    try
    {
      for (int i=0; i<messages.length; i++)
      {
        m[i] = (IMAPMessage)messages[i];
        msgnums[i] = m[i].getMessageNumber();
      }
    }
    catch (ClassCastException e)
    {
      throw new MessagingException("Only IMAPMessages can be fetched");
    }
    // execute
    try
    {
      IMAPConnection connection = ((IMAPStore)store).connection;
      synchronized (connection)
      {
        MessageStatus[] ms = connection.fetch(msgnums, commands);
        for (int i=0; i<ms.length; i++)
        {
          int msgnum = ms[i].getMessageNumber();
          for (int j=0; j<msgnums.length; j++)
          {
            if (msgnums[j]==msgnum)
            {
              m[j].update(ms[i]);
              break;
            }
          }
        }
      }
    }
    catch (IOException e)
    {
      throw new MessagingException(e.getMessage(), e);
    }
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
      ListEntry[] entries;
      synchronized (connection)
      {
        entries = connection.list(path, pattern);
      }
      return getFolders(entries);
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
      ListEntry[] entries;
      synchronized (connection)
      {
        entries = connection.lsub(path, pattern);
      }
      return getFolders(entries);
    }
    catch (IOException e)
    {
      throw new MessagingException(e.getMessage(), e);
    }
  }

  Folder[] getFolders(ListEntry[] entries)
    throws MessagingException
  {
    List unique = new ArrayList(entries.length);
    for (int i=0; i<entries.length; i++)
    {
      boolean suppress = false;
      int type = -1;
      List attributes = entries[i].attributes;
      if (attributes!=null)
      {
        suppress = (attributes.contains(LIST_NOSELECT));
        if (attributes.contains(LIST_NOINFERIORS))
          type = Folder.HOLDS_MESSAGES;
        else
          type = Folder.HOLDS_FOLDERS;
      }
      if (!suppress)
      {
        Folder f = getFolder(entries[i].mailbox, type, entries[i].delimiter);
        if (!unique.contains(f))
          unique.add(f);
      }
    }
    Folder[] folders = new Folder[unique.size()];
    unique.toArray(folders);
    return folders;
  }

  /**
   * Returns the parent folder of this folder.
   */
  public Folder getParent() 
    throws MessagingException 
  {
    IMAPConnection connection = ((IMAPStore)store).connection;
    getSeparator();
    int di = path.lastIndexOf(delimiter);
    if (di==-1)
      return null;
    return new IMAPFolder(store, path.substring(0, di), delimiter);
  }

  /**
   * Returns a subfolder with the specified name.
   */
  public Folder getFolder(String name) 
    throws MessagingException 
  {
    return getFolder(name, -1, getSeparator());
  }

  /**
   * Returns a configured subfolder.
   */
  protected IMAPFolder getFolder(String name, int type, char delimiter)
    throws MessagingException
  {
    IMAPConnection connection = ((IMAPStore)store).connection;
    StringBuffer path = new StringBuffer(this.path);
    if (path.length()>0)
        path.append(delimiter);
    path.append(name);
    return new IMAPFolder(store, path.toString(), type, delimiter);
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
        ListEntry[] entries;
        synchronized (connection)
        {
          entries = connection.list(path, null);
        }
        if (entries.length>0)
          delimiter = entries[0].delimiter;
        else
          throw new MessagingException("Error retrieving separator"); 
      }
      catch (IOException e)
      {
        throw new MessagingException(e.getMessage(), e);
      }
    }
    return delimiter;
  }

  public boolean equals(Object other)
  {
    if (other instanceof IMAPFolder)
      return ((IMAPFolder)other).path.equals(path);
    return super.equals(other);
  }

}
