/*
 * SharedInputStream.java
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

package javax.mail.internet;

import java.io.InputStream;

/**
 * An InputStream that is backed by data that can be shared by multiple readers
 * may implement this interface. This allows users of such an InputStream to
 * determine the current positionin the InputStream, and to create new
 * InputStreams representing a subset of the data in the original InputStream.
 * The new InputStream will access the same underlying data as the original,
 * without copying the data.
 *
 * @author <a href="mailto:dog@gnu.org">Chris Burdess</a>
 */
public interface SharedInputStream
{

  /**
   * Return the current position in the InputStream, as an offset from the
   * beginning of the InputStream.
   * @return the current position
   */
  public long getPosition();

  /**
   * Return a new InputStream representing a subset of the data from this
   * InputStream, starting at <code>start</code> (inclusive) up to 
   * <code>end</code> (exclusive). <code>start</code> must be non-negative.
   * If <code>end</code> is -1, the new stream ends at the same place
   * as this stream. The returned InputStream will also implement the
   * SharedInputStream interface.
   * @param start the starting position
   * @param end the ending position + 1
   * @return the new stream
   */
  public InputStream newStream(long start, long end);
  
}
