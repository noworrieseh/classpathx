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

import java.net.*;
import java.io.*;
import java.util.Hashtable;
import java.util.Vector;
import javax.mail.*;
import javax.mail.event.TransportEvent;
import javax.mail.internet.*;
import gnu.mail.util.*;

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
 * @version     2.0
 */
public class SMTPTransport
  extends Transport
{

  //-------------------------------------------------------------
  // Variables --------------------------------------------------
  //-------------------------------------------------------------

  //private static final String[] ignoreList = null;
  private static final byte[] CRLF = {13, 10};
  private static int deliveryStatus;

  private MimeMessage mimeMessage;
  private Address[] addresses;
  //private InternetAddress[] validSentAddr;
  private Vector validSentAddrList;
  //private InternetAddress[] validUnsentAddr;
  private Vector validUnsentAddrList;
  //private InternetAddress[] invalidAddr;
  private Vector invalidAddrList;
  private Hashtable extMap;
  private boolean noAuth;
  private String name;
  private BufferedReader serverInput;
  private CRLFOutputStream serverOutput;
  private String lastServerResponse;
  private Socket socket;
  private static String localHostName;


  //-------------------------------------------------------------
  // Initialization ---------------------------------------------
  //-------------------------------------------------------------

  /**
   * Creates a new <code>SMTPTransport</code> instance.
   *
   * @param session a <code>Session</code> value
   * @param urlName an <code>URLName</code> value
   */
  public SMTPTransport(Session session, URLName urlName) 
  {
    super(session, urlName);
    validSentAddrList = new Vector(2);
    validUnsentAddrList = new Vector(2);
    invalidAddrList = new Vector();
    // TODO
  }


  //-------------------------------------------------------------
  // Methods ----------------------------------------------------
  //-------------------------------------------------------------

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

    int connectPort;
    
    try 
    {	
      // Check mail.smtp.port
      String mailPort = session.getProperty("mail.smtp.port");
      
      if (mailPort != null) 
      {
        connectPort = Integer.parseInt(mailPort);
      } 
      else if (port == -1) 
      {
        connectPort = 25;
      } 
      else 
      {
        connectPort = port;
      }
      
      // Open Server
      openServer(host, connectPort);
      
      // TODO: Currently doesn't support user authentication
      
    } 
    catch (Exception e) 
    {
      e.printStackTrace();
      return false;
    } 
    
    // Return Valid Connection
    return true;
    
  }
  
  private void openServer(String host, int port) throws MessagingException 
  {
    
    // Variables
    String timeout;
    
    // Create Server Socket
    try
    {
      socket = new Socket(host, port);
    } 
    catch (IOException ioe)
    {
      ioe.printStackTrace();
    }
    
    // Check for "mail.smtp.timeout" option
    timeout = session.getProperty("mail.smtp.timeout");
    if (timeout != null) 
    {
      try
      {
        socket.setSoTimeout(Integer.parseInt(timeout));
      } 
      catch (SocketException se)
      {
        se.printStackTrace();
      }
    }
    
    try 
    {
      InputStream sin = socket.getInputStream();
      OutputStream sout = socket.getOutputStream();
      serverInput = new BufferedReader(new InputStreamReader(sin));
      serverOutput = new CRLFOutputStream(sout);
    } 
    catch (IOException ioe)
    {
      ioe.printStackTrace();
    }
    
  }
  
  /**
   * Sends the SMTP message to the server.
   *
   * @param message the Message to send
   * @param addresses the Addresses to send the Message to 
   * @exception MessagingException if an error occurs
   * @exception SendFailedException if an error occurs
   */
  public synchronized void sendMessage(Message message,
      Address[] addresses)
    throws MessagingException, SendFailedException 
  {
    
    // Variables
    String ehlo;
    
    // Store Message and addresses
    mimeMessage = (MimeMessage) message;
    this.addresses = addresses;
    
    // Greeting
    readServerResponse();
    
    // EHLO/HELO
    ehlo = session.getProperty("mail.smtp.ehlo");
    if (ehlo != null && ehlo.toUpperCase().equals("FALSE")) 
    {
      helo(getLocalHost());
    } 
    else if (ehlo(getLocalHost()) == false) 
    {
      helo(getLocalHost());
    }
    
    // Send Header
    mailFrom();
    rcptTo();
    
    // Send Data
    try
    {
      mimeMessage.writeTo(data());
    } 
    catch (IOException ioe)
    {
      ioe.printStackTrace();
    }
    finishData();
    
    notifyTransportListeners();
    
  }
  
  /**
   * Inform anybody listening to us how the message sending resulted.
   */
  private void notifyTransportListeners() 
  {
    Address[] validSentAddr = new Address[validSentAddrList.size()];
    validSentAddrList.copyInto(validSentAddr);
    Address[] validUnsentAddr = new Address[validUnsentAddrList.size()];
    validUnsentAddrList.copyInto(validUnsentAddr);
    Address[] invalidAddr = new Address[invalidAddrList.size()];
    invalidAddrList.copyInto(invalidAddr);

    notifyTransportListeners(deliveryStatus, validSentAddr, validUnsentAddr,
			     invalidAddr, mimeMessage);
  }

  public synchronized void connect() throws MessagingException 
  {

    // Variables
    String host;
    String user;

    // Check for Properties
    host = session.getProperty("mail.smtp.host");
    user = session.getProperty("mail.smtp.user");

    // Check if Host Set
    if (host == null) 
    {
      throw new MessagingException("No SMTP host has been " +
          "set (mail.smtp.host)");
    } 
    
    // Connect
    connect(host, user, null);
    
  }

