/*
 * FromStringTerm.java
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

import javax.mail.Address;
import javax.mail.Message;

/**
 * This class implements string comparisons for the From Address header.
 * <p>
 * Note that this class differs from the FromTerm class in that this class 
 * does comparisons on address strings rather than Address objects. The 
 * string comparisons are case-insensitive.
 */
public final class FromStringTerm
  extends AddressStringTerm
{

  /**
   * Constructor.
   * @param address the address pattern to be compared.
   */
  public FromStringTerm(String pattern)
  {
    super(pattern);
  }

  /**
   * Check whether the address string specified in the constructor is a
   * substring of the From address of this Message.
   * @param msg The comparison is applied to this Message's From address.
   * @return true if the match succeeds, otherwise false.
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
    return (other instanceof FromStringTerm && super.equals(other));
  }

}
