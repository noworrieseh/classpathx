/*
 * Newsrc.java
 * Copyright (C) 1999 dog <dog@dog.net.uk>
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2 of the License, or (at your option) any later version.
 * 
 * You also have permission to link it with the Sun Microsystems, Inc. 
 * JavaMail(tm) extension and run that combination.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 * 
 * You may retrieve the latest version of this library from
 * http://www.dog.net.uk/knife/
 */

package dog.mail.nntp;

import javax.mail.MessagingException;

/**
 * Interface for a .newsrc configuration.
 *
 * @author dog <dog@dog.net.uk>
 * @author Torgeir Veimo <tv@sevenmountains.no>
 * @version 1.3
 */
public interface Newsrc 
{
	
  /**
   * Returns a list currently subscribed newsgroups.
   */
  public Newsgroup[] list() 
  throws MessagingException;

  /**
   * Indicates whether a newsgroup is subscribed in this newsrc.
   */
  public boolean isSubscribed(String newsgroup);

  /**
   * Sets whether a newsgroup is subscribed in this newsrc.
   */
  public void setSubscribed(String newsgroup, boolean subs);

  /**
   * Indicates whether an article is marked as seen in the specified newsgroup.
   */
  public boolean isSeen(String newsgroup, int article); 

  /**
   * Sets whether an article is marked as seen in the specified newsgroup.
   */
  public void setSeen(String newsgroup, int article, boolean seen);

  /**
   * Closes and possibly stores updated SEEN flags for articles.
   */
  public void close() 
  throws MessagingException;

}
