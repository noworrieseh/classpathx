/*
 * GnomeXmlReader.java
 * Copyright (C) 2004 The Free Software Foundation
 * 
 * This file is part of GNU JAXP, a library.
 * 
 * GNU JAXP is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 * GNU JAXP is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 * As a special exception, if you link this library with other files to
 * produce an executable, this library does not by itself cause the
 * resulting executable to be covered by the GNU General Public License.
 * This exception does not however invalidate any other reasons why the
 * executable file might be covered by the GNU General Public License.
 */
package gnu.xml.libxmlj.sax;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.DTDHandler;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.SAXParseException;
import org.xml.sax.XMLReader;

/**
 * A SAX2 parser that uses libxml2.
 *
 * @author <a href='mailto:dog@gnu.org'>Chris Burdess</a>
 */
public class GnomeXMLReader
implements XMLReader
{

  static
  {
    System.loadLibrary("xmlj");
  }

  private static final String FEATURES_PREFIX = "http://xml.org/sax/features/";
  private static final List RECOGNIZED_FEATURES = Arrays.asList(new String[] {
    "external-general-entities", "external-parameter-entities",
    "is-standalone", "lexical-handler/parameter-entities", "namespaces",
    "namespace-prefixes", "resolve-dtd-uris", "string-interning",
    "use-attributes2", "use-locator2", "use-entity-resolver2", "validation"
  });
  private static final String PROPERTIES_PREFIX = "http://xml.org/sax/properties/";
  private static final List RECOGNIZED_PROPERTIES = Arrays.asList(new String[] {
    "declaration-handler", "dom-node", "lexical-handler", "xml-string"
  });

  // Instance members

  private boolean namespaceAware;

  private boolean validating;

  private ContentHandler contentHandler;

  private DTDHandler dtdHandler;

  private EntityResolver entityResolver;

  private ErrorHandler errorHandler;

  private GnomeLocator locator;

  private transient Namespaces namespaces;

  /*
   * xmlParserCtxtPtr
   */
  private int context;

  public GnomeXMLReader()
  {
    this(true, true);
  }

  public GnomeXMLReader(boolean namespaceAware, boolean validating)
  {
    this.namespaceAware = namespaceAware;
    this.validating = validating;
    // TODO validation on SAX parse
    context = createContext();
    namespaces = new Namespaces();
  }

  protected void finalize()
  {
    clearContext(context);
  }

  native int createContext();

  native int clearContext(int context);

  public ContentHandler getContentHandler()
  {
    return contentHandler;
  }

  public void setContentHandler(ContentHandler handler)
  {
    contentHandler = handler;
  }

  public DTDHandler getDTDHandler()
  {
    return dtdHandler;
  }

  public void setDTDHandler(DTDHandler handler)
  {
    dtdHandler = handler;
  }

  public EntityResolver getEntityResolver()
  {
    return entityResolver;
  }

  public void setEntityResolver(EntityResolver resolver)
  {
    entityResolver = resolver;
  }

  public ErrorHandler getErrorHandler()
  {
    return errorHandler;
  }

  public void setErrorHandler(ErrorHandler handler)
  {
    errorHandler = handler;
  }

  // Features

  public boolean getFeature(String name)
    throws SAXNotRecognizedException, SAXNotSupportedException
  {
    checkFeatureName(name);
    switch (getFeature(context, name))
    {
      case 1:
        return true;
      case 0:
        return false;
      default:
        throw new SAXNotSupportedException(name);
    }
  }

  native int getFeature(int context, String name);

  public void setFeature(String name, boolean value)
    throws SAXNotRecognizedException, SAXNotSupportedException
  {
    checkFeatureName(name);
    if (setFeature(context, name, value ? 1 : 0) == -1)
      throw new SAXNotSupportedException(name);
  }
  
  native int setFeature(int context, String name, int value);

  /**
   * Check that the specified feature name is recognized.
   */
  static void checkFeatureName(String name)
    throws SAXNotRecognizedException
  {
    if (name == null || !name.startsWith(FEATURES_PREFIX))
      throw new SAXNotRecognizedException(name);
    String key = name.substring(FEATURES_PREFIX.length());
    if (!RECOGNIZED_FEATURES.contains(key))
      throw new SAXNotRecognizedException(name);
  }
  
  // Properties

  public Object getProperty(String name)
    throws SAXNotRecognizedException, SAXNotSupportedException
  {
    checkPropertyName(name);
    throw new SAXNotSupportedException(name);
  }
  
  public void setProperty(String name, Object value)
    throws SAXNotRecognizedException, SAXNotSupportedException
  {
    checkPropertyName(name);
    throw new SAXNotSupportedException(name);
  }
  
  /**
   * Check that the specified property name is recognized.
   */
  static void checkPropertyName(String name)
    throws SAXNotRecognizedException
  {
    if (!name.startsWith(PROPERTIES_PREFIX))
      throw new SAXNotRecognizedException(name);
    String key = name.substring(PROPERTIES_PREFIX.length());
    if (!RECOGNIZED_PROPERTIES.contains(key))
      throw new SAXNotRecognizedException(name);
  }
  
  // Parse

  public synchronized void parse(InputSource input)
    throws IOException, SAXException
  {
    String systemId = input.getSystemId();
    if (systemId != null)
      parseFile(context, systemId);
    else
    {
      InputStream in = input.getByteStream();
      ByteArrayOutputStream out = new ByteArrayOutputStream();
      int max = in.available();
      byte[] buf = new byte[max];
      for (int len = in.read(buf); len != -1; len = in.read(buf))
        out.write(buf, 0, len);
      buf = null;
      parseMemory(context, out.toByteArray());
    }
  }

  public synchronized void parse(String systemId)
    throws IOException, SAXException
  {
    parseFile(context, systemId);
  }
  
  native int parseFile(int context, String filename)
    throws IOException, SAXException;

  native int parseMemory(int context, byte[] buf)
    throws IOException, SAXException;

  String getURI(String prefix)
  {
    if (!namespaceAware)
      return null;
    return namespaces.getURI(prefix);
  }

  // Callbacks from libxmlj

  private void notationDecl(String name, String publicId, String systemId)
    throws SAXException
  {
    if (dtdHandler != null)
      dtdHandler.notationDecl(name, publicId, systemId);
  }

  private void unparsedEntityDecl(String name, String publicId,
      String systemId, String notationName)
    throws SAXException
  {
    if (dtdHandler != null)
      dtdHandler.unparsedEntityDecl(name, publicId, systemId, notationName);
  }

  private void setDocumentLocator(int id)
  {
    locator = new GnomeLocator(id);
    if (contentHandler != null)
      contentHandler.setDocumentLocator(locator);
  }

  private void startDocument()
    throws SAXException
  {
    if (contentHandler != null)
      contentHandler.startDocument();
  }

  private void endDocument()
    throws SAXException
  {
    if (contentHandler != null)
      contentHandler.endDocument();
  }

  private void startElement(String name, String[] attrs)
    throws SAXException
  {
    if (contentHandler != null)
    {
      XMLName xName = new XMLName(this, name);
      if (namespaceAware)
      {
        // Handle defined namespaces
        namespaces.push();
        int len = attrs.length;
        ArrayList filtered = new ArrayList(len);
        for (int i = 0; i < len; i += 2)
        {
          String attName = attrs[i];
          String attValue = attrs[i + 1];
          if (attName.equals("xmlns"))
            startPrefixMapping(null, attValue);
          else if (attName.startsWith("xmlns:"))
            startPrefixMapping(attName.substring(6), attValue);
          else
          {
            filtered.add(attName);
            filtered.add(attValue);
          }
        }
        // Remove xmlns attributes
        attrs = new String[filtered.size()];
        filtered.toArray(attrs);
      }
      // Construct attributes
      Attributes atts = new StringArrayAttributes(this, attrs);
      contentHandler.startElement(xName.uri, xName.localName, xName.qName,
          atts);
    }
  }
  
  private void endElement(String name)
    throws SAXException
  {
    if (contentHandler != null)
    {
      XMLName xName = new XMLName(this, name);
      contentHandler.endElement(xName.uri, xName.localName, xName.qName);
      // Handle undefining namespaces
      if (namespaceAware)
      {
        for (Iterator i = namespaces.currentPrefixes(); i.hasNext(); )
          endPrefixMapping((String)i.next());
        namespaces.pop(); // releases current depth
      }
    }
  }

  private void startPrefixMapping(String prefix, String uri)
    throws SAXException
  {
    if (contentHandler != null)
    {
      namespaces.define(prefix, uri);
      contentHandler.startPrefixMapping(prefix, uri);
    }
  }

  private void endPrefixMapping(String prefix)
    throws SAXException
  {
    if (contentHandler != null)
      contentHandler.endPrefixMapping(prefix);
  }

  private void characters(String text, int len)
    throws SAXException
  {
    if (contentHandler != null)
    {
      char[] ch = text.toCharArray();
      contentHandler.characters(ch, 0, len);
    }
  }

  private void ignorableWhitespace(String text, int len)
    throws SAXException
  {
    if (contentHandler != null)
    {
      char[] ch = text.toCharArray();
      contentHandler.ignorableWhitespace(ch, 0, len);
    }
  }

  private void processingInstruction(String target, String data)
    throws SAXException
  {
    if (contentHandler != null)
      contentHandler.processingInstruction(target, data);
  }

  private void warning(String message)
    throws SAXException
  {
    if (errorHandler != null)
      errorHandler.warning(new SAXParseException(message, locator));
  }

  private void error(String message)
    throws SAXException
  {
    if (errorHandler != null)
      errorHandler.error(new SAXParseException(message, locator));
  }

  private void fatalError(String message)
    throws SAXException
  {
    if (errorHandler != null)
      errorHandler.fatalError(new SAXParseException(message, locator));
  }

}
