package gnu.xml.validation.xmlschema;

import javax.xml.validation.TypeInfoProvider;
import javax.xml.validation.ValidatorHandler;
import org.w3c.dom.ls.LSResourceResolver;
import org.xml.sax.ContentHandler;
import org.xml.sax.ErrorHandler;

final class XMLSchemaValidatorHandler
  extends ValidatorHandler
{

  final XMLSchema schema;
  final TypeInfoProvider typeInfoProvider;
  ContentHandler contentHandler;
  ErrorHandler errorHandler;
  LSResourceResolver resourceResolver;

  XMLSchemaValidatorHandler(XMLSchema schema)
  {
    this.schema = schema;
    typeInfoProvider = new XMLSchemaTypeInfoProvider(this);
  }

  public ContentHandler getContentHandler()
  {
    return contentHandler;
  }

  public void setContentHandler(ContentHandler contentHandler)
  {
    this.contentHandler = contentHandler;
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

  public TypeInfoProvider getTypeInfoProvider()
  {
    return typeInfoProvider;
  }

  TypeInfo getElementTypeInfo()
  {
    // TODO
    return null;
  }

  TypeInfo getAttributeTypeInfo(int index)
  {
    // TODO
    return null;
  }

  boolean isIdAttribute(int index)
  {
    // TODO
    return false;
  }

  boolean isSpecified(int index)
  {
    // TODO
    return false;
  }

  public void setDocumentLocator(Locator locator)
  {
    if (contentHandler != null)
      {
        contentHandler.setDocumentLocator(locator);
      }
  }

  public void startDocument()
    throws SAXException
  {
    if (contentHandler != null)
      {
        contentHandler.startDocument();
      }
  }

  public void endDocument()
    throws SAXException
  {
    if (contentHandler != null)
      {
        contentHandler.endDocument();
      }
  }

  public void startPrefixMapping(String prefix, String uri)
    throws SAXException
  {
    // TODO
    if (contentHandler != null)
      {
        contentHandler.startPrefixMapping(prefix, uri);
      }
  }

  public void endPrefixMapping(String prefix)
    throws SAXException
  {
    // TODO
    if (contentHandler != null)
      {
        contentHandler.endPrefixMapping(prefix);
      }
  }

  public void startElement(String uri, String localName, String qName,
                           Attributes atts)
    throws SAXException
  {
    // TODO
    if (contentHandler != null)
      {
        contentHandler.startElement(uri, localName, qName, atts);
      }
  }

  public void endElement(String uri, String localName, String qName)
    throws SAXException
  {
    // TODO
    if (contentHandler != null)
      {
        contentHandler.endElement(uri, localName, qName);
      }
  }

  public void characters(char[] ch, int start, int length)
    throws SAXException
  {
    // TODO
    if (contentHandler != null)
      {
        contentHandler.characters(ch, start, length);
      }
  }

  public void ignorableWhitespace(char[] ch, int start, int length)
    throws SAXException
  {
    if (contentHandler != null)
      {
        contentHandler.ignorableWhitespace(ch, start, length);
      }
  }

  public void processingInstruction(String target, String data)
    throws SAXException
  {
    if (contentHandler != null)
      {
        contentHandler.processingInstruction(target, data);
      }
  }
  
  public void skippedEntity(String name)
    throws SAXException
  {
    if (contentHandler != null)
      {
        contentHandler.skippedEntity(name);
      }
  }
  
}

