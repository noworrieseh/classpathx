/*
 * xmlj_dom.c
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
 * along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 * As a special exception, if you link this library with other files to
 * produce an executable, this library does not by itself cause the
 * resulting executable to be covered by the GNU General Public License.
 * This exception does not however invalidate any other reasons why the
 * executable file might be covered by the GNU General Public License.
 */
#include "xmlj_dom.h"
#include "xmlj_node.h"
#include "xmlj_util.h"

#include <sys/types.h>
#include <sys/stat.h>
#include <fcntl.h>
#include <unistd.h>

resolveEntitySAXFunc defaultResolveEntity;
warningSAXFunc defaultWarning;
errorSAXFunc defaultError;
fatalErrorSAXFunc defaultFatalError;

JNIEnv *dom_cb_env;
jobject dom_cb_obj;

/* -- GnomeAttr -- */

/*
 * Class:     gnu_xml_libxmlj_dom_GnomeAttr
 * Method:    getSpecified
 * Signature: ()Z
 */
JNIEXPORT jboolean JNICALL
Java_gnu_xml_libxmlj_dom_GnomeAttr_getSpecified (JNIEnv *env,
    jobject self)
{
  xmlAttrPtr attr;

  attr = (xmlAttrPtr)xmljGetNodeID(env, self);
  return (attr->atype != 0);
}

/*
 * Class:     gnu_xml_libxmlj_dom_GnomeAttr
 * Method:    getValue
 * Signature: ()Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL
Java_gnu_xml_libxmlj_dom_GnomeAttr_getValue (JNIEnv *env,
    jobject self)
{
  xmlNodePtr node;
  xmlBufferPtr buf;
  jstring ret;

  node = xmljGetNodeID(env, self);
  buf = xmlBufferCreate();
  xmlNodeBufGetContent(buf, node);
  ret = xmljNewString(env, buf->content);
  xmlFree(buf);
  return ret;
}

/*
 * Class:     gnu_xml_libxmlj_dom_GnomeAttr
 * Method:    setValue
 * Signature: (Ljava/lang/String;)V
 */
JNIEXPORT void JNICALL
Java_gnu_xml_libxmlj_dom_GnomeAttr_setValue (JNIEnv *env,
    jobject self,
    jstring value)
{
  xmlNodePtr node;
  const xmlChar *s_value;

  node = xmljGetNodeID(env, self);
  s_value = xmljGetStringChars(env, value);
  xmlNodeSetContent(node, s_value);
  xmljReleaseStringChars(env, value, s_value);
}

/* -- GnomeDocument -- */

/*
 * Class:     gnu_xml_libxmlj_dom_GnomeDocument
 * Method:    finalize
 * Signature: ()V
 */
JNIEXPORT void JNICALL
Java_gnu_xml_libxmlj_dom_GnomeDocument_finalize (JNIEnv *env,
    jobject self)
{
  xmlDocPtr doc;
  
  doc = (xmlDocPtr)xmljGetNodeID(env, self);
  xmlFree(doc);
}

/*
 * Class:     gnu_xml_libxmlj_dom_GnomeDocument
 * Method:    getDoctype
 * Signature: ()Lorg/w3c/dom/DocumentType;
 */
JNIEXPORT jobject JNICALL
Java_gnu_xml_libxmlj_dom_GnomeDocument_getDoctype (JNIEnv *env,
    jobject self)
{
  xmlDocPtr doc;
  xmlDtdPtr dtd;

  doc = (xmlDocPtr)xmljGetNodeID(env, self);
  dtd = doc->extSubset;
  return xmljGetNodeInstance(env, (xmlNodePtr)dtd);
}

/*
 * Class:     gnu_xml_libxmlj_dom_GnomeDocument
 * Method:    getDocumentElement
 * Signature: ()Lorg/w3c/dom/Element;
 */
JNIEXPORT jobject JNICALL
Java_gnu_xml_libxmlj_dom_GnomeDocument_getDocumentElement (JNIEnv *env,
    jobject self)
{
  xmlDocPtr doc;

  doc = (xmlDocPtr)xmljGetNodeID(env, self);
  return xmljGetNodeInstance(env, xmlDocGetRootElement(doc));
}

/*
 * Class:     gnu_xml_libxmlj_dom_GnomeDocument
 * Method:    createDocumentFragment
 * Signature: ()Lorg/w3c/dom/DocumentFragment;
 */
JNIEXPORT jobject JNICALL
Java_gnu_xml_libxmlj_dom_GnomeDocument_createDocumentFragment (JNIEnv *env,
    jobject self)
{
  xmlDocPtr doc;

  doc = (xmlDocPtr)xmljGetNodeID(env, self);
  return xmljGetNodeInstance(env, xmlNewDocFragment(doc));
}

/*
 * Class:     gnu_xml_libxmlj_dom_GnomeDocument
 * Method:    createTextNode
 * Signature: (Ljava/lang/String;)Lorg/w3c/dom/Text;
 */
JNIEXPORT jobject JNICALL
Java_gnu_xml_libxmlj_dom_GnomeDocument_createTextNode (JNIEnv *env,
    jobject self,
    jstring data)
{
  xmlDocPtr doc;
  xmlNodePtr text;
  const xmlChar *s_data;

  doc = (xmlDocPtr)xmljGetNodeID(env, self);
  s_data = xmljGetStringChars(env, data);
  text = xmlNewDocText(doc, s_data);
  xmljReleaseStringChars(env, data, s_data);
  return xmljGetNodeInstance(env, text);
}

/*
 * Class:     gnu_xml_libxmlj_dom_GnomeDocument
 * Method:    createComment
 * Signature: (Ljava/lang/String;)Lorg/w3c/dom/Comment;
 */
JNIEXPORT jobject JNICALL
Java_gnu_xml_libxmlj_dom_GnomeDocument_createComment (JNIEnv *env,
    jobject self,
    jstring data)
{
  xmlDocPtr doc;
  xmlNodePtr comment;
  const xmlChar *s_data;

  doc = (xmlDocPtr)xmljGetNodeID(env, self);
  s_data = xmljGetStringChars(env, data);
  comment = xmlNewDocComment(doc, s_data);
  xmljReleaseStringChars(env, data, s_data);
  return xmljGetNodeInstance(env, comment);
}

/*
 * Class:     gnu_xml_libxmlj_dom_GnomeDocument
 * Method:    createCDATASection
 * Signature: (Ljava/lang/String;)Lorg/w3c/dom/CDATASection;
 */
JNIEXPORT jobject JNICALL
Java_gnu_xml_libxmlj_dom_GnomeDocument_createCDATASection (JNIEnv *env,
    jobject self,
    jstring data)
{
  xmlDocPtr doc;
  xmlNodePtr cdata;
  const xmlChar *s_data;
  int len;

  doc = (xmlDocPtr)xmljGetNodeID(env, self);
  s_data = xmljGetStringChars(env, data);
  len = xmlStrlen(s_data);
  cdata = xmlNewCDataBlock(doc, s_data, len);
  xmljReleaseStringChars(env, data, s_data);
  return xmljGetNodeInstance(env, cdata);
}

/*
 * Class:     gnu_xml_libxmlj_dom_GnomeDocument
 * Method:    createProcessingInstruction
 * Signature: (Ljava/lang/String;Ljava/lang/String;)Lorg/w3c/dom/ProcessingInstruction;
 */
