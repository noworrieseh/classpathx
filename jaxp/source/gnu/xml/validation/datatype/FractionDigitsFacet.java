package gnu.xml.validation.datatype;

/**
 * The <code>fractionDigits</code> facet.
 *
 * @author <a href='mailto:dog@gnu.org'>Chris Burdess</a>
 */
public final class FractionDigitsFacet
  extends Facet
{
  
  public final int value;

  public final boolean fixed;

  public FractionDigitsFacet(int value, boolean fixed, Annotation annotation)
  {
    super(FRACTION_DIGITS, annotation);
    this.value = value;
    this.fixed = fixed;
  }
  
  public int hashCode()
  {
    return value;
  }

  public boolean equals(Object other)
  {
    return (other instanceof FractionDigitsFacet &&
            ((FractionDigitsFacet) other).value == value);
  }
  
}

