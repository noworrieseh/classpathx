/*
 * Service.java
 * Copyright (C) 2001 dog <dog@dog.net.uk>
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2 of the License, or (at your option) any later version.
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

package javax.mail;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.Vector;
import javax.mail.event.ConnectionEvent;
import javax.mail.event.ConnectionListener;
import javax.mail.event.MailEvent;

/**
 * An abstract class that contains the functionality common to messaging 
 * services, such as stores and transports.
 * <p>
 * A messaging service is created from a Session and is named using a URLName.
 * A service must be connected before it can be used.
 * Connection events are sent to reflect its connection status.
 */
public abstract class Service
{

  /**
   * The session from which this service was created.
   */
  protected Session session;

  /**
   * The URLName of this service.
   */
  protected URLName url;

  /**
   * Debug flag for this service.
   * Set from the session's debug flag when this service is created.
   */
  protected boolean debug;

  /*
   * @see #isConnected
   */
  private boolean connected;

  private Vector connectionListeners;

  /**
   * Constructor.
   * @param session Session object for this service
   * @param url URLName object to be used for this service
   */
  protected Service(Session session, URLName url)
  {
    this.session = session;
    this.url = url;
    debug = session.getDebug();
  }

  /**
   * A generic connect method that takes no parameters. 
   * Subclasses can implement the appropriate authentication schemes. 
   * Subclasses that need additional information might want to use 
   * some properties or might get it interactively using a popup window.
   * <p>
   * If the connection is successful, an "open" ConnectionEvent is delivered 
   * to any ConnectionListeners on this service.
   * <p>
   * Most clients should just call this method to connect to the service.
   * <p>
   * It is an error to connect to an already connected service.
   * <p>
   * The implementation provided here simply calls the following 
   * <code>connect(String, String, String)</code> method with nulls.
   * @exception AuthenticationFailedException for authentication failures
   * @exception MessagingException for other failures
   * @exception IllegalStateException if the service is already connected
   */
  public void connect()
    throws MessagingException
  {
    connect(null, null, null);
  }

  /**
   * Connect to the specified address. 
   * This method provides a simple authentication scheme 
   * that requires a username and password.
   * <p>
   * If the connection is successful, an "open" ConnectionEvent is delivered 
   * to any ConnectionListeners on this service.
   * <p>
   * It is an error to connect to an already connected service.
   * <p>
   * The implementation in the Service class will collect defaults for the 
   * host, user, and password from the session, from the URLName for this 
   * service, and from the supplied parameters and then call the 
   * <code>protocolConnect</code> method. If the <code>protocolConnect</code>
   * method returns false, the user will be prompted for any missing 
   * information and the <code>protocolConnect</code> method will be called 
   * again. The subclass should override the <code>protocolConnect</code>
   * method. The subclass should also implement the <code>getURLName</code>
   * method, or use the implementation in this class.
   * <p>
   * On a successful connection, the <code>setURLName</code> method is called 
   * with a URLName that includes the information used to make the connection,
   * including the password.
   * <p>
   * If the password passed in is null and this is the first successful
   * connection to this service, the user name and the password collected from
   * the user will be saved as defaults for subsequent connection attempts to
   * this same service when using other Service object instances (the 
   * connection information is typically always saved within a particular 
   * Service object instance). The password is saved using the Session method
   * <code>setPasswordAuthenticaiton</code>. If the password passed in is not 
   * null, it is not saved, on the assumption that the application is managing 
   * passwords explicitly.
   * @param host the host to connect to
   * @param user the user name
   * @param password this user's password
   * @exception AuthenticationFailedException for authentication failures
   * @exception MessagingException for other failures
   * @exception IllegalStateException if the service is already connected
   */
  public void connect(String host, String user, String password)
    throws MessagingException
  {
    connect(host, -1, user, password);
  }

