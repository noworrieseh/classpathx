# Process this file with automake to generate Makefile.in

# Makefile.am
# Copyright (C) 2004 The Free Software Foundation
# 
# This file is part of GNU JAXP, a library.
#
# GNU JAXP is free software; you can redistribute it and/or modify
# it under the terms of the GNU General Public License as published by
# the Free Software Foundation; either version 2 of the License, or
# (at your option) any later version.
# 
# GNU JAXP is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
# GNU General Public License for more details.
# 
# You should have received a copy of the GNU General Public License
# along with this program; if not, write to the Free Software
# Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
#
# Linking this library statically or dynamically with other modules is
# making a combined work based on this library.  Thus, the terms and
# conditions of the GNU General Public License cover the whole
# combination.
#
# As a special exception, the copyright holders of this library give you
# permission to link this library with independent modules to produce an
# executable, regardless of the license terms of these independent
# modules, and to copy and distribute the resulting executable under
# terms of your choice, provided that you also meet, for each linked
# independent module, the terms and conditions of the license of that
# module.  An independent module is a module which is not derived from
# or based on this library.  If you modify this library, you may extend
# this exception to your version of the library, but you are not
# obliged to do so.  If you do not wish to do so, delete this
# exception statement from your version. 

JAVADOC = javadoc
JAR = @JAR@
JAVAC = @JAVAC@
JAVAH = @JAVAH@

SOFLAGS=        @SOFLAGS@
LIBMAJOR=       @XMLJ_VERSION_MAJOR@
LIBVERSION=     @XMLJ_VERSION_MAJOR@.@XMLJ_VERSION_MINOR@

GCJ_CLASS_FLAGS = -fassume-compiled -I$(srcdir)/source
AM_GCJFLAGS = -fjni -fassume-compiled -I$(srcdir)/source

libj_jarfile=   gnujaxp.jar

#XMLJ_LFLAGS=    -shared $(SOFLAGS) $(LDFLAGS) $(XML_LDFLAGS) $(XSLT_LDFLAGS) -release $(LIBVERSION) -single_module
#XMLJ_CFLAGS=    $(CFLAGS) $(XML_CFLAGS) $(XSLT_CFLAGS) -I. -pedantic -Wmissing-declarations -Wmissing-prototypes -Wstrict-prototypes -Wall -Wno-long-long -fbounds-check
#LT_COMPILE=     $(LIBTOOL) --mode=compile $(CC) -c $(XMLJ_CFLAGS)
#LT_LINK=        $(LIBTOOL) --mode=link $(CC) $(XMLJ_LFLAGS) $(XSLT_LIBS)
#LT_INSTALL=     $(LIBTOOL) --mode=install $(INSTALL)

package_names = \
	javax.xml \
	javax.xml.datatype \
	javax.xml.namespace \
	javax.xml.parsers \
	javax.xml.transform \
	javax.xml.transform.dom \
	javax.xml.transform.sax \
	javax.xml.transform.stream \
	javax.xml.validation \
	javax.xml.xpath \
	org.w3c.dom \
	org.w3c.dom.bootstrap \
	org.w3c.dom.css \
	org.w3c.dom.events \
	org.w3c.dom.html2 \
	org.w3c.dom.ls \
	org.w3c.dom.ranges \
	org.w3c.dom.stylesheets \
	org.w3c.dom.traversal \
	org.w3c.dom.views \
	org.w3c.dom.xpath \
	org.xml.sax \
	org.xml.sax.ext \
	org.xml.sax.helpers \
	gnu.xml.aelfred2 \
	gnu.xml.dom \
	gnu.xml.dom.ls \
	gnu.xml.libxmlj.dom \
	gnu.xml.libxmlj.sax \
	gnu.xml.libxmlj.transform \
	gnu.xml.pipeline \
	gnu.xml.transform \
	gnu.xml.util \
	gnu.xml.xpath
	
lib_LTLIBRARIES = \
	libxmlj.la \
	lib-javax-xml.la \
	lib-org-w3c-dom.la \
	lib-org-xml-sax.la \
	lib-gnu-xml.la

libxmlj_la_SOURCES = \
	xmlj_dom.c \
	xmlj_dom.h \
	xmlj_error.c \
	xmlj_error.h \
	xmlj_io.c \
	xmlj_io.h \
	xmlj_node.c \
	xmlj_node.h \
	xmlj_sax.c \
	xmlj_sax.h \
	xmlj_transform.c \
	xmlj_util.c \
	xmlj_util.h \
	xmlj_xpath.c

libxmlj_la_LDFLAGS = -release $(LIBVERSION) -export-dynamic -shrext @JMODSUFFIX@
libxmlj_la_CFLAGS = -pedantic -Wall -Wno-long-long -fbounds-check
libxmlj_la_DEPENDENCIES = $(noinst_HEADERS)

