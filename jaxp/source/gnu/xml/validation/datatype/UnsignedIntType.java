package gnu.xml.validation.datatype;

import javax.xml.XMLConstants;
import javax.xml.namespace.QName;

/**
 * The XML Schema unsignedInt type.
 *
 * @author <a href='mailto:dog@gnu.org'>Chris Burdess</a>
 */
final class UnsignedIntType
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

  static final String MAX_VALUE = "4294967295";
  static final int LENGTH = MAX_VALUE.length();

  UnsignedIntType()
  {
    super(new QName(XMLConstants.W3C_XML_SCHEMA_NS_URI, "unsignedInt"),
          Type.UNSIGNED_LONG);
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
    boolean compare = false;
    for (int i = 0; i < len; i++)
      {
        if (len - i > LENGTH)
          {
            return false;
          }
        else if (len - i == LENGTH)
          {
            compare = true;
          }
        char c = value.charAt(i);
        if (c >= 0x30 && c <= 0x39)
          {
            if (compare)
              {
                char d = MAX_VALUE.charAt(i);
                if (Character.digit(c, 10) > Character.digit(d, 10))
                  {
                    return false;
                  }
              }
            continue;
          }
        return false;
      }
    return true;
  }
  
}

