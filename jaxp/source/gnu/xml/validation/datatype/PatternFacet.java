package gnu.xml.validation.datatype;

import java.util.regex.Pattern;

/**
 * The <code>pattern</code> facet.
 *
 * @author <a href='mailto:dog@gnu.org'>Chris Burdess</a>
 */
public final class PatternFacet
  extends Facet
{

  public final Pattern value;

  public PatternFacet(Pattern value, Annotation annotation)
  {
    super(PATTERN, annotation);
    this.value = value;
  }

  public int hashCode()
  {
    return value.hashCode();
  }

  public boolean equals(Object other)
  {
    return (other instanceof PatternFacet &&
            ((PatternFacet) other).value.equals(value));
  }
  
}

