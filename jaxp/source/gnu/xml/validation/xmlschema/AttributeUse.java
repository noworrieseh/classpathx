package gnu.xml.validation.xmlschema;

/**
 * An XML Schema attribute use schema component.
 *
 * @author <a href='mailto:dog@gnu.org'>Chris Burdess</a>
 */
class AttributeUse
{

  /**
   * Whether the attribute is required.
   */
  final boolean required;

  /**
   * The constraint type.
   * One of NONE, DEFAULT, FIXED.
   */
  final int type;
  
  /**
   * The value constraint.
   */
  final String value;

  /**
   * The name of the attribute to which this declaration refers.
   */
  final AttributeDeclaration declaration;

  AttributeUse(boolean required, int type, String value,
               AttributeDeclaration declaration)
  {
    this.required = required;
    this.type = type;
    this.value = value;
    this.declaration = declaration;
  }
  
}

