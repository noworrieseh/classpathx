package gnu.xml.validation.datatype;

import javax.xml.XMLConstants;
import javax.xml.namespace.QName;

/**
 * The XML Schema NMTOKEN type.
 *
 * @author <a href='mailto:dog@gnu.org'>Chris Burdess</a>
 */
final class NMTokenType
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

  NMTokenType()
  {
    super(new QName(XMLConstants.W3C_XML_SCHEMA_NS_URI, "NMTOKEN"),
          Type.TOKEN);
  }

  public int[] getConstrainingFacets()
  {
    return CONSTRAINING_FACETS;
  }

  public boolean matches(String value)
  {
    // TODO
    return true;
  }
  
}

