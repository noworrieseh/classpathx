/*
 * AddressTerm.java
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

/**
 * This class implements Message Address comparisons.
 *
 * @author <a href="mailto:dog@gnu.org">Chris Burdess</a>
 */
public abstract class AddressTerm
  extends SearchTerm
{

  /**
   * The address.
   */
  protected Address address;

  protected AddressTerm(Address address)
  {
    this.address = address;
  }

  /**
   * Return the address to match with.
   */
  public Address getAddress()
  {
    return address;
  }

  /**
   * Match against the argument Address.
   */
  protected boolean match(Address address)
  {
    return address.equals(this.address);
  }

  /**
   * Equality comparison.
   */
  public boolean equals(Object other)
  {
    return ((other instanceof AddressTerm) && 
        ((AddressTerm)other).address.equals(address));
  }

  /**
   * Compute a hashCode for this object.
   */
  public int hashCode()
  {
    return address.hashCode();
  }
  
}
