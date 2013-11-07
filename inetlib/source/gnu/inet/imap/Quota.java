/*
 * Quota.java
 * Copyright (C) 2004 The Free Software Foundation
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
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * An IMAP quota entry.
 *
 * @author <a href='mailto:dog@gnu.org'>Chris Burdess</a>
 */
public class Quota
{

  /**
   * A quota resource entry.
   */
  public static class Resource
  {

    String name;
    int current;
    int limit;

    /**
     * Specifies a new resource with the given name and limit.
     * @param name the resource name
     * @param limit the resource limit
     */
    public Resource(String name, int limit)
    {
      this(name, -1, limit);
    }

    Resource(String name, int current, int limit)
    {
      this.name = name;
      this.current = current;
      this.limit = limit;
    }

    /**
     * Returns the resource name.
     */
    public String getName()
    {
      return name;
    }

    /**
     * Returns the current usage of the resource, or <code>-1</code> if not
     * known.
     */
    public int getCurrentUsage()
    {
      return current;
    }

    /**
     * Returns the resource limit.
     */
    public int getLimit()
    {
      return limit;
    }

    /**
     * Debugging.
     */
    public String toString()
    {
      StringBuffer buf = new StringBuffer();
      buf.append('(');
      buf.append(name);
      if (current >= 0)
        {
          buf.append(' ');
          buf.append(current);
        }
      buf.append(' ');
      buf.append(limit);
      buf.append(')');
      return buf.toString();
    }
    
  }

  String quotaRoot;
  List resources;

  Quota(String text)
  {
    int len = text.length();
    List acc = new ArrayList();
    Namespaces.parse(text, 0, len, acc);

    quotaRoot = (String) acc.get(0);
    resources = new ArrayList();
    len = acc.size();
    for (int i = 1; i < len; i++)
      {
        resources.add(parseResource((List) acc.get(i)));
      }
  }

  private Resource parseResource(List triple)
  {
    String name = (String) triple.get(0);
    String current = (String) triple.get(1);
    String limit = (String) triple.get(2);
    return new Resource(name,
                        Integer.parseInt(current),
                        Integer.parseInt(limit));
  }

  /**
   * Returns the quota root. All mailboxes that share the same named
   * quota root share the resource limits of the quota root.
   */
  public String getQuotaRoot()
  {
    return quotaRoot;
  }

  /**
   * Returns the list of quota resources.
   */
  public Resource[] getResources()
  {
    Resource[] ret = new Resource[resources.size()];
    resources.toArray(ret);
    return ret;
  }

  /**
   * Debugging.
   */
  public String toString()
  {
    StringBuffer buf = new StringBuffer();
    buf.append(IMAPConnection.quote(UTF7imap.encode(quotaRoot)));
    int len = resources.size();
    for (int i = 0; i < len; i++)
      {
        buf.append(' ');
        buf.append(resources.get(i));
      }
    return buf.toString();
  }
  
}

