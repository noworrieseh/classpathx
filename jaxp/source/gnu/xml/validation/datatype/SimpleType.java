package gnu.xml.validation.datatype;

import java.util.Iterator;
import java.util.Set;
import java.util.regex.Matcher;
import javax.xml.XMLConstants;
import javax.xml.namespace.QName;

/**
 * An XML Schema simple type.
 *
 * @author <a href='mailto:dog@gnu.org'>Chris Burdess</a>
 */
public class SimpleType
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

  public SimpleType(QName name, int variety, Set facets,
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
  public boolean matches(String value)
  {
    if (facets != null)
      {
        for (Iterator i = facets.iterator(); i.hasNext(); )
          {
            Facet facet = (Facet) i.next();
            switch (facet.type)
              {
              case Facet.LENGTH:
                LengthFacet lf = (LengthFacet) facet;
                if (value.length() != lf.value)
                  {
                    return false;
                  }
                break;
              case Facet.MIN_LENGTH:
                MinLengthFacet nlf = (MinLengthFacet) facet;
                if (value.length() < nlf.value)
                  {
                    return false;
                  }
                break;
              case Facet.MAX_LENGTH:
                MaxLengthFacet xlf = (MaxLengthFacet) facet;
                if (value.length() > xlf.value)
                  {
                    return false;
                  }
                break;
              case Facet.PATTERN:
                PatternFacet pf = (PatternFacet) facet;
                Matcher matcher = pf.value.matcher(value);
                if (!matcher.find())
                  {
                    return false;
                  }
                break;
              case Facet.ENUMERATION:
                // TODO
                break;
              case Facet.WHITESPACE:
                // TODO
                break;
              case Facet.MAX_INCLUSIVE:
              case Facet.MAX_EXCLUSIVE:
              case Facet.MIN_EXCLUSIVE:
              case Facet.MIN_INCLUSIVE:
                // TODO
                break;
              case Facet.TOTAL_DIGITS:
                TotalDigitsFacet tdf = (TotalDigitsFacet) facet;
                if (countDigits(value, true) > tdf.value)
                  {
                    return false;
                  }
                break;
              case Facet.FRACTION_DIGITS:
                FractionDigitsFacet fdf = (FractionDigitsFacet) facet;
                if (countDigits(value, false) > fdf.value)
                  {
                    return false;
                  }
                break;
              }
          }
      }
    return true;
  }

  private static int countDigits(String value, boolean any)
  {
    int count = 0;
    int len = value.length();
    boolean seenDecimal = false;
    for (int i = 0; i < len; i++)
      {
        char c = value.charAt(i);
        if (c == 0x2e)
          {
            seenDecimal = true;
          }
        else if (c >= 0x30 && c <= 0x39 && (any || seenDecimal))
          {
            count++;
          }
      }
    return count;
  }
  
}

