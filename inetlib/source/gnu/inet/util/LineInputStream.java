/*
 * LineInputStream.java
 * Copyright (C) 2002,2006 The Free Software Foundation
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

package gnu.inet.util;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.IOException;

/**
 * An input stream that can read lines of input.
 *
 * @author <a href="mailto:dog@gnu.org">Chris Burdess</a>
 */
public class LineInputStream
  extends InputStream
{

  /**
   * The underlying input stream.
   */
  protected InputStream in;

  /*
   * Line buffer.
   */
  private ByteArrayOutputStream buf;

  /*
   * Encoding to use when translating bytes to characters.
   */
  private String encoding;

  /*
   * End-of-stream flag.
   */
  private boolean eof;

  /**
   * Whether we can use block reads.
   */
  private final boolean blockReads;

  /**
   * Constructor using the US-ASCII character encoding.
   * @param in the underlying input stream
   */
  public LineInputStream(InputStream in)
  {
    this(in, "US-ASCII");
  }

  /**
   * Constructor.
   * @param in the underlying input stream
   * @param encoding the character encoding to use
   */
  public LineInputStream(InputStream in, String encoding)
  {
    this.in = in;
    buf = new ByteArrayOutputStream();
    this.encoding = encoding;
    eof = false;
    blockReads = in.markSupported();
  }

  public int read()
    throws IOException
  {
    return in.read();
  }

  public int read(byte[] buf)
    throws IOException
  {
    return in.read(buf);
  }

  public int read(byte[] buf, int off, int len)
    throws IOException
  {
    return in.read(buf, off, len);
  }

  /**
   * Read a line of input.
   */
  public String readLine()
    throws IOException
  {
    if (eof)
      {
        return null;
      }
    do
      {
        if (blockReads)
          {
            // Use mark and reset to read chunks of bytes
            final int MIN_LENGTH = 1024;
            int len, pos;

            len = in.available();
            len = (len < MIN_LENGTH) ? MIN_LENGTH : len;
            byte[] b = new byte[len];
            in.mark(len);
            // Read into buffer b
            len = in.read(b, 0, len);
            // Handle EOF
            if (len == -1)
              {
                eof = true;
                if (buf.size() == 0)
                  {
                    return null;
                  }
                else
                  {
                    // We don't care about resetting buf
                    return buf.toString(encoding);
                  }
              }
            // Get index of LF in b
            pos = indexOf(b, len, (byte) 0x0a);
            if (pos != -1)
              {
                // Write pos bytes to buf
                buf.write(b, 0, pos);
                // Reset stream, and read pos + 1 bytes
                in.reset();
                pos += 1;
                while (pos > 0)
                  {
                    len = in.read(b, 0, pos);
                    pos = (len == -1) ? -1 : pos - len;
                  }
                // Return line
                String ret = buf.toString(encoding);
                buf.reset();
                return ret;
              }
            else
              {
                // Append everything to buf and fall through to re-read.
                buf.write(b, 0, len);
              }
          }
        else
          {
            // We must use character reads in order not to read too much
            // from the underlying stream.
            int c = in.read();
            switch (c)
              {
              case -1:
                eof = true;
                if (buf.size() == 0)
                  {
                    return null;
                  }
                // Fall through and return contents of buffer.
              case 0x0a:                // LF
                String ret = buf.toString(encoding);
                buf.reset();
                return ret;
              default:
                buf.write(c);
              }
          }
      }
    while (true);
  }

  private int indexOf(byte[] b, int len, byte c)
  {
    for (int pos = 0; pos < len; pos++)
      {
        if (b[pos] == c)
          {
            return pos;
          }
      }
    return -1;
  }

}

