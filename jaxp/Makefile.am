# 
# Copyright (C) 2003 Julian Scheid
# 
# This file is part of GNU LibxmlJ, a JAXP-compliant Java wrapper for
# the XML and XSLT C libraries for Gnome (libxml2/libxslt).
# 
# GNU LibxmlJ is free software; you can redistribute it and/or modify it
# under the terms of the GNU General Public License as published by the
# Free Software Foundation; either version 2, or (at your option) any
# later version.
#  
# GNU LibxmlJ is distributed in the hope that it will be useful, but
# WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
# General Public License for more details.
# 
# You should have received a copy of the GNU General Public License
# along with GNU LibxmlJ; see the file COPYING.  If not, write to the
# Free Software Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA
# 02111-1307 USA. 
#

JAVADOC = javadoc

src = @srcdir@/source
dst = @srcdir@/classes
doc = @srcdir@/apidoc

JARFILE = gnujaxp.jar
#data_DATA = ${JARFILE} - needs $(prefix)/share/java

lib_LTLIBRARIES = libxmlj.la

sources = \
$(src)/org/w3c/dom/Attr.java \
$(src)/org/w3c/dom/CDATASection.java \
$(src)/org/w3c/dom/CharacterData.java \
$(src)/org/w3c/dom/Comment.java \
$(src)/org/w3c/dom/DOMConfiguration.java \
$(src)/org/w3c/dom/DOMError.java \
$(src)/org/w3c/dom/DOMErrorHandler.java \
$(src)/org/w3c/dom/DOMException.java \
$(src)/org/w3c/dom/DOMImplementation.java \
$(src)/org/w3c/dom/DOMImplementationList.java \
$(src)/org/w3c/dom/DOMImplementationSource.java \
$(src)/org/w3c/dom/DOMLocator.java \
$(src)/org/w3c/dom/DOMStringList.java \
$(src)/org/w3c/dom/Document.java \
$(src)/org/w3c/dom/DocumentFragment.java \
$(src)/org/w3c/dom/DocumentType.java \
$(src)/org/w3c/dom/Element.java \
$(src)/org/w3c/dom/Entity.java \
$(src)/org/w3c/dom/EntityReference.java \
$(src)/org/w3c/dom/NameList.java \
$(src)/org/w3c/dom/NamedNodeMap.java \
$(src)/org/w3c/dom/Node.java \
$(src)/org/w3c/dom/NodeList.java \
$(src)/org/w3c/dom/Notation.java \
$(src)/org/w3c/dom/ProcessingInstruction.java \
$(src)/org/w3c/dom/Text.java \
$(src)/org/w3c/dom/TypeInfo.java \
$(src)/org/w3c/dom/UserDataHandler.java \
$(src)/org/w3c/dom/bootstrap/DOMImplementationRegistry.java \
$(src)/org/w3c/dom/css/CSS2Azimuth.java \
$(src)/org/w3c/dom/css/CSS2BackgroundPosition.java \
$(src)/org/w3c/dom/css/CSS2BorderSpacing.java \
$(src)/org/w3c/dom/css/CSS2CounterIncrement.java \
$(src)/org/w3c/dom/css/CSS2CounterReset.java \
$(src)/org/w3c/dom/css/CSS2Cursor.java \
$(src)/org/w3c/dom/css/CSS2FontFaceSrc.java \
$(src)/org/w3c/dom/css/CSS2FontFaceWidths.java \
$(src)/org/w3c/dom/css/CSS2PageSize.java \
$(src)/org/w3c/dom/css/CSS2PlayDuring.java \
$(src)/org/w3c/dom/css/CSS2Properties.java \
$(src)/org/w3c/dom/css/CSS2TextShadow.java \
$(src)/org/w3c/dom/css/CSSCharsetRule.java \
$(src)/org/w3c/dom/css/CSSFontFaceRule.java \
$(src)/org/w3c/dom/css/CSSImportRule.java \
$(src)/org/w3c/dom/css/CSSMediaRule.java \
$(src)/org/w3c/dom/css/CSSPageRule.java \
$(src)/org/w3c/dom/css/CSSPrimitiveValue.java \
$(src)/org/w3c/dom/css/CSSRule.java \
$(src)/org/w3c/dom/css/CSSRuleList.java \
$(src)/org/w3c/dom/css/CSSStyleDeclaration.java \
$(src)/org/w3c/dom/css/CSSStyleRule.java \
$(src)/org/w3c/dom/css/CSSStyleSheet.java \
$(src)/org/w3c/dom/css/CSSUnknownRule.java \
$(src)/org/w3c/dom/css/CSSValue.java \
$(src)/org/w3c/dom/css/CSSValueList.java \
$(src)/org/w3c/dom/css/Counter.java \
$(src)/org/w3c/dom/css/DOMImplementationCSS.java \
$(src)/org/w3c/dom/css/DocumentCSS.java \
$(src)/org/w3c/dom/css/ElementCSSInlineStyle.java \
$(src)/org/w3c/dom/css/RGBColor.java \
$(src)/org/w3c/dom/css/Rect.java \
$(src)/org/w3c/dom/css/ViewCSS.java \
$(src)/org/w3c/dom/events/DocumentEvent.java \
$(src)/org/w3c/dom/events/Event.java \
$(src)/org/w3c/dom/events/EventException.java \
$(src)/org/w3c/dom/events/EventListener.java \
$(src)/org/w3c/dom/events/EventTarget.java \
$(src)/org/w3c/dom/events/MouseEvent.java \
$(src)/org/w3c/dom/events/MutationEvent.java \
$(src)/org/w3c/dom/events/UIEvent.java \
$(src)/org/w3c/dom/html/HTMLAnchorElement.java \
$(src)/org/w3c/dom/html/HTMLAppletElement.java \
$(src)/org/w3c/dom/html/HTMLAreaElement.java \
$(src)/org/w3c/dom/html/HTMLBRElement.java \
$(src)/org/w3c/dom/html/HTMLBaseElement.java \
$(src)/org/w3c/dom/html/HTMLBaseFontElement.java \
$(src)/org/w3c/dom/html/HTMLBodyElement.java \
$(src)/org/w3c/dom/html/HTMLButtonElement.java \
$(src)/org/w3c/dom/html/HTMLCollection.java \
$(src)/org/w3c/dom/html/HTMLDListElement.java \
$(src)/org/w3c/dom/html/HTMLDOMImplementation.java \
$(src)/org/w3c/dom/html/HTMLDirectoryElement.java \
$(src)/org/w3c/dom/html/HTMLDivElement.java \
$(src)/org/w3c/dom/html/HTMLDocument.java \
$(src)/org/w3c/dom/html/HTMLElement.java \
$(src)/org/w3c/dom/html/HTMLFieldSetElement.java \
$(src)/org/w3c/dom/html/HTMLFontElement.java \
$(src)/org/w3c/dom/html/HTMLFormElement.java \
$(src)/org/w3c/dom/html/HTMLFrameElement.java \
$(src)/org/w3c/dom/html/HTMLFrameSetElement.java \
$(src)/org/w3c/dom/html/HTMLHRElement.java \
$(src)/org/w3c/dom/html/HTMLHeadElement.java \
$(src)/org/w3c/dom/html/HTMLHeadingElement.java \
$(src)/org/w3c/dom/html/HTMLHtmlElement.java \
$(src)/org/w3c/dom/html/HTMLIFrameElement.java \
$(src)/org/w3c/dom/html/HTMLImageElement.java \
$(src)/org/w3c/dom/html/HTMLInputElement.java \
$(src)/org/w3c/dom/html/HTMLIsIndexElement.java \
$(src)/org/w3c/dom/html/HTMLLIElement.java \
$(src)/org/w3c/dom/html/HTMLLabelElement.java \
$(src)/org/w3c/dom/html/HTMLLegendElement.java \
$(src)/org/w3c/dom/html/HTMLLinkElement.java \
$(src)/org/w3c/dom/html/HTMLMapElement.java \
$(src)/org/w3c/dom/html/HTMLMenuElement.java \
$(src)/org/w3c/dom/html/HTMLMetaElement.java \
$(src)/org/w3c/dom/html/HTMLModElement.java \
$(src)/org/w3c/dom/html/HTMLOListElement.java \
$(src)/org/w3c/dom/html/HTMLObjectElement.java \
$(src)/org/w3c/dom/html/HTMLOptGroupElement.java \
$(src)/org/w3c/dom/html/HTMLOptionElement.java \
$(src)/org/w3c/dom/html/HTMLParagraphElement.java \
$(src)/org/w3c/dom/html/HTMLParamElement.java \
$(src)/org/w3c/dom/html/HTMLPreElement.java \
$(src)/org/w3c/dom/html/HTMLQuoteElement.java \
$(src)/org/w3c/dom/html/HTMLScriptElement.java \
$(src)/org/w3c/dom/html/HTMLSelectElement.java \
$(src)/org/w3c/dom/html/HTMLStyleElement.java \
$(src)/org/w3c/dom/html/HTMLTableCaptionElement.java \
$(src)/org/w3c/dom/html/HTMLTableCellElement.java \
$(src)/org/w3c/dom/html/HTMLTableColElement.java \
$(src)/org/w3c/dom/html/HTMLTableElement.java \
$(src)/org/w3c/dom/html/HTMLTableRowElement.java \
$(src)/org/w3c/dom/html/HTMLTableSectionElement.java \
$(src)/org/w3c/dom/html/HTMLTextAreaElement.java \
$(src)/org/w3c/dom/html/HTMLTitleElement.java \
$(src)/org/w3c/dom/html/HTMLUListElement.java \
$(src)/org/w3c/dom/ls/DOMImplementationLS.java \
$(src)/org/w3c/dom/ls/LSException.java \
$(src)/org/w3c/dom/ls/LSInput.java \
$(src)/org/w3c/dom/ls/LSLoadEvent.java \
$(src)/org/w3c/dom/ls/LSOutput.java \
$(src)/org/w3c/dom/ls/LSParser.java \
$(src)/org/w3c/dom/ls/LSParserFilter.java \
$(src)/org/w3c/dom/ls/LSProgressEvent.java \
$(src)/org/w3c/dom/ls/LSResourceResolver.java \
$(src)/org/w3c/dom/ls/LSSerializer.java \
$(src)/org/w3c/dom/ls/LSSerializerFilter.java \
$(src)/org/w3c/dom/ranges/DocumentRange.java \
$(src)/org/w3c/dom/ranges/Range.java \
$(src)/org/w3c/dom/ranges/RangeException.java \
$(src)/org/w3c/dom/stylesheets/DocumentStyle.java \
$(src)/org/w3c/dom/stylesheets/LinkStyle.java \
$(src)/org/w3c/dom/stylesheets/MediaList.java \
$(src)/org/w3c/dom/stylesheets/StyleSheet.java \
$(src)/org/w3c/dom/stylesheets/StyleSheetList.java \
$(src)/org/w3c/dom/traversal/DocumentTraversal.java \
$(src)/org/w3c/dom/traversal/NodeFilter.java \
$(src)/org/w3c/dom/traversal/NodeIterator.java \
$(src)/org/w3c/dom/traversal/TreeWalker.java \
$(src)/org/w3c/dom/views/AbstractView.java \
$(src)/org/w3c/dom/views/DocumentView.java \
$(src)/org/xml/sax/AttributeList.java \
$(src)/org/xml/sax/Attributes.java \
$(src)/org/xml/sax/ContentHandler.java \
$(src)/org/xml/sax/DTDHandler.java \
$(src)/org/xml/sax/DocumentHandler.java \
$(src)/org/xml/sax/EntityResolver.java \
$(src)/org/xml/sax/ErrorHandler.java \
$(src)/org/xml/sax/HandlerBase.java \
$(src)/org/xml/sax/InputSource.java \
$(src)/org/xml/sax/Locator.java \
$(src)/org/xml/sax/Parser.java \
$(src)/org/xml/sax/SAXException.java \
$(src)/org/xml/sax/SAXNotRecognizedException.java \
$(src)/org/xml/sax/SAXNotSupportedException.java \
$(src)/org/xml/sax/SAXParseException.java \
$(src)/org/xml/sax/XMLFilter.java \
$(src)/org/xml/sax/XMLReader.java \
$(src)/org/xml/sax/ext/Attributes2.java \
$(src)/org/xml/sax/ext/Attributes2Impl.java \
$(src)/org/xml/sax/ext/DeclHandler.java \
$(src)/org/xml/sax/ext/DefaultHandler2.java \
$(src)/org/xml/sax/ext/EntityResolver2.java \
$(src)/org/xml/sax/ext/LexicalHandler.java \
$(src)/org/xml/sax/ext/Locator2.java \
$(src)/org/xml/sax/ext/Locator2Impl.java \
$(src)/org/xml/sax/helpers/AttributeListImpl.java \
$(src)/org/xml/sax/helpers/AttributesImpl.java \
$(src)/org/xml/sax/helpers/DefaultHandler.java \
$(src)/org/xml/sax/helpers/LocatorImpl.java \
$(src)/org/xml/sax/helpers/NamespaceSupport.java \
$(src)/org/xml/sax/helpers/NewInstance.java \
$(src)/org/xml/sax/helpers/ParserAdapter.java \
$(src)/org/xml/sax/helpers/ParserFactory.java \
$(src)/org/xml/sax/helpers/XMLFilterImpl.java \
$(src)/org/xml/sax/helpers/XMLReaderAdapter.java \
$(src)/org/xml/sax/helpers/XMLReaderFactory.java \
$(src)/javax/xml/parsers/ClassStuff.java \
$(src)/javax/xml/parsers/DocumentBuilder.java \
$(src)/javax/xml/parsers/DocumentBuilderFactory.java \
$(src)/javax/xml/parsers/FactoryConfigurationError.java \
$(src)/javax/xml/parsers/ParserConfigurationException.java \
$(src)/javax/xml/parsers/SAXParser.java \
$(src)/javax/xml/parsers/SAXParserFactory.java \
$(src)/javax/xml/transform/ClassStuff.java \
$(src)/javax/xml/transform/ErrorListener.java \
$(src)/javax/xml/transform/OutputKeys.java \
$(src)/javax/xml/transform/Result.java \
$(src)/javax/xml/transform/Source.java \
$(src)/javax/xml/transform/SourceLocator.java \
$(src)/javax/xml/transform/Templates.java \
$(src)/javax/xml/transform/Transformer.java \
$(src)/javax/xml/transform/TransformerConfigurationException.java \
$(src)/javax/xml/transform/TransformerException.java \
$(src)/javax/xml/transform/TransformerFactory.java \
$(src)/javax/xml/transform/TransformerFactoryConfigurationError.java \
$(src)/javax/xml/transform/URIResolver.java \
$(src)/javax/xml/transform/dom/DOMLocator.java \
$(src)/javax/xml/transform/dom/DOMResult.java \
$(src)/javax/xml/transform/dom/DOMSource.java \
$(src)/javax/xml/transform/sax/SAXResult.java \
$(src)/javax/xml/transform/sax/SAXSource.java \
$(src)/javax/xml/transform/sax/SAXTransformerFactory.java \
$(src)/javax/xml/transform/sax/TemplatesHandler.java \
$(src)/javax/xml/transform/sax/TransformerHandler.java \
$(src)/javax/xml/transform/stream/StreamResult.java \
$(src)/javax/xml/transform/stream/StreamSource.java \
$(src)/gnu/xml/aelfred2/JAXPFactory.java \
$(src)/gnu/xml/aelfred2/SAXDriver.java \
$(src)/gnu/xml/aelfred2/XmlParser.java \
$(src)/gnu/xml/aelfred2/XmlReader.java \
$(src)/gnu/xml/dom/Consumer.java \
$(src)/gnu/xml/dom/DomAttr.java \
$(src)/gnu/xml/dom/DomCDATA.java \
$(src)/gnu/xml/dom/DomCharacterData.java \
$(src)/gnu/xml/dom/DomComment.java \
$(src)/gnu/xml/dom/DomDoctype.java \
$(src)/gnu/xml/dom/DomDocument.java \
$(src)/gnu/xml/dom/DomElement.java \
$(src)/gnu/xml/dom/DomEntity.java \
$(src)/gnu/xml/dom/DomEntityReference.java \
$(src)/gnu/xml/dom/DomEvent.java \
$(src)/gnu/xml/dom/DomEx.java \
$(src)/gnu/xml/dom/DomExtern.java \
$(src)/gnu/xml/dom/DomFragment.java \
$(src)/gnu/xml/dom/DomImpl.java \
$(src)/gnu/xml/dom/DomIterator.java \
$(src)/gnu/xml/dom/DomNamedNodeMap.java \
$(src)/gnu/xml/dom/DomNode.java \
$(src)/gnu/xml/dom/DomNotation.java \
$(src)/gnu/xml/dom/DomNsNode.java \
$(src)/gnu/xml/dom/DomPI.java \
$(src)/gnu/xml/dom/DomText.java \
$(src)/gnu/xml/dom/JAXPFactory.java \
$(src)/gnu/xml/pipeline/CallFilter.java \
$(src)/gnu/xml/pipeline/DomConsumer.java \
$(src)/gnu/xml/pipeline/EventConsumer.java \
$(src)/gnu/xml/pipeline/EventFilter.java \
$(src)/gnu/xml/pipeline/LinkFilter.java \
$(src)/gnu/xml/pipeline/NSFilter.java \
$(src)/gnu/xml/pipeline/PipelineFactory.java \
$(src)/gnu/xml/pipeline/TeeConsumer.java \
$(src)/gnu/xml/pipeline/TextConsumer.java \
$(src)/gnu/xml/pipeline/ValidationConsumer.java \
$(src)/gnu/xml/pipeline/WellFormednessFilter.java \
$(src)/gnu/xml/pipeline/XIncludeFilter.java \
$(src)/gnu/xml/pipeline/XsltFilter.java \
$(src)/gnu/xml/util/DoParse.java \
$(src)/gnu/xml/util/DomParser.java \
$(src)/gnu/xml/util/Resolver.java \
$(src)/gnu/xml/util/SAXNullTransformerFactory.java \
$(src)/gnu/xml/util/XCat.java \
$(src)/gnu/xml/util/XHTMLWriter.java \
$(src)/gnu/xml/util/XMLWriter.java \
$(src)/gnu/xml/libxmlj/sax/GnomeLocator.java \
$(src)/gnu/xml/libxmlj/sax/GnomeSAXParser.java \
$(src)/gnu/xml/libxmlj/sax/GnomeSAXParserFactory.java \
$(src)/gnu/xml/libxmlj/sax/GnomeXMLReader.java \
$(src)/gnu/xml/libxmlj/sax/Namespaces.java \
$(src)/gnu/xml/libxmlj/sax/StringArrayAttributes.java \
$(src)/gnu/xml/libxmlj/sax/XMLName.java \
$(src)/gnu/xml/libxmlj/dom/GnomeAttr.java \
$(src)/gnu/xml/libxmlj/dom/GnomeCDATASection.java \
$(src)/gnu/xml/libxmlj/dom/GnomeCharacterData.java \
$(src)/gnu/xml/libxmlj/dom/GnomeComment.java \
$(src)/gnu/xml/libxmlj/dom/GnomeDocument.java \
$(src)/gnu/xml/libxmlj/dom/GnomeDocumentBuilder.java \
$(src)/gnu/xml/libxmlj/dom/GnomeDocumentBuilderFactory.java \
$(src)/gnu/xml/libxmlj/dom/GnomeDocumentFragment.java \
$(src)/gnu/xml/libxmlj/dom/GnomeDocumentType.java \
$(src)/gnu/xml/libxmlj/dom/GnomeElement.java \
$(src)/gnu/xml/libxmlj/dom/GnomeEntity.java \
$(src)/gnu/xml/libxmlj/dom/GnomeEntityReference.java \
$(src)/gnu/xml/libxmlj/dom/GnomeNamedNodeMap.java \
$(src)/gnu/xml/libxmlj/dom/GnomeNode.java \
$(src)/gnu/xml/libxmlj/dom/GnomeNodeList.java \
$(src)/gnu/xml/libxmlj/dom/GnomeNotation.java \
$(src)/gnu/xml/libxmlj/dom/GnomeProcessingInstruction.java \
$(src)/gnu/xml/libxmlj/dom/GnomeText.java \
$(src)/gnu/xml/libxmlj/dom/MatchingNodeList.java \
$(src)/gnu/xml/libxmlj/transform/DefaultErrorListenerImpl.java \
$(src)/gnu/xml/libxmlj/transform/DefaultURIResolverImpl.java \
$(src)/gnu/xml/libxmlj/transform/ErrorListenerProxy.java \
$(src)/gnu/xml/libxmlj/transform/IOToolkit.java \
$(src)/gnu/xml/libxmlj/transform/JavaContext.java \
$(src)/gnu/xml/libxmlj/transform/LibxmlDocument.java \
$(src)/gnu/xml/libxmlj/transform/LibxsltStylesheet.java \
$(src)/gnu/xml/libxmlj/transform/SourceLocatorImpl.java \
$(src)/gnu/xml/libxmlj/transform/SourceWrapper.java \
$(src)/gnu/xml/libxmlj/transform/TemplatesImpl.java \
$(src)/gnu/xml/libxmlj/transform/TransformTest.java \
$(src)/gnu/xml/libxmlj/transform/TransformerFactoryImpl.java \
$(src)/gnu/xml/libxmlj/transform/TransformerImpl.java \
$(src)/gnu/xml/libxmlj/transform/URIResolverProxy.java \
$(src)/gnu/xml/libxmlj/util/EmptyNodeList.java \
$(src)/gnu/xml/libxmlj/util/StandaloneDocumentType.java \
$(src)/gnu/xml/libxmlj/util/StandaloneLocator.java

