/*
 * FromTerm.java
 * Copyright (C) 2002 The Free Software Foundation
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

import javax.mail.Address;
import javax.mail.Message;

/**
 * This class implements comparisons for the From Address header.
 *
 * @author <a href="mailto:dog@gnu.org">Chris Burdess</a>
 */
public final class FromTerm
  extends AddressTerm
{

  /**
   * Constructor
   * @param address The Address to be compared
   */
  public FromTerm(Address address)
  {
    super(address);
  }

  /**
   * The address comparator.
   * @param msg The address comparison is applied to this Message
   * @return true if the comparison succeeds, otherwise false
   */
  public boolean match(Message msg)
  {
    try
    {
      Address[] addresses = msg.getFrom();
      if (addresses!=null)
      {
        for (int i = 0; i<addresses.length; i++)
        {
          if (super.match(addresses[i]))
            return true;
        }
      }
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
    return (other instanceof FromTerm && super.equals(other));
  }

}
