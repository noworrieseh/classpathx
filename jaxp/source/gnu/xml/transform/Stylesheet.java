/*
 * Stylesheet.java
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

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeSet;
import javax.xml.namespace.QName;
import javax.xml.transform.Source;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.stream.StreamSource;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.DOMException;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import gnu.xml.xpath.Expr;
import gnu.xml.xpath.Root;

/**
 * An XSL stylesheet.
 *
 * @author <a href='mailto:dog@gnu.org'>Chris Burdess</a>
 */
class Stylesheet
{

  static final String XSL_NS = "http://www.w3.org/1999/XSL/Transform";
  
  static final int OUTPUT_XML = 0;
  static final int OUTPUT_HTML = 1;
  static final int OUTPUT_TEXT = 2;

  final TransformerFactoryImpl factory;
  final int precedence;

  /**
   * Version of XSLT.
   */
  String version;

  /**
   * Set of element names for which we should strip whitespace.
   */
  Set stripSpace;

  /**
   * Set of element names for which we should preserve whitespace.
   */
  Set preserveSpace;

  /**
   * Output method.
   */
  int outputMethod;
  String outputPublicId;
  String outputSystemId;
  String outputEncoding;
  boolean outputIndent;

  // TODO keys
  // TODO decimal-format
  // TODO namespace-alias

  /**
   * Attribute-sets.
   */
  Map attributeSets;
  Map usedAttributeSets;

  /**
   * Variables (cannot be overridden by parameters)
   */
  Map variables;

  /**
   * Parameters (can be overridden)
   */
  Map parameters;

  /**
   * Templates.
   */
  List templates;

  Stylesheet(TransformerFactoryImpl factory, Document doc, int precedence)
    throws TransformerConfigurationException
  {
    this.factory = factory;
    this.precedence = precedence;
    stripSpace = new LinkedHashSet();
    preserveSpace = new LinkedHashSet();
    attributeSets = new LinkedHashMap();
    usedAttributeSets = new LinkedHashMap();
    variables = new LinkedHashMap();
    parameters = new LinkedHashMap();
    templates = new LinkedList();
    parse(doc.getDocumentElement(), true);
  }