classes = $(patsubst $(src)/%.java, $(dst)/%.class, $(sources))

headers = \
gnu_xml_libxmlj_sax_GnomeLocator.h \
gnu_xml_libxmlj_sax_GnomeXMLReader.h \
gnu_xml_libxmlj_dom_GnomeAttr.h \
gnu_xml_libxmlj_dom_GnomeDocument.h \
gnu_xml_libxmlj_dom_GnomeDocumentBuilder.h \
gnu_xml_libxmlj_dom_GnomeDocumentType.h \
gnu_xml_libxmlj_dom_GnomeElement.h \
gnu_xml_libxmlj_dom_GnomeEntity.h \
gnu_xml_libxmlj_dom_GnomeNamedNodeMap.h \
gnu_xml_libxmlj_dom_GnomeNode.h \
gnu_xml_libxmlj_dom_GnomeNodeList.h \
gnu_xml_libxmlj_dom_GnomeNotation.h \
gnu_xml_libxmlj_dom_GnomeProcessingInstruction.h \
gnu_xml_libxmlj_dom_MatchingNodeList.h \
javax_xml_transform_OutputKeys.h \
gnu_xml_libxmlj_transform_JavaContext.h \
gnu_xml_libxmlj_transform_LibxmlDocument.h \
gnu_xml_libxmlj_transform_LibxsltStylesheet.h \
gnu_xml_libxmlj_transform_TransformerFactoryImpl.h

