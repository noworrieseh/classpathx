package gnu.xml.validation.datatype;

/**
 * The <code>maxExclusive</code> facet.
 *
 * @author <a href='mailto:dog@gnu.org'>Chris Burdess</a>
 */
public final class MaxExclusiveFacet
  extends Facet
{
  
  public final int value;

  public final boolean fixed;

  public MaxExclusiveFacet(int value, boolean fixed, Annotation annotation)
  {
    super(MAX_EXCLUSIVE, annotation);
    this.value = value;
    this.fixed = fixed;
  }
  
  public int hashCode()
  {
    return value;
  }

  public boolean equals(Object other)
  {
    return (other instanceof MaxExclusiveFacet &&
            ((MaxExclusiveFacet) other).value == value);
  }
  
}

