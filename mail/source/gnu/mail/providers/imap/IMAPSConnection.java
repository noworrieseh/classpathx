/*
 * IMAPSConnection.java
 * Copyright (C) 2003 Chris Burdess <dog@gnu.org>
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

package gnu.mail.providers.imap;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.net.SocketFactory;
import javax.net.ssl.SSLSocketFactory;

/**
 * The protocol class implementing IMAP4rev1 over TLS.
 *
 * @author <a href='mailto:dog@gnu.org'>Chris Burdess</a>
 * @version 0.1
 */
public class IMAPSConnection extends IMAPConnection
{

	/**
	 * Constructor.
	 */
	public IMAPSConnection(String host, int port)
		throws UnknownHostException, IOException
	{
		super(host, port);
	}
	
	/**
	 * Creates a secure socket.
	 */
	protected Socket createSocket(String host , int port)
		throws UnknownHostException, IOException
	{
		SocketFactory factory = SSLSocketFactory.getDefault();
		return factory.createSocket(host, port);
	}

}
