/*
 * Newsgroup.java
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
import java.util.*;
import javax.mail.*;
import javax.mail.event.*;

/**
 * The folder class implementing the NNTP mail protocol.
 *
 * @author dog <dog@dog.net.uk>
 * @author Torgeir Veimo <tv@sevenmountains.no>
 * @version 1.4.1
 */
public class Newsgroup 
extends Folder 
{

  protected String name;
  protected boolean postingAllowed = false;

  protected int first;
  protected int last;
  protected int count;
  protected boolean open = false;

  protected Article[] articles;
  protected Date checkpoint;
  protected Calendar calendar;

  /**
   * Constructor.
   */
  public Newsgroup(NNTPStore store, String name) 
  {
    this(store, name, 0, 0, 0);
  }

  /**
   * Constructor.
   */
  public Newsgroup(NNTPStore store, String name, int count, int first, int last) 
  {
    super(store);
    this.name = name;
    this.count = count;
    this.first = first;
    this.last = last;
  }

  /**
   * Returns the name of this folder.
   */
  public String getName() 
  {
    return name;
  }

  /**
   * Returns the full name of this folder.
   */
  public String getFullName() 
  {
    return getName();
  }

  /**
   * Returns the type of this folder.
   */
  public int getType() 
  throws MessagingException 
  {
    return HOLDS_MESSAGES;
  }

  /**
   * Indicates whether this folder exists.
   */
  public boolean exists() 
  throws MessagingException 
  {
    if (open) 
    return true;
    try 
    {
      open(READ_ONLY);
      close(false);
      return true;
    }
    catch (MessagingException e) 
    {
      return false;
    }
  }

  /**
   * Indicates whether this folder contains any new articles.
   */
  public boolean hasNewMessages() 
  throws MessagingException 
  {
    return getNewMessageCount()>0;
  }

  /**
   * Opens this folder.
   */
  public void open(int mode) 
  throws MessagingException 
  {
    if (!open) 
    {
      if (mode!=READ_ONLY)
      throw new MessagingException("Newsgroup is read-only");
      ((NNTPStore)store).open(this);
      open = true;
      notifyConnectionListeners(ConnectionEvent.OPENED);
    }
  }

  /**
   * Closes this folder.
   */
  public void close(boolean expunge) 
  throws MessagingException 
  {
    if (open) 
    {
      ((NNTPStore)store).close(this);
      open = false;
      notifyConnectionListeners(ConnectionEvent.CLOSED);
    }
  }

  /**
   * Expunges this folder.
   */
  public Message[] expunge() 
  throws MessagingException 
  {
    return new Article[0];
  }

  /**
   * Indicates whether this folder is open.
   */
  public boolean isOpen() 
  {
    return open;
  }

  /**
   * Indicates whether this folder is subscribed.
   */
  public boolean isSubscribed() 
  {
    return ((NNTPStore)store).isSubscribed(getName());
  }

  /**
   * Set whether this folder is subscribed.
   */
  public void setSubscribed(boolean subscribed) 
  {
    ((NNTPStore)store).setSubscribed(name, subscribed);
  }

  /**
   * Indicates whether a given article is seen.
   */
  public boolean isSeen(int num) 
  {
    return ((NNTPStore)store).isSeen(name, num);
  }
	
  /**
   * Set whether a given article is seen.
   */
  public void setSeen(int num, boolean seen) 
  {
    ((NNTPStore)store).setSeen(name, num, seen);
  }
	
  /**
   * Returns the permanent flags for this folder.
   */
  public Flags getPermanentFlags() 
  {
    return new Flags(); 
  }
	
  /**
   * Returns the number of articles in this folder.
   */
  public int getMessageCount() 
  throws MessagingException 
  {
    return getMessages().length;
  }

  /**
   * Returns the articles in this folder.
   */
  public Message[] getMessages() 
  throws MessagingException 
  {
    if (!open)
    throw new MessagingException("Newsgroup is not open");
    NNTPStore s = (NNTPStore)store;
		
    // do we need to check for new articles?
    boolean needNew = false;
    if (checkpoint!=null) 
    {
      if (calendar==null)
      calendar = Calendar.getInstance();
      calendar.setTime(checkpoint);
      calendar.add(Calendar.MINUTE, 1);
      Date d = calendar.getTime();
      needNew = new Date().after(d);
    }
		
    if (articles!=null && needNew && s.useNewNews) 
    {
      Article[] nm = s.getNewArticles(this, checkpoint);
      if (nm.length>0) 
      {
	Article[] m2 = new Article[articles.length+nm.length];
	System.arraycopy(articles, 0, m2, 0, articles.length);
	System.arraycopy(nm, 0, m2, articles.length, nm.length);
	articles = m2;
      }
      checkpoint = new Date();
    } 
    else if (articles==null || !s.useNewNews) 
    {
      articles = s.getArticles(this);
      checkpoint = new Date();
    }
    return articles;
  }
	
  /**
   * Returns the specified article in this folder.
   * Since NNTP articles are not stored in sequential order,
   * the effect is just to reference articles returned by getMessages().
   */
  public Message getMessage(int msgnum) 
  throws MessagingException 
  {
    return getMessages()[msgnum-1];
  }
	
  /**
   * NNTP folders are read-only.
   */
  public void appendMessages(Message articles[]) 
  throws MessagingException 
  {
    throw new MessagingException("Folder is read-only");
  }

  /**
   * Does nothing.
   */
  public void fetch(Message articles[], FetchProfile fetchprofile) 
  throws MessagingException 
  {
  }

  // -- These must be overridden to throw exceptions --

  /**
   * NNTP folders can't have parents.
   */
  public Folder getParent() 
  throws MessagingException 
  {
    throw new MessagingException("Newsgroup can't have a parent");
  }

  /**
   * NNTP folders can't contain subfolders.
   */
  public Folder[] list(String s) 
  throws MessagingException 
  {
    throw new MessagingException("Newsgroups can't contain subfolders");
  }

  /**
   * NNTP folders can't contain subfolders.
   */
  public Folder getFolder(String s) 
  throws MessagingException 
  {
    throw new MessagingException("Newsgroups can't contain subfolders");
  }

  /**
   * NNTP folders can't contain subfolders.
   */
  public char getSeparator() 
  throws MessagingException 
  {
    throw new MessagingException("Newsgroups can't contain subfolders");
  }

  /**
   * NNTP folders can't be created, deleted, or renamed.
   */
  public boolean create(int i) 
  throws MessagingException 
  {
    throw new MessagingException("Newsgroups can't be created");
  }

  /**
   * NNTP folders can't be created, deleted, or renamed.
   */
  public boolean delete(boolean flag) 
  throws MessagingException 
  {
    throw new MessagingException("Newsgroups can't be deleted");
  }

  /**
   * NNTP folders can't be created, deleted, or renamed.
   */
  public boolean renameTo(Folder folder) 
  throws MessagingException 
  {
    throw new MessagingException("Newsgroups can't be renamed");
  }

}
