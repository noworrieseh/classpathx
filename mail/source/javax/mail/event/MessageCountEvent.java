/*
 * MessageCountEvent.java
 * Copyright (C) 2002 The Free Software Foundation
 * 
 * This file is part of GNU JavaMail, a library.
 * 
 * GNU JavaMail is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 * GNU JavaMail is distributed in the hope that it will be useful,
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

package javax.mail.event;

import javax.mail.Folder;
import javax.mail.Message;

/**
 * This class notifies changes in the number of messages in a folder.
 * <p>
 * Note that some folder types may only deliver MessageCountEvents at certain
 * times or after certain operations. IMAP in particular will only notify the
 * client of MessageCountEvents when a client issues a new command. Refer to 
 * RFC 2060 http://www.ietf.org/rfc/rfc2060.txt for details. 
 * A client may want "poll" the folder by occasionally calling the 
 * <code>getMessageCount</code> or <code>isConnected</code> methods
 * to solicit any such notifications.
 *
 * @author <a href="mailto:dog@gnu.org">Chris Burdess</a>
 */
public class MessageCountEvent
  extends MailEvent
{

  /**
   * The messages were added to their folder
   */
  public static final int ADDED = 1;

  /**
   * The messages were removed from their folder
   */
  public static final int REMOVED = 2;

  /**
   * The event type.
   */
  protected int type;

  /**
   * If true, this event is the result of an explicit expunge by this client,
   * and the messages in this folder have been renumbered to account for this.
   * If false, this event is the result of an expunge by external sources.
   */
  protected boolean removed;

  /**
   * The messages.
   */
  protected transient Message[] msgs;

  /**
   * Constructor.
   * @param source The containing folder
   * @param type The event type
   * @param removed If true, this event is the result of an explicit expunge by
   * this client, and the messages in this folder have been renumbered to
   * account for this. If false, this event is the result of an expunge by
   * external sources.
   * @param msgs The messages added/removed
   */
  public MessageCountEvent(Folder source, int type, boolean removed, 
      Message[] msgs)
  {
    super(source);
    this.type = type;
    this.removed = removed;
    this.msgs = msgs;
  }

  /**
   * Return the type of this event.
   */
  public int getType()
  {
    return type;
  }

  /**
   * Indicates whether this event is the result of an explicit expunge by this
   * client, or due to an expunge from external sources. If true, this event is
   * due to an explicit expunge and hence all remaining messages in this folder
   * have been renumbered. If false, this event is due to an external expunge.
   * <p>
   * Note that this method is valid only if the type of this event is REMOVED
   */
  public boolean isRemoved()
  {
    return removed;
  }

  /**
   * Return the array of messages added or removed.
   */
  public Message[] getMessages()
  {
    return msgs;
  }

  /**
   * Invokes the appropriate MessageCountListener method.
   */
  public void dispatch(Object listener)
  {
    MessageCountListener l = (MessageCountListener)listener;
    switch (type)
    {
      case ADDED:
        l.messagesAdded(this);
        break;
      case REMOVED:
        l.messagesRemoved(this);
        break;
    }
  }

}
