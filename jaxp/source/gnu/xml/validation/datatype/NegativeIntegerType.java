package gnu.xml.validation.datatype;

import javax.xml.XMLConstants;
import javax.xml.namespace.QName;

/**
 * The XML Schema negativeInteger type.
 *
 * @author <a href='mailto:dog@gnu.org'>Chris Burdess</a>
 */
final class NegativeIntegerType
  extends AtomicSimpleType
{

  static final int[] CONSTRAINING_FACETS = {
    Facet.TOTAL_DIGITS,
    Facet.FRACTION_DIGITS,
    Facet.PATTERN,
    Facet.WHITESPACE,
    Facet.ENUMERATION,
    Facet.MAX_INCLUSIVE,
    Facet.MAX_EXCLUSIVE,
    Facet.MIN_INCLUSIVE,
    Facet.MIN_EXCLUSIVE
  };

  NegativeIntegerType()
  {
    super(new QName(XMLConstants.W3C_XML_SCHEMA_NS_URI, "negativeInteger"),
          Type.NON_POSITIVE_INTEGER);
  }

  public int[] getConstrainingFacets()
  {
    return CONSTRAINING_FACETS;
  }

  public boolean matches(String value)
  {
    int len = value.length();
    if (len == 0)
      {
        return false;
      }
    for (int i = 0; i < len; i++)
      {
        char c = value.charAt(i);
        if (c == '-')
          {
            if (i == 0)
              {
                continue;
              }
          }
        else if (c >= 0x30 && c <= 0x39)
          {
            continue;
          }
        else
          {
            return false;
          }
      }
    return true;
  }
  
}