JNIEXPORT jobject JNICALL
Java_gnu_xml_libxmlj_dom_GnomeDocument_createProcessingInstruction (JNIEnv *env,
    jobject self,
    jstring target,
    jstring data)
{
  xmlDocPtr doc;
  xmlNodePtr pi;
  const xmlChar *s_target;
  const xmlChar *s_data;

  doc = (xmlDocPtr)xmljGetNodeID(env, self);
  s_target = xmljGetStringChars(env, target);
  s_data = xmljGetStringChars(env, data);
  pi = xmlNewPI(s_target, s_data);
  xmljReleaseStringChars(env, target, s_target);
  xmljReleaseStringChars(env, data, s_data);
  return xmljGetNodeInstance(env, pi);
}

/*
 * Class:     gnu_xml_libxmlj_dom_GnomeDocument
 * Method:    createEntityReference
 * Signature: (Ljava/lang/String;)Lorg/w3c/dom/EntityReference;
 */
JNIEXPORT jobject JNICALL
Java_gnu_xml_libxmlj_dom_GnomeDocument_createEntityReference (JNIEnv *env,
    jobject self,
    jstring name)
{
  xmlDocPtr doc;
  xmlNodePtr ref;
  const xmlChar *s_name;

  doc = (xmlDocPtr)xmljGetNodeID(env, self);
  s_name = xmljGetStringChars(env, name);
  ref = xmlNewReference(doc, s_name);
  xmljReleaseStringChars(env, name, s_name);
  return xmljGetNodeInstance(env, ref);
}

/*
 * Class:     gnu_xml_libxmlj_dom_GnomeDocument
 * Method:    getElementsByTagName
 * Signature: (Ljava/lang/String;)Lorg/w3c/dom/NodeList;
 */
JNIEXPORT jobject JNICALL
Java_gnu_xml_libxmlj_dom_GnomeDocument_getElementsByTagName (JNIEnv *env,
    jobject self,
    jstring qName)
{
  /* TODO */
  return NULL;
}

/*
 * Class:     gnu_xml_libxmlj_dom_GnomeDocument
 * Method:    importNode
 * Signature: (Lorg/w3c/dom/Node;Z)Lorg/w3c/dom/Node;
 */
JNIEXPORT jobject JNICALL
Java_gnu_xml_libxmlj_dom_GnomeDocument_importNode (JNIEnv *env,
    jobject self,
    jobject importedNode,
    jboolean deep)
{
  /* TODO */
  return NULL;
}

/*
 * Class:     gnu_xml_libxmlj_dom_GnomeDocument
 * Method:    createElementNS
 * Signature: (Ljava/lang/String;Ljava/lang/String;)Lorg/w3c/dom/Element;
 */
JNIEXPORT jobject JNICALL
Java_gnu_xml_libxmlj_dom_GnomeDocument_createElementNS (JNIEnv *env,
    jobject self,
    jstring uri,
    jstring qName)
{
  xmlDocPtr doc;
  xmlNodePtr element;
  xmlNsPtr ns;
  const xmlChar *s_uri;
  const xmlChar *s_qName;
  const xmlChar *s_prefix;
  const xmlChar *s_localName;

  doc = (xmlDocPtr)xmljGetNodeID(env, self);
  s_qName = xmljGetStringChars(env, qName);
  if (uri != NULL)
  {
    s_uri = xmljGetStringChars(env, uri);
    /* FIXME separate prefix and localName */
    s_prefix = NULL;
    s_localName = s_qName;
    ns = NULL;
    element = xmlNewDocNode(doc, ns, s_localName, NULL);
    xmljReleaseStringChars(env, uri, s_uri);
  }
  else
    element = xmlNewDocNode(doc, NULL, s_qName, NULL);
  xmljReleaseStringChars(env, qName, s_qName);
  return xmljGetNodeInstance(env, element);
}

/*
 * Class:     gnu_xml_libxmlj_dom_GnomeDocument
 * Method:    createAttributeNS
 * Signature: (Ljava/lang/String;Ljava/lang/String;)Lorg/w3c/dom/Attr;
 */
JNIEXPORT jobject JNICALL
Java_gnu_xml_libxmlj_dom_GnomeDocument_createAttributeNS (JNIEnv *env,
    jobject self,
    jstring uri,
    jstring qName)
{
  xmlDocPtr doc;
  xmlNodePtr attr;
  xmlNsPtr ns;
  const xmlChar *s_uri;
  const xmlChar *s_qName;
  const xmlChar *s_prefix;
  const xmlChar *s_localName;

  doc = (xmlDocPtr)xmljGetNodeID(env, self);
  s_qName = xmljGetStringChars(env, qName);
  if (uri != NULL)
  {
    s_uri = xmljGetStringChars(env, uri);
    /* FIXME separate prefix and localName */
    s_prefix = NULL;
    s_localName = s_qName;
    ns = NULL;
    attr = (xmlNodePtr)xmlNewNsProp((xmlNodePtr)doc, ns, s_localName, NULL);
    xmljReleaseStringChars(env, uri, s_uri);
  }
  else
    attr = (xmlNodePtr)xmlNewNsProp((xmlNodePtr)doc, NULL, s_qName, NULL);
  xmljReleaseStringChars(env, qName, s_qName);
  return xmljGetNodeInstance(env, attr);
}

/*
 * Class:     gnu_xml_libxmlj_dom_GnomeDocument
 * Method:    getElementsByTagNameNS
 * Signature: (Ljava/lang/String;Ljava/lang/String;)Lorg/w3c/dom/NodeList;
 */
JNIEXPORT jobject JNICALL
Java_gnu_xml_libxmlj_dom_GnomeDocument_getElementsByTagNameNS (JNIEnv *env,
    jobject self,
    jstring uri,
    jstring localName)
{
  /* TODO */
  return NULL;
}

/*
 * Class:     gnu_xml_libxmlj_dom_GnomeDocument
 * Method:    getElementById
 * Signature: (Ljava/lang/String;)Lorg/w3c/dom/Element;
 */
JNIEXPORT jobject JNICALL
Java_gnu_xml_libxmlj_dom_GnomeDocument_getElementById (JNIEnv *env,
    jobject self,
    jstring elementId)
{
  /* TODO */
  return NULL;
}

/* -- GnomeDocumentBuilder -- */

/*
 * Class:     gnu_xml_libxmlj_dom_GnomeDocumentBuilder
 * Method:    init
 * Signature: ()V
 */
JNIEXPORT void JNICALL
Java_gnu_xml_libxmlj_dom_GnomeDocumentBuilder_init (JNIEnv *env,
    jobject self,
    jboolean validate,
    jboolean coalesce,
    jboolean expandEntities)
{
  xmlParserCtxtPtr context;
  jclass cls;
  jfieldID field;
  int options;
  
  dom_cb_env = env;
  dom_cb_obj = self;
  
  /* Initialise context */
  context = xmlNewParserCtxt();
  if (context != NULL)
  {
    defaultResolveEntity = context->sax->resolveEntity;
    defaultWarning = context->sax->warning;
    defaultError = context->sax->error;
    defaultFatalError = context->sax->fatalError;
    options = 0;
    
    if (validate)
    {
      options |= XML_PARSE_DTDLOAD;
      options |= XML_PARSE_DTDVALID;
    }
    if (coalesce)
      options |= XML_PARSE_NOCDATA;
    if (expandEntities)
      options |= XML_PARSE_NOENT;
    
    xmlCtxtUseOptions(context, options);
  }
  else if (validate)
  {
    /* TODO throw exception */
  }
  
  /* Save into field */
  cls = (*env)->GetObjectClass(env, self);
  field = (*env)->GetFieldID(env, cls, "context", "I");
  (*env)->SetIntField(env, self, field, (jint)context);
}

