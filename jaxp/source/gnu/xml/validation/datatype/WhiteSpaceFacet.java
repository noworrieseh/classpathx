package gnu.xml.validation.datatype;

/**
 * The <code>whiteSpace</code> facet.
 *
 * @author <a href='mailto:dog@gnu.org'>Chris Burdess</a>
 */
public final class WhiteSpaceFacet
  extends Facet
{
  
  public static final int PRESERVE = 0;
  public static final int REPLACE = 1;
  public static final int COLLAPSE = 2;
  
  public final int value;

  public WhiteSpaceFacet(int value, Annotation annotation)
  {
    super(WHITESPACE, annotation);
    this.value = value;
  }
  
  public int hashCode()
  {
    return value;
  }

  public boolean equals(Object other)
  {
    return (other instanceof WhiteSpaceFacet &&
            ((WhiteSpaceFacet) other).value == value);
  }
  
}

