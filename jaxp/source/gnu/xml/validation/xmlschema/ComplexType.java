package gnu.xml.validation.xmlschema;

import java.util.LinkedHashSet;
import java.util.Set;
import javax.xml.namespace.QName;
import gnu.xml.validation.datatype.Type;

/**
 * A complex type definition.
 *
 * @author <a href='mailto:dog@gnu.org'>Chris Burdess</a>
 */
class ComplexType
  extends Type
{

  /**
   * Either a simple type definition or a complex type definition.
   */
  QName baseType;

  /**
   * Either EXTENSION or RESTRICTION.
   */
  int derivationMethod;

  /**
   * A subset of {EXTENSION, RESTRICTION}.
   */
  final int finality;

  final boolean isAbstract;

  Set attributeUses;

  AnyAttribute attributeWildcard;

  /**
   * One of EMPTY, SIMPLE, MIXED, or ELEMENT_ONLY.
   */
  int contentType;

  /**
   * A simple type definition or a Particle.
   */
  Object contentModel;

  final int prohibitedSubstitutions;

  Set annotations;

  ComplexType(QName name,
              boolean isAbstract,
              int prohibitedSubstitutions,
              int finality)
  {
    super(name);
    this.isAbstract = isAbstract;
    this.prohibitedSubstitutions = prohibitedSubstitutions;
    this.finality = finality;
    attributeUses = new LinkedHashSet();
  }
  
}

