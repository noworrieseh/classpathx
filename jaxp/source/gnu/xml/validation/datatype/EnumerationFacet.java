package gnu.xml.validation.datatype;

/**
 * The <code>enumeration</code> facet.
 *
 * @author <a href='mailto:dog@gnu.org'>Chris Burdess</a>
 */
public final class EnumerationFacet
  extends Facet
{
  
  public final String value;

  public EnumerationFacet(String value, Annotation annotation)
  {
    super(ENUMERATION, annotation);
    this.value = value;
  }

  public int hashCode()
  {
    return value.hashCode();
  }

  public boolean equals(Object other)
  {
    return (other instanceof EnumerationFacet &&
            ((EnumerationFacet) other).value.equals(value));
  }
  
}

