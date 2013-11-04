/*
 * AddressStringTerm.java
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
import javax.mail.internet.InternetAddress;

/**
 * This abstract class implements string comparisons for Message addresses.
 * <p>
 * Note that this class differs from the AddressTerm class in that this 
 * class does comparisons on address strings rather than Address objects.
 */
public abstract class AddressStringTerm
  extends StringTerm
{

  /**
   * Constructor.
   * @param pattern the address pattern to be compared.
   */
  protected AddressStringTerm(String pattern)
  {
    super(pattern, true);
  }

  /**
   * Check whether the address pattern specified in the constructor is a
   * substring of the string representation of the given Address object.
   * <p>
   * Note that if the string representation of the given Address object 
   * contains charset or transfer encodings, the encodings must be accounted 
   * for, during the match process.
   * @param a The comparison is applied to this Address object.
   * @return true if the match succeeds, otherwise false.
   */
  protected boolean match(Address a)
  {
    if (a instanceof InternetAddress)
      return super.match(((InternetAddress)a).toUnicodeString());
    else
      return super.match(a.toString());
  }

  /**
   * Equality comparison.
   */
  public boolean equals(Object other)
  {
    return ((other instanceof AddressStringTerm) && super.equals(other));
  }

}
