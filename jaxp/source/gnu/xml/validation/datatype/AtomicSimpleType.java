package gnu.xml.validation.datatype;

import java.util.Set;
import javax.xml.namespace.QName;

/**
 * An XML Schema atomic simple type.
 *
 * @author <a href='mailto:dog@gnu.org'>Chris Burdess</a>
 */
public class AtomicSimpleType
  extends SimpleType
{
  
  public AtomicSimpleType(QName name,
                          Set facets,
                          int fundamentalFacets,
                          SimpleType baseType,
                          Annotation annotation)
  {
    super(name, ATOMIC, facets, fundamentalFacets, baseType, annotation);
  }

  // Only for use by built-in types
  AtomicSimpleType(QName name, SimpleType baseType)
  {
    super(name, ATOMIC, null, 0, baseType, null);
  }

  public boolean matches(String value)
  {
    if (baseType != null && !baseType.matches(value))
      {
        return false;
      }
    // TODO fundamentalFacets
    // TODO facets
    return true;
  }
  
}

