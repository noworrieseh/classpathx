/*
 * UIDFolder.java
 * Copyright(C) 2002 The Free Software Foundation
 * 
 * This file is part of GNU JavaMail, a library.
 * 
 * GNU JavaMail is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 *(at your option) any later version.
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

import java.util.NoSuchElementException;

/**
 * The UIDFolder interface is implemented by Folders that can support the
 * "disconnected" mode of operation, by providing unique-ids for messages in 
 * the folder. This interface is based on the IMAP model for supporting 
 * disconnected operation.
 * <p>
 * A Unique identifier(UID) is a positive long value, assigned to each 
 * message in a specific folder. Unique identifiers are assigned in a strictly 
 * ascending fashion in the mailbox. That is, as each message is added to the 
 * mailbox it is assigned a higher UID than the message(s) which were added 
 * previously. Unique identifiers persist across sessions. This permits a 
 * client to resynchronize its state from a previous session with the server.
 * <p>
 * Associated with every mailbox is a unique identifier validity value. If 
 * unique identifiers from an earlier session fail to persist to this session,
 * the unique identifier validity value must be greater than the one used in 
 * the earlier session.
 * <p>
 * Refer to RFC 2060 http://www.ietf.org/rfc/rfc2060.txt for more information.
 *
 * @author <a href="mailto:dog@gnu.org">Chris Burdess</a>
 * @version 1.3
 */
public interface UIDFolder
{

  /**
   * A fetch profile item for fetching UIDs. 
   * This inner class extends the FetchProfile.Item class to add new 
   * FetchProfile item types, specific to UIDFolders. The only item 
   * currently defined here is the UID item.
   */
  static class FetchProfileItem 
    extends FetchProfile.Item
  {

    /**
     * UID is a fetch profile item that can be included in a 
     * FetchProfile during a fetch request to a Folder. 
     * This item indicates that the UIDs for messages in the specified 
     * range are desired to be prefetched.
     * <p>
     * An example of how a client uses this is below:
     * <pre>
     FetchProfile fp = new FetchProfile();
     fp.add(UIDFolder.FetchProfileItem.UID);
     folder.fetch(msgs, fp);
     </pre>
     */
    public static final FetchProfileItem UID = new FetchProfileItem("UID");

    protected FetchProfileItem(String name)
    {
      super(name);
    }
    
  }

  /**
   * This is a special value that can be used as the end parameter in
   * <code>getMessages(start, end)</code>, to denote the last UID 
   * in this folder.
   */
  long LASTUID = -1L;

  /**
   * Returns the UIDValidity value associated with this folder.
   * <p>
   * Clients typically compare this value against a UIDValidity value 
   * saved from a previous session to insure that any cached UIDs not stale.
   */
  long getUIDValidity()
    throws MessagingException;

  /**
   * Get the Message corresponding to the given UID.
   * If no such message exists, null is returned.
   * @param uid UID for the desired message
   */
  Message getMessageByUID(long uid)
    throws MessagingException;

  /**
   * Get the Messages specified by the given range.
   * The special value LASTUID can be used for the end parameter 
   * to indicate the last available UID.
   * @param start start UID
   * @param end end UID
   */
  Message[] getMessagesByUID(long start, long end)
    throws MessagingException;

  /**
   * Get the Messages specified by the given array of UIDs.
   * If any UID is invalid, null is returned for that entry.
   * <p>
   * Note that the returned array will be of the same size as the specified
   * array of UIDs, and null entries may be present in the array to indicate
   * invalid UIDs.
   * @param uids array of UIDs
   */
  Message[] getMessagesByUID(long[] uids)
    throws MessagingException;

  /**
   * Get the UID for the specified message. 
   * Note that the message must belong to this folder.
   * Else NoSuchElementException is thrown.
   * @param message Message from this folder
   * @return UID for this message
   * @exception NoSuchElementException if the given Message is not in this
   * Folder.
   */
  long getUID(Message message)
    throws MessagingException;
  
}