libxmlj_la_DEPENDENCIES = $(JARFILE) $(headers)

libxmlj_la_SOURCES = \
xmlj_dom.c \
xmlj_error.c \
xmlj_io.c \
xmlj_node.c \
xmlj_sax.c \
xmlj_transform.c \
xmlj_util.c

libxmlj_la_LDFLAGS = -release 0.1.2 -export-dynamic
libxmlj_la_CFLAGS = -pedantic -Wall -fbounds-check

package_names = \
javax.xml.parsers \
javax.xml.transform \
javax.xml.transform.dom \
javax.xml.transform.sax \
javax.xml.transform.stream \
org.w3c.dom \
org.w3c.dom.bootstrap \
org.w3c.dom.css \
org.w3c.dom.events \
org.w3c.dom.html \
org.w3c.dom.ls \
org.w3c.dom.ranges \
org.w3c.dom.stylesheets \
org.w3c.dom.traversal \
org.w3c.dom.views \
org.xml.sax \
org.xml.sax.ext \
org.xml.sax.helpers \
gnu.xml.aelfred2 \
gnu.xml.dom \
gnu.xml.libxmlj.dom \
gnu.xml.libxmlj.sax \
gnu.xml.libxmlj.transform \
gnu.xml.pipeline \
gnu.xml.util

