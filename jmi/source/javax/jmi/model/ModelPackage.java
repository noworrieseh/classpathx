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

import javax.jmi.reflect.RefPackage;

public interface ModelPackage extends RefPackage
{
  
  public MultiplicityType createMultiplicityType(int lower, int upper,
                                                 boolean isOrdered,
                                                 boolean isUnique);
  
  public Aliases getAliases();
  
  public AliasTypeClass getAliasType();
  
  public AssociationClass getAssociation();
  
  public AssociationEndClass getAssociationEnd();
  
  public AttachesTo getAttachesTo();
  
  public AttributeClass getAttribute();
  
  public BehavioralFeatureClass getBehavioralFeature();
  
  public CanRaise getCanRaise();
  
  public ClassifierClass getClassifier();
  
  public CollectionTypeClass getCollectionType();
  
  public ConstantClass getConstant();
  
  public Constrains getConstrains();
  
  public ConstraintClass getConstraint();
  
  public Contains getContains();
  
  public DataTypeClass getDataType();
  
  public DependsOn getDependsOn();
  
  public EnumerationTypeClass getEnumerationType();
  
  public Exposes getExposes();
  
  public FeatureClass getFeature();
  
  public GeneralizableElementClass getGeneralizableElement();
  
  public Generalizes getGeneralizes();
  
  public ImportClass getImport();
  
  public IsOfType getIsOfType();
  
  public ModelElementClass getModelElement();
  
  public MofClassClass getMofClass();
  
  public MofExceptionClass getMofException();
  
  public MofPackageClass getMofPackage();
  
  public NamespaceClass getNamespace();
  
  public OperationClass getOperation();
  
  public ParameterClass getParameter();
  
  public PrimitiveTypeClass getPrimitiveType();
  
  public ReferenceClass getReference();
  
  public RefersTo getRefersTo();
  
  public StructuralFeatureClass getStructuralFeature();
  
  public StructureFieldClass getStructureField();
  
  public StructureTypeClass getStructureType();
  
  public TagClass getTag();
  
  public TypedElementClass getTypedElement();;
  
}
