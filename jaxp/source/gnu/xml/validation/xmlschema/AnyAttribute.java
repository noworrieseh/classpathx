package gnu.xml.validation.xmlschema;

import gnu.xml.validation.datatype.Annotation;

/**
 * An attribute wildcard.
 *
 * @author <a href='mailto:dog@gnu.org'>Chris Burdess</a>
 */
final class AnyAttribute
{

  static final int STRICT = 0;
  static final int LAX = 1;
  static final int SKIP = 2;

  final String namespace;

  final int processContents;

  Annotation annotation;

  AnyAttribute(String namespace, int processContents)
  {
    this.namespace = namespace;
    this.processContents = processContents;
  }
  
}

