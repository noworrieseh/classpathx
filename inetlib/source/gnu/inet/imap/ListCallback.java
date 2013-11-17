/*
 * ListCallback.java
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
 * An IMAP LIST or LSUB response callback.
 * @version 1.2
 * @since 1.2
 * @author <a href='mailto:dog@gnu.org'>Chris Burdess</a>
 */
public interface ListCallback
  extends IMAPCallback
{

  /**
   * Notification of a list entry.
   * @param attributes the mailbox attributes
   * @param delimiter the mailbox hierarchy delimiter
   * @param mailbox the mailbox name
   */
  void list(List<String> attributes, String delimiter, String mailbox);

}
