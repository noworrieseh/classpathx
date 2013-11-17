/*
 * UIDPlusCallback.java
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

/**
 * Callback interface for receiving APPENDUID and COPYUID responses.
 * A callback can additionally implement this interface to receive UIDPlus
 * notifications.
 * @version 1.2
 * @since 1.2
 * @author <a href='mailto:dog@gnu.org'>Chris Burdess</a>
 * @see RFC 2359
 */
public interface UIDPlusCallback
{

  /**
   * Indicates that the message has been appended to the destination
   * mailbox.
   * @param uidvalidity uidvalidity of the destination mailbox
   * @param uid uid of the appended message in the destination mailbox
   */
  void appenduid(long uidvalidity, long uid);

  /**
   * Indicates that the message(s) have been copied to the destination
   * mailbox with the stated UID(s).
   * @param uidvalidity uidvalidity of the destination mailbox
   * @param source UID(s) of the message(s) in the source mailbox
   * @param destination UID(s) of the message(s) in the destination mailbox
   */
  void copyuid(long uidvalidity, UIDSet source, UIDSet destination);

  /**
   * Indicates that the server does not support persistent UIDs, and APPEND
   * and COPY will not return APPENDUID or COPYUID responses.
   */
  void uidnotsticky();

}
