/*
 * POP3Connection.java
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

package gnu.mail.providers.pop3;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.InputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.ProtocolException;
import java.net.UnknownHostException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import gnu.mail.util.CRLFInputStream;
import gnu.mail.util.CRLFOutputStream;
import gnu.mail.util.LineInputStream;
import gnu.mail.util.Logger;
import gnu.mail.util.MessageInputStream;

/**
 * A POP3 client connection.
 * This implements the entire POP3 specification as detailed in RFC 1939,
 * with the exception of the no-arg LIST and UIDL commands (use STAT
 * followed by multiple LIST and/or UIDL instead) and TOP with a specified
 * number of content lines. It also implements the POP3 extension mechanism
 * CAPA, documented in RFC 2449, as well as the STLS command to initiate TLS
 * over POP3 documented in RFC 2595.
 *
 * @author <a href='mailto:dog@gnu.org'>Chris Burdess</a>
 * @version 1.3
 */
public class POP3Connection
{

  /**
   * The default POP3 port.
   */
  public static final int DEFAULT_PORT = 110;

	// -- POP3 vocabulary --
	protected static final String OK = "+OK";
	protected static final String ERR = "-ERR";
	protected static final String STAT = "STAT";
	protected static final String LIST = "LIST";
	protected static final String RETR = "RETR";
	protected static final String DELE = "DELE";
	protected static final String NOOP = "NOOP";
	protected static final String RSET = "RSET";
	protected static final String QUIT = "QUIT";
	protected static final String TOP = "TOP";
	protected static final String UIDL = "UIDL";
	protected static final String USER = "USER";
	protected static final String PASS = "PASS";
	protected static final String APOP = "APOP";
	protected static final String CAPA = "CAPA";
	protected static final String STLS = "STLS";

	/**
	 * The socket used to communicate with the server.
	 */
  protected Socket socket;

	/**
	 * The socket input stream.
	 */
  protected LineInputStream in;

	/**
	 * The socket output stream.
	 */
  protected CRLFOutputStream out;
  
	/**
	 * The last response received from the server.
	 * The status code (+OK or -ERR) is stripped from the line.
	 */
  protected String response;

	/**
	 * If true, print debugging information.
	 */
	protected boolean debug;

	/**
	 * The APOP timestamp, if sent by the server on connection.
	 * Otherwise null.
	 */
	protected byte[] timestamp;

  /**
   * Creates a new connection to the server.
	 * @param hostname the hostname of the server to connect to
	 * @param port the port to connect to (if &lt;=0, use default POP3 port)
   */
  public POP3Connection(String hostname, int port)
		throws UnknownHostException, IOException
	{
		this(hostname, port, -1, -1, false);
	}
	
  /**
   * Creates a new connection to the server.
	 * @param hostname the hostname of the server to connect to
	 * @param port the port to connect to (if &lt;=0, use default POP3 port)
	 * @param connectionTimeout the connection timeout, in milliseconds
	 * @param timeout the I/O timeout, in milliseconds
	 * @param debug print debugging information
   */
  public POP3Connection(String hostname, int port,
			int connectionTimeout, int timeout, boolean debug)
		throws UnknownHostException, IOException
  {
		this.debug = debug;
    if (port<=0)
      port = DEFAULT_PORT;

		// Set up socket
		// TODO connection timeout
		socket = new Socket(hostname, port);
		if (timeout>0)
			socket.setSoTimeout(timeout);
		
		InputStream in = socket.getInputStream();
		in = new BufferedInputStream(in);
		in = new CRLFInputStream(in);
		this.in = new LineInputStream(in);
		OutputStream out = socket.getOutputStream();
		out = new BufferedOutputStream(out);
		this.out = new CRLFOutputStream(out);
		
		if (!getResponse())
			throw new ProtocolException("Connect failed: "+response);
		// APOP timestamp
		timestamp = parseTimestamp(response);
	}

