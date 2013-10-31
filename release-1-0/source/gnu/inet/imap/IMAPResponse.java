/*
 * $Id: IMAPResponse.java,v 1.3 2004-06-08 19:05:28 dog Exp $
 * Copyright (C) 2003 The Free Software Foundation
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

package gnu.inet.imap;

import java.util.List;

/**
 * An IMAP4rev1 server response.
 *
 * @author <a href='mailto:dog@gnu.org'>Chris Burdess</a>
 * @version $Revision: 1.3 $ $Date: 2004-06-08 19:05:28 $
 */
public class IMAPResponse
{

  /**
   * The untagged response tag.
   */
  public static final String UNTAGGED = "*".intern ();

  /**
   * The continuation response tag.
   */
  public static final String CONTINUATION = "+".intern ();

  /**
   * The tag for this response.
   */
  protected String tag = null;

  /**
   * The response ID.
   */
  protected String id = null;

  /**
   * The message count (for responses returning counts).
   */
  protected int count = -1;

  /**
   * The mailbox (for STATUS responses).
   */
  protected String mailbox = null;

  /**
   * The response code.
   */
  protected List code = null;

  /**
   * The human-readable text.
   */
  protected String text;

  public String getTag ()
  {
    return tag;
  }

  public boolean isTagged ()
  {
    return (tag != UNTAGGED && tag != CONTINUATION);
  }

  public boolean isUntagged ()
  {
    return (tag == UNTAGGED);
  }

  public boolean isContinuation ()
  {
    return (tag == CONTINUATION);
  }

  public String getID ()
  {
    return id;
  }

  public int getCount ()
  {
    return count;
  }

  public List getResponseCode ()
  {
    return code;
  }

  public String getText ()
  {
    return text;
  }

  /**
   * ANSI-coloured toString for debugging.
   */
  public String toANSIString ()
  {
    StringBuffer buffer = new StringBuffer ();
    buffer.append (tag);
    if (count != -1)
    {
      buffer.append (" \u001b[00;31m");
      buffer.append (count);
      buffer.append ("\u001b[00m");
    }
    if (!isContinuation ())
    {
      buffer.append (" \u001b[01m");
      buffer.append (id);
      buffer.append ("\u001b[00m");
    }
    if (mailbox != null)
    {
      buffer.append (" \u001b[00;35m");
      buffer.append (mailbox);
      buffer.append ("\u001b[00m");
    }
    if (code != null)
    {
      buffer.append (" \u001b[00;36m");
      buffer.append (code);
      buffer.append ("\u001b[00m");
    }
    if (text != null)
    {
      buffer.append (" \u001b[00;33m");
      buffer.append (text);
      buffer.append ("\u001b[00m");
    }
    return buffer.toString ();
  }

  public String toString ()
  {
    StringBuffer buffer = new StringBuffer ();
    buffer.append (tag);
    if (count != -1)
    {
      buffer.append (' ');
      buffer.append (count);
    }
    if (!isContinuation ())
    {
      buffer.append (' ');
      buffer.append (id);
    }
    if (mailbox != null)
    {
      buffer.append (' ');
      buffer.append (mailbox);
    }
    if (code != null)
    {
      buffer.append (' ');
      buffer.append (code);
    }
    if (text != null)
    {
      buffer.append (' ');
      buffer.append (text);
    }
    return buffer.toString ();
  }

}