lib_javax_xml_la_SOURCES = \
	source/javax/xml/datatype/DatatypeConfigurationException.java \
	source/javax/xml/datatype/DatatypeConstants.java \
	source/javax/xml/datatype/DatatypeFactory.java \
	source/javax/xml/datatype/Duration.java \
	source/javax/xml/datatype/XMLGregorianCalendar.java \
	source/javax/xml/XMLConstants.java \
	source/javax/xml/namespace/NamespaceContext.java \
	source/javax/xml/namespace/QName.java \
	source/javax/xml/parsers/DocumentBuilder.java \
	source/javax/xml/parsers/DocumentBuilderFactory.java \
	source/javax/xml/parsers/FactoryConfigurationError.java \
	source/javax/xml/parsers/ParserConfigurationException.java \
	source/javax/xml/parsers/SAXParser.java \
	source/javax/xml/parsers/SAXParserFactory.java \
	source/javax/xml/transform/dom/DOMLocator.java \
	source/javax/xml/transform/dom/DOMResult.java \
	source/javax/xml/transform/dom/DOMSource.java \
	source/javax/xml/transform/sax/SAXResult.java \
	source/javax/xml/transform/sax/SAXSource.java \
	source/javax/xml/transform/sax/SAXTransformerFactory.java \
	source/javax/xml/transform/sax/TemplatesHandler.java \
	source/javax/xml/transform/sax/TransformerHandler.java \
	source/javax/xml/transform/ErrorListener.java \
	source/javax/xml/transform/OutputKeys.java \
	source/javax/xml/transform/Result.java \
	source/javax/xml/transform/Source.java \
	source/javax/xml/transform/SourceLocator.java \
	source/javax/xml/transform/Templates.java \
	source/javax/xml/transform/Transformer.java \
	source/javax/xml/transform/TransformerConfigurationException.java \
	source/javax/xml/transform/TransformerException.java \
	source/javax/xml/transform/TransformerFactory.java \
	source/javax/xml/transform/TransformerFactoryConfigurationError.java \
	source/javax/xml/transform/URIResolver.java \
	source/javax/xml/transform/stream/StreamResult.java \
	source/javax/xml/transform/stream/StreamSource.java \
	source/javax/xml/validation/Schema.java \
	source/javax/xml/validation/SchemaFactory.java \
	source/javax/xml/validation/TypeInfoProvider.java \
	source/javax/xml/validation/Validator.java \
	source/javax/xml/validation/ValidatorHandler.java \
	source/javax/xml/xpath/XPath.java \
	source/javax/xml/xpath/XPathConstants.java \
	source/javax/xml/xpath/XPathException.java \
	source/javax/xml/xpath/XPathExpression.java \
	source/javax/xml/xpath/XPathExpressionException.java \
	source/javax/xml/xpath/XPathFactory.java \
	source/javax/xml/xpath/XPathFactoryConfigurationException.java \
	source/javax/xml/xpath/XPathFunction.java \
	source/javax/xml/xpath/XPathFunctionException.java \
	source/javax/xml/xpath/XPathFunctionResolver.java \
	source/javax/xml/xpath/XPathVariableResolver.java
lib_javax_xml_la_LDFLAGS = -release $(VERSION)

