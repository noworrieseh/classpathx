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


import gnu.lists.Pair;
import java.io.InputStream;
import java.io.IOException;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Store;
import javax.mail.MessagingException;
import javax.mail.FolderNotFoundException;
import javax.mail.event.ConnectionEvent;


/** represents an IMAP mailbox.
 * A mailbox is associated with a store and through a store an IMAP connection.
 * The IMAP requirement that an IMAP connection have only one selected folder
 * is in opposition to JavaMail's concept of having multiple folders open.
 * In this implementation a folder can be "open" even though it is not the selected
 * folder on the imap connection. All operations cause the correct folder to be
 * selected before the operation continues. The connection object tracks the currently
 * selected folder reducing to no-ops the sequential selection of the same folder.
 *
 * <p><h4>Connection events</h4>
 * The javamail spec kinda mandates the use of a connection per folder, partly
 * because connection listeners are part of the Folder class. In this implementation
 * the folder doesn't have it's own connection so at the moment the connection
 * events are fired when the methods <code>open()</code> and <code>close()</code>
 * are called.</p>
 *
 * <p>The DISCONNECTED connection event could be fired by our implementation but we
 * don't do it yet. Sun's implementation also doesn't fire the event.</p>
 *
 * @author Nic Ferrier - Tapsell-Ferrier Limited
 */
public class IMAPFolder
extends IMAPAuthenticated
{

  /** this is the foldername used to create this folder.
   */
  String folderName;

  /** if this was created with a linked list then this stores it.
   */
  Pair creationDetails;

  /** is the folder read only?
   * This is null when we don't know whether the folder is RO or not.
   */
  Boolean isReadOnly=null;


  //constructors

  /** create the folder.
   */
  protected IMAPFolder(Store store,String foldername)
  {
    super(store);
    folderName=foldername;
  }

  /** create the folder with a list specifying the necessary details.
   * This is the form of cons used to create the folder during a
   * LIST operation. The data in the specified linked list is what
   * comes back from the IMAP LIST operation.
   *
   * @see IMAPAuthenticated#list which is the method which uses this cons
   */
  IMAPFolder(IMAPStore store,Pair details)
  {
    super((Store)store);
    creationDetails=details;
    //the first item on the list is the name
    folderName=details.car.toString();
  }

  /** javamail:: open the folder.
   * The connection is obtained in order to establish the read
   * status of this folder.
   *
   * @throws FolderNotFoundException if the folder doesn't exist
   * @throws MessagingException is something else wierd happens
   */
  public void open(int mode)
  throws MessagingException
  {
    if(!exists())
    throw new FolderNotFoundException(folderName+" doesn't exist",this);
    try
    {
      synchronized(connectionLock)
      {
	IMAPConnection con=imapStore.getConnection(folderName);
	//handle RO status only if we haven't got it already
	if(isReadOnly==null)
	isReadOnly=new Boolean(con.isReadOnly());
      }
      if(isReadOnly.booleanValue() && mode==READ_WRITE)
      throw new MessagingException(folderName+" is read only");
      super.open(mode);
      notifyConnectionListeners(ConnectionEvent.OPENED);
    }
    catch(IMAPException e)
    {
      throw new MessagingException("can't open the "+folderName+" due to protocol error.");
    }
  }

  /** javamail:: close the folder.
   * This causes the folder to consider itself closed.
   *
   * @param exunge this class ignores the expunge since it's not
   *   implemented right now
   */
  public void close(boolean expunge)
  {
    isReadOnly=null;
    super.close(expunge);
    notifyConnectionListeners(ConnectionEvent.CLOSED);
  }

  /** javamail:: does the IMAP folder exist on the store?
   * Checks the current list of folders to see if this is a folder
   * that is in existance or not.
   *
   * <p><h4>Connection semantics</h4>
   * This method must use the latest list results. The latest list
   * results are always cached on the store.</p>
   *
   * <p>Javamail specs state that this method can be called on a
   * closed folder. In Sun's implementation the store's IMAP connection
   * is used to read the folder list. In OJE implementation we ask the
   * store for a connection and it connects as needed.</p>
   */
  public boolean exists()
  throws MessagingException
  {
    try
    {
      Folder[] allFolders=imapStore.getList();
      boolean found=false;
      for(int i=0; i<allFolders.length && !found; i++)
      found=allFolders[i].getName().equals(folderName);
      return found;
    }
    catch(IMAPException e)
    {
      throw new MessagingException("connection error");
    }
  }

  /** javamail:: get the full name, includes the root path.
   */
  public String getFullName()
  {
    return folderName;
  }

  /** javamail:: get the folder name.
   */
  public String getName()
  {
    return folderName;
  }

  /** javamail:: get a representation of the specified message.
   * The object returned is (initially) extreemly lightweight.
   */
  public Message getMessage(int msgNumber)
  throws MessagingException
  {
    if(!isOpen)
    throw new IllegalStateException(folderName+" must be opened before"
				    +" getMessage("+msgNumber+") works");
    IMAPMessage msg=new IMAPMessage(imapStore,this,msgNumber);
    return msg;
  }

  /** javamail:: get the number of messages in this folder.
   * If the folder is closed then -1 is returned.
   * If the folder is open then a SELECT is performed on the
   * folder. The SELECT ensures that we know the message count
   * (because the response to the SELECT includes the number
   * of messages).
   */
  public int getMessageCount()
  throws MessagingException
  {
    if(!isOpen)
    return -1;
    try
    {
      int count=-1;
      boolean readOnly=false;
      synchronized(connectionLock)
      {
	IMAPConnection con=imapStore.getConnection(folderName);
	count=con.getMessageCount();
	//handle RO status only if we haven't got it already
	if(isReadOnly==null)
	isReadOnly=new Boolean(con.isReadOnly());
      }
      return count;
    }
    catch(IMAPException e)
    {
      throw new MessagingException("a protocol error occured.");
    }
  }

  /** javamail:: get the number of messages in this folder.
   * If the folder is closed then -1 is returned.
   * If the folder is open then a SELECT is performed on the
   * folder. The SELECT ensures that we know the message count
   * (because the response to the SELECT includes the number
   * of messages).
   */
  public int getNewMessageCount()
  throws MessagingException
  {
    if(!isOpen)
    return -1;
    try
    {
      int count=-1;
      synchronized(connectionLock)
      {
	IMAPConnection con=imapStore.getConnection(folderName);
	count=con.getNewMessageCount();
	//handle RO status only if we haven't got it already
	if(isReadOnly==null)
	isReadOnly=new Boolean(con.isReadOnly());	
      }
      return count;
    }
    catch(IMAPException e)
    {
      throw new MessagingException("a protocol error occured.");
    }
  }


  //yet to implement properly

  /** javamail:: get the flags from the connection
   */
  public Flags getPermanentFlags()
  {
    //yet to be implemented
    return null;
  }

  public Folder getParent()
  throws MessagingException
  {
    throw new MessagingException("we don't do this yet.");
  }

  public int getType()
  throws MessagingException
  {
    return -1;
  }

  public boolean create(int type)
  throws MessagingException
  {
    return false;
  }

  public boolean renameTo(Folder f)
  throws MessagingException
  {
    /* does nothing */
    return false;
  }

  public boolean delete(boolean recursive)
  throws MessagingException
  {
    /* does nothing */
    return false;
  }

  public void appendMessages(Message[] toAppend)
  throws MessagingException
  {
    throw new MessagingException("no folder selected - can't append messages");
  }

  public Message[] expunge()
  throws MessagingException
  {
    throw new MessagingException("no folder selected - no messages to expunge");
  }

}
