/*
 * MboxStore.java
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
import java.net.*;
import javax.mail.*;
import javax.mail.event.*;
import java.util.Hashtable;
import gnu.mail.util.*;

/**
 * The storage class implementing the Mbox mailbox file format.
 *
 * @author dog@dog.net.uk
 * @version 2.0
 */
public class MboxStore 
  extends Store 
{

  private static final char separatorChar = '/';
  
  static int fetchsize = 1024;
  static boolean attemptFallback = true;

  /**
   * Constructor.
   */
  public MboxStore(Session session, URLName urlname) 
  {
    super(session, urlname);
    String fs = session.getProperty("mail.mbox.fetchsize");
    if (fs!=null) 
    {
      try 
      { 
        fetchsize = Math.max(Integer.parseInt(fs), 1024); 
      } 
      catch (NumberFormatException e) 
      {
        log("fetchsize "+fs+" is not a number");
      }
    }
    String af = session.getProperty("mail.mbox.attemptFallback");
    if (af!=null) 
      attemptFallback = Boolean.valueOf(af).booleanValue();
  }
	
  /**
   * There isn't a protocol to implement, so this method just returns.
   */
  protected boolean protocolConnect(
      String host, 
      int port, 
      String username,
      String password) 
    throws MessagingException 
  {
    return true;
  }

  /**
   * Returns the default folder.
   */
  public Folder getDefaultFolder() 
    throws MessagingException 
  {
    // If the url used to contruct the store references a file directly,
    // return this file.
    if (url!=null) 
    {
      String file = url.getFile();
      if (file!=null && file.length()>0) 
        return getFolder(file);
    }
    // Otherwise attempt to return a sensible root folder.
    String mailhome = session.getProperty("mail.mbox.mailhome");
    if (mailhome == null)
    {
      try
      {
        String userhome = System.getProperty("user.home");
        mailhome = userhome+"/Mail"; // elm
        if (!exists(mailhome))
          mailhome = userhome+"/mail";
        if (!exists(mailhome))
          mailhome = null;
      } 
      catch (SecurityException e) 
      {
        log("access denied reading system properties");
        mailhome = "/";
      }
    }
    return getFolder(mailhome);
  }

  /**
   * Returns the folder with the specified filename.
   */
  public Folder getFolder(String filename) 
    throws MessagingException
  {
    boolean inbox = false;
    if ("inbox".equalsIgnoreCase(filename)) 
    {
      // First try the session property mail.mbox.inbox.
      String inboxname = session.getProperty("mail.mbox.inbox");
      if (!exists(inboxname))
        inboxname = null;
      if (inboxname==null && attemptFallback) 
      {
        // Try some common (UNIX) locations.
        try 
        {
          String username = System.getProperty("user.name");
          inboxname = "/var/mail/"+username;
          if (!exists(inboxname))
            inboxname = "/var/spool/mail/"+username;
          if (!exists(inboxname))
          {
            inboxname = null;
            String userhome = System.getProperty("user.home");
            inboxname = userhome+"/mbox";
          }
          if (!exists(inboxname))
            inboxname = null;
        } 
        catch (SecurityException e) 
        {
          // not allowed to read system properties
          log("unable to access system properties");
        }
      }
      if (inboxname!=null)
      {
        filename = inboxname;
        inbox = true;
      }
      // otherwise we assume it is actually a file called "inbox"
    }
    
    // convert into a valid filename for this platform
    StringBuffer buf = new StringBuffer();
    if (filename.length()<1 || filename.charAt(0)!=separatorChar)
      buf.append(File.separator);
    if (separatorChar!=File.separatorChar)
      buf.append(filename.replace(separatorChar, File.separatorChar));
    else
      buf.append(filename);
    filename = buf.toString();
    
    return new MboxFolder(this, filename, inbox);
  }

  /*
   * Indicates whether the file referred to by the specified filename exists.
   */
  private boolean exists(String filename)
  {
    if (filename!=null)
    {
      File file = new File(filename);
      if (separatorChar!=File.separatorChar)
        file = new File(filename.replace(separatorChar, File.separatorChar));
      return file.exists();
    }
    return false;
  }

  /**
   * Returns the folder specified by the filename of the URLName.
   */
  public Folder getFolder(URLName urlname) 
    throws MessagingException 
  {
    return getFolder(urlname.getFile());
  }
	
  Session getSession() 
  {
    return session;
  }

  /**
   * Print a log message.
   */
  void log(String message)
  {
    if (session.getDebug())
      System.out.println("mbox: "+message);
  }

}
