package gnu.xml.validation.xmlschema;

import org.w3c.dom.TypeInfo;
import gnu.xml.validation.datatype.SimpleType;

/**
 * Abstract superclass providing simple type derivation.
 *
 * @author <a href='mailto:dog@gnu.org'>Chris Burdess</a>
 */
abstract class XMLSchemaTypeInfo
  implements TypeInfo
{

  protected boolean simpleTypeIsDerivedFrom(SimpleType simpleType,
                                            String typeNamespace,
                                            String typeName,
                                            int derivationMethod)
  {
    switch (derivationMethod)
      {
      case TypeInfo.DERIVATION_RESTRICTION:
        SimpleType baseType = simpleType.baseType;
        while (baseType != null)
          {
            if (baseType.name.getNamespaceURI().equals(typeNamespace) &&
                baseType.name.getLocalPart().equals(typeName))
              {
                return true;
              }
            baseType = baseType.baseType;
          }
        break;
        // TODO other methods
      }
    return false;
  }
  
}

