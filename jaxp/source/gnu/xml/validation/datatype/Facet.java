package gnu.xml.validation.datatype;

/**
 * An XML Schema constraining facet.
 *
 * @author <a href='mailto:dog@gnu.org'>Chris Burdess</a>
 */
public abstract class Facet
{

  public static final int LENGTH = 1;
  public static final int MIN_LENGTH = 2;
  public static final int MAX_LENGTH = 3;
  public static final int PATTERN = 4;
  public static final int ENUMERATION = 5;
  public static final int WHITESPACE = 6;
  public static final int MAX_INCLUSIVE = 7;
  public static final int MAX_EXCLUSIVE = 8;
  public static final int MIN_EXCLUSIVE = 9;
  public static final int MIN_INCLUSIVE = 10;
  public static final int TOTAL_DIGITS = 11;
  public static final int FRACTION_DIGITS = 12;

  /**
   * The type of this facet.
   */
  public final int type;

  /**
   * Optional annotation.
   */
  public final Annotation annotation;

  protected Facet(int type, Annotation annotation)
  {
    this.type = type;
    this.annotation = annotation;
  }
  
}