  /**
   * Similar to connect(host, user, password) except a specific port can be
   * specified.
   * @param host the host to connect to
   * @param port the port to use (-1 means use default port)
   * @param user the user name
   * @param password this user's password
   * @exception AuthenticationFailedException for authentication failures
   * @exception MessagingException for other failures
   * @exception IllegalStateException if the service is already connected
   */
  public void connect(String host, int port, String user, String password)
    throws MessagingException
  {
    if (isConnected())
      throw new MessagingException("already connected");
    
    boolean connected = false;
    boolean authenticated = false;
    String protocol = null;
    String file = null;
    if (url!=null)
    {
      protocol = url.getProtocol();
      if (host==null)
        host = url.getHost();
      if (port==-1)
        port = url.getPort();
      if (user==null)
      {
        user = url.getUsername();
        if (password==null)
          password = url.getPassword();
      }
      else if (password==null && user.equals(url.getUsername()))
        password = url.getPassword();
      file = url.getFile();
    }
    if (protocol!=null)
    {
      if (host==null)
        host = session.getProperty("mail." + protocol + ".host");
      if (user==null)
        user = session.getProperty("mail." + protocol + ".user");
    }
    if (host==null)
      host = session.getProperty("mail.host");
    if (user==null)
      user = session.getProperty("mail.user");
    if (user==null)
    {
      try
      {
        user = System.getProperty("user.name");
      }
      catch (SecurityException e)
      {
        if (debug)
          e.printStackTrace();
      }
    }
    if (password==null && url!=null)
    {
      setURLName(new URLName(protocol, host, port, file, user, password));
      PasswordAuthentication auth = 
        session.getPasswordAuthentication(getURLName());
      if (auth!=null)
      {
        if (user==null)
        {
          user = auth.getUserName();
          password = auth.getPassword();
        }
        else if (user.equals(auth.getUserName()))
          password = auth.getPassword();
      }
      else
        authenticated = true;
    }
    AuthenticationFailedException afex = null;
    try
    {
      connected = protocolConnect(host, port, user, password);
    }
    catch (AuthenticationFailedException afex2)
    {
      afex = afex2;
    }
    if (!connected)
    {
      InetAddress address = null;
      try
      {
        address = InetAddress.getByName(host);
      }
      catch (UnknownHostException e)
      {
      }
      PasswordAuthentication auth = 
        session.requestPasswordAuthentication(address, port, protocol,
            null, user);
      if (auth!=null)
      {
        user = auth.getUserName();
        password = auth.getPassword();
        connected = protocolConnect(host, port, user, password);
      }
    }
    if (!connected)
    {
      if (afex!=null)
        throw afex;
      else
        throw new AuthenticationFailedException();
    }
    setURLName(new URLName(protocol, host, port, file, user, password));
    if (authenticated)
      session.setPasswordAuthentication(getURLName(), 
          new PasswordAuthentication(user, password));
    setConnected(true);
    notifyConnectionListeners(ConnectionEvent.OPENED);
  }

  /**
   * The service implementation should override this method to perform the
   * actual protocol-specific connection attempt. 
   * The default implementation of the <code>connect</code> method calls 
   * this method as needed.
   * <p>
   * The <code>protocolConnect</code> method should return false if a user 
   * name or password is required for authentication but the corresponding 
   * parameter is null; the connect method will prompt the user when needed 
   * to supply missing information. This method may also return false if 
   * authentication fails for the supplied user name or password. 
   * Alternatively, this method may throw an AuthenticationFailedException 
   * when authentication fails. This exception may include a String message 
   * with more detail about the failure.
   * <p>
   * The <code>protocolConnect</code> method should throw an exception to 
   * report failures not related to authentication, such as an invalid host 
   * name or port number, loss of a connection during the authentication 
   * process, unavailability of the server, etc.
   * @param host the name of the host to connect to
   * @param port the port to use (-1 means use default port)
   * @param user the name of the user to login as
   * @param password the user's password
   * @return true if connection successful, false if authentication failed
   * @exception AuthenticationFailedException for authentication failures
   * @exception MessagingException for non-authentication failures
   */
  protected boolean protocolConnect(String host, int port, 
      String user, String password)
    throws MessagingException
  {
    return false;
  }

  /**
   * Is this service currently connected?
   * <p>
   * This implementation uses a private boolean field to store the connection
   * state. This method returns the value of that field.
   * <p>
   * Subclasses may want to override this method to verify that any connection
   * to the message store is still alive.
   */
  public boolean isConnected()
  {
    return connected;
  }

  /**
   * Set the connection state of this service.
   * The connection state will automatically be set by the service 
   * implementation during the connect and close methods.
   * Subclasses will need to call this method to set the state if
   * the service was automatically disconnected.
   * <p>
   * The implementation in this class merely sets the private field 
   * returned by the <code>isConnected</code> method.
   */
  protected void setConnected(boolean connected)
  {
    this.connected = connected;
  }