  void parse(Node node, boolean root)
    throws TransformerConfigurationException
  {
    if (node == null)
      {
        return;
      }
    try
      {
        String namespaceUri = node.getNamespaceURI();
        if (XSL_NS.equals(namespaceUri) &&
            node.getNodeType() == Node.ELEMENT_NODE)
          {
            Element element = (Element) node;
            String name = element.getLocalName();
            if ("stylesheet".equals(name))
              {
                version = element.getAttribute("version");
                parse(element.getFirstChild(), false);
              }
            else if ("template".equals(name))
              {
                String tname = element.getAttribute("name");
                String m = element.getAttribute("match");
                Expr match = (m != null) ?
                  (Expr) factory.xpath.compile(m) : null;
                String priority = element.getAttribute("priority");
                String mode = element.getAttribute("mode");
                double p = (priority == null ||
                            priority.length() == 0) ?
                  Template.DEFAULT_PRIORITY :
                  Double.parseDouble(priority);
                templates.add(new Template(this,
                                           tname,
                                           match,
                                           element.getFirstChild(),
                                           precedence,
                                           p,
                                           mode));
                parse(element.getNextSibling(), false);
              }
            else if ("param".equals(name) ||
                     "variable".equals(name))
              {
                Map target = "variable".equals(name) ? variables : parameters;
                Node content = element.getFirstChild();
                String paramName = element.getAttribute("name");
                String select = element.getAttribute("select");
                if (select != null)
                  {
                    if (content != null)
                      {
                        throw new TransformerConfigurationException("parameter has both select and content", new DOMSourceLocator(element));
                      }
                    target.put(paramName,
                               factory.xpath.compile(select));
                  }
                else if (content != null)
                  {
                    target.put(paramName, content);
                  }
                else
                  {
                    target.put(paramName, "");
                  }
                parse(element.getNextSibling(), false);
              }
            else if ("include".equals(name) || "import".equals(name))
              {
                int p = "import".equals(name) ? -1 : 0;
                String systemId = element.getAttribute("href");
                Source source = new StreamSource(systemId);
                Stylesheet stylesheet =
                  factory.newStylesheet(source, precedence + p);
                templates.addAll(stylesheet.templates);
                parse(element.getNextSibling(), false);
              }
            else if ("output".equals(name))
              {
                String method = element.getAttribute("method");
                if ("xml".equals(method))
                  {
                    outputMethod = OUTPUT_XML;
                  }
                else if ("html".equals(method))
                  {
                    outputMethod = OUTPUT_HTML;
                  }
                else if ("text".equals(method))
                  {
                    outputMethod = OUTPUT_TEXT;
                  }
                else
                  {
                    throw new TransformerConfigurationException("unsupported output method: " + method, new DOMSourceLocator(element));
                  }
                outputPublicId = element.getAttribute("public-id");
                outputSystemId = element.getAttribute("system-id");
                outputEncoding = element.getAttribute("encoding");
                String indent = element.getAttribute("indent");
                outputIndent = "yes".equals(indent);
                parse(element.getNextSibling(), false);
              }
            else if ("preserve-space".equals(name))
              {
                String elements = element.getAttribute("elements");
                StringTokenizer st = new StringTokenizer(elements,
                                                         " ");
                while (st.hasMoreTokens())
                  {
                    preserveSpace.add(QName.valueOf(st.nextToken()));
                  }
                parse(element.getNextSibling(), false);
              }
            else if ("strip-space".equals(name))
              {
                String elements = element.getAttribute("elements");
                StringTokenizer st = new StringTokenizer(elements,
                                                         " ");
                while (st.hasMoreTokens())
                  {
                    stripSpace.add(QName.valueOf(st.nextToken()));
                  }
                parse(element.getNextSibling(), false);
              }
            // TODO keys
            // TODO decimal-format
            // TODO namespace-alias
            else if ("attribute-set".equals(name))
              {
                String asName = element.getAttribute("name");
                String uas = element.getAttribute("use-attribute-sets");
                attributeSets.put(asName, element.getFirstChild());
                if (uas != null)
                  {
                    usedAttributeSets.put(asName, uas);
                  }
              }
            else
              {
                // Forwards-compatible processing: ignore unknown XSL
                // elements
                parse(element.getNextSibling(), false);
              }
          }
        else if (root)
          {
            // Literal document element
            Attr versionNode =
              ((Element)node).getAttributeNodeNS(XSL_NS, "version");
            if (versionNode == null)
              {
                String msg = "no xsl:version attribute on literal result node";
                DOMSourceLocator l = new DOMSourceLocator(node);
                throw new TransformerConfigurationException(msg, l);
              }
            version = versionNode.getValue();
            Node rootClone = node.cloneNode(true);
            NamedNodeMap attrs = rootClone.getAttributes();
            attrs.removeNamedItemNS(XSL_NS, "version");
            templates.add(new Template(this,
                                       null,
                                       new Root(),
                                       rootClone,
                                       precedence,
                                       Template.DEFAULT_PRIORITY,
                                       null));
          }
        else
          {
            // Skip unknown elements, text, comments, etc
            parse(node.getNextSibling(), false);
          }
      }
    catch (DOMException e)
      {
        throw new TransformerConfigurationException(e);
      }
    catch (XPathExpressionException e)
      {
        throw new TransformerConfigurationException(e);
      }
  }

  void applyTemplates(Node context, Expr select, String mode,
                      Node parent, Node nextSibling)
    throws TransformerException
  {
    Object ret = select.evaluate(context);
    //System.out.println("applyTemplates: "+select+" selected "+ret);
    if (ret != null && ret instanceof Collection)
      {
        Collection ns = (Collection) ret;
        // TODO sort
        for (Iterator i = ns.iterator(); i.hasNext(); )
          {
            Node subject = (Node) i.next();
            applyTemplates(context, subject, mode, parent, nextSibling);
          }
      }
  }

  void applyTemplates(Node context, Node subject, String mode,
                      Node parent, Node nextSibling)
    throws TransformerException
  {
    //System.out.println("applyTemplates: subject="+subject);
    Set candidates = new TreeSet();
    for (Iterator j = templates.iterator(); j.hasNext(); )
      {
        Template t = (Template) j.next();
        if (t.matches(context, subject, mode))
          {
            candidates.add(t);
          }
      }
    //System.out.println("applyTemplates: candidates="+candidates);
    if (!candidates.isEmpty())
      {
        Template t =
          (Template) candidates.iterator().next();
        //System.out.println("applyTemplates: template="+t.expr+" subject="+subject);
        t.apply(this, subject, mode, parent, nextSibling);
      }
  }

  void callTemplate(Node context, String name, String mode,
                    Node parent, Node nextSibling)
    throws TransformerException
  {
    for (Iterator j = templates.iterator(); j.hasNext(); )
      {
        Template t = (Template) j.next();
        if (name.equals(t.name))
          {
            t.apply(this, context, mode, parent, nextSibling);
            return;
          }
      }
  }
    
}
