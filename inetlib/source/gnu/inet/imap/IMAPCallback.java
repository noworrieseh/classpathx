/*
 * IMAPCallback.java
 * Copyright (C) 2013 The Free Software Foundation
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

package gnu.inet.imap;

import java.util.List;

/**
 * An IMAP response callback.
 * @version 1.2
 * @since 1.2
 * @author <a href='mailto:dog@gnu.org'>Chris Burdess</a>
 */
public interface IMAPCallback
{

  /**
   * Notification of a user alert.
   * This should be displayed to the user somehow.
   * @param message the message text
   */
  void alert(String message);

  /**
   * Notification of capabilities change.
   * @param capabilities the new capabilities
   */
  void capability(List<String> capabilities);

  /**
   * Notification of the number of messages in a mailbox.
   * @param messages the number of messages
   */
  void exists(int messages);

  /**
   * Notification of the number of messages with the recent flag set.
   * @param messages the number of messages
   */
  void recent(int messages);

  /**
   * Notification that a given messages has been permanently deleted.
   * @param message the message number
   */
  void expunge(int message);

  /**
   * Notification of data specific to a message
   * @param message the message number
   * @param data the message data
   */
  void fetch(int message, List<FetchDataItem> data);

}
