package gnu.xml.validation.datatype;

import java.util.Arrays;
import java.util.Collections;
import java.util.Set;
import java.util.TreeSet;
import javax.xml.XMLConstants;
import javax.xml.namespace.QName;

/**
 * The XML Schema double type.
 *
 * @author <a href='mailto:dog@gnu.org'>Chris Burdess</a>
 */
final class DoubleType
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

  static final Set SPECIAL =
    new TreeSet(Arrays.asList(new String[] {"INF", "-INF", "NaN"}));

  DoubleType()
  {
    super(new QName(XMLConstants.W3C_XML_SCHEMA_NS_URI, "double"),
          Type.ANY_SIMPLE_TYPE);
  }

  public int[] getConstrainingFacets()
  {
    return CONSTRAINING_FACETS;
  }

  public boolean matches(String value)
  {
    if (SPECIAL.contains(value))
      {
        return true;
      }
    try
      {
        Double.parseDouble(value);
        return true;
      }
    catch (NumberFormatException e)
      {
        return false;
      }
  }
  
}

