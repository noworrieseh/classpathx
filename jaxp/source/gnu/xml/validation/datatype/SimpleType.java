package gnu.xml.validation.datatype;

import java.util.Set;
import javax.xml.XMLConstants;
import javax.xml.namespace.QName;

/**
 * An XML Schema simple type.
 *
 * @author <a href='mailto:dog@gnu.org'>Chris Burdess</a>
 */
public abstract class SimpleType
  extends Type
{

  /**
   * The variety of the <code>anySimpleType</code> datatype.
   */
  public static final int ANY = 0;
  
  /**
   * The atomic variety.
   */
  public static final int ATOMIC = 1;
  
  /**
   * The list variety.
   */
  public static final int LIST = 2;

  /**
   * The union variety.
   */
  public static final int UNION = 3;

  /**
   * The variety of this simple type.
   */
  public final int variety;

  /**
   * The facets of this simple type.
   */
  public final Set facets;

  /**
   * The fundamental facets of this simple type.
   */
  public final int fundamentalFacets;

  /**
   * If this datatype has been derived by restriction, then the component
   * from which it was derived.
   */
  public final SimpleType baseType;

  /**
   * Optional annotation.
   */
  public final Annotation annotation;

  protected SimpleType(QName name, int variety, Set facets,
                       int fundamentalFacets, SimpleType baseType,
                       Annotation annotation)
  {
    super(name);
    this.variety = variety;
    this.facets = facets;
    this.fundamentalFacets = fundamentalFacets;
    this.baseType = baseType;
    this.annotation = annotation;
  }

  /**
   * Indicates whether this type permits the specified value.
   */
  public abstract boolean matches(String value);
  
}

