package gnu.xml.validation.datatype;

import javax.xml.XMLConstants;
import javax.xml.namespace.QName;

/**
 * The XML Schema duration type.
 *
 * @author <a href='mailto:dog@gnu.org'>Chris Burdess</a>
 */
final class DurationType
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

  DurationType()
  {
    super(new QName(XMLConstants.W3C_XML_SCHEMA_NS_URI, "duration"),
          Type.ANY_SIMPLE_TYPE);
  }

  public int[] getConstrainingFacets()
  {
    return CONSTRAINING_FACETS;
  }

  public boolean matches(String value)
  {
    int len = value.length();
    char expect = 'P';
    boolean seenT = false;
    for (int i = 0; i < len; i++)
      {
        char c = value.charAt(i);
        if (c == '-' && expect == 'P')
          {
            continue;
          }
        if (c == expect)
          {
            if (c == 'P')
              {
                expect = 'Y';
              }
            else if (c == 'Y')
              {
                expect = 'M';
              }
            else if (c == 'M' && !seenT)
              {
                expect = 'D';
              }
            else if (c == 'D')
              {
                expect = 'T';
              }
            else if (c == 'T')
              {
                expect = 'H';
                seenT = true;
              }
            else if (c == 'H')
              {
                expect = 'M';
              }
            else if (c == 'M' && seenT)
              {
                expect = 'S';
              }
            else if (c == 'S')
              {
                if (i + 1 != len)
                  {
                    return false;
                  }
              }
            continue;
          }
        if (c >= 0x30 && c <= 0x39 && expect != 'P' && expect != 'T')
          {
            continue;
          }
        return false;
      }
    return true;
  }
  
}

