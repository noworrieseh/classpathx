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

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
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
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFunction;
import javax.xml.xpath.XPathFunctionResolver;
import javax.xml.xpath.XPathExpressionException;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.DOMException;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import gnu.xml.xpath.Expr;
import gnu.xml.xpath.NameTest;
import gnu.xml.xpath.Pattern;
import gnu.xml.xpath.Selector;
import gnu.xml.xpath.Root;
import gnu.xml.xpath.XPathImpl;

/**
 * An XSL stylesheet.
 *
 * @author <a href='mailto:dog@gnu.org'>Chris Burdess</a>
 */
class Stylesheet
  implements XPathFunctionResolver, Cloneable
{

  static final String XSL_NS = "http://www.w3.org/1999/XSL/Transform";
  
  static final int OUTPUT_XML = 0;
  static final int OUTPUT_HTML = 1;
  static final int OUTPUT_TEXT = 2;

  final TransformerFactoryImpl factory;
  TransformerImpl transformer;
  final XPathImpl xpath;
  final String systemId;
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
   * Output options.
   */
  int outputMethod;
  String outputVersion;
  String outputEncoding;
  boolean outputOmitXmlDeclaration;
  boolean outputStandalone;
  String outputPublicId;
  String outputSystemId;
  Collection outputCdataSectionElements;
  boolean outputIndent;
  String outputMediaType;

  // TODO keys

  /**
   * Decimal formats.
   */
  Map decimalFormats;
  
  /**
   * Namespace aliases.
   */
  Map namespaceAliases;

  /**
   * Attribute-sets.
   */
  Map attributeSets;
  Map usedAttributeSets;

  /**
   * Variable and parameter bindings.
   */
  Bindings bindings;

  /**
   * Templates.
   */
  List templates;

  TemplateNode builtInNodeTemplate;
  TemplateNode builtInTextTemplate;

  /**
   * Holds the current node while parsing.
   * Necessary to associate the document function with its declaring node.
   */
  transient Node current;

  Stylesheet(TransformerFactoryImpl factory,
             Stylesheet parent,
             Document doc,
             String systemId,
             int precedence)
    throws TransformerConfigurationException
  {
    this.factory = factory;
    this.systemId = systemId;
    this.precedence = precedence;
    stripSpace = new LinkedHashSet();
    preserveSpace = new LinkedHashSet();
    attributeSets = new LinkedHashMap();
    usedAttributeSets = new LinkedHashMap();
    decimalFormats = new LinkedHashMap();
    namespaceAliases = new LinkedHashMap();
    outputCdataSectionElements = new LinkedHashSet();
    if (parent == null)
      {
        bindings = new Bindings();
        templates = new LinkedList();
      }
    else
      {
        bindings = parent.bindings;
        templates = parent.templates;
      }

    factory.xpathFactory.setXPathVariableResolver(bindings);
    factory.xpathFactory.setXPathFunctionResolver(this);
    xpath = (XPathImpl) factory.xpathFactory.newXPath();

    builtInNodeTemplate =
      new ApplyTemplatesNode(null, null,
                             new Selector(Selector.CHILD,
                                          Collections.EMPTY_LIST),
                             null, null, null);
    builtInTextTemplate =
      new ValueOfNode(null, null,
                      new Selector(Selector.SELF, Collections.EMPTY_LIST),
                      false);
    
    parse(doc.getDocumentElement(), true);
    
    /*for (Iterator i = templates.iterator(); i.hasNext(); )
      {
        Template t = (Template) i.next();
        t.list(System.out);
        System.out.println("--------------------");
      }
      */
  }

  public Object clone()
  {
    try
      {
        Stylesheet clone = (Stylesheet) super.clone();
        clone.bindings = (Bindings) bindings.clone();
        return clone;
      }
    catch (CloneNotSupportedException e)
      {
        throw new Error(e.getMessage());
      }
  }

  /**
   * template
   */
  final Template parseTemplate(Node node, NamedNodeMap attrs)
    throws TransformerConfigurationException, XPathExpressionException
  {
    String name = getAttribute(attrs, "name");
    String m = getAttribute(attrs, "match");
    Pattern match = (m != null) ? (Pattern) xpath.compile(m) : null;
    String p = getAttribute(attrs, "priority");
    String mode = getAttribute(attrs, "mode");
    double priority = (p == null) ? Template.DEFAULT_PRIORITY :
      Double.parseDouble(p);
    return new Template(this, name, match,
                        node.getFirstChild(),
                        precedence, priority, mode);
  }

  /**
   * output
   */
  final void parseOutput(Node node, NamedNodeMap attrs)
    throws TransformerConfigurationException
  {
    String method = getAttribute(attrs, "method");
    if ("xml".equals(method) || method == null)
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
        String msg = "unsupported output method: " + method;
        DOMSourceLocator l = new DOMSourceLocator(node);
        throw new TransformerConfigurationException(msg, l);
      }
    outputPublicId = getAttribute(attrs, "public-id");
    outputSystemId = getAttribute(attrs, "system-id");
    outputEncoding = getAttribute(attrs, "encoding");
    String indent = getAttribute(attrs, "indent");
    if (indent != null)
      {
        outputIndent = "yes".equals(indent);
      }
    outputVersion = getAttribute(attrs, "version");
    String omitXmlDecl = getAttribute(attrs, "omit-xml-declaration");
    if (omitXmlDecl != null)
      {
        outputOmitXmlDeclaration = "yes".equals(omitXmlDecl);
      }
    String standalone = getAttribute(attrs, "standalone");
    if (standalone != null)
      {
        outputStandalone = "yes".equals(standalone);
      }
    outputMediaType = getAttribute(attrs, "media-type");
    String cdataSectionElements =
      getAttribute(attrs, "cdata-section-elements");
    if (cdataSectionElements != null)
      {
        StringTokenizer st = new StringTokenizer(cdataSectionElements, " ");
        while (st.hasMoreTokens())
          {
            outputCdataSectionElements.add(st.nextToken());
          }
      }
  }

  /**
   * keys
   */
  final void parseKeys(Node node, NamedNodeMap attrs)
    throws TransformerConfigurationException, XPathExpressionException
  {
    // TODO
    throw new UnsupportedOperationException("not yet implemented");
  }

  /**
   * decimal-format
   */
  final void parseDecimalFormat(Node node, NamedNodeMap attrs)
    throws TransformerConfigurationException
  {
    String dfName = getRequiredAttribute(attrs, "name", node);
    DecimalFormat df = new DecimalFormat();
    DecimalFormatSymbols symbols = new DecimalFormatSymbols();
    symbols.setDecimalSeparator(parseDFChar(attrs, "decimal-separator", '.'));
    symbols.setGroupingSeparator(parseDFChar(attrs, "grouping-spearator", ','));
    symbols.setInfinity(parseDFString(attrs, "infinity", "Infinity"));
    symbols.setMinusSign(parseDFChar(attrs, "minus-sign", '-'));
    symbols.setNaN(parseDFString(attrs, "NaN", "NaN"));
    symbols.setPercent(parseDFChar(attrs, "percent", '%'));
    symbols.setPerMill(parseDFChar(attrs, "per-mille", '\u2030'));
    symbols.setZeroDigit(parseDFChar(attrs, "zero-digit", '0'));
    symbols.setDigit(parseDFChar(attrs, "digit", '#'));
    symbols.setPatternSeparator(parseDFChar(attrs, "pattern-separator", ';'));
    df.setDecimalFormatSymbols(symbols);
    decimalFormats.put(dfName, df);
  }

  private final char parseDFChar(NamedNodeMap attrs, String name, char def)
    throws TransformerConfigurationException
  {
    Node attr = attrs.getNamedItem(name);
    try
      {
        return (attr == null) ? def : attr.getNodeValue().charAt(0);
      }
    catch (StringIndexOutOfBoundsException e)
      {
        throw new TransformerConfigurationException("empty attribute '" +
                                                    name +
                                                    "' in decimal-format", e);
      }
  }

  private final String parseDFString(NamedNodeMap attrs, String name,
                                     String def)
  {
    Node attr = attrs.getNamedItem(name);
    return (attr == null) ? def : attr.getNodeValue();
  }
  
  /**
   * namespace-alias
   */
  final void parseNamespaceAlias(Node node, NamedNodeMap attrs)
    throws TransformerConfigurationException
  {
    String sp = getRequiredAttribute(attrs, "stylesheet-prefix", node);
    String rp = getRequiredAttribute(attrs, "result-prefix", node);
    namespaceAliases.put(sp, rp);
  }

  /**
   * attribute-set
   */
  final void parseAttributeSet(Node node, NamedNodeMap attrs)
    throws TransformerConfigurationException, XPathExpressionException
  {
    String name = getRequiredAttribute(attrs, "name", node);
    String uas = getAttribute(attrs, "use-attribute-sets");
    if (uas != null)
      {
        usedAttributeSets.put(name, uas);
      }
    TemplateNode last = null;
    for (Node ctx = node.getLastChild(); ctx != null;
         ctx = ctx.getPreviousSibling())
      {
        String ctxNamespaceUri = ctx.getNamespaceURI();
        if (XSL_NS.equals(ctxNamespaceUri) &&
            ctx.getNodeType() == Node.ELEMENT_NODE &&
            "attribute".equals(ctx.getLocalName()))
          {
            NamedNodeMap aattrs = ctx.getAttributes();
            String aname = getRequiredAttribute(aattrs, "name", ctx);
            String ns = getAttribute(aattrs, "namespace");
            TemplateNode n = parseAttributeValueTemplate(aname, node);
            Node children = ctx.getFirstChild();
            last = new AttributeNode(parse(children), last, n, ns);
          }
      }
    attributeSets.put(name, last);
  }

  final TemplateNode parse(Node node)
    throws TransformerConfigurationException
  {
    if (node == null)
      {
        return null;
      }
    Node next = node.getNextSibling();
    return new LiteralNode(null, parse(next), node);
  }
  
  void parse(Node node, boolean root)
    throws TransformerConfigurationException
  {
    current = node;
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
            String name = node.getLocalName();
            NamedNodeMap attrs = node.getAttributes();
            if ("stylesheet".equals(name))
              {
                version = getAttribute(attrs, "version");
                parse(node.getFirstChild(), false);
                return;
              }
            else if ("template".equals(name))
              {
                templates.add(parseTemplate(node, attrs));
              }
            else if ("param".equals(name) ||
                     "variable".equals(name))
              {
                boolean global = "variable".equals(name);
                Object content = node.getFirstChild();
                String paramName = getAttribute(attrs, "name");
                String select = getAttribute(attrs, "select");
                if (select != null && select.length() > 0)
                  {
                    if (content != null)
                      {
                        String msg = "parameter '" + paramName +
                          "' has both select and content";
                        DOMSourceLocator l = new DOMSourceLocator(node);
                        throw new TransformerConfigurationException(msg, l);
                      }
                    content = xpath.compile(select);
                  }
                if (content == null)
                  {
                    content = "";
                  }
                bindings.set(paramName, content, global);
              }
            else if ("include".equals(name) || "import".equals(name))
              {
                int delta = "import".equals(name) ? -1 : 0;
                String href = getRequiredAttribute(attrs, "href", node);
                Source source;
                synchronized (factory.resolver)
                  {
                    if (transformer != null)
                      {
                        factory.resolver
                          .setUserResolver(transformer.getURIResolver());
                        factory.resolver
                          .setUserListener(transformer.getErrorListener());
                      }
                    source = factory.resolver.resolve(systemId, href);
                  }
                factory.newStylesheet(source, precedence + delta, this);
              }
            else if ("output".equals(name))
              {
                parseOutput(node, attrs);
              }
            else if ("preserve-space".equals(name))
              {
                String elements =
                  getRequiredAttribute(attrs, "elements", node);
                StringTokenizer st = new StringTokenizer(elements,
                                                         " \t\n\r");
                while (st.hasMoreTokens())
                  {
                    preserveSpace.add(parseNameTest(st.nextToken()));
                  }
              }
            else if ("strip-space".equals(name))
              {
                String elements =
                  getRequiredAttribute(attrs, "elements", node);
                StringTokenizer st = new StringTokenizer(elements,
                                                         " \t\n\r");
                while (st.hasMoreTokens())
                  {
                    stripSpace.add(parseNameTest(st.nextToken()));
                  }
              }
            else if ("keys".equals(name))
              {
                parseKeys(node, attrs);
              }
            else if ("decimal-format".equals(name))
              {
                parseDecimalFormat(node, attrs);
              }
            else if ("namespace-alias".equals(name))
              {
                parseNamespaceAlias(node, attrs);
              }
            else if ("attribute-set".equals(name))
              {
                parseAttributeSet(node, attrs);
              }
            parse(node.getNextSibling(), false);
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
    catch (TransformerException e)
      {
        DOMSourceLocator l = new DOMSourceLocator(node);
        throw new TransformerConfigurationException(e.getMessage(), l, e);
      }
    catch (DOMException e)
      {
        DOMSourceLocator l = new DOMSourceLocator(node);
        throw new TransformerConfigurationException(e.getMessage(), l, e);
      }
    catch (XPathExpressionException e)
      {
        DOMSourceLocator l = new DOMSourceLocator(node);
        throw new TransformerConfigurationException(e.getMessage(), l, e);
      }
  }

  NameTest parseNameTest(String token)
  {
    if ("*".equals(token))
      {
        return new NameTest("", true, true);
      }
    else if (token.endsWith(":*"))
      {
        return new NameTest(token.substring(0, token.length() - 2),
                            true, false);
      }
    else
      {
        return new NameTest(token, false, false);
      }
  }

  final TemplateNode parseAttributeValueTemplate(String value, Node source)
    throws TransformerConfigurationException, XPathExpressionException
  {
    // Check for attribute value template    
    int start = value.lastIndexOf('{');
    int end = value.lastIndexOf('}');
    TemplateNode ret = null;
    Document doc = source.getOwnerDocument();
    if (start != -1 && end > start)
      {
        while (value.length() > 0 && start != -1 && end > start)
          {
            // Verbatim text at end
            String sub = value.substring(end + 1);
            if (sub.length() > 0)
              {
                ret = new LiteralNode(null, ret, doc.createTextNode(sub));
              }
            // Expression text
            String expr = value.substring(start + 1, end);
            if (expr.length() == 0)
              {
                String msg =
                  "attribute value template must contain expression";
                DOMSourceLocator l = new DOMSourceLocator(source);
                throw new TransformerConfigurationException(msg, l);
              }
            Expr select = (Expr) xpath.compile(expr);
            ret = new ValueOfNode(null, ret, select, false);
            // work backwards through the text
            value = value.substring(0, start);
            start = value.lastIndexOf('{');
            end = value.lastIndexOf('}');
          }
        if (value.length() > 0)
          {
            // Verbatim text at beginning
            ret = new LiteralNode(null, ret, doc.createTextNode(value));
          }
      }
    else
      {
        // Verbatim
        ret = new LiteralNode(null, ret, doc.createTextNode(value));
      }
    return ret;
  }

  static final String getAttribute(NamedNodeMap attrs, String name)
  {
    return Template.getAttribute(attrs, name);
  }

  static final String getRequiredAttribute(NamedNodeMap attrs, String name,
                                           Node node)
    throws TransformerConfigurationException
  {
    return Template.getRequiredAttribute(attrs, name, node);
  }
  
  boolean isPreserved(Text text)
  {
    // Check characters in text
    String value = text.getData();
    if (value != null)
      {
        int len = value.length();
        for (int i = 0; i < len; i++)
          {
            char c = value.charAt(i);
            if (c != 0x20 && c != 0x09 && c != 0x0a && c != 0x0d)
              {
                return true;
              }
          }
      }
    // Check parent node
    Node ctx = text.getParentNode();
    for (Iterator i = preserveSpace.iterator(); i.hasNext(); )
      {
        NameTest preserveTest = (NameTest) i.next();
        if (preserveTest.matches(ctx, 1, 1))
          {
            boolean override = false;
            for (Iterator j = stripSpace.iterator(); j.hasNext(); )
              {
                NameTest stripTest = (NameTest) j.next();
                if (stripTest.matches(ctx, 1, 1))
                  {
                    override = true;
                    break;
                  }
              }
            if (!override)
              {
                return true;
              }
          }
      }
    // Check whether any ancestor specified xml:space
    while (ctx != null)
      {
        if (ctx.getNodeType() == Node.ELEMENT_NODE)
          {
            Element element = (Element) ctx;
            String xmlSpace = element.getAttribute("xml:space");
            if ("default".equals(xmlSpace))
              {
                break;
              }
            else if ("preserve".equals(xmlSpace))
              {
                return true;
              }
            else if ("text".equals(ctx.getLocalName()) &&
                     XSL_NS.equals(ctx.getNamespaceURI()))
              {
                // xsl:text implies xml:space='preserve'
                return true;
              }
          }
        ctx = ctx.getParentNode();
      }
    return false;
  }

  void applyTemplates(Expr select, String mode,
                      Node context, int pos, int len,
                      Node parent, Node nextSibling)
    throws TransformerException
  {
    Object ret = select.evaluate(context, pos, len);
    if (ret != null && ret instanceof Collection)
      {
        Collection ns = (Collection) ret;
        int l = ns.size();
        int p = 1;
        for (Iterator i = ns.iterator(); i.hasNext(); )
          {
            Node node = (Node) i.next();
            applyTemplates(mode,
                           node, p++, l,
                           parent, nextSibling);
          }
      }
  }

  void applyTemplates(String mode,
                      Node context, int pos, int len,
                      Node parent, Node nextSibling)
    throws TransformerException
  {
    //System.out.println("applyTemplates:");
    //System.out.println("\tcontext="+context);
    Set candidates = new TreeSet();
    for (Iterator j = templates.iterator(); j.hasNext(); )
      {
        Template t = (Template) j.next();
        boolean isMatch = t.matches(mode, context);
        //System.out.println("applyTemplates: "+context+" "+t+"="+isMatch);
        if (isMatch)
          {
            candidates.add(t);
          }
      }
    //System.out.println("\tcandidates="+candidates);
    if (candidates.isEmpty())
      {
        // Apply built-in template
        //System.out.println("\tbuiltInTemplate context="+context);
        switch (context.getNodeType())
          {
          case Node.ELEMENT_NODE:
          case Node.DOCUMENT_NODE:
          case Node.DOCUMENT_FRAGMENT_NODE:
            builtInNodeTemplate.apply(this, mode,
                                      context, pos, len,
                                      parent, nextSibling);
            break;
          case Node.TEXT_NODE:
          case Node.ATTRIBUTE_NODE:
            builtInTextTemplate.apply(this, mode,
                                      context, pos, len,
                                      parent, nextSibling);
            break;
          }
      }
    else
      {
        Template t =
          (Template) candidates.iterator().next();
        //System.out.println("\ttemplate="+t+" context="+context);
        t.apply(this, mode,
                context, pos, len,
                parent, nextSibling);
      }
  }

  void callTemplate(String name, String mode,
                    Node context, int pos, int len,
                    Node parent, Node nextSibling)
    throws TransformerException
  {
    
    for (Iterator j = templates.iterator(); j.hasNext(); )
      {
        Template t = (Template) j.next();
        if (name.equals(t.name))
          {
            //System.err.println("*** calling "+t);
            //System.err.println("*** bindings="+bindings);
            t.apply(this, mode,
                    context, pos, len,
                    parent, nextSibling);
            return;
          }
      }
    throw new TransformerException("template '" + name + "' not found");
  }

  public XPathFunction resolveFunction(QName name, int arity)
  {
    String uri = name.getNamespaceURI();
    if (XSL_NS.equals(uri) || uri == null || uri.length() == 0)
      {
        String localName = name.getLocalName();
        if ("document".equals(localName) && (arity == 1 || arity == 2))
          {
            return new DocumentFunction(this, current);
          }
        else if ("key".equals(localName) && (arity == 2))
          {
            return new KeyFunction(this);
          }
        else if ("format-number".equals(localName) &&
                 (arity == 2 || arity == 3))
          {
            return new FormatNumberFunction(this);
          }
        else if ("current".equals(localName) && (arity == 0))
          {
            return new CurrentFunction();
          }
        else if ("unparsed-entity-uri".equals(localName) && (arity == 1))
          {
            return new UnparsedEntityUriFunction();
          }
        else if ("generate-id".equals(localName) &&
                 (arity == 1 || arity == 0))
          {
            return new GenerateIdFunction();
          }
        else if ("system-property".equals(localName) && (arity == 1))
          {
            return new SystemPropertyFunction();
          }
      }
    return null;
  }
  
}

