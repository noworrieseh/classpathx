/*
 * ReadOnlyFolderException.java
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

package javax.mail;

/**
 * This exception is thrown when an attempt is made to open a folder 
 * read-write access when the folder is marked read-only.
 * <p>
 * The <code>getMessage()</code> method returns more detailed information 
 * about the error that caused this exception.
 *
 * @author <a href="mailto:dog@gnu.org">Chris Burdess</a>
 */
public class ReadOnlyFolderException
  extends MessagingException
{

  /**
   * The Folder.
   */
  private Folder folder;

  public ReadOnlyFolderException(Folder folder)
  {
    this(folder, null);
  }

  public ReadOnlyFolderException(Folder folder, String message)
  {
    super(message);
    this.folder = folder;
  }

  /**
   * Returns the dead Folder object.
   */
  public Folder getFolder()
  {
    return folder;
  }

}
