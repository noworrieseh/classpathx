/*
 * SearchTerm.java
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

package javax.mail.search;

import java.io.Serializable;
import javax.mail.Message;

/**
 * Search criteria are expressed as a tree of search-terms,
 * forming a parse-tree for the search expression.
 * <p>
 * Search-terms are represented by this class. This is an abstract class;
 * subclasses implement specific match methods.
 * <p>
 * Search terms are serializable, which allows storing a search term between
 * sessions. Warning: Serialized objects of this class may not be compatible 
 * with future JavaMail API releases. The current serialization support is 
 * appropriate for short term storage.
 * <p>
 * Warning: Search terms that include references to objects of type
 * Message.RecipientType will not be deserialized correctly on JDK 1.1 
 * systems. While these objects will be deserialized without throwing any 
 * exceptions, the resulting objects violate the type-safe enum contract of 
 * the Message.RecipientType class. Proper deserialization of these objects 
 * depends on support for the readReplace method, added in JDK 1.2.
 *
 * @author <a href="mailto:dog@gnu.org">Chris Burdess</a>
 * @version 1.3
 */
public abstract class SearchTerm
  implements Serializable
{

  /**
   * This method applies a specific match criterion to the given message and
   * returns the result.
   * @param msg The match criterion is applied on this message
   * @return true, it the match succeeds, false if the match fails
   */
  public abstract boolean match(Message msg);

}
