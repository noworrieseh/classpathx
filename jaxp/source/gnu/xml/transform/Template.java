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
import gnu.xml.xpath.Root;
import gnu.xml.xpath.Selector;
import gnu.xml.xpath.Step;
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
  final Expr match;
  final TemplateNode node;
  final double priority;
  final int precedence;
  final String mode;

  Template(Stylesheet stylesheet, String name, Expr match, Node source,
           int precedence, double priority, String mode)
    throws TransformerConfigurationException
  {
    this.stylesheet = stylesheet;
    this.name = name;
    if (match instanceof Selector || match instanceof Step)
      {
        Expr root = new Root();
        Test nt = new NodeTypeTest((short) 0);
        Selector s = new Selector(Selector.DESCENDANT_OR_SELF,
                                  Collections.singletonList(nt));
        match = new Step(s, match);
        match = new Step(root, match);
      }
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

  boolean matches(Node context, Node node, String mode)
  {
    if (mode != null && !mode.equals(this.mode))
      {
        return false;
      }
    if (match == null)
      {
        return false;
      }
    Object ret = match.evaluate(context);
    if (ret != null && ret instanceof Collection)
      {
        Collection ns = (Collection) ret;
        return ns.contains(node);
      }
    return false;
  }

  /**
   * @param stylesheet the stylesheet
   * @param context the context node in the source document
   * @param parent the parent of result nodes
   * @param nextSibling if non-null, add result nodes before this node
   */
  void apply(Stylesheet stylesheet, Node context, String mode,
             Node parent, Node nextSibling)
    throws TransformerException
  {
    if (node != null)
      {
        node.apply(stylesheet, context, mode, parent, nextSibling);
      }
  }

  TemplateNode parse(Node source)
    throws TransformerConfigurationException
  {
    if (source == null)
      {
        return null;
      }
    Node children = source.getFirstChild();
    Node next = source.getNextSibling();
    
    String namespaceUri = source.getNamespaceURI();
    if (Stylesheet.XSL_NS.equals(namespaceUri) &&
        Node.ELEMENT_NODE == source.getNodeType())
      {
        Element element = (Element) source;
        try
          {
            String name = element.getLocalName();
            if ("apply-templates".equals(name))
              {
                String mode = element.getAttribute("mode");
                String s = element.getAttribute("select");
                if (s == null)
                  {
                    s = "child::node()";
                  }
                Expr select = (Expr) stylesheet.factory.xpath.compile(s);
                List sortKeys = parseSortKeys(children);
                return new ApplyTemplatesNode(null, parse(next),
                                              select, mode, sortKeys);
              }
            else if ("call-template".equals(name))
              {
                String tname = element.getAttribute("name");
                return new CallTemplateNode(null, parse(next), name);
              }
            else if ("value-of".equals(name))
              {
                String s = element.getAttribute("select");
                Expr select = (Expr) stylesheet.factory.xpath.compile(s);
                String doe = element.getAttribute("disable-output-escaping");
                boolean d = "yes".equals(doe);
                return new ValueOfNode(null, parse(next), select, d);
              }
            else if ("if".equals(name))
              {
                String t = element.getAttribute("test");
                Expr test = (Expr) stylesheet.factory.xpath.compile(t);
                return new IfNode(parse(children), parse(next), test);
              }
            else if ("choose".equals(name))
              {
                return new ChooseNode(parse(children), parse(next));
              }
            else if ("when".equals(name))
              {
                String t = element.getAttribute("test");
                Expr test = (Expr) stylesheet.factory.xpath.compile(t);
                return new WhenNode(parse(children), parse(next), test);
              }
            else if ("otherwise".equals(name))
              {
                return new OtherwiseNode(parse(children), parse(next));
              }
            else if ("element".equals(name))
              {
                String ename = element.getAttribute("name");
                String ns = element.getAttribute("namespace");
                String uas = element.getAttribute("used-attribute-sets");
                // TODO uas
                return new ElementNode(parse(children), parse(next),
                                       ename, ns);
              }
            else if ("attribute".equals(name))
              {
                String aname = element.getAttribute("name");
                String ns = element.getAttribute("namespace");
                return new AttributeNode(parse(children), parse(next),
                                         aname, ns);
              }
            else if ("text".equals(name))
              {
                String doe = element.getAttribute("disable-output-escaping");
                boolean d = "yes".equals(doe);
                return new TextNode(parse(children), parse(next), d);
              }
            // TODO processing-instruction
            // TODO comment
            // TODO copy
            // TODO number
            else if ("for-each".equals(name))
              {
                String s = element.getAttribute("select");
                Expr select = (Expr) stylesheet.factory.xpath.compile(s);
                List sortKeys = parseSortKeys(children);
                return new ForEachNode(parse(children), parse(next), select, sortKeys);
              }
            else if ("sort".equals(name))
              {
                return new DummyNode(null, parse(next));
              }
          }
        catch (XPathExpressionException e)
          {
            DOMSourceLocator l = new DOMSourceLocator(source);
            throw new TransformerConfigurationException(e.getMessage(), l, e);
          }
      }
    return new LiteralNode(parse(children), parse(next), source);
  }

  List parseSortKeys(Node node)
    throws XPathExpressionException
  {
    List ret = new LinkedList();
    while (node != null)
      {
        String namespaceUri = node.getNamespaceURI();
        if (Stylesheet.XSL_NS.equals(namespaceUri) &&
            Node.ELEMENT_NODE == node.getNodeType())
          {
            Element element = (Element) node;
            String name = element.getLocalName();

            if ("sort".equals(name))
              {
                String s = element.getAttribute("select");
                Expr select = (Expr) stylesheet.factory.xpath.compile(s);
                String lang = element.getAttribute("lang");
                String dataType = element.getAttribute("data-type");
                String order = element.getAttribute("order");
                boolean descending = "descending".equals(order);
                String caseOrder = element.getAttribute("case-order");
                int co =
                  "upper-first".equals(caseOrder) ? SortKey.UPPER_FIRST :
                  "lower-first".equals(caseOrder) ? SortKey.LOWER_FIRST :
                  SortKey.DEFAULT;
                ret.add(new SortKey(select, lang, dataType, descending, co));
              }
          }
    
        node = node.getNextSibling();
      }
    return ret.isEmpty() ? null : ret;
  }

}
