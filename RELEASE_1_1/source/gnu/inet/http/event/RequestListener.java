/*
 * $Id: RequestListener.java,v 1.1 2004-07-22 13:23:52 dog Exp $
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

package gnu.inet.http.event;

import java.util.EventListener;

/**
 * A request listener.
 *
 * @author <a href='mailto:dog@gnu.org'>Chris Burdess</a>
 */
public interface RequestListener
extends EventListener
{

  /**
   * Callback invoked when a request is created from the associated
   * connection.
   */
  void requestCreated (RequestEvent event);

  /**
   * Callback invoked when the request has been initialised with all data
   * and before sending this data to the server.
   */
  void requestSending (RequestEvent event);

  /**
   * Callback invoked after all request data has been sent to the server.
   */
  void requestSent (RequestEvent event);
  
}
