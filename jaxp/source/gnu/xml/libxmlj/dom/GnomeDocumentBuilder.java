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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.IOException;
import java.io.PushbackInputStream;
import java.net.MalformedURLException;
import java.net.URL;
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
      System.loadLibrary("xmlj");
    }

  // -- DocumentBuilder --

  private boolean validate;
  private boolean coalesce;
  private boolean expandEntities;
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
  public GnomeDocumentBuilder(boolean validate,
                              boolean coalesce,
                              boolean expandEntities)
    {
      this.validate = validate;
      this.coalesce = coalesce;
      this.expandEntities = expandEntities;
    }

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
      return validate;
    }

  public Document newDocument()
    {
      return createDocument(null, null, null);
    }

  public Document parse(InputSource input) throws SAXException, IOException
    {
      InputStream in = getInputStream (input);
      String publicId = input.getPublicId();
      String systemId = input.getSystemId();
      return parseStream(in, publicId, systemId, validate, coalesce,
                         expandEntities, entityResolver, errorHandler);
    }

  InputStream getInputStream(InputSource input) throws IOException
    {
      InputStream in = input.getByteStream();
      if (in == null)
        {
          String systemId = input.getSystemId();
          if (systemId != null)
            in = new URL(systemId).openStream();
          else
            throw new IOException("Unable to locate input source");
        }
      return new PushbackInputStream(in, 50);
    }
  
  private native Document parseStream(InputStream in,
                                      String publicId,
                                      String systemId,
                                      boolean validate,
                                      boolean coalesce,
                                      boolean expandEntities,
                                      EntityResolver resolver,
                                      ErrorHandler errorHandler);

  public void setEntityResolver(EntityResolver resolver)
    {
      entityResolver = resolver;
    }

  public void setErrorHandler(ErrorHandler handler)
    {
      errorHandler = handler;
    }

  // -- DOMImplementation --

  public boolean hasFeature(String feature, String version)
    {
      // TODO
      throw new UnsupportedOperationException();
    }

  // DOM Level 3

  public Object getFeature (String feature, String version)
    {
      // TODO
      return null;
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
