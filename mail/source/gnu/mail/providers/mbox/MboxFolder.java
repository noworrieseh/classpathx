/*
 * MboxFolder.java
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
 * Contributor(s): Daniel Thor Kristjan <danielk@cat.nyu.edu>
 *                   close and expunge clarification.
 *                 Sverre Huseby <sverrehu@online.no> gzipped mailboxes
 */

package gnu.mail.providers.mbox;

import java.io.*;
import java.net.*;
import java.text.*;
import java.util.*;
import java.util.zip.*;
import javax.mail.*;
import javax.mail.event.*;
import javax.mail.internet.*;
import gnu.mail.util.*;
import gnu.mail.treeutil.StatusEvent;

/**
 * The folder class implementing a UNIX mbox-format mailbox.
 *
 * @author dog@dog.net.uk
 * @version 2.0
 */
public class MboxFolder 
  extends Folder 
{

  static final DateFormat df = new SimpleDateFormat("EEE MMM d H:m:s yyyy");
  static final String GNU_MESSAGE_ID = "X-GNU-Message-Id";
  static final String FROM = "From ";
	
  File file;
  MboxMessage[] messages;
  boolean open;
  boolean readOnly;
  int type;
  boolean inbox;
  
  Flags permanentFlags = null;
		
  /**
   * Constructor.
   */
  protected MboxFolder(Store store, String filename, boolean inbox) 
  {
    super(store);
    
    file = new File(filename);
    if (file.exists() && file.isDirectory())
      type = HOLDS_FOLDERS;
    else
      type = HOLDS_MESSAGES;
    this.inbox = inbox;
    open = false;
    readOnly = true;
    messages = new MboxMessage[0];
  }
	
  /**
   * Constructor.
   */
  protected MboxFolder(Store store, String filename) 
  {
    this(store, filename, false);
  }

  /**
   * Returns the name of this folder.
   */
  public String getName() 
  {
    if (inbox)
      return "INBOX";
    return file.getName();
  }
	
  /**
   * Returns the full name of this folder.
   */
  public String getFullName() 
  {
    if (inbox)
      return "INBOX";
    return file.getPath();
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
    return file.exists();
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
    if (mode==READ_WRITE) 
    {
      if (!file.canWrite())
        throw new MessagingException("Folder is read-only");
      if (!acquireLock())
        throw new MessagingException("Unable to acquire lock");
      readOnly = false;
    }

    if (!file.canRead())
      throw new MessagingException("Can't read folder: "+file.getName());

    LineInputStream in = null;
    String filename = file.getAbsolutePath();
    try 
    {
      // Read messages
      MboxStore store = (MboxStore)this.store;
      store.log("reading "+filename);
      
      Vector vm = new Vector(256);
      in = new LineInputStream(getInputStream());
      int count = 1;
      String line, fromLine = null;
      ByteArrayOutputStream buf = null;

      // notify listeners
      store.processStatusEvent(new StatusEvent(store,
            StatusEvent.OPERATION_START,
            "open"));
          
      for (line = in.readLine(); line!=null; line = in.readLine()) 
      {
        if (line.indexOf(FROM)==0) 
        {
          if (buf!=null)
          {
            byte[] bytes = buf.toByteArray();
            ByteArrayInputStream bin = new ByteArrayInputStream(bytes);
            MboxMessage m = new MboxMessage(this, fromLine, bin, count++);
            vm.addElement(m);

            store.processStatusEvent(new StatusEvent(store,
                  StatusEvent.OPERATION_UPDATE,
                  "open",
                  1,
                  StatusEvent.UNKNOWN,
                  count-1));
          }
          fromLine = line;
          buf = new ByteArrayOutputStream();
        }
        else if (buf!=null)
        {
          byte[] bytes = decodeFrom(line).getBytes();
          buf.write(bytes, 0, bytes.length);
          buf.write(10); // LF
        }
      }
      if (buf!=null)
      {
        byte[] bytes = buf.toByteArray();
        ByteArrayInputStream bin = new ByteArrayInputStream(bytes);
        MboxMessage m = new MboxMessage(this, fromLine, bin, count++);
        vm.addElement(m);

        store.processStatusEvent(new StatusEvent(store,
              StatusEvent.OPERATION_UPDATE,
              "open",
              1,
              StatusEvent.UNKNOWN,
              count-1));
      }
      messages = new MboxMessage[vm.size()];
      vm.copyInto(messages);
      buf = null;
      vm = null;

      store.processStatusEvent(new StatusEvent(store,
            StatusEvent.OPERATION_END,
            "open"));

      // OK
      open = true;
      notifyConnectionListeners(ConnectionEvent.OPENED);
    }
    catch (IOException e) 
    {
      throw new MessagingException("Unable to open folder: "+filename, e);
    }
    finally
    {
      // release any file descriptors
      try
      {
        if (in!=null)
          in.close();
      }
      catch (IOException e)
      {
        // we tried
      }
    }
  }

  /**
   * Returns the specified line with any From_ line encoding removed.
   */
  public static String decodeFrom(String line)
  {
    if (line!=null)
    {
      int len = line.length();
      for (int i=0; i<(len-5); i++)
      {
        char c = line.charAt(i);
        if (i>0 &&
            (c=='F' &&
             line.charAt(i+1)=='r' &&
             line.charAt(i+2)=='o' &&
             line.charAt(i+3)=='m' &&
             line.charAt(i+4)==' '))
          return line.substring(1);
        if (c!='>')
          break;
      }
    }
    return line;
  }
  
  /**
   * Closes this folder.
   * @param expunge if the folder is to be expunged before it is closed
   * @exception MessagingException if a messaging error occurred
   */
  public void close(boolean expunge) 
    throws MessagingException 
  {
    if (open) 
    {
      if (expunge)
        expunge();
    
      if (!readOnly)
      {
        // Save messages
        MboxStore store = (MboxStore)this.store;
        store.log("saving "+file.getAbsolutePath());
        synchronized (this) 
        {
          OutputStream os = null;
          try 
          {
            os = getOutputStream();
            BufferedOutputStream bos = new BufferedOutputStream(os);
            MboxOutputStream mos = new MboxOutputStream(bos);

            store.processStatusEvent(new StatusEvent(store,
                  StatusEvent.OPERATION_START,
                  "close"));
            for (int i=0; i<messages.length; i++) 
            {
              String fromLine = fromLine(messages[i]);
              bos.write(fromLine.getBytes());
              bos.write('\n');
              bos.flush();
              messages[i].writeTo(mos);
              mos.flush();

              store.processStatusEvent(new StatusEvent(store,
                    StatusEvent.OPERATION_UPDATE,
                    "close",
                    1,
                    messages.length,
                    i+1));
            }

            store.processStatusEvent(new StatusEvent(store,
                  StatusEvent.OPERATION_END,
                  "close"));
          } 
          catch (IOException e) 
          {
            throw new MessagingException("I/O error writing mailbox", e);
          }
          finally
          {
            // close any file descriptors
            try
            {
              if (os!=null)
                os.close();
            }
            catch (IOException e)
            {
              // we tried
            }
          }
        }
        if (!releaseLock())
          store.log("unable to clear up lock file!");
      }
      
      open = false;
      messages = new MboxMessage[0]; // release memory
      notifyConnectionListeners(ConnectionEvent.CLOSED);
    }
  }

  /**
   * Returns the From_ line for the specified mbox message.
   * If this does not already exist (the message was appended to the folder
   * since it was last opened), we will attempt to generate a suitable From_
   * line for it.
   */
  protected String fromLine(MboxMessage message)
    throws MessagingException
  {
    String fromLine = message.fromLine;
    if (fromLine==null)
    {
      StringBuffer buf = new StringBuffer("From ");
      
      String from = "-";
      try
      {
        Address[] f = message.getFrom();
        if (f!=null && f.length>0) 
        {
          if (f[0] instanceof InternetAddress)
            from = ((InternetAddress)f[0]).getAddress();
          else
            from = f[0].toString();
        }
      }
      catch (AddressException e)
      {
        // these things happen...
      }
      buf.append(from);
      buf.append(' ');
      
      Date date = message.getSentDate();
      if (date==null)
        date = message.getReceivedDate();
      if (date==null)
        date = new Date();
      buf.append(df.format(date));
      
      fromLine = buf.toString();
    }
    return fromLine;
  }
	
  /**
   * Expunges this folder.
   * This deletes all the messages marked as deleted.
   * @exception MessagingException if a messaging error occurred
   */
  public synchronized Message[] expunge() 
    throws MessagingException 
  {
    Vector ve = new Vector();
    if (open) 
    {
      Vector vm = new Vector();
      for (int i=0; i<messages.length; i++) 
      {
        Flags flags = messages[i].getFlags();
        if (flags.contains(Flags.Flag.DELETED))
          ve.addElement(messages[i]);
        else
          vm.addElement(messages[i]);
      }
      messages = new MboxMessage[vm.size()];
      vm.copyInto(messages);
    }
    Message[] expunged = new Message[ve.size()];
    ve.copyInto(expunged);
    if (expunged.length>0)
      notifyMessageRemovedListeners(true, expunged);
    return expunged;
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
    if (permanentFlags == null) 
    {
      Flags flags = new Flags(); 
      flags.add(Flags.Flag.DELETED);
      flags.add(Flags.Flag.SEEN);
      flags.add(Flags.Flag.RECENT);
      permanentFlags = flags;
    }
    return permanentFlags;
  }
	
  /**
   * Returns the number of messages in this folder.
   * @exception MessagingException if a messaging error occurred
   */
  public int getMessageCount() 
    throws MessagingException 
  {
    return messages.length;
  }

  /**
   * Returns the specified message number from this folder.
   * @exception MessagingException if a messaging error occurred
   */
  public Message getMessage(int msgnum) 
    throws MessagingException 
  {
    int index = msgnum-1;
    if (index<0 || index>=messages.length)
      throw new MessagingException("No such message: "+msgnum);
    return messages[index];
  }
	
  /**
   * Returns the messages in this folder.
   * @exception MessagingException if a messaging error occurred
   */
  public synchronized Message[] getMessages() 
    throws MessagingException 
  {
    // Return a copy of the message array
    Message[] m = new Message[messages.length];
    System.arraycopy(messages, 0, m, 0, messages.length);
    return m;
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
    Vector appended = new Vector(m.length);
    int count = messages.length;
    for (int i=0; i<m.length; i++) 
    {
      if (m[i] instanceof MimeMessage) 
      {
        MimeMessage mimem = (MimeMessage)m[i];
        MboxMessage mboxm = new MboxMessage(this, mimem, count++);
        if (mimem instanceof MboxMessage)
          mboxm.fromLine = ((MboxMessage)mimem).fromLine;
        appended.addElement(mboxm);
      }
    }
    int appendedLength = appended.size();
    if (appendedLength>0) 
    {
      MboxMessage[] n = new MboxMessage[appendedLength];
      appended.copyInto(n);

      // copy into the messages array
      Vector accumulator = new Vector(messages.length+n.length);
      for (int i=0; i<messages.length; i++)
        accumulator.addElement(messages[i]);
      for (int i=0; i<n.length; i++)
        accumulator.addElement(n[i]);
      messages = new MboxMessage[accumulator.size()];
      accumulator.copyInto(messages);
      
      // propagate event
      notifyMessageAddedListeners(n);
    }
  }

  /**
   * Does nothing.
   * @exception MessagingException ignore
   */
  public void fetch(Message[] messages, FetchProfile fetchprofile) 
    throws MessagingException 
  {
  }

  /**
   * Returns the parent folder.
   */
  public Folder getParent() 
    throws MessagingException 
  {
    return store.getFolder(file.getParent());
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
      String[] files = file.list();
      Folder[] folders = new Folder[files.length];
      for (int i=0; i<files.length; i++)
      folders[i] =
        store.getFolder(file.getAbsolutePath()+File.separator+files[i]);
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
      String[] files = file.list(new MboxFilenameFilter(pattern));
      Folder[] folders = new Folder[files.length];
      for (int i=0; i<files.length; i++)
      folders[i] =
        store.getFolder(file.getAbsolutePath()+File.separator+files[i]);
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
    if (file.exists())
      throw new MessagingException("Folder already exists");
    switch (type) 
    {
      case HOLDS_FOLDERS:
        try 
        {
          file.mkdirs();
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
            createNewFile(file);
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
        if (!readOnly)
          releaseLock();
        file.delete();
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
        if (!readOnly)
          releaseLock();
        file.delete();
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
        file.renameTo(new File(filename));
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
    String INBOX = "INBOX";
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
    return store.getFolder(file.getAbsolutePath()+File.separator+filename);
  }

  /**
   * Checks if the current file is or is supposed to be
   * compressed. Uses the filename to figure it out.
   */
  private boolean isGzip() 
  {
    return file.getName().toLowerCase().endsWith(".gz");
  }

  /**
   * Creates an input stream that possibly will decompress the
   * file contents.
   */
  private InputStream getInputStream() 
    throws IOException 
  {
    InputStream in;

    in = new FileInputStream(file);
    if (isGzip())
      in = new GZIPInputStream(in);
    return in;
  }

  /**
   * Creates an output stream that possibly will compress
   * whatever is sent to it, based on the current filename.
   */
  private OutputStream getOutputStream() 
    throws IOException 
  {
    OutputStream out;

    out = new FileOutputStream(file);
    if (isGzip())
      out = new GZIPOutputStream(out);
    return out;
  }
	
  /**
   * Locks this mailbox.
   * This uses a dotlock-like mechanism - see createNewFile().
   * If the directory containing the mbox
   * folder is not writable, we will not be able to open the mbox for
   * writing either.
   */
  public synchronized boolean acquireLock() 
  {
    try
    {
      String filename = file.getPath();
      String lockFilename = filename+".lock";
      File lock = new File(lockFilename);
      MboxStore store = (MboxStore)this.store;
      store.log("creating "+lock.getPath());
      if (lock.exists())
        return false;
      //if (!lock.canWrite())
      //  return false;
      createNewFile(lock);
      return true;
    }
    catch (IOException e)
    {
      MboxStore store = (MboxStore)this.store;
      store.log("I/O exception acquiring lock on "+file.getPath());
    }
    catch (SecurityException e)
    {
      MboxStore store = (MboxStore)this.store;
      store.log("Security exception acquiring lock on "+file.getPath());
    }
    return false;
  }

  /**
   * This method creates a new file.
   * Because Java cannot properly dotlock a file by creating a temporary
   * file and hardlinking it (some platforms do not support hard links) we
   * must use this method to create a zero-length inode.
   * This is a replacement for File.createNewFile(), which only exists in
   * the JDK since 1.2.
   * The idea is simply to touch the specified file.
   */
  private void createNewFile(File file)
    throws IOException
  {
    // there may be another, more efficient way to do this.
    // certainly just setLastModified() does not work.
    BufferedOutputStream out = new BufferedOutputStream(new 
        FileOutputStream(file));
    out.flush();
    out.close();
    
  }
	
  /**
   * Unlocks this mailbox.
   * This deletes any associated lockfile if it exists. It returns false if
   * an existing lockfile could not be deleted.
   */
  public synchronized boolean releaseLock() 
  {
    try
    {
      String filename = file.getPath();
      String lockFilename = filename+".lock";
      File lock = new File(lockFilename);
      MboxStore store = (MboxStore)this.store;
      store.log("removing "+lock.getPath());
      if (lock.exists())
      {
        if (!lock.delete())
          return false;
      }
      return true;
    }
    catch (SecurityException e)
    {
      MboxStore store = (MboxStore)this.store;
      store.log("Security exception releasing lock on "+file.getPath());
    }
    return false;
  }

  class MboxFilenameFilter 
    implements FilenameFilter 
  {

    String pattern;
    int asteriskIndex, percentIndex;
	   
    MboxFilenameFilter(String pattern) 
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
        return (directory.equals(file) &&
            name.startsWith(start) &&
            name.endsWith(end));
      }
      return name.equals(pattern);
    }
  }
    
}
