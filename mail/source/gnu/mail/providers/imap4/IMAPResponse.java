/*
 * IMAPResponse.java
 * Copyright (C) 2003 Chris Burdess <dog@gnu.org>
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

package gnu.mail.providers.imap4;

import java.util.List;

/**
 * An IMAP4rev1 server response.
 *
 * @author <a href='mailto:dog@gnu.org'>Chris Burdess</a>
 * @version 0.1
 */
public class IMAPResponse
{

  /**
   * The untagged response tag.
   */
  public static final String UNTAGGED = "*".intern();

  /**
   * The continuation response tag.
   */
  public static final String CONTINUATION = "+".intern();

  /**
   * The tag for this response.
   */
  protected String tag;
  
  /**
   * The response ID.
   */
  protected String id;

  /**
   * The message count (for responses returning counts).
   */
  protected int count = -1;

  /**
   * The mailbox (for STATUS responses).
   */
  protected String mailbox;

  /**
   * The response code.
   */
  protected List code;

  /**
   * The content of a FETCH response.
   */
  protected byte[] content;

  /**
   * The human-readable text.
   */
  protected String text;

  public String getTag()
  {
    return tag;
  }

  public boolean isTagged()
  {
    return (tag!=UNTAGGED && tag!=CONTINUATION);
  }

  public boolean isUntagged()
  {
    return (tag==UNTAGGED);
  }

  public boolean isContinuation()
  {
    return (tag==CONTINUATION);
  }

  public String getID()
  {
    return id;
  }

  public int getCount()
  {
    return count;
  }

  public List getResponseCode()
  {
    return code;
  }

  public byte[] getContent()
  {
    return content;
  }
  
  public String getText()
  {
    return text;
  }

  /**
   * ANSI-coloured toString for debugging.
   */
  public String toString()
  {
    StringBuffer buffer = new StringBuffer();
    buffer.append(tag);
    if (count!=-1)
    {
      buffer.append(" \u001b[00;31m");
      buffer.append(count);
      buffer.append("\u001b[00m");
    }
    buffer.append(" \u001b[01m");
    buffer.append(id);
    buffer.append("\u001b[00m");
    if (mailbox!=null)
    {
      buffer.append(" \u001b[00;35m");
      buffer.append(mailbox);
      buffer.append("\u001b[00m");
    }
    if (code!=null)
    {
      buffer.append(" \u001b[00;36m");
      buffer.append(code);
      buffer.append("\u001b[00m");
    }
    if (content!=null)
    {
      buffer.append(" \u001b[00;31m{");
      buffer.append(content.length);
      buffer.append("}\u001b[00m");
    }
    if (text!=null)
    {
      buffer.append(" \u001b[00;33m");
      buffer.append(text);
      buffer.append("\u001b[00m");
    }
    return buffer.toString();
  }

}
