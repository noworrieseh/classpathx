package gnu.xml.validation.xmlschema;

import java.util.LinkedHashMap;
import java.util.Map;
import javax.xml.validation.Schema;
import javax.xml.validation.Validator;
import javax.xml.validation.ValidatorHandler;

/**
 * An XML Schema schema.
 *
 * @author <a href='mailto:dog@gnu.org'>Chris Burdess</a>
 */
final class XMLSchema
  extends Schema
{

  static final int FINAL_NONE = 0x00;
  static final int FINAL_EXTENSION = 0x01;
  static final int FINAL_RESTRICTION = 0x02;
  static final int FINAL_LIST = 0x04;
  static final int FINAL_UNION = 0x08;
  static final int FINAL_ALL = 0x0f;
  
  static final int BLOCK_NONE = 0x00;
  static final int BLOCK_EXTENSION = 0x01;
  static final int BLOCK_RESTRICTION = 0x02;
  static final int BLOCK_SUBSTITUTION = 0x04;
  static final int BLOCK_ALL = 0x07;

  static final int GLOBAL = 0x00;
  static final int LOCAL = 0x01;
  static final int ABSENT = 0x02;

  final String targetNamespace;
  final String version;
  final int finalDefault;
  final int blockDefault;
  final boolean attributeFormQualified;
  final boolean elementFormQualified;

  /**
   * The element declarations in this schema.
   */
  final Map elementDeclarations;

  /**
   * The attribute declarations in this schema.
   */
  final Map attributeDeclarations;

  // TODO type declarations

  XMLSchema(String targetNamespace, String version,
            int finalDefault, int blockDefault,
            boolean attributeFormQualified,
            boolean elementFormQualified)
  {
    this.targetNamespace = targetNamespace;
    this.version = version;
    this.finalDefault = finalDefault;
    this.blockDefault = blockDefault;
    this.attributeFormQualified = attributeFormQualified;
    this.elementFormQualified = elementFormQualified;
    elementDeclarations = new LinkedHashMap();
    attributeDeclarations = new LinkedHashMap();
  }

  public Validator newValidator()
  {
    // TODO
    //return new XMLSchemaValidator(this);
    return null;
  }

  public ValidatorHandler newValidatorHandler()
  {
    // TODO
    //return new XMLSchemaValidatorHandler(this);
    return null;
  }
  
}

