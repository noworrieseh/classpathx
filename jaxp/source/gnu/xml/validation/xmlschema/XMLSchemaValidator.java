package gnu.xml.validation.xmlschema;

import java.io.IOException;
import javax.xml.validation.Validator;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import org.w3c.dom.ls.LSResourceResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;

/**
 * JAXP validator for an XML Schema.
 *
 * @author <a href='mailto:dog@gnu.org'>Chris Burdess</a>
 */
final class XMLSchemaValidator
  extends Validator
{

  final XMLSchema schema;

  ErrorHandler errorHandler;
  LSResourceResolver resourceResolver;
  
  XMLSchemaValidator(XMLSchema schema)
  {
    this.schema = schema;
  }

  public void reset()
  {
  }

  public void validate(Source source, Result result)
    throws SAXException, IOException
  {
    // TODO
  }

  public ErrorHandler getErrorHandler()
  {
    return errorHandler;
  }

  public void setErrorHandler(ErrorHandler errorHandler)
  {
    this.errorHandler = errorHandler;
  }

  public LSResourceResolver getResourceResolver()
  {
    return resourceResolver;
  }

  public void setResourceResolver(LSResourceResolver resourceResolver)
  {
    this.resourceResolver = resourceResolver;
  }
  
}

