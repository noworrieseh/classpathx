/*
 * NNTPTransport.java
 * Copyright (C) 1999 dog <dog@dog.net.uk>
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2 of the License, or (at your option) any later version.
 * 
 * You also have permission to link it with the Sun Microsystems, Inc. 
 * JavaMail(tm) extension and run that combination.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 * 
 * You may retrieve the latest version of this library from
 * http://www.dog.net.uk/knife/
 */

package gnu.mail.providers.nntp;

import java.io.*;
import java.net.*;
import java.util.*;
import javax.mail.*;
import javax.mail.event.*;
import javax.mail.internet.*;
import gnu.mail.util.*;
import gnu.mail.treeutil.StatusEvent;
import gnu.mail.treeutil.StatusListener;
import gnu.mail.treeutil.StatusSource;

/**
 * The transport class implementing the NNTP Usenet news protocol.
 *
 * @author dog@dog.net.uk
 * @version 1.4.1
 */
public class NNTPTransport 
extends Transport 
{
	
  protected NNTPStore store;
	
  public NNTPTransport(Session session, URLName urlname) 
  {
    super(session, urlname);
  }
	
  public boolean protocolConnect(String host, int port, String user, String password) 
  throws MessagingException 
  {
    try 
    {
      InetAddress address = InetAddress.getByName(host);
      store = NNTPStore.getStore(address, port);
      if (store==null) 
      {
	store = new NNTPStore(session, url);
	return store.protocolConnect(host, port, user, password);
      }
      return true;
    } 
    catch (UnknownHostException e) 
    {
      throw new MessagingException(e.getMessage(), e);
    }
  }
	
  public void sendMessage(Message message, Address[] addresses) 
  throws MessagingException 
  {
    store.postArticle(message, addresses);
  }
	
}
