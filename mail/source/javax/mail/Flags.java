/*
 * Flags.java
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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * The Flags class represents the set of flags on a Message.
 * Flags are composed of predefined system flags, and user defined flags.
 *
 * A System flag is represented by the Flags.Flag inner class. 
 * A User defined flag is represented as a String.
 * User flags are case-independent.
 * <p>
 * A set of standard system flags are predefined.
 * Most folder implementations are expected to support these flags.
 * Some implementations may also support arbitrary user-defined flags.
 * The getPermanentFlags method on a Folder returns a Flags object
 * that holds all the flags that are supported by that folder
 * implementation.
 * <p>
 * A Flags object is serializable so that (for example) the use of Flags 
 * objects in search terms can be serialized along with the search terms.
 * <p>
 * The below code sample illustrates how to set, examine and get 
 * the flags for a message.
 * <pre>
 Message m = folder.getMessage(1);
 m.setFlag(Flags.Flag.DELETED, true); // set the DELETED flag

 // Check if DELETED flag is set of this message
 if (m.isSet(Flags.Flag.DELETED))
    System.out.println("DELETED message");

 // Examine ALL system flags for this message
 Flags flags = m.getFlags();
 Flags.Flag[] sf = flags.getSystemFlags();
 for (int i = 0; i < sf.length; i++) {
    if (sf[i]==Flags.Flag.DELETED)
            System.out.println("DELETED message");
    else if (sf[i]==Flags.Flag.SEEN)
            System.out.println("SEEN message");
      ......
      ......
 }
 </pre>
 *
 * @author <a href="mailto:dog@gnu.org">Chris Burdess</a>
 * @version 1.3
 */
