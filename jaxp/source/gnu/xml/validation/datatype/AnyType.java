package gnu.xml.validation.datatype;

import javax.xml.XMLConstants;
import javax.xml.namespace.QName;

final class AnyType
  extends SimpleType
{
  
  AnyType()
  {
    super(new QName(XMLConstants.W3C_XML_SCHEMA_NS_URI, "anyType"),
          ANY, /* variety */
          null, /* facets */
          0, /* fundamentalFacets */
          null, /* baseType */
          null);
  }

  public boolean matches(String value)
  {
    return true;
  }
  
}

