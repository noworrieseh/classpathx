/*
 * IMAPResponse.java
 * Copyright (C) 2003, 2013 The Free Software Foundation
 * 
 * This file is part of GNU Classpath Extensions (classpathx).
 * For more information please visit https://www.gnu.org/software/classpathx/
 *
 * classpathx is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * classpathx is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with classpathx.
 * If not, see <http://www.gnu.org/licenses/>.
 */

package gnu.inet.imap;

import java.util.List;

/**
 * An IMAP4rev1 server response.
 * @version 1.2
 * @author <a href='mailto:dog@gnu.org'>Chris Burdess</a>
 */
class IMAPResponse
{

  /**
   * The untagged response tag.
   */
  static final String UNTAGGED = "*";

  /**
   * The continuation response tag.
   */
  static final String CONTINUATION = "+";

  /**
   * The tag for this response.
   */
  String tag = null;

  /**
   * The response ID.
   */
  String id = null;

  /**
   * The message count (for responses returning counts).
   */
  int count = -1;

  /**
   * The mailbox (for STATUS responses).
   */
  String mailbox = null;

  /**
   * The response code.
   */
  List code = null;

  /**
   * The human-readable text.
   */
  String text;

  public String getTag()
  {
    return tag;
  }

  boolean isTagged()
  {
    return (tag != UNTAGGED && tag != CONTINUATION);
  }

  public boolean isUntagged()
  {
    return (tag == UNTAGGED);
  }

  boolean isContinuation()
  {
    return (tag == CONTINUATION);
  }

  String getID()
  {
    return id;
  }

  int getCount()
  {
    return count;
  }

  List getResponseCode()
  {
    return code;
  }

  String getText()
  {
    return text;
  }

  /**
   * ANSI-coloured toString for debugging.
   */
  String toANSIString()
  {
    StringBuffer buffer = new StringBuffer();
    buffer.append(tag);
    if (count != -1)
      {
        buffer.append(" \u001b[00;31m");
        buffer.append(count);
        buffer.append("\u001b[00m");
      }
    if (!isContinuation())
      {
        buffer.append(" \u001b[01m");
        buffer.append(id);
        buffer.append("\u001b[00m");
      }
    if (mailbox != null)
      {
        buffer.append(" \u001b[00;35m");
        buffer.append(mailbox);
        buffer.append("\u001b[00m");
      }
    if (code != null)
      {
        buffer.append(" \u001b[00;36m");
        buffer.append(code);
        buffer.append("\u001b[00m");
      }
    if (text != null)
      {
        buffer.append(" \u001b[00;33m");
        buffer.append(text);
        buffer.append("\u001b[00m");
      }
    return buffer.toString();
  }

  public String toString()
  {
    StringBuffer buffer = new StringBuffer();
    buffer.append(tag);
    if (count != -1)
      {
        buffer.append(' ');
        buffer.append(count);
      }
    if (!isContinuation())
      {
        buffer.append(' ');
        buffer.append(id);
      }
    if (mailbox != null)
      {
        buffer.append(' ');
        buffer.append(mailbox);
      }
    if (code != null)
      {
        buffer.append(' ');
        buffer.append(code);
      }
    if (text != null)
      {
        buffer.append(' ');
        buffer.append(text);
      }
    return buffer.toString();
  }

}

