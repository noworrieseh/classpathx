/*
 * ReceivedDateTerm.java
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

import java.util.Date;
import javax.mail.Message;

/**
 * This class implements comparisons for the Message Received date
 *
 * @author <a href="mailto:dog@gnu.org">Chris Burdess</a>
 */
public final class ReceivedDateTerm
  extends DateTerm
{

  /**
   * Constructor.
   * @param comparison the Comparison type
   * @param date the date to be compared
   */
  public ReceivedDateTerm(int comparison, Date date)
  {
    super(comparison, date);
  }

  /**
   * The match method.
   * @param msg the date comparator is applied to this Message's sent date
   * @return true if the comparison succeeds, otherwise false
   */
  public boolean match(Message msg)
  {
    try
    {
      Date date = msg.getReceivedDate();
      if (date!=null)
        return super.match(date);
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
    return (other instanceof ReceivedDateTerm && super.equals(other));
  }

}
