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


import java.io.IOException;
import java.util.Properties;
import javax.mail.Folder;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.URLName;
import javax.mail.MessagingException;
import javax.mail.event.StoreEvent;


/** an IMAPStore is an abstract connection to an IMAP server.
 * This store implementation differs substantially from Sun's implementation.
 * This store provides only one connection to the IMAP server to all folders.
 * Folders obtain the connection through methods in this store.
 *
 * <p><h4>Authentication</h4>
 * An IMAPStore *must* be authenticated to only one user. The authentication
 * cannot be switched later on. This is a function of IMAP (see
 * rfc2060:6.1.3). It's a pain though (because you can't pool connections)
 * so in future this client will allow connection re-use.</p>
 *
 * <p><h4>Listener events</h4>
 * Because the store's connection goes up and down a lot users might notice some
 * differences in when listeners get fired.
 * </p>
 *
 * @author Nic Ferrier - Tapsell-Ferrier Limited
 */
public class IMAPStore
extends Store
implements ConnectionListener
{

  /** sets the standard for the size of things to download.
   */
  int fetchSize=5000;

  /** the host this store is tied to.
   */
  String m_host;

  /** the port this store is tied to.
   */
  int m_port;

  /** the username this store will authenticate against.
   */
  String m_username;

  /** the password the store uses to authenticate.
   */
  String m_password;

  /** the connection to the store.
   * When not "live" this is null.
   *
   * @see #connectionLock which is used to lock access
   */
  private IMAPConnection m_con=null;

  /** the connection lock.
   * Objects outside the store should use this when accessing the connection.
   */
  Boolean connectionLock=new Boolean(true);

  /** the folder namespace delimiter.
   */
  Character delimitter=null;

  /** the list of all folders available on the IMAP store.
   * This is used by folders to establish existance and other things.
   * It's cached but regenerated when we need it (after a disconnection
   * for example).
   *
   * @see #folderLock the lock for this object
   */
  Folder[] m_allFolders=null;

  /** the object to use to synchronize on instead of the folderlist.
   */
  Boolean folderLock=new Boolean(true);


  /** create an IMAP store object.
   */
  public IMAPStore(Session session,URLName name)
  {
    super(session,name);
  }


  //connection handling

  /** make a connection.
   * This creates connections independant of the state of the class.
   * The method is hidden from the rest of the implementation and final so
   * that it can be inlined.
   *
   * @param store the store which is needing a connection
   * @param host the host which we'll connect to
   * @param port the port running IMAP on the host
   * @param username the username to log in as
   * @param password the password to authenticate with
   */
  private final static IMAPConnection makeConnection(IMAPStore store,String host,int port,
				       String username,String password)
  throws IMAPException,IOException
  {
    IMAPConnection con=null;
    //I'm not sure this should be synched actually... need to think about it
    synchronized(store)
    {
      con=new IMAPConnection(store,host,port,username,password);
    }
    return con;
  }

  /** checkout the store's connection.
   * Classes within the IMAP provider can easily obtain the connection
   * that the store is managing.
   *
   * <p><h4>Ensuring a safe connection</h4>
   * The connection returned from this method is safely connected.
   * That means it's not about to timeout any time soon. We achieve
   * that by sending a NOOP if the connection is currently established.
   * </p>
   *
   * @param folderName the name of a folder to select,
   *  if <code>null</code> no selection action is performed.
   * @return the connection - safely established
   */
  final IMAPConnection getConnection(String folderName)
  throws MessagingException,IMAPException
  {
    //if the connection is established it might be "about to run out"
    //It *might* get dropped between the return of the con and
    //the actual use of it - so we have to send a NOOP.
    //
    //Note that if the connection isn't already established there's
    //no need to NOOP because the server timeout won't be reached
    if(m_con==null)
    protocolConnect(m_host,m_port,m_username,m_password);
    else
    {
      try
      {
	synchronized(connectionLock)
	{
	  m_con.noop();
	}
      }
      catch(IMAPException e)
      {
	//something went wrong which probably means it got disconnected
	//...try and reconnect
	protocolConnect(m_host,m_port,m_username,m_password);
      }
    }
    //select the folder if appropriate
    if(folderName!=null)
    {
      synchronized(connectionLock)
      {
	m_con.select(folderName);
      }
    }
    //now we can return the connection
    return m_con;
  }

  /** get the cached list of all folder names.
   * If the cache is out of date it is refreshed (by making the
   * connection and listing again if necessary).
   */
  final Folder[] getList()
  throws MessagingException,IMAPException
  {
    if(m_allFolders!=null)
    return m_allFolders;
    //otherwise we need to connect and store
    synchronized(folderLock)
    {
      m_allFolders=getDefaultFolder().list();
    }
    //get the delimiter
    if(delimitter==null)
    delimitter=new Character(m_con.getNSDelimitter().charAt(0));
    return m_allFolders;
  }

  /** the finalizer kills the connection if it's open.
   */
  protected void finalize()
  {
    if(m_con!=null)
    {
      m_con.disconnect();
    }
  }

  /** connect to the IMAP server.
   * The host, port, username and password are all cached so a later connection
   * will cause the same details to be used.
   *
   * @param host the server host name, the session property value is used if <code>null</code>
   * @param port the server port, the session property value is used if <code>-1</code>
   * @param username the user to connect as
   * @param password the password to connect with
   * @throws IllegalStateException if the connection is already made
   */
  protected boolean protocolConnect(String host,int port,String username,String password)
  throws MessagingException
  {
    //do some protection of values
    if(host==null)
    host=session.getProperty("mail.imap.host");
    if(port<0)
    {
      try
      {
	port=Integer.parseInt(session.getProperty("mail.imap.port"));
      }
      catch(Exception e)
      {
	//parse error? doesn't matter - we catch errors below
      }
      if(port<0)
      port=143;
    }
    try
    {
      if(m_con!=null)
      throw new IllegalStateException("already connected");
      //connect the store
      synchronized(connectionLock)
      {
	m_con=makeConnection(this,host,port,username,password);
	super.setConnected(true);
	//save the values within the class
	m_host=host;
	m_port=port;
	m_username=username;
	m_password=password;
      }
      return true;
    }
    catch(IOException e)
    {
      throw new MessagingException("couldn't make connection",e);
    }
    catch(IMAPException e)
    {
      throw new MessagingException("protocol error occured",e);
    }
  }


  //javamail implementation

  /** this is like getting a handle to the "authenticated state".
   * The folder returned has no message access but is able to deal with
   * selecting folders and so on.
   *
   * @see IMAPAuthenticated the class which represents the root namespace
   */
  public Folder getDefaultFolder()
  throws MessagingException
  {
    return (Folder)new IMAPAuthenticated(this);
  }

  /** get a folder with the specified name.
   * Folder names beginging with the namespace seperator and foldernames
   * without beginning without the namespace seperator are equivalent.
   */
  public Folder getFolder(String name)
  throws MessagingException
  {
    return (Folder)new IMAPFolder(this,name);
  }

  /** get a folder with the name from the URL.
   */
  public Folder getFolder(URLName urlname)
  throws MessagingException
  {
    return (Folder)new IMAPFolder(this,urlname.getFile());
  }


  //connection listener implementation

  /** called when the connection has been created.
   * The store listeners are notified with a NOTICE and the message.
   */
  public void connected(IMAPConnection con,String message)
  {
    //some usefull debug
    // System.err.println("Listener says we're connected: "+message);
    notifyStoreListeners(StoreEvent.NOTICE,message);
  }

  /** called when the connection has been authenticated.
   * The store listeners are notified with a NOTICE and the message.
   */
  public void authenticated(IMAPConnection con,String message)
  {
    notifyStoreListeners(StoreEvent.NOTICE,message);
  }

  /** called when the connection has died.
   * This is probably due to a server timeout. Store listeners are notified
   * with a NOTICE and the disconnection message.
   *
   * <p><h4>Synchronization</h4>
   * The method synchs the nulling of the connection and the array of
   * folder names.</p>
   *
   * @see #m_con which is set to <code>null</code> when this happens.
   * @see #m_allFolders which is set to <code>null</code> when this happens.
   * @see #folderLock which is the lock used to access the folder list
   */
  public void disconnected(IMAPConnection con,String message)
  {
    synchronized(connectionLock)
    {
      m_con=null;
    }
    synchronized(folderLock)
    {
      m_allFolders=null;
    }
    super.setConnected(false);
    notifyStoreListeners(StoreEvent.NOTICE,message);
  }

  /** called when an alert has been issued from the server.
   * The store listeners recieve an ALERT notification.
   */
  public void alert(IMAPConnection con,String message)
  {
    notifyStoreListeners(StoreEvent.ALERT,message);
  }

  /** called when the server issues a UID validity change.
   * we don't do anything with this yet.
   */
  public void uidvalidityChanged(IMAPConnection con,long newValidity)
  {
    System.out.println("Listener says there is a new uidvalidity: "+newValidity);
  }

}
