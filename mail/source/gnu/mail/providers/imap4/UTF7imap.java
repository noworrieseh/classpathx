/*
 * UTF7imap.java
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

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Encodes and decodes text according to the IMAP4rev1 mailbox name
 * encoding scheme.
 *
 * @author <a href="mailto:dog@gnu.org">Chris Burdess</a>
 */
public final class UTF7imap
{

  private static final String US_ASCII = "US-ASCII";

  private static final byte[] src =
  {
    'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 
    'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 
    'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 
    'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 
    'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 
    'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', 
    '8', '9', '+', ','
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
  
  private UTF7imap()
  {
  }

  /**
   * Encode the specified byte array using the modified BASE64 algorithm
   * specified by UTF-7, with further IMAP4rev1 modifications.
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
   * Decode the specified byte array using the modified BASE64 algorithm
   * specified by UTF-7, with further IMAP4rev1 modifications.
   *
   * @param bs the source byte array
   */
  static int[] decode(byte[] bs)
  {
    int[] buffer = new int[bs.length];
    int buflen = 0;
    int si = 0;
    int len = bs.length-si;
    while (len>0)
    {
      byte b0 = dst[bs[si++] & 0xff];
      byte b2 = dst[bs[si++] & 0xff];
      buffer[buflen++] = (b0<<2 & 0xfc | b2>>>4 & 0x3);
      if (len>2)
      {
        b0 = b2;
        b2 = dst[bs[si++] & 0xff];
        buffer[buflen++] = (b0<<4 & 0xf0 | b2>>>2 & 0xf);
        if (len>3)
        {
          b0 = b2;
          b2 = dst[bs[si++] & 0xff];
          buffer[buflen++] = (b0<<6 & 0xc0 | b2 & 0x3f);
        }
      }
      len = bs.length-si;
    }
    int[] bt = new int[buflen];
    System.arraycopy(buffer, 0, bt, 0, buflen);
    return bt;
  }

  /**
   * Encodes the specified name using the UTF-7.imap encoding.
   * See IMAP4rev1 spec, section 5.1.3
   */
  public static String encode(String name)
  {
    try
    {
      StringBuffer buffer = null;
      ByteArrayOutputStream encoderSink = null;
      char[] chars = name.toCharArray();
      boolean encoding = false;
      for (int i=0; i<chars.length; i++)
      {
        char c = chars[i];
        if (c=='&')
        {
          if (buffer==null)
          {
            buffer = new StringBuffer();
            for (int j=0; j<i; j++)
              buffer.append(chars[j]);
          }
          buffer.append('&');
          buffer.append('-');
        }
        if (c<0x1f || c>0x7f)
        {
          // needs encoding
          if (buffer==null)
          {
            buffer = new StringBuffer();
            for (int j=0; j<i; j++)
              buffer.append(chars[j]);
            encoderSink = new ByteArrayOutputStream();
          }
          if (!encoding)
          {
            encoderSink.reset();
            buffer.append('&');
            encoding = true;
          }
          encoderSink.write(((int)c)/0x100);
          encoderSink.write(((int)c)%0x100);
        }
        else if (encoding)
        {
          encoderSink.flush();
          byte[] encoded = encode(encoderSink.toByteArray());
          buffer.append(new String(encoded, US_ASCII));
          buffer.append('-');
          encoding = false;
          if (c!='-')
            buffer.append(c);
        }
        else if (buffer!=null)
          buffer.append(c);
      }
      if (encoding)
      {
        encoderSink.flush();
        byte[] encoded = encode(encoderSink.toByteArray());
        buffer.append(new String(encoded, US_ASCII));
        buffer.append('-');
      }
      if (buffer!=null)
        return buffer.toString();
    }
    catch (IOException e)
    {
      // This should never happen
      throw new RuntimeException(e.getMessage());
    }
    return name;
  }

  /**
   * Decodes the specified name using the UTF-7.imap decoding.
   * See IMAP4rev1 spec, section 5.1.3
   */
  public static String decode(String name)
  {
    StringBuffer buffer = null;
    ByteArrayOutputStream decoderSink = null;
    char[] chars = name.toCharArray();
    boolean encoded = false;
    for (int i=0; i<chars.length; i++)
    {
      char c = chars[i];
      if (c=='&')
      {
        if (buffer==null)
        {
          buffer = new StringBuffer();
          decoderSink = new ByteArrayOutputStream();
          for (int j=0; j<i; j++)
            buffer.append(chars[j]);
        }
        decoderSink.reset();
        encoded = true;
      }
      else if (c=='-' && encoded)
      {
        if (decoderSink.size()==0)
          buffer.append('&');
        else
        {
          int[] decoded = decode(decoderSink.toByteArray());
          for (int j=0; j<decoded.length-1; j+=2)
          {
            int hibyte = decoded[j];
            int lobyte = decoded[j+1];
            int d = (hibyte*0x100) | lobyte;
            buffer.append((char)d);
          }
        }
        encoded = false;
      }
      else if (encoded)
        decoderSink.write((byte)c);
      else if (buffer!=null)
        buffer.append(c);
    }
    if (buffer!=null)
      return buffer.toString();
    return name;
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
        if (decode)
          System.out.println(args[i]+" = "+decode(args[i]));
        else
          System.out.println(args[i]+" = "+encode(args[i]));
      }
    }
  }

}
