package gnu.xml.validation.xmlschema;

import gnu.xml.validation.datatype.Annotation;
import gnu.xml.validation.datatype.AtomicSimpleType;
import gnu.xml.validation.datatype.ListSimpleType;
import gnu.xml.validation.datatype.SimpleType;
import gnu.xml.validation.datatype.Type;
import gnu.xml.validation.datatype.UnionSimpleType;

import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;
import javax.xml.XMLConstants;
import javax.xml.namespace.QName;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

/**
 * Parses an XML Schema DOM tree, constructing a compiled internal
 * representation.
 *
 * @author <a href='mailto:dog@gnu.org'>Chris Burdess</a>
 */
class XMLSchemaBuilder
{

  XMLSchema schema;

  void parseSchema(Node node)
  {
    String uri = node.getNamespaceURI();
    String name = node.getLocalName();
    if (XMLConstants.W3C_XML_SCHEMA_NS_URI.equals(uri) &&
        node.getNodeType() == Node.ELEMENT_NODE)
      {
        if ("schema".equals(name))
          {
            NamedNodeMap attrs = node.getAttributes();
            String targetNamespace = getAttribute(attrs, "targetNamespace");
            String version = getAttribute(attrs, "version");
            String fd = getAttribute(attrs, "finalDefault");
            int finalDefault = parseFullDerivationSet(fd);
            String bd = getAttribute(attrs, "blockDefault");
            int blockDefault = parseBlockSet(bd);
            String afd = getAttribute(attrs, "attributeFormDefault");
            boolean attributeFormQualified = "qualified".equals(afd);
            String efd = getAttribute(attrs, "elementFormDefault");
            boolean elementFormQualified = "qualified".equals(efd);
            schema = new XMLSchema(targetNamespace, version,
                                   finalDefault, blockDefault,
                                   attributeFormQualified,
                                   elementFormQualified);
            for (Node child = node.getFirstChild(); child != null;
                 child = child.getNextSibling())
              {
                parseTopLevelElement(child);
              }
            return;
          }
      }
    // TODO throw schema exception
  }

  void parseTopLevelElement(Node node)
  {
    String uri = node.getNamespaceURI();
    String name = node.getLocalName();
    if (XMLConstants.W3C_XML_SCHEMA_NS_URI.equals(uri) &&
        node.getNodeType() == Node.ELEMENT_NODE)
      {
        if ("element".equals(name))
          {
            ElementDeclaration ed =
              (ElementDeclaration) parseElement(node, null);
            schema.elementDeclarations.put(ed.name, ed);
            // TODO
          }
        else if ("attribute".equals(name))
          {
            AttributeDeclaration ad =
              (AttributeDeclaration) parseAttribute(node, true);
            schema.attributeDeclarations.put(ad.name, ad);
            // TODO
          }
        else if ("type".equals(name))
          {
            // TODO
          }
        else if ("group".equals(name))
          {
            // TODO
          }
        else if ("attributeGroup".equals(name))
          {
            // TODO
          }
        else if ("notation".equals(name))
          {
            // TODO
          }
        else if ("identityConstraint".equals(name))
          {
            // TODO
          }
      }
    // TODO throw schema exception
  }

  Object parseAttribute(Node node, boolean scope)
  {
    NamedNodeMap attrs = node.getAttributes();
    String def = getAttribute(attrs, "default");
    String fixed = getAttribute(attrs, "fixed");
    int constraintType = AttributeDeclaration.NONE;
    String constraintValue = null;
    if (def != null)
      {
        constraintType = AttributeDeclaration.DEFAULT;
        constraintValue = def;
      }
    else if (fixed != null)
      {
        constraintType = AttributeDeclaration.FIXED;
        constraintValue = fixed;
      }
    // TODO form = (qualified | unqualified)
    String attrName = getAttribute(attrs, "name");
    String attrNamespace = getAttribute(attrs, "targetNamespace");
    String ref = getAttribute(attrs, "ref");
    String use = getAttribute(attrs, "use");
    String type = getAttribute(attrs, "type");
    SimpleType datatype = (type == null) ? null :
      parseSimpleType(type);
    Annotation annotation = null;
    for (Node child = node.getFirstChild(); child != null;
         child = child.getNextSibling())
      {
        String uri = child.getNamespaceURI();
        String name = child.getLocalName();
        if (XMLConstants.W3C_XML_SCHEMA_NS_URI.equals(uri) &&
            node.getNodeType() == Node.ELEMENT_NODE)
          {
            if ("annotation".equals(name))
              {
                annotation = parseAnnotation(child);
              }
            else if ("simpleType".equals(name))
              {
                datatype = parseSimpleType(child);
              }
            else
              {
                // TODO throw schema exception
              }
          }
      }
    if (scope)
      {
        return new AttributeDeclaration(scope,
                                        constraintType,
                                        constraintValue,
                                        new QName(attrNamespace, attrName),
                                        datatype,
                                        annotation);
      }
    else 
      {
        boolean required = "required".equals(use);
        // TODO ref
        AttributeDeclaration decl = (ref == null) ?
              new AttributeDeclaration(scope,
                                       AttributeDeclaration.NONE,
                                       null,
                                       new QName(attrNamespace, attrName),
                                       datatype,
                                       annotation) :
              /*schema.getAttributeDeclaration(ref)*/ null;
        return new AttributeUse(required,
                                constraintType,
                                constraintValue,
                                decl);
      }
  }

