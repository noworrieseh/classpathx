/*
 * SaslNTLM.java
 * Copyright (C) 2013 The Free Software Foundation
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

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.net.InetAddress;
import java.net.UnknownHostException;
import javax.security.sasl.SaslClient;
import javax.security.sasl.SaslException;

/**
 * SASL mechanism for NTLM.
 *
 * @author <a href='mailto:dog@gnu.org'>Chris Burdess</a>
 * @version 1.2
 */
public class SaslNTLM
  implements SaslClient
{

  private static final Charset ISO_8859_1 = Charset.forName("ISO-8859-1");

  private static final int STATE_TYPE1MESSAGE = 0;
  private static final int STATE_TYPE3MESSAGE = 1;
  private static final int STATE_DONE = 2;

  private static final String NTLMSSP = "NTLMSSP";
  private static final int NTLM_MESSAGE_TYPE = 0x1000000;

  private static final int FLAG_UNICODE = 1;
  private static final int FLAG_OEM = 2;
  private static final int FLAG_NTLM = 0x200;
  private static final int FLAG_DOMAIN_SUPPLIED = 0x1000;
  private static final int FLAG_WORKSTATION_SUPPLIED = 0x2000;

  private String username;
  private String password;
  private String domain;
  private String hostname;
  private int flags;
  private int state;

  public SaslNTLM(String username, String password)
  {
    int di = username.indexOf('.');
    if (di != -1)
      {
        domain = username.substring(0, di).toUpperCase();
        username = username.substring(di + 1);
      }
    else
      {
        domain = "";
      }
    this.username = username;
    this.password = password;
    try
      {
        InetAddress localhost = InetAddress.getLocalHost();
        hostname = localhost.getCanonicalHostName();
        if (hostname == null)
          {
            hostname = new StringBuilder()
              .append('[')
              .append(localhost.getHostAddress())
              .append(']')
              .toString();
          }
        else
          {
            di = hostname.indexOf('.');
            if (di != -1)
              {
                hostname = hostname.substring(0, di);
              }
          }
      }
    catch (UnknownHostException e)
      {
        hostname = "";
      }
    flags = FLAG_OEM | FLAG_NTLM;
    int dl = domain.length();
    if (dl > 0)
      {
        flags |= FLAG_DOMAIN_SUPPLIED;
        if (dl > 8)
          {
            domain = domain.substring(0, 8);
          }
      }
    int hl = hostname.length();
    if (hl > 0)
      {
        flags |= FLAG_WORKSTATION_SUPPLIED;
        if (hl > 8)
          {
            hostname = hostname.substring(0, 8);
          }
      }
  }

  public String getMechanismName()
  {
    return "NTLM";
  }

  public boolean hasInitialResponse()
  {
    return true;
  }

  public byte[] evaluateChallenge(byte[] challenge)
    throws SaslException
  {
    switch (state)
      {
      case STATE_TYPE1MESSAGE:
        byte[] t1 = new byte[256];
        writeString(NTLMSSP, t1, 0);
        writeLong(NTLM_MESSAGE_TYPE, t1, 8);
        writeLong(flags, t1, 12);
        if (domain.length() > 0)
          {
            writeString(domain, t1, 16);
          }
        if (hostname.length() > 0)
          {
            writeString(hostname, t1, 24);
          }
        state = STATE_TYPE3MESSAGE;
        return t1;
      case STATE_TYPE3MESSAGE:
        byte[] t3 = new byte[256];
        // TODO
        state = STATE_DONE;
        return t3;
      default:
        return null;
      }
  }

  // Write little-endian.
  private static void writeLong(int val, byte[] msg, int off)
  {
    msg[off] = (byte) (val & 0xff);
    msg[off + 1] = (byte) ((val >> 4) & 0xff);
  }

  private static void writeString(String val, byte[] msg, int off)
  {
    byte[] b = val.getBytes(ISO_8859_1);
    System.arraycopy(b, 0, val, off, b.length);
  }

  public boolean isComplete()
  {
    return state == STATE_DONE;
  }

  public byte[] unwrap(byte[] incoming, int off, int len)
    throws SaslException
  {
    byte[] ret = new byte[len - off];
    System.arraycopy(incoming, off, ret, 0, len);
    return ret;
  }

  public byte[] wrap(byte[] outgoing, int off, int len)
    throws SaslException
  {
    byte[] ret = new byte[len - off];
    System.arraycopy(outgoing, off, ret, 0, len);
    return ret;
  }

  public Object getNegotiatedProperty(String name)
  {
    return null;
  }

  public void dispose()
  {
  }
  
}

