/*
 * FolderClosedException.java
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

package javax.mail;

/**
 * This exception is thrown when a method is invoked on a Messaging object 
 * and the Folder that owns that object has died due to some reason.
 * <p>
 * Following the exception, the Folder is reset to the "closed" state. All
 * messaging objects owned by the Folder should be considered invalid.
 * The Folder can be reopened using the "open" method to reestablish the 
 * lost connection.
 * <p>
 * The <code>getMessage()</code> method returns more detailed information 
 * about the error that caused this exception.
 *
 * @author <a href="mailto:dog@gnu.org">Chris Burdess</a>
 */
public class FolderClosedException
  extends MessagingException
{

  /*
   * The Folder
   */
  private Folder folder;

  public FolderClosedException(Folder folder)
  {
    this(folder, null);
  }

  public FolderClosedException(Folder folder, String message)
  {
    super(message);
    this.folder = folder;
  }

  /**
   * Returns the dead Folder object
   */
  public Folder getFolder()
  {
    return folder;
  }

}
