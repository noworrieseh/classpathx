package gnu.xml.validation.datatype;

/**
 * The <code>minInclusive</code> facet.
 *
 * @author <a href='mailto:dog@gnu.org'>Chris Burdess</a>
 */
public final class MinInclusiveFacet
  extends Facet
{
  
  public final int value;

  public final boolean fixed;

  public MinInclusiveFacet(int value, boolean fixed, Annotation annotation)
  {
    super(MIN_INCLUSIVE, annotation);
    this.value = value;
    this.fixed = fixed;
  }
  
  public int hashCode()
  {
    return value;
  }

  public boolean equals(Object other)
  {
    return (other instanceof MinInclusiveFacet &&
            ((MinInclusiveFacet) other).value == value);
  }
  
}

