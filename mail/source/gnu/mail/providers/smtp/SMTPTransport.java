/*
  GNU-Classpath Extensions: GNU Javamail - SMTP Service Provider
  Copyright (C) 2001 Benjamin A. Speakmon

  For more information on the classpathx please mail: classpathx-discuss@gnu.org

  This program is free software; you can redistribute it and/or
  modify it under the terms of the GNU Lesser General Public License
  as published by the Free Software Foundation; either version 2
  of the License, or (at your option) any later version.

  This program is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  GNU General Public License for more details.

  You should have received a copy of the GNU Lesser General Public License
  along with this program; if not, write to the Free Software
  Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
*/

package gnu.mail.providers.smtp;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.InputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.SendFailedException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.URLName;
import javax.mail.event.TransportEvent;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import gnu.mail.util.CRLFInputStream;
import gnu.mail.util.CRLFOutputStream;

/** 
 * This transport handles communications with an SMTP server.
 *
 * <P><B>Important System Properties</B><BR>
 * 
 * <UL>
 *   <LI><code>mail.smtp.port</code> - the port used to connect
 *       to the SMTP server.
 *   <LI><code>mail.smtp.timeout</code> - milliseconds
 *       to wait for a reply from the server before timing out.
 *   <LI><code>mail.smtp.ehlo</code> - if true, will attempt
 *       to connect to SMTP server with "EHLO" first.
 *   <LI><code>mail.smtp.host</code> - the SMTP server to connect
 *       with.
 *   <LI><code>mail.smtp.user</code> - the user name to connect as
 *       (not yet implemented)
 *   <LI><code>mail.smtp.from</code> - the RFC 2822 address to use 
 *       as the message sender
 *   <LI><code>mail.smtp.localhost</code> - the hostname to use
 *       when identifying the local host to the SMTP server
 * </UL>
 *
 *
 * @author      Andrew Selkirk
 * @author      Ben Speakmon
 * @author <a href='mailto:dog@gnu.org'>Chris Burdess</a>
 * @version     2.0
 */
