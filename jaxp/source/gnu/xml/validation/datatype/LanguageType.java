package gnu.xml.validation.datatype;

import java.util.regex.Pattern;
import javax.xml.XMLConstants;
import javax.xml.namespace.QName;

/**
 * The XML Schema language type.
 *
 * @author <a href='mailto:dog@gnu.org'>Chris Burdess</a>
 */
final class LanguageType
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

  static final Pattern PATTERN =
    Pattern.compile("[a-zA-Z]{1,8}(-[a-zA-Z0-9]{1,8})*");

  LanguageType()
  {
    super(new QName(XMLConstants.W3C_XML_SCHEMA_NS_URI, "language"),
          Type.TOKEN);
  }

  public int[] getConstrainingFacets()
  {
    return CONSTRAINING_FACETS;
  }

  public boolean matches(String value)
  {
    return PATTERN.matcher(value).matches();
  }
  
}

