/*
 * StreamSerializer.java
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

package gnu.xml.transform;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

/**
 * Serializes a DOM node to an output stream.
 *
 * @author <a href='mailto:dog@gnu.org'>Chris Burdess</a>
 */
public class StreamSerializer
{
  
  static final int SPACE = 0x20;
  static final int BANG = 0x21; // !
  static final int APOS = 0x27; // '
  static final int SLASH = 0x2f; // /
  static final int BRA = 0x3c; // <
  static final int KET = 0x3e; // >
  static final int EQ = 0x3d; // =

  protected String encoding;
  final int mode;
  final Map namespaces;
  protected String eol;

  protected boolean discardDefaultContent;
  protected boolean xmlDeclaration = true;

  public StreamSerializer()
  {
    this(Stylesheet.OUTPUT_XML, null, null);
  }

  public StreamSerializer(String encoding)
  {
    this(Stylesheet.OUTPUT_XML, encoding, null);
  }

  public StreamSerializer(int mode, String encoding, String eol)
  {
    this.mode = mode;
    this.encoding = (encoding != null) ? encoding : "UTF-8";
    this.eol = (eol != null) ? eol : System.getProperty("line.separator");
    namespaces = new HashMap();
  }

  public void serialize(final Node node, final OutputStream out)
    throws IOException
  {
    if (out == null)
      {
        throw new NullPointerException("no output stream");
      }
    String value;
    Node children;
    Node next = node.getNextSibling();
    String uri = node.getNamespaceURI();
    boolean defined = false;
    switch (node.getNodeType())
      {
      case Node.ATTRIBUTE_NODE:
        if (uri != null && !isDefined(uri))
          {
            String prefix = define(uri, node.getPrefix());
            String nsname = (prefix == null) ? "xmlns" : "xmlns:" + prefix;
            out.write(SPACE);
            out.write(nsname.getBytes(encoding));
            out.write(EQ);
            String nsvalue = "'" + encode(uri) + "'";
            out.write(nsvalue.getBytes(encoding));
            defined = true;
          }
        out.write(SPACE);
        out.write(node.getNodeName().getBytes(encoding));
        out.write(EQ);
        value = "'" + encode(node.getNodeValue()) + "'";
        out.write(value.getBytes(encoding));
        break;
      case Node.ELEMENT_NODE:
        value = node.getNodeName();
        out.write(BRA);
        out.write(value.getBytes(encoding));
        if (uri != null && !isDefined(uri))
          {
            String prefix = define(uri, node.getPrefix());
            String nsname = (prefix == null) ? "xmlns" : "xmlns:" + prefix;
            out.write(SPACE);
            out.write(nsname.getBytes(encoding));
            out.write(EQ);
            String nsvalue = "'" + encode(uri) + "'";
            out.write(nsvalue.getBytes(encoding));
            defined = true;
          }
        NamedNodeMap attrs = node.getAttributes();
        if (attrs != null)
          {
            int len = attrs.getLength();
            for (int i = 0; i < len; i++)
              {
                Attr attr = (Attr) attrs.item(i);
                if (discardDefaultContent && !attr.getSpecified())
                  {
                    // NOOP
                  }
                else
                  {
                    serialize(attr, out);
                  }
              }
          }
        children = node.getFirstChild();
        if (children == null)
          {
            out.write(SLASH);
            out.write(KET);
          }
        else
          {
            out.write(KET);
            serialize(children, out);
            out.write(BRA);
            out.write(SLASH);
            out.write(value.getBytes(encoding));
            out.write(KET);
          }
        break;
      case Node.TEXT_NODE:
        value = node.getNodeValue();
        if (!"yes".equals(node.getUserData("disable-output-encoding")))
          {
            value = encode(value);
          }
        out.write(value.getBytes(encoding));
        break;
      case Node.CDATA_SECTION_NODE:
        value = "<![CDATA[" + node.getNodeValue() + "]]>";
        out.write(value.getBytes(encoding));
        break;
      case Node.COMMENT_NODE:
        value = "<!--" + encode(node.getNodeValue()) + "-->";
        out.write(value.getBytes(encoding));
        Node cp = node.getParentNode();
        if (cp != null && cp.getNodeType() == Node.DOCUMENT_NODE)
          {
            out.write(eol.getBytes(encoding));
          }
        break;
      case Node.DOCUMENT_NODE:
      case Node.DOCUMENT_FRAGMENT_NODE:
        if (mode == Stylesheet.OUTPUT_XML)
          {
            if (!"yes".equals(node.getUserData("omit-xml-declaration")) &&
                xmlDeclaration)
              {
                Document doc = (node instanceof Document) ?
                  (Document) node : null;
                String version = (doc != null) ? doc.getXmlVersion() : null;
                if (version == null)
                  {
                    version = (String) node.getUserData("version");
                  }
                if (version == null)
                  {
                    version = "1.0";
                  }
                out.write(BRA);
                out.write(0x3f);
                out.write("xml version='".getBytes("US-ASCII"));
                out.write(version.getBytes("US-ASCII"));
                out.write(APOS);
                if (!("UTF-8".equalsIgnoreCase(encoding)))
                  {
                    out.write(" encoding='".getBytes("US-ASCII"));
                    out.write(encoding.getBytes("US-ASCII"));
                    out.write(APOS);
                  }
                if ((doc != null && doc.getXmlStandalone()) ||
                    "yes".equals(node.getUserData("standalone")))
                  {
                    out.write(" standalone='yes'".getBytes("US-ASCII"));
                  }
                out.write(0x3f);
                out.write(KET);
                out.write(eol.getBytes(encoding));
              }
            // TODO warn if not outputting the declaration would be a
            // problem
          }
        children = node.getFirstChild();
        if (children != null)
          {
            serialize(children, out);
          }
        break;
      case Node.DOCUMENT_TYPE_NODE:
        DocumentType doctype = (DocumentType) node;
        out.write(BRA);
        out.write(BANG);
        value = doctype.getNodeName();
        out.write(value.getBytes(encoding));
        String publicId = doctype.getPublicId();
        if (publicId != null)
          {
            out.write(" PUBLIC ".getBytes(encoding));
            out.write(APOS);
            out.write(publicId.getBytes(encoding));
            out.write(APOS);
          }
        String systemId = doctype.getSystemId();
        if (systemId != null)
          {
            out.write(" SYSTEM ".getBytes(encoding));
            out.write(APOS);
            out.write(systemId.getBytes(encoding));
            out.write(APOS);
          }
        String internalSubset = doctype.getInternalSubset();
        if (internalSubset != null)
          {
            out.write(internalSubset.getBytes(encoding));
          }
        out.write(KET);
        out.write(eol.getBytes(encoding));
        break;
      case Node.ENTITY_REFERENCE_NODE:
        value = "&" + node.getNodeValue() + ";";
        out.write(value.getBytes(encoding));
        break;
      case Node.PROCESSING_INSTRUCTION_NODE:
        value = "<?" + node.getNodeName() + " " + node.getNodeValue() + "?>";
        out.write(value.getBytes(encoding));
        Node pp = node.getParentNode();
        if (pp != null && pp.getNodeType() == Node.DOCUMENT_NODE)
          {
            out.write(eol.getBytes(encoding));
          }
        break;
      }
    if (defined)
      {
        undefine(uri);
      }
    if (next != null)
      {
        serialize(next, out);
      }
  }