/*
 * Class:     gnu_xml_libxmlj_dom_GnomeDocumentBuilder
 * Method:    finalize
 * Signature: ()V
 */
JNIEXPORT void JNICALL
Java_gnu_xml_libxmlj_dom_GnomeDocumentBuilder_finalize (JNIEnv *env,
    jobject self)
{
  xmlParserCtxtPtr context;
  
  context = getContext(env, self);
  
  /* Free */
  if (context != NULL)
    xmlClearParserCtxt(context);
}

/*
 * Class:     gnu_xml_libxmlj_dom_GnomeDocumentBuilder
 * Method:    parseFile
 * Signature: (Ljava/lang/String;)Lorg/w3c/dom/Document;
 */
JNIEXPORT jobject JNICALL
Java_gnu_xml_libxmlj_dom_GnomeDocumentBuilder_parseFile (JNIEnv *env,
    jobject self,
    jstring filename)
{
  xmlParserCtxtPtr context;
  xmlDocPtr doc;
  const char *s_filename;
  int fd;

  /* Get parser context */
  context = getContext(env, self);
  
  dom_cb_env = env;
  dom_cb_obj = self;
  
  /* Parse */
  s_filename = (*env)->GetStringUTFChars(env, filename, 0);
  if (context == NULL)
    doc = xmlParseFile(s_filename);
  else
  {
    fd = open(s_filename, O_RDONLY);
    if (fd == -1)
      doc = NULL;
    else
    {
      doc = xmlCtxtReadFd(context, fd, NULL, NULL, context->options);
      close(fd);
      xmlCtxtReset(context);
    }
  }
  (*env)->ReleaseStringUTFChars(env, filename, s_filename);
  if (doc == NULL)
    return NULL;
  else
    return createDocument(env, self, doc);
}

/*
 * Class:     gnu_xml_libxmlj_dom_GnomeDocumentBuilder
 * Method:    parseMemory
 * Signature: ([B)Lorg/w3c/dom/Document;
 */
JNIEXPORT jobject JNICALL
Java_gnu_xml_libxmlj_dom_GnomeDocumentBuilder_parseMemory (JNIEnv *env,
    jobject self,
    jbyteArray bytes)
{
  xmlParserCtxtPtr context;
  xmlDocPtr doc;
  xmlChar *s_bytes;
  jint len;

  /* Get parser context */
  context = getContext(env, self);
  
  dom_cb_env = env;
  dom_cb_obj = self;
  
  /* Parse */
  len = (*env)->GetArrayLength(env, bytes);
  s_bytes = (*env)->GetByteArrayElements(env, bytes, 0);
  if (context == NULL)
    doc = xmlParseMemory(s_bytes, len);
  else
  {
    doc = xmlCtxtReadDoc(context, s_bytes, NULL, NULL,
        context->options);
    xmlCtxtReset(context);
  }
  (*env)->ReleaseByteArrayElements(env, bytes, s_bytes, 0);
  if (doc == NULL)
    return NULL;
  else
    return createDocument(env, self, doc);
}

/*
 * Class:     gnu_xml_libxmlj_dom_GnomeDocumentBuilder
 * Method:    setCustomEntityResolver
 * Signature: (Z)V
 */
JNIEXPORT void JNICALL
Java_gnu_xml_libxmlj_dom_GnomeDocumentBuilder_setCustomEntityResolver (JNIEnv *env,
    jobject self,
    jboolean flag)
{
  xmlParserCtxtPtr context;
  
  context = getContext(env, self);
  if (context != NULL)
  {
    if (flag)
      context->sax->resolveEntity = customResolveEntity;
    else
      context->sax->resolveEntity = defaultResolveEntity;
  }
  else
  {
    /* TODO raise exception */
  }
}

/*
 * Class:     gnu_xml_libxmlj_dom_GnomeDocumentBuilder
 * Method:    setCustomErrorHandler
 * Signature: (Z)V
 */
JNIEXPORT void JNICALL
Java_gnu_xml_libxmlj_dom_GnomeDocumentBuilder_setCustomErrorHandler (JNIEnv *env,
    jobject self,
    jboolean flag)
{
  xmlParserCtxtPtr context;
  
  context = getContext(env, self);
  if (context != NULL)
  {
    if (flag)
    {
      context->sax->warning = customWarning;
      context->sax->error = customError;
      context->sax->fatalError = customFatalError;
    }
    else
    {
      context->sax->warning = defaultWarning;
      context->sax->error = defaultError;
      context->sax->fatalError = defaultFatalError;
    }
  }
  else
  {
    /* TODO raise exception */
  }
}

/*
 * Class:     gnu_xml_libxmlj_dom_GnomeDocumentBuilder
 * Method:    createDocument
 * Signature: (Ljava/lang/String;Ljava/lang/String;Lorg/w3c/dom/DocumentType;)Lorg/w3c/dom/Document;
 */
JNIEXPORT jobject JNICALL
Java_gnu_xml_libxmlj_dom_GnomeDocumentBuilder_createDocument (JNIEnv *env,
    jobject self,
    jstring namespaceURI,
    jstring qualifiedName,
    jobject doctype)
{
  xmlDocPtr doc;

  doc = xmlNewDoc("1.0");
  /* TODO namespaceURI
   * TODO qualifiedName
   * TODO doctype */
  return createDocument(env, self, doc);
}

/* -- GnomeDocumentType -- */

/*
 * Class:     gnu_xml_libxmlj_dom_GnomeDocumentType
 * Method:    getEntities
 * Signature: ()Lorg/w3c/dom/NamedNodeMap;
 */
JNIEXPORT jobject JNICALL
Java_gnu_xml_libxmlj_dom_GnomeDocumentType_getEntities (JNIEnv *env,
    jobject self)
{
  xmlDtdPtr dtd;

  dtd = (xmlDtdPtr)xmljGetNodeID(env, self);
  /* TODO dtd->entities */
  return NULL;
}

/*
 * Class:     gnu_xml_libxmlj_dom_GnomeDocumentType
 * Method:    getNotations
 * Signature: ()Lorg/w3c/dom/NamedNodeMap;
 */
JNIEXPORT jobject JNICALL
Java_gnu_xml_libxmlj_dom_GnomeDocumentType_getNotations (JNIEnv *env,
    jobject self)
{
  xmlDtdPtr dtd;

  dtd = (xmlDtdPtr)xmljGetNodeID(env, self);
  /* TODO dtd->entities */
  return NULL;
}

/*
 * Class:     gnu_xml_libxmlj_dom_GnomeDocumentType
 * Method:    getPublicId
 * Signature: ()Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL
Java_gnu_xml_libxmlj_dom_GnomeDocumentType_getPublicId (JNIEnv *env,
    jobject self)
{
  xmlDtdPtr dtd;
  
  dtd = (xmlDtdPtr)xmljGetNodeID(env, self);
  return xmljNewString(env, dtd->ExternalID);
}

/*
 * Class:     gnu_xml_libxmlj_dom_GnomeDocumentType
 * Method:    getSystemId
 * Signature: ()Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL
Java_gnu_xml_libxmlj_dom_GnomeDocumentType_getSystemId (JNIEnv *env,
    jobject self)
{
  xmlDtdPtr dtd;
  
  dtd = (xmlDtdPtr)xmljGetNodeID(env, self);
  return xmljNewString(env, dtd->SystemID);
}

/*
 * Class:     gnu_xml_libxmlj_dom_GnomeDocumentType
 * Method:    getInternalSubset
 * Signature: ()Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL
Java_gnu_xml_libxmlj_dom_GnomeDocumentType_getInternalSubset (JNIEnv *env,
    jobject self)
{
  /* TODO */
  return NULL;
}

