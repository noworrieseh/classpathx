/*
 * Header.java
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

package javax.mail;

/**
 * The Header class stores a name/value pair to represent headers.
 */
public class Header
{

  /**
   * The name.
   */
  private String name;

  /**
   * The value.
   */
  private String value;

  /**
   * Construct a Header object.
   * @param name name of the header
   * @param value value of the header
   */
  public Header(String name, String value)
  {
    this.name = name;
    this.value = value;
  }

  /**
   * Returns the name of this header.
   */
  public String getName()
  {
    return name;
  }

  /**
   * Returns the value of this header.
   */
  public String getValue()
  {
    return value;
  }

}
