package gnu.xml.validation.xmlschema;

import gnu.xml.validation.datatype.Annotation;
import gnu.xml.validation.datatype.SimpleType;
import javax.xml.namespace.QName;

/**
 * An XML Schema attribute declaration schema component.
 *
 * @author <a href='mailto:dog@gnu.org'>Chris Burdess</a>
 */
class AttributeDeclaration
{

  static final int NONE = 0;
  static final int DEFAULT = 1;
  static final int FIXED = 2;

  /**
   * The scope of this attribute declaration (global or local).
   */
  final boolean scope;

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
  final QName name;

  /**
   * The type definition corresponding to this attribute.
   */
  final SimpleType datatype;
  
  /**
   * The annotation associated with this attribute declaration, if any.
   */
  final Annotation annotation;

  AttributeDeclaration(boolean scope, int type, String value, QName name,
                       SimpleType datatype, Annotation annotation)
  {
    this.scope = scope;
    this.type = type;
    this.value = value;
    this.name = name;
    this.datatype = datatype;
    this.annotation = annotation;
  }
  
}