  int parseFullDerivationSet(String value)
  {
    int ret = XMLSchema.FINAL_NONE;
    if ("#all".equals(value))
      {
        ret = XMLSchema.FINAL_ALL;
      }
    else
      {
        StringTokenizer st = new StringTokenizer(value, " ");
        while (st.hasMoreTokens())
          {
            String token = st.nextToken();
            if ("extension".equals(token))
              {
                ret |= XMLSchema.FINAL_EXTENSION;
              }
            else if ("restriction".equals(token))
              {
                ret |= XMLSchema.FINAL_RESTRICTION;
              }
            else if ("list".equals(token))
              {
                ret |= XMLSchema.FINAL_LIST;
              }
            else if ("union".equals(token))
              {
                ret |= XMLSchema.FINAL_UNION;
              }
          }
      }
    return ret;
  }

  int parseSimpleTypeDerivationSet(String value)
  {
    int ret = XMLSchema.FINAL_NONE;
    if ("#all".equals(value))
      {
        ret = XMLSchema.FINAL_ALL;
      }
    else
      {
        StringTokenizer st = new StringTokenizer(value, " ");
        while (st.hasMoreTokens())
          {
            String token = st.nextToken();
            if ("list".equals(token))
              {
                ret |= XMLSchema.FINAL_LIST;
              }
            else if ("union".equals(token))
              {
                ret |= XMLSchema.FINAL_UNION;
              }
            else if ("restriction".equals(token))
              {
                ret |= XMLSchema.FINAL_RESTRICTION;
              }
          }
      }
    return ret;
  }

  int parseBlockSet(String value)
  {
    int ret = XMLSchema.BLOCK_NONE;
    if ("#all".equals(value))
      {
        ret = XMLSchema.BLOCK_ALL;
      }
    else
      {
        StringTokenizer st = new StringTokenizer(value, " ");
        while (st.hasMoreTokens())
          {
            String token = st.nextToken();
            if ("extension".equals(token))
              {
                ret |= XMLSchema.BLOCK_EXTENSION;
              }
            else if ("restriction".equals(token))
              {
                ret |= XMLSchema.BLOCK_RESTRICTION;
              }
            else if ("substitution".equals(token))
              {
                ret |= XMLSchema.BLOCK_SUBSTITUTION;
              }
          }
      }
    return ret;
  }

