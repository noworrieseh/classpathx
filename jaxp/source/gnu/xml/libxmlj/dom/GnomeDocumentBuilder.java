/*
 * GnomeDocumentBuilder.java
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
package gnu.xml.libxmlj.dom;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.io.IOException;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.DOMImplementation;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 * A JAXP DOM implementation that uses Gnome libxml2 as the underlying
 * parser and node representation.
 *
 * @author <a href='mailto:dog@gnu.org'>Chris Burdess</a>
 */
public class GnomeDocumentBuilder
extends DocumentBuilder
implements DOMImplementation
{

  static
  {
    System.loadLibrary("xml2-jni");
  }

  // -- DocumentBuilder --

  private boolean validating;
  
  // xmlParserCtxt
  private int context;

  private EntityResolver entityResolver;

  private ErrorHandler errorHandler;

  /**
   * Constructs a new validating document builder.
   */
  public GnomeDocumentBuilder()
  {
    this(true, false, false);
  }

  /**
   * Constructs a new document builder.
   * @param validate whether to validate during parsing
   * @param coalesce whether to merge CDATA as text nodes
   * @param expandEntities whether to expand entity references
   */
  public GnomeDocumentBuilder(boolean validate, boolean coalesce,
      boolean expandEntities)
  {
    validating = validate;
    init(validate, coalesce, expandEntities);
  }

  private native void init(boolean validate, boolean coalesce,
      boolean expandEntities);

  protected native void finalize();

  public DOMImplementation getDOMImplementation()
  {
    return this;
  }

  public boolean isNamespaceAware()
  {
    return true;
  }

  public boolean isValidating()
  {
    return validating;
  }

  public Document newDocument()
  {
    return createDocument(null, null, null);
  }

  public Document parse(File file)
    throws SAXException, IOException
  {
    String filename = file.getPath();
    return parseFile(filename);
  }

  public Document parse(InputSource input)
    throws SAXException, IOException
  {
    String systemId = input.getSystemId();
    if (systemId != null)
      return parseFile(systemId);
    else
    {
      InputStream in = input.getByteStream();
      ByteArrayOutputStream out = new ByteArrayOutputStream();
      int max = in.available();
      byte[] buf = new byte[max];
      for (int len = in.read(buf); len != -1; len = in.read(buf))
        out.write(buf, 0, len);
      buf = null;
      return parseMemory(out.toByteArray());
    }
  }

  private native Document parseFile(String filename);
  
  private native Document parseMemory(byte[] bytes);

  public void setEntityResolver(EntityResolver resolver)
  {
    entityResolver = resolver;
    setCustomEntityResolver(resolver != null);
  }

  private native void setCustomEntityResolver(boolean flag);

  public void setErrorHandler(ErrorHandler handler)
  {
    errorHandler = handler;
    setCustomErrorHandler(handler != null);
  }

  private native void setCustomErrorHandler(boolean flag);

  // -- DOMImplementation --

  public boolean hasFeature(String feature, String version)
  {
    // TODO
    throw new UnsupportedOperationException();
  }
  
  public native Document createDocument(String namespaceURI,
      String qualifiedName, DocumentType doctype);

  public DocumentType createDocumentType(String qualifiedName,
      String publicId, String systemId)
  {
    return new StandaloneDocumentType(qualifiedName, publicId, systemId);
  }

  // Callback hooks from JNI

  private void warning(String message, String publicId, String systemId,
      int lineNumber, int columnNumber)
    throws SAXException
  {
    SAXParseException e = new SAXParseException(message, publicId, systemId,
        lineNumber, columnNumber);
    errorHandler.warning(e);
  }

  private void error(String message, String publicId, String systemId,
      int lineNumber, int columnNumber)
    throws SAXException
  {
    SAXParseException e = new SAXParseException(message, publicId, systemId,
        lineNumber, columnNumber);
    errorHandler.error(e);
  }

  private void fatalError(String message, String publicId, String systemId,
      int lineNumber, int columnNumber)
    throws SAXException
  {
    SAXParseException e = new SAXParseException(message, publicId, systemId,
        lineNumber, columnNumber);
    errorHandler.fatalError(e);
  }

}
