/*
 * BodyTerm.java
 * Copyright (C) 2002 The Free Software Foundation
 * 
 * This file is part of GNU JavaMail, a library.
 * 
 * GNU JavaMail is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 * GNU JavaMail is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 * As a special exception, if you link this library with other files to
 * produce an executable, this library does not by itself cause the
 * resulting executable to be covered by the GNU General Public License.
 * This exception does not however invalidate any other reasons why the
 * executable file might be covered by the GNU General Public License.
 */

package javax.mail.search;

import javax.mail.*;

/**
 * This class implements searches on Message Body.
 * Body searches are done only for:
 * (1) single-part messages whose primary-type is Text OR
 * (2) multipart/mixed messages whose first body-part's primary-type is Text.
 * In this case, the search is done on the first body-part.
 *
 * @author <a href="mailto:dog@gnu.org">Chris Burdess</a>
 */
public final class BodyTerm
  extends StringTerm
{

  /**
   * Constructor
   * @param pattern The String to search for
   */
  public BodyTerm(String pattern)
  {
    super(pattern);
  }

  /**
   * The match method.
   * @param msg The pattern search is applied on this Message's body
   * @return true if the pattern is found; otherwise false
   */
  public boolean match(Message msg)
  {
    try
    {
      Part part = msg;
      String contentType = part.getContentType();
      if (contentType.regionMatches(true, 0, "text/", 0, 5))
      {
        return super.match((String)part.getContent());
      }
      else if (contentType.regionMatches(true, 0, "multipart/mixed", 0, 15))
      {
        part = ((Multipart)part.getContent()).getBodyPart(0);
        contentType = part.getContentType();
        if (contentType.regionMatches(true, 0, "text/", 0, 5))
          return super.match((String)part.getContent());
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
    return ((other instanceof BodyTerm) && super.equals(other));
  }
  
}
