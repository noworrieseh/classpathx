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
 * Linking this library statically or dynamically with other modules is
 * making a combined work based on this library.  Thus, the terms and
 * conditions of the GNU General Public License cover the whole
 * combination.
 *
 * As a special exception, the copyright holders of this library give you
 * permission to link this library with independent modules to produce an
 * executable, regardless of the license terms of these independent
 * modules, and to copy and distribute the resulting executable under
 * terms of your choice, provided that you also meet, for each linked
 * independent module, the terms and conditions of the license of that
 * module.  An independent module is a module which is not derived from
 * or based on this library.  If you modify this library, you may extend
 * this exception to your version of the library, but you are not
 * obliged to do so.  If you do not wish to do so, delete this
 * exception statement from your version.
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
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import gnu.xml.libxmlj.util.NamedInputStream;
import gnu.xml.libxmlj.util.StandaloneDocumentType;
import gnu.xml.libxmlj.util.StandaloneLocator;
import gnu.xml.libxmlj.util.XMLJ;

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
    XMLJ.init ();
  }

  // -- DocumentBuilder --

  private boolean validate;
  private boolean coalesce;
  private boolean expandEntities;
  private EntityResolver entityResolver;
  private ErrorHandler errorHandler;
  private boolean seenFatalError;

  /**
   * Constructs a new validating document builder.
   */
  public GnomeDocumentBuilder ()
  {
    this (true, false, false);
  }
  
  /**
   * Constructs a new document builder.
   * @param validate whether to validate during parsing
   * @param coalesce whether to merge CDATA as text nodes
   * @param expandEntities whether to expand entity references
   */
  public GnomeDocumentBuilder (boolean validate,
                               boolean coalesce,
                               boolean expandEntities)
  {
    this.validate = validate;
    this.coalesce = coalesce;
    this.expandEntities = expandEntities;
  }

  public DOMImplementation getDOMImplementation ()
  {
    return this;
  }

  public boolean isNamespaceAware ()
  {
    return true;
  }

  public boolean isValidating ()
  {
    return validate;
  }

  public Document newDocument()
  {
    return createDocument(null, null, null);
  }

  public Document parse (InputSource input)
    throws SAXException, IOException
  {
    NamedInputStream in = XMLJ.getInputStream (input);
    byte[] detectBuffer = in.getDetectBuffer ();
    String publicId = input.getPublicId ();
    String systemId = input.getSystemId ();
    String base = XMLJ.getBaseURI (systemId);
    // Handle zero-length document
    if (detectBuffer == null)
      {
        throw new SAXParseException ("No document element", publicId,
                                     systemId, 0, 0);
      }
    seenFatalError = false;
    return parseStream(in,
                       detectBuffer,
                       publicId,
                       systemId,
                       base,
                       validate,
                       coalesce,
                       expandEntities,
                       entityResolver != null,
                       errorHandler != null);
  }
  
  private native Document parseStream (InputStream in,
                                       byte[] detectBuffer,
                                       String publicId,
                                       String systemId,
                                       String base,
                                       boolean validate,
                                       boolean coalesce,
                                       boolean expandEntities,
                                       boolean entityResolver,
                                       boolean errorHandler);
  
  public void setEntityResolver (EntityResolver resolver)
  {
    entityResolver = resolver;
  }

  public void setErrorHandler (ErrorHandler handler)
  {
    errorHandler = handler;
  }

  // -- DOMImplementation --

  public boolean hasFeature (String feature, String version)
  {
    if ("XML".equalsIgnoreCase (feature))
      {
        return ("3.0".equals (version) || "2.0" .equals (version) ||
                "1.0".equals (version) || version == null);
      }
    if ("HTML".equalsIgnoreCase (feature))
      {
        return false; // TODO
      }
    if ("Core".equalsIgnoreCase (feature))
      {
        return ("3.0".equals (version) || "2.0" .equals (version) ||
                "1.0".equals (version) || version == null);
      }
    if ("Stylesheets".equalsIgnoreCase (feature))
      {
        return false; // TODO
      }
    if ("CSS".equalsIgnoreCase (feature))
      {
        return false; // TODO
      }
    if ("CSS2".equalsIgnoreCase (feature))
      {
        return false; // TODO
      }
    if ("XPath".equalsIgnoreCase (feature))
      {
        return ("3.0".equals (version) || version ==  null);
      }
    if ("Traversal".equalsIgnoreCase (feature))
      {
        return ("2.0".equals (version) || version ==  null);
      }
    if ("Range".equalsIgnoreCase (feature))
      {
        return false; // TODO
      }
    return false;
  }
  
  // DOM Level 3

  public Object getFeature (String feature, String version)
  {
    // TODO
    return null;
  }
  
  public native Document createDocument (String namespaceURI,
                                         String qualifiedName,
                                         DocumentType doctype);

  public DocumentType createDocumentType (String qualifiedName,
                                          String publicId,
                                          String systemId)
  {
    return new StandaloneDocumentType (qualifiedName, publicId, systemId);
  }
  
  // Callback hooks from JNI
  
  private void setDocumentLocator (Object ctx, Object loc)
  {
    // ignore
  }
  
  private InputStream resolveEntity (String publicId, String systemId)
    throws SAXException, IOException
  {
    if (entityResolver == null)
      {
        return null;
      }
    InputSource source = entityResolver.resolveEntity (publicId, systemId);
    return (source == null) ? null : XMLJ.getInputStream (source);
  }
  
  private void warning (String message,
                        int lineNumber,
                        int columnNumber,
                        String publicId,
                        String systemId)
    throws SAXException
  {
    if (!seenFatalError && errorHandler != null)
      {
        Locator l = new StandaloneLocator (lineNumber,
                                           columnNumber,
                                           publicId,
                                           systemId);
        errorHandler.warning (new SAXParseException (message, l));
      }
  }

  private void error (String message,
                      int lineNumber,
                      int columnNumber,
                      String publicId,
                      String systemId)
    throws SAXException
  {
    if (!seenFatalError && errorHandler != null)
      {
        Locator l = new StandaloneLocator (lineNumber,
                                           columnNumber,
                                           publicId,
                                           systemId);
        errorHandler.error (new SAXParseException (message, l));
      }
  }

  private void fatalError (String message,
                           int lineNumber,
                           int columnNumber,
                           String publicId,
                           String systemId)
    throws SAXException
  {
    if (!seenFatalError && errorHandler != null)
      {
        seenFatalError = true;
        Locator l = new StandaloneLocator (lineNumber,
                                           columnNumber,
                                           publicId,
                                           systemId);
        errorHandler.fatalError (new SAXParseException (message, l));
      }
  }

}
