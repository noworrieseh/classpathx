/*
 * Template.java
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

import java.io.PrintStream;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.LinkedList;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.xpath.XPathExpressionException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import gnu.xml.xpath.Expr;
import gnu.xml.xpath.NameTest;
import gnu.xml.xpath.NodeTypeTest;
import gnu.xml.xpath.Pattern;
import gnu.xml.xpath.Root;
import gnu.xml.xpath.Selector;
import gnu.xml.xpath.Steps;
import gnu.xml.xpath.Test;
import gnu.xml.xpath.XPathImpl;

/**
 * A template in an XSL stylesheet.
 *
 * @author <a href='mailto:dog@gnu.org'>Chris Burdess</a>
 */
class Template
  implements Comparable
{

  static final double DEFAULT_PRIORITY = 0.5d;

  final Stylesheet stylesheet;
  final String name;
  final Pattern match;
  final TemplateNode node;
  final double priority;
  final int precedence;
  final String mode;

  Template(Stylesheet stylesheet, String name, Pattern match, Node source,
           int precedence, double priority, String mode)
    throws TransformerConfigurationException
  {
    this.stylesheet = stylesheet;
    this.name = name;
    this.match = match;
    // adjust priority if necessary
    // see XSLT section 5.5
    Test test = getNodeTest(match);
    if (test != null)
      {
        if (test instanceof NameTest)
          {
            NameTest nameTest = (NameTest) test;
            if (nameTest.matchesAny() ||
                nameTest.matchesAnyLocalName())
              {
                priority = -0.25d;
              }
            else
              {
                priority = 0.0d;
              }
          }
        else
          {
            NodeTypeTest nodeTypeTest = (NodeTypeTest) test;
            if (nodeTypeTest.getNodeType() ==
                Node.PROCESSING_INSTRUCTION_NODE &&
                nodeTypeTest.getData() != null)
              {
                priority = 0.0d;
              }
            else
              {
                priority = -0.5d;
              }
          }
      }
    node = parse(source);
    this.precedence = precedence;
    this.priority = priority;
    this.mode = mode;
  }

  public int compareTo(Object other)
  {
    if (other instanceof Template)
      {
        Template t = (Template) other;
        int d = t.precedence - precedence;
        if (d != 0)
          {
            return d;
          }
        double d2 = t.priority - priority;
        if (d2 != 0.0d)
          {
            return (int) Math.round(d2 * 1000.0d);
          }
      }
    return 0;
  }

  Test getNodeTest(Expr expr)
  {
    if (expr instanceof Selector)
      {
        Selector selector = (Selector) expr;
        Test[] tests = selector.getTests();
        if (tests.length > 0)
          {
            return tests[0];
          }
      }
    return null;
  }

  boolean matches(String mode, Node node)
  {
    if (mode != null && !mode.equals(this.mode))
      {
        return false;
      }
    if (match == null)
      {
        return false;
      }
    return match.matches(node);
  }

  /**
   * @param stylesheet the stylesheet
   * @param parent the parent of result nodes
   * @param context the context node in the source document
   * @param pos the context position
   * @param len the context size
   * @param nextSibling if non-null, add result nodes before this node
   */
  void apply(Stylesheet stylesheet, String mode,
             Node context, int pos, int len,
             Node parent, Node nextSibling)
    throws TransformerException
  {
    //System.err.println("...applying " + toString() + " to " + context);
    if (node != null)
      {
        node.apply(stylesheet, mode,
                   context, pos, len,
                   parent, nextSibling);
      }
  }

  /**
   * apply-templates
   */
  TemplateNode parseApplyTemplates(Node node, Node children, Node next)
    throws TransformerConfigurationException, XPathExpressionException
  {
    NamedNodeMap attrs = node.getAttributes();
    String mode = getAttribute(attrs, "mode");
    String s = getAttribute(attrs, "select");
    if (s == null)
      {
        s = "child::node()";
      }
    List sortKeys = parseSortKeys(children);
    List withParams = parseWithParams(children);
    Expr select = (Expr) stylesheet.xpath.compile(s);
    return new ApplyTemplatesNode(null, parse(next),
                                  select, mode,
                                  sortKeys, withParams);
  }

  /**
   * call-template
   */
  TemplateNode parseCallTemplate(Node node, Node children, Node next)
    throws TransformerConfigurationException, XPathExpressionException
  {
    NamedNodeMap attrs = node.getAttributes();
    String name = getRequiredAttribute(attrs, "name", node);
    List withParams = parseWithParams(children);
    return new CallTemplateNode(null, parse(next), name,
                                withParams);
  }
  
  /**
   * value-of
   */
  TemplateNode parseValueOf(Node node, Node children, Node next)
    throws TransformerConfigurationException, XPathExpressionException
  {
    NamedNodeMap attrs = node.getAttributes();
    String s = getRequiredAttribute(attrs, "select", node);
    String doe = getAttribute(attrs, "disable-output-escaping");
    boolean d = "yes".equals(doe);
    Expr select = (Expr) stylesheet.xpath.compile(s);
    return new ValueOfNode(null, parse(next), select, d);
  }
  
  /**
   * for-each
   */
  TemplateNode parseForEach(Node node, Node children, Node next)
    throws TransformerConfigurationException, XPathExpressionException
  {
    NamedNodeMap attrs = node.getAttributes();
    String s = getRequiredAttribute(attrs, "select", node);
    List sortKeys = parseSortKeys(children);
    Expr select = (Expr) stylesheet.xpath.compile(s);
    return new ForEachNode(parse(children), parse(next), select, sortKeys);
  }
  
  /**
   * if
   */
  TemplateNode parseIf(Node node, Node children, Node next)
    throws TransformerConfigurationException, XPathExpressionException
  {
    NamedNodeMap attrs = node.getAttributes();
    String t = getRequiredAttribute(attrs, "test", node);
    Expr test = (Expr) stylesheet.xpath.compile(t);
    return new IfNode(parse(children), parse(next), test);
  }
  
  /**
   * when
   */
  TemplateNode parseWhen(Node node, Node children, Node next)
    throws TransformerConfigurationException, XPathExpressionException
  {
    NamedNodeMap attrs = node.getAttributes();
    String t = getRequiredAttribute(attrs, "test", node);
    Expr test = (Expr) stylesheet.xpath.compile(t);
    return new WhenNode(parse(children), parse(next), test);
  }
  
  /**
   * element
   */
  TemplateNode parseElement(Node node, Node children, Node next)
    throws TransformerConfigurationException, XPathExpressionException
  {
    NamedNodeMap attrs = node.getAttributes();
    String name = getRequiredAttribute(attrs, "name", node);
    String ns = getAttribute(attrs, "namespace");
    String uas = getAttribute(attrs, "used-attribute-sets");
    TemplateNode n = stylesheet.parseAttributeValueTemplate(name, node);
    return new ElementNode(parse(children), parse(next), n, ns, uas);
  }

  /**
   * attribute
   */
  TemplateNode parseAttribute(Node node, Node children, Node next)
    throws TransformerConfigurationException, XPathExpressionException
  {
    NamedNodeMap attrs = node.getAttributes();
    String name = getRequiredAttribute(attrs, "name", node);
    String ns = getAttribute(attrs, "namespace");
    TemplateNode n = stylesheet.parseAttributeValueTemplate(name, node);
    return new AttributeNode(parse(children), parse(next), n, ns);
  }
  
  /**
   * text
   */
  TemplateNode parseText(Node node, Node children, Node next)
    throws TransformerConfigurationException, XPathExpressionException
  {
    NamedNodeMap attrs = node.getAttributes();
    String doe = getAttribute(attrs, "disable-output-escaping");
    boolean d = "yes".equals(doe);
    return new TextNode(parse(children), parse(next), d);
  }
  
  /**
   * processing-instruction
   */
  TemplateNode parseProcessingInstruction(Node node, Node children, Node next)
    throws TransformerConfigurationException, XPathExpressionException
  {
    NamedNodeMap attrs = node.getAttributes();
    String name = getRequiredAttribute(attrs, "name", node);
    return new ProcessingInstructionNode(parse(children),
                                         parse(next), name);
  }
  
  /**
   * number
   */
  TemplateNode parseNumber(Node node, Node children, Node next)
    throws TransformerConfigurationException, XPathExpressionException
  {
    NamedNodeMap attrs = node.getAttributes();
    String v = getAttribute(attrs, "value");
    String format = getAttribute(attrs, "format");
    if (format == null)
      {
        format = "1";
      }
    String lang = getAttribute(attrs, "lang");
    String lv = getAttribute(attrs, "letter-value");
    int letterValue = "traditional".equals(lv) ?
      AbstractNumberNode.TRADITIONAL :
      AbstractNumberNode.ALPHABETIC;
    String gs = getAttribute(attrs, "grouping-separator");
    String gz = getAttribute(attrs, "grouping-size");
    int gz2 = (gz != null && gz.length() > 0) ?
      Integer.parseInt(gz) : 1;
    if (v != null && v.length() > 0)
      {
        Expr value = (Expr) stylesheet.xpath.compile(v);
        return new NumberNode(parse(children), parse(next),
                              value, format, lang,
                              letterValue, gs, gz2);
      }
    else
      {
        String l = getAttribute(attrs, "level");
        int level =
          "multiple".equals(l) ? NodeNumberNode.MULTIPLE :
                      "any".equals(l) ? NodeNumberNode.ANY :
                      NodeNumberNode.SINGLE;
        String c = getAttribute(attrs, "count");
        if (c == null)
          {
            c = "countable()";
          }
        Pattern count = (Pattern) stylesheet.xpath.compile(c);
        String f = getAttribute(attrs, "from");
        if (f == null)
          {
            f = ".";
          }
        Expr from = (Expr) stylesheet.xpath.compile(f);
        return new NodeNumberNode(parse(children), parse(next),
                                  level, count, from,
                                  format, lang,
                                  letterValue, gs, gz2);
      }
  }
  
  /**
   * copy-of
   */
  TemplateNode parseCopyOf(Node node, Node children, Node next)
    throws TransformerConfigurationException, XPathExpressionException
  {
    NamedNodeMap attrs = node.getAttributes();
    String s = getRequiredAttribute(attrs, "select", node);
    Expr select = (Expr) stylesheet.xpath.compile(s);
    return new CopyOfNode(parse(children), parse(next), select);
  }
  
  /**
   * Main parse function.
   */
  TemplateNode parse(Node node)
    throws TransformerConfigurationException
  {
    // Hack to associate the document function with its declaring node
    stylesheet.current = node;
    if (node == null)
      {
        return null;
      }
    Node children = node.getFirstChild();
    Node next = node.getNextSibling();
    try
      {
        String namespaceUri = node.getNamespaceURI();
        if (Stylesheet.XSL_NS.equals(namespaceUri) &&
            Node.ELEMENT_NODE == node.getNodeType())
          {
            String name = node.getLocalName();
            if ("apply-templates".equals(name))
              {
                return parseApplyTemplates(node, children, next);
              }
            else if ("call-template".equals(name))
              {
                return parseCallTemplate(node, children, next);
              }
            else if ("value-of".equals(name))
              {
                return parseValueOf(node, children, next);
              }
            else if ("for-each".equals(name))
              {
                return parseForEach(node, children, next);
              }
            else if ("if".equals(name))
              {
                return parseIf(node, children, next);
              }
            else if ("choose".equals(name))
              {
                return new ChooseNode(parse(children), parse(next));
              }
            else if ("when".equals(name))
              {
                return parseWhen(node, children, next);
              }
            else if ("otherwise".equals(name))
              {
                return new OtherwiseNode(parse(children), parse(next));
              }
            else if ("element".equals(name))
              {
                return parseElement(node, children, next);
              }
            else if ("attribute".equals(name))
              {
                return parseAttribute(node, children, next);
              }
            else if ("text".equals(name))
              {
                return parseText(node, children, next);
              }
            else if ("copy".equals(name))
              {
                return new CopyNode(parse(children), parse(next));
              }
            else if ("processing-instruction".equals(name))
              {
                return parseProcessingInstruction(node, children, next);
              }
            else if ("comment".equals(name))
              {
                return new CommentNode(parse(children), parse(next));
              }
            else if ("number".equals(name))
              {
                return parseNumber(node, children, next);
              }
            else if ("param".equals(name) ||
                     "variable".equals(name))
              {
                boolean global = "variable".equals(name);
                NamedNodeMap attrs = node.getAttributes();
                TemplateNode content = parse(children);
                String paramName = getRequiredAttribute(attrs, "name", node);
                String select = getAttribute(attrs, "select");
                if (select != null)
                  {
                    if (content != null)
                      {
                        String msg = "parameter '" + paramName +
                          "' has both select and content";
                        DOMSourceLocator l = new DOMSourceLocator(node);
                        throw new TransformerConfigurationException(msg, l);
                      }
                    Expr expr = (Expr) stylesheet.xpath.compile(select);
                    return new ParameterNode(null, parse(next),
                                             paramName, expr, global);
                  }
                else
                  {
                    return new ParameterNode(content, parse(next),
                                             paramName, null, global);
                  }
              }
            else if ("copy-of".equals(name))
              {
                return parseCopyOf(node, children, next);
              }
            else
              {
                // Pass over any other XSLT nodes
                return parse(next);
              }
          }
        switch (node.getNodeType())
          {
          case Node.TEXT_NODE:
            // Determine whether to strip whitespace
            Text text = (Text) node;
            if (!stylesheet.isPreserved(text))
              {
                return parse(next);
              }
            break;
          case Node.COMMENT_NODE:
            // Ignore comments
            return parse(next);
          case Node.ELEMENT_NODE:
            // Check for attribute value templates and use-attribute-sets
            NamedNodeMap attrs = node.getAttributes();
            boolean convert = false;
            String useAttributeSets = null;
            int len = attrs.getLength();
            for (int i = 0; i < len; i++)
              {
                Node attr = attrs.item(i);
                String value = attr.getNodeValue();
                if (Stylesheet.XSL_NS.equals(attr.getNamespaceURI()) &&
                    "use-attribute-sets".equals(attr.getLocalName()))
                  {
                    useAttributeSets = value;
                    convert = true;
                    break;
                  }
                int start = value.indexOf('{');
                int end = value.indexOf('}');
                if (start != -1 && end > start)
                  {
                    convert = true;
                    break;
                  }
              }
            if (convert)
              {
                // Create an element-producing template node instead
                // with appropriate attribute-producing child template nodes
                TemplateNode child = parse(children);
                for (int i = 0; i < len; i++)
                  {
                    Node attr = attrs.item(i);
                    if (Stylesheet.XSL_NS.equals(attr.getNamespaceURI()) &&
                        "use-attribute-sets".equals(attr.getLocalName()))
                      {
                        continue;
                      }
                    String value = attr.getNodeValue();
                    String aname = attr.getNodeName();
                    TemplateNode grandchild =
                      stylesheet.parseAttributeValueTemplate(value, node);
                    TemplateNode n =
                      stylesheet.parseAttributeValueTemplate(aname, node);
                    child = new AttributeNode(grandchild, child,
                                              n, attr.getNamespaceURI());
                  }
                String ename = node.getNodeName();
                TemplateNode n =
                  stylesheet.parseAttributeValueTemplate(ename, node);
                return new ElementNode(child, parse(next),
                                       n, namespaceUri, useAttributeSets);
              }
            // Otherwise fall through
            break;
          }
      }
    catch (XPathExpressionException e)
      {
        DOMSourceLocator l = new DOMSourceLocator(node);
        throw new TransformerConfigurationException(e.getMessage(), l, e);
      }
    return new LiteralNode(parse(children), parse(next), node);
  }

  final List parseSortKeys(Node node)
    throws TransformerConfigurationException, XPathExpressionException
  {
    List ret = new LinkedList();
    while (node != null)
      {
        String namespaceUri = node.getNamespaceURI();
        if (Stylesheet.XSL_NS.equals(namespaceUri) &&
            Node.ELEMENT_NODE == node.getNodeType() &&
            "sort".equals(node.getLocalName()))
          {
            NamedNodeMap attrs = node.getAttributes();
            String s = getRequiredAttribute(attrs, "select", node);
            Expr select = (Expr) stylesheet.xpath.compile(s);
            String lang = getAttribute(attrs, "lang");
            String dataType = getAttribute(attrs, "data-type");
            String order = getAttribute(attrs, "order");
            boolean descending = "descending".equals(order);
            String caseOrder = getAttribute(attrs, "case-order");
            int co =
              "upper-first".equals(caseOrder) ? SortKey.UPPER_FIRST :
              "lower-first".equals(caseOrder) ? SortKey.LOWER_FIRST :
              SortKey.DEFAULT;
            ret.add(new SortKey(select, lang, dataType, descending, co));
          }
        node = node.getNextSibling();
      }
    return ret.isEmpty() ? null : ret;
  }

  final List parseWithParams(Node node)
    throws TransformerConfigurationException, XPathExpressionException
  {
    List ret = new LinkedList();
    while (node != null)
      {
        String namespaceUri = node.getNamespaceURI();
        if (Stylesheet.XSL_NS.equals(namespaceUri) &&
            Node.ELEMENT_NODE == node.getNodeType() &&
            "with-param".equals(node.getLocalName()))
          {
            NamedNodeMap attrs = node.getAttributes();
            TemplateNode content = parse(node.getFirstChild());
            String name = getRequiredAttribute(attrs, "name", node);
            String select = getAttribute(attrs, "select");
            if (select != null)
              {
                if (content != null)
                  {
                    String msg = "parameter '" + name +
                      "' has both select and content";
                    DOMSourceLocator l = new DOMSourceLocator(node);
                    throw new TransformerConfigurationException(msg, l);
                  }
                Expr expr = (Expr) stylesheet.xpath.compile(select);
                ret.add(new WithParam(name, expr));
              }
            else
              {
                ret.add(new WithParam(name, content));
              }
          }
        node = node.getNextSibling();
      }
    return ret.isEmpty() ? null : ret;
  }

  static final String getAttribute(NamedNodeMap attrs, String name)
  {
    Node attr = attrs.getNamedItem(name);
    return (attr == null) ? null : attr.getNodeValue();
  }

  static final String getRequiredAttribute(NamedNodeMap attrs, String name,
                                           Node source)
    throws TransformerConfigurationException
  {
    String value = getAttribute(attrs, name);
    if (value == null || value.length() == 0)
      {
        String msg =
          name + " attribute is required on " + source.getNodeName();
        DOMSourceLocator l = new DOMSourceLocator(source);
        throw new TransformerConfigurationException(msg, l);
      }
    return value;
  }

  public String toString()
  {
    /*
    StringBuffer buf = new StringBuffer(getClass().getName());
    buf.append('[');
    if (name != null)
      {
        buf.append("name=");
        buf.append(name);
      }
    else if (match != null)
      {
        buf.append("match=");
        buf.append(match);
      }
    buf.append(']');
    return buf.toString();
    */
    return (name != null) ? name : match.toString();
  }

  void list(PrintStream out)
  {
    out.println(toString());
    if (node != null)
      {
        node.list(1, out, true);
      }
  }

}