public class Flags
  implements Cloneable, Serializable
{

  /**
   * This inner class represents an individual system flag.
   * A set of standard system flag objects are predefined here.
   */
  public static final class Flag
  {

    /*
     * This holds the reverse mappings for flag bits to
     * flag objects.
     * It is used internally by the Flags class.
     */
    private static final HashMap flag2flag = new HashMap (7);
    
    /**
     * This message has been answered.
     * This flag is set by clients to indicate that this message 
     * has been answered to.
     */
    public static final Flag ANSWERED = new Flag (0x00000001);

    /**
     * This message is marked deleted.
     * Clients set this flag to mark a message as deleted.
     * The expunge operation on a folder removes all messages in that
     * folder that are marked for deletion.
     */
    public static final Flag DELETED = new Flag (0x00000002);

    /**
     * This message is a draft.
     * This flag is set by clients to indicate that the message is
     * a draft message.
     */
    public static final Flag DRAFT = new Flag (0x00000004);

    /**
     * This message is flagged.
     * No semantic is defined for this flag. Clients alter this flag.
     */
    public static final Flag FLAGGED = new Flag (0x00000008);

    /**
     * This message is recent.
     * Folder implementations set this flag to indicate that this message
     * is new to this folder, that is, it has arrived since the last time
     * this folder was opened.
     * <p>
     * Clients cannot alter this flag.
     */
    public static final Flag RECENT = new Flag (0x00000010);

    /**
     * This message is seen.
     * This flag is implicitly set by the implementation when this 
     * Message's content is returned to the client in some form. The
     * <code>getInputStream</code> and <code>getContent</code> methods 
     * on Message cause this flag to be set.
     * <p>
     * Clients can alter this flag.
     */
    public static final Flag SEEN = new Flag (0x00000020);

    /**
     * A special flag that indicates that this folder supports 
     * user defined flags.
     * <p>
     * The implementation sets this flag.
     * Clients cannot alter this flag but can use it to determine if
     * a folder supports user defined flags by using
     * <code>folder.getPermanentFlags().contains(Flags.Flag.USER)</code>.
     */
    public static final Flag USER = new Flag (0x80000000);

    private int flag;

    /*
     * Constructor.
     */
    private Flag (int flag)
    {
      this.flag = flag;
      flag2flag.put (new Integer (flag), this);
    }
    
  }

  private int systemFlags;

  private HashMap userFlags;
  
  /**
   * Construct an empty Flags object.
   */
  public Flags ()
  {
    systemFlags = 0;
    userFlags = null;
  }

  /**
   * Construct a Flags object initialized with the given flags.
   * @param flags the flags for initialization
   */
  public Flags (Flags flags)
  {
    systemFlags = flags.systemFlags;
    if (flags.userFlags != null)
      {
        userFlags = (HashMap) flags.userFlags.clone ();
      }
    else
      {
        userFlags = null;
      }
  }

  /**
   * Construct a Flags object initialized with the given system flag.
   * @param flag the flag for initialization
   */
  public Flags (Flag flag)
  {
    systemFlags = systemFlags | flag.flag;
    userFlags = null;
  }

  /**
   * Construct a Flags object initialized with the given user flag.
   * @param flag the flag for initialization
   */
  public Flags (String flag)
  {
    systemFlags = 0;
    userFlags = new HashMap (1);
    userFlags.put (flag.toLowerCase (), flag);
  }

  /**
   * Add the specified system flag to this Flags object.
   * @param flag the flag to add
   */
  public void add (Flag flag)
  {
    systemFlags = systemFlags | flag.flag;
  }

  /**
   * Add the specified user flag to this Flags object.
   * @param flag the flag to add
   */
  public void add (String flag)
  {
    if (userFlags == null)
      {
        userFlags = new HashMap (1);
      }
    synchronized (userFlags)
      {
        userFlags.put (flag.toLowerCase (), flag);
      }
  }

  /**
   * Add all the flags in the given Flags object to this Flags object.
   * @param flags the Flags object to add flags from
   */
  public void add (Flags flags)
  {
    systemFlags = systemFlags | flags.systemFlags;
    if (flags.userFlags != null)
      {
        synchronized (flags.userFlags)
          {
            if (userFlags == null)
              {
                userFlags = new HashMap (flags.userFlags);
              }
            else
              {
                synchronized (userFlags)
                  {
                    userFlags.putAll (flags.userFlags);
                  }
              }
          }
      }
  }

  /**
   * Remove the specified system flag from this Flags object.
   * @param flag the flag to be removed
   */
  public void remove (Flag flag)
  {
    systemFlags = systemFlags & ~flag.flag;
  }

  /**
   * Remove the specified user flag from this Flags object.
   * @param flag the flag to be removed
   */
  public void remove (String flag)
  {
    if (userFlags != null)
      {
        synchronized (userFlags)
          {
            userFlags.remove (flag.toLowerCase ());
          }
      }
  }

  /**
   * Remove all flags in the given Flags object from this Flags object.
   * @param flags the flags to be removed
   */
  public void remove (Flags flags)
  {
    systemFlags = systemFlags & ~flags.systemFlags;
    if (userFlags != null && flags.userFlags != null)
      {
        synchronized (flags.userFlags)
          {
            synchronized (userFlags)
              {
                for (Iterator i = flags.userFlags.keySet ().iterator ();
                     i.hasNext (); )
                  {
                    userFlags.remove (i.next ());
                  }
              }
          }
      }
  }

  /**
   * Indicates whether the specified system flag is present 
   * in this Flags object.
   */
  public boolean contains (Flag flag)
  {
    return (systemFlags & flag.flag) != 0;
  }

  /**
   * Indicates whether the specified user flag is present 
   * in this Flags object.
   */
  public boolean contains (String flag)
  {
    if (userFlags == null)
      {
        return false;
      }
    return userFlags.containsKey (flag.toLowerCase ());
  }

  /**
   * Indicates whether all the flags in the specified Flags object 
   * are present in this Flags object.
   */
  public boolean contains (Flags flags)
  {
    if ((systemFlags & flags.systemFlags) == 0)
      {
        return false;
      }
    if (flags.userFlags != null)
      {
        if (userFlags == null)
          {
            return false;
          }
        synchronized (userFlags)
          {
            String[] fuf = flags.getUserFlags ();
            for (int i = 0; i < fuf.length; i++)
              {
                if (!userFlags.containsKey (fuf[i].toLowerCase ()))
                  {
                    return false;
                  }
              }
          }
    }
    return true;
  }

  /**
   * Indicates whether the two Flags objects are equal.
   */
  public boolean equals (Object other)
  {
    if (other == this)
      {
        return true;
      }
    if (!(other instanceof Flags))
      {
        return false;
      }
    Flags flags = (Flags) other;
    if (flags.systemFlags != systemFlags)
      {
        return false;
      }
    if (flags.userFlags == null && userFlags == null)
      {
        return true;
      }
    return (flags.userFlags != null && userFlags != null &&
            flags.userFlags.equals (userFlags));
  }

  /**
   * Returns a hash code for this object.
   */
  public int hashCode ()
  {
    int hashCode = systemFlags;
    if (userFlags != null)
      {
        hashCode += userFlags.hashCode ();
      }
    return hashCode;
  }

  /**
   * Return all the system flags in this Flags object.
   * Returns an array of size zero if no flags are set.
   */
  public Flag[] getSystemFlags ()
  {
    ArrayList acc = new ArrayList (7);
    for (Iterator i = Flag.flag2flag.keySet ().iterator (); i.hasNext (); )
      {
        Integer flag = (Integer) i.next ();
        if ((systemFlags & flag.intValue ()) != 0)
          {
            acc.add (Flag.flag2flag.get (flag));
          }
      }
    Flag[] f = new Flag[acc.size ()];
    acc.toArray (f);
    return f;
  }

  /**
   * Return all the user flags in this Flags object.
   * Returns an array of size zero if no flags are set.
   */    
  public String[] getUserFlags ()
  {
    if (userFlags == null)
      {
        return new String[0];
      }
    else
      {
        synchronized (userFlags)
          {
            String[] f = new String[userFlags.size ()];
            int index = 0;
            for (Iterator i = userFlags.keySet ().iterator (); i.hasNext (); )
              {
                f[index++] = (String) userFlags.get (i.next ());
              }
            return f;
          }
      }
  }

  /**
   * Returns a clone of this Flags object.
   */
  public Object clone ()
  {
    return new Flags (this);
  }

}