//    private void checkConnected() 
//    {
//      // TODO
//    }

  /**
   * Close our streams and sockets. This will be called by
   * the superclass after our sendMessage() returns.
   *
   * @exception MessagingException if an error occurs
   */
  public synchronized void close()
    throws MessagingException 
  {
    // TODO

    // Check if connection is Open
    if (isConnected()) 
    {
      closeConnection();
    } 

    super.close();

  }

  private void closeConnection()
    throws MessagingException 
  {

    simpleCommand("QUIT");

    try 
    {
      // Close Streams
      serverOutput.close();
      serverInput.close();
      socket.close();
      
    } 
    catch (IOException e) 
    {
    }
    
  }

  /**
   * Get localhost domain for the helo/ehlo handshake.  This value
   * can be set using the "mail.smtp.localhost" property if
   * InetAddress.getLocalHost() doesn't provide the correct information.
   * @returns Local host domain.  If unknown return ""
   */
  private String getLocalHost() 
  {

    // LocalHost is calculated as follows:
    // 1)  Check for mail.smtp.localhost property
    // 2)  Generate from InetAddress.getLocalHost()
    // 3)  Otherwise, return empty ""

    // Check for Local Host String
    if (localHostName != null) 
    {
      return localHostName;
    } 
    
    // Check For mail.smtp.localhost property
    localHostName = session.getProperty("mail.smtp.localhost");
    if (localHostName != null) 
    {
      return localHostName;
    } 
    
    // Determine Localhost
    try 
    {
      localHostName = InetAddress.getLocalHost().getHostName();
      return localHostName;
    } 
    catch (UnknownHostException e) 
    {
      // not sure what circumstances can cause this to happen.
    }
    
    // Return Unknown
    return "";

  }

  /**
   * Delegates to superclass method.
   *
   * @return true if connected.
   */
  public synchronized boolean isConnected() 
  {
    return super.isConnected();
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
    if (value.charAt(3) == ' ') 
    {
      return false;
    } 

    return true;
  }

  //  private void issueCommand(String value1, int value2) throws MessagingException 
  //{
    // TODO
  //}

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

    String command = "EHLO " + domain;

    int response = simpleCommand(command);

    if (response != 250) 
    {
      return false;
    }
    
    return true;

  }

  /**
   * Send a HELO command to the SMTP server.  If there is a problem,
   * an exception should be thrown.
   * @param domain HELO domain for handshaking
   * @exception MessagingException Problem has occurred
   */
  private void helo(String domain) throws MessagingException 
  {

    // Variables
    int response;
    String command;

    // Construct Command
    command = "HELO " + domain;

    // Command
    response = simpleCommand(command);

  }

  /**
   * Offers our mail sender(s) to the SMTP server.
   *
   * @exception SendFailedException if no valid message sender is found.
   * @exception MessagingException for any other error cause.
   */
  private void mailFrom()
    throws SendFailedException, MessagingException 
  {

    Address from;
    String addressFromProperty;
    String mailFrom;
    int response;

    addressFromProperty = session.getProperty("mail.smtp.from");
    if (addressFromProperty != null) 
    {
      from = new InternetAddress(addressFromProperty);
    }
    else if ((from = mimeMessage.getFrom()[0]) != null)
    {
      from = mimeMessage.getFrom()[0];
    }
    else
    {
      deliveryStatus = TransportEvent.MESSAGE_NOT_DELIVERED;
      throw new SendFailedException("No valid message sender specified");
    }
    
    mailFrom = "MAIL FROM: <" + from + ">";
    response = simpleCommand(mailFrom);
    
    if (response != 250) 
    {
      deliveryStatus = TransportEvent.MESSAGE_NOT_DELIVERED;
      throw new SendFailedException("Sender " + from + " was rejected: "
          + response);
    }
    
  }

  /**
   * Send list of mail recipients to SMTP server.  Keeps track
   * of which messages are valid/invalid.
   *
   * @exception SendFailedException Problem has occurred
   */
  private void rcptTo()
    throws SendFailedException, MessagingException 
  {
    
    // Process each Address
    for (int i = 0; i < addresses.length; i++) 
    {
      
      String rcptTo = "RCPT TO: <" + addresses[i] + ">";
      int response = simpleCommand(rcptTo);
      
      if (response == 250) 
      {
        validSentAddrList.insertElementAt(addresses[i], 0);
      }
      else
      {
        invalidAddrList.insertElementAt(addresses[i], 0);
      }
    }
    
    if (validSentAddrList.size() == 0)
    {
      deliveryStatus = TransportEvent.MESSAGE_NOT_DELIVERED;
      throw new SendFailedException("No valid mail recipients specified");	
    }
    
  }

  private OutputStream data() throws MessagingException 
  {

    int response = simpleCommand("DATA");

    return new SMTPOutputStream(serverOutput);

  }

  /**
   * Send end-of-data tag to SMTP server.
   * @exception MessagingException Problem has occurred
   */
  private void finishData() throws MessagingException 
  {

    String dataEnd = "\r\n.\r\n";

    int response = simpleCommand(dataEnd);

    /* Process address Lists depending on result of DATA command. */
    if (response != 250)
    {
      /* The following method is not present in JDK1.1
      validUnsentAddrList.addAll(validSentAddrList);
      */
      int size = validSentAddrList.size();
      validUnsentAddrList.ensureCapacity(size);
      for (int i=0; i<size; i++)
        validUnsentAddrList.addElement(validSentAddrList.elementAt(i));
      
      /* The following method is not present in JDK1.1
      validSentAddrList.clear();
      */
      validSentAddrList.removeAllElements();
      
      deliveryStatus = TransportEvent.MESSAGE_NOT_DELIVERED;
    }
    else 
    {
      if (invalidAddrList.size() == 0)
      {
        deliveryStatus = TransportEvent.MESSAGE_DELIVERED;
      }
      else 
      {
        deliveryStatus = TransportEvent.MESSAGE_PARTIALLY_DELIVERED;
      }
    }
    
  }
  
  private String normalizeAddress(String address) 
  {
    return null; // TODO
  }
    
  private int readServerResponse() 
  {

    // TODO: Figure out if this method should be processing all
    // the responses from the SMTP server, or only a single line
    // at a time leaving the looping to the caller.  For now,
    // method processes all responses.

    // Variables
    String code;
    boolean end;

    // Read Each Line of Input From Server
    
    try
    {
      lastServerResponse = serverInput.readLine();
      while (isNotLastLine(lastServerResponse)) 
      {
        lastServerResponse = serverInput.readLine();
      }
    } 
    catch (IOException ioe)
    {
      ioe.printStackTrace();
    }
    
    if (session.getDebug())
    {
      System.out.println("DEBUG SMTP RECV: " + lastServerResponse);
    }
    code = lastServerResponse.substring(0, 3);
    
    // Return Code
    return Integer.parseInt(code);

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

    // Send Command
    sendCommand(command);

    // Return Response
    return readServerResponse();

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
      serverOutput.write(command.getBytes());
      serverOutput.write(CRLF);
    } 
    catch (IOException e) 
    {
      e.printStackTrace();
      throw new MessagingException("Problem writing to server");
    }
    
    if (session.getDebug()) 
    {
      System.out.println("DEBUG SMTP SENT: " + command);
    }
    
  }

//    private boolean supportsAuthentication(String value) 
//    {
//      return false; // TODO
//    }

//    private boolean supportsExtension(String value) 
//    {
//      return false; // TODO
//    }


}
