package gnu.xml.validation.datatype;

import java.util.Collections;
import java.util.Set;
import javax.xml.XMLConstants;
import javax.xml.namespace.QName;

/**
 * The XML Schema base64Binary type.
 *
 * @author <a href='mailto:dog@gnu.org'>Chris Burdess</a>
 */
final class Base64BinaryType
  extends AtomicSimpleType
{

  static final String B64 = "ABCDEFGHIJKLMNOPQRSTUVWXYZ" +
    "abcdefghijklmnopqrstuvwxyz0123456789+/";
  static final String B16 = "AEIMQUYcgkosw048";
  static final String B04 = "AQgw";

  static final int[] CONSTRAINING_FACETS = {
    Facet.LENGTH,
    Facet.MIN_LENGTH,
    Facet.MAX_LENGTH,
    Facet.PATTERN,
    Facet.ENUMERATION,
    Facet.WHITESPACE
  };

  Base64BinaryType()
  {
    super(new QName(XMLConstants.W3C_XML_SCHEMA_NS_URI, "base64Binary"),
          Type.ANY_SIMPLE_TYPE);
  }

  public int[] getConstrainingFacets()
  {
    return CONSTRAINING_FACETS;
  }

  public boolean matches(String value)
  {
    // TODO value = collapseWhitespace(value);
    int len = value.length();
    try
      {
        for (int i = len - 1; i >= 0; )
          {
            char c4 = value.charAt(i--);
            if (c4 == ' ')
              {
                c4 = value.charAt(i--);
              }
            char c3 = value.charAt(i--);
            if (c3 == ' ')
              {
                c3 = value.charAt(i--);
              }
            char c2 = value.charAt(i--);
            if (c2 == ' ')
              {
                c2 = value.charAt(i--);
              }
            char c1 = value.charAt(i--);
            if (c1 == ' ')
              {
                c1 = value.charAt(i--);
              }
            
            if (c4 == '=')
              {
                if (c3 == '=')
                  {
                    if (B04.indexOf(c2) != -1 &&
                        B64.indexOf(c1) != -1)
                      {
                        continue;
                      }
                  }
                else if (B16.indexOf(c3) != -1)
                  {
                    if (B64.indexOf(c2) != -1 &&
                        B64.indexOf(c1) != -1)
                      {
                        continue;
                      }
                  }
              }
            else if (B64.indexOf(c4) != -1)
              {
                continue;
              }
            return false;
          }
      }
    catch (IndexOutOfBoundsException e)
      {
        return false;
      }
    return true;
  }
  
}

