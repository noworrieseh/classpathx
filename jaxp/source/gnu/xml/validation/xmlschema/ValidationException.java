package gnu.xml.validation.xmlschema;

import org.xml.sax.Locator;
import org.xml.sax.SAXParseException;

/**
 * An XML Schema validation rule violation.
 *
 * @author <a href='mailto:dog@gnu.org'>Chris Burdess</a>
 */
class ValidationException
  extends SAXParseException
{

  ValidationException(String message, Locator locator)
  {
    super(message, locator);
  }
  
}

