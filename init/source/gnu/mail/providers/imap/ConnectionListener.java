/*
  GNU Javamail IMAP provider
  Copyright (C) N.J.Ferrier, Tapsell-Ferrier Limited 2000 for the OJE project

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


public interface ConnectionListener
{

  /** the connection has been made.
   *
   * @param con the related connection
   * @param message any message that the server sent
   */
  public void connected(IMAPConnection con,String message);

  /** a login was succesfull.
   *
   * @param con the related connection
   * @param message any message that the server sent
   */
  public void authenticated(IMAPConnection con,String message);

  /** the connection has been dropped.
   * This is caused either by the user requesting logout or by
   * the server deciding it's timeout time.
   *
   * @param con the related connection
   * @param message any message that the server sent
   */
  public void disconnected(IMAPConnection con,String message);

  /** the server sent an ALERT response code.
   *
   * @param con the related connection
   * @param message the alert meassge
   */
  public void alert(IMAPConnection con,String message);

  /** the server has told us that the UIDVALIDITY number has changed.
   *
   * @param con the related connection.
   * @param newValidity the new number.
   */
  public void uidvalidityChanged(IMAPConnection con,long newValidity);

}
