package gnu.xml.validation.datatype;

import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.xml.namespace.QName;

/**
 * An XML Schema union simple type.
 *
 * @author <a href='mailto:dog@gnu.org'>Chris Burdess</a>
 */
public class UnionSimpleType
  extends SimpleType
{

  /**
   * The member types in this union.
   */
  public final List memberTypes;
  
  public UnionSimpleType(QName name, Set facets,
                         int fundamentalFacets, SimpleType baseType,
                         Annotation annotation, List memberTypes)
  {
    super(name, UNION, facets, fundamentalFacets, baseType, annotation);
    this.memberTypes = memberTypes;
  }

  public boolean matches(String value)
  {
    for (Iterator i = memberTypes.iterator(); i.hasNext(); )
      {
        SimpleType type = (SimpleType) i.next();
        if (type.matches(value))
          {
            return true;
          }
      }
    return false;
  }
  
}