  boolean isDefined(String uri)
  {
    return namespaces.containsKey(uri);
  }

  String define(String uri, String prefix)
  {
    while (namespaces.containsValue(prefix))
      {
        // Fabricate new prefix
        prefix = prefix + "_";
      }
    namespaces.put(uri, prefix);
    return prefix;
  }

  void undefine(String uri)
  {
    namespaces.remove(uri);
  }

  static String encode(String text)
  {
    int len = text.length();
    StringBuffer buf = null;
    for (int i = 0; i < len; i++)
      {
        char c = text.charAt(i);
        if (c == '<')
          {
            if (buf == null)
              {
                buf = new StringBuffer(text.substring(0, i));
              }
            buf.append("&lt;");
          }
        else if (c == '>')
          {
            if (buf == null)
              {
                buf = new StringBuffer(text.substring(0, i));
              }
            buf.append("&gt;");
          }
        else if (c == '&')
          {
            if (buf == null)
              {
                buf = new StringBuffer(text.substring(0, i));
              }
            buf.append("&amp;");
          }
        else if (c == '\'')
          {
            if (buf == null)
              {
                buf = new StringBuffer(text.substring(0, i));
              }
            buf.append("&apos;");
          }
        else if (c == '"')
          {
            if (buf == null)
              {
                buf = new StringBuffer(text.substring(0, i));
              }
            buf.append("&quot;");
          }
        else if (buf != null)
          {
            buf.append(c);
          }
      }
    return (buf == null) ? text : buf.toString();
  }

  String toString(Node node)
  {
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    try
      {
        serialize(node, out);
        return new String(out.toByteArray(), encoding);
      }
    catch (IOException e)
      {
        throw new RuntimeException(e.getMessage());
      }
  }
  
}
