package gnu.xml.validation.datatype;

import java.util.Collections;
import java.util.Set;
import javax.xml.XMLConstants;
import javax.xml.namespace.QName;

/**
 * The XML Schema hexBinary type.
 *
 * @author <a href='mailto:dog@gnu.org'>Chris Burdess</a>
 */
final class HexBinaryType
  extends AtomicSimpleType
{

  static final String HEX = "0123456789ABCDEF";

  static final int[] CONSTRAINING_FACETS = {
    Facet.LENGTH,
    Facet.MIN_LENGTH,
    Facet.MAX_LENGTH,
    Facet.PATTERN,
    Facet.ENUMERATION,
    Facet.WHITESPACE
  };

  HexBinaryType()
  {
    super(new QName(XMLConstants.W3C_XML_SCHEMA_NS_URI, "hexBinary"),
          Type.ANY_SIMPLE_TYPE);
  }

  public int[] getConstrainingFacets()
  {
    return CONSTRAINING_FACETS;
  }

  public boolean matches(String value)
  {
    int len = value.length();
    for (int i = 0; i < len; i++)
      {
        char c = value.charAt(i);
        if (HEX.indexOf(c) == -1)
          {
            return false;
          }
      }
    return true;
  }
  
}

