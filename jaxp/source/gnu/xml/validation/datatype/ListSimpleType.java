package gnu.xml.validation.datatype;

import java.util.Set;
import java.util.StringTokenizer;
import javax.xml.namespace.QName;

/**
 * An XML Schema list simple type.
 *
 * @author <a href='mailto:dog@gnu.org'>Chris Burdess</a>
 */
public class ListSimpleType
  extends SimpleType
{

  /**
   * The type of the items in this list (atomic or union).
   */
  public final SimpleType itemType;
  
  public ListSimpleType(QName name, Set facets,
                        int fundamentalFacets, SimpleType baseType,
                        Annotation annotation, SimpleType itemType)
  {
    super(name, LIST, facets, fundamentalFacets, baseType, annotation);
    this.itemType = itemType;
  }
  
  public boolean matches(String value)
  {
    StringTokenizer st = new StringTokenizer(value, " ");
    if (!st.hasMoreTokens())
      {
        return false;
      }
    while (st.hasMoreTokens())
      {
        String token = st.nextToken();
        if (!itemType.matches(token))
          {
            return false;
          }
      }
    return true;
  }
  
}