/* -- GnomeElement -- */

/*
 * Class:     gnu_xml_libxmlj_dom_GnomeElement
 * Method:    removeAttributeNode
 * Signature: (Lorg/w3c/dom/Attr;)Lorg/w3c/dom/Attr;
 */
JNIEXPORT jobject JNICALL
Java_gnu_xml_libxmlj_dom_GnomeElement_removeAttributeNode (JNIEnv *env,
    jobject self,
    jobject oldAttr)
{
  /* TODO */
  return NULL;
}

/*
 * Class:     gnu_xml_libxmlj_dom_GnomeElement
 * Method:    getAttributeNS
 * Signature: (Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL
Java_gnu_xml_libxmlj_dom_GnomeElement_getAttributeNS (JNIEnv *env,
    jobject self,
    jstring uri,
    jstring localName)
{
  xmlNodePtr node;
  const xmlChar *s_uri;
  const xmlChar *s_localName;
  const xmlChar *s_value;

  node = xmljGetNodeID(env, self);
  s_localName = xmljGetStringChars(env, localName);
  if (uri == NULL)
  {
    s_value = xmlGetNoNsProp(node, s_localName);
  }
  else
  {
    s_uri = xmljGetStringChars(env, uri);
    s_value = xmlGetNsProp(node, s_localName, s_uri);
    xmljReleaseStringChars(env, uri, s_uri);
  }
  xmljReleaseStringChars(env, localName, s_localName);
  return xmljNewString(env, s_value);
}

/*
 * Class:     gnu_xml_libxmlj_dom_GnomeElement
 * Method:    setAttributeNS
 * Signature: (Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V
 */
JNIEXPORT void JNICALL
Java_gnu_xml_libxmlj_dom_GnomeElement_setAttributeNS (JNIEnv *env,
    jobject self,
    jstring uri,
    jstring qName,
    jstring value)
{
  xmlNodePtr node;
  xmlNsPtr ns;
  const xmlChar *s_uri;
  const xmlChar *s_qName;
  const xmlChar *s_prefix;
  const xmlChar *s_localName;
  const xmlChar *s_value;

  node = xmljGetNodeID(env, self);
  s_qName = xmljGetStringChars(env, qName);
  s_value = xmljGetStringChars(env, value);
  if (uri == NULL)
  {
    xmlSetProp(node, s_qName, s_value);
  }
  else
  {
    /* FIXME separate localName and prefix from qName */
    s_prefix = NULL;
    s_localName = s_qName;
    s_uri = xmljGetStringChars(env, uri);
    ns = xmlNewNs(node, s_uri, s_prefix);
    xmlSetNsProp(node, ns, s_localName, s_value);
    xmljReleaseStringChars(env, uri, s_uri);
  }
  xmljReleaseStringChars(env, qName, s_qName);
  xmljReleaseStringChars(env, value, s_value);
}

/*
 * Class:     gnu_xml_libxmlj_dom_GnomeElement
 * Method:    removeAttributeNS
 * Signature: (Ljava/lang/String;Ljava/lang/String;)V
 */
JNIEXPORT void JNICALL
Java_gnu_xml_libxmlj_dom_GnomeElement_removeAttributeNS (JNIEnv *env,
    jobject self,
    jstring uri,
    jstring localName)
{
  /* TODO */
}

/*
 * Class:     gnu_xml_libxmlj_dom_GnomeElement
 * Method:    getAttributeNodeNS
 * Signature: (Ljava/lang/String;Ljava/lang/String;)Lorg/w3c/dom/Attr;
 */
JNIEXPORT jobject JNICALL
Java_gnu_xml_libxmlj_dom_GnomeElement_getAttributeNodeNS (JNIEnv *env,
    jobject self,
    jstring uri,
    jstring localName)
{
  xmlNodePtr node;
  xmlAttrPtr attr;
  const xmlChar *s_uri;
  const xmlChar *s_localName;
  
  node = xmljGetNodeID(env, self);
  attr = node->properties;
  s_uri = xmljGetStringChars(env, uri);
  s_localName = xmljGetStringChars(env, localName);
  while (attr != NULL)
  {
    if (s_uri == NULL)
    {
      if (xmljMatch(s_localName, (xmlNodePtr)attr))
        break;
    }
    else
    {
      if (xmljMatchNS(s_uri, s_localName, (xmlNodePtr)attr))
        break;
    }
    attr = attr->next;
  }
  if (uri != NULL)
    xmljReleaseStringChars(env, uri, s_uri);
  xmljReleaseStringChars(env, localName, s_localName);
  return xmljGetNodeInstance(env, (xmlNodePtr)attr);
}

/*
 * Class:     gnu_xml_libxmlj_dom_GnomeElement
 * Method:    setAttributeNodeNS
 * Signature: (Lorg/w3c/dom/Attr;)Lorg/w3c/dom/Attr;
 */
JNIEXPORT jobject JNICALL
Java_gnu_xml_libxmlj_dom_GnomeElement_setAttributeNodeNS (JNIEnv *env,
    jobject self,
    jobject newAttr)
{
  /* TODO */
  return NULL;
}

/*
 * Class:     gnu_xml_libxmlj_dom_GnomeElement
 * Method:    getElementsByTagNameNS
 * Signature: (Ljava/lang/String;Ljava/lang/String;)Lorg/w3c/dom/NodeList;
 */
JNIEXPORT jobject JNICALL
Java_gnu_xml_libxmlj_dom_GnomeElement_getElementsByTagNameNS (JNIEnv *env,
    jobject self,
    jstring uri,
    jstring localName)
{
  /* TODO */
  return NULL;
}

/*
 * Class:     gnu_xml_libxmlj_dom_GnomeElement
 * Method:    hasAttributeNS
 * Signature: (Ljava/lang/String;Ljava/lang/String;)Z
 */
JNIEXPORT jboolean JNICALL
Java_gnu_xml_libxmlj_dom_GnomeElement_hasAttributeNS (JNIEnv *env,
    jobject self,
    jstring uri,
    jstring localName)
{
  xmlNodePtr node;
  xmlAttrPtr attr;
  const xmlChar *s_uri;
  const xmlChar *s_localName;

  node = xmljGetNodeID(env, self);
  attr = node->properties;
  s_uri = xmljGetStringChars(env, uri);
  s_localName = xmljGetStringChars(env, localName);
  while (attr != NULL)
  {
    if (s_uri == NULL)
    {
      if (xmljMatch(s_localName, (xmlNodePtr)attr))
        break;
    }
    else
    {
      if (xmljMatchNS(s_uri, s_localName, (xmlNodePtr)attr))
        break;
    }
    attr = attr->next;
  }
  if (uri != NULL)
    xmljReleaseStringChars(env, uri, s_uri);
  xmljReleaseStringChars(env, localName, s_localName);
  return (attr != NULL);
}

/* -- GnomeEntity -- */

/*
 * Class:     gnu_xml_libxmlj_dom_GnomeEntity
 * Method:    getPublicId
 * Signature: ()Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL
Java_gnu_xml_libxmlj_dom_GnomeEntity_getPublicId (JNIEnv *env,
    jobject self)
{
  /* TODO */
  return NULL;
}