lib_org_w3c_dom_la_SOURCES = \
	source/org/w3c/dom/bootstrap/DOMImplementationRegistry.java \
	source/org/w3c/dom/Attr.java \
	source/org/w3c/dom/CDATASection.java \
	source/org/w3c/dom/CharacterData.java \
	source/org/w3c/dom/Comment.java \
	source/org/w3c/dom/DOMConfiguration.java \
	source/org/w3c/dom/DOMError.java \
	source/org/w3c/dom/DOMErrorHandler.java \
	source/org/w3c/dom/DOMException.java \
	source/org/w3c/dom/DOMImplementation.java \
	source/org/w3c/dom/DOMImplementationList.java \
	source/org/w3c/dom/DOMImplementationSource.java \
	source/org/w3c/dom/DOMLocator.java \
	source/org/w3c/dom/DOMStringList.java \
	source/org/w3c/dom/Document.java \
	source/org/w3c/dom/DocumentFragment.java \
	source/org/w3c/dom/DocumentType.java \
	source/org/w3c/dom/Element.java \
	source/org/w3c/dom/Entity.java \
	source/org/w3c/dom/EntityReference.java \
	source/org/w3c/dom/NameList.java \
	source/org/w3c/dom/NamedNodeMap.java \
	source/org/w3c/dom/Node.java \
	source/org/w3c/dom/NodeList.java \
	source/org/w3c/dom/Notation.java \
	source/org/w3c/dom/ProcessingInstruction.java \
	source/org/w3c/dom/Text.java \
	source/org/w3c/dom/TypeInfo.java \
	source/org/w3c/dom/UserDataHandler.java \
	source/org/w3c/dom/css/CSS2Properties.java \
	source/org/w3c/dom/css/CSSCharsetRule.java \
	source/org/w3c/dom/css/CSSFontFaceRule.java \
	source/org/w3c/dom/css/CSSImportRule.java \
	source/org/w3c/dom/css/CSSMediaRule.java \
	source/org/w3c/dom/css/CSSPageRule.java \
	source/org/w3c/dom/css/CSSPrimitiveValue.java \
	source/org/w3c/dom/css/CSSRule.java \
	source/org/w3c/dom/css/CSSRuleList.java \
	source/org/w3c/dom/css/CSSStyleDeclaration.java \
	source/org/w3c/dom/css/CSSStyleRule.java \
	source/org/w3c/dom/css/CSSStyleSheet.java \
	source/org/w3c/dom/css/CSSUnknownRule.java \
	source/org/w3c/dom/css/CSSValue.java \
	source/org/w3c/dom/css/CSSValueList.java \
	source/org/w3c/dom/css/Counter.java \
	source/org/w3c/dom/css/DOMImplementationCSS.java \
	source/org/w3c/dom/css/DocumentCSS.java \
	source/org/w3c/dom/css/ElementCSSInlineStyle.java \
	source/org/w3c/dom/css/RGBColor.java \
	source/org/w3c/dom/css/Rect.java \
	source/org/w3c/dom/css/ViewCSS.java \
	source/org/w3c/dom/events/DocumentEvent.java \
	source/org/w3c/dom/events/Event.java \
	source/org/w3c/dom/events/EventException.java \
	source/org/w3c/dom/events/EventListener.java \
	source/org/w3c/dom/events/EventTarget.java \
	source/org/w3c/dom/events/MouseEvent.java \
	source/org/w3c/dom/events/MutationEvent.java \
	source/org/w3c/dom/events/UIEvent.java \
	source/org/w3c/dom/html2/HTMLAnchorElement.java \
	source/org/w3c/dom/html2/HTMLAppletElement.java \
	source/org/w3c/dom/html2/HTMLAreaElement.java \
	source/org/w3c/dom/html2/HTMLBRElement.java \
	source/org/w3c/dom/html2/HTMLBaseElement.java \
	source/org/w3c/dom/html2/HTMLBaseFontElement.java \
	source/org/w3c/dom/html2/HTMLBodyElement.java \
	source/org/w3c/dom/html2/HTMLButtonElement.java \
	source/org/w3c/dom/html2/HTMLCollection.java \
	source/org/w3c/dom/html2/HTMLDListElement.java \
	source/org/w3c/dom/html2/HTMLDirectoryElement.java \
	source/org/w3c/dom/html2/HTMLDivElement.java \
	source/org/w3c/dom/html2/HTMLDocument.java \
	source/org/w3c/dom/html2/HTMLElement.java \
	source/org/w3c/dom/html2/HTMLFieldSetElement.java \
	source/org/w3c/dom/html2/HTMLFontElement.java \
	source/org/w3c/dom/html2/HTMLFormElement.java \
	source/org/w3c/dom/html2/HTMLFrameElement.java \
	source/org/w3c/dom/html2/HTMLFrameSetElement.java \
	source/org/w3c/dom/html2/HTMLHRElement.java \
	source/org/w3c/dom/html2/HTMLHeadElement.java \
	source/org/w3c/dom/html2/HTMLHeadingElement.java \
	source/org/w3c/dom/html2/HTMLHtmlElement.java \
	source/org/w3c/dom/html2/HTMLIFrameElement.java \
	source/org/w3c/dom/html2/HTMLImageElement.java \
	source/org/w3c/dom/html2/HTMLInputElement.java \
	source/org/w3c/dom/html2/HTMLIsIndexElement.java \
	source/org/w3c/dom/html2/HTMLLIElement.java \
	source/org/w3c/dom/html2/HTMLLabelElement.java \
	source/org/w3c/dom/html2/HTMLLegendElement.java \
	source/org/w3c/dom/html2/HTMLLinkElement.java \
	source/org/w3c/dom/html2/HTMLMapElement.java \
	source/org/w3c/dom/html2/HTMLMenuElement.java \
	source/org/w3c/dom/html2/HTMLMetaElement.java \
	source/org/w3c/dom/html2/HTMLModElement.java \
	source/org/w3c/dom/html2/HTMLOListElement.java \
	source/org/w3c/dom/html2/HTMLObjectElement.java \
	source/org/w3c/dom/html2/HTMLOptGroupElement.java \
	source/org/w3c/dom/html2/HTMLOptionElement.java \
	source/org/w3c/dom/html2/HTMLOptionsCollection.java \
	source/org/w3c/dom/html2/HTMLParagraphElement.java \
	source/org/w3c/dom/html2/HTMLParamElement.java \
	source/org/w3c/dom/html2/HTMLPreElement.java \
	source/org/w3c/dom/html2/HTMLQuoteElement.java \
	source/org/w3c/dom/html2/HTMLScriptElement.java \
	source/org/w3c/dom/html2/HTMLSelectElement.java \
	source/org/w3c/dom/html2/HTMLStyleElement.java \
	source/org/w3c/dom/html2/HTMLTableCaptionElement.java \
	source/org/w3c/dom/html2/HTMLTableCellElement.java \
	source/org/w3c/dom/html2/HTMLTableColElement.java \
	source/org/w3c/dom/html2/HTMLTableElement.java \
	source/org/w3c/dom/html2/HTMLTableRowElement.java \
	source/org/w3c/dom/html2/HTMLTableSectionElement.java \
	source/org/w3c/dom/html2/HTMLTextAreaElement.java \
	source/org/w3c/dom/html2/HTMLTitleElement.java \
	source/org/w3c/dom/html2/HTMLUListElement.java \
	source/org/w3c/dom/ls/DOMImplementationLS.java \
	source/org/w3c/dom/ls/LSException.java \
	source/org/w3c/dom/ls/LSInput.java \
	source/org/w3c/dom/ls/LSLoadEvent.java \
	source/org/w3c/dom/ls/LSOutput.java \
	source/org/w3c/dom/ls/LSParser.java \
	source/org/w3c/dom/ls/LSParserFilter.java \
	source/org/w3c/dom/ls/LSProgressEvent.java \
	source/org/w3c/dom/ls/LSResourceResolver.java \
	source/org/w3c/dom/ls/LSSerializer.java \
	source/org/w3c/dom/ls/LSSerializerFilter.java \
	source/org/w3c/dom/ranges/DocumentRange.java \
	source/org/w3c/dom/ranges/Range.java \
	source/org/w3c/dom/ranges/RangeException.java \
	source/org/w3c/dom/stylesheets/DocumentStyle.java \
	source/org/w3c/dom/stylesheets/LinkStyle.java \
	source/org/w3c/dom/stylesheets/MediaList.java \
	source/org/w3c/dom/stylesheets/StyleSheet.java \
	source/org/w3c/dom/stylesheets/StyleSheetList.java \
	source/org/w3c/dom/traversal/DocumentTraversal.java \
	source/org/w3c/dom/traversal/NodeFilter.java \
	source/org/w3c/dom/traversal/NodeIterator.java \
	source/org/w3c/dom/traversal/TreeWalker.java \
	source/org/w3c/dom/views/AbstractView.java \
	source/org/w3c/dom/views/DocumentView.java \
	source/org/w3c/dom/xpath/XPathEvaluator.java \
	source/org/w3c/dom/xpath/XPathException.java \
	source/org/w3c/dom/xpath/XPathExpression.java \
	source/org/w3c/dom/xpath/XPathNSResolver.java \
	source/org/w3c/dom/xpath/XPathNamespace.java \
	source/org/w3c/dom/xpath/XPathResult.java
lib_org_w3c_dom_la_LDFLAGS = -release $(VERSION)

