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


import gnu.lists.LList;
import gnu.lists.Pair;

import java.io.IOException;
import java.util.Vector;

import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Store;
import javax.mail.MessagingException;


/** this is a special IMAP folder which represents IMAP "authenticated state".
 * Authenticated state is an IMAP state during which no message operations can be
 * done because a folder is not selected. These operations are still possible
 * in "selected state" so the Folder implementation which allows message interaction
 * is an extension of this.
 *
 * @author Nic Ferrier - Tapsell-Ferrier Limited.
 */
class IMAPAuthenticated
extends Folder
{

  /** the list of connection event listeners.
   */
  Vector m_connectionListeners=new Vector();

  /** the list of folder event listeners.
   */
  Vector m_folderListeners=new Vector();

  /** the list of message changed event listeners.
   */
  Vector m_messageChangedListeners=new Vector();

  /** the list of message count event listeners.
   */
  Vector m_messageCountListeners=new Vector();

  /** our copy of the store - cast correctly.
   */
  IMAPStore imapStore;

  /** the folder connection lock.
   */
  Boolean connectionLock;

  /** is this pseudo folder open or not?
   */
  protected boolean isOpen=false;

  /** create the "root" folder on the specified store.
   * The authenticated state.
   */
  IMAPAuthenticated(Store store)
  {
    super(store);
    imapStore=(IMAPStore)store;
    connectionLock=imapStore.connectionLock;
  }


  //the javamail implementation

  /** javamail:: open the folder.
   * This class ignores the mode since it's not usefull.
   * The default setting of mode in this class is:
   * <code>READ_ONLY</code> and it can't be changed.
   */
  public void open(int mode)
  throws MessagingException
  {
    if(isOpen)
    throw new IllegalStateException("folder is alerady open");
    isOpen=true;
    super.mode=mode;
  }

  /** javamail:: close the folder.
   * This causes the folder to consider itself closed.
   *
   * @param exunge this class ignores the expunge since it's not usefull
   */
  public void close(boolean expunge)
  {
    if(!isOpen)
    throw new IllegalStateException("folder is not open");
    isOpen=false;
    super.mode=-1;
  }

  /** get the mode of the current folder.
   */
  public int getMode()
  {
    return super.mode;
  }

  /** get a folder from the top level namespace.
   */
  public Folder getFolder(String foldername)
  throws MessagingException
  {
    IMAPFolder folder=new IMAPFolder(imapStore,foldername);
    return (Folder)folder;
  }

  /** javamail:: does the IMAP folder exist on the store?
   *
   * @return true because authenticated state always exists on an IMAP server
   */
  public boolean exists()
  throws MessagingException
  {
    return true;
  }

  /** javamail:: is the folder open?
   */
  public boolean isOpen()
  {
    return isOpen;
  }

  /** javamail:: get the name of this folder.
   *
   * @return <code>null</code> to indicate that this is the top level.
   */
  public String getName()
  {
    return null;
  }

  /** javamail:: get the full name of this folder.
   *
   * @return the string "/"
   */
  public String getFullName()
  {
    return "/";
  }

  /** get the seperator used for the folder name space.
   */
  public char getSeparator()
  throws MessagingException
  {
    return imapStore.delimitter.charValue();
  }

  /** list all the folders under this folder.
   * This method is actually implemented so that only folders
   * existing under the current folder are listed, in the default
   * folder of course that equates to the entire folder tree.
   */
  public Folder[] list()
  throws MessagingException
  {
    return list("*");
  }

  /** list the folders matching pattern.
   * This method is actually implemented so that only folders
   * existing under the current folder which match the pattern
   * are listed, in the default folder of course that equates to
   * the entire folder tree.
   */
  public Folder[] list(String pattern)
  throws MessagingException
  {
    try
    {
      Pair foldersList=null;
      String name=getName();
      if(name==null)
      name="/";
      //do the connection bit
      synchronized(connectionLock)
      {
	IMAPConnection con=imapStore.getConnection(null);
	//close any currently open folder to take us to authenticated state
	con.close();
	//ask for the list which is, funnily enough, returned as a list...
	foldersList=con.listFolders(name,pattern);
	//set the imap store's delimitter
	if(imapStore.delimitter==null)
	imapStore.delimitter=new Character(con.getNSDelimitter().charAt(0));	
      }
      //now turn the results into an array of folders
      Folder[] result=new Folder[foldersList.length()];
      int index=0;
      //now extract the different list items and make folders out of them
      while(true)
      {
	Pair entry=(Pair)(foldersList.car);
	if(entry!=null)
	result[index++]=new IMAPFolder(imapStore,entry);
	//if this is the end of the list then break out
	if(foldersList.cdr==LList.Empty)
	break;
	//otherwise we can go round again
	foldersList=(Pair)(foldersList.cdr);
      }
      //if the index differs from the size that means we had null entries
      if(index<result.length)
      {
	//resize the array
      }
      return result;
    }
    catch(IMAPException e)
    {
      e.printStackTrace();
    }
    catch(IOException e)
    {
      e.printStackTrace();
    }
    return null;
  }


  //unimplemented javamail implementation

  /** javamail:: get the flags from the connection
   */
  public Flags getPermanentFlags()
  {
    //yet to be implemented
    return null;
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


  //javamail implementation that isn't relevant
  
  public Message getMessage(int msgNumber)
  throws MessagingException
  {
    throw new MessagingException("no folder selected - can't get message");
  }

  /** javamail:: get the message count.
   * I'm not sure whether this ought to return -1 or throw an exception.
   *
   * @return -1 because this can't have messages
   */
  public int getMessageCount()
  throws MessagingException
  {
    // throw new MessagingException("no folder selected - can't get message count");
    return -1;
  }

  /** javamail:: are there new messages?
   * I'm not sure whether this ought to return false or throw an exception.
   *
   * @return false because this can't have messages
   */
  public boolean hasNewMessages()
  throws MessagingException
  {
    // throw new MessagingException("no folder selected - can't get message");
    return false;
  }

  public Folder getParent()
  throws MessagingException
  {
    throw new MessagingException("this is the root folder");
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