	/**
	 * Authenticate the specified user.
	 * @param username the user to authenticate
	 * @param password the user's password
	 */
	public boolean authenticate(String username, String password)
		throws IOException
	{
		if (username==null || password==null)
			return false;
		String cmd;
		if (timestamp!=null)
		{
			// APOP <username> <digest>
			try
			{
        byte[] secret = password.getBytes("US-ASCII");
        // compute digest
        byte[] target = new byte[timestamp.length + secret.length];
        System.arraycopy(timestamp, 0, target, 0, timestamp.length);
        System.arraycopy(secret, 0, target, timestamp.length, secret.length);
				MessageDigest md5 = MessageDigest.getInstance("MD5");
				byte[] db = md5.digest(target);
        // create hexadecimal representation
				StringBuffer digest = new StringBuffer();
        for (int i=0; i<db.length; i++)
        {
          int c = (int)db[i];
          if (c<0)
            c += 256;
          digest.append(Integer.toHexString(c));
        }
				// send command
				cmd = new StringBuffer(APOP)
					.append(' ')
					.append(username)
					.append(' ')
					.append(digest.toString())
					.toString();
				send(cmd);
				return getResponse();
			}
			catch (NoSuchAlgorithmException e)
			{
				Logger logger = Logger.getInstance();
				logger.log("pop3", "MD5 algorithm not found, falling back "+
						"to plain authentication");
			}
		}
		// USER <username>
		cmd = new StringBuffer(USER)
			.append(' ')
			.append(username)
			.toString();
		send(cmd);
		if (!getResponse())
			return false;
		// PASS <password>
		cmd = new StringBuffer(PASS)
			.append(' ')
			.append(password)
			.toString();
		send(cmd);
		return getResponse();
	}

  /**
   * Attempts to start TLS on the specified connection.
   * See RFC 2595 for details
   * @return true if successful, false otherwise
   */
  public boolean stls()
    throws IOException
  {
    send(STLS);
    if (!getResponse())
      return false;
    try
    {
      // Attempt to instantiate an SSLSocketFactory
      // This requires introspection, as the class may not be available in
      // all runtimes.
      Class factoryClass = Class.forName("javax.net.ssl.SSLSocketFactory");
      java.lang.reflect.Method getDefault =
        factoryClass.getMethod("getDefault", new Class[0]);
      Object factory = getDefault.invoke(null, new Object[0]);
      // Use the factory to negotiate a TLS session and wrap the current
      // socket.
      Class[] pt = new Class[4];
      pt[0] = Socket.class;
      pt[1] = String.class;
      pt[2] = Integer.TYPE;
      pt[3] = Boolean.TYPE;
      java.lang.reflect.Method createSocket =
        factoryClass.getMethod("createSocket", pt);
      Object[] args = new Object[4];
      args[0] = socket;
      args[1] = socket.getInetAddress().getHostName();
      args[2] = new Integer(socket.getPort());
      args[3] = Boolean.TRUE;
      
			socket = (Socket)createSocket.invoke(factory, args);
      
			// set up streams
			InputStream in = socket.getInputStream();
			in = new BufferedInputStream(in);
			in = new CRLFInputStream(in);
			this.in = new LineInputStream(in);
			OutputStream out = socket.getOutputStream();
			out = new BufferedOutputStream(out);
			this.out = new CRLFOutputStream(out);
			
      return true;
    }
    catch (Exception e)
    {
      return false;
    }
  }
  
	/**
	 * Returns the number of messages in the maildrop.
	 */
	public int stat()
		throws IOException
	{
		send(STAT);
		if (!getResponse())
			throw new ProtocolException("STAT failed: "+response);
		try
		{
			return Integer.parseInt(response.substring(0, response.indexOf(' ')));
		}
		catch (NumberFormatException e)
		{
			throw new ProtocolException("Not a number: "+response);
		}
		catch (ArrayIndexOutOfBoundsException e)
		{
			throw new ProtocolException("Not a STAT response: "+response);
		}
	}

	/**
	 * Returns the size of the specified message.
	 * @param msgnum the message number
	 */
	public int list(int msgnum)
		throws IOException
	{
		String cmd = new StringBuffer(LIST)
			.append(' ')
			.append(msgnum)
			.toString();
		send(cmd);
		if (!getResponse())
			throw new ProtocolException("LIST failed: "+response);
		try
		{
			return Integer.parseInt(response.substring(response.indexOf(' ')+1));
		}
		catch (NumberFormatException e)
		{
			throw new ProtocolException("Not a number: "+response);
		}
	}

	/**
	 * Returns an input stream containing the entire message.
	 * This input stream must be read in its entirety before further commands
	 * can be issued on this connection.
	 * @param msgnum the message number
	 */
	public InputStream retr(int msgnum)
		throws IOException
	{
		String cmd = new StringBuffer(RETR)
			.append(' ')
			.append(msgnum)
			.toString();
		send(cmd);
		if (!getResponse())
			throw new ProtocolException("RETR failed: "+response);
		return new MessageInputStream(in);
	}

