/********************************************************************
 * Copyright (c) Open Java Extensions, Andrew Selkirk  LGPL License *
 ********************************************************************/

package gnu.mail.providers.smtp;

// Imports
import java.net.*;
import java.io.*;
import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;
import gnu.mail.util.*;


/** SMTP Transport.
 * This transport handles communications with an SMTP server.
 *
 * @author	Andrew Selkirk
 * @version	1.0
 */
public class SMTPTransport
extends Transport
{

  //-------------------------------------------------------------
  // Variables --------------------------------------------------
  //-------------------------------------------------------------

  private static final String[] ignoreList = null;
  private static final byte[] CRLF = {13, 10};

  private MimeMessage message;
  private Address[] addresses;
  private Address[] validSentAddr;
  private Address[] validUnsentAddr;
  private Address[] invalidAddr;
  private Hashtable extMap;
  private boolean noAuth;
  private String  name;
  private BufferedInputStream serverInput;
  private LineInputStream lineInputStream;
  private OutputStream serverOutput;
  private String lastServerResponse;
  private Socket serverSocket;
  private static String  localHostName = null;


  //-------------------------------------------------------------
  // Initialization ---------------------------------------------
  //-------------------------------------------------------------

  public SMTPTransport(Session session, URLName urlName) 
  {
    super(session, urlName);
    // TODO
  } // SMTPTransport()


  //-------------------------------------------------------------
  // Methods ----------------------------------------------------
  //-------------------------------------------------------------

  protected void finalize() throws Throwable 
  {
    close();
  } // finalize()

  public synchronized void close() throws MessagingException 
  {
    // TODO

    // Check if connection is Open
    if (isConnected() == true) 
    {
      closeConnection();
    } // if

  } // close()

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
    } // if

    // Connect
    connect(host, user, null);

  } // connect()

  private void checkConnected() 
  {
    // TODO
  } // checkConnected()

  private void closeConnection() throws MessagingException 
  {

    // QUIT Command
    simpleCommand("QUIT");

    try 
    {

      // Close Streams
      serverOutput.close();
      lineInputStream.close();
      serverInput.close();
      serverSocket.close();

    } catch (IOException e) 
    {
    } // try

  } // closeConnection()

  private OutputStream data() throws MessagingException 
  {

    // Variables
    int response;

    // Command
    response = simpleCommand("DATA");

    // Return Output Stream
    return new CRLFOutputStream(serverOutput);

  } // data()

  /**
   * Send a ELHO command to the SMTP server.  Since it is possible
   * that the SMTP server doesn't support extensions, false would be
   * returned in such a scenario.  Otherwise, if there is a problem,
   * an exception should be thrown.
   * @param domain EHLO domain for handshaking
   * @returns true if SMTP extensions are supported, false otherwise
   * @exception MessagingException Problem has occurred
   */
  private boolean ehlo(String domain) throws MessagingException 
  {

    // Variables
    int response;
    String command;

    // Construct Command
    command = "EHLO " + domain;

    // Command
    response = simpleCommand(command);

    // Check For Invalid Response
    if (response != 250) 
    {
      return false;
    }

    // Valid Response
    return true;

  } // ehlo()

  /**
   * Send end-of-data tag to SMTP server.
   * @exception MessagingException Problem has occurred
   */
  private void finishData() throws MessagingException 
  {

    // Variables
    int response;
    byte[] dataEnd = {13, 10, 46, 13, 10};

    // Command
    response = simpleCommand(dataEnd);

  } // finishData()

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
    } // if

    // Check For mail.smtp.localhost property
    localHostName = session.getProperty("mail.smtp.localhost");
    if (localHostName != null) 
    {
      return localHostName;
    } // if

    // Determine Localhost
    try 
    {
      localHostName = InetAddress.getLocalHost().getHostName();
      return localHostName;
    } catch (UnknownHostException e) 
    {
    } // try

    // Return Unknown
    return "";

  } // getLocalHost()

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

  } // helo()

  public synchronized boolean isConnected() 
  {
    return super.isConnected();
  } // isConnected()

  private boolean isNotLastLine(String value) 
  {

    // Check for space after code
    if (value.chatAt(3) == ' ') 
    {
      return false;
    } // if

    return true;
  } // isNotLastLine()

  private void issueCommand(String value1, int value2) throws MessagingException 
  {
    // TODO
  } // issueCommand()

  private void mailFrom() throws MessagingException 
  {

    // Variables
    Address[] from;
    String fromAddress;
    String mailFrom;
    int response;

    // Get Mail From
    fromAddress = session.getProperty("mail.smtp.from");
    if (fromAddress == null) 
    {
      from = message.getFrom();
      if (from == null || from.length == 0) 
      {
	fromAddress = InetAddress.getLocalAddress();
      } else 
      {
	fromAddress = from[0];
      }
    } // if
    mailFrom = "MAIL FROM:<" + fromAddress + ">";

    // Command
    response = simpleCommand(mailFrom);

    // Check Response
    if (response != 250) 
    {
      throw new MessagingException("Invalid MAIL FROM");
    }

  } // mailFrom()


  private String normalizeAddress(String address) 
  {
    return null; // TODO
  } // normalizeAddress()

  private void openServer(String host, int port) throws MessagingException 
  {

    // Variables
    String timeout;

    // Create Server Socket
    serverSocket = new Socket(host, port);

    // Check for "mail.smtp.timeout" option
    timeout = session.getProperty("mail.smtp.timeout");
    if (timeout != null) 
    {
      serverSocket.setSoTimeout(Integer.parseInt(timeout));
    } // if

    serverInput = serverSocket.getInputStream();
    serverOutput = new SMTPOutputStream(serverSocker.getOutputStream());
    lineOutputStream = new LineOutputStream(serverOutput);

  } // openServer()


  protected boolean protocolConnect(String host, int port, String user,
				    String password)
  throws MessagingException 
  {
    // Variables
    String mailPort;
    int connectPort;
    
    try 
    {
      
      // Check Port
      mailPort = session.getProperty("mail.smtp.port");
      if (mailPort != null) 
      {
	connectPort = Integer.parseInt(mailPort);
      } else if (port == -1) 
      {
	connectPort = 25;
      } else 
      {
	connectPort = port;
      }
      
      // Open Server
      openServer(host, port);
      
      // Currently doesn't support user authentication
      
    } catch (Exception e) 
    {
      return false;
    } // try
    
    // Return Valid Connection
    return true;
    
  } // protocolConnect()

  /**
   * Send list of mail recipients to SMTP server.  Keeps track
   * of which messages are valid/invalid
   * @exception MessagingException Problem has occurred
   */
  private void rcptTo() throws MessagingException 
  {
    
    // Variables
    String rcptTo;
    int response;
    int index;
    
    // Process each Address
    for (index = 0; index < addresses.length; index++) 
    {
      
      // Construct RCPT TO
      rcptTo = "RCPT TO:<" + addresses[index] + ">";
      
      // Command
      response = simpleCommand(rcptTo);
      
      // TODO: Depending on response, update addresses lists
      // whether the address if valid/invalid
      
      // Check Response
      // if (response != 250) 
      {
	// throw new MessagingException("Invalid MAIL FROM");
	//
      }

    } // for
      
  }// rcptTo()
    
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
    lastServerResponse = lineInputStream.readLine();
    while (isNotLastLine(lastServerResponse) == true) 
    {
      lastServerResponse = lineInputStream.readLine();
    } // while
    code = lastServerResponse.substring(0, 3);

    // Return Code
    return Integer.parseInt(code);

  } // readServerResponse()

  /**
   * Send command to SMTP server.
   * @param command Command to send
   * @exception MessagingException Problem has occurred
   */
  private void sendCommand(String command) throws MessagingException 
  {
    sendCommand(command.getBytes());
  } // sendCommand()

  /**
   * Send command to SMTP server.
   * @param command Command to send
   * @exception MessagingException Problem has occurred
   */
  private void sendCommand(byte[] command) throws MessagingException 
  {
    
    try 
    {
      
      // Write Command
      serverOutput.write(command);
      
    } catch (IOException e) 
    {
      throw new MessagingException("Problem writing to server");
    } // try
    
    // Check Debug
    if (session.getDebug() == true) 
    {
      System.out.println("DEBUG SMTP SENT: " + String.valueOf(command));
    } // if
      
  } // sendCommand()


  public synchronized void sendMessage(Message message,
				       Address[] addresses)
  throws MessagingException,
  SendFailedException 
  {

    // Variables
    String ehlo;

    // Store Message and addresses
    this.message = (MimeMessage) message;
    this.addresses = addresses;

    // Greeting
    readServerResponse();

    // EHLO/HELO
    ehlo = session.getProperty("mail.smtp.elho");
    if (ehlo != null && elho.toUpperCase().equals("FALSE") == true) 
    {
      helo(getLocalHost());
    } else if (ehlo(getLocalHost()) == false) 
    {
      helo(getLocalHost());
    }

    // Send Header
    mailFrom();
    rcptTo();

    // Send Data
    message.writeTo(data());
    finishData();

    // Close

  } // sendMessage()

  /**
   * Write a command to the SMTP server and parse the response.
   * @param command Command to send
   * @returns Return code from SMTP server
   * @exception MessagingException Problem has occurred
   */
  private int simpleCommand(String command) throws MessagingException 
  {

    // Send Command
    sendCommand(value);

    // Return Response
    return readServerResponse();

  } // simpleCommand()

  /**
   * Write a command to the SMTP server and parse the response.
   * @param command Command to send
   * @returns Return code from SMTP server
   * @exception MessagingException Problem has occurred
   */
  private int simpleCommand(byte[] command) throws MessagingException 
  {

    // Send Command
    sendCommand(command);

    // Return Response
    return readServerResponse();

  } // simpleCommand()

  private boolean supportsAuthentication(String value) 
  {
    return false; // TODO
  } // supportsAuthentication()

  private boolean supportsExtension(String value) 
  {
    return false; // TODO
  } // supportsExtension()


} // SMTPTransport