lib_org_xml_sax_la_SOURCES = \
	source/org/xml/sax/ext/Attributes2.java \
	source/org/xml/sax/ext/Attributes2Impl.java \
	source/org/xml/sax/ext/DeclHandler.java \
	source/org/xml/sax/ext/DefaultHandler2.java \
	source/org/xml/sax/ext/EntityResolver2.java \
	source/org/xml/sax/ext/LexicalHandler.java \
	source/org/xml/sax/ext/Locator2.java \
	source/org/xml/sax/ext/Locator2Impl.java \
	source/org/xml/sax/AttributeList.java \
	source/org/xml/sax/Attributes.java \
	source/org/xml/sax/ContentHandler.java \
	source/org/xml/sax/DTDHandler.java \
	source/org/xml/sax/DocumentHandler.java \
	source/org/xml/sax/EntityResolver.java \
	source/org/xml/sax/ErrorHandler.java \
	source/org/xml/sax/HandlerBase.java \
	source/org/xml/sax/InputSource.java \
	source/org/xml/sax/Locator.java \
	source/org/xml/sax/Parser.java \
	source/org/xml/sax/SAXException.java \
	source/org/xml/sax/SAXNotRecognizedException.java \
	source/org/xml/sax/SAXNotSupportedException.java \
	source/org/xml/sax/SAXParseException.java \
	source/org/xml/sax/XMLFilter.java \
	source/org/xml/sax/XMLReader.java \
	source/org/xml/sax/helpers/AttributeListImpl.java \
	source/org/xml/sax/helpers/AttributesImpl.java \
	source/org/xml/sax/helpers/DefaultHandler.java \
	source/org/xml/sax/helpers/LocatorImpl.java \
	source/org/xml/sax/helpers/NamespaceSupport.java \
	source/org/xml/sax/helpers/NewInstance.java \
	source/org/xml/sax/helpers/ParserAdapter.java \
	source/org/xml/sax/helpers/ParserFactory.java \
	source/org/xml/sax/helpers/XMLFilterImpl.java \
	source/org/xml/sax/helpers/XMLReaderAdapter.java \
	source/org/xml/sax/helpers/XMLReaderFactory.java
lib_org_xml_sax_la_LDFLAGS = -release $(VERSION)

