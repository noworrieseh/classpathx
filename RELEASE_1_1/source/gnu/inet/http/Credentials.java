/*
 * $Id: Credentials.java,v 1.1 2004-07-22 13:23:52 dog Exp $
 * Copyright (C) 2004 The Free Software Foundation
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

package gnu.inet.http;

/**
 * Represents a username/password combination that can be used to
 * authenticate to an HTTP server.
 *
 * @author <a href='mailto:dog@gnu.org'>Chris Burdess</a>
 */
public class Credentials
{

  /**
   * The username.
   */
  private String username;

  /**
   * The password.
   */
  private String password;

  /**
   * Constructor.
   * @param username the username
   * @param password the password
   */
  public Credentials (String username, String password)
  {
    this.username = username;
    this.password = password;
  }

  /**
   * Returns the username.
   */
  public String getUsername ()
  {
    return username;
  }

  /**
   * Returns the password.
   */
  public String getPassword ()
  {
    return password;
  }
  
}
