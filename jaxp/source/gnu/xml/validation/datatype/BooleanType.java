package gnu.xml.validation.datatype;

import java.util.Arrays;
import java.util.Collections;
import java.util.Set;
import java.util.TreeSet;
import javax.xml.XMLConstants;
import javax.xml.namespace.QName;

/**
 * The XML Schema boolean type.
 *
 * @author <a href='mailto:dog@gnu.org'>Chris Burdess</a>
 */
final class BooleanType
  extends AtomicSimpleType
{

  static final int[] CONSTRAINING_FACETS = {
    Facet.PATTERN,
    Facet.WHITESPACE
  };

  static final Set VALUE_SPACE =
    new TreeSet(Arrays.asList(new String[] {"true", "false", "1", "0"}));

  BooleanType()
  {
    super(new QName(XMLConstants.W3C_XML_SCHEMA_NS_URI, "boolean"),
          Type.ANY_SIMPLE_TYPE);
  }

  public int[] getConstrainingFacets()
  {
    return CONSTRAINING_FACETS;
  }

  public boolean matches(String value)
  {
    return VALUE_SPACE.contains(value);
  }
  
}