BUILT_SOURCES = ${libxmlj_la_DEPENDENCIES}

# Generate header files for classes containing native code.
#$(headers): %.h: $(dst)/$(%:_=/).class) $(header_deps)
#	@JAVAH@ -jni -d $(jni) -classpath $(dst) $($<:/=.)

# I did remove the -force argument (not available in gcjh nor in kaffe)

gnu_xml_libxmlj_dom_%.h: $(JARFILE)
	CLASSPATH=$(JARFILE) @JAVAH@ -jni gnu.xml.libxmlj.dom.$*

gnu_xml_libxmlj_sax_%.h: $(JARFILE)
	CLASSPATH=$(JARFILE) @JAVAH@ -jni gnu.xml.libxmlj.sax.$*

gnu_xml_libxmlj_transform_%.h: $(JARFILE)
	CLASSPATH=$(JARFILE) @JAVAH@ -jni gnu.xml.libxmlj.transform.$*

javax_xml_transform_%.h: 
	CLASSPATH=$(JARFILE) @JAVAH@ -jni javax.xml.transform.$*

# Rule for compiling Java source files
$(classes): $(dst)/%.class: $(src)/%.java $(dst)
	@JAVAC@ @JAVACFLAGS@ -d $(dst) -classpath $(src) $<

$(dst):
	mkdir -p $(dst)

