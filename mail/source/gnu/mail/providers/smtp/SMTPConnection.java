/*
 * SMTPConnection.java
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

package gnu.mail.providers.smtp;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.InputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.ProtocolException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import gnu.mail.util.CRLFInputStream;
import gnu.mail.util.CRLFOutputStream;

/**
 * An SMTP client.
 * This implements RFC 2821.
 *
 * @author <a href="mailto:dog@gnu.org">Chris Burdess</a>
 */
public class SMTPConnection
{

  /**
   * The default SMTP port.
   */
  public static final int DEFAULT_PORT = 25;

  protected static final String MAIL_FROM = "MAIL FROM:";
  protected static final String RCPT_TO = "RCPT TO:";
  protected static final String SP = " SP ";
  protected static final String DATA = "DATA";
  protected static final String FINISH_DATA = "\n.";
  protected static final String RSET = "RSET";
  protected static final String VRFY = "VRFY";
  protected static final String EXPN = "EXPN";
  protected static final String HELP = "HELP";
  protected static final String QUIT = "QUIT";

  protected static final int READY = 220;
  protected static final int OK = 250;
  protected static final int OK_NOT_LOCAL = 251;
  protected static final int OK_UNVERIFIED = 252;
  protected static final int SEND_DATA = 354;
  protected static final int AMBIGUOUS = 553;

  /**
   * The underlying socket used for communicating with the server.
   */
  protected Socket socket;

  /**
   * The input stream used to read responses from the server.
   */
  protected CRLFInputStream in;

  /**
   * The output stream used to send commands to the server.
   */
  protected CRLFOutputStream out;

  /**
   * If true, log events to System.err.
   */
  protected boolean debug;

  /**
   * The last response message received from the server.
   */
  protected String response;

  /**
   * If true, there are more responses to read.
   */
  protected boolean continuation;

  /**
   * Creates a new connection to the specified host, using the default SMTP
   * port.
   * @param host the server hostname
   */
  public SMTPConnection(String host)
    throws IOException
  {
    this(host, DEFAULT_PORT);
  }
  
  /**
   * Creates a new connection to the specified host, using the specified
   * port.
   * @param host the server hostname
   * @param port the port to connect to
   */
  public SMTPConnection(String host, int port)
    throws IOException
  {
    this(host, port, false);
  }
  
  /**
   * Creates a new connection to the specified host, using the specified
   * port.
   * @param host the server hostname
   * @param port the port to connect to
   * @param debug whether to log progress
   */
  public SMTPConnection(String host, int port, boolean debug)
    throws IOException
  {
    if (port<=0)
      port = DEFAULT_PORT;
    response = null;
    continuation = false;
    this.debug = debug;
    
    // Initialise socket
    socket = new Socket(host, port);
    // Initialise streams
    InputStream in = socket.getInputStream();
    in = new BufferedInputStream(in);
    this.in = new CRLFInputStream(in);
    OutputStream out = socket.getOutputStream();
    out = new BufferedOutputStream(out);
    this.out = new CRLFOutputStream(out);
    
    // Greeting
    if (getResponse()!=READY)
      throw new ProtocolException(response);
  }

  // -- 3.3 Mail transactions --

  /**
   * Execute a MAIL command.
   * @param reversePath the source mailbox (from address)
   * @param parameters optional ESMTP parameters
   * @return true if accepted, false otherwise
   */
  public boolean mailFrom(String reversePath, ParameterList parameters)
    throws IOException
  {
    StringBuffer command = new StringBuffer(MAIL_FROM);
    command.append('<');
    command.append(reversePath);
    command.append('>');
    if (parameters!=null)
    {
      command.append(SP);
      command.append(parameters);
    }
    send(command.toString());
    switch (getResponse())
    {
      case OK:
      case OK_NOT_LOCAL:
      case OK_UNVERIFIED:
        return true;
      default:
        return false;
    }
  }

