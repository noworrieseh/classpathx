package gnu.xml.validation.datatype;

/**
 * The <code>minLength</code> facet.
 *
 * @author <a href='mailto:dog@gnu.org'>Chris Burdess</a>
 */
public final class MinLengthFacet
  extends Facet
{

  public final int value;

  public final boolean fixed;

  public MinLengthFacet(int value, boolean fixed, Annotation annotation)
  {
    super(MIN_LENGTH, annotation);
    this.value = value;
    this.fixed = fixed;
  }

  public int hashCode()
  {
    return value;
  }

  public boolean equals(Object other)
  {
    return (other instanceof MinLengthFacet &&
            ((MinLengthFacet) other).value == value);
  }
  
}

