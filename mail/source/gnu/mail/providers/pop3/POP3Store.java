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
import java.net.Socket;
import java.net.UnknownHostException;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.URLName;

import gnu.mail.util.CRLFInputStream;
import gnu.mail.util.CRLFOutputStream;
import gnu.mail.util.MessageInputStream;

/**
 * The storage class implementing the POP3 mail protocol.
 *
 * @author <a href='mailto:dog@gnu.org'>Chris Burdess</a>
 * @author <a href='mailto:nferrier@tapsellferrier.co.uk'>Nic Ferrier</a>
 * @version 1.2
 */
public final class POP3Store
  extends Store
{

  /**
   * The default POP3 port.
   */
  public static final int DEFAULT_PORT = 110;

  static int fetchsize = 1024;

  Socket socket;
  CRLFInputStream in;
  CRLFOutputStream out;
  String hostname; // the server hostname

  static int OK = 0, ERR = -1; // response codes
  String response; // last response

  POP3Folder root; // the root folder

  /**
   * Constructor.
   */
  public POP3Store(Session session, URLName urlname)
  {
    super(session, urlname);
    debug = session.getDebug();
    String ccs = session.getProperty("mail.pop3.fetchsize");
    if (ccs!=null)
    {
      try
      {
        fetchsize = Math.max(Integer.parseInt(ccs), 1024);
      }
      catch (NumberFormatException e)
      {}
    }
  }


  //protocol and socket methods

  /**
   * Connects to the POP3 server and authenticates with the specified
   * parameters.
   */
  protected boolean protocolConnect(String host, int port, String username, String password)
    throws MessagingException
  {
    if (port<0)
      port = DEFAULT_PORT;
    if (host==null || username==null || password==null)
      return false;
    if (socket!=null)
      return true;
    synchronized (this)
    {
      try
      {
        hostname = host;
        socket = new Socket(host, port);
        in = new CRLFInputStream(socket.getInputStream());
        out = new CRLFOutputStream(socket.getOutputStream());
        if (getResponse()!=OK)
          throw new MessagingException("Connect failed: "+response);
        int index = response.indexOf(' ');
        if (index>-1)
          hostname = response.substring(0, index);
        send("USER "+username);
        if (getResponse()!=OK)
          return false;
        send("PASS "+password);
        if (getResponse()!=OK)
          return false;
        return true;
      }
      catch (UnknownHostException e)
      {
        throw new MessagingException("Connect failed", e);
      }
      catch (IOException e)
      {
        throw new MessagingException("Connect failed", e);
      }
      catch (NullPointerException e)
      {
        throw new MessagingException("Connect failed",e);
      }
    }
  }

  /**
   * Closes the connection.
   */
  public synchronized void close()
    throws MessagingException
  {
    if (socket!=null)
    {
      synchronized (this)
      {
        try
        {
          send("QUIT");
          if (getResponse()!=OK)
            throw new MessagingException("Close failed: "+response);
          socket.close();
          socket = null;
        }
        catch (IOException e)
        {
          // socket.close() always seems to throw an exception!
          //throw new MessagingException("Close failed", e);
        }
      }
    }
    super.close();

  }

  /**
   * Parse the response from the server.
   * If the <code>Store</code> switch <code>debug</code> is
   * <code>true</code> then the response is echoed to
   * <code>System.err</code>.
   */
  private int getResponse()
    throws IOException
  {
    String okstr = "+OK", errstr = "-ERR";
    response = in.readLine();
    if (debug)
      System.err.println("POP< "+response);
    if (response.indexOf(okstr)==0)
    {
      response = response.substring(okstr.length()).trim();
      return OK;
    }
    else if (response.indexOf(errstr)==0)
      response = response.substring(errstr.length()).trim();
    return ERR;
  }

  /** 
   * Send the command to the server.
   * If the <code>Store</code> switch <code>debug</code> is
   * <code>true</code> then the command is echoed to
   * <code>System.err</code>.
   */
  private void send(String command)
    throws IOException
  {
    if (debug)
      System.err.println("POP> "+command);
    out.write(command.getBytes());
    out.writeln();
    out.flush();
  }

  /** 
   * Get a stream of content related to a particular message.
   */
  synchronized InputStream popRETR(int msgnum)
    throws MessagingException
  {
    try
    {
      send("RETR "+msgnum);
      if (getResponse()!=OK)
        throw new MessagingException("Retrieve failed: "+response);
      return new MessageInputStream(in);
    }
    catch (IOException e)
    {
      throw new MessagingException("Retrieve failed.", e);
    }
    catch (NumberFormatException e)
    {
      throw new MessagingException("Retrieve failed.", e);
    }
  }

  /** 
   * Get just the headers of a particular message.
   * This method issues the command
   * <blockquote>
   *   TOP msg 0
   * </blockquote>
   * Which servers should understand to be:
   * <blockquote>
   *   send 0 lines of the content, ie: the just the header
   * </blockquote>
   * But I'm not sure of the extent of support in POP servers for
   * this particular trick. If it goes wrong this code will die
   * because we expect to see something.
   */
  synchronized InputStream popTOP(int msgnum)
    throws MessagingException
  {
    try
    {
      send("TOP "+msgnum+" 0");
      if (getResponse()!=OK)
        throw new MessagingException("Retrieve failed: "+response);
      return new MessageInputStream(in);
    }
    catch (IOException e)
    {
      throw new MessagingException("Retrieve failed.", e);
    }
    catch (NumberFormatException e)
    {
      throw new MessagingException("Retrieve failed.", e);
    }
  }

  /** 
   * @return the hostname of the POP server.
   */
  String getHostName()
  {
    return hostname;
  }

  //javamail provider methods

  /** 
   * Used by the Folder to assess how many messages there are.
   */
  synchronized int getMessageCount()
    throws MessagingException
  {
    try
    {
      send("STAT");
      if (getResponse()!=OK)
        throw new MessagingException("Status failed: "+response);
      try
      {
        return Integer.parseInt(response.substring(0, response.indexOf(' ')));
      }
      catch (NumberFormatException e)
      {
        throw new MessagingException("Status failed: "+response);
      }
    }
    catch (IOException e)
    {
      throw new MessagingException("Status failed.", e);
    }
  }

  /** 
   * Retrieve the message.
   * This method tries to build a nearlly empty message object (with
   * just the size). If it can't do that it pulls back the whole thing.
   */
  synchronized Message getMessage(POP3Folder folder, int msgnum)
    throws MessagingException
  {
    int size = -1;
    try
    {
      send("LIST "+msgnum);
      if (getResponse()!=OK)
        throw new MessagingException("Top failed: "+response);
      //try and get the message size
      String sizePart = response.substring(response.indexOf(' ')+1);
      size = Integer.parseInt(sizePart);
    }
    catch (Throwable t)
    {
      //failed to work out the size
      //- just try to retrieve the whole message
      try
      {
        send("RETR "+msgnum);
        if (getResponse()!=OK)
          throw new MessagingException("Retrieve failed: "+response);
      }
      catch (IOException e)
      {
        throw new MessagingException("Retrieve failed.", e);
      }
    }
    // get the message and create an object for it
		InputStream min = new MessageInputStream(in, debug);
    return (size>-1) ?
      new POP3Message(folder, min, msgnum, size) :
			new POP3Message(folder, min, msgnum);
  }

  synchronized void delete(int msgnum)
    throws MessagingException
  {
    try
    {
      send("DELE "+msgnum);
      if (getResponse()!=OK)
        throw new MessagingException("Delete failed: "+response);
    }
    catch (IOException e)
    {
      throw new MessagingException("Delete failed.", e);
    }
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
