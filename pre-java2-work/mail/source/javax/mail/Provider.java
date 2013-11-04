/*
 * Provider.java
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

package javax.mail;

/**
 * The Provider is a class that describes a protocol implementation.
 * The values come from the <code>javamail.providers</code> &amp; 
 * <code>javamail.default.providers</code> resource files.
 *
 * @author <a href="mailto:dog@gnu.org">Chris Burdess</a>
 */
public class Provider
{

  /**
   * This inner class defines the Provider type.
   * Currently, STORE and TRANSPORT are the only two provider types supported.
   */
  public static class Type
  {

    public static final Type STORE = new Type("Store");
    public static final Type TRANSPORT = new Type("Transport");

    private String type;

    private Type(String type)
    {
      this.type = type;
    }
    
  }

  private Type type;
  private String protocol;
  private String className;
  private String vendor;
  private String version;

  Provider(Type type, 
      String protocol, String className, String vendor, String version)
  {
    this.type = type;
    this.protocol = protocol;
    this.className = className;
    this.vendor = vendor;
    this.version = version;
  }

  /**
   * Returns the type of this Provider.
   */
  public Type getType()
  {
    return type;
  }

  /**
   * Returns the protocol supported by this Provider.
   */
  public String getProtocol()
  {
    return protocol;
  }

  /**
   * Returns the name of the class that implements the protocol.
   */
  public String getClassName()
  {
    return className;
  }

  /**
   * Returns name of vendor associated with this implementation.
   * May be null.
   */
  public String getVendor()
  {
    return vendor;
  }

  /**
   * Returns version of this implementation or null if no version.
   */
  public String getVersion()
  {
    return version;
  }

  /**
   * Overrides Object.toString()
   */
  public String toString()
  {
    StringBuffer buffer = new StringBuffer();
    buffer.append("javax.mail.Provider[");
    if (type==Type.STORE)
      buffer.append("STORE,");
    else if (type==Type.TRANSPORT)
      buffer.append("TRANSPORT,");
    buffer.append(protocol);
    buffer.append(',');
    buffer.append(className);
    if (vendor!=null)
    {
      buffer.append(',');
      buffer.append(vendor);
    }
    if (version!=null)
    {
      buffer.append(',');
      buffer.append(version);
    }
    buffer.append("]");
    return buffer.toString();
  }
  
}