lib_gnu_xml_la_SOURCES = \
	source/gnu/xml/aelfred2/JAXPFactory.java \
	source/gnu/xml/aelfred2/SAXDriver.java \
	source/gnu/xml/aelfred2/XmlParser.java \
	source/gnu/xml/aelfred2/XmlReader.java \
	source/gnu/xml/aelfred2/ContentHandler2.java \
	source/gnu/xml/dom/ls/DomLSEx.java \
	source/gnu/xml/dom/ls/DomLSInput.java \
	source/gnu/xml/dom/ls/DomLSOutput.java \
	source/gnu/xml/dom/ls/DomLSParser.java \
	source/gnu/xml/dom/ls/DomLSSerializer.java \
	source/gnu/xml/dom/ls/FilteredSAXEventSink.java \
	source/gnu/xml/dom/ls/ReaderInputStream.java \
	source/gnu/xml/dom/ls/SAXEventSink.java \
	source/gnu/xml/dom/ls/WriterOutputStream.java \
	source/gnu/xml/dom/Consumer.java \
	source/gnu/xml/dom/DomAttr.java \
	source/gnu/xml/dom/DomCDATA.java \
	source/gnu/xml/dom/DomCharacterData.java \
	source/gnu/xml/dom/DomComment.java \
	source/gnu/xml/dom/DomDoctype.java \
	source/gnu/xml/dom/DomDocument.java \
	source/gnu/xml/dom/DomDocumentBuilder.java \
	source/gnu/xml/dom/DomDocumentBuilderFactory.java \
	source/gnu/xml/dom/DomDocumentConfiguration.java \
	source/gnu/xml/dom/DomElement.java \
	source/gnu/xml/dom/DomEntity.java \
	source/gnu/xml/dom/DomEntityReference.java \
	source/gnu/xml/dom/DomEvent.java \
	source/gnu/xml/dom/DomEx.java \
	source/gnu/xml/dom/DomExtern.java \
	source/gnu/xml/dom/DomFragment.java \
	source/gnu/xml/dom/DomImpl.java \
	source/gnu/xml/dom/DomIterator.java \
	source/gnu/xml/dom/DomNamedNodeMap.java \
	source/gnu/xml/dom/DomNode.java \
	source/gnu/xml/dom/DomNodeIterator.java \
	source/gnu/xml/dom/DomNotation.java \
	source/gnu/xml/dom/DomNsNode.java \
	source/gnu/xml/dom/DomNSResolverContext.java \
	source/gnu/xml/dom/DomPI.java \
	source/gnu/xml/dom/DomText.java \
	source/gnu/xml/dom/DomXPathExpression.java \
	source/gnu/xml/dom/DomXPathNSResolver.java \
	source/gnu/xml/dom/DomXPathResult.java \
	source/gnu/xml/dom/DTDAttributeTypeInfo.java \
	source/gnu/xml/dom/DTDElementTypeInfo.java \
	source/gnu/xml/dom/ImplementationList.java \
	source/gnu/xml/dom/ImplementationSource.java \
	source/gnu/xml/dom/JAXPFactory.java \
	source/gnu/xml/libxmlj/dom/GnomeAttr.java \
	source/gnu/xml/libxmlj/dom/GnomeCDATASection.java \
	source/gnu/xml/libxmlj/dom/GnomeCharacterData.java \
	source/gnu/xml/libxmlj/dom/GnomeComment.java \
	source/gnu/xml/libxmlj/dom/GnomeDOMException.java \
	source/gnu/xml/libxmlj/dom/GnomeDOMStringList.java \
	source/gnu/xml/libxmlj/dom/GnomeDocument.java \
	source/gnu/xml/libxmlj/dom/GnomeDocumentBuilder.java \
	source/gnu/xml/libxmlj/dom/GnomeDocumentBuilderFactory.java \
	source/gnu/xml/libxmlj/dom/GnomeDocumentFragment.java \
	source/gnu/xml/libxmlj/dom/GnomeDocumentType.java \
	source/gnu/xml/libxmlj/dom/GnomeElement.java \
	source/gnu/xml/libxmlj/dom/GnomeEntity.java \
	source/gnu/xml/libxmlj/dom/GnomeEntityReference.java \
	source/gnu/xml/libxmlj/dom/GnomeNamedNodeMap.java \
	source/gnu/xml/libxmlj/dom/GnomeNode.java \
	source/gnu/xml/libxmlj/dom/GnomeNodeList.java \
	source/gnu/xml/libxmlj/dom/GnomeNotation.java \
	source/gnu/xml/libxmlj/dom/GnomeProcessingInstruction.java \
	source/gnu/xml/libxmlj/dom/GnomeText.java \
	source/gnu/xml/libxmlj/dom/GnomeTypeInfo.java \
	source/gnu/xml/libxmlj/dom/GnomeXPathExpression.java \
	source/gnu/xml/libxmlj/dom/GnomeXPathNSResolver.java \
	source/gnu/xml/libxmlj/dom/GnomeXPathNodeList.java \
	source/gnu/xml/libxmlj/dom/GnomeXPathResult.java \
	source/gnu/xml/libxmlj/sax/GnomeLocator.java \
	source/gnu/xml/libxmlj/sax/GnomeSAXParser.java \
	source/gnu/xml/libxmlj/sax/GnomeSAXParserFactory.java \
	source/gnu/xml/libxmlj/sax/GnomeXMLReader.java \
	source/gnu/xml/libxmlj/sax/Namespaces.java \
	source/gnu/xml/libxmlj/sax/StringArrayAttributes.java \
	source/gnu/xml/libxmlj/sax/XMLName.java \
	source/gnu/xml/libxmlj/RawData.java \
	source/gnu/xml/libxmlj/RawData32.java \
	source/gnu/xml/libxmlj/RawData64.java \
	source/gnu/xml/libxmlj/transform/ErrorListenerErrorHandler.java \
	source/gnu/xml/libxmlj/transform/GnomeTransformer.java \
	source/gnu/xml/libxmlj/transform/GnomeTransformerFactory.java \
	source/gnu/xml/libxmlj/transform/TransformTest.java \
	source/gnu/xml/libxmlj/transform/URIResolverEntityResolver.java \
	source/gnu/xml/libxmlj/util/EmptyNodeList.java \
	source/gnu/xml/libxmlj/util/NamedInputStream.java \
	source/gnu/xml/libxmlj/util/StandaloneDocumentType.java \
	source/gnu/xml/libxmlj/util/StandaloneLocator.java \
	source/gnu/xml/libxmlj/util/XMLJ.java \
	source/gnu/xml/pipeline/CallFilter.java \
	source/gnu/xml/pipeline/DomConsumer.java \
	source/gnu/xml/pipeline/EventConsumer.java \
	source/gnu/xml/pipeline/EventFilter.java \
	source/gnu/xml/pipeline/LinkFilter.java \
	source/gnu/xml/pipeline/NSFilter.java \
	source/gnu/xml/pipeline/PipelineFactory.java \
	source/gnu/xml/pipeline/TeeConsumer.java \
	source/gnu/xml/pipeline/TextConsumer.java \
	source/gnu/xml/pipeline/ValidationConsumer.java \
	source/gnu/xml/pipeline/WellFormednessFilter.java \
	source/gnu/xml/pipeline/XIncludeFilter.java \
	source/gnu/xml/pipeline/XsltFilter.java \
	source/gnu/xml/util/DoParse.java \
	source/gnu/xml/util/DomParser.java \
	source/gnu/xml/util/Resolver.java \
	source/gnu/xml/util/SAXNullTransformerFactory.java \
	source/gnu/xml/util/XCat.java \
	source/gnu/xml/util/XHTMLWriter.java \
	source/gnu/xml/util/XMLWriter.java \
	source/gnu/xml/xpath/AndExpr.java \
	source/gnu/xml/xpath/ArithmeticExpr.java \
	source/gnu/xml/xpath/Constant.java \
	source/gnu/xml/xpath/EqualityExpr.java \
	source/gnu/xml/xpath/Expr.java \
	source/gnu/xml/xpath/Function.java \
	source/gnu/xml/xpath/FunctionCall.java \
	source/gnu/xml/xpath/NameTest.java \
	source/gnu/xml/xpath/NegativeExpr.java \
	source/gnu/xml/xpath/NodeTypeTest.java \
	source/gnu/xml/xpath/OrExpr.java \
	source/gnu/xml/xpath/ParenthesizedExpr.java \
	source/gnu/xml/xpath/Path.java \
	source/gnu/xml/xpath/RelationalExpr.java \
	source/gnu/xml/xpath/Root.java \
	source/gnu/xml/xpath/Selector.java \
	source/gnu/xml/xpath/Steps.java \
	source/gnu/xml/xpath/Test.java \
	source/gnu/xml/xpath/UnionExpr.java \
	source/gnu/xml/xpath/XPathParser.java \
	source/gnu/xml/xpath/XPathTokenizer.java \
	source/gnu/xml/xpath/BooleanFunction.java \
	source/gnu/xml/xpath/CeilingFunction.java \
	source/gnu/xml/xpath/ConcatFunction.java \
	source/gnu/xml/xpath/ContainsFunction.java \
	source/gnu/xml/xpath/CountFunction.java \
	source/gnu/xml/xpath/DocumentOrderComparator.java \
	source/gnu/xml/xpath/FalseFunction.java \
	source/gnu/xml/xpath/FloorFunction.java \
	source/gnu/xml/xpath/IdFunction.java \
	source/gnu/xml/xpath/LangFunction.java \
	source/gnu/xml/xpath/LastFunction.java \
	source/gnu/xml/xpath/LocalNameFunction.java \
	source/gnu/xml/xpath/NameFunction.java \
	source/gnu/xml/xpath/NamespaceUriFunction.java \
	source/gnu/xml/xpath/NormalizeSpaceFunction.java \
	source/gnu/xml/xpath/NotFunction.java \
	source/gnu/xml/xpath/NumberFunction.java \
	source/gnu/xml/xpath/PositionFunction.java \
	source/gnu/xml/xpath/RoundFunction.java \
	source/gnu/xml/xpath/StartsWithFunction.java \
	source/gnu/xml/xpath/StringFunction.java \
	source/gnu/xml/xpath/StringLengthFunction.java \
	source/gnu/xml/xpath/SubstringAfterFunction.java \
	source/gnu/xml/xpath/SubstringBeforeFunction.java \
	source/gnu/xml/xpath/SubstringFunction.java \
	source/gnu/xml/xpath/SumFunction.java \
	source/gnu/xml/xpath/TranslateFunction.java \
	source/gnu/xml/xpath/TrueFunction.java \
	source/gnu/xml/xpath/VariableReference.java \
	source/gnu/xml/xpath/XPathFactoryImpl.java \
	source/gnu/xml/xpath/XPathImpl.java \
	source/gnu/xml/xpath/XPathTest.java \
	source/gnu/xml/xpath/Pattern.java \
	source/gnu/xml/xpath/Predicate.java \
	source/gnu/xml/xpath/NamespaceTest.java \
	source/gnu/xml/transform/AbstractNumberNode.java \
	source/gnu/xml/transform/ApplyImportsNode.java \
	source/gnu/xml/transform/ApplyTemplatesNode.java \
	source/gnu/xml/transform/AttributeNode.java \
	source/gnu/xml/transform/Bindings.java \
	source/gnu/xml/transform/CallTemplateNode.java \
	source/gnu/xml/transform/ChooseNode.java \
	source/gnu/xml/transform/CommentNode.java \
	source/gnu/xml/transform/CopyNode.java \
	source/gnu/xml/transform/CopyOfNode.java \
	source/gnu/xml/transform/DOMSourceLocator.java \
	source/gnu/xml/transform/DocumentFunction.java \
	source/gnu/xml/transform/ElementAvailableFunction.java \
	source/gnu/xml/transform/ElementNode.java \
	source/gnu/xml/transform/ErrorListenerErrorHandler.java \
	source/gnu/xml/transform/ForEachNode.java \
	source/gnu/xml/transform/FunctionAvailableFunction.java \
	source/gnu/xml/transform/IfNode.java \
	source/gnu/xml/transform/KeyFunction.java \
	source/gnu/xml/transform/LiteralNode.java \
	source/gnu/xml/transform/NodeNumberNode.java \
	source/gnu/xml/transform/NumberNode.java \
	source/gnu/xml/transform/OtherwiseNode.java \
	source/gnu/xml/transform/ParameterNode.java \
	source/gnu/xml/transform/ProcessingInstructionNode.java \
	source/gnu/xml/transform/SAXSerializer.java \
	source/gnu/xml/transform/SortKey.java \
	source/gnu/xml/transform/StreamSerializer.java \
	source/gnu/xml/transform/Stylesheet.java \
	source/gnu/xml/transform/Template.java \
	source/gnu/xml/transform/TemplateNode.java \
	source/gnu/xml/transform/TemplatesImpl.java \
	source/gnu/xml/transform/TextNode.java \
	source/gnu/xml/transform/TransformerFactoryImpl.java \
	source/gnu/xml/transform/TransformerImpl.java \
	source/gnu/xml/transform/TransformerOutputProperties.java \
	source/gnu/xml/transform/URIResolverEntityResolver.java \
	source/gnu/xml/transform/ValueOfNode.java \
	source/gnu/xml/transform/WhenNode.java \
	source/gnu/xml/transform/WithParam.java \
	source/gnu/xml/transform/XSLComparator.java \
	source/gnu/xml/transform/XSLURIResolver.java \
	source/gnu/xml/transform/CurrentFunction.java \
	source/gnu/xml/transform/FormatNumberFunction.java \
	source/gnu/xml/transform/GenerateIdFunction.java \
	source/gnu/xml/transform/SystemPropertyFunction.java \
	source/gnu/xml/transform/UnparsedEntityUriFunction.java \
	source/gnu/xml/transform/MessageNode.java \
	source/gnu/xml/transform/AttributeSet.java \
	source/gnu/xml/transform/Key.java \
	source/gnu/xml/validation/datatype/Annotation.java \
	source/gnu/xml/validation/datatype/AnySimpleType.java \
	source/gnu/xml/validation/datatype/AnyType.java \
	source/gnu/xml/validation/datatype/AnyURIType.java \
	source/gnu/xml/validation/datatype/AtomicSimpleType.java \
	source/gnu/xml/validation/datatype/Base64BinaryType.java \
	source/gnu/xml/validation/datatype/BooleanType.java \
	source/gnu/xml/validation/datatype/ByteType.java \
	source/gnu/xml/validation/datatype/DateTimeType.java \
	source/gnu/xml/validation/datatype/DateType.java \
	source/gnu/xml/validation/datatype/DecimalType.java \
	source/gnu/xml/validation/datatype/DoubleType.java \
	source/gnu/xml/validation/datatype/DurationType.java \
	source/gnu/xml/validation/datatype/EntitiesType.java \
	source/gnu/xml/validation/datatype/EntityType.java \
	source/gnu/xml/validation/datatype/EnumerationFacet.java \
	source/gnu/xml/validation/datatype/Facet.java \
	source/gnu/xml/validation/datatype/FloatType.java \
	source/gnu/xml/validation/datatype/FractionDigitsFacet.java \
	source/gnu/xml/validation/datatype/GDayType.java \
	source/gnu/xml/validation/datatype/GMonthDayType.java \
	source/gnu/xml/validation/datatype/GMonthType.java \
	source/gnu/xml/validation/datatype/GYearMonthType.java \
	source/gnu/xml/validation/datatype/GYearType.java \
	source/gnu/xml/validation/datatype/HexBinaryType.java \
	source/gnu/xml/validation/datatype/IDRefType.java \
	source/gnu/xml/validation/datatype/IDRefsType.java \
	source/gnu/xml/validation/datatype/IDType.java \
	source/gnu/xml/validation/datatype/IntType.java \
	source/gnu/xml/validation/datatype/IntegerType.java \
	source/gnu/xml/validation/datatype/LanguageType.java \
	source/gnu/xml/validation/datatype/LengthFacet.java \
	source/gnu/xml/validation/datatype/ListSimpleType.java \
	source/gnu/xml/validation/datatype/LongType.java \
	source/gnu/xml/validation/datatype/MaxExclusiveFacet.java \
	source/gnu/xml/validation/datatype/MaxInclusiveFacet.java \
	source/gnu/xml/validation/datatype/MaxLengthFacet.java \
	source/gnu/xml/validation/datatype/MinExclusiveFacet.java \
	source/gnu/xml/validation/datatype/MinInclusiveFacet.java \
	source/gnu/xml/validation/datatype/MinLengthFacet.java \
	source/gnu/xml/validation/datatype/NCNameType.java \
	source/gnu/xml/validation/datatype/NMTokenType.java \
	source/gnu/xml/validation/datatype/NMTokensType.java \
	source/gnu/xml/validation/datatype/NameType.java \
	source/gnu/xml/validation/datatype/NegativeIntegerType.java \
	source/gnu/xml/validation/datatype/NonNegativeIntegerType.java \
	source/gnu/xml/validation/datatype/NonPositiveIntegerType.java \
	source/gnu/xml/validation/datatype/NormalizedStringType.java \
	source/gnu/xml/validation/datatype/NotationType.java \
	source/gnu/xml/validation/datatype/PatternFacet.java \
	source/gnu/xml/validation/datatype/PositiveIntegerType.java \
	source/gnu/xml/validation/datatype/QNameType.java \
	source/gnu/xml/validation/datatype/ShortType.java \
	source/gnu/xml/validation/datatype/SimpleType.java \
	source/gnu/xml/validation/datatype/StringType.java \
	source/gnu/xml/validation/datatype/TimeType.java \
	source/gnu/xml/validation/datatype/TokenType.java \
	source/gnu/xml/validation/datatype/TotalDigitsFacet.java \
	source/gnu/xml/validation/datatype/Type.java \
	source/gnu/xml/validation/datatype/UnionSimpleType.java \
	source/gnu/xml/validation/datatype/UnsignedByteType.java \
	source/gnu/xml/validation/datatype/UnsignedIntType.java \
	source/gnu/xml/validation/datatype/UnsignedLongType.java \
	source/gnu/xml/validation/datatype/UnsignedShortType.java \
	source/gnu/xml/validation/datatype/WhiteSpaceFacet.java \
	source/gnu/xml/validation/xmlschema/AnyAttribute.java \
	source/gnu/xml/validation/xmlschema/AttributeDeclaration.java \
	source/gnu/xml/validation/xmlschema/AttributeUse.java \
	source/gnu/xml/validation/xmlschema/ElementDeclaration.java \
	source/gnu/xml/validation/xmlschema/Particle.java \
	source/gnu/xml/validation/xmlschema/ValidationException.java \
	source/gnu/xml/validation/xmlschema/XMLSchema.java \
	source/gnu/xml/validation/xmlschema/XMLSchemaAttributeTypeInfo.java \
	source/gnu/xml/validation/xmlschema/XMLSchemaBuilder.java \
	source/gnu/xml/validation/xmlschema/XMLSchemaElementTypeInfo.java \
	source/gnu/xml/validation/xmlschema/XMLSchemaTypeInfo.java \
	source/gnu/xml/validation/xmlschema/XMLSchemaTypeInfoProvider.java \
	source/gnu/xml/validation/xmlschema/XMLSchemaValidator.java \
	source/gnu/xml/validation/xmlschema/XMLSchemaValidatorHandler.java

