package gnu.xml.validation.datatype;

/**
 * The <code>totalDigits</code> facet.
 *
 * @author <a href='mailto:dog@gnu.org'>Chris Burdess</a>
 */
public final class TotalDigitsFacet
  extends Facet
{
  
  public final int value;

  public final boolean fixed;

  public TotalDigitsFacet(int value, boolean fixed, Annotation annotation)
  {
    super(TOTAL_DIGITS, annotation);
    this.value = value;
    this.fixed = fixed;
  }
  
  public int hashCode()
  {
    return value;
  }

  public boolean equals(Object other)
  {
    return (other instanceof TotalDigitsFacet &&
            ((TotalDigitsFacet) other).value == value);
  }
  
}

