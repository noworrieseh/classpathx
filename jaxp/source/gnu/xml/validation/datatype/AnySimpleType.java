package gnu.xml.validation.datatype;

import java.util.Set;
import javax.xml.XMLConstants;
import javax.xml.namespace.QName;

final class AnySimpleType
  extends SimpleType
{
  
  AnySimpleType()
  {
    super(new QName(XMLConstants.W3C_XML_SCHEMA_NS_URI, "anySimpleType"),
          ANY, /* variety */
          (Set) null, /* facets */
          0, /* fundametalFacets */
          (SimpleType) Type.ANY_TYPE, /* baseType */
          null);
  }

  public boolean matches(String value)
  {
    return true;
  }
  
}

