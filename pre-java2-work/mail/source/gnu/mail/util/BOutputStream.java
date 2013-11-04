/*
 * BOutputStream.java
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

import java.io.OutputStream;

/**
 * Provides RFC 2047 "B" transfer encoding.
 * See section 4.1.
 */
public class BOutputStream
  extends Base64OutputStream
{

  public BOutputStream(OutputStream out)
  {
    super(out, 0x7fffffff);
  }

  public static int encodedLength(byte[] bytes)
  {
    return ((bytes.length+2)/3)*4;
  }
  
}
