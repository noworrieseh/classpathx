package gnu.xml.validation.datatype;

/**
 * The <code>maxLength</code> facet.
 *
 * @author <a href='mailto:dog@gnu.org'>Chris Burdess</a>
 */
public final class MaxLengthFacet
  extends Facet
{

  public final int value;

  public final boolean fixed;

  public MaxLengthFacet(int value, boolean fixed, Annotation annotation)
  {
    super(MAX_LENGTH, annotation);
    this.value = value;
    this.fixed = fixed;
  }

  public int hashCode()
  {
    return value;
  }

  public boolean equals(Object other)
  {
    return (other instanceof MaxLengthFacet &&
            ((MaxLengthFacet) other).value == value);
  }
  
}

