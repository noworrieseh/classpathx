package gnu.xml.validation.xmlschema;

import org.w3c.dom.TypeInfo;
import gnu.xml.validation.datatype.SimpleType;
import gnu.xml.validation.datatype.Type;

/**
 * Element type information provided by validation against an XML Schema.
 *
 * @author <a href='mailto:dog@gnu.org'>Chris Burdess</a>
 */
final class XMLSchemaElementTypeInfo
  extends XMLSchemaTypeInfo
{

  final XMLSchema schema;
  final ElementDeclaration decl;
  final Type type;
  boolean nil;

  XMLSchemaElementTypeInfo(XMLSchema schema, ElementDeclaration decl,
                           Type type)
  {
    this.schema = schema;
    this.decl = decl;
    this.type = type;
  }

  public String getTypeName()
  {
    return type.name.getLocalPart();
  }

  public String getTypeNamespace()
  {
    return type.name.getNamespaceURI();
  }

  public boolean isDerivedFrom(String typeNamespace, String typeName,
                               int derivationMethod)
  {
    if (type instanceof SimpleType)
      {
        SimpleType simpleType = (SimpleType) type;
        return simpleTypeIsDerivedFrom(simpleType, typeNamespace, typeName,
                                       derivationMethod);
      }
    else
      {
        // TODO
        return false;
      }
  }
  
}

