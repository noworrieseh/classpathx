/*
 * RFC2822OutputStream.java
 * Copyright (C) 2002 The Free Software Foundation
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

package gnu.mail.util;

import java.io.*;

/** 
 * An output stream that ensures that lines of characters in the body are
 * limited to 998 octets (excluding CRLF).
 *
 * This is required by RFC 2822, section 2.3.
 *
 * In order to conform to further requirements of RFC 2822 the underlying
 * stream must be a CRLFOutputStream.
 *
 * @author <a href="mailto:dog@gnu.org">Chris Burdess</a>
 */
public class RFC2822OutputStream
  extends FilterOutputStream
{

  /**
   * The CR octet.
   */
  public static final int CR = 13;

  /**
   * The LF octet.
   */
  public static final int LF = 10;

  /**
   * The number of bytes in the line.
   */
  protected int count;

  /**
   * Constructs an RFC2822 output stream
   * connected to the specified CRLF output stream.
   * @param out the underlying CRLFOutputStream
   */
  public RFC2822OutputStream(CRLFOutputStream out)
  {
    super(out);
    count = 0;
  }

  /**
   * Writes a character to the underlying stream.
   * @exception IOException if an I/O error occurred
   */
  public void write(int ch)
    throws IOException
  {
    if (ch==CR || ch==LF)
    {
      out.write(ch);
      count = 0;
    }
    else
    {
      if (count>998)
      {
        out.write(LF);
        count = 0;
      }
      out.write(ch);
      count++;
    }
  }

  /**
   * Writes a byte array to the underlying stream.
   * @exception IOException if an I/O error occurred
   */
  public void write(byte b[])
    throws IOException
  {
    write(b, 0, b.length);
  }

  /**
   * Writes a portion of a byte array to the underlying stream.
   * @exception IOException if an I/O error occurred
   */
  public void write(byte b[], int off, int len)
    throws IOException
  {
    int d = off;
    len += off;
    for (int i=off; i<len; i++)
    {
      if (b[i]==CR || b[i]==LF)
      {
        out.write(b, d, i-d);
        out.write(LF);
        d = i;
        count = 0;
      }
      else
      {
        if (count>998)
        {
          out.write(LF);
          out.write(b, d, count);
          d = i;
          count = 0;
        }
      }
      count++;
    }
    if (count>0)
      out.write(b, d, count);
  }

}
