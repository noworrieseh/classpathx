package gnu.xml.validation.xmlschema;

import org.w3c.dom.TypeInfo;
import gnu.xml.validation.datatype.SimpleType;

/**
 * Attribute type information provided by validation against an XML Schema.
 *
 * @author <a href='mailto:dog@gnu.org'>Chris Burdess</a>
 */
final class XMLSchemaAttributeTypeInfo
  extends XMLSchemaTypeInfo
{

  final XMLSchema schema;
  final AttributeDeclaration decl;
  final SimpleType type;
  boolean id;
  final boolean specified;

  XMLSchemaAttributeTypeInfo(XMLSchema schema, AttributeDeclaration decl,
                             boolean specified)
  {
    this.schema = schema;
    this.decl = decl;
    this.specified = specified;
    type = (decl == null) ? null : decl.datatype;
  }

  public String getTypeName()
  {
    if (type == null)
      {
        return "CDATA";
      }
    return type.name.getLocalPart();
  }

  public String getTypeNamespace()
  {
    if (type == null)
      {
        return "";
      }
    return type.name.getNamespaceURI();
  }

  public boolean isDerivedFrom(String typeNamespace, String typeName,
                               int derivationMethod)
  {
    if (type == null)
      {
        return false;
      }
    else
      {
        return simpleTypeIsDerivedFrom(type, typeNamespace, typeName,
                                       derivationMethod);
      }
  }
  
}

