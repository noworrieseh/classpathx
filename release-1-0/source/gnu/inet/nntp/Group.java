/*
 * $Id: Group.java,v 1.3 2004-06-09 18:24:40 dog Exp $
 * Copyright (C) 2002 The Free Software Foundation
 * 
 * This file is part of GNU inetlib, a library.
 * 
 * GNU inetlib is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 * GNU inetlib is distributed in the hope that it will be useful,
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

package gnu.inet.nntp;

/**
 * An item in an NNTP newsgroup listing.
 *
 * @author <a href='mailto:dog@gnu.org'>Chris Burdess</a>
 * @version $Revision: 1.3 $ $Date: 2004-06-09 18:24:40 $
 */
public final class Group
{

  String name;
  int last;
  int first;
  boolean canPost;

  Group (String name, int last, int first, boolean canPost)
    {
      this.name = name;
      this.last = last;
      this.first = first;
      this.canPost = canPost;
    }

  /**
   * The name of the newsgroup.
   */
  public String getName ()
    {
      return name;
    }

  /**
   * The number of the last known article currently in the newsgroup.
   */
  public int getLast ()
    {
      return last;
    }

  /**
   * The number of the first article currently in the newsgroup.
   */
  public int getFirst ()
    {
      return first;
    }

  /**
   * True if posting to this newsgroup is allowed.
   */
  public boolean isCanPost ()
    {
      return canPost;
    }

}
