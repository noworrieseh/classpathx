package gnu.xml.validation.datatype;

/**
 * A schema component annotation.
 *
 * @author <a href='mailto:dog@gnu.org'>Chris Burdess</a>
 */
public class Annotation
{

  public final String documentation;

  public Annotation(String documentation)
  {
    this.documentation = documentation;
  }

  public String toString()
  {
    return documentation;
  }
  
}

