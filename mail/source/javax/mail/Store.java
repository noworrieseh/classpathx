/*
 * Store.java
 * Copyright (C) 2001 dog <dog@dog.net.uk>
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

import java.util.Enumeration;
import java.util.Vector;
import javax.mail.event.FolderEvent;
import javax.mail.event.FolderListener;
import javax.mail.event.StoreEvent;
import javax.mail.event.StoreListener;

/**
 * An abstract class that models a message store and its access protocol,
 * for storing and retrieving messages.
 * Subclasses provide actual implementations.
 * <p>
 * Note that Store extends the Service class, which provides many common 
 * methods for naming stores, connecting to stores, and listening to 
 * connection events.
 */
public abstract class Store 
  extends Service
{

  private Vector storeListeners;
  private Vector folderListeners;

  /**
   * Constructor.
   * @param session Session object for this Store.
   * @param url URLName object to be used for this Store
   */
  protected Store(Session session, URLName url)
  {
    super(session, url);
  }

  /**
   * Returns a Folder object that represents the 'root' of the default 
   * namespace presented to the user by the Store.
   * @exception IllegalStateException if this Store is not connected.
   */
  public abstract Folder getDefaultFolder()
    throws MessagingException;

  /**
   * Return the Folder object corresponding to the given name.
   * Note that a Folder object is returned even if the named folder 
   * does not physically exist on the Store. The <code>exists()</code>
   * method on the folder object indicates whether this folder really exists.
   * <p>
   * Folder objects are not cached by the Store, so invoking this method on 
   * the same name multiple times will return that many distinct Folder 
   * objects.
   * @param name The name of the Folder. In some Stores, <code>name</code>
   * can be an absolute path if it starts with the hierarchy delimiter.
   * Else it is interpreted relative to the 'root' of this namespace.
   * @exception IllegalStateException if this Store is not connected.
   */
  public abstract Folder getFolder(String name)
    throws MessagingException;

  /**
   * Return a closed Folder object, corresponding to the given URLName.
   * The store specified in the given URLName should refer to this Store 
   * object.
   * <p>
   * Implementations of this method may obtain the name of the actual folder
   * using the <code>getFile()</code> method on URLName, and use that name 
   * to create the folder.
   * @param url URLName that denotes a folder
   * @exception IllegalStateException if this Store is not connected.
   */
  public abstract Folder getFolder(URLName url)
    throws MessagingException;

  /**
   * Return a set of folders representing the personal namespaces for the
   * current user. A personal namespace is a set of names that is considered
   * within the personal scope of the authenticated user. Typically, only the
   * authenticated user has access to mail folders in their personal namespace.
   * If an INBOX exists for a user, it must appear within the user's personal
   * namespace. In the typical case, there should be only one personal 
   * namespace for each user in each Store.
   * <p>
   * This implementation returns an array with a single entry containing the
   * return value of the getDefaultFolder method. Subclasses should override
   * this method to return appropriate information.
   */
  public Folder[] getPersonalNamespaces()
    throws MessagingException
  {
    Folder[] folders = new Folder[1];
    folders[0] = getDefaultFolder();
    return folders;
  }

  /**
   * Return a set of folders representing the namespaces for user.
   * The namespaces returned represent the personal namespaces for the user.
   * To access mail folders in the other user's namespace, the currently
   * authenticated user must be explicitly granted access rights. For example,
   * it is common for a manager to grant to their secretary access rights to
   * their mail folders.
   * <p>
   * This implementation returns an empty array. Subclasses should override 
   * this method to return appropriate information.
   */
  public Folder[] getUserNamespaces(String user)
    throws MessagingException
  {
    return new Folder[0];
  }

  /**
   * Return a set of folders representing the shared namespaces.
   * A shared namespace is a namespace that consists of mail folders that 
   * are intended to be shared amongst users and do not exist within a 
   * user's personal namespace.
   * <p>
   * This implementation returns an empty array. Subclasses should override 
   * this method to return appropriate information.
   */
  public Folder[] getSharedNamespaces()
    throws MessagingException
  {
    return new Folder[0];
  }

  // -- Event management --
  
  /*
   * Because the propagation of events of different kinds in the JavaMail
   * API is so haphazard, I have here sacrificed a small time advantage for
   * readability and consistency.
   *
   * All the various propagation methods now call a method with a name based
   * on the eventual listener method name prefixed by 'fire', as is the
   * preferred pattern for usage of the EventListenerList in Swing.
   *
   * Note that all events are currently delivered synchronously, where in
   * Sun's implementation a different thread is used for event delivery.
   * 
   * TODO Examine the impact of this.
   */
  
  // -- Store events --

  /**
   * Add a listener for StoreEvents on this Store.
   */
  public synchronized void addStoreListener(StoreListener l)
  {
    if (storeListeners==null)
      storeListeners = new Vector();
    storeListeners.addElement(l);
  }

  /**
   * Remove a listener for Store events.
   */
  public synchronized void removeStoreListener(StoreListener l)
  {
    if (storeListeners!=null)
      storeListeners.removeElement(l);
  }

  /**
   * Notify all StoreListeners.
   * Store implementations are expected to use this method to broadcast 
   * StoreEvents.
   */
  protected void notifyStoreListeners(int type, String message)
  {
    StoreEvent event = new StoreEvent(this, type, message);
    fireNotification(event);
  }

  /*
   * Propagates a StoreEvent to all registered listeners.
   */
  void fireNotification(StoreEvent event)
  {
    if (storeListeners!=null)
    {
      for (Enumeration e = storeListeners.elements(); 
          e.hasMoreElements(); )
        ((StoreListener)e.nextElement()).notification(event);
    }
  }

  // -- Folder events --

  /**
   * Add a listener for Folder events on any Folder object obtained from this
   * Store. FolderEvents are delivered to FolderListeners on the affected 
   * Folder as well as to FolderListeners on the containing Store.
   */
  public synchronized void addFolderListener(FolderListener l)
  {
    if (folderListeners==null)
      folderListeners = new Vector();
    folderListeners.addElement(l);
  }

  /**
   * Remove a listener for Folder events.
   */
  public synchronized void removeFolderListener(FolderListener l)
  {
    if (folderListeners!=null)
      folderListeners.removeElement(l);
  }

  /**
   * Notify all FolderListeners. Store implementations are expected to use 
   * this method to broadcast Folder events.
   */
  protected void notifyFolderListeners(int type, Folder folder)
  {
    FolderEvent event = new FolderEvent(this, folder, type);
    switch (type)
    {
      case FolderEvent.CREATED:
        fireFolderCreated(event);
        break;
      case FolderEvent.DELETED:
        fireFolderDeleted(event);
        break;
    }
  }

  /**
   * Notify all FolderListeners about the renaming of a folder. Store
   * implementations are expected to use this method to broadcast Folder 
   * events indicating the renaming of folders.
   */
  protected void notifyFolderRenamedListeners(Folder oldFolder, 
      Folder newFolder)
  {
    FolderEvent event = new FolderEvent(this, oldFolder, newFolder, 
        FolderEvent.RENAMED);
    fireFolderRenamed(event);
  }

  /*
   * Propagates a CREATED FolderEvent to all registered listeners.
   */
  void fireFolderCreated(FolderEvent event)
  {
    if (folderListeners!=null)
    {
      for (Enumeration e = folderListeners.elements(); 
          e.hasMoreElements(); )
        ((FolderListener)e.nextElement()).folderCreated(event);
    }
  }

  /*
   * Propagates a DELETED FolderEvent to all registered listeners.
   */
  void fireFolderDeleted(FolderEvent event)
  {
    if (folderListeners!=null)
    {
      for (Enumeration e = folderListeners.elements(); 
          e.hasMoreElements(); )
        ((FolderListener)e.nextElement()).folderDeleted(event);
    }
  }

  /*
   * Propagates a RENAMED FolderEvent to all registered listeners.
   */
  void fireFolderRenamed(FolderEvent event)
  {
    if (folderListeners!=null)
    {
      for (Enumeration e = folderListeners.elements(); 
          e.hasMoreElements(); )
        ((FolderListener)e.nextElement()).folderRenamed(event);
    }
  }

}
