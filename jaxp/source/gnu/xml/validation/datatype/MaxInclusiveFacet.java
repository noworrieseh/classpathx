package gnu.xml.validation.datatype;

/**
 * The <code>maxInclusive</code> facet.
 *
 * @author <a href='mailto:dog@gnu.org'>Chris Burdess</a>
 */
public final class MaxInclusiveFacet
  extends Facet
{
  
  public final int value;

  public final boolean fixed;

  public MaxInclusiveFacet(int value, boolean fixed, Annotation annotation)
  {
    super(MAX_INCLUSIVE, annotation);
    this.value = value;
    this.fixed = fixed;
  }
  
  public int hashCode()
  {
    return value;
  }

  public boolean equals(Object other)
  {
    return (other instanceof MaxInclusiveFacet &&
            ((MaxInclusiveFacet) other).value == value);
  }
  
}

