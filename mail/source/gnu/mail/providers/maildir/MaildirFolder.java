/*
 * MaildirFolder.java
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

package gnu.mail.providers.maildir;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import javax.mail.Address;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Store;
import javax.mail.URLName;
import javax.mail.event.ConnectionEvent;
import javax.mail.event.FolderEvent;
import javax.mail.internet.MimeMessage;
import gnu.mail.util.LineInputStream;

import gnu.mail.treeutil.StatusEvent;

/**
 * The folder class implementing a Maildir-format mailbox.
 *
 * @author <a href='mailto:dog@gnu.org'>Chris Burdess</a>
 */
public final class MaildirFolder 
  extends Folder 
{

  /**
   * Singleton instance of filter.
   */
  static final FilenameFilter filter = new MaildirFilter();

  static final String INBOX = "INBOX";

  /**
   * The maildir base directory.
   */
  File maildir;

  /**
   * The maildir <code>tmp</code> directory.
   */
  File tmpdir;

  /**
   * The maildir <code>new</code> directory.
   */
  MaildirTuple newdir;

  /**
   * The maildir <code>cur</code> directory.
   */
  MaildirTuple curdir;

  int type;
  boolean inbox;
  
  static Flags permanentFlags = new Flags();
		
  /**
   * Constructor.
   */
  protected MaildirFolder(Store store, String filename, boolean root,
      boolean inbox) 
  {
    super(store);
    
    maildir = new File(filename);
    tmpdir = new File(maildir, "tmp");
    newdir = new MaildirTuple(new File(maildir, "new"));
    curdir =  new MaildirTuple(new File(maildir, "cur"));
    
    mode = -1;
    type = root ? HOLDS_FOLDERS : HOLDS_MESSAGES;
    this.inbox = inbox;
  }
	
  /**
   * Constructor.
   */
  protected MaildirFolder(Store store, String filename) 
  {
    this(store, filename, false, false);
  }

  /**
   * Returns the name of this folder.
   */
  public String getName() 
  {
    if (inbox)
      return INBOX;
    return maildir.getName();
  }
	
  /**
   * Returns the full name of this folder.
   */
  public String getFullName() 
  {
    if (inbox)
      return INBOX;
    return maildir.getPath();
  }

  /**
   * Return a URLName representing this folder.
   */
  public URLName getURLName()
    throws MessagingException
  {
    URLName url = super.getURLName();
    return new URLName(url.getProtocol(), 
        null, -1, url.getFile(),
        null, null);
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
    return maildir.exists();
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
   * If the folder is opened for writing, a lock must be acquired on the
   * mbox. If this fails a MessagingException is thrown.
   * @exception MessagingException if a messaging error occurred
   */
  public void open(int mode) 
    throws MessagingException 
  {
    if (mode!=-1)
      throw new IllegalStateException("Folder is open");
    if (!maildir.exists() || !maildir.canRead())
      throw new FolderNotFoundException(this);
    // create subdirectories if necessary
    try
    {
      if (!tmpdir.exists())
        tmpdir.mkdirs();
      if (!newdir.dir.exists())
        newdir.dir.mkdirs();
      if (!curdir.dir.exists())
        curdir.dir.mkdirs();
    }
    catch (IOException e)
    {
      throw new FolderNotFoundException(this, "Can't create subdirectories");
    }
    if (mode==READ_WRITE) 
    {
      if (!maildir.canWrite())
        throw new MessagingException("Folder is read-only");
    }
    // OK
    this.mode = mode;
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
    if (mode==-1)
      throw new IllegalStateException("Folder is closed");
    if (expunge)
      expunge();
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
    if (mode==-1)
      throw new IllegalStateException("Folder is closed");
    if (!exists())
      throw new FolderNotFoundException(this);
    if (mode==Folder.READ_ONLY)
      throw new IllegalWriteException();
    Message[] expunged;
    synchronized (this)
    {
      List elist = new ArrayList();
      try
      {
        // delete in new
        if (newdir.messages!=null)
        {
          int nlen = newdir.messages.length;
          for (int i=0; i<nlen; i++)
            newdir.messages[i].file.delete();
        }
        // delete in cur
        if (newdir.messages!=null)
        {
          int nlen = newdir.messages.length;
          for (int i=0; i<nlen; i++)
            newdir.messages[i].file.delete();
        }
      }
      catch (SecurityException e)
      {
        throw new IllegalWriteException(e.getMessage());
      }
      expunged = new Message[elist.size()];
      elist.toArray(expunged);
    }
    if (expunged.length>0)
      notifyMessageRemovedListeners(true, expunged);
    return expunged;
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
  public synchronized int getMessageCount() 
    throws MessagingException 
  {
    statDir(curdir);
    statDir(newdir);
    return curdir.messages.length + newdir.messages.length;
  }

  /**
   * Returns the number of new messages in this folder.
   * @exception MessagingException if a messaging error occurred
   */
  public synchronized int getNewMessageCount()
    throws MessagingException
  {
    statDir(newdir);
    return newdir.messages.length;
  }

  /**
   * Returns the specified message number from this folder.
   * @exception MessagingException if a messaging error occurred
   */
  public synchronized Message getMessage(int msgnum) 
    throws MessagingException 
  {
    statDir(curdir);
    statDir(newdir);
    int clen = curdir.messages.length;
    int alen = clen+newdir.messages.length;
    int index = msgnum-1;
    if (index<0 || index>=alen)
      throw new MessagingException("No such message: "+msgnum);
    if (index<clen)
      return curdir.messages[index];
    else
      return newdir.messages[index-clen];
  }
	
  /**
   * Returns the messages in this folder.
   * @exception MessagingException if a messaging error occurred
   */
  public synchronized Message[] getMessages() 
    throws MessagingException 
  {
    statDir(curdir);
    statDir(newdir);
    int clen = curdir.messages.length;
    int nlen = newdir.messages.length;
    int alen = clen+nlen;
    Message[] m = new Message[alen];
    System.arraycopy(curdir.messages, 0, m, 0, clen);
    System.arraycopy(newdir.messages, 0, m, clen, nlen);
    return m;
  }

  /**
   * Check the specified directory for messages,
   * repopulating its <code>messages</code> member if necessary,
   * and updating its timestamp.
   */
  void statDir(MaildirTuple dir)
    throws MessagingException
  {
    long timestamp = dir.dir.lastModified();
    if (timestamp==dir.timestamp)
      return;
    try
    {
      File[] files = dir.dir.listFiles(filter);
      int mlen = files.length;
      dir.messages = new MaildirMessage[mlen];
      for (int i=0; i<mlen; i++)
      {
        File file = files[i];
        String uniq = file.getName();
        String info = null;
        int ci = uniq.indexOf(':');
        if (ci!=-1)
        {
          info = uniq.substring(ci+1);
          uniq = uniq.substring(0, ci);
        }
        dir.messages[i] = new MaildirMessage(this, file, uniq, info, i+1);
      }
      dir.timestamp = timestamp;
    }
    catch (IOException e)
    {
      throw new MessagingException(e.getMessage(), e);
    }
  }

  /**
   * Move the specified message between new and cur,
   * depending on whether it has been seen or not.
   */
  void setSeen(MaildirMessage message, boolean seen)
    throws MessagingException
  {
    try
    {
      File src = message.file;
      if (seen)
      {
        String dstname = new StringBuffer(message.uniq)
          .append(':')
          .append(message.getInfo())
          .toString();
        File dst = new File(curdir.dir, dstname);
        src.renameTo(dst);
      }
      else
      {
        File dst = new File(newdir.dir, message.uniq);
        src.renameTo(dst);
      }
    }
    catch (IOException e)
    {
      throw new MessagingException(e.getMessage(), e);
    }
  }

  /**
   * Appends messages to this folder.
   * Only MimeMessages within the array will be appended, as we don't know
   * how to retrieve internet content for other kinds.
   * @param m an array of messages to be appended
   */
  public synchronized void appendMessages(Message[] m) 
    throws MessagingException 
  {
    MaildirMessage[] n;
    synchronized (this)
    {
      // make sure our message counts are up to date
      statDir(newdir);
      statDir(curdir);
      int nlen = newdir.messages.length;
      int clen = curdir.messages.length;
      
      List appended = new ArrayList(m.length);
      for (int i=0; i<m.length; i++) 
      {
        if (m[i] instanceof MimeMessage) 
        {
          MimeMessage src = (MimeMessage)m[i];
          Flags flags = src.getFlags();
          int count = flags.contains(Flags.Flag.SEEN) ? ++clen : ++nlen;
          MaildirMessage dst = new MaildirMessage(this, src, count);
          // TODO deliver to new
          throw new javax.mail.MethodNotSupportedException("Not yet implemented");
          //appended.add(dst);
        }
      }
      n = new MaildirMessage[appended.size()];
      if (n.length>0) 
        appended.toArray(n);
    }
    // propagate event
    if (n.length>0)
      notifyMessageAddedListeners(n);
  }

  /**
   * Returns the parent folder.
   */
  public Folder getParent() 
    throws MessagingException 
  {
    return store.getFolder(maildir.getParent());
  }

  /**
   * Returns the subfolders of this folder.
   */
  public Folder[] list() 
    throws MessagingException 
  {
    if (type!=HOLDS_FOLDERS)
      throw new MessagingException("This folder can't contain subfolders");
    try 
    {
      String[] files = maildir.list();
      Folder[] folders = new Folder[files.length];
      for (int i=0; i<files.length; i++)
      folders[i] =
        store.getFolder(maildir.getAbsolutePath()+File.separator+files[i]);
      return folders;
    }
    catch (SecurityException e) 
    {
      throw new MessagingException("Access denied", e);
    }
  }

  /**
   * Returns the subfolders of this folder matching the specified pattern.
   */
  public Folder[] list(String pattern) 
    throws MessagingException 
  {
    if (type!=HOLDS_FOLDERS)
      throw new MessagingException("This folder can't contain subfolders");
    try 
    {
      String[] files = maildir.list(new MaildirListFilter(pattern));
      Folder[] folders = new Folder[files.length];
      for (int i=0; i<files.length; i++)
      folders[i] =
        store.getFolder(maildir.getAbsolutePath()+File.separator+files[i]);
      return folders;
    } 
    catch (SecurityException e) 
    {
      throw new MessagingException("Access denied", e);
    }
  }

  /**
   * Returns the separator character.
   */
  public char getSeparator() 
    throws MessagingException 
  {
    return File.separatorChar;
  }

  /**
   * Creates this folder in the store.
   */
  public boolean create(int type) 
    throws MessagingException 
  {
    if (maildir.exists())
      throw new MessagingException("Folder already exists");
    switch (type) 
    {
      case HOLDS_FOLDERS:
        try 
        {
          maildir.mkdirs();
          this.type = type;
          notifyFolderListeners(FolderEvent.CREATED);
          return true;
        }
        catch (SecurityException e) 
        {
          throw new MessagingException("Access denied", e);
        }
      case HOLDS_MESSAGES:
        try 
        {
          synchronized (this) 
          {
            maildir.mkdirs();
            tmpdir.mkdirs();
            newdir.dir.mkdirs();
            curdir.dir.mkdirs();
          }
          this.type = type;
          notifyFolderListeners(FolderEvent.CREATED);
          return true;
        } 
        catch (IOException e) 
        {
          throw new MessagingException("I/O error writing mailbox", e);
        }
        catch (SecurityException e) 
        {
          throw new MessagingException("Access denied", e);
        }
    }
    return false;
  }

  /**
   * Deletes this folder.
   */
  public boolean delete(boolean recurse) 
    throws MessagingException 
  {
    if (recurse) 
    {
      try 
      {
        if (type==HOLDS_FOLDERS) 
        {
          Folder[] folders = list();
          for (int i=0; i<folders.length; i++)
            if (!folders[i].delete(recurse))
              return false;
        }
        delete(maildir);
        notifyFolderListeners(FolderEvent.DELETED);
        return true;
      } 
      catch (SecurityException e) 
      {
        throw new MessagingException("Access denied", e);
      }
    } 
    else 
    {
      try 
      {
        if (type==HOLDS_FOLDERS) 
        {
          Folder[] folders = list();
          if (folders.length>0)
            return false;
        }
        delete(maildir);
        notifyFolderListeners(FolderEvent.DELETED);
        return true;
      } 
      catch (SecurityException e) 
      {
        throw new MessagingException("Access denied", e);
      }
    }
  }

  /**
   * Depth-first file/directory delete.
   */
  void delete(File file)
    throws SecurityException
  {
    if (file.isDirectory())
    {
      File[] files = file.listFiles();
      for (int i=0; i<files.length; i++)
        delete(files[i]);
    }
    file.delete();
  }

  /**
   * Renames this folder.
   */
  public boolean renameTo(Folder folder) 
    throws MessagingException 
  {
    try 
    {
      String filename = folder.getFullName();
      if (filename!=null) 
      {
        maildir.renameTo(new File(filename));
        notifyFolderListeners(FolderEvent.RENAMED);
        return true;
      } 
      else
        throw new MessagingException("Illegal filename: null");
    } 
    catch (SecurityException e) 
    {
      throw new MessagingException("Access denied", e);
    }
  }

  /**
   * Returns the subfolder of this folder with the specified name.
   */
  public Folder getFolder(String filename) 
    throws MessagingException 
  {
    if (INBOX.equalsIgnoreCase(filename))
    {
      try
      {
        return store.getFolder(INBOX);
      }
      catch (MessagingException e)
      {
        // fall back to standard behaviour
      }
    }
    return store.getFolder(maildir.getAbsolutePath()+File.separator+filename);
  }

  /**
   * Filename filter that rejects dotfiles.
   */
  static class MaildirFilter
    implements FilenameFilter
  {

    public boolean accept(File dir, String name)
    {
      return name.length()>0 && name.charAt(0)!=0x2e;
    }
    
  }

  /**
   * Structure holding the details for a maildir subdirectory.
   */
  static class MaildirTuple
  {
    
    File dir;
    long timestamp = 0L;
    MaildirMessage[] messages = null;

    MaildirTuple(File dir)
    {
      this.dir = dir;
    }
    
  }

  /**
   * Filename filter for listing subfolders.
   */
  class MaildirListFilter 
    implements FilenameFilter 
  {

    String pattern;
    int asteriskIndex, percentIndex;
	   
    MaildirListFilter(String pattern) 
    {
      this.pattern = pattern;
      asteriskIndex = pattern.indexOf('*');
      percentIndex = pattern.indexOf('%');
    }
	   
    public boolean accept(File directory, String name) 
    {
      if (asteriskIndex>-1) 
      {
        String start = pattern.substring(0, asteriskIndex);
        String end = pattern.substring(asteriskIndex+1, pattern.length());
        return (name.startsWith(start) &&
            name.endsWith(end));
      } 
      else if (percentIndex>-1) 
      {
        String start = pattern.substring(0, percentIndex);
        String end = pattern.substring(percentIndex+1, pattern.length());
        return (directory.equals(maildir) &&
            name.startsWith(start) &&
            name.endsWith(end));
      }
      return name.equals(pattern);
    }
  }
    
}
