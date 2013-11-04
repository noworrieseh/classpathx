/*
 * TransportEvent.java
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

package javax.mail.event;

import javax.mail.*;

/**
 * This class models Transport events.
 */
public class TransportEvent
  extends MailEvent
{

  /**
   * Message has been successfully delivered to all recipients by the
   * transport firing this event.
   * validSent[] contains all the addresses this transport sent to
   * successfully. validUnsent[] and invalid[] should be null.
   */
  public static final int MESSAGE_DELIVERED = 1;

  /**
   * Message was not sent for some reason. 
   * validSent[] should be null. validUnsent[] may have addresses that 
   * are valid (but the message wasn't sent to them).
   * invalid[] should likely contain invalid addresses.
   */
  public static final int MESSAGE_NOT_DELIVERED = 2;

  /**
   * Message was successfully sent to some recipients but not to all.
   * validSent[] holds addresses of recipients to whom the message was sent.
   * validUnsent[] holds valid addresses to which the message was not sent.
   * invalid[] holds invalid addresses, if any.
   */
  public static final int MESSAGE_PARTIALLY_DELIVERED = 3;

  /**
   * The event type.
   */
  protected int type;

  protected transient Address[] validSent;
  protected transient Address[] validUnsent;
  protected transient Address[] invalid;
  protected transient Message msg;

  /**
   * Constructor.
   * @param source The Transport object
   */
  public TransportEvent(Transport transport, int type,
      Address[] validSent, Address[] validUnsent, Address[] invalid,
      Message msg)
  {
    super(transport);
    this.type = type;
    this.validSent = validSent;
    this.validUnsent = validUnsent;
    this.invalid = invalid;
    this.msg = msg;
  }

  /**
   * Return the type of this event.
   */
  public int getType()
  {
    return type;
  }

  /**
   * Return the addresses to which this message was sent succesfully.
   */
  public Address[] getValidSentAddresses()
  {
    return validSent;
  }

  /**
   * Return the addresses that are valid but to which this message was not 
   * sent.
   */
  public Address[] getValidUnsentAddresses()
  {
    return validUnsent;
  }

  /**
   * Return the addresses to which this message could not be sent.
   */
  public Address[] getInvalidAddresses()
  {
    return invalid;
  }

  /**
   * Get the Message object associated with this Transport Event.
   */
  public Message getMessage()
  {
    return msg;
  }

  /**
   * Invokes the appropriate TransportListener method.
   */
  public void dispatch(Object listener)
  {
    TransportListener l = (TransportListener)listener;
    switch (type)
    {
      case MESSAGE_DELIVERED:
        l.messageDelivered(this);
        break;
      case MESSAGE_NOT_DELIVERED:
        l.messageNotDelivered(this);
        break;
      case MESSAGE_PARTIALLY_DELIVERED:
        l.messagePartiallyDelivered(this);
        break;
    }
  }

}
