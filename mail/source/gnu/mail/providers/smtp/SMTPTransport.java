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

  //private static final String[] ignoreList = null;
  private static final byte[] CRLF = {13, 10};

  private MimeMessage message;
  private Address[] addresses;
  private Address[] validSentAddr;
  private Address[] validUnsentAddr;
  private Address[] invalidAddr;
  private Hashtable extMap;
  private boolean noAuth;
  private String  name;
  private BufferedReader serverInput;
  //private BufferedReader lineInputStream;
  private SMTPOutputStream serverOutput;
  private String lastServerResponse;
  private Socket socket;
  private static String  localHostName = null;


  //-------------------------------------------------------------
  // Initialization ---------------------------------------------
  //-------------------------------------------------------------

  public SMTPTransport(Session session, URLName urlName) 
  {
    super(session, urlName);
    // TODO
  }


  //-------------------------------------------------------------
  // Methods ----------------------------------------------------
  //-------------------------------------------------------------

  //    protected void finalize() throws Throwable 
  //    {
  //      close();
  //    }

  public synchronized void close() throws MessagingException 
  {
    // TODO

    // Check if connection is Open
    if (isConnected()) 
      {
	closeConnection();
      } 

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

  private void checkConnected() 
  {
    // TODO
  }

  private void closeConnection() throws MessagingException 
  {

    // QUIT Command
    simpleCommand("QUIT");

    try 
      {

	// Close Streams
	serverOutput.close();
	//lineInputStream.close();
	serverInput.close();
	socket.close();

      } catch (IOException e) 
	{
	}

  }

  private OutputStream data() throws MessagingException 
  {

    // Variables
    int response;

    // Command
    response = simpleCommand("DATA");

    // Return Output Stream
    return new SMTPOutputStream(serverOutput);

  }

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

  }

  /**
   * Send end-of-data tag to SMTP server.
   * @exception MessagingException Problem has occurred
   */
  private void finishData() throws MessagingException 
  {

    // Variables
    int response;
    //byte[] dataEnd = {13, 10, 46, 13, 10};
    String dataEnd = "\r\n.\r\n";

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
      } catch (UnknownHostException e) 
	{
	}

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

  }

  public synchronized boolean isConnected() 
  {
    return super.isConnected();
  }

  private boolean isNotLastLine(String value) 
  {

    // Check for space after code
    if (value.charAt(3) == ' ') 
      {
	return false;
      } 

    return true;
  }

  private void issueCommand(String value1, int value2) throws MessagingException 
  {
    // TODO
  }

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
	    try 
	      {
		fromAddress = InetAddress.getLocalHost().getHostAddress();
	      } catch (UnknownHostException uhe)
		{
		  uhe.printStackTrace();
		}
	  } else 
	    {
	      fromAddress = from[0].toString();
	    }
      }
    mailFrom = "MAIL FROM: <" + fromAddress + ">";

    // Command
    response = simpleCommand(mailFrom);

    // Check Response
    if (response != 250) 
      {
	throw new MessagingException("Invalid MAIL FROM");
      }

  }


  private String normalizeAddress(String address) 
  {
    return null; // TODO
  }

  private void openServer(String host, int port) throws MessagingException 
  {

    // Variables
    String timeout;

    // Create Server Socket
    try
      {
	socket = new Socket(host, port);
      } catch (IOException ioe)
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
	  } catch (SocketException se)
	    {
	      se.printStackTrace();
	    }
      }

    try 
      {
	serverInput = new BufferedReader(new InputStreamReader(socket.getInputStream()));
	serverOutput = new SMTPOutputStream(socket.getOutputStream());
	//lineInputStream = new BufferedReader(new InputStreamReader(socket.getInputStream()));
      } catch (IOException ioe)
	{
	  ioe.printStackTrace();
	}

  }


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
	openServer(host, connectPort);
      
	// Currently doesn't support user authentication
      
      } catch (Exception e) 
	{
	  e.printStackTrace();
	  return false;
	} 
    
    // Return Valid Connection
    return true;
    
  }

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

      }
      
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
      } catch (IOException ioe)
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
   * Send command to SMTP server.
   * @param command Command to send
   * @exception MessagingException Problem has occurred
   */
  private void sendCommand(String command) throws MessagingException 
  {
    try 
      {
	// Write Command
	serverOutput.write(command.getBytes());
	serverOutput.write(CRLF);
      
      } catch (IOException e) 
	{
	  throw new MessagingException("Problem writing to server");
	}
    
    // Check Debug
    if (session.getDebug()) 
      {
	System.out.println("DEBUG SMTP SENT: " + command);
      }
      
  }


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
    ehlo = session.getProperty("mail.smtp.ehlo");
    if (ehlo != null && ehlo.toUpperCase().equals("FALSE")) 
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
    try
      {
	message.writeTo(data());
      } catch (IOException ioe)
	{
	  ioe.printStackTrace();
	}
    finishData();

    // Close

  }

  /**
   * Write a command to the SMTP server and parse the response.
   * @param command Command to send
   * @returns Return code from SMTP server
   * @exception MessagingException Problem has occurred
   */
  private int simpleCommand(String command) throws MessagingException 
  {

    // Send Command
    sendCommand(command);

    // Return Response
    return readServerResponse();

  }

  /**
   * Write a command to the SMTP server and parse the response.
   * @param command Command to send
   * @returns Return code from SMTP server
   * @exception MessagingException Problem has occurred
   */
//    private int simpleCommand(byte[] command) throws MessagingException 
//    {

//      // Send Command
//      sendCommand(command);

//      // Return Response
//      return readServerResponse();

//    }

  private boolean supportsAuthentication(String value) 
  {
    return false; // TODO
  }

  private boolean supportsExtension(String value) 
  {
    return false; // TODO
  }


}