/*
 * Class:     gnu_xml_libxmlj_dom_GnomeEntity
 * Method:    getSystemId
 * Signature: ()Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL
Java_gnu_xml_libxmlj_dom_GnomeEntity_getSystemId (JNIEnv *env,
    jobject self)
{
  /* TODO */
  return NULL;
}

/*
 * Class:     gnu_xml_libxmlj_dom_GnomeEntity
 * Method:    getNotationName
 * Signature: ()Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL
Java_gnu_xml_libxmlj_dom_GnomeEntity_getNotationName (JNIEnv *env,
    jobject self)
{
  /* TODO */
  return NULL;
}

/* -- GnomeNamedNodeMap -- */

/*
 * Class:     gnu_xml_libxmlj_dom_GnomeNamedNodeMap
 * Method:    getNamedItem
 * Signature: (Ljava/lang/String;)Lorg/w3c/dom/Node;
 */
JNIEXPORT jobject JNICALL
Java_gnu_xml_libxmlj_dom_GnomeNamedNodeMap_getNamedItem (JNIEnv *env,
    jobject self,
    jstring name)
{
  xmlAttrPtr attr;

  attr = getNamedItem(env, self, name);
  return xmljGetNodeInstance(env, (xmlNodePtr)attr);
}

/*
 * Class:     gnu_xml_libxmlj_dom_GnomeNamedNodeMap
 * Method:    setNamedItem
 * Signature: (Lorg/w3c/dom/Node;)Lorg/w3c/dom/Node;
 */
JNIEXPORT jobject JNICALL
Java_gnu_xml_libxmlj_dom_GnomeNamedNodeMap_setNamedItem (JNIEnv *env,
    jobject self,
    jobject arg)
{
  xmlNodePtr node;
  xmlNodePtr argNode;

  node = xmljGetNodeID(env, self);
  argNode = xmljGetNodeID(env, arg);
  
  if (argNode->doc != node->doc)
    xmljThrowDOMException(env, 4, NULL); /* WRONG_DOCUMENT_ERR */
  if (argNode->parent != NULL)
    xmljThrowDOMException(env, 10, NULL); /* INUSE_ATTRIBUTE_ERR */
  if (!xmlAddChild(node, argNode))
    xmljThrowDOMException(env, 3, NULL); /* HIERARCHY_REQUEST_ERR */
  return arg;
}

/*
 * Class:     gnu_xml_libxmlj_dom_GnomeNamedNodeMap
 * Method:    removeNamedItem
 * Signature: (Ljava/lang/String;)Lorg/w3c/dom/Node;
 */
JNIEXPORT jobject JNICALL
Java_gnu_xml_libxmlj_dom_GnomeNamedNodeMap_removeNamedItem (JNIEnv *env,
    jobject self,
    jstring name)
{
  xmlAttrPtr attr;
  
  attr = getNamedItem(env, self, name);
  if (attr == NULL)
  {
    xmljThrowDOMException(env, 8, NULL); /* NOT_FOUND_ERR */
    return NULL;
  }
  else
  {
    xmlUnlinkNode((xmlNodePtr)attr);
    return xmljGetNodeInstance(env, (xmlNodePtr)attr);
  }
}

/*
 * Class:     gnu_xml_libxmlj_dom_GnomeNamedNodeMap
 * Method:    item
 * Signature: (I)Lorg/w3c/dom/Node;
 */
JNIEXPORT jobject JNICALL
Java_gnu_xml_libxmlj_dom_GnomeNamedNodeMap_item (JNIEnv *env,
    jobject self,
    jint index)
{
  xmlNodePtr node;
  xmlAttrPtr attr;
  jint count;

  node = xmljGetNodeID(env, self);
  attr = node->properties;
  for (count = 0; attr != NULL && count < index; count++)
    attr = attr->next;
  if (attr == NULL)
    printf("No attribute at index %d\n", index);
  return xmljGetNodeInstance(env, (xmlNodePtr)attr);
}

/*
 * Class:     gnu_xml_libxmlj_dom_GnomeNamedNodeMap
 * Method:    getLength
 * Signature: ()I
 */
JNIEXPORT jint JNICALL
Java_gnu_xml_libxmlj_dom_GnomeNamedNodeMap_getLength (JNIEnv *env,
    jobject self)
{
  xmlNodePtr node;
  xmlAttrPtr attr;
  jint count;

  node = xmljGetNodeID(env, self);
  count = 0;
  attr = node->properties;
  while (attr != NULL)
  {
    count++;
    attr = attr->next;
  }
  return count;
}

/*
 * Class:     gnu_xml_libxmlj_dom_GnomeNamedNodeMap
 * Method:    getNamedItemNS
 * Signature: (Ljava/lang/String;Ljava/lang/String;)Lorg/w3c/dom/Node;
 */
JNIEXPORT jobject JNICALL
Java_gnu_xml_libxmlj_dom_GnomeNamedNodeMap_getNamedItemNS (JNIEnv *env,
    jobject self,
    jstring uri,
    jstring localName)
{
  xmlAttrPtr attr;

  attr = getNamedItemNS(env, self, uri, localName);
  return xmljGetNodeInstance(env, (xmlNodePtr)attr);
}

/*
 * Class:     gnu_xml_libxmlj_dom_GnomeNamedNodeMap
 * Method:    setNamedItemNS
 * Signature: (Lorg/w3c/dom/Node;)Lorg/w3c/dom/Node;
 */
JNIEXPORT jobject JNICALL
Java_gnu_xml_libxmlj_dom_GnomeNamedNodeMap_setNamedItemNS (JNIEnv *env,
    jobject self,
    jobject arg)
{
  return Java_gnu_xml_libxmlj_dom_GnomeNamedNodeMap_setNamedItem(env, self,
      arg);
}

/*
 * Class:     gnu_xml_libxmlj_dom_GnomeNamedNodeMap
 * Method:    removeNamedItemNS
 * Signature: (Ljava/lang/String;Ljava/lang/String;)Lorg/w3c/dom/Node;
 */
JNIEXPORT jobject JNICALL
Java_gnu_xml_libxmlj_dom_GnomeNamedNodeMap_removeNamedItemNS (JNIEnv *env,
    jobject self,
    jstring uri,
    jstring localName)
{
  xmlAttrPtr attr;

  attr = getNamedItemNS(env, self, uri, localName);
  if (attr == NULL)
  {
    throwDOMException(env, 8, NULL); /* NOT_FOUND_ERR */
    return NULL;
  }
  else
  {
    xmlUnlinkNode((xmlNodePtr)attr);
    return xmljGetNodeInstance(env, (xmlNodePtr)attr);
  }
}

/* -- GnomeNode -- */

/*
 * Class:     gnu_xml_libxmlj_dom_GnomeNode
 * Method:    getNodeName
 * Signature: ()Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL
Java_gnu_xml_libxmlj_dom_GnomeNode_getNodeName (JNIEnv *env,
    jobject self)
{
  xmlNodePtr node;

  node = xmljGetNodeID(env, self);
  return xmljNewString(env, node->name);
}

/*
 * Class:     gnu_xml_libxmlj_dom_GnomeNode
 * Method:    getNodeValue
 * Signature: ()Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL
Java_gnu_xml_libxmlj_dom_GnomeNode_getNodeValue (JNIEnv *env,
    jobject self)
{
  xmlNodePtr node;
  xmlBufferPtr buf;
  jstring ret;


  node = xmljGetNodeID(env, self);

  /* If not character data, return null */
  if (node->type != XML_TEXT_NODE &&
      node->type != XML_CDATA_SECTION_NODE && 
      node->type != XML_COMMENT_NODE)
    return NULL;

  buf = xmlBufferCreate();
  xmlNodeBufGetContent(buf, node);
  ret = xmljNewString(env, buf->content);
  xmlFree(buf);
  return ret;
}

