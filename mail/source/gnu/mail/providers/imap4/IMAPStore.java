/*
 * IMAPStore.java
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

import java.io.IOException;
import java.net.UnknownHostException;
import javax.mail.Folder;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.URLName;

/**
 * The storage class implementing the IMAP4rev1 mail protocol.
 *
 * @author <a href='mailto:dog@gnu.org'>Chris Burdess</a>
 * @version 0.1
 */
public class IMAPStore
  extends Store
{

  // -- TESTING --
  public static void main(String[] args) {
    try {
      Session session = Session.getDefaultInstance(System.getProperties());
      session.setDebug(true);
      URLName url = new URLName("imap://localhost/");
      Store s = session.getStore(url);
      s.connect("localhost", "test", "imaptest");
      Folder f = s.getFolder("INBOX");
      f.open(Folder.READ_ONLY);
      javax.mail.Message[] m = f.getMessages();
      for (int i=0; i<m.length; i++) {
        System.out.println(m[i].getSubject());
      }
      f.close(false);
      s.close();
    } catch (Exception e) {
      e.printStackTrace(System.err);
    }
  }
  // -- END TESTING --

  /**
   * The default IMAP port.
   */
  public static final int DEFAULT_PORT = 143;

  /**
   * The connection to the IMAP server.
   */
  protected IMAPConnection connection;

  /**
   * Folder representing the root namespace of the IMAP connection.
   */
  protected IMAPFolder root;

  /**
   * Constructor.
   */
  public IMAPStore(Session session, URLName url)
  {
    super(session, url);
  }

  /**
   * Connects to the IMAP server and authenticates with the specified
   * parameters.
   */
  protected boolean protocolConnect(String host, int port, String username,
      String password)
    throws MessagingException
  {
    if (port<0) port = DEFAULT_PORT;
    if (host==null || username==null || password==null)
      return false;
    if (connection!=null)
      return true;
    synchronized (this)
    {
      try
      {
        connection = new IMAPConnection(host, port);
        if (session.getDebug())
          connection.setDebug(true);
        return connection.login(username, password);
      }
      catch (UnknownHostException e)
      {
        throw new MessagingException(e.getMessage(), e);
      }
      catch (IOException e)
      {
        throw new MessagingException(e.getMessage(), e);
      }
    }
  }

  /**
   * Closes the connection.
   */
  public synchronized void close()
    throws MessagingException
  {
    if (connection!=null)
    {
      synchronized (this)
      {
        try
        {
          connection.logout();
        }
        catch (IOException e)
        {
        }
        connection = null;
      }
    }
    super.close();
  }

  /**
   * Returns the root folder.
   */
  public Folder getDefaultFolder()
    throws MessagingException
  {
    return getFolder("");
  }

  /**
   * Returns the folder with the specified name.
   */
  public Folder getFolder(String name)
    throws MessagingException
  {
    return new IMAPFolder(this, name);
  }

  /**
   * Returns the folder whose name is the file part of the specified URLName.
   */
  public Folder getFolder(URLName urlname)
    throws MessagingException
  {
    return getFolder(urlname.getFile());
  }

}
