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

import java.util.Collection;
import java.util.List;
import javax.jmi.reflect.RefObject;

public interface ModelElement extends RefObject
{
  
  public String ALLDEP = "all";
  
  public String CONSTRAINEDELEMENTSDEP = "constrained elements";
  
  public String CONSTRAINTDEP = "constraint";
  
  public String CONTAINERDEP = "container";
  
  public String CONTENTSDEP = "contents";
  
  public String IMPORTDEP = "import";
  
  public String INDIRECTDEP = "indirect";
  
  public String REFERENCEDENDSDEP = "referenced end";
  
  public String SIGNATUREDEP = "signature";
  
  public String SPECIALIZATIONDEP = "specialization";
  
  public String TAGGEDELEMENTSDEP = "tagged elements";
  
  public String TYPEDEFINITIONDEP = "type definition";
  
  public Collection findRequiredElements(Collection kinds, boolean recursive);
  
  public String getAnnotation();
  
  public Collection getConstraints();
  
  public NameSpace getContainer();
  
  public String getName();
  
  public List getQualifiedName();
  
  public Collection getRequiredElements();
  
  public boolean isFrozen();
  
  public boolean isRequiredBecause(ModelElement otherElement, String[] reason);
  
  public boolean isVisible(ModelElement otherElement);
  
  public void setAnnotation(String newValue);
  
  public void setContainer(NameSpace newValue);
  
  public void setName(String newValue);
  
}