/*
 * Class:     gnu_xml_libxmlj_dom_GnomeNode
 * Method:    setNodeValue
 * Signature: (Ljava/lang/String;)V
 */
JNIEXPORT void JNICALL
Java_gnu_xml_libxmlj_dom_GnomeNode_setNodeValue (JNIEnv *env,
    jobject self,
    jstring nodeValue)
{
  xmlNodePtr node;
  const xmlChar *s_nodeValue;

  node = xmljGetNodeID(env, self);

  /* If not character data, return */
  if (node->type != XML_TEXT_NODE &&
      node->type != XML_CDATA_SECTION_NODE && 
      node->type != XML_COMMENT_NODE)
    return;

  s_nodeValue = xmljGetStringChars(env, nodeValue);
  xmlNodeSetContent(node, s_nodeValue);
  xmljReleaseStringChars(env, nodeValue, s_nodeValue);
}

/*
 * Class:     gnu_xml_libxmlj_dom_GnomeNode
 * Method:    getNodeType
 * Signature: ()S
 */
JNIEXPORT jshort JNICALL
Java_gnu_xml_libxmlj_dom_GnomeNode_getNodeType (JNIEnv *env,
    jobject self)
{
  xmlNodePtr node;

  node = xmljGetNodeID(env, self);
  return node->type;
}

/*
 * Class:     gnu_xml_libxmlj_dom_GnomeNode
 * Method:    getParentNode
 * Signature: ()Lorg/w3c/dom/Node;
 */
JNIEXPORT jobject JNICALL
Java_gnu_xml_libxmlj_dom_GnomeNode_getParentNode (JNIEnv *env,
    jobject self)
{
  xmlNodePtr node;

  node = xmljGetNodeID(env, self);
  return xmljGetNodeInstance(env, node->parent);
}

/*
 * Class:     gnu_xml_libxmlj_dom_GnomeNode
 * Method:    getChildNodes
 * Signature: ()Lorg/w3c/dom/NodeList;
 */
JNIEXPORT jobject JNICALL
Java_gnu_xml_libxmlj_dom_GnomeNode_getChildNodes (JNIEnv *env,
    jobject self)
{
  xmlNodePtr node;
  jclass cls;
  jmethodID method;

  node = xmljGetNodeID(env, self);

  /* Construct node list object */
  cls = (*env)->FindClass(env, "gnu/xml/libxmlj/dom/GnomeNodeList");
  method = (*env)->GetMethodID(env, cls, "<init>", "(I)V");
  return (*env)->NewObject(env, cls, method, node);
}

/*
 * Class:     gnu_xml_libxmlj_dom_GnomeNode
 * Method:    getFirstChild
 * Signature: ()Lorg/w3c/dom/Node;
 */
JNIEXPORT jobject JNICALL
Java_gnu_xml_libxmlj_dom_GnomeNode_getFirstChild (JNIEnv *env,
    jobject self)
{
  xmlNodePtr node;

  node = xmljGetNodeID(env, self);
  return xmljGetNodeInstance(env, node->children);
}

/*
 * Class:     gnu_xml_libxmlj_dom_GnomeNode
 * Method:    getLastChild
 * Signature: ()Lorg/w3c/dom/Node;
 */
JNIEXPORT jobject JNICALL
Java_gnu_xml_libxmlj_dom_GnomeNode_getLastChild (JNIEnv *env,
    jobject self)
{
  xmlNodePtr node;

  node = xmljGetNodeID(env, self);
  return xmljGetNodeInstance(env, node->last);
}

/*
 * Class:     gnu_xml_libxmlj_dom_GnomeNode
 * Method:    getPreviousSibling
 * Signature: ()Lorg/w3c/dom/Node;
 */
JNIEXPORT jobject JNICALL
Java_gnu_xml_libxmlj_dom_GnomeNode_getPreviousSibling (JNIEnv *env,
    jobject self)
{
  xmlNodePtr node;

  node = xmljGetNodeID(env, self);
  return xmljGetNodeInstance(env, node->prev);
}

/*
 * Class:     gnu_xml_libxmlj_dom_GnomeNode
 * Method:    getNextSibling
 * Signature: ()Lorg/w3c/dom/Node;
 */
JNIEXPORT jobject JNICALL
Java_gnu_xml_libxmlj_dom_GnomeNode_getNextSibling (JNIEnv *env,
    jobject self)
{
  xmlNodePtr node;

  node = xmljGetNodeID(env, self);
  return xmljGetNodeInstance(env, node->next);
}

/*
 * Class:     gnu_xml_libxmlj_dom_GnomeNode
 * Method:    getAttributes
 * Signature: ()Lorg/w3c/dom/NamedNodeMap;
 */
JNIEXPORT jobject JNICALL
Java_gnu_xml_libxmlj_dom_GnomeNode_getAttributes (JNIEnv *env,
    jobject self)
{
  xmlNodePtr node;
  jclass cls;
  jmethodID method;

  node = xmljGetNodeID(env, self);

  /* Construct named node map object */
  cls = (*env)->FindClass(env, "gnu/xml/libxmlj/dom/GnomeNamedNodeMap");
  method = (*env)->GetMethodID(env, cls, "<init>", "(I)V");
  return (*env)->NewObject(env, cls, method, node);
}

/*
 * Class:     gnu_xml_libxmlj_dom_GnomeNode
 * Method:    getOwnerDocument
 * Signature: ()Lorg/w3c/dom/Document;
 */
JNIEXPORT jobject JNICALL
Java_gnu_xml_libxmlj_dom_GnomeNode_getOwnerDocument (JNIEnv *env,
    jobject self)
{
  xmlNodePtr node;

  node = xmljGetNodeID(env, self);
  return xmljGetNodeInstance(env, (xmlNodePtr)node->doc);
}

/*
 * Class:     gnu_xml_libxmlj_dom_GnomeNode
 * Method:    insertBefore
 * Signature: (Lorg/w3c/dom/Node;Lorg/w3c/dom/Node;)Lorg/w3c/dom/Node;
 */
JNIEXPORT jobject JNICALL
Java_gnu_xml_libxmlj_dom_GnomeNode_insertBefore (JNIEnv *env,
    jobject self,
    jobject newChild,
    jobject refChild)
{
  xmlNodePtr newChildNode;
  xmlNodePtr refChildNode;

  newChildNode = xmljGetNodeID(env, newChild);
  refChildNode = xmljGetNodeID(env, refChild);

  if (newChildNode->doc != refChildNode->doc)
    throwDOMException(env, 4, NULL); /* WRONG_DOCUMENT_ERR */
  if (!xmlAddPrevSibling(refChildNode, newChildNode))
    throwDOMException(env, 3, NULL); /* HIERARCHY_REQUEST_ERR */
  return newChild;
}

/*
 * Class:     gnu_xml_libxmlj_dom_GnomeNode
 * Method:    replaceChild
 * Signature: (Lorg/w3c/dom/Node;Lorg/w3c/dom/Node;)Lorg/w3c/dom/Node;
 */
