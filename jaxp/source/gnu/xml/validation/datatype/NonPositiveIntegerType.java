package gnu.xml.validation.datatype;

import javax.xml.XMLConstants;
import javax.xml.namespace.QName;

/**
 * The XML Schema nonPositiveInteger type.
 *
 * @author <a href='mailto:dog@gnu.org'>Chris Burdess</a>
 */
final class NonPositiveIntegerType
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

  NonPositiveIntegerType()
  {
    super(new QName(XMLConstants.W3C_XML_SCHEMA_NS_URI, "nonPositiveInteger"),
          Type.INTEGER);
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
    boolean positive = true;
    for (int i = 0; i < len; i++)
      {
        char c = value.charAt(i);
        if (c == 0x30)
          {
            continue;
          }
        else if (c >= 0x31 && c <= 0x39)
          {
            if (positive)
              {
                return false;
              }
            continue;
          }
        else if (c == '+' && i == 0)
          {
            continue;
          }
        else if (c == '-' && i == 0)
          {
            positive = false;
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