# Jar file
$(JARFILE): $(classes)
	@JAR@ cfm $(JARFILE) $(top_srcdir)/manifest.mf \
		-C $(dst) gnu \
		-C $(dst) javax \
		-C $(dst) org

# API documentation

javadoc:
	mkdir -p $(doc)
	$(JAVADOC) -d $(doc) -windowtitle "GNU JAXP Library" \
		-sourcepath @srcdir@/source $(package_names)

# Remove classes and jar file

clean-local:
	rm -f $(JARFILE)
	rm -rf $(dst) $(doc)
	rm -f $(headers)

# Run a simple transformation to check that the package is working

check-local: gnu/xml/libxmlj/transform/TransformTest.class
	@JAVA@  -classpath "${CLASSPATH}:${JARFILE}:." -Djava.library.path=.libs gnu.xml.libxmlj.transform.TransformTest @srcdir@/testdocs/test.xml @srcdir@/testdocs/testsheet.xsl transformed.xml
	diff @srcdir@/testdocs/transformed.xml transformed.xml

dist-hook:
	mkdir $(distdir)/source/gnu
	cp -r $(srcdir)/source/gnu/* $(distdir)/source/gnu
	mkdir $(distdir)/testdocs
	cp $(srcdir)/testdocs/* $(distdir)/testdocs

install-data-local:
	@srcdir@/mkinstalldirs $(prefix)/share/java
	$(INSTALL_DATA) ${JARFILE} $(prefix)/share/java