  /**
   * Execute a RCPT command.
   * @param forwardPath the forward-path (recipient address)
   * @param parameters optional ESMTP parameters
   * @return true if successful, false otherwise
   */
  public boolean rcptTo(String forwardPath, ParameterList parameters)
    throws IOException
  {
    StringBuffer command = new StringBuffer(RCPT_TO);
    command.append('<');
    command.append(forwardPath);
    command.append('>');
    if (parameters!=null)
    {
      command.append(SP);
      command.append(parameters);
    }
    send(command.toString());
    switch (getResponse())
    {
      case OK:
      case OK_NOT_LOCAL:
      case OK_UNVERIFIED:
        return true;
      default:
        return false;
    }
  }

  /**
   * Requests an output stream to write message data to.
   * When the entire message has been written to the stream, the
   * <code>flush</code> method must be called on the stream. Until then no
   * further methods should be called on the connection.
   * Immediately after this procedure is complete, <code>finishData</code>
   * must be called to complete the transfer and determine its success.
   * @return a stream for writing messages to
   */
  public OutputStream data()
    throws IOException
  {
    send(DATA);
    switch (getResponse())
    {
      case SEND_DATA:
        return new SMTPOutputStream(out);
      default:
        throw new ProtocolException(response);
    }
  }

  /**
   * Completes the DATA procedure.
   * @see #data
   * @return true id transfer was successful, false otherwise
   */
  public boolean finishData()
    throws IOException
  {
    send(FINISH_DATA);
    switch (getResponse())
    {
      case OK:
        return true;
      default:
        return false;
    }
  }

  /**
   * Aborts the current mail transaction.
   */
  public void rset()
    throws IOException
  {
    send(RSET);
    if (getResponse()!=OK)
      throw new ProtocolException(response);
  }

  // -- 3.5 Commands for Debugging Addresses --

  /**
   * Returns a list of valid possibilities for the specified address, or
   * null on failure.
   * @param address a mailbox, or real name and mailbox
   */
  public List vrfy(String address)
    throws IOException
  {
    String command = new StringBuffer(VRFY)
      .append(' ')
      .append(address)
      .toString();
    send(command);
    List list = new ArrayList();
    do
    {
      switch (getResponse())
      {
        case OK:
        case AMBIGUOUS:
          response = response.trim();
          if (response.indexOf('@')!=-1)
            list.add(response);
          else if (response.indexOf('<')!=-1)
            list.add(response);
          else if (response.indexOf(' ')==-1)
            list.add(response);
          break;
        default:
          return null;
      }
    }
    while (continuation);
    return Collections.unmodifiableList(list);
  }

  /**
   * Returns a list of valid possibilities for the specified mailing list,
   * or null on failure.
   * @param address a mailing list name
   */
  public List expn(String address)
    throws IOException
  {
    String command = new StringBuffer(EXPN)
      .append(' ')
      .append(address)
      .toString();
    send(command);
    List list = new ArrayList();
    do
    {
      switch (getResponse())
      {
        case OK:
          response = response.trim();
          list.add(response);
          break;
        default:
          return null;
      }
    }
    while (continuation);
    return Collections.unmodifiableList(list);
  }

  // TODO HELP
  // TODO NOOP

  /**
   * Close the connection to the server.
   */
  public void quit()
    throws IOException
  {
    send(QUIT);
    if (getResponse()!=OK)
      throw new ProtocolException(response);
    socket.close();
  }
  
  // -- Utility methods --

  /**
   * Send the specified command string to the server.
   * @param command the command to send
   */
  protected void send(String command)
    throws IOException
  {
    if (debug)
      System.err.println("smtp: > "+command);
    out.write(command.getBytes("US-ASCII")); // TODO check encoding
    out.write('\n'); // This is automatically converted to CRLF
    out.flush();
  }

  /**
   * Returns the next response from the server.
   */
  protected int getResponse()
    throws IOException
  {
    String line = null;
    try
    {
      line = in.readLine();
      // Handle special case eg 334 where CRLF occurs after code.
      if (line.length()<4)
        line = new StringBuffer(line)
          .append('\n')
          .append(in.readLine())
          .toString();
      if (debug)
        System.err.println("smtp: < "+line);
      int code = Integer.parseInt(line.substring(0, 3));
      continuation = (line.charAt(3)=='-');
      response = line.substring(4);
      return code;
    }
    catch (NumberFormatException e)
    {
      throw new ProtocolException("Unexpected response: "+line);
    }
  }
  
}