lib_gnu_xml_la_LDFLAGS = -release-info $(VERSION)
lib_gnu_xml_la_LIBADD = -l-javax-xml -l-org-w3c-dom -l-org-xml-sax

data_JARS = javax-xml-$(VERSION).jar gnu-xml-$(VERSION).jar \
	org-w3c-dom-$(VERSION).jar org-xml-sax-$(VERSION).jar \
	$(libj_jarfile)

BUILT_SOURCES = $(data_JARS) $(noinst_HEADERS)

noinst_HEADERS = \
	gnu_xml_libxmlj_dom_GnomeDocument.h \
	gnu_xml_libxmlj_dom_GnomeXPathNodeList.h \
	gnu_xml_libxmlj_dom_GnomeDocumentType.h \
	gnu_xml_libxmlj_dom_GnomeProcessingInstruction.h \
	gnu_xml_libxmlj_dom_GnomeXPathNSResolver.h \
	gnu_xml_libxmlj_dom_GnomeTypeInfo.h \
	gnu_xml_libxmlj_dom_GnomeNodeList.h \
	gnu_xml_libxmlj_dom_GnomeNotation.h \
	gnu_xml_libxmlj_dom_GnomeXPathResult.h \
	gnu_xml_libxmlj_dom_GnomeElement.h \
	gnu_xml_libxmlj_dom_GnomeEntity.h \
	gnu_xml_libxmlj_dom_GnomeNode.h \
	gnu_xml_libxmlj_dom_GnomeXPathExpression.h \
	gnu_xml_libxmlj_dom_GnomeNamedNodeMap.h \
	gnu_xml_libxmlj_dom_GnomeDocumentBuilder.h \
	gnu_xml_libxmlj_dom_GnomeAttr.h \
	gnu_xml_libxmlj_sax_GnomeLocator.h \
	gnu_xml_libxmlj_sax_GnomeXMLReader.h \
	gnu_xml_libxmlj_transform_GnomeTransformer.h \
	gnu_xml_libxmlj_transform_GnomeTransformerFactory.h

