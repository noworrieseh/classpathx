package gnu.xml.validation.datatype;

import javax.xml.XMLConstants;
import javax.xml.namespace.QName;

/**
 * The XML Schema gYearMonth type.
 *
 * @author <a href='mailto:dog@gnu.org'>Chris Burdess</a>
 */
final class GYearMonthType
  extends AtomicSimpleType
{

  static final int[] CONSTRAINING_FACETS = {
    Facet.PATTERN,
    Facet.ENUMERATION,
    Facet.WHITESPACE,
    Facet.MAX_INCLUSIVE,
    Facet.MAX_EXCLUSIVE,
    Facet.MIN_INCLUSIVE,
    Facet.MIN_EXCLUSIVE
  };

  GYearMonthType()
  {
    super(new QName(XMLConstants.W3C_XML_SCHEMA_NS_URI, "gYearMonth"),
          Type.ANY_SIMPLE_TYPE);
  }

  public int[] getConstrainingFacets()
  {
    return CONSTRAINING_FACETS;
  }

  public boolean matches(String value)
  {
    int len = value.length();
    int state = 0;
    int start = 0;
    for (int i = 0; i < len; i++)
      {
        char c = value.charAt(i);
        if (c == '-' && i == 0)
          {
            start++;
            continue;
          }
        if (c >= 0x30 && c <= 0x39)
          {
            continue;
          }
        switch (state)
          {
          case 0: // year
            if (c == '-')
              {
                String year = value.substring(start, i);
                if (year.length() < 4 || Integer.parseInt(year) == 0)
                  {
                    return false;
                  }
                state = 1;
                start = i + 1;
                continue;
              }
            break;
          }
        return false;
      }
    switch (state)
      {
      case 1: // month
        if (len - start != 2)
          {
            return false;
          }
        break;
      default:
        return false;
      }
    return true;
  }
  
}

