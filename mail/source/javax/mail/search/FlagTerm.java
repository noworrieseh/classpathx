/*
 * FlagTerm.java
 * Copyright (C) 2001 dog <dog@dog.net.uk>
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

package javax.mail.search;

import javax.mail.Flags;
import javax.mail.Message;

/**
 * This class implements comparisons for Message Flags.
 */
public final class FlagTerm
  extends SearchTerm
{

  /**
   * Indicates whether to test for the presence or absence of the specified
   * Flag. If true, then test whether all the specified flags are present, 
   * else test whether all the specified flags are absent.
   */
  protected boolean set;

  /**
   * Flags object containing the flags to test.
   */
  protected Flags flags;

  /**
   * Constructor.
   * @param flags Flags object containing the flags to check for
   * @param set the flag setting to check for
   */
  public FlagTerm(Flags flags, boolean set)
  {
    this.flags = flags;
    this.set = set;
  }

  /**
   * Return the Flags to test.
   */
  public Flags getFlags()
  {
    return (Flags)flags.clone();
  }

  /**
   * Return true if testing whether the flags are set.
   */
  public boolean getTestSet()
  {
    return set;
  }

  /**
   * The comparison method.
   * @param msg The flag comparison is applied to this Message
   * @return true if the comparson succeeds, otherwise false.
   */
  public boolean match(Message msg)
  {
    try
    {
      Flags messageFlags = msg.getFlags();
      if (set)
        return messageFlags.contains(flags);
      Flags.Flag[] systemFlags = flags.getSystemFlags();
      for (int i = 0; i<systemFlags.length; i++)
      {
        if (messageFlags.contains(systemFlags[i]))
          return false;
      }
      String[] userFlags = flags.getUserFlags();
      for (int i = 0; i<userFlags.length; i++)
      {
        if (messageFlags.contains(userFlags[i]))
          return false;
      }
      return true;
    }
    catch (Exception e)
    {
    }
    return false;
  }

  /**
   * Equality comparison.
   */
  public boolean equals(Object other)
  {
    if (other instanceof FlagTerm)
    {
      FlagTerm ft = (FlagTerm)other;
      return (ft.set==set && ft.flags.equals(flags));
    }
    return false;
  }

  /**
   * Compute a hashCode for this object.
   */
  public int hashCode()
  {
    return set ?  flags.hashCode() : ~flags.hashCode();
  }
  
}
