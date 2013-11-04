/*
 * MessageCountListener.java
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

import java.util.EventListener;

/**
 * This is the Listener interface for MessageCount events.
 */
public interface MessageCountListener
  extends EventListener
{

  /**
   * Invoked when messages are added into a folder.
   */
  public void messagesAdded(MessageCountEvent e);

  /**
   * Invoked when messages are removed (expunged) from a folder.
   */
  public void messagesRemoved(MessageCountEvent e);
  
}