public class SMTPTransport
  extends Transport
{

  public static final int SMTP_PORT = 25;
  
  public static final int READY = 220;
  public static final int OK = 250;
  public static final int OK_NOT_LOCAL = 251;
  public static final int OK_UNVERIFIED = 252;
  public static final int SEND_DATA = 354;
  
  private static final byte[] CRLF = {13, 10};

  private Socket socket = null;
  private CRLFInputStream in = null;
  private CRLFOutputStream out = null;
  
  private String localHostName;

  private List authenticationMechanisms = null;

  /**
   * Creates a new <code>SMTPTransport</code> instance.
   *
   * @param session a <code>Session</code> value
   * @param urlName an <code>URLName</code> value
   */
  public SMTPTransport(Session session, URLName urlName) 
  {
    super(session, urlName);
   
    // Check for mail.smtp.localhost property
    localHostName = session.getProperty("mail.smtp.localhost");
    if (localHostName==null)
    {
      try
      {
        localHostName = InetAddress.getLocalHost().getHostName();
      }
      catch (UnknownHostException e)
      {
        localHostName = "localhost";
      }
    }
  }

  void log(String message)
  {
    System.err.println("smtp: "+message);
  }

  /**
   * Describe <code>protocolConnect</code> method here.
   *
   * @param host the SMTP server to connect to
   * @param port the port to attempt connection on
   * @param user the user to connect as 
   * @param password the password to use for authenticating user
   * @return true if the connection attempt succeeds.
   * @exception MessagingException if an error occurs
   */
  protected boolean protocolConnect(String host, int port, String user,
                                    String password)
    throws MessagingException 
  {

    if (host==null)
      host = session.getProperty("mail.smtp.host");
    if (port==-1)
    {
      String smtpPort = session.getProperty("mail.smtp.port");
      if (smtpPort!=null)
      {
        try
        {
          port = Integer.parseInt(smtpPort);
        }
        catch (NumberFormatException e)
        {
          throw new MessagingException("Invalid mail.smtp.port value: "+
              smtpPort);
        }
      }
      else
          port = SMTP_PORT;
    }
    if (user==null)
      user = session.getProperty("mail.smtp.user");
    
    // Check host
    if (host==null) 
      throw new MessagingException("No SMTP host has been set "+
          "(mail.smtp.host)");
    
    try 
    {	
      // Create Server Socket
      socket = new Socket(host, port);
    
      // Check for "mail.smtp.timeout" option
      String smtpTimeout = session.getProperty("mail.smtp.timeout");
      if (smtpTimeout!=null) 
      {
        try
        {
          socket.setSoTimeout(Integer.parseInt(smtpTimeout));
        } 
        catch (SocketException se)
        {
          log("WARNING: unable to set socket timeout property");
        }
        catch (NumberFormatException e)
        {
          throw new MessagingException("Invalid mail.smtp.timeout value: "+
              smtpTimeout);
        }
      }
    
      in = new CRLFInputStream(
          new BufferedInputStream(
            socket.getInputStream()));
      out = new CRLFOutputStream(
          new BufferedOutputStream(
            socket.getOutputStream()));
      
      // Greeting
      Response greeting = readResponse();
      if (greeting.code!=READY)
        throw new MessagingException("Server not ready");
      
      // EHLO/HELO
      String smtpEhlo = session.getProperty("mail.smtp.ehlo");
      if (smtpEhlo==null || new Boolean(smtpEhlo).booleanValue())
        helo(localHostName);
      else if (!ehlo(localHostName)) 
        helo(localHostName);
      
      // User authentication
      if (authenticationMechanisms!=null &&
          !authenticationMechanisms.isEmpty())
      {
        if (user!=null && password!=null)
        {
          // TODO more authentication mechanisms eg CRAM-MD5
          if (authenticationMechanisms.contains("LOGIN"))
            return login(user, password);
        }
        return false;
      }
    } 
    catch (IOException e)
    {
      if (session.getDebug())
        log(e.getMessage());
      return false;
    }
    return true;
  }
  
  /**
   * Sends the SMTP message to the server.
   *
   * @param message the Message to send
   * @param addresses the Addresses to send the Message to 
   * @exception MessagingException if an error occurs
   * @exception SendFailedException if an error occurs
   */
  public void sendMessage(Message message,
      Address[] addresses)
    throws MessagingException, SendFailedException 
  {
    if (!(message instanceof MimeMessage))
      throw new SendFailedException("only MimeMessages are supported");
    // Cast message
    MimeMessage mimeMessage = (MimeMessage)message;
      
    int len = addresses.length;
    List sent = new ArrayList(len);
    List unsent = new ArrayList(len);
    List invalid = new ArrayList(len);
    int deliveryStatus = 0;
    
    synchronized (this)
    {
      if (!isConnected())
        throw new MessagingException("not connected");
      
      // MAIL FROM
      mailFrom(mimeMessage);
      
      // RCPT TO
      rcptTo(addresses, sent, invalid);
      
      // DATA
      try
      {
        if (simpleCommand("DATA")!=SEND_DATA)
          throw new MessagingException("Error sending data");
        SMTPOutputStream sout = new SMTPOutputStream(out);
        mimeMessage.writeTo(sout);
        sout.flush();
      }
      catch (IOException e)
      {
        throw new SendFailedException(e.getMessage());
      }
      deliveryStatus = finishData(sent, unsent, invalid);
    }
  
    // Notify transport listeners
    Address[] a_sent = new Address[sent.size()];
    sent.toArray(a_sent);
    Address[] a_unsent = new Address[unsent.size()];
    unsent.toArray(a_unsent);
    Address[] a_invalid = new Address[invalid.size()];
    invalid.toArray(a_invalid);

    notifyTransportListeners(deliveryStatus, a_sent, a_unsent, a_invalid,
        mimeMessage);
  }

  /**
   * Close our streams and sockets. This will be called by
   * the superclass after our sendMessage() returns.
   *
   * @exception MessagingException if an error occurs
   */
  public synchronized void close()
    throws MessagingException 
  {
    // Check if connection is Open
    if (isConnected()) 
    {
      try 
      {
        simpleCommand("QUIT");
        
        // Close streams
        out.close();
        in.close();
        socket.close();
        out = null;
        in = null;
        socket = null;
      } 
      catch (IOException e) 
      {
      }
    } 
    super.close();
  }

  /**
   * Checks lines of input from the server and returns true
   * if the server has finished sending this reply to us.
   * This may happen in response to EHLO or HELP commands.
   *
   * @param value a line of input from the SMTP server
   * @return true if the server is waiting for us now.
   */
  private boolean isNotLastLine(String value) 
  {
    // Check for space after code
    return (value.charAt(3)!=' ');
  }

  /**
   * Send a ELHO command to the SMTP server.  Since it is possible
   * that the SMTP server doesn't support extensions, false would be
   * returned in such a scenario.  Otherwise, if there is a problem,
   * an exception should be thrown.
   *
   * @param domain EHLO domain for handshaking
   * @returns true if SMTP extensions are supported, false otherwise
   * @exception MessagingException Problem has occurred
   */
  private boolean ehlo(String domain)
    throws MessagingException 
  {
    String command = new StringBuffer("EHLO ")
      .append(domain)
      .toString();
    sendCommand(command);
    while (true)
    {
      Response response = readResponse();
      if (response.text.startsWith("AUTH "))
      {
        authenticationMechanisms = new ArrayList();
        StringTokenizer st = new StringTokenizer(response.text.substring(5));
        while (st.hasMoreTokens())
          authenticationMechanisms.add(st.nextToken());
      }
      if (response.last)
        return (response.code==OK);
    }
  }

  /**
   * Send a HELO command to the SMTP server.  If there is a problem,
   * an exception should be thrown.
   * @param domain HELO domain for handshaking
   * @exception MessagingException Problem has occurred
   */
  private void helo(String domain) throws MessagingException 
  {
    String command = new StringBuffer("HELO ")
      .append(domain)
      .toString();
    simpleCommand(command);
  }

  /**
   * Attempts a username/password login.
   */
  private boolean login(String user, String password)
    throws MessagingException
  {
    String command = new StringBuffer("AUTH LOGIN ")
      .append(user)
      .append(' ')
      .append(password)
      .toString();
    switch (simpleCommand(command))
    {
      case 235:
        return true;
      default:
        return false;
    }
  }

  /**
   * Offers our mail sender(s) to the SMTP server.
   *
   * @exception SendFailedException if no valid message sender is found.
   * @exception MessagingException for any other error cause.
   */
  private void mailFrom(MimeMessage message)
    throws SendFailedException, MessagingException 
  {
    Address from = null;
    String smtpFrom = session.getProperty("mail.smtp.from");
    if (smtpFrom!=null)
      from = new InternetAddress(smtpFrom);
    if (from==null)
    {
      Address[] f = message.getFrom(); // returns Sender if From absent
      if (f!=null && f.length>0)
        from = f[0];
    }
    if (from==null)
      throw new SendFailedException("No valid message sender specified");
    
    String command = new StringBuffer("MAIL FROM:<")
      .append(from.toString())
      .append(">")
      .toString();
    switch (simpleCommand(command))
    {
      case OK:
      case OK_NOT_LOCAL:
      case OK_UNVERIFIED:
        break;
      default:
        throw new SendFailedException("Sender "+from+" rejected");
    }
  }

  /**
   * Send list of mail recipients to SMTP server.  Keeps track
   * of which messages are valid/invalid.
   *
   * @exception SendFailedException Problem has occurred
   */
  private void rcptTo(Address[] addresses, List sent, List invalid)
    throws SendFailedException, MessagingException 
  {
    for (int i=0; i<addresses.length; i++) 
    {
      String command = new StringBuffer("RCPT TO:<")
        .append(addresses[i].toString())
        .append(">")
        .toString();
      switch (simpleCommand(command))
      {
        case OK:
        case OK_NOT_LOCAL:
        case OK_UNVERIFIED:
          sent.add(0, addresses[i]);
          break;
        default:
          invalid.add(0, addresses[i]);
      }
    }
    if (sent.isEmpty())
      throw new SendFailedException("No valid mail recipients specified");	
  }

  /**
   * Send end-of-data tag to SMTP server.
   * @exception MessagingException Problem has occurred
   */
  private int finishData(List sent, List unsent, List invalid)
    throws MessagingException 
  {
    String dataEnd = "\r\n.\r\n";
    if (simpleCommand(dataEnd)!=OK)
    {
      unsent.addAll(sent);
      sent.clear();
      return TransportEvent.MESSAGE_NOT_DELIVERED;
    }
    else 
    {
      return invalid.isEmpty() ?
        TransportEvent.MESSAGE_DELIVERED :
        TransportEvent.MESSAGE_PARTIALLY_DELIVERED;
    }
  }
  
  private Response readResponse()
    throws MessagingException
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
      return new Response(line);
    } 
    catch (IOException e)
    {
      throw new MessagingException(e.getMessage(), e);
    }
    catch (NumberFormatException e)
    {
      throw new MessagingException("Unexpected response: "+line);
    }
    finally
    {
      if (session.getDebug())
        log("< " + line);
    }
  }

  /**
   * Write a command to the SMTP server and parse the response.
   * @param command Command to send
   * @returns Return code from SMTP server
   * @exception MessagingException Problem has occurred
   */
  private int simpleCommand(String command)
    throws MessagingException 
  {
    sendCommand(command);
    Response response = readResponse();
    while (!response.last)
      response = readResponse();
    return response.code;
  }

  /**
   * Send command to SMTP server.
   * @param command Command to send
   * @exception MessagingException Problem has occurred
   */
  private void sendCommand(String command)
    throws MessagingException 
  {
    try 
    {
      out.write(command.getBytes());
      out.write(CRLF);
      out.flush();
    } 
    catch (IOException e) 
    {
      throw new MessagingException(e.getMessage(), e);
    }
    finally
    {
      if (session.getDebug()) 
        log("> " + command);
    }
  }

  /**
   * Models and parses an SMTP server response.
   */
  static class Response
  {
    int code;
    boolean last;
    String text;

    Response(String line)
    {
      code = Integer.parseInt(line.substring(0, 3));
      last = (line.charAt(3)!='-');
      text = line.substring(4);
    }

    boolean success()
    {
      return (code>=200 && code<300);
    }
  }

}
