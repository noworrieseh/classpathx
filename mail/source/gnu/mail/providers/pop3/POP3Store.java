/*
 * POP3Store.java
 * Copyright (C) 1999, 2003 Chris Burdess <dog@gnu.org>
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

package gnu.mail.providers.pop3;

import java.io.InputStream;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.List;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.URLName;

/**
 * The storage class implementing the POP3 mail protocol.
 *
 * @author <a href='mailto:dog@gnu.org'>Chris Burdess</a>
 * @author <a href='mailto:nferrier@tapsellferrier.co.uk'>Nic Ferrier</a>
 * @version 1.3
 */
public final class POP3Store
  extends Store
{

	/*
	 * The connection.
	 */
	POP3Connection connection;

	/*
	 * The root folder.
	 */
  POP3Folder root;

  /**
   * Constructor.
   */
  public POP3Store(Session session, URLName urlname)
  {
    super(session, urlname);
  }

  /**
   * Connects to the POP3 server and authenticates with the specified
   * parameters.
   */
  protected boolean protocolConnect(String host, int port, String username,
		 	String password)
    throws MessagingException
  {
    if (host==null || username==null || password==null)
      return false;
    if (connection!=null)
      return true;
    synchronized (this)
    {
      try
      {
				int connectionTimeout =
					getIntProperty(session.getProperty("mail.pop3.connectiontimeout"));
				int timeout =
					getIntProperty(session.getProperty("mail.pop3.timeout"));
				connection = new POP3Connection(host, port,
						connectionTimeout, timeout, session.getDebug());
        // Disable APOP if necessary
        if ("false".equals(session.getProperty("mail.pop3.apop")))
          connection.timestamp = null;
        List capa = connection.capa();
        if (capa!=null)
        {
          if (capa.contains(POP3Connection.STLS))
            connection.stls();
        }
				return connection.authenticate(username, password);
      }
      catch (UnknownHostException e)
      {
        throw new MessagingException("Connect failed", e);
      }
      catch (IOException e)
      {
        throw new MessagingException("Connect failed", e);
      }
    }
  }

	private int getIntProperty(String property)
	{
		if (property!=null)
		{
			try
			{
				return Integer.parseInt(property);
			}
			catch (Exception e)
			{
			}
		}
		return -1;
	}

  /**
   * Closes the connection.
   */
  public void close()
    throws MessagingException
  {
    if (connection!=null)
    {
			boolean rsetBeforeQuit =
				"true".equals(session.getProperty("mail.pop3.rsetbeforequit"));
      synchronized (connection)
      {
        try
        {
					if (rsetBeforeQuit)
						connection.rset();
					connection.quit();
        }
        catch (IOException e)
        {
          throw new MessagingException("Close failed", e);
        }
      }
			connection = null;
    }
    super.close();
  }

  /**
   * Returns the root folder.
   */
  public Folder getDefaultFolder()
    throws MessagingException
  {
    synchronized (this)
    {
      if (root==null)
        root = new POP3Folder(this, Folder.HOLDS_FOLDERS);
    }
    return root;
  }

  /**
   * Returns the folder with the specified name.
   */
  public Folder getFolder(String s)
    throws MessagingException
  {
    return getDefaultFolder().getFolder(s);
  }

  /**
   * Returns the folder whose name is the file part of the specified URLName.
   */
  public Folder getFolder(URLName urlname)
    throws MessagingException
  {
    return getDefaultFolder().getFolder(urlname.getFile());
  }

}