  Object parseElement(Node node, ElementDeclaration parent)
  {
    NamedNodeMap attrs = node.getAttributes();
    Integer minOccurs = null;
    Integer maxOccurs = null;
    Node parentNode = node.getParentNode();
    boolean notTopLevel = !"schema".equals(parentNode.getLocalName());
    if (notTopLevel)
      {
        String ref = getAttribute(attrs, "ref");
        if (ref != null)
          {
            minOccurs = getOccurrence(getAttribute(attrs, "minOccurs"));
            maxOccurs = getOccurrence(getAttribute(attrs, "maxOccurs"));
            // TODO resolve top-level element declaration
            ElementDeclaration ad = null;
            return new Particle(minOccurs, maxOccurs, ad);
          }
      }
    String elementName = getAttribute(attrs, "name");
    String elementNamespace = getAttribute(attrs, "targetNamespace");
    String type = getAttribute(attrs, "type");
    Type datatype = (type != null) ? parseSimpleType(type) : null;
    int scope = (parent == null) ?
      XMLSchema.GLOBAL :
      XMLSchema.LOCAL;
    String def = getAttribute(attrs, "default");
    String fixed = getAttribute(attrs, "fixed");
    int constraintType = AttributeDeclaration.NONE;
    String constraintValue = null;
    if (def != null)
      {
        constraintType = AttributeDeclaration.DEFAULT;
        constraintValue = def;
      }
    else if (fixed != null)
      {
        constraintType = AttributeDeclaration.FIXED;
        constraintValue = fixed;
      }
    String sg = getAttribute(attrs, "substitutionGroup");
    QName substitutionGroup = QName.valueOf(sg);
    String sgPrefix = substitutionGroup.getPrefix();
    if (sgPrefix != null && !"".equals(sgPrefix))
      {
        String sgName = substitutionGroup.getLocalPart();
        String sgNamespace = node.lookupNamespaceURI(sgPrefix);
        substitutionGroup = new QName(sgNamespace, sgName);
      }
    
    String block = getAttribute(attrs, "block");
    int substitutionGroupExclusions = (block == null) ?
      schema.blockDefault :
      parseBlockSet(block);
    String final_ = getAttribute(attrs, "final");
    int disallowedSubstitutions = (final_ == null) ?
      schema.finalDefault :
      parseFullDerivationSet(final_);
    
    boolean nillable = "true".equals(getAttribute(attrs, "nillable"));
    boolean isAbstract = "true".equals(getAttribute(attrs, "abstract"));
    
    if (notTopLevel)
      {
        minOccurs = getOccurrence(getAttribute(attrs, "minOccurs"));
        maxOccurs = getOccurrence(getAttribute(attrs, "maxOccurs"));
        String form = getAttribute(attrs, "form");
        if (form != null)
          {
            if ("qualified".equals(form))
              {
                elementNamespace = schema.targetNamespace;
              }
          }
        else if (schema.elementFormQualified)
          {
            elementNamespace = schema.targetNamespace;
          }
      }
    ElementDeclaration ed =
      new ElementDeclaration(new QName(elementNamespace, elementName),
                             datatype,
                             scope, parent,
                             constraintType, constraintValue,
                             nillable,
                             substitutionGroup, 
                             substitutionGroupExclusions, 
                             disallowedSubstitutions,
                             isAbstract);
    
    for (Node child = node.getFirstChild(); child != null;
         child = child.getNextSibling())
      {
        String uri = child.getNamespaceURI();
        String name = child.getLocalName();
        if (XMLConstants.W3C_XML_SCHEMA_NS_URI.equals(uri) &&
            node.getNodeType() == Node.ELEMENT_NODE)
          {
            if ("annotation".equals(name))
              {
                ed.annotation = parseAnnotation(child);
              }
            else if ("simpleType".equals(name) && datatype == null)
              {
                ed.datatype = parseSimpleType(child);
              }
            else if ("complexType".equals(name) && datatype == null)
              {
                ed.datatype = parseComplexType(child, ed);
              }
            else
              {
                // throw schema exception
              }
          }
      }

    if (notTopLevel)
      {
        return new Particle(minOccurs, maxOccurs, ed);
      }
    else
      {
        return ed;
      }
  }

  Integer getOccurrence(String value)
  {
    if (value == null)
      {
        return new Integer(1);
      }
    else if ("unbounded".equals(value))
      {
        return null;
      }
    else
      {
        return new Integer(value);
      }
  }

  SimpleType parseSimpleType(String name)
  {
    return (SimpleType) Type.forName(name);
  }

  SimpleType parseSimpleType(Node simpleType)
  {
    NamedNodeMap attrs = simpleType.getAttributes();
    String typeFinal = getAttribute(attrs, "final");
    if (typeFinal == null)
      {
        Node schema = simpleType.getParentNode();
        while (schema != null && !"schema".equals(schema.getLocalName()))
          {
            schema = schema.getParentNode();
          }
        if (schema != null)
          {
            NamedNodeMap schemaAttrs = schema.getAttributes();
            typeFinal = getAttribute(schemaAttrs, "finalDefault");
          }
      }
    int typeFinality = parseSimpleTypeDerivationSet(typeFinal);
    String typeName = getAttribute(attrs, "name");
    // TODO
    return null;
  }

  Type parseComplexType(Node complexType, ElementDeclaration parent)
  {
    // TODO
    return null;
  }

  Annotation parseAnnotation(Node node)
  {
    // TODO
    return null;
  }

  private static String getAttribute(NamedNodeMap attrs, String name)
  {
    Node attr = attrs.getNamedItem(name);
    return (attr == null) ? null : attr.getNodeValue();
  }
  
}

