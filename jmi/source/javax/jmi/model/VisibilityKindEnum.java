/*
  GNU-Classpath Extensions: JMI
  Copyright (C) 2004 Free Software Foundation, Inc.

  For more information on the GNU Classpathx project please mail:
  classpathx-discuss@gnu.org

  This file is part of GNU JMI

  GNU JMI is free software; you can redistribute it and/or modify it
  under the terms of the GNU General Public License as published by the
  Free Software Foundation; either version 2, or (at your option) any
  later version.

  GNU JMI is distributed in the hope that it will be useful, but
  WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
  General Public License for more details.

  You should have received a copy of the GNU General Public License
  along with program; see the file COPYING. If not, write to the Free
  Software Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA
  02111-1307, USA.
*/

package javax.jmi.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.jmi.reflect.RefEnum;

/**
 * 
 */
public class VisibilityKindEnum implements VisibilityKind
{

  private final String literalName;

  /**
   * 
   */
  public static final VisibilityKindEnum PRIVATE_VIS = new VisibilityKindEnum("private_vis");

  /**
   * 
   */
  public static final VisibilityKindEnum PROTECTED_VIS = new VisibilityKindEnum(
      "protected_vis");

  /**
   * 
   */
  public static final VisibilityKindEnum PUBLIC_VIS = new VisibilityKindEnum(
      "public_vis");

  private static List typeName;

  /**
   * @param literalName
   */
  public VisibilityKindEnum(String literalName)
  {
    super();
    this.literalName = literalName;
    ArrayList list = new ArrayList();
    list.add("Model");
    list.add("DirectionKind");
    VisibilityKindEnum.typeName = Collections.unmodifiableList(list);
  }

  /**
   * @return
   * @see javax.jmi.reflect.RefEnum#refTypeName()
   */
  public List refTypeName()
  {
    return VisibilityKindEnum.typeName;
  }

  public String toString()
  {
    return this.literalName;
  }

  public int hashCode()
  {
    return this.literalName.hashCode();
  }

  public boolean equals(Object o)
  {
    boolean result = false;
    if (o instanceof VisibilityKindEnum)
    {
      result = (o == this);
    }
    else
      if (o instanceof DirectionKind)
      {
        result = (o.toString().equals(literalName));
      }
      else
        if (o instanceof RefEnum)
        {
          if (((RefEnum) o).refTypeName().equals(typeName))
          {
            result = o.toString().equals(literalName);
          }
        }
    return result;
  }

  protected Object readResolve() throws java.io.ObjectStreamException
  {
    try
    {
      return forName(literalName);
    }
    catch (IllegalArgumentException iae)
    {
      throw new java.io.InvalidObjectException(iae.getMessage());
    }
  }

  public static VisibilityKind forName(java.lang.String value)
  {
    if (value.equals("private_vis"))
      return VisibilityKindEnum.PRIVATE_VIS;
    if (value.equals("protected_vis"))
      return VisibilityKindEnum.PROTECTED_VIS;
    if (value.equals("public_vis"))
      return VisibilityKindEnum.PUBLIC_VIS;
    throw new IllegalArgumentException("Unknown enumeration value '" + value
        + "' for type 'Model.DirectionKind'");
  }

}

