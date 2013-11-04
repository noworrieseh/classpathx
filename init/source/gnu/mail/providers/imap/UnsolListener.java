/*
  GNU Javamail IMAP provider
  Copyright (C) N.J.Ferrier, Tapsell-Ferrier Limited 2000,2001 for the OJE project

  For more information on this please mail: nferrier@tapsellferrier.co.uk

  This program is free software; you can redistribute it and/or
  modify it under the terms of the GNU Lesser General Public License
  as published by the Free Software Foundation; either version 2
  of the License, or (at your option) any later version.

  This program is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  GNU General Public License for more details.

  You should have received a copy of the GNU Lesser General Public License
  along with this program; if not, write to the Free Software
  Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
*/
package gnu.mail.providers.imap;


/** an interface for listening to interesting connection data.
 * The connection fires these methods on all registered listeners.
 * This gives classes a convieniant way to listen to IMAP specific
 * info that might crop up on the session from time to time and
 * be important.
 *
 * @author Nic Ferrier - Tapsell-Ferrier Limited
 */
public interface UnsolListener
{

  /** notify the listener of the number of messages in the mail box.
   * Outisde changes can occur; due, for example, to an SMTP MTA delivering
   * a new mail to the user's store.
   *
   * <p>The event can be fired because of a SELECT or EXAMINE or can be
   * unsolicited (due to some outside event).</p>
   *
   * @param folderName the folder this applies to
   * @param messagesExisting the number of messages considered to be in the folder.
   */
  public void existsChanged(String folderName,int messagesExisting);

  /** notification of the number of recent messages.
   * According to the IMAP RFC this isn't terribly reliable information (for
   * one reason or another) but it might be usefull.
   *
   * <p>The event can be fired because of a SELECT or EXAMINE command.
   * It's never unsolicited.</p>
   *
   * @param folderName the folder this applies to
   * @param messagesRecent the number of messages in the folder that are "recent".
   */
  public void recentChanged(String folderName,int messagesRecent);

  /** notification of a the minimum flag settings for a folder.
   * The flags specified here can be applied to messages in the folder.
   *
   * <p>The event can be fired because of a SELECT or EXAMINE command.
   * It's never unsolicited.</p>
   *
   * @param folderName the folder this applies to
   * @param flagsResponse the flags as atoms in a list
   */  
  public void flagsSet(String folderName,String flagsResponse);

}
