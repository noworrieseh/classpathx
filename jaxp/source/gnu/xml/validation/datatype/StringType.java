package gnu.xml.validation.datatype;

import java.util.Collections;
import java.util.Set;
import javax.xml.XMLConstants;
import javax.xml.namespace.QName;

/**
 * The XML Schema string type.
 *
 * @author <a href='mailto:dog@gnu.org'>Chris Burdess</a>
 */
final class StringType
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

  StringType()
  {
    super(new QName(XMLConstants.W3C_XML_SCHEMA_NS_URI, "string"),
          Type.ANY_SIMPLE_TYPE);
  }

  public int[] getConstrainingFacets()
  {
    return CONSTRAINING_FACETS;
  }

  public boolean matches(String value)
  {
    return true;
  }
  
}

