/*
 * PasswordAuthentication.java
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
 * The class PasswordAuthentication is a data holder that is used by
 * Authenticator.
 * It is simply a repository for a user name and a password.
 *
 * @author <a href="mailto:dog@gnu.org">Chris Burdess</a>
 */
public final class PasswordAuthentication
{

  /*
   * The user name.
   */
  private String userName;

  /*
   * The password.
   */
  private String password;

  /**
   * Initialize a new PasswordAuthentication
   * @param userName the user name
   * @param password The user's password
   */
  public PasswordAuthentication(String userName, String password)
  {
    this.userName = userName;
    this.password = password;
  }

  /**
   * @return the user name
   */
  public String getUserName()
  {
    return userName;
  }

  /**
   * @return the password
   */
  public String getPassword()
  {
    return password;
  }
}
