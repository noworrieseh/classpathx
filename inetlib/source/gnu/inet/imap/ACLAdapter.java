/*
 * ACLAdapter.java
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
 * Adapter for an IMAP Access Control List response callback.
 * This is provided for convenience of implementation.
 * @version 1.2
 * @since 1.2
 * @author <a href='mailto:dog@gnu.org'>Chris Burdess</a>
 */
public class ACLAdapter
  extends IMAPAdapter
  implements ACLCallback
{

  public void acl(String mailbox, Map<String,String> rights)
  {
  }

  public void listrights(String mailbox, String identifier, String required,
                         List<String> optional)
  {
  }

  public void myrights(String mailbox, String rights)
  {
  }

}
