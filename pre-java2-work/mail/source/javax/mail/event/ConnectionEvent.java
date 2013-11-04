/*
 * ConnectionEvent.java
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

/**
 * This class models Connection events.
 */
public class ConnectionEvent
  extends MailEvent
{

  /**
   * A connection was opened.
   */
  public static final int OPENED = 1;

  /**
   * A connection was disconnected (not currently used).
   */
  public static final int DISCONNECTED = 2;

  /**
   * A connection was closed.
   */
  public static final int CLOSED = 3;

  /**
   * The event type.
   */
  protected int type;

  /**
   * Constructor
   * @param source The source object
   */
  public ConnectionEvent(Object source, int type)
  {
    super(source);
    this.type = type;
  }

  /**
   * Return the type of this event
   */
  public int getType()
  {
    return type;
  }

  /**
   * Invokes the appropriate ConnectionListener method
   */
  public void dispatch(Object listener)
  {
    ConnectionListener l = (ConnectionListener)listener;
    switch (type)
    {
      case OPENED:
        l.opened(this);
        break;
      case DISCONNECTED:
        l.disconnected(this);
        break;
      case CLOSED:
        l.closed(this);
        break;
    }
  }
}
