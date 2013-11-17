/*
 * UIDSet.java
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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

/**
 * A set of UIDs. These are long values.
 * @version 1.2
 * @since 1.2
 * @author <a href='mailto:dog@gnu.org'>Chris Burdess</a>
 * @see RFC 2359
 */
public class UIDSet
  implements Set
{

  private final String s;
  private final List elements;
  private int len;
  private int count;

  UIDSet(String s)
  {
    this.s = s;
    count = 0;
    elements = new ArrayList();
    StringTokenizer st = new StringTokenizer(s, ",");
    while (st.hasMoreTokens())
      {
        String token = st.nextToken();
        int ci = token.indexOf(':');
        if (ci == -1)
          {
            elements.add(new Long(token));
            count++;
          }
        else
          {
            long start = Long.parseLong(token.substring(0, ci));
            long end = Long.parseLong(token.substring(ci + 1));
            elements.add(new Range(start, end));
            count += (end - start) + 1;
          }
      }
    len = elements.size();
  }

  public int size()
  {
    return count;
  }

  public boolean isEmpty()
  {
    return count == 0;
  }

  public void clear()
  {
    throw new UnsupportedOperationException();
  }

  public boolean contains(Object o)
  {
    for (int i = 0; i < len; i++)
      {
        Object e = elements.get(i);
        if (e instanceof Range)
          {
            if (((Range) e).contains(o))
              {
                return true;
              }
          }
        else
          {
            if (e.equals(o))
              {
                return true;
              }
          }
      }
    return false;
  }

  public boolean add(Object o)
  {
    throw new UnsupportedOperationException();
  }

  public boolean remove(Object o)
  {
    throw new UnsupportedOperationException();
  }

  public boolean containsAll(Collection c)
  {
    for (Iterator i = c.iterator(); i.hasNext(); )
      {
        if (!contains(i.next()))
          {
            return false;
          }
      }
    return true;
  }

  public boolean addAll(Collection c)
  {
    throw new UnsupportedOperationException();
  }

  public boolean retainAll(Collection c)
  {
    throw new UnsupportedOperationException();
  }

  public boolean removeAll(Collection c)
  {
    throw new UnsupportedOperationException();
  }

  public boolean equals(Object o)
  {
    return (o instanceof UIDSet && ((UIDSet) o).s.equals(s));
  }

  public int hashCode()
  {
    return s.hashCode();
  }

  public String toString()
  {
    return s;
  }

  public Iterator iterator()
  {
    return new UIDSetIterator(elements.iterator());
  }

  public Object[] toArray()
  {
    Long[] l = new Long[count];
    return toArray(l);
  }

  public Object[] toArray(Object[] a)
  {
    if (a == null || !(a instanceof Long[]) || a.length < count)
      {
        a = new Long[count];
      }
    Iterator li = iterator();
    for (int i = 0; i < count; i++)
      {
        a[i] = li.next();
      }
    return a;
  }

  private static class Range
  {

    private final long start;
    private final long end;

    Range(long start, long end)
    {
      this.start = start;
      this.end = end;
    }

    public boolean contains(Object o)
    {
      long l = ((Long) o).longValue();
      return l >= start && l <= end;
    }

    public Iterator iterator()
    {
      return new RangeIterator(start, end);
    }

    private static class RangeIterator
      implements Iterator
    {

      private long index;
      private long end;
      
      RangeIterator(long start, long end) {
        index = start;
        this.end = end;
      }

      public Object next()
      {
        return Long.valueOf(index++);
      }

      public boolean hasNext()
      {
        return (index <= end);
      }

      public void remove()
      {
        throw new UnsupportedOperationException();
      }

    }

  }

  private static class UIDSetIterator
    implements Iterator
  {

    private Iterator ei;
    private Iterator ri;

    UIDSetIterator(Iterator ei) {
      this.ei = ei;
    }

    public Object next()
    {
      if (ri != null)
        {
          Object ret = ri.next();
          if (!ri.hasNext())
            {
              ri = null;
            }
          return ret;
        }
      Object o = ei.next();
      if (o instanceof Range)
        {
          ri = ((Range) o).iterator();
          return next();
        }
      return o;
    }

    public boolean hasNext()
    {
      if (ri != null)
        {
          return ri.hasNext();
        }
      return ei.hasNext();
    }

    public void remove()
    {
      throw new UnsupportedOperationException();
    }

  }

}
