package gnu.xml.validation.xmlschema;

import javax.xml.validation.TypeInfoProvider;
import org.w3c.dom.TypeInfo;

/**
 * TypeInfo provider for XML Schema validator handler.
 * This simply delegates to the handler. It wouldn't be required if
 * TypeInfoProvider were an interface instead of an abstract class.
 *
 * @author <a href='mailto:dog@gnu.org'>Chris Burdess</a>
 */
final class XMLSchemaTypeInfoProvider
  extends TypeInfoProvider
{

  final XMLSchemaValidatorHandler handler;

  XMLSchemaTypeInfoProvider(XMLSchemaValidatorHandler handler)
  {
    this.handler = handler;
  }

  public TypeInfo getElementTypeInfo()
  {
    return handler.getElementTypeInfo();
  }

  public TypeInfo getAttributeTypeInfo(int index)
  {
    return handler.getAttributeTypeInfo(index);
  }

  public boolean isIdAttribute(int index)
  {
    return handler.isIdAttribute(index);
  }

  public boolean isSpecified(int index)
  {
    return handler.isSpecified(index);
  }
  
}

