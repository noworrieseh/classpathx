/*
 * SAXEventSink.java
 * Copyright (C) 1999,2000,2001 The Free Software Foundation
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
 * along with this program; if not, write to the Free Software
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

package gnu.xml.dom.ls;

import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.Text;
import org.xml.sax.Attributes;
import org.xml.sax.DTDHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.ext.Attributes2;
import org.xml.sax.ext.DeclHandler;
import org.xml.sax.ext.LexicalHandler;
import gnu.xml.aelfred2.ContentHandler2;
import gnu.xml.dom.DomAttr;
import gnu.xml.dom.DomDocument;
import gnu.xml.dom.DomDoctype;

/**
 * A SAX content and lexical handler used to construct a DOM document.
 *
 * @author <a href='mailto:dog@gnu.org'>Chris Burdess</a>
 */
class SAXEventSink
  implements ContentHandler2, LexicalHandler, DTDHandler, DeclHandler
{

  boolean namespaceAware;
  boolean ignoreWhitespace;
  boolean expandEntityReferences;
  boolean ignoreComments;
  boolean coalescing;
  
  DomDocument doc;
  Node ctx;
  Locator locator;
  boolean inCDATA;
  boolean inDTD;
  boolean interrupted;

  void interrupt()
  {
    interrupted = true;
  }

  // -- ContentHandler2 --
  
  public void setDocumentLocator(Locator locator)
  {
    this.locator = locator;
  }

  public void startDocument()
    throws SAXException
  {
    doc = new DomDocument();
    ctx = doc;
  }

  public void xmlDecl(String version, String encoding, boolean standalone)
    throws SAXException
  {
    if (interrupted)
      {
        return;
      }
    doc.setXmlVersion(version);
    doc.setXmlEncoding(encoding);
    doc.setXmlStandalone(standalone);
  }

  public void endDocument()
    throws SAXException
  {
    ctx = null;
    locator = null;
  }

  public void startPrefixMapping(String prefix, String uri)
    throws SAXException
  {
    // TODO
  }

  public void endPrefixMapping(String prefix)
    throws SAXException
  {
    // TODO
  }

  public void startElement(String uri, String localName, String qName,
                           Attributes atts)
    throws SAXException
  {
    if (interrupted)
      {
        return;
      }
    Element element = createElement(uri, localName, qName, atts);
    // add element to context
    ctx.appendChild(element);
    ctx = element;
  }

  protected Element createElement(String uri, String localName, String qName,
                                  Attributes atts)
    throws SAXException
  {
    // create element node
    Element element = namespaceAware ?
      doc.createElementNS(uri, localName) :
      doc.createElement(qName);
    // add attributes
    int len = atts.getLength();
    Attributes2 atts2 = (atts instanceof Attributes2) ? (Attributes2) atts :
      null;
    NamedNodeMap attrs = element.getAttributes();
    for (int i = 0; i < len; i++)
      {
        // create attribute
        DomAttr attr;
        if (namespaceAware)
          {
            String a_uri = atts.getURI(i);
            String a_localName = atts.getLocalName(i);
            attr = (DomAttr) doc.createAttributeNS(a_uri, a_localName);
          }
        else
          {
            String a_qName = atts.getQName(i);
            attr = (DomAttr) doc.createAttribute(a_qName);
          }
        attr.setNodeValue(atts.getValue(i));
        if (atts2 != null)
          {
            // TODO attr.setDeclared(atts2.isDeclared(i));
            attr.setSpecified(atts2.isSpecified(i));
          }
        // add attribute to element
        if (namespaceAware)
          {
            attrs.setNamedItemNS(attr);
          }
        else
          {
            attrs.setNamedItem(attr);
          }
      }
    return element;
  }

  public void endElement(String uri, String localName, String qName)
    throws SAXException
  {
    if (interrupted)
      {
        return;
      }
    ctx = ctx.getParentNode();
  }

  public void characters(char[] c, int off, int len)
    throws SAXException
  {
    if (interrupted)
      {
        return;
      }
    ctx.appendChild(createText(c, off, len));
  }

  protected Text createText(char[] c, int off, int len)
    throws SAXException
  {
    Text text = (inCDATA && !coalescing) ?
      doc.createCDATASection(new String(c, off, len)) :
      doc.createTextNode(new String(c, off, len));
    return text;
  }

  public void ignorableWhitespace(char[] c, int off, int len)
    throws SAXException
  {
    if (interrupted)
      {
        return;
      }
    if (!ignoreWhitespace)
      {
        characters(c, off, len);
      }
  }

  public void processingInstruction(String target, String data)
    throws SAXException
  {
    if (interrupted)
      {
        return;
      }
    if (!inDTD)
      {
        Node pi = doc.createProcessingInstruction(target, data);
        ctx.appendChild(pi);
      }
  }

  public void skippedEntity(String name)
    throws SAXException
  {
    // This callback is totally pointless
  }

  // -- LexicalHandler --
  
  public void startDTD(String name, String publicId, String systemId)
    throws SAXException
  {
    if (interrupted)
      {
        return;
      }
    DomDoctype doctype = new DomDoctype(doc, name, publicId, systemId);
    doc.appendChild(doctype);
    ctx = doctype;
    inDTD = true;
  }

  public void endDTD()
    throws SAXException
  {
    if (interrupted)
      {
        return;
      }
    inDTD = false;
    ctx = ctx.getParentNode();
  }

  public void startEntity(String name)
    throws SAXException
  {
    // TODO
    /*
    Node entity = new DomEntity(doc, name);
    ctx.appendChild(entity);
    ctx = entityRef;
    */
  }

  public void endEntity(String name)
    throws SAXException
  {
    // TODO
    /*
    ctx = ctx.getParentNode();
    */
  }

  public void startCDATA()
    throws SAXException
  {
    inCDATA = true;
  }

  public void endCDATA()
    throws SAXException
  {
    inCDATA = false;
  }

  public void comment(char[] c, int off, int len)
    throws SAXException
  {
    if (interrupted)
      {
        return;
      }
    if (!inDTD)
      {
        Node comment = doc.createComment(new String(c, off, len));
        ctx.appendChild(comment);
      }
  }

  // -- DTDHandler --

  public void notationDecl(String name, String publicId, String systemId)
    throws SAXException
  {
    if (interrupted)
      {
        return;
      }
    DomDoctype doctype = (DomDoctype) ctx;
    doctype.declareNotation(name, publicId, systemId);
  }

  public void unparsedEntityDecl(String name, String publicId, String systemId,
                                 String notationName)
    throws SAXException
  {
    if (interrupted)
      {
        return;
      }
    DomDoctype doctype = (DomDoctype) ctx;
    doctype.declareEntity(name, publicId, systemId, notationName);
  }

  // -- DeclHandler --
  
  public void elementDecl(String name, String model)
    throws SAXException
  {
    if (interrupted)
      {
        return;
      }
    DomDoctype doctype = (DomDoctype) ctx;
    doctype.elementDecl(name, model);
  }

  public void attributeDecl(String eName, String aName, String type,
                            String mode, String value)
    throws SAXException
  {
    if (interrupted)
      {
        return;
      }
    DomDoctype doctype = (DomDoctype) ctx;
    doctype.attributeDecl(eName, aName, type, mode, value);
  }

  public void internalEntityDecl(String name, String value)
    throws SAXException
  {
    if (interrupted)
      {
        return;
      }
    DomDoctype doctype = (DomDoctype) ctx;
    doctype.declareEntity(name, null, null, null); // TODO value
  }

  public void externalEntityDecl(String name, String publicId, String systemId)
    throws SAXException
  {
    if (interrupted)
      {
        return;
      }
    DomDoctype doctype = (DomDoctype) ctx;
    doctype.declareEntity(name, publicId, systemId, null);
  }
  
}

