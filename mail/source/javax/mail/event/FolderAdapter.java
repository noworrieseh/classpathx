/*
 * FolderAdapter.java
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

package javax.mail.event;

/**
 * The adapter which receives Folder events.
 * The methods in this class are empty; this class is provided 
 * as a convenience for easily creating listeners by extending this class 
 * and overriding only the methods of interest.
 *
 * @author <a href="mailto:dog@gnu.org">Chris Burdess</a>
 */
public abstract class FolderAdapter
  implements FolderListener
{

  /**
   * Invoked when a Folder is created.
   */
  public void folderCreated(FolderEvent e)
  {
  }

  /**
   * Invoked when a folder is renamed.
   */
  public void folderRenamed(FolderEvent e)
  {
  }

  /**
   * Invoked when a folder is deleted.
   */
  public void folderDeleted(FolderEvent e)
  {
  }

}
