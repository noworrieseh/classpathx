/*
 * Transport.java
 * Copyright (C) 2002, 2013 The Free Software Foundation
 *
 * This file is part of GNU Classpath Extensions (classpathx).
 * For more information please visit https://www.gnu.org/software/classpathx/
 *
 * classpathx is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * classpathx is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with classpathx.
 * If not, see <http://www.gnu.org/licenses/>.
 */

package javax.mail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.ResourceBundle;
import javax.mail.event.TransportEvent;
import javax.mail.event.TransportListener;

/**
 * A message transport mechanism that can be used to deliver messages.
 *
 * @author <a href='mailto:dog@gnu.org'>Chris Burdess</a>
 * @version 1.5
 */
public abstract class Transport
  extends Service
{

  private static final ResourceBundle L10N
    = ResourceBundle.getBundle("javax.mail.L10N");

  /*
   * Transport listener list.
   */
  private ArrayList transportListeners = null;

  /**
   * Constructor.
   * @param session the session context for this transport
   * @param url a URLName to be used for this transport
   */
  public Transport(Session session, URLName url)
  {
    super(session, url);
  }

  /**
   * Sends the specified message.
   * The message will be sent to all recipient addresses specified in the
   * message, using transports appropriate to each address (specified by the
   * <code>javamail.address.map</code> resource).
   * @param msg the message to send
   * @exception SendFailedException if the message could not be sent to
   * some or any of the recipients
   */
  public static void send(Message msg)
    throws MessagingException
  {
    msg.saveChanges();
    doSend(msg, msg.getAllRecipients(), null, null);
  }

  /**
   * Sends the specified message.
   * The message will be sent to all recipient addresses specified in the
   * message, using transports appropriate to each address (specified by the
   * <code>javamail.address.map</code> resource).
   * @param msg the message to send
   * @param username the name of the user to authenticate as
   * @param password the password of the user to authenticate as
   * @exception SendFailedException if the message could not be sent to
   * some or any of the recipients
   * @since JavaMail 1.5
   */
  public static void send(Message msg, String username, String password)
    throws MessagingException
  {
    msg.saveChanges();
    doSend(msg, msg.getAllRecipients(), username, password);
  }

  /**
   * Sends the message to the specified addresses, ignoring any recipients
   * specified in the message itself.
   * @param msg the message to send
   * @param addresses the addresses to which to send the message
   * @exception SendFailedException if the message could not be sent to
   * some or any of the recipients
   */
  public static void send(Message msg, Address[] addresses)
    throws MessagingException
  {
    msg.saveChanges();
    doSend(msg, addresses, null, null);
  }

  /**
   * Sends the message to the specified addresses, ignoring any recipients
   * specified in the message itself.
   * @param msg the message to send
   * @param addresses the addresses to which to send the message
   * @param username the name of the user to authenticate as
   * @param password the password of the user to authenticate as
   * @exception SendFailedException if the message could not be sent to
   * some or any of the recipients
   * @since JavaMail 1.5
   */
  public static void send(Message msg, Address[] addresses,
                          String username, String password)
    throws MessagingException
  {
    msg.saveChanges();
    doSend(msg, addresses, username, password);
  }

  /*
   * Performs the send after saveChanges() has been called.
   */
  private static void doSend(Message msg, Address[] addresses,
                             String username, String password)
    throws MessagingException
  {
    if (addresses == null || addresses.length == 0)
      {
        String m = L10N.getString("err.no_recipient_addresses");
        throw new SendFailedException(m);
      }

    HashMap addressesByType = new HashMap();
    for (int i = 0; i < addresses.length; i++)
      {
        String type = addresses[i].getType();
        if (addressesByType.containsKey(type))
          {
           ((ArrayList) addressesByType.get(type)).add(addresses[i]);
          }
        else
          {
            ArrayList addressList = new ArrayList();
            addressList.add(addresses[i]);
            addressesByType.put(type, addressList);
          }
      }

    int size = addressesByType.size();
    if (size == 0)
      {
        String m = L10N.getString("err.no_recipient_addresses");
        throw new SendFailedException(m);
      }

    Session session = msg.session;
    if (session == null)
      {
        session = Session.getDefaultInstance(System.getProperties(), null);
      }

    MessagingException ex = null;
    boolean error = false;
    ArrayList validSent = new ArrayList();
    ArrayList validUnsent = new ArrayList();
    ArrayList invalid = new ArrayList();

    for (Iterator i = addressesByType.values().iterator(); i.hasNext(); )
      {
        ArrayList addressList = (ArrayList) i.next();
        Address[] addressArray = new Address[addressList.size()];
        addressList.toArray(addressArray);

        if (addressArray.length < 1)
          {
            break;
          }

        Transport transport = session.getTransport(addressArray[0]);
        if (transport == null)
          {
            invalid.addAll(Arrays.asList(addressArray));
          }
        else
          {
            synchronized (transport)
              {
                URLName urlname = transport.getURLName();
                PasswordAuthentication oldpw =
                  session.getPasswordAuthentication(urlname);
                try
                  {
                    if (username != null)
                      {
                        PasswordAuthentication pw =
                          new PasswordAuthentication(username, password);
                        session.setPasswordAuthentication(urlname, pw);
                      }
                    transport.connect();
                    transport.sendMessage(msg, addressArray);
                  }
                catch (SendFailedException sfex)
                  {
                    error = true;
                    if (ex == null)
                      {
                        ex = sfex;
                      }
                    else
                      {
                        ex.setNextException(sfex);
                      }

                    Address[] a;

                    a = sfex.getValidSentAddresses();
                    if (a != null)
                      {
                        validSent.addAll(Arrays.asList(a));
                      }
                    a = sfex.getValidUnsentAddresses();
                    if (a != null)
                      {
                        validUnsent.addAll(Arrays.asList(a));
                      }
                    a = sfex.getInvalidAddresses();
                    if (a != null)
                      {
                        invalid.addAll(Arrays.asList(a));
                      }
                  }
                catch (MessagingException mex)
                  {
                    error = true;
                    if (ex == null)
                      {
                        ex = mex;
                      }
                    else
                      {
                        ex.setNextException(mex);
                      }
                  }
                finally
                  {
                    transport.close();
                    if (username != null)
                      {
                        session.setPasswordAuthentication(urlname, oldpw);
                      }
                  }
              }
          }
      }

    if (error || invalid.size() != 0 || validSent.size() != 0)
      {
        Address[] validSentAddresses = null;
        Address[] validUnsentAddresses = null;
        Address[] invalidAddresses = null;

        if (validSent.size() > 0)
          {
            validSentAddresses = new Address[validSent.size()];
            validSent.toArray(validSentAddresses);
          }
        if (validUnsent.size() > 0)
          {
            validUnsentAddresses = new Address[validUnsent.size()];
            validUnsent.toArray(validUnsentAddresses);
          }
        if (invalid.size() > 0)
          {
            invalidAddresses = new Address[invalid.size()];
            invalid.toArray(invalidAddresses);
          }
        String m = L10N.getString("err.send_failed");
        throw new SendFailedException(m, ex,
                                      validSentAddresses,
                                      validUnsentAddresses,
                                      invalidAddresses);
      }
  }

  /**
   * Sends the message to the specified list of addresses.
   * @param msg the message to be sent
   * @param addresses the addresses to send this message to
   * @exception SendFailedException if the send failed because of
   * invalid addresses
   * @exception MessagingException if the transport is not connected
   */
  public abstract void sendMessage(Message msg, Address[] addresses)
    throws MessagingException;

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

  // -- Transport events --

  /**
   * Adds a listener for transport events.
   */
  public void addTransportListener(TransportListener l)
  {
    if (transportListeners == null)
      {
        transportListeners = new ArrayList();
      }
    synchronized (transportListeners)
      {
        transportListeners.add(l);
      }
  }

  /**
   * Removes a transport event listener.
   */
  public void removeTransportListener(TransportListener l)
  {
    if (transportListeners != null)
      {
        synchronized (transportListeners)
          {
            transportListeners.remove(l);
          }
      }
  }

  /**
   * Notifies all transport listeners.
   */
  protected void notifyTransportListeners(int type,
                                           Address[] validSent,
                                           Address[] validUnsent,
                                           Address[] invalid,
                                           Message msg)
  {
    TransportEvent event =
      new TransportEvent(this, type, validSent, validUnsent, invalid, msg);
    switch (type)
      {
      case TransportEvent.MESSAGE_DELIVERED:
        fireMessageDelivered(event);
        break;
      case TransportEvent.MESSAGE_NOT_DELIVERED:
        fireMessageNotDelivered(event);
        break;
      case TransportEvent.MESSAGE_PARTIALLY_DELIVERED:
        fireMessagePartiallyDelivered(event);
        break;
      }
  }

  /*
   * Propagates a MESSAGE_DELIVERED TransportEvent
   * to all registered listeners.
   */
  void fireMessageDelivered(TransportEvent event)
  {
    if (transportListeners != null)
      {
        TransportListener[] l = null;
        synchronized (transportListeners)
          {
            l = new TransportListener[transportListeners.size()];
            transportListeners.toArray(l);
          }
        for (int i = 0; i < l.length; i++)
          {
            l[i].messageDelivered(event);
          }
      }
  }

  /*
   * Propagates a MESSAGE_NOT_DELIVERED TransportEvent
   * to all registered listeners.
   */
  void fireMessageNotDelivered(TransportEvent event)
  {
    if (transportListeners != null)
      {
        TransportListener[] l = null;
        synchronized (transportListeners)
          {
            l = new TransportListener[transportListeners.size()];
            transportListeners.toArray(l);
          }
        for (int i = 0; i < l.length; i++)
          {
            l[i].messageNotDelivered(event);
          }
      }
  }

  /*
   * Propagates a MESSAGE_PARTIALLY_DELIVERED TransportEvent
   * to all registered listeners.
   */
  void fireMessagePartiallyDelivered(TransportEvent event)
  {
    if (transportListeners != null)
      {
        TransportListener[] l = null;
        synchronized (transportListeners)
          {
            l = new TransportListener[transportListeners.size()];
            transportListeners.toArray(l);
          }
        for (int i = 0; i < l.length; i++)
          {
            l[i].messagePartiallyDelivered(event);
          }
      }
  }

}