# Generate header files for classes containing native code.
# I did remove the -force argument (not available in gcjh nor in kaffe)
$(noinst_HEADERS): $(gnu_xml_jar_CLASSES)

gnu_xml_libxmlj_dom_%.h: classes/gnu/xml/libxmlj/dom/%.class
	$(JAVAH) -jni -classpath classes gnu.xml.libxmlj.dom.$*

gnu_xml_libxmlj_sax_%.h: classes/gnu/xml/libxmlj/sax/%.class
	$(JAVAH) -jni -classpath classes gnu.xml.libxmlj.sax.$*

gnu_xml_libxmlj_transform_%.h: classes/gnu/xml/libxmlj/transform/%.class
	$(JAVAH) -jni -classpath classes gnu.xml.libxmlj.transform.$*

gnu_xml_jar_CLASSES     = $(patsubst source/%.java,classes/%.class,$(lib_gnu_xml_la_SOURCES))
javax_xml_jar_CLASSES   = $(patsubst source/%.java,classes/%.class,$(lib_javax_xml_la_SOURCES))
org_w3c_dom_jar_CLASSES = $(patsubst source/%.java,classes/%.class,$(lib_org_w3c_dom_la_SOURCES))
org_xml_sax_jar_CLASSES = $(patsubst source/%.java,classes/%.class,$(lib_org_xml_sax_la_SOURCES))