	/**
	 * Marks the specified message as deleted.
	 * @param msgnum the message number
	 */
	public void dele(int msgnum)
		throws IOException
	{
		String cmd = new StringBuffer(DELE)
			.append(' ')
			.append(msgnum)
			.toString();
		send(cmd);
		if (!getResponse())
			throw new ProtocolException("DELE failed: "+response);
	}

	/**
	 * Does nothing.
	 * This can be used to keep the connection alive.
	 */
	public void noop()
		throws IOException
	{
		send(NOOP);
		if (!getResponse())
			throw new ProtocolException("NOOP failed: "+response);
	}

	/**
	 * If any messages have been marked as deleted, they are unmarked.
	 */
	public void rset()
		throws IOException
	{
		send(RSET);
		if (!getResponse())
			throw new ProtocolException("RSET failed: "+response);
	}

  /**
   * Closes the connection.
	 * No further commands may be issued on this connection after this method
	 * has been called.
	 * @return true if all deleted messages were successfully removed, false
	 * otherwise
   */
  public boolean quit()
    throws IOException
  {
		send(QUIT);
		boolean ret = getResponse();
		socket.close();
		return ret;
  }

	/**
	 * Returns just the headers of the specified message as an input stream.
	 * The stream must be read in its entirety before further commands can be
	 * issued.
	 * @param msgnum the message number
	 */
	public InputStream top(int msgnum)
		throws IOException
	{
		String cmd = new StringBuffer(TOP)
			.append(' ')
			.append(msgnum)
			.append(' ')
			.append(0)
			.toString();
		send(cmd);
		if (!getResponse())
			throw new ProtocolException("TOP failed: "+response);
		return new MessageInputStream(in);
	}

	/**
	 * Returns a unique identifier for the specified message.
	 * @param msgnum the message number
	 */
	public String uidl(int msgnum)
		throws IOException
	{
		String cmd = new StringBuffer(UIDL)
			.append(' ')
			.append(msgnum)
			.toString();
		send(cmd);
		if (!getResponse())
			throw new ProtocolException("UIDL failed: "+response);
		return response.substring(response.indexOf(' ')+1);
	}

  /**
   * Returns a list of capabilities supported by the POP3 server.
   * If the server does not support POP3 extensions, returns
   * <code>null</code>.
   */
  public List capa()
    throws IOException
  {
    send(CAPA);
    if (getResponse())
    {
      final String DOT = ".";
      List list = new ArrayList();
      for (String line = in.readLine();
          !DOT.equals(line);
          line = in.readLine())
        list.add(line);
      return list;
    }
    return null;
  }

  /** 
   * Send the command to the server.
   * If <code>debug</code> is <code>true</code>,
	 * the command is logged.
   */
  protected void send(String command)
    throws IOException
  {
    if (debug)
		{
			Logger logger = Logger.getInstance();
      logger.log("pop3", "> "+command);
		}
    out.write(command);
    out.writeln();
    out.flush();
  }

  /**
   * Parse the response from the server.
   * If <code>debug</code> is <code>true</code>,
	 * the response is logged.
   */
  protected boolean getResponse()
    throws IOException
  {
    response = in.readLine();
    if (debug)
		{
			Logger logger = Logger.getInstance();
      logger.log("pop3", "< "+response);
		}
    if (response.indexOf(OK)==0)
    {
      response = response.substring(3).trim();
      return true;
    }
    else if (response.indexOf(ERR)==0)
		{
      response = response.substring(4).trim();
			return false;
		}
		else
		{
			throw new ProtocolException("Unexpected response: "+response);
		}
  }

	/*
	 * Parse the APOP timestamp from the server's banner greeting.
	 */
	byte[] parseTimestamp(String greeting)
    throws IOException
	{
		int bra = greeting.indexOf('<');
		if (bra!=-1)
		{
			int ket = greeting.indexOf('>', bra);
			if (ket!=-1)
			{
				String mid = greeting.substring(bra, ket+1);
				int at = mid.indexOf('@');
				if (at!=-1) // This is a valid RFC822 msg-id
					return mid.getBytes("US-ASCII");
			}
		}
		return null;
	}

}