JNIEXPORT jobject JNICALL
Java_gnu_xml_libxmlj_dom_GnomeNode_replaceChild (JNIEnv *env,
    jobject self,
    jobject newChild,
    jobject oldChild)
{
  xmlNodePtr newChildNode;
  xmlNodePtr oldChildNode;

  newChildNode = xmljGetNodeID(env, newChild);
  oldChildNode = xmljGetNodeID(env, oldChild);
  
  if (newChildNode->doc != oldChildNode->doc)
    throwDOMException(env, 4, NULL); /* WRONG_DOCUMENT_ERR */
  if (!xmlReplaceNode(oldChildNode, newChildNode))
    throwDOMException(env, 3, NULL); /* HIERARCHY_REQUEST_ERR */
  return newChild;
}

/*
 * Class:     gnu_xml_libxmlj_dom_GnomeNode
 * Method:    removeChild
 * Signature: (Lorg/w3c/dom/Node;)Lorg/w3c/dom/Node;
 */
JNIEXPORT jobject JNICALL
Java_gnu_xml_libxmlj_dom_GnomeNode_removeChild (JNIEnv *env,
    jobject self,
    jobject oldChild)
{
  xmlNodePtr node;
  xmlNodePtr oldChildNode;

  node = xmljGetNodeID(env, self);
  oldChildNode = xmljGetNodeID(env, oldChild);

  if (oldChildNode->parent != node)
    throwDOMException(env, 8, NULL); /* NOT_FOUND_ERR */
  xmlUnlinkNode(oldChildNode);
  return oldChild;
}

/*
 * Class:     gnu_xml_libxmlj_dom_GnomeNode
 * Method:    appendChild
 * Signature: (Lorg/w3c/dom/Node;)Lorg/w3c/dom/Node;
 */
JNIEXPORT jobject JNICALL
Java_gnu_xml_libxmlj_dom_GnomeNode_appendChild (JNIEnv *env,
    jobject self,
    jobject newChild)
{
  xmlNodePtr node;
  xmlNodePtr newChildNode;

  node = xmljGetNodeID(env, self);
  newChildNode = xmljGetNodeID(env, newChild);

  if (newChildNode->doc != node->doc)
    throwDOMException(env, 4, NULL); /* WRONG_DOCUMENT_ERR */
  if (!xmlAddChild(node, newChildNode))
    throwDOMException(env, 3, NULL); /* HIERARCHY_REQUEST_ERR */
  return newChild;
}

/*
 * Class:     gnu_xml_libxmlj_dom_GnomeNode
 * Method:    hasChildNodes
 * Signature: ()Z
 */
JNIEXPORT jboolean JNICALL
Java_gnu_xml_libxmlj_dom_GnomeNode_hasChildNodes (JNIEnv *env,
    jobject self)
{
  xmlNodePtr node;

  node = xmljGetNodeID(env, self);
  return (node->children != NULL);
}

/*
 * Class:     gnu_xml_libxmlj_dom_GnomeNode
 * Method:    cloneNode
 * Signature: (Z)Lorg/w3c/dom/Node;
 */
JNIEXPORT jobject JNICALL
Java_gnu_xml_libxmlj_dom_GnomeNode_cloneNode (JNIEnv *env,
    jobject self,
    jboolean deep)
{
  /* TODO */
  return NULL;
}

/*
 * Class:     gnu_xml_libxmlj_dom_GnomeNode
 * Method:    normalize
 * Signature: ()V
 */
JNIEXPORT void JNICALL
Java_gnu_xml_libxmlj_dom_GnomeNode_normalize (JNIEnv *env,
    jobject self)
{
  /* TODO */
}

/*
 * Class:     gnu_xml_libxmlj_dom_GnomeNode
 * Method:    isSupported
 * Signature: (Ljava/lang/String;Ljava/lang/String;)Z
 */
JNIEXPORT jboolean JNICALL
Java_gnu_xml_libxmlj_dom_GnomeNode_isSupported (JNIEnv *env,
    jobject self,
    jstring feature,
    jstring version)
{
  /* TODO */
  return 0;
}

/*
 * Class:     gnu_xml_libxmlj_dom_GnomeNode
 * Method:    getNamespaceURI
 * Signature: ()Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL
Java_gnu_xml_libxmlj_dom_GnomeNode_getNamespaceURI (JNIEnv *env,
    jobject self)
{
  xmlNodePtr node;

  node = xmljGetNodeID(env, self);
  if (node->ns == NULL)
    return NULL;
  else
    return xmljNewString(env, node->ns->href);	
}

/*
 * Class:     gnu_xml_libxmlj_dom_GnomeNode
 * Method:    getPrefix
 * Signature: ()Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL
Java_gnu_xml_libxmlj_dom_GnomeNode_getPrefix (JNIEnv *env,
    jobject self)
{
  xmlNodePtr node;

  node = xmljGetNodeID(env, self);
  if (node->ns == NULL)
    return NULL;
  else
    return xmljNewString(env, node->ns->prefix);	
}

/*
 * Class:     gnu_xml_libxmlj_dom_GnomeNode
 * Method:    setPrefix
 * Signature: (Ljava/lang/String;)V
 */
JNIEXPORT void JNICALL
Java_gnu_xml_libxmlj_dom_GnomeNode_setPrefix (JNIEnv *env,
    jobject self,
    jstring prefix)
{
  xmlNodePtr node;
  const xmlChar *s_prefix;
  
  s_prefix = xmljGetStringChars(env, prefix);
  if (xmlValidateName(s_prefix, 0))
    throwDOMException(env, 5, NULL); /* INVALID_CHARACTER_ERR */
  node = xmljGetNodeID(env, self);
  if (node->ns == NULL)
    throwDOMException(env, 14, NULL); /* NAMESPACE_ERR */
  node->ns->prefix = s_prefix;
  xmljReleaseStringChars(env, prefix, s_prefix);
}

/*
 * Class:     gnu_xml_libxmlj_dom_GnomeNode
 * Method:    getLocalName
 * Signature: ()Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL
Java_gnu_xml_libxmlj_dom_GnomeNode_getLocalName (JNIEnv *env,
    jobject self)
{
  xmlNodePtr node;

  node = xmljGetNodeID(env, self);
  if (node->name == NULL)
    return NULL;
  else
    return xmljNewString(env, node->name);
}

/*
 * Class:     gnu_xml_libxmlj_dom_GnomeNode
 * Method:    hasAttributes
 * Signature: ()Z
 */
JNIEXPORT jboolean JNICALL
Java_gnu_xml_libxmlj_dom_GnomeNode_hasAttributes (JNIEnv *env,
    jobject self)
{
  xmlNodePtr node;

  node = xmljGetNodeID(env, self);
  return (node->properties != NULL);
}

/* -- GnomeNodeList -- */

/*
 * Class:     gnu_xml_libxmlj_dom_GnomeNodeList
 * Method:    item
 * Signature: (I)Lorg/w3c/dom/Node;
 */
JNIEXPORT jobject JNICALL
Java_gnu_xml_libxmlj_dom_GnomeNodeList_item (JNIEnv *env,
    jobject self,
    jint index)
{
  xmlNodePtr node;
  jint count;

  node = xmljGetNodeID(env, self);
  node = node->children;
  count = 0;
  for (count = 0; node != NULL && count < index; count++)
    node = node->next;
  return xmljGetNodeInstance(env, node);
}

/*
 * Class:     gnu_xml_libxmlj_dom_GnomeNodeList
 * Method:    getLength
 * Signature: ()I
 */
