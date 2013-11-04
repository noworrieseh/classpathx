/*
 * RecipientStringTerm.java
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
 * This class implements string comparisons for the Recipient Address headers.
 * <p>
 * Note that this class differs from the RecipientTerm class in that this 
 * class does comparisons on address strings rather than Address objects.
 * The string comparisons are case-insensitive.
 */
public final class RecipientStringTerm
  extends AddressStringTerm
{

  /**
   * The recipient type.
   */
  protected Message.RecipientType type;

  /**
   * Constructor.
   * @param type the recipient type
   * @param address the address pattern to be compared.
   */
  public RecipientStringTerm(Message.RecipientType type, String pattern)
  {
    super(pattern);
    this.type = type;
  }

  /**
   * Return the type of recipient to match with.
   */
  public Message.RecipientType getRecipientType()
  {
    return type;
  }

  /**
   * Check whether the address specified in the constructor is a substring of
   * the recipient address of this Message.
   * @param msg The comparison is applied to this Message's recepient address.
   * @return true if the match succeeds, otherwise false.
   */
  public boolean match(Message msg)
  {
    try
    {
      Address[] addresses = msg.getRecipients(type);
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
    return (other instanceof RecipientStringTerm &&
        ((RecipientStringTerm)other).type.equals(type) &&
        super.equals(other));
  }

  /**
   * Compute a hashCode for this object.
   */
  public int hashCode()
  {
    return type.hashCode() + super.hashCode();
  }
  
}
