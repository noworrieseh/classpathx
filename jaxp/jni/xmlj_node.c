/*
 * xmlj_node.c
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
#include "xmlj_node.h"
#include "xmlj_util.h"
#include <libxml/xmlstring.h>

/*
 * Returns the node ID for the given GnomeNode object.
 */
xmlNodePtr
xmljGetNodeID (JNIEnv *env,
	jobject self)
{
    jclass cls;
    jfieldID field;
    xmlNodePtr node;

	cls = (*env)->GetObjectClass(env, self);
    field = (*env)->GetFieldID(env, cls, "id", "I");
    node = (xmlNodePtr)(*env)->GetIntField(env, self, field);
    if (node == NULL)
      xmljThrowDOMException(env, 8, NULL); /* NOT_FOUND_ERR */
    return node;
}

/*
 * Returns the Java node instanced corresponding to the specified node ID.
 * TODO cache the resulting instances
 */
jobject
xmljGetNodeInstance (JNIEnv *env,
	xmlNodePtr node)
{
	jclass cls;
	jmethodID method;

	if (node == NULL)
		return NULL;

	/* This method just constructs a new object of the given type */
	cls = (*env)->FindClass(env, xmljNodeClass(node->type));
    method = (*env)->GetMethodID(env, cls, "<init>", "(I)V");
    return (*env)->NewObject(env, cls, method, node);
}

/*
 * Returns the Java class used to instantiate the given node type.
 */
char *
xmljNodeClass (xmlElementType type)
{
	switch (type)
	{
	case XML_ELEMENT_NODE:
		return "gnu/xml/libxmlj/dom/GnomeElement";
	case XML_ATTRIBUTE_NODE:
		return "gnu/xml/libxmlj/dom/GnomeAttr";
	case XML_TEXT_NODE:
		return "gnu/xml/libxmlj/dom/GnomeText";
	case XML_CDATA_SECTION_NODE:
		return "gnu/xml/libxmlj/dom/GnomeCDATASection";
	case XML_ENTITY_REF_NODE:
		return "gnu/xml/libxmlj/dom/GnomeEntityReference";
	case XML_ENTITY_NODE:
		return "gnu/xml/libxmlj/dom/GnomeEntity";
	case XML_PI_NODE:
		return "gnu/xml/libxmlj/dom/GnomeProcessingInstruction";
	case XML_COMMENT_NODE:
		return "gnu/xml/libxmlj/dom/GnomeComment";
	case XML_DOCUMENT_NODE:
		return "gnu/xml/libxmlj/dom/GnomeDocument";
	case XML_DOCUMENT_TYPE_NODE:
		return "gnu/xml/libxmlj/dom/GnomeDocumentType";
	case XML_DOCUMENT_FRAG_NODE:
		return "gnu/xml/libxmlj/dom/GnomeDocumentFragment";
	default:
        printf("warning: unhandled element type: %d\n", type);
		return "gnu/xml/libxmlj/dom/GnomeNode";
	}	
}

int
xmljMatch (const xmlChar *name,
    xmlNodePtr node)
{
  xmlNsPtr ns;
  xmlChar *qName;
  int ret;
  
  ns = node->ns;
  if (ns == NULL || ns->prefix == NULL)
    qName = xmlStrdup(node->name);
  else
  {
    qName = "";
    xmlStrcat(qName, ns->prefix);
    xmlStrcat(qName, ":");
    xmlStrcat(qName, node->name);
  }
  ret = xmlStrcmp(name, qName);
  free(qName);
  return ret;
}

int
xmljMatchNS (const xmlChar *uri,
    const xmlChar *localName,
    xmlNodePtr node)
{
  xmlNsPtr ns;

  ns = node->ns;
  if (ns == NULL || ns->href == NULL)
  {
    if (uri != NULL)
      return 0;
    return xmlStrcmp(localName, node->name);
  }
  else
  {
    if (uri == NULL)
      return 0;
    return (xmlStrcmp(localName, node->name) &&
        xmlStrcmp(uri, ns->href));
  }
}

