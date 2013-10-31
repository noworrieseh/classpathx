/*
 * $Id: SaslCallbackHandler.java,v 1.3 2004-06-08 19:05:28 dog Exp $
 * Copyright (C) 2002 The Free Software Foundation
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

package gnu.inet.util;

import java.io.IOException;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.UnsupportedCallbackException;

/**
 * A callback handler that can manage username and password callbacks.
 *
 * @author <a href="mailto:dog@gnu.org">Chris Burdess</a>
 * @version $Revision: 1.3 $ $Date: 2004-06-08 19:05:28 $
 */
public final class SaslCallbackHandler implements CallbackHandler
{

  /*
   * The username.
   */
  private final String username;

  /*
   * The password.
   */
  private final String password;

  /**
   * Constructor.
   * @param username the value to respond to Name callbacks with
   * @param password the value to respond to Password callbacks with
   */
  public SaslCallbackHandler (String username, String password)
    {
      this.username = username;
      this.password = password;
    }

  /**
   * Handle callbacks.
   */
  public void handle (Callback[]callbacks)
    throws IOException, UnsupportedCallbackException
    {
      for (int i = 0; i < callbacks.length; i++)
        {
          if (callbacks[i] instanceof NameCallback)
            {
              NameCallback nc = (NameCallback) callbacks[i];
              nc.setName (username);
            }
          else if (callbacks[i] instanceof PasswordCallback)
            {
              PasswordCallback pc = (PasswordCallback) callbacks[i];
              pc.setPassword (password.toCharArray ());
            }
          else
            throw new UnsupportedCallbackException (callbacks[i]);
        }
    }
  
}
