/*
 * MessageNumberTerm.java
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

import javax.mail.Message;

/**
 * This class implements comparisons for Message numbers.
 *
 * @author <a href="mailto:dog@gnu.org">Chris Burdess</a>
 */
public final class MessageNumberTerm
  extends IntegerComparisonTerm
{

  /**
   * Constructor.
   * @param number the Message number
   */
  public MessageNumberTerm(int number)
  {
    super(EQ, number);
  }

  /**
   * The match method.
   * @param msg the Message number is matched with this Message
   * @param true if the match succeeds, otherwise false
   */
  public boolean match(Message msg)
  {
    try
    {
      return super.match(msg.getMessageNumber());
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
    return (other instanceof MessageNumberTerm && super.equals(other));
  }

}
