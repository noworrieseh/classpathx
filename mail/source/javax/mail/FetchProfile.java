/*
 * FetchProfile.java
 * Copyright (C) 2002 The Free Software Foundation
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

package javax.mail;

import java.util.ArrayList;

/**
 * Clients use a FetchProfile to list the Message attributes that it 
 * wishes to prefetch from the server for a range of messages.
 * <p>
 * Messages obtained from a Folder are light-weight objects that typically 
 * start off as empty references to the actual messages. Such a Message
 * object is filled in "on-demand" when the appropriate get*() methods are
 * invoked on that particular Message. Certain server-based message access
 * protocols (Ex: IMAP) allow batch fetching of message attributes for a
 * range of messages in a single request. Clients that want to use message
 * attributes for a range of Messages (Example: to display the top-level
 * headers in a headerlist) might want to use the optimization provided by
 * such servers. The FetchProfile allows the client to indicate this desire
 * to the server.
 * <p>
 * Note that implementations are not obligated to support FetchProfiles, since
 * there might be cases where the backend service does not allow easy,
 * efficient fetching of such profiles.
 * <p>
 * Sample code that illustrates the use of a FetchProfile is given below:
 * <pre>
      Message[] msgs = folder.getMessages();
    
      FetchProfile fp = new FetchProfile();
      fp.add(FetchProfile.Item.ENVELOPE);
      fp.add("X-mailer");
      folder.fetch(msgs, fp);
   </pre>
 *
 * @author <a href="mailto:dog@gnu.org">Chris Burdess</a>
 * @version 1.3
 */
public class FetchProfile
{

  /**
   * This inner class is the base class of all items that can be requested
   * in a FetchProfile. The items currently defined here are ENVELOPE,
   * CONTENT_INFO and FLAGS. The UIDFolder interface defines the UID Item
   * as well.
   * <p>
   * Note that this class only has a protected constructor, thereby
   * restricting new Item types to either this class or subclasses.
   * This effectively implements an enumeration of allowed Item types.
   */
  public static class Item
  {

    /**
     * This is the Envelope item.
     * <p>   
     * The Envelope is an aggregration of the common attributes of a Message.
     * Implementations should include the following attributes: 
     * From, To, Cc, Bcc, ReplyTo, Subject and Date.
     * More items may be included as well.
     * <p>
     * For implementations of the IMAP4 protocol (RFC 2060),
     * the Envelope should include the ENVELOPE data item.
     * More items may be included too.
     */
    public static final Item ENVELOPE = new Item ("ENVELOPE");

    /**
     * This item is for fetching information about the content of the message.
     * <p>
     * This includes all the attributes that describe the content of the 
     * message.
     * Implementations should include the following attributes: 
     * ContentType, ContentDisposition, ContentDescription, Size and LineCount. 
     * Other items may be included as well.
     */
    public static final Item CONTENT_INFO = new Item ("CONTENT_INFO");

    /**
     * This is the Flags item.
     */
    public static final Item FLAGS = new Item ("FLAGS");
    
    private String name;

    protected Item (String name)
    {
      this.name = name;
    }

    public String toString ()
    {
      return name;
    }
    
  }


  private ArrayList items = null;
  private ArrayList headers = null;

  /**
   * Create an empty FetchProfile.
   */
  public FetchProfile ()
  {
  }

  /**
   * Add the given special item as one of the attributes to be prefetched.
   * @param item the special item to be fetched
   */
  public void add (Item item)
  {
    if (items == null)
      {
        items = new ArrayList ();
      }
    synchronized (items)
      {
        items.add (item);
      }
  }

  /**
   * Add the specified header-field to the list of attributes to be prefetched.
   * @param header the header to be prefetched
   */
  public void add (String header)
  {
    if (headers == null)
      {
        headers = new ArrayList ();
      }
    synchronized (headers)
      {
        headers.add (header);
      }
  }

  /**
   * Returns true if the fetch profile contains given special item.
   */
  public boolean contains (Item item)
  {
    return (items != null && items.contains (item));
  }

  /**
   * Returns true if the fetch profile contains the given header name.
   */
  public boolean contains(String header)
  {
    return (headers!=null && headers.contains(header));
  }

  /**
   * Get the items set in this profile.
   */
  public Item[] getItems ()
  {
    if (items == null)
      {
        return new Item[0];
      }
    synchronized (items)
      {
        Item[] i = new Item[items.size ()];
        items.toArray (i);
        return i;
      }
  }

  /**
   * Get the names of the header-fields set in this profile.
   */
  public String[] getHeaderNames ()
  {
    if (headers == null)
      {
        return new String[0];
      }
    synchronized (headers)
      {
        String[] h = new String[headers.size ()];
        headers.toArray (h);
        return h;
      }
  }
  
}