  /**
   * Close this service and terminate its connection.
   * A close ConnectionEvent is delivered to any ConnectionListeners.
   * Any Messaging components (Folders, Messages, etc.) belonging to 
   * this service are invalid after this service is closed. Note that 
   * the service is closed even if this method terminates abnormally 
   * by throwing a MessagingException.
   * <p>
   * This implementation uses <code>setConnected(false)</code> to set 
   * this service's connected state to false. It will then send a close 
   * ConnectionEvent to any registered ConnectionListeners. Subclasses 
   * overriding this method to do implementation specific cleanup should 
   * call this method as a last step to insure event notification, 
   * probably by including a call to <code>super.close()</code> in
   * a finally clause.
   */
  public synchronized void close()
    throws MessagingException
  {
    setConnected(false);
    notifyConnectionListeners(ConnectionEvent.CLOSED);
  }

  /**
   * Return a URLName representing this service.
   * The returned URLName does not include the password field.
   * <p>
   * Subclasses should only override this method if their URLName does not
   * follow the standard format.
   * <p>
   * The implementation in the Service class returns (usually a copy of)
   * the url field with the password and file information stripped out.
   */
  public URLName getURLName()
  {
    if (url!=null && (url.getPassword()!=null || url.getFile()!=null))
      return new URLName(url.getProtocol(), url.getHost(), url.getPort(),
          null, url.getUsername(), null);
    return url;
  }

  /**
   * Set the URLName representing this service.
   * Normally used to update the url field after a service has 
   * successfully connected.
   * <p>
   * Subclasses should only override this method if their URL does not 
   * follow the standard format. In particular, subclasses should override 
   * this method if their URL does not require all the possible fields 
   * supported by URLName; a new URLName should be constructed with any 
   * unneeded fields removed.
   * <p>
   * The implementation in the Service class simply sets the url field.
   */
  protected void setURLName(URLName url)
  {
    this.url = url;
  }

  // -- Event management --

  /*
   * Because the propagation of events of different kinds in the JavaMail
   * API is so haphazard, I have here sacrificed a small time advantage for
   * readability and consistency.
   *
   * All the various propagation methods now call a method with a name based
   * on the eventual listener method name prefixed by 'fire', as is the
   * preferred pattern for usage of the EventListenerList in Swing.
   *
   * Note that all events are currently delivered synchronously, where in
   * Sun's implementation a different thread is used for event delivery.
   * 
   * TODO Examine the impact of this.
   */

  // -- Connection events --

  /**
   * Add a listener for Connection events on this service.
   */
  public synchronized void addConnectionListener(ConnectionListener l)
  {
    if (connectionListeners==null)
      connectionListeners = new Vector();
    connectionListeners.addElement(l);
  }

  /**
   * Remove a Connection event listener.
   */
  public synchronized void removeConnectionListener(ConnectionListener l)
  {
    if (connectionListeners!=null)
      connectionListeners.removeElement(l);
  }

  /**
   * Notify all ConnectionListeners. 
   * Service implementations are expected to use this method 
   * to broadcast connection events.
   */
  protected void notifyConnectionListeners(int type)
  {
    ConnectionEvent event = new ConnectionEvent(this, type);
    switch (type)
    {
      case ConnectionEvent.OPENED:
        fireOpened(event);
        break;
      case ConnectionEvent.DISCONNECTED:
        fireDisconnected(event);
        break;
      case ConnectionEvent.CLOSED:
        fireClosed(event);
        break;
    }
  }

  /*
   * Propagates an OPENED ConnectionEvent to all registered listeners.
   */
  void fireOpened(ConnectionEvent event)
  {
    if (connectionListeners!=null)
    {
      for (Enumeration e = connectionListeners.elements(); 
          e.hasMoreElements(); )
        ((ConnectionListener)e.nextElement()).opened(event);
    }
  }

  /*
   * Propagates a DISCONNECTED ConnectionEvent to all registered listeners.
   */
  void fireDisconnected(ConnectionEvent event)
  {
    if (connectionListeners!=null)
    {
      for (Enumeration e = connectionListeners.elements(); 
          e.hasMoreElements(); )
        ((ConnectionListener)e.nextElement()).disconnected(event);
    }
  }

  /*
   * Propagates a CLOSED ConnectionEvent to all registered listeners.
   */
  void fireClosed(ConnectionEvent event)
  {
    if (connectionListeners!=null)
    {
      for (Enumeration e = connectionListeners.elements(); 
          e.hasMoreElements(); )
        ((ConnectionListener)e.nextElement()).closed(event);
    }
  }

  /**
   * Return getURLName.toString() if this service has a URLName,
   * otherwise it will return the default toString.
   */
  public String toString()
  {
    URLName url = getURLName();
    return (url!=null) ? url.toString() : super.toString();
  }

}
