package gnu.xml.validation.datatype;

import javax.xml.XMLConstants;
import javax.xml.namespace.QName;

/**
 * The XML Schema short type.
 *
 * @author <a href='mailto:dog@gnu.org'>Chris Burdess</a>
 */
final class ShortType
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

  static final String MAX_VALUE = "32767";
  static final String MIN_VALUE = "32768";
  static final int LENGTH = MAX_VALUE.length();

  ShortType()
  {
    super(new QName(XMLConstants.W3C_XML_SCHEMA_NS_URI, "short"),
          Type.INT);
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
    int i = 0, off = 0;
    boolean compare = false;
    String compareTo = MAX_VALUE;
    char c = value.charAt(0);
    if (c == '+')
      {
        i++;
      }
    else if (c == '-')
      {
        compareTo = MIN_VALUE;
        i++;
      }
    if (len - i > LENGTH)
      {
        return false;
      }
    else if (len - i == LENGTH)
      {
        compare = true;
      }
    for (; i < len; i++)
      {
        c = value.charAt(i);
        if (c >= 0x30 && c <= 0x39)
          {
            if (compare)
              {
                char d = compareTo.charAt(off);
                if (Character.digit(c, 10) > Character.digit(d, 10))
                  {
                    return false;
                  }
              }
            off++;
            continue;
          }
        return false;
      }
    return true;
  }
  
}

