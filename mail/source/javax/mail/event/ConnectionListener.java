/*
 * ConnectionListener.java
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
 * This is the Listener interface for Connection events.
 *
 * @author <a href="mailto:dog@gnu.org">Chris Burdess</a>
 */
public interface ConnectionListener
  extends EventListener
{

  /**
   * Invoked when a Store/Folder/Transport is opened.
   */
  public void opened(ConnectionEvent e);

  /**
   * Invoked when a Store is disconnected.
   * Note that a folder cannot be disconnected, so a folder will not fire 
   * this event.
   */
  public void disconnected(ConnectionEvent e);

  /**
   * Invoked when a Store/Folder/Transport is closed.
   */
  public void closed(ConnectionEvent e);

}
