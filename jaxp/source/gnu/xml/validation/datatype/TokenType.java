package gnu.xml.validation.datatype;

import javax.xml.XMLConstants;
import javax.xml.namespace.QName;

/**
 * The XML Schema token type.
 *
 * @author <a href='mailto:dog@gnu.org'>Chris Burdess</a>
 */
final class TokenType
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

  TokenType()
  {
    super(new QName(XMLConstants.W3C_XML_SCHEMA_NS_URI, "token"),
          Type.NORMALIZED_STRING);
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
        return true;
      }
    if (value.charAt(0) == ' ' || value.charAt(len - 1) == ' ')
      {
        return false;
      }
    char last = '\u0000';
    for (int i = 0; i < len; i++)
      {
        char c = value.charAt(i);
        if (c == 0x0a || c == 0x0d || c == 0x09)
          {
            return false;
          }
        if (c == ' ' && last == ' ')
          {
            return false;
          }
        last = c;
      }
    return true;
  }
  
}

