/*
 * BodyPart.java
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
 * This class models a Part that is contained within a Multipart.
 * This is an abstract class. Subclasses provide actual implementations.
 * <p>
 * BodyPart implements the Part interface.
 * Thus, it contains a set of attributes and a "content".
 * 
 * @author <a href="mailto:dog@gnu.org">Chris Burdess</a>
 */
public abstract class BodyPart
  implements Part
{


  /**
   * The Multipart object containing this BodyPart, if known.
   */
  protected Multipart parent;

  /**
   * Return the containing Multipart object, or null if not known.
   */
  public Multipart getParent()
  {
    return parent;
  }

  void setParent(Multipart multipart)
  {
    parent = multipart;
  }

}
