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
import javax.xml.namespace.NamespaceContext;
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
  implements NamespaceContext, XPathFunctionResolver, Cloneable
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

  /**
   * Keys.
   */
  Map keys;

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
  LinkedList templates;

  TemplateNode builtInNodeTemplate;
  TemplateNode builtInTextTemplate;

  /**
   * Holds the current node while parsing.
   * Necessary to associate the document function with its declaring node,
   * and resolve namespaces.
   */
  transient Node current;

  /**
   * Set by a terminating message.
   */
  boolean terminated;

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
    outputCdataSectionElements = new LinkedHashSet();
    if (parent == null)
      {
        bindings = new Bindings(this);
        attributeSets = new LinkedHashMap();
        usedAttributeSets = new LinkedHashMap();
        namespaceAliases = new LinkedHashMap();
        templates = new LinkedList();
        keys = new LinkedHashMap();
        decimalFormats = new LinkedHashMap();
        initDefaultDecimalFormat();
      }
    else
      {
        bindings = parent.bindings;
        attributeSets = parent.attributeSets;
        usedAttributeSets = parent.usedAttributeSets;
        namespaceAliases = parent.namespaceAliases;
        templates = parent.templates;
        keys = parent.keys;
        decimalFormats = parent.decimalFormats;
      }

    xpath = (XPathImpl) factory.xpathFactory.newXPath();
    xpath.setNamespaceContext(this);
    xpath.setXPathVariableResolver(bindings);
    xpath.setXPathFunctionResolver(this);

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
    
    if ("yes".equals(System.getProperty("xsl.debug")))
      {
        System.err.println("Stylesheet: " + doc.getDocumentURI());
        for (Iterator i = templates.iterator(); i.hasNext(); )
          {
            Template t = (Template) i.next();
            t.list(System.err);
            System.err.println("--------------------");
          }
      }
  }
  
  void initDefaultDecimalFormat()
  {
    DecimalFormat defaultDecimalFormat = new DecimalFormat();
    DecimalFormatSymbols symbols = new DecimalFormatSymbols();
    symbols.setDecimalSeparator('.');
    symbols.setGroupingSeparator(',');
    symbols.setPercent('%');
    symbols.setPerMill('\u2030');
    symbols.setZeroDigit('0');
    symbols.setDigit('#');
    symbols.setPatternSeparator(';');
    symbols.setInfinity("Infinity");
    symbols.setNaN("NaN");
    symbols.setMinusSign('-');
    defaultDecimalFormat.setDecimalFormatSymbols(symbols);
    decimalFormats.put(null, defaultDecimalFormat);
  }
  
  // -- Cloneable --

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

  // -- NamespaceContext --

  public String getNamespaceURI(String prefix)
  {
    return (current == null) ? null : current.lookupNamespaceURI(prefix);
  }

  public String getPrefix(String namespaceURI)
  {
    return (current == null) ? null : current.lookupPrefix(namespaceURI);
  }

  public Iterator getPrefixes(String namespaceURI)
  {
    // TODO
    return Collections.singleton(getPrefix(namespaceURI)).iterator();
  }
  
  // -- Apply templates --
  
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
    //System.err.println("applyTemplates:");
    //System.err.println("\tcontext="+context);
    Set candidates = new TreeSet();
    for (Iterator j = templates.iterator(); j.hasNext(); )
      {
        Template t = (Template) j.next();
        boolean isMatch = t.matches(mode, context);
        //System.err.println("applyTemplates: "+context+" "+t+"="+isMatch);
        if (isMatch)
          {
            candidates.add(t);
          }
      }
    //System.err.println("\tcandidates="+candidates);
    if (candidates.isEmpty())
      {
        // Apply built-in template
        //System.err.println("\tbuiltInTemplate context="+context);
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
            if (!isPreserved((Text) context))
              {
                return;
              }
            // fall through
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
        //System.err.println("\ttemplate="+t+" context="+context);
        t.apply(this, mode,
                context, pos, len,
                parent, nextSibling);
      }
  }

  void callTemplate(QName name, String mode,
                    Node context, int pos, int len,
                    Node parent, Node nextSibling)
    throws TransformerException
  {
    Set candidates = new TreeSet();
    for (Iterator j = templates.iterator(); j.hasNext(); )
      {
        Template t = (Template) j.next();
        if (name.equals(t.name))
          {
            candidates.add(t);
          }
      }
    if (candidates.isEmpty())
      {
        throw new TransformerException("template '" + name + "' not found");
      }
    Template t =
      (Template) candidates.iterator().next();
    //System.err.println("*** calling "+t);
    //System.err.println("*** bindings="+bindings);
    t.apply(this, mode,
            context, pos, len,
            parent, nextSibling);
  }

  /**
   * template
   */
  final Template parseTemplate(Node node, NamedNodeMap attrs)
    throws TransformerConfigurationException, XPathExpressionException
  {
    String n = getAttribute(attrs, "name");
    QName name = (n == null) ? null : getQName(n);
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
   * key
   */
  final void parseKey(Node node, NamedNodeMap attrs)
    throws TransformerConfigurationException, XPathExpressionException
  {
    String name = getRequiredAttribute(attrs, "name", node);
    String m = getRequiredAttribute(attrs, "match", node);
    String u = getRequiredAttribute(attrs, "use", node);
    Expr use = (Expr) xpath.compile(u);
    try
      {
        Pattern match = (Pattern) xpath.compile(m);
        Key key = new Key(name, match, use);
        keys.put(name, key);
      }
    catch (ClassCastException e)
      {
        throw new TransformerConfigurationException("invalid pattern: " + m);
      }
  }

  /**
   * decimal-format
   */
  final void parseDecimalFormat(Node node, NamedNodeMap attrs)
    throws TransformerConfigurationException
  {
    String dfName = getAttribute(attrs, "name");
    DecimalFormat df = new DecimalFormat();
    DecimalFormatSymbols symbols = new DecimalFormatSymbols();
    symbols.setDecimalSeparator(parseDFChar(attrs, "decimal-separator", '.'));
    symbols.setGroupingSeparator(parseDFChar(attrs, "grouping-separator", ','));
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
        // Merge the two use-attribute-sets specifiers,
        // ensuring that only one of each name is present
        String oldUas = (String) usedAttributeSets.get(name);
        if (oldUas != null)
          {
            Set set = new LinkedHashSet();
            StringTokenizer st = new StringTokenizer(oldUas, " ");
            while (st.hasMoreTokens())
              {
                set.add(st.nextToken());
              }
            st = new StringTokenizer(uas, " ");
            while (st.hasMoreTokens())
              {
                set.add(st.nextToken());
              }
            StringBuffer buf = new StringBuffer();
            for (Iterator i = set.iterator(); i.hasNext(); )
              {
                if (buf.length() != 0)
                  {
                    buf.append(' ');
                  }
                buf.append(i.next());
              }
            uas = buf.toString();
          }
        usedAttributeSets.put(name, uas);
      }
    // Create the attribute-set template node(s)
    TemplateNode last = (TemplateNode) attributeSets.get(name);
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
            last = new AttributeNode(parse(children), last, n, ns, ctx);
          }
      }
    attributeSets.put(name, last);
  }

  /**
   * Parse top-level elements.
   */
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
                templates.addFirst(parseTemplate(node, attrs));
              }
            else if ("param".equals(name) ||
                     "variable".equals(name))
              {
                boolean global = "variable".equals(name);
                Object content = parse(node.getFirstChild());
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
            else if ("key".equals(name))
              {
                parseKey(node, attrs);
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

  final NameTest parseNameTest(String token)
  {
    if ("*".equals(token))
      {
        return new NameTest(null, true, true);
      }
    else if (token.endsWith(":*"))
      {
        QName qName = getQName(token.substring(0, token.length() - 2));
        return new NameTest(qName, true, false);
      }
    else
      {
        QName qName = getQName(token);
        return new NameTest(qName, false, false);
      }
  }

  final QName getQName(String name)
  {
    QName qName = QName.valueOf(name);
    String prefix = qName.getPrefix();
    String uri = qName.getNamespaceURI();
    if (prefix != null && (uri == null || uri.length() == 0))
      {
        uri = getNamespaceURI(prefix);
        String localName = qName.getLocalName();
        qName = new QName(uri, localName, prefix);
      }
    return qName;
  }

  final TemplateNode parseAttributeValueTemplate(String value, Node source)
    throws TransformerConfigurationException, XPathExpressionException
  {
    current = source;
    // Check for attribute value template    
    int start = value.lastIndexOf('{');
    int end = value.lastIndexOf('}');
    TemplateNode ret = null;
    Document doc = source.getOwnerDocument();
    if (start != -1 && end > start &&
        // Check for doubled accolades to abort processing
        !(start > 0 && value.charAt(start - 1) == '{' &&
          value.charAt(end - 1) == '}'))
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

  public XPathFunction resolveFunction(QName name, int arity)
  {
    String uri = name.getNamespaceURI();
    if (XSL_NS.equals(uri) || uri == null || uri.length() == 0)
      {
        String localName = name.getLocalName();
        if ("document".equals(localName) && (arity == 1 || arity == 2))
          {
            if (current == null)
              {
                throw new RuntimeException("current is null");
              }
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
  
  // -- Parsing --

  /**
   * apply-templates
   */
  final TemplateNode parseApplyTemplates(Node node, Node children, Node next)
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
    Expr select = (Expr) xpath.compile(s);
    return new ApplyTemplatesNode(null, parse(next),
                                  select, mode,
                                  sortKeys, withParams);
  }

  /**
   * call-template
   */
  final TemplateNode parseCallTemplate(Node node, Node children, Node next)
    throws TransformerConfigurationException, XPathExpressionException
  {
    NamedNodeMap attrs = node.getAttributes();
    String n = getRequiredAttribute(attrs, "name", node);
    QName name = getQName(n);
    List withParams = parseWithParams(children);
    return new CallTemplateNode(null, parse(next), name,
                                withParams);
  }
  
  /**
   * value-of
   */
  final TemplateNode parseValueOf(Node node, Node children, Node next)
    throws TransformerConfigurationException, XPathExpressionException
  {
    NamedNodeMap attrs = node.getAttributes();
    String s = getRequiredAttribute(attrs, "select", node);
    String doe = getAttribute(attrs, "disable-output-escaping");
    boolean d = "yes".equals(doe);
    Expr select = (Expr) xpath.compile(s);
    return new ValueOfNode(null, parse(next), select, d);
  }
  
  /**
   * for-each
   */
  final TemplateNode parseForEach(Node node, Node children, Node next)
    throws TransformerConfigurationException, XPathExpressionException
  {
    NamedNodeMap attrs = node.getAttributes();
    String s = getRequiredAttribute(attrs, "select", node);
    List sortKeys = parseSortKeys(children);
    Expr select = (Expr) xpath.compile(s);
    return new ForEachNode(parse(children), parse(next), select, sortKeys);
  }
  
  /**
   * if
   */
  final TemplateNode parseIf(Node node, Node children, Node next)
    throws TransformerConfigurationException, XPathExpressionException
  {
    NamedNodeMap attrs = node.getAttributes();
    String t = getRequiredAttribute(attrs, "test", node);
    Expr test = (Expr) xpath.compile(t);
    return new IfNode(parse(children), parse(next), test);
  }
  
  /**
   * when
   */
  final TemplateNode parseWhen(Node node, Node children, Node next)
    throws TransformerConfigurationException, XPathExpressionException
  {
    NamedNodeMap attrs = node.getAttributes();
    String t = getRequiredAttribute(attrs, "test", node);
    Expr test = (Expr) xpath.compile(t);
    return new WhenNode(parse(children), parse(next), test);
  }
  
  /**
   * element
   */
  final TemplateNode parseElement(Node node, Node children, Node next)
    throws TransformerConfigurationException, XPathExpressionException
  {
    NamedNodeMap attrs = node.getAttributes();
    String name = getRequiredAttribute(attrs, "name", node);
    String ns = getAttribute(attrs, "namespace");
    String uas = getAttribute(attrs, "use-attribute-sets");
    TemplateNode n = parseAttributeValueTemplate(name, node);
    return new ElementNode(parse(children), parse(next), n, ns, uas, node);
  }

  /**
   * attribute
   */
  final TemplateNode parseAttribute(Node node, Node children, Node next)
    throws TransformerConfigurationException, XPathExpressionException
  {
    NamedNodeMap attrs = node.getAttributes();
    String name = getRequiredAttribute(attrs, "name", node);
    String ns = getAttribute(attrs, "namespace");
    TemplateNode n = parseAttributeValueTemplate(name, node);
    return new AttributeNode(parse(children), parse(next), n, ns, node);
  }
  
  /**
   * text
   */
  final TemplateNode parseText(Node node, Node children, Node next)
    throws TransformerConfigurationException, XPathExpressionException
  {
    NamedNodeMap attrs = node.getAttributes();
    String doe = getAttribute(attrs, "disable-output-escaping");
    boolean d = "yes".equals(doe);
    return new TextNode(parse(children), parse(next), d);
  }
  
  /**
   * copy
   */
  final TemplateNode parseCopy(Node node, Node children, Node next)
    throws TransformerConfigurationException, XPathExpressionException
  {
    NamedNodeMap attrs = node.getAttributes();
    String uas = getAttribute(attrs, "use-attribute-sets");
    return new CopyNode(parse(children), parse(next), uas);
  }
  
  /**
   * processing-instruction
   */
  final TemplateNode parseProcessingInstruction(Node node, Node children,
                                                Node next)
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
  final TemplateNode parseNumber(Node node, Node children, Node next)
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
        Expr value = (Expr) xpath.compile(v);
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
        Pattern count = null;
        if (c != null)
          {
            try
              {
                count = (Pattern) xpath.compile(c);
              }
            catch (ClassCastException e)
              {
                throw new TransformerConfigurationException("invalid pattern: " +
                                                            c);
              }
          }
        String f = getAttribute(attrs, "from");
        if (f == null)
          {
            f = ".";
          }
        Expr from = (Expr) xpath.compile(f);
        return new NodeNumberNode(parse(children), parse(next),
                                  level, count, from,
                                  format, lang,
                                  letterValue, gs, gz2);
      }
  }
  
  /**
   * copy-of
   */
  final TemplateNode parseCopyOf(Node node, Node children, Node next)
    throws TransformerConfigurationException, XPathExpressionException
  {
    NamedNodeMap attrs = node.getAttributes();
    String s = getRequiredAttribute(attrs, "select", node);
    Expr select = (Expr) xpath.compile(s);
    return new CopyOfNode(parse(children), parse(next), select);
  }
  
  /**
   * message
   */
  final TemplateNode parseMessage(Node node, Node children, Node next)
    throws TransformerConfigurationException, XPathExpressionException
  {
    NamedNodeMap attrs = node.getAttributes();
    String t = getAttribute(attrs, "terminate");
    boolean terminate = "yes".equals(t);
    return new MessageNode(parse(children), parse(next), terminate);
  }
  
  /**
   * Parse template-level elements.
   */
  final TemplateNode parse(Node node)
    throws TransformerConfigurationException
  {
    // Hack to associate the document function with its declaring node
    current = node;
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
                return parseCopy(node, children, next);
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
                    Expr expr = (Expr) xpath.compile(select);
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
            else if ("message".equals(name))
              {
                return parseMessage(node, children, next);
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
            if (!isPreserved(text))
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
                    String ns = attr.getNamespaceURI();
                    String aname = attr.getNodeName();
                    if (Stylesheet.XSL_NS.equals(ns) &&
                        "use-attribute-sets".equals(attr.getLocalName()))
                      {
                        continue;
                      }
                    String value = attr.getNodeValue();
                    TemplateNode grandchild =
                      parseAttributeValueTemplate(value, node);
                    TemplateNode n =
                      parseAttributeValueTemplate(aname, node);
                    child = new AttributeNode(grandchild, child, n, ns, attr);
                  }
                String ename = node.getNodeName();
                TemplateNode n =
                  parseAttributeValueTemplate(ename, node);
                return new ElementNode(child, parse(next),
                                       n, namespaceUri, useAttributeSets,
                                       node);
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
            Expr select = (Expr) xpath.compile(s);
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
                Expr expr = (Expr) xpath.compile(select);
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

}

