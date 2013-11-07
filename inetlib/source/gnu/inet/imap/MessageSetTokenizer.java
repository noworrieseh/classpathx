/*
 * MessageSetTokenizer.java
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

import java.util.Iterator;
import java.util.LinkedList;

/**
 * Tokenizer for an IMAP UID message-set.
 * This iterates over the UIDs specified in the set.
 *
 * @author <a href='mailto:dog@gnu.org'>Chris Burdess</a>
 */
class MessageSetTokenizer
  implements Iterator
{

  private Iterator iterator;

  MessageSetTokenizer(String spec)
  {
    LinkedList acc = new LinkedList();
    for (int ci = spec.indexOf(','); ci != -1; ci = spec.indexOf(','))
      {
        addToken(acc, spec.substring(0, ci));
        spec = spec.substring(ci + 1);
      }
    addToken(acc, spec);
    iterator = acc.iterator();
  }

  private void addToken(LinkedList acc, String token)
  {
    int ci = token.indexOf(':');
    if (ci == -1)
      {
        acc.add(new Long(token));
      }
    else
      {
        long start = Long.parseLong(token.substring(0, ci));
        long end = Long.parseLong(token.substring(ci + 1));
        while (start <= end)
          {
            acc.add(new Long(start++));
          }
      }
  }

  public boolean hasNext()
  {
    return iterator.hasNext();
  }

  public Object next()
  {
    return iterator.next();
  }

  public void remove()
  {
    iterator.remove();
  }
  
}

