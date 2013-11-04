/*
 * LineInputStream.java
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

package gnu.mail.util;

import java.io.*;

/**
 * An input stream that can read lines of input.
 */
public class LineInputStream
  extends FilterInputStream
{

  private char[] line;
  private static final int LF = 10, CR = 13;

  /**
   * Constructor.
   */
  public LineInputStream(InputStream in)
  {
    this(in, 80);
  }
  
  /**
   * Constructor.
   * @param in the underlying input stream
   * @param size the buffer size
   */
  public LineInputStream(InputStream in, int size)
  {
    super(in);
    line = new char[size];
  }

  /**
   * Read a line of input.
   */
  public String readLine()
    throws IOException
  {
    char[] chars = line;
    int len = chars.length;
    int pos = 0;
    int c;
    for (c = in.read(); c!=-1; c = in.read()) 
    {
      if (c==LF)
        break;
      if (c==CR)
      {
        // Peek ahead
        int peek = in.read();
        if (peek!=LF)
        {
          if (!(in instanceof PushbackInputStream))
            in = new PushbackInputStream(in);
          ((PushbackInputStream)in).unread(peek);
        }
        break;
      }
      len--;
      if (len<0)
      {
        chars = new char[pos+line.length];
        len = (chars.length-pos)-1;
        System.arraycopy(line, 0, chars, 0, len+1);
        line = chars;
      }
      chars[pos] = (char)c;
      pos++;
    }
    if (c==-1 && pos==0)
      return null;
    else
      return new String(chars, 0, pos);
  }

}
