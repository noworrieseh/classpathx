/*
 * BASE64.java
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

/**
 * Encodes and decodes text according to the BASE64 encoding.
 *
 * @author <a href="mailto:dog@gnu.org">Chris Burdess</a>
 */
public final class BASE64
{

  private static final byte[] src =
  {
    'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 
    'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 
    'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 
    'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 
    'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 
    'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', 
    '8', '9', '+', '/'
  };
 
  private static final byte[] dst;
  static
  {
    dst = new byte[256];
    for (int i = 0; i<255; i++)
      dst[i] = -1;
    for (int i = 0; i<src.length; i++)
      dst[src[i]] = (byte)i;
    
  }
  
  private BASE64()
  {
  }

  /**
   * Encode the specified byte array using the BASE64 algorithm.
   *
   * @param bs the source byte array
   */
  static byte[] encode(byte[] bs)
  {
    int si = 0, ti = 0; // source/target array indices
    byte[] bt = new byte[((bs.length+2)/3)*4]; // target byte array
    for (; si<bs.length; si+=3)
    {
      int buflen = bs.length-si;
      if (buflen==1)
      {
        byte b = bs[si];
        int i = 0;
        boolean flag = false;
        bt[ti++] = src[b>>>2 & 0x3f];
        bt[ti++] = src[(b<<4 & 0x30) + (i>>>4 & 0xf)];
      }
      else if (buflen==2)
      {
        byte b1 = bs[si], b2 = bs[si+1];
        int i = 0;
        bt[ti++] = src[b1>>>2 & 0x3f];
        bt[ti++] = src[(b1<<4 & 0x30) + (b2>>>4 & 0xf)];
        bt[ti++] = src[(b2<<2 & 0x3c) + (i>>>6 & 0x3)];
      }
      else if (buflen==3)
      {
        byte b1 = bs[si], b2 = bs[si+1], b3 = bs[si+2];
        bt[ti++] = src[b1>>>2 & 0x3f];
        bt[ti++] = src[(b1<<4 & 0x30) + (b2>>>4 & 0xf)];
        bt[ti++] = src[(b2<<2 & 0x3c) + (b3>>>6 & 0x3)];
        bt[ti++] = src[b3 & 0x3f];
      }
    }
    return bt;
  }

  /**
   * Decode the specified byte array using the BASE64 algorithm.
   *
   * @param bs the source byte array
   */
  static byte[] decode(byte[] bs)
  {
    byte[] buffer = new byte[bs.length];
    int buflen = 0;
    int si = 0;
    int len = bs.length-si;
    while (len>0)
    {
      byte b0 = dst[bs[si++] & 0xff];
      byte b2 = dst[bs[si++] & 0xff];
      buffer[buflen++] = (byte)(b0<<2 & 0xfc | b2>>>4 & 0x3);
      if (len>2)
      {
        b0 = b2;
        b2 = dst[bs[si++] & 0xff];
        buffer[buflen++] = (byte)(b0<<4 & 0xf0 | b2>>>2 & 0xf);
        if (len>3)
        {
          b0 = b2;
          b2 = dst[bs[si++] & 0xff];
          buffer[buflen++] = (byte)(b0<<6 & 0xc0 | b2 & 0x3f);
        }
      }
      len = bs.length-si;
    }
    byte[] bt = new byte[buflen];
    System.arraycopy(buffer, 0, bt, 0, buflen);
    return bt;
  }

  public static void main(String[] args)
  {
    boolean decode = false;
    for (int i=0; i<args.length; i++)
    {
      if (args[i].equals("-d"))
        decode = true;
      else
      {
        try
        {
          byte[] in = args[i].getBytes("US-ASCII");
          byte[] out = decode ? decode(in) : encode(in);
          System.out.println(args[i]+" = "+new String(out, "US-ASCII"));
        }
        catch (java.io.UnsupportedEncodingException e)
        {
          e.printStackTrace(System.err);
        }
      }
    }
  }

}