JNIEXPORT jint JNICALL
Java_gnu_xml_libxmlj_dom_GnomeNodeList_getLength (JNIEnv *env,
    jobject self)
{
  xmlNodePtr node;
  jint count;

  node = xmljGetNodeID(env, self);
  count = 0;
  node = node->children;
  while (node != NULL)
  {
    count++;
    node = node->next;
  }
  return count;
}

/* -- GnomeNotation -- */

/*
 * Class:     gnu_xml_libxmlj_dom_GnomeNotation
 * Method:    getPublicId
 * Signature: ()Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL
Java_gnu_xml_libxmlj_dom_GnomeNotation_getPublicId (JNIEnv *env,
    jobject self)
{
  xmlNotationPtr notation;

  notation = (xmlNotationPtr)xmljGetNodeID(env, self);
  if (notation->PublicID == NULL)
    return NULL;
  else
    return xmljNewString(env, notation->PublicID);
}

/*
 * Class:     gnu_xml_libxmlj_dom_GnomeNotation
 * Method:    getSystemId
 * Signature: ()Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL
Java_gnu_xml_libxmlj_dom_GnomeNotation_getSystemId (JNIEnv *env,
    jobject self)
{
  xmlNotationPtr notation;

  notation = (xmlNotationPtr)xmljGetNodeID(env, self);
  if (notation->SystemID == NULL)
    return NULL;
  else
    return xmljNewString(env, notation->SystemID);
}

/* -- GnomeProcessingInstruction -- */

/*
 * Class:     gnu_xml_libxmlj_dom_GnomeProcessingInstruction
 * Method:    getData
 * Signature: ()Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL
Java_gnu_xml_libxmlj_dom_GnomeProcessingInstruction_getData (JNIEnv *env,
    jobject self)
{
  xmlNodePtr node;
  xmlBufferPtr buf;
  jstring ret;

  node = xmljGetNodeID(env, self);
  buf = xmlBufferCreate();
  xmlNodeBufGetContent(buf, node);
  ret = xmljNewString(env, buf->content);
  xmlFree(buf);
  return ret;
}

/*
 * Class:     gnu_xml_libxmlj_dom_GnomeProcessingInstruction
 * Method:    setData
 * Signature: (Ljava/lang/String;)V
 */
JNIEXPORT void JNICALL
Java_gnu_xml_libxmlj_dom_GnomeProcessingInstruction_setData (JNIEnv *env,
    jobject self,
    jstring data)
{
  xmlNodePtr node;
  const xmlChar *s_data;

  node = xmljGetNodeID(env, self);
  s_data = xmljGetStringChars(env, data);
  xmlNodeSetContent(node, s_data);
  xmljReleaseStringChars(env, data, s_data);
}

/* -- Utility -- */

/*
 * Create GnomeDocument object from the given xmlDocPtr
 */
jobject
createDocument (JNIEnv *env,
    jobject self,
    xmlDocPtr doc)
{
  jclass cls;
  jmethodID method;
  jfieldID field;
  jobject ret;
  
  /* Construct document object */
  cls = (*env)->FindClass(env, "gnu/xml/libxmlj/dom/GnomeDocument");
  method = (*env)->GetMethodID(env, cls, "<init>", "(I)V");
  ret = (*env)->NewObject(env, cls, method, doc);

  /* Set DOM implementation field */
  field = (*env)->GetFieldID(env, cls, "dom",
      "Lorg/w3c/dom/DOMImplementation;");
  (*env)->SetObjectField(env, ret, field, self);
  return ret;
}

/*
 * Get parser context from GnomeDocumentBuilder field
 */
xmlParserCtxtPtr
getContext (JNIEnv *env,
    jobject self)
{
  xmlParserCtxtPtr ret;
  jclass cls;
  jfieldID field;
  
  cls = (*env)->GetObjectClass(env, self);
  field = (*env)->GetFieldID(env, cls, "context", "I");
  ret = (xmlParserCtxtPtr)(*env)->GetIntField(env, self, field);
  return ret;
}

/* -- Callback functions -- */

xmlParserInputPtr
customResolveEntity (void *ctx,
    const xmlChar *publicId,
    const xmlChar *systemId)
{
  /* TODO */
  return defaultResolveEntity(ctx, publicId, systemId);
}

void
customWarning (void *ctx,
    const char *msg,
    ...)
{
  va_list args;
  
  va_start(args, msg);
  dispatchLogCallback(msg, "warning");
  va_end(args);
}

void
customError (void *ctx,
    const char *msg,
    ...)
{
  va_list args;
  
  va_start(args, msg);
  dispatchLogCallback(msg, "error");
  va_end(args);
}

void
customFatalError (void *ctx,
    const char *msg,
    ...)
{
  va_list args;
  
  va_start(args, msg);
  dispatchLogCallback(msg, "fatalError");
  va_end(args);
}

void
dispatchLogCallback(const char *msg,
    const char *methodName)
{
  xmlParserCtxtPtr context;
  
  jclass cls;
  jmethodID method;
  
  jstring j_msg;
  jstring j_publicId;
  jstring j_systemId;
  jint lineNumber;
  jint columnNumber;

  context = getContext(dom_cb_env, dom_cb_obj);
  
  /* Get the logging method to invoke */
  cls = (*dom_cb_env)->GetObjectClass(dom_cb_env, dom_cb_obj);
  method = (*dom_cb_env)->GetMethodID(dom_cb_env, cls,
      methodName,
      "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;II)V");
  if (method == NULL)
    return;
  
  /* Prepare arguments */
  j_msg = (context->lastError.message == NULL) ? NULL :
    xmljNewString(dom_cb_env, context->lastError.message);
  if (j_msg == NULL)
    j_msg = (msg == NULL) ? NULL :
      xmljNewString(dom_cb_env, msg);
  j_publicId = NULL;
  j_systemId = (context->lastError.file == NULL) ? NULL :
    xmljNewString(dom_cb_env, context->lastError.file);
  lineNumber = (jint)context->lastError.line;
  columnNumber = -1;

  /* Invoke the method */
  (*dom_cb_env)->CallVoidMethod(dom_cb_env, dom_cb_obj, method,
                                  j_msg, j_publicId, j_systemId,
                                  lineNumber, columnNumber);
}

xmlAttrPtr
getNamedItem (JNIEnv *env,
    jobject self,
    jstring name)
{
  xmlNodePtr node;
  xmlAttrPtr attr;
  const xmlChar *s_name;

  s_name = xmljGetStringChars(env, name);

  node = xmljGetNodeID(env, self);
  attr = node->properties;
  while (attr != NULL)
  {
    if (xmljMatch(s_name, (xmlNodePtr)attr))
      break;
    attr = attr->next;
  }

  xmljReleaseStringChars(env, name, s_name);
  return attr;
}

xmlAttrPtr
getNamedItemNS (JNIEnv *env,
    jobject self,
    jstring uri,
    jstring localName)
{
  xmlNodePtr node;
  xmlAttrPtr attr;
  const xmlChar *s_uri;
  const xmlChar *s_localName;

  s_uri = xmljGetStringChars(env, uri);
  s_localName = xmljGetStringChars(env, localName);

  node = xmljGetNodeID(env, self);
  attr = node->properties;
  while (attr != NULL)
  {
    if (xmljMatchNS(s_uri, s_localName, (xmlNodePtr)attr))
        break;
    attr = attr->next;
  }

  xmljReleaseStringChars(env, uri, s_uri);
  xmljReleaseStringChars(env, localName, s_localName);
  return attr;
}

