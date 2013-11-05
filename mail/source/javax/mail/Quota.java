/*
 * Quota.java
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

package javax.mail;

/**
 * A set of quotas for a given quota root.
 *
 * @author <a href="mailto:dog@gnu.org">Chris Burdess</a>
 * @see RFC 2087
 * @version 1.5
 * @since JavaMail 1.4
 */
public class Quota
{

  /**
   * An individual quota resource.
   * @since JavaMail 1.4
   */
  public static class Resource
  {

    /**
     * The resource name.
     */
    public String name;

    /**
     * The current resource usage.
     */
    public long usage;

    /**
     * The usage limit for the resource.
     */
    public long limit;

    /**
     * Constructor.
     * @param name the resource name
     * @param usage the current usage
     * @param limit the usage limit
     */
    public Resource(String name, long usage, long limit)
    {
      this.name = name;
      this.usage = usage;
      this.limit = limit;
    }

  }

  /**
   * The quota root.
   */
  public String quotaRoot;

  /**
   * The resources associated with this quota.
   */
  public Resource[] resources;

  /**
   * Constructor.
   * @param quotaRoot the quota root
   */
  public Quota(String quotaRoot)
  {
    this.quotaRoot = quotaRoot;
  }

  /**
   * Sets a resource limit.
   * @param name the resource name
   * @param limit the usage limit
   */
  public void setResourceLimit(String name, long limit)
  {
    if (resources != null)
      {
        boolean found = false;
        for (int i = 0; i < resources.length; i++)
          {
            if (resources[i].name.equals(name))
              {
                resources[i].limit = limit;
                found = true;
              }
          }
        if (!found)
          {
            Resource[] r = new Resource[resources.length + 1];
            System.arraycopy(resources, 0, r, 0, resources.length);
            r[resources.length] = new Resource(name, 0L, limit);
            resources = r;
          }
      }
    else
      {
        resources = new Resource[1];
        resources[0] = new Resource(name, 0L, limit);
      }
  }

}