gnu-xml-$(VERSION).jar: classes $(gnu_xml_jar_CLASSES)
	$(JAR) cf $@ -C classes gnu

javax-xml-$(VERSION).jar: classes $(javax_xml_jar_CLASSES)
	$(JAR) cf $@ -C classes javax

org-w3c-dom-$(VERSION).jar: classes $(org_w3c_dom_jar_CLASSES)
	$(JAR) cf $@ -C classes org/w3c

org-xml-sax-$(VERSION).jar: classes $(org_xml_sax_jar_CLASSES)
	$(JAR) cf $@ -C classes org/xml

$(libj_jarfile): $(gnu_xml_jar_CLASSES) $(javax_xml_jar_CLASSES) $(org_w3c_dom_jar_CLASSES) $(org_xml_sax_jar_CLASSES) classes/META-INF/services/javax.xml.parsers.SAXParserFactory classes/META-INF/services/javax.xml.parsers.DocumentBuilderFactory classes/META-INF/services/javax.xml.parsers.TransformerFactory
	$(JAR) cfm $@ @srcdir@/manifest.mf -C classes META-INF -C classes gnu -C classes javax -C classes org

$(javax_xml_jar_CLASSES) $(org_w3c_dom_jar_CLASSES) $(org_xml_sax_jar_CLASSES) $(gnu_xml_jar_CLASSES): classes

$(javax_xml_jar_CLASSES) $(org_w3c_dom_jar_CLASSES) $(org_xml_sax_jar_CLASSES) $(gnu_xml_jar_CLASSES): classes/%.class: $(srcdir)/source/%.java
	$(JAVAC) -g -d classes -classpath @srcdir@/source $<
#	$(GCJ) $(GCJ_CLASS_FLAGS) -g -C -d classes $<

classes:
	mkdir -p classes

classes/META-INF: classes
	mkdir -p classes/META-INF

classes/META-INF/services: classes/META-INF
	mkdir -p classes/META-INF/services

classes/META-INF/services/javax.xml.parsers.SAXParserFactory: classes/META-INF/services
	echo "gnu.xml.aelfred2.JAXPFactory" > $@

classes/META-INF/services/javax.xml.parsers.DocumentBuilderFactory: classes/META-INF/services
	echo "gnu.xml.dom.DomDocumentBuilderFactory" > $@

classes/META-INF/services/javax.xml.parsers.TransformerFactory: classes/META-INF/services
	echo "gnu.xml.transform.TransformerFactoryImpl" > $@

# API documentation
javadoc:
	mkdir -p @srcdir@/apidoc
	$(JAVADOC) -d @srcdir@/apidoc -sourcepath @srcdir@/source \
		$(package_names)
#		-windowtitle "GNU JAXP Library" $(package_names)

# Remove classes and jar file

clean-local:
	rm -f $(data_JARS)
	rm -rf classes
	rm -f $(noinst_HEADERS)

# Run a simple transformation to check that the package is working
check-local: gnu/xml/libxmlj/transform/TransformTest.class
	@JAVA@  -classpath "${CLASSPATH}:${libj_jarfile}:." -Djava.library.path=.libs gnu.xml.libxmlj.transform.TransformTest @srcdir@/testdocs/test.xml @srcdir@/testdocs/testsheet.xsl transformed.xml
	diff @srcdir@/testdocs/transformed.xml transformed.xml
#	./transform-test $(srcdir)/testdocs/test.xml $(srcdir)/testdocs/testsheet.xsl transformed.xml
#	diff $(srcdir)/testdocs/transformed.xml transformed.xml


dist-hook:
	mkdir $(distdir)/source/gnu
	cp -r $(srcdir)/source/gnu/* $(distdir)/source/gnu
	mkdir $(distdir)/testdocs
	cp $(srcdir)/testdocs/* $(distdir)/testdocs

install-data-hook:
	$(INSTALL) -d $(datadir)/java
	$(INSTALL) -d $(datadir)/java/ext
	$(INSTALL) --mode 644 $(data_JARS) $(datadir)/java
	$(LN_S) -f $(datadir)/java/javax-xml-$(VERSION).jar $(datadir)/java/ext/javax-xml.jar
	$(LN_S) -f $(datadir)/java/gnu-xml-$(VERSION).jar $(datadir)/java/ext/gnu-xml.jar
	$(LN_S) -f $(datadir)/java/org-w3c-dom-$(VERSION).jar $(datadir)/java/ext/org-w3c-dom.jar
	$(LN_S) -f $(datadir)/java/org-xml-sax-$(VERSION).jar $(datadir)/java/ext/org-xml-sax.jar
