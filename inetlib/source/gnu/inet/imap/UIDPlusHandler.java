/*
 * UIDPlusHandler.java
 * Copyright (C) 2005 The Free Software Foundation
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
 * See RFC 2359 for details.
 *
 * @author <a href='mailto:dog@gnu.org'>Chris Burdess</a>
 */
public interface UIDPlusHandler
{

  /**
   * Notification of an APPENDUID response.
   * Called on a successful APPEND to a server that supports the UIDPLUS
   * extension.
   * @param uidvalidity the UIDVALIDITY of the destination mailbox
   * @param uid the UID assigned to the appended message.
   */
  void appenduid(long uidvalidity, long uid);

  /**
   * Notification of a COPYUID response.
   * Called on a successful COPY to a server that supports the UIDPLUS
   * extension.
   * If more than one message is copied, this method will be called multiple
   * times, once for each message copied.
   * @param uidvalidity the UIDVALIDITY of the destination mailbox
   * @param oldUID the UID of the message in the source mailbox
   * @param newUID the UID of the corresponding message in the target
   * mailbox
   */
  void copyuid(long uidvalidity, long oldUID, long newUID);
  
}

