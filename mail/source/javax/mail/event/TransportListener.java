/*
 * TransportListener.java
 * Copyright (C) 2002 The Free Software Foundation
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

import java.util.EventListener;

/**
 * This is the Listener interface for Transport events.
 *
 * @author <a href="mailto:dog@gnu.org">Chris Burdess</a>
 */
public interface TransportListener
  extends EventListener
{

  /**
   * Invoked when a Message is succesfully delivered.
   */
  public void messageDelivered(TransportEvent e);

  /**
   * Invoked when a Message is not delivered.
   */
  public void messageNotDelivered(TransportEvent e);

  /**
   * Invoked when a Message is partially delivered.
   */
  public void messagePartiallyDelivered(TransportEvent e);
  
}
