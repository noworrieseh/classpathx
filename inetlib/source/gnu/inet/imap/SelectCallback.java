/*
 * SelectCallback.java
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
 * An IMAP SELECT response callback.
 * @version 1.2
 * @since 1.2
 * @author <a href='mailto:dog@gnu.org'>Chris Burdess</a>
 */
public interface SelectCallback
  extends IMAPCallback
{

  /**
   * Notification of the flags applicable for the associated mailbox.
   */
  void flags(List<String> flags);

  /**
   * Notification of the permanent flags applicable for the associated
   * mailbox. These are flags that the user can change permanently. If not
   * present, all flags can be changed permanently.
   */
  void permanentflags(List<String> flags);

  /**
   * The message sequence number of the first unseen message in the mailbox.
   * If not present, a SEARCH command must be issued to discover this value.
   */
  void unseen(int message);

  /**
   * The unique identifier validity value. If not present, the server does
   * not support UIDs.
   */
  void uidvalidity(long uidvalidity);

  /**
   * The next unique identifier value.
   */
  void uidnext(long uid);

  /**
   * Indicates that the mailbox was opened read-write.
   */
  void readWrite();

  /**
   * Indicates that the mailbox was opened read-only.
   */
  void readOnly();

  /**
   * Indicates that the mailbox does not exist yet but could be created.
   */
  void tryCreate();

}

