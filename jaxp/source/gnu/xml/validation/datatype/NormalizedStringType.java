package gnu.xml.validation.datatype;

import javax.xml.XMLConstants;
import javax.xml.namespace.QName;

/**
 * The XML Schema normalizedString type.
 *
 * @author <a href='mailto:dog@gnu.org'>Chris Burdess</a>
 */
final class NormalizedStringType
  extends AtomicSimpleType
{

  static final int[] CONSTRAINING_FACETS = {
    Facet.LENGTH,
    Facet.MIN_LENGTH,
    Facet.MAX_LENGTH,
    Facet.PATTERN,
    Facet.ENUMERATION,
    Facet.WHITESPACE
  };

  NormalizedStringType()
  {
    super(new QName(XMLConstants.W3C_XML_SCHEMA_NS_URI, "normalizedString"),
          Type.STRING);
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
        if (c == 0x0a || c == 0x0d || c == 0x09)
          {
            return false;
          }
      }
    return true;
  }
  
}

