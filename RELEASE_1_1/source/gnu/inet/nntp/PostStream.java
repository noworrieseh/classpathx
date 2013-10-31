/*
 * $Id: PostStream.java,v 1.5 2004-07-28 09:37:21 dog Exp $
 * Copyright (C) 2002, 2003 The free Software Foundation
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

package gnu.inet.nntp;

import java.io.FilterOutputStream;
import java.io.OutputStream;
import java.io.IOException;

/**
 * A stream to which article contents should be written.
 *
 * @author <a href='mailto:dog@gnu.org'>Chris Burdess</a>
 * @version $Revision: 1.5 $ $Date: 2004-07-28 09:37:21 $
 */
public final class PostStream extends FilterOutputStream
{

  NNTPConnection connection;
  boolean isTakethis;
  byte last;
  
  PostStream (NNTPConnection connection, boolean isTakethis)
  {
    super (connection.out);
    this.connection = connection;
    this.isTakethis = isTakethis;
  }
  
  public void write (int c) throws IOException
  {
    super.write (c);
    last = (byte) c;
  }

  public void write (byte[] bytes, int pos, int len) throws IOException
  {
    super.write (bytes, pos, len);
    if (len > 0)
      {
        last = bytes[pos + len - 1];
      }
  }
  
  /**
   * Close the stream.
   * This calls NNTPConnection.postComplete().
   */
  public void close () throws IOException
  {
    if (last != 0x0d)
      {
        // Need to add LF
        write (0x0d);
      }
    if (isTakethis)
      {
        connection.takethisComplete ();
      }
    else
      {
        connection.postComplete ();
      }
  }
  
}
