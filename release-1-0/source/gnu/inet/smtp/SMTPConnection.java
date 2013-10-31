/*
 * $Id: SMTPConnection.java,v 1.9 2004-06-08 19:05:28 dog Exp $
 * Copyright (C) 2003 Chris Burdess <dog@gnu.org>
 * 
 * This file is part of GNU inetlib, a library.
 * 
 * GNU inetlib is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 * GNU inetlib is distributed in the hope that it will be useful,
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

package gnu.inet.smtp;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.InputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.ProtocolException;
import java.net.Socket;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;

import javax.security.auth.callback.CallbackHandler;
import javax.security.sasl.Sasl;
import javax.security.sasl.SaslClient;
import javax.security.sasl.SaslException;

import gnu.inet.util.BASE64;
import gnu.inet.util.CRLFInputStream;
import gnu.inet.util.CRLFOutputStream;
import gnu.inet.util.EmptyX509TrustManager;
import gnu.inet.util.LineInputStream;
import gnu.inet.util.Logger;
import gnu.inet.util.MessageOutputStream;
import gnu.inet.util.SaslCallbackHandler;
import gnu.inet.util.SaslInputStream;
import gnu.inet.util.SaslOutputStream;

/**
 * An SMTP client.
 * This implements RFC 2821.
 *
 * @author <a href="mailto:dog@gnu.org">Chris Burdess</a>
 * @version $Revision: 1.9 $ $Date: 2004-06-08 19:05:28 $
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
  protected static final String NOOP = "NOOP";
  protected static final String QUIT = "QUIT";
  protected static final String HELO = "HELO";
  protected static final String EHLO = "EHLO";
  protected static final String AUTH = "AUTH";
  protected static final String STARTTLS = "STARTTLS";

  protected static final int INFO = 214;
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
  protected LineInputStream in;

  /**
   * The output stream used to send commands to the server.
   */
  protected CRLFOutputStream out;

  /**
   * If true, log events.
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
   * The greeting message given by the server.
   */
  protected final String greeting;

  /**
   * Creates a new connection to the specified host, using the default SMTP
   * port.
   * @param host the server hostname
   */
  public SMTPConnection (String host) throws IOException
    {
      this (host, DEFAULT_PORT);
    }

  /**
   * Creates a new connection to the specified host, using the specified
   * port.
   * @param host the server hostname
   * @param port the port to connect to
   */
  public SMTPConnection (String host, int port) throws IOException
    {
      this (host, port, -1, -1, false);
    }

  /**
   * Creates a new connection to the specified host, using the specified
   * port.
   * @param host the server hostname
   * @param port the port to connect to
   * @param connectionTimeout the connection timeout in milliseconds
   * @param timeout the I/O timeout in milliseconds
   * @param debug whether to log progress
   */
  public SMTPConnection (String host, int port,
                         int connectionTimeout, int timeout, boolean debug)
    throws IOException
    {
      if (port <= 0)
        {
          port = DEFAULT_PORT;
        }
      response = null;
      continuation = false;
      this.debug = debug;
      
      // Initialise socket
      // TODO connectionTimeout
      socket = new Socket (host, port);
      if (timeout > 0)
        {
          socket.setSoTimeout (timeout);
        }
      
      // Initialise streams
      InputStream in = socket.getInputStream ();
      in = new BufferedInputStream (in);
      in = new CRLFInputStream (in);
      this.in = new LineInputStream (in);
      OutputStream out = socket.getOutputStream ();
      out = new BufferedOutputStream (out);
      this.out = new CRLFOutputStream (out);
      
      // Greeting
      if (getResponse () != READY)
        {
          throw new ProtocolException (response);
        }
      greeting = response;
    }

  /**
   * Returns the server greeting message.
   */
  public String getGreeting ()
    {
      return greeting;
    }

  /**
   * Returns the text of the last response received from the server.
   */
  public String getLastResponse ()
    {
      return response;
    }

  // -- 3.3 Mail transactions --

  /**
   * Execute a MAIL command.
   * @param reversePath the source mailbox (from address)
   * @param parameters optional ESMTP parameters
   * @return true if accepted, false otherwise
   */
  public boolean mailFrom (String reversePath, ParameterList parameters)
    throws IOException
    {
      StringBuffer command = new StringBuffer (MAIL_FROM);
      command.append ('<');
      command.append (reversePath);
      command.append ('>');
      if (parameters != null)
        {
          command.append (SP);
          command.append (parameters);
        }
      send (command.toString ());
      switch (getResponse ())
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
  public boolean rcptTo (String forwardPath, ParameterList parameters)
    throws IOException
    {
      StringBuffer command = new StringBuffer (RCPT_TO);
      command.append ('<');
      command.append (forwardPath);
      command.append ('>');
      if (parameters != null)
        {
          command.append (SP);
          command.append (parameters);
        }
      send (command.toString ());
      switch (getResponse ())
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
  public OutputStream data () throws IOException
    {
      send (DATA);
      switch (getResponse ())
        {
        case SEND_DATA:
          return new MessageOutputStream (out);
        default:
          throw new ProtocolException (response);
        }
    }

  /**
   * Completes the DATA procedure.
   * @see #data
   * @return true id transfer was successful, false otherwise
   */
  public boolean finishData () throws IOException
    {
      send (FINISH_DATA);
      switch (getResponse ())
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
  public void rset () throws IOException
    {
      send (RSET);
      if (getResponse () != OK)
        {
          throw new ProtocolException (response);
        }
    }

  // -- 3.5 Commands for Debugging Addresses --

  /**
   * Returns a list of valid possibilities for the specified address, or
   * null on failure.
   * @param address a mailbox, or real name and mailbox
   */
  public List vrfy (String address) throws IOException
    {
      String command = VRFY + ' ' + address;
      send (command);
      List list = new ArrayList ();
      do
        {
          switch (getResponse ())
            {
            case OK:
            case AMBIGUOUS:
              response = response.trim ();
              if (response.indexOf ('@') != -1)
                {
                  list.add (response);
                }
              else if (response.indexOf ('<') != -1)
                {
                  list.add (response);
                }
              else if (response.indexOf (' ') == -1)
                {
                  list.add (response);
                }
              break;
            default:
              return null;
            }
        }
      while (continuation);
      return Collections.unmodifiableList (list);
    }

  /**
   * Returns a list of valid possibilities for the specified mailing list,
   * or null on failure.
   * @param address a mailing list name
   */
  public List expn (String address) throws IOException
    {
      String command = EXPN + ' ' + address;
      send (command);
      List list = new ArrayList ();
      do
        {
          switch (getResponse ())
            {
            case OK:
              response = response.trim ();
              list.add (response);
              break;
            default:
              return null;
            }
        }
      while (continuation);
      return Collections.unmodifiableList (list);
    }

  /**
   * Returns some useful information about the specified parameter.
   * Typically this is a command.
   * @param arg the context of the query, or null for general information
   * @return a list of possibly useful information, or null if the command
   * failed.
   */
  public List help (String arg) throws IOException
    {
      String command = (arg == null) ? HELP :
        HELP + ' ' + arg;
      send (command);
      List list = new ArrayList ();
      do
        {
          switch (getResponse ())
            {
            case INFO:
              list.add (response);
              break;
            default:
              return null;
            }
        }
      while (continuation);
      return Collections.unmodifiableList (list);
    }

  /**
   * Issues a NOOP command.
   * This does nothing, but can be used to keep the connection alive.
   */
  public void noop () throws IOException
    {
      send (NOOP);
      do
        {
          getResponse ();
        }
      while (continuation);
    }

  /**
   * Close the connection to the server.
   */
  public void quit () throws IOException
    {
      try
        {
          send (QUIT);
          getResponse ();
          /* RFC 2821 states that the server MUST send an OK reply here, but
           * many don't: postfix, for instance, sends 221.
           * In any case we have done our best. */
        }
      catch (IOException e)
        {
        }
      finally
        {
          // Close the socket anyway.
          socket.close ();
        }
    }

  /**
   * Issues a HELO command.
   * @param hostname the local host name
   */
  public boolean helo (String hostname) throws IOException
    {
      String command = HELO + ' ' + hostname;
      send (command);
      return (getResponse () == OK);
    }

  /**
   * Issues an EHLO command.
   * If successful, returns a list of the SMTP extensions supported by the
   * server.
   * Otherwise returns null, and HELO should be called.
   * @param hostname the local host name
   */
  public List ehlo (String hostname) throws IOException
    {
      String command = EHLO + ' ' + hostname;
      send (command);
      List extensions = new ArrayList ();
      do
        {
          switch (getResponse ())
            {
            case OK:
              extensions.add (response);
              break;
            default:
              return null;
            }
        }
      while (continuation);
      return Collections.unmodifiableList (extensions);
    }

  /**
   * Negotiate TLS over the current connection.
   * This depends on many features, such as the JSSE classes being in the
   * classpath. Returns true if successful, false otherwise.
   */
  public boolean starttls () throws IOException
    {
      try
        {
          // Use SSLSocketFactory to negotiate a TLS session and wrap the
          // current socket.
          SSLContext context = SSLContext.getInstance ("TLS");
          // We don't require strong validation of the server certificate
          TrustManager[] trust = new TrustManager[1];
          trust[0] = new EmptyX509TrustManager ();
          context.init (null, trust, null);
          SSLSocketFactory factory = context.getSocketFactory ();

          send (STARTTLS);
          if (getResponse () != OK)
            {
              return false;
            }

          String hostname = socket.getInetAddress ().getHostName ();
          int port = socket.getPort ();
          SSLSocket ss =
            (SSLSocket) factory.createSocket (socket, hostname, port, true);
          String[] protocols = { "TLSv1", "SSLv3" };
          ss.setEnabledProtocols (protocols);
          ss.setUseClientMode (true);
          ss.startHandshake ();

          // Set up streams
          InputStream in = ss.getInputStream ();
          in = new BufferedInputStream (in);
          in = new CRLFInputStream (in);
          this.in = new LineInputStream (in);
          OutputStream out = ss.getOutputStream ();
          out = new BufferedOutputStream (out);
          this.out = new CRLFOutputStream (out);
          return true;
        }
      catch (GeneralSecurityException e)
        {
          return false;
        }
    }

  // -- Authentication --

  /**
   * Authenticates the connection using the specified SASL mechanism,
   * username, and password.
   * @param mechanism a SASL authentication mechanism, e.g. LOGIN, PLAIN,
   * CRAM-MD5, GSSAPI
   * @param username the authentication principal
   * @param password the authentication credentials
   * @return true if authentication was successful, false otherwise
   */
  public boolean authenticate (String mechanism, String username,
                               String password) throws IOException
    {
      try
        {
          String[] m = new String[] { mechanism };
          CallbackHandler ch = new SaslCallbackHandler (username, password);
          // Avoid lengthy callback procedure for GNU Crypto
          Properties p = new Properties ();
          p.put ("gnu.crypto.sasl.username", username);
          p.put ("gnu.crypto.sasl.password", password);
          SaslClient sasl =
            Sasl.createSaslClient (m, null, "smtp",
                                   socket.getInetAddress ().getHostName (),
                                   p, ch);
          if (sasl == null)
            {
              return false;
            }

          StringBuffer cmd = new StringBuffer (AUTH);
          cmd.append (' ');
          cmd.append (mechanism);
          if (sasl.hasInitialResponse ())
            {
              cmd.append (' ');
              byte[] init = sasl.evaluateChallenge (new byte[0]);
              cmd.append (new String (init, "US-ASCII"));
            }
          send (cmd.toString ());
          while (true)
            {
              switch (getResponse ())
                {
                case 334:
                  try
                    {
                      byte[] c0 = response.getBytes ("US-ASCII");
                      byte[] c1 = BASE64.decode (c0);       // challenge
                      byte[] r0 = sasl.evaluateChallenge (c1);
                      byte[] r1 = BASE64.encode (r0);       // response
                      out.write (r1);
                      out.write (0x0d);
                      out.flush ();
                    }
                  catch (SaslException e)
                    {
                      // Error in SASL challenge evaluation - cancel exchange
                      out.write (0x2a);
                      out.write (0x0d);
                      out.flush ();
                    }
                  break;
                case 235:
                  String qop = (String) sasl.getNegotiatedProperty (Sasl.QOP);
                  if ("auth-int".equalsIgnoreCase (qop)
                      || "auth-conf".equalsIgnoreCase (qop))
                    {
                      InputStream in = socket.getInputStream ();
                      in = new BufferedInputStream (in);
                      in = new SaslInputStream (sasl, in);
                      in = new CRLFInputStream (in);
                      this.in = new LineInputStream (in);
                      OutputStream out = socket.getOutputStream ();
                      out = new BufferedOutputStream (out);
                      out = new SaslOutputStream (sasl, out);
                      this.out = new CRLFOutputStream (out);
                    }
                  return true;
                default:
                  return false;
                }
            }
        }
      catch (SaslException e)
        {
          return false;             // No provider for mechanism
        }
      catch (RuntimeException e)
        {
          return false;             // No javax.security.sasl classes
        }
    }

  /**
   * LOGIN authentication mechanism.
   * If this returns true, EHLO should be re-issued.
   *
   public boolean authLogin(String username, String password)
   throws IOException
   {
   send("AUTH LOGIN");
   if (getResponse()==334)
   {
   String US_ASCII = "US-ASCII";
   byte[] bytes = username.getBytes(US_ASCII);
   String encoded = new String(BASE64.encode(bytes), US_ASCII);
   send(encoded);
   if (getResponse()==334)
   {
   bytes = password.getBytes(US_ASCII);
   encoded = new String(BASE64.encode(bytes), US_ASCII);
   send(encoded);
   if (getResponse()==235)
   return true;
   }
   }
   return false;
   }*/

  /**
   * PLAIN authentication mechanism.
   *
   public boolean authPlain(String username, String password)
   throws IOException
   {
   String plain = new StringBuffer(username)
   .append('\u0000')
   .append(username)
   .append('\u0000')
   .append(password)
   .toString();
   String US_ASCII = "US-ASCII";
   byte[] bytes = plain.getBytes(US_ASCII);
   String encoded = new String(BASE64.encode(bytes), US_ASCII);
   String command = new StringBuffer("AUTH PLAIN ")
   .append(encoded)
   .toString();
   send(command);
   return (getResponse()==235);
   }*/

  // -- Utility methods --

  /**
   * Send the specified command string to the server.
   * @param command the command to send
   */
  protected void send (String command) throws IOException
    {
      if (debug)
        {
          Logger logger = Logger.getInstance ();
          logger.log ("smtp", "> " + command);
        }
      out.write (command.getBytes ("US-ASCII"));    // TODO check encoding
      out.write (0x0d);
      out.flush ();
    }

  /**
   * Returns the next response from the server.
   */
  protected int getResponse () throws IOException
    {
      String line = null;
      try
        {
          line = in.readLine ();
          // Handle special case eg 334 where CRLF occurs after code.
          if (line.length () < 4)
            {
              line = line + '\n' + in.readLine();
            }
          if (debug)
            {
              Logger logger = Logger.getInstance ();
              logger.log ("smtp", "< " + line);
            }
          int code = Integer.parseInt (line.substring (0, 3));
          continuation = (line.charAt (3) == '-');
          response = line.substring (4);
          return code;
        }
      catch (NumberFormatException e)
        {
          throw new ProtocolException ("Unexpected response: " + line);
        }
    }

}
