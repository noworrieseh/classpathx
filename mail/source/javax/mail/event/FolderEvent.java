/*
 * FolderEvent.java
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

package javax.mail.event;

import javax.mail.Folder;

/**
 * This class models Folder existence events.
 * FolderEvents are delivered to FolderListeners registered on 
 * the affected Folder as well as the containing Store.
 * <p>
 * Service providers vary widely in their ability to notify clients of these
 * events. At a minimum, service providers must notify listeners registered 
 * on the same Store or Folder object on which the operation occurs.
 * Service providers may also notify listeners when changes are made through 
 * operations on other objects in the same virtual machine, or by other 
 * clients in the same or other hosts. Such notifications are not required 
 * and are typically not supported by mail protocols (including IMAP).
 *
 * @author <a href="mailto:dog@gnu.org">Chris Burdess</a>
 */
public class FolderEvent
  extends MailEvent
{

  /**
   * The folder was created.
   */
  public static final int CREATED = 1;

  /**
   * The folder was deleted.
   */
  public static final int DELETED = 2;

  /**
   * The folder was renamed.
   */
  public static final int RENAMED = 3;

  /**
   * The event type.
   */
  protected int type;

  /**
   * The folder the event occurred on.
   */
  protected transient Folder folder;

  /**
   * The folder that represents the new name, in case of a RENAMED event.
   */
  protected transient Folder newFolder;

  /**
   * Constructor.
   * @param source The source of the event
   * @param folder The affected folder
   * @param type The event type
   */
  public FolderEvent(Object source, Folder folder, int type)
  {
    this(source, folder, folder, type);
  }

  /**
   * Constructor. Use for RENAMED events.
   * @param source The source of the event
   * @param oldFolder The folder that is renamed
   * @param newFolder The folder that represents the new name
   * @param type The event type
   */
  public FolderEvent(Object source, Folder oldFolder, Folder newFolder, 
      int type)
  {
    super(source);
    folder = oldFolder;
    this.newFolder = newFolder;
    this.type = type;
  }

  /**
   * Return the type of this event.
   */
  public int getType()
  {
    return type;
  }

  /**
   * Return the affected folder.
   * @see #getNewFolder
   */
  public Folder getFolder()
  {
    return folder;
  }

  /**
   * If this event indicates that a folder is renamed, (i.e, the event type is
   * RENAMED), then this method returns the Folder object representing the new
   * name. The getFolder() method returns the folder that is renamed.
   * @see #getFolder
   */
  public Folder getNewFolder()
  {
    return newFolder;
  }

  /**
   * Invokes the appropriate FolderListener method.
   */
  public void dispatch(Object listener)
  {
    FolderListener l = (FolderListener)listener;
    switch (type)
    {
      case CREATED:
        l.folderCreated(this);
        break;
      case DELETED:
        l.folderDeleted(this);
        break;
      case RENAMED:
        l.folderRenamed(this);
        break;
    }
  }

}
