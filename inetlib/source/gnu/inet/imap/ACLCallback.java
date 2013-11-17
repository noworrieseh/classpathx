/*
 * ACLCallback.java
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
import java.util.Map;

/**
 * An IMAP Access Control List response callback.
 * @version 1.2
 * @since 1.2
 * @author <a href='mailto:dog@gnu.org'>Chris Burdess</a>
 * @see RFC 4314
 */
public interface ACLCallback
  extends IMAPCallback
{

  /**
   * Notification of access control rights.
   * This occurs in response to a GETACL command.
   * @param mailbox the mailbox name
   * @param rights the identifier-rights pairs
   */
  void acl(String mailbox, Map<String,String> rights);

  /**
   * Notification of a list of rights.
   * This occurs in response to a LISTRIGHTS command.
   * @param mailbox the mailbox name
   * @param identifier the identifier
   * @param required the required rights for this identifier
   * @param rights the list of optional rights for this identifier
   */
  void listrights(String mailbox, String identifier, String required,
                  List<String> optional);

  /**
   * Notification of the user's rights with respect to the mailbox.
   * This occurs in response to a MYRIGHTS command.
   * @param mailbox the mailbox name
   * @param rights the rights for the current user
   */
  void myrights(String mailbox, String rights);

}


