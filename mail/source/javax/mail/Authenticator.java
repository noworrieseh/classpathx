/*
 * Authenticator.java
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

import java.net.InetAddress;

/**
 * The class Authenticator represents an object that knows how to obtain
 * authentication for a network connection.
 * Usually, it will do this by prompting the user for information.
 * <p>
 * Applications use this class by creating a subclass, and registering an
 * instance of that subclass with the session when it is created.
 * When authentication is required, the system will invoke a method on the
 * subclass(like getPasswordAuthentication). The subclass's method can
 * query about the authentication being requested with a number of inherited
 * methods(getRequestingXXX()), and form an appropriate message for the
 * user.
 * <p>
 * All methods that request authentication have a default implementation that
 * fails.
 *
 * @author <a href="mailto:dog@gnu.org">Chris Burdess</a>
 * @version 1.3
 */
public abstract class Authenticator
{

  private String defaultUserName;
  private int requestingPort = -1;
  private String requestingPrompt;
  private String requestingProtocol;
  private InetAddress requestingSite;

  final PasswordAuthentication requestPasswordAuthentication(
      InetAddress requestingSite, 
      int requestingPort, 
      String requestingProtocol, 
      String requestingPrompt, 
      String defaultUserName)
  {
    this.requestingSite = requestingSite;
    this.requestingPort = requestingPort;
    this.requestingProtocol = requestingProtocol;
    this.requestingPrompt = requestingPrompt;
    this.defaultUserName = defaultUserName;
    return getPasswordAuthentication();
  }

  /**
   * Returns the default user name given by the requestor.
   */
  protected final String getDefaultUserName()
  {
    return defaultUserName;
  }

  /**
   * Called when password authentication is needed.
   * Subclasses should override the default implementation, which returns null.
   * <p>
   * Note that if this method uses a dialog to prompt the user for this
   * information, the dialog needs to block until the user supplies the
   * information. This method can not simply return after showing the dialog.
   */
  protected PasswordAuthentication getPasswordAuthentication()
  {
    return null;
  }

  /**
   * Returns the port for the requested connection.
   */
  protected final int getRequestingPort()
  {
    return requestingPort;
  }

  /**
   * Returns the prompt string given by the requestor.
   */
  protected final String getRequestingPrompt()
  {
    return requestingPrompt;
  }

  /**
   * Returns the protocol requesting the connection.
   * Often this will be based on a URLName.
   */
  protected final String getRequestingProtocol()
  {
    return requestingProtocol;
  }

  /**
   * Returns the address of the site requesting authorization,
   * or null if not available.
   */
  protected final InetAddress getRequestingSite()
  {
    return requestingSite;
  }

}
