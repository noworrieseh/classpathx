/* * xmlj_sax.c
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

#include "xmlj_sax.h"
#include "xmlj_io.h"
#include "xmlj_util.h"
#include <unistd.h>

static xmlSAXHandler handler = {
  (internalSubsetSAXFunc)xmljSAXInternalSubset,
  (isStandaloneSAXFunc)xmljSAXIsStandalone,
  (hasInternalSubsetSAXFunc)xmljSAXHasInternalSubset,
  (hasExternalSubsetSAXFunc)xmljSAXHasExternalSubset,
  (resolveEntitySAXFunc)xmljSAXResolveEntity,
  (getEntitySAXFunc)xmljSAXGetEntity,
  (entityDeclSAXFunc)xmljSAXEntityDecl,
  (notationDeclSAXFunc)xmljSAXNotationDecl,
  (attributeDeclSAXFunc)xmljSAXAttributeDecl,
  (elementDeclSAXFunc)xmljSAXElementDecl,
  (unparsedEntityDeclSAXFunc)xmljSAXUnparsedEntityDecl,
  (setDocumentLocatorSAXFunc)xmljSAXSetDocumentLocator,
  (startDocumentSAXFunc)xmljSAXStartDocument,
  (endDocumentSAXFunc)xmljSAXEndDocument,
  (startElementSAXFunc)xmljSAXStartElement,
  (endElementSAXFunc)xmljSAXEndElement,
  (referenceSAXFunc)xmljSAXReference,
  (charactersSAXFunc)xmljSAXCharacters,
  (ignorableWhitespaceSAXFunc)xmljSAXIgnorableWhitespace,
  (processingInstructionSAXFunc)xmljSAXProcessingInstruction,
  (commentSAXFunc)xmljSAXComment,
  (warningSAXFunc)xmljSAXWarning,
  (errorSAXFunc)xmljSAXError,
  (fatalErrorSAXFunc)xmljSAXFatalError,
};

typedef struct _SAXParseContext
{
  
  JNIEnv *env; /* Current JNI environment */
  jobject obj; /* The gnu.xml.libxmlj.sax.GnomeXmlReader instance */
  xmlParserCtxtPtr ctx; /* libxml2 parser context */
  
}
SAXParseContext;

void xmljInitSAXHandler(xmlSAXHandler *sax);

SAXParseContext *
xmljNewSAXParseContext(JNIEnv *env,
    jobject obj,
    xmlParserCtxtPtr ctx)
{
  SAXParseContext *ret;

  ret = (SAXParseContext *)malloc(sizeof(SAXParseContext));
  ret->env = env;
  ret->obj = obj;
  ret->ctx = ctx;
  return ret;
}

void
xmljFreeSAXParseContext(SAXParseContext *saxCtx)
{
  free(saxCtx);
}

/* -- GnomeLocator -- */

JNIEXPORT jstring JNICALL
Java_gnu_xml_libxmlj_sax_GnomeLocator_getPublicId (JNIEnv * env,
	jobject self)
{
	xmlSAXLocatorPtr locator;
	jclass cls;
	jfieldID field;
	const xmlChar *ret;
	jstring j_ret;

	/* Get the locator ID */
	cls = (*env)->GetObjectClass(env, self);
	field = (*env)->GetFieldID(env, cls, "id", "I");
	locator = (xmlSAXLocatorPtr)(*env)->GetIntField(env, self, field);

	/*ret = locator->getPublicId();*/
    ret = NULL; /* FIXME */
	j_ret = xmljNewString(env, ret);
	return j_ret;
}

JNIEXPORT jstring JNICALL
Java_gnu_xml_libxmlj_sax_GnomeLocator_getSystemId (JNIEnv * env,
	jobject self)
{
	xmlSAXLocatorPtr locator;
	jclass cls;
	jfieldID field;
	const xmlChar *ret;
	jstring j_ret;

	/* Get the locator ID */
	cls = (*env)->GetObjectClass(env, self);
	field = (*env)->GetFieldID(env, cls, "id", "I");
	locator = (xmlSAXLocatorPtr)(*env)->GetIntField(env, self, field);

	/*ret = locator->getSystemId();*/
    ret = NULL; /* FIXME */
	j_ret = xmljNewString(env, ret);
	return j_ret;
}

JNIEXPORT jint JNICALL
Java_gnu_xml_libxmlj_sax_GnomeLocator_getLineNumber (JNIEnv *env,
	jobject self)
{
	xmlSAXLocatorPtr locator;
	jclass cls;
	jfieldID field;

	/* Get the locator ID */
	cls = (*env)->GetObjectClass(env, self);
	field = (*env)->GetFieldID(env, cls, "id", "I");
	locator = (xmlSAXLocatorPtr)(*env)->GetObjectField(env, self, field);

	/*return locator->getLineNumber();*/
    return -1; /* FIXME */
}

JNIEXPORT jint JNICALL
Java_gnu_xml_libxmlj_sax_GnomeLocator_getColumnNumber (JNIEnv *env,
	jobject self)
{
	xmlSAXLocatorPtr locator;
	jclass cls;
	jfieldID field;

	/* Get the locator ID */
	cls = (*env)->GetObjectClass(env, self);
	field = (*env)->GetFieldID(env, cls, "id", "I");
	locator = (xmlSAXLocatorPtr)(*env)->GetObjectField(env, self, field);

	/*return locator->getColumnNumber();*/
    return -1; /* FIXME */
}

/* -- GnomeXMLReader -- */

/*JNIEXPORT jint JNICALL
Java_gnu_xml_libxmlj_sax_GnomeXMLReader_createContext (JNIEnv *env,
		jobject self)
{
	return (jint)xmlCreateIOParserCtxt(&sax_parser, NULL, NULL, NULL, NULL,
		XML_CHAR_ENCODING_UTF8);
}*/

/*JNIEXPORT jint JNICALL
Java_gnu_xml_libxmlj_sax_GnomeXMLReader_clearContext (JNIEnv *env,
		jobject self,
		jint context)
{
  xmlClearParserCtxt((xmlParserCtxtPtr)context);
  return 0;
}*/

JNIEXPORT jint JNICALL
Java_gnu_xml_libxmlj_sax_GnomeXMLReader_getFeature (JNIEnv *env,
		jobject self,
		jint context,
		jstring name)
{
	jint ret;
	const char *s_name;
	void *result;

	s_name = (*env)->GetStringUTFChars(env, name, 0);
    result = NULL; /* TODO */
	ret = xmlGetFeature((xmlParserCtxtPtr)context, s_name, result);
	(*env)->ReleaseStringUTFChars(env, name, s_name);
	if (ret == -1)
		return ret;
	else
		return 0; /* TODO */
}

JNIEXPORT jint JNICALL
Java_gnu_xml_libxmlj_sax_GnomeXMLReader_setFeature (JNIEnv *env,
		jobject self,
		jint context,
		jstring name,
		jint value)
{
	jint ret;
	const char *s_name;
	void *p_value;

	s_name = (*env)->GetStringUTFChars(env, name, 0);
	p_value = &value;
	ret = xmlSetFeature((xmlParserCtxtPtr)context, s_name, p_value);
	(*env)->ReleaseStringUTFChars(env, name, s_name);
	return ret;
}

JNIEXPORT void JNICALL
Java_gnu_xml_libxmlj_sax_GnomeXMLReader_parseStream (JNIEnv *env,
    jobject self,
    jobject in,
    jstring publicId,
    jstring systemId,
    jboolean validate)
{
  xmlParserCtxtPtr ctx;
  SAXParseContext *saxCtx;
  
  ctx = xmljEstablishParserContext(env, in, systemId, publicId,
     validate, 0, 0, NULL, NULL, 0);
  if (ctx != NULL)
  {
    saxCtx = xmljNewSAXParseContext(env, self, ctx);
    if (saxCtx != NULL)
    {
      ctx->_private = saxCtx;
      ctx->userData = ctx;
      /*xmljInitSAXHandler(ctx->sax);*/
      ctx->sax = &handler;
      xmlParseDocument(ctx);
      xmljFreeSAXParseContext(saxCtx);
      return;
    }
    else
      xmljReleaseParserContext(ctx);
  }
  if (!(*env)->ExceptionOccurred(env))
  {
    xmljThrowException(env, "java/io/IOException",
        "Unable to create parser context");
  }
}

/* Causes NullPointerException. Why? */
void
xmljInitSAXHandler (xmlSAXHandler *sax)
{
  sax->internalSubset = &xmljSAXInternalSubset;
  sax->isStandalone = &xmljSAXIsStandalone;
  sax->hasInternalSubset = &xmljSAXHasInternalSubset;
  sax->hasExternalSubset = &xmljSAXHasExternalSubset;
  sax->resolveEntity = &xmljSAXResolveEntity;
  sax->getEntity = &xmljSAXGetEntity;
  sax->entityDecl = &xmljSAXEntityDecl;
  sax->notationDecl = &xmljSAXNotationDecl;
  sax->attributeDecl = &xmljSAXAttributeDecl;
  sax->elementDecl = &xmljSAXElementDecl;
  sax->unparsedEntityDecl = &xmljSAXUnparsedEntityDecl;
  sax->setDocumentLocator = &xmljSAXSetDocumentLocator;
  sax->startDocument = &xmljSAXStartDocument;
  sax->endDocument = &xmljSAXEndDocument;
  sax->startElement = &xmljSAXStartElement;
  sax->endElement = &xmljSAXEndElement;
  sax->reference = &xmljSAXReference;
  sax->characters = &xmljSAXCharacters;
  sax->ignorableWhitespace = &xmljSAXIgnorableWhitespace;
  sax->processingInstruction = &xmljSAXProcessingInstruction;
  sax->comment = &xmljSAXComment;
  sax->warning = &xmljSAXWarning;
  sax->error = &xmljSAXError;
  sax->fatalError = &xmljSAXFatalError;
}

/*
 * Class:     gnu_xml_libxmlj_sax_GnomeXMLReader
 * Method:    parseFile
 * Signature: (ILjava/lang/String;)V
 *
JNIEXPORT jint JNICALL
Java_gnu_xml_libxmlj_sax_GnomeXMLReader_parseFile (JNIEnv *env,
		jobject self,
		jint context,
		jstring filename)
{
	const char *s_filename;
	jint ret;

	sax_cb_env = env;
	s_filename = (*env)->GetStringUTFChars(env, filename, 0);
	ret = xmlSAXUserParseFile(&sax_parser, self, s_filename);
	(*env)->ReleaseStringUTFChars(env, filename, s_filename);
	return ret;
}*/

/* -- Callback functions -- */

void
xmljSAXInternalSubset(void *vctx,
    const xmlChar *name,
    const xmlChar *publicId,
    const xmlChar *systemId)
{
  xmlParserCtxtPtr ctx;
  SAXParseContext *sax;
  
  JNIEnv *env;
  jobject target;
  jclass cls;
  jmethodID method;
  
  jstring j_name;
  jstring j_publicId;
  jstring j_systemId;

  ctx = (xmlParserCtxtPtr)vctx;
  sax = (SAXParseContext *)ctx->_private;
  env = sax->env;
  target = sax->obj;

  xmljCheckWellFormed(ctx);
  
  cls = (*env)->GetObjectClass(env, target);
  
  /* Get the startDTD method */
  method = (*env)->GetMethodID(env, cls, "startDTD",
      "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V");
  
  j_name = xmljNewString(env, name);
  j_publicId = xmljNewString(env, publicId);
  j_systemId = xmljNewString(env, systemId);
  
  /* Invoke the method */
  (*env)->CallVoidMethod(env, target, method, j_name,
                         j_publicId, j_systemId);
}

int
xmljSAXIsStandalone(void *vctx)
{
  /* TODO */
  return 0;
}

int
xmljSAXHasInternalSubset(void *vctx)
{
  /* TODO */
  return 0;
}

int
xmljSAXHasExternalSubset(void *vctx)
{
  /* TODO */
  return 0;
}
  
xmlParserInputPtr
xmljSAXResolveEntity (void *vctx,
    const xmlChar *publicId,
    const xmlChar *systemId)
{
  /* TODO */
  return NULL;
}

xmlEntityPtr
xmljSAXGetEntity(void *vctx,
    const xmlChar *name)
{
  /* TODO */
  return NULL;
}

void
xmljSAXEntityDecl(void *vctx,
    const xmlChar *name,
    int type,
    const xmlChar *publicId,
    const xmlChar *systemId,
    xmlChar *content)
{
  xmlParserCtxtPtr ctx;
  SAXParseContext *sax;
  
  JNIEnv *env;
  jobject target;
  jclass cls;
  jmethodID method;
  
  jstring j_name;
  jstring j_publicId;
  jstring j_systemId;
  jstring j_value;

  ctx = (xmlParserCtxtPtr)vctx;
  sax = (SAXParseContext *)ctx->_private;
  env = sax->env;
  target = sax->obj;

  xmljCheckWellFormed(ctx);
  
  cls = (*env)->GetObjectClass(env, target);
  
  /* Get the method */
  j_name = xmljNewString(env, name);
  switch (type)
  {
    case XML_INTERNAL_GENERAL_ENTITY:
    case XML_INTERNAL_PARAMETER_ENTITY:
    case XML_INTERNAL_PREDEFINED_ENTITY:
      method = (*env)->GetMethodID(env, cls, "internalEntityDecl",
          "(Ljava/lang/String;Ljava/lang/String;)V");
      j_value = xmljNewString(env, content);
      (*env)->CallVoidMethod(env, target, method, j_name, j_value);
      break;
    default:
      method = (*env)->GetMethodID(env, cls, "externalEntityDecl",
          "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V");
      j_publicId = xmljNewString(env, publicId);
      j_systemId = xmljNewString(env, systemId);
      (*env)->CallVoidMethod(env, target, method, j_name,
                             j_publicId, j_systemId);
  }
}

void
xmljSAXNotationDecl (void *vctx, 
    const xmlChar *name, 
    const xmlChar *publicId, 
    const xmlChar *systemId)
{
  xmlParserCtxtPtr ctx;
  SAXParseContext *sax;
  
  JNIEnv *env;
  jobject target;
  jclass cls;
  jmethodID method;
  
  jstring j_name;
  jstring j_publicId;
  jstring j_systemId;

  ctx = (xmlParserCtxtPtr)vctx;
  sax = (SAXParseContext *)ctx->_private;
  env = sax->env;
  target = sax->obj;

  xmljCheckWellFormed(ctx);
  
  cls = (*env)->GetObjectClass(env, target);
  
  /* Get the notationDecl method */
  method = (*env)->GetMethodID(env, cls, "notationDecl",
      "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V");
  
  j_name = xmljNewString(env, name);
  j_publicId = xmljNewString(env, publicId);
  j_systemId = xmljNewString(env, systemId);
  
  /* Invoke the method */
  (*env)->CallVoidMethod(env, target, method, j_name,
                         j_publicId, j_systemId);
}

void
xmljSAXAttributeDecl(void *vctx,
    const xmlChar *elem,
    const xmlChar *fullName,
    int type,
    int def,
    const xmlChar *defaultValue,
    xmlEnumerationPtr tree)
{
  /* TODO */
  xmlParserCtxtPtr ctx;
  SAXParseContext *sax;
  
  JNIEnv *env;
  jobject target;
  jclass cls;
  jmethodID method;
  
  jstring j_eName;
  jstring j_aName;
  jstring j_type;
  jstring j_mode;
  jstring j_value;

  ctx = (xmlParserCtxtPtr)vctx;
  sax = (SAXParseContext *)ctx->_private;
  env = sax->env;
  target = sax->obj;

  xmljCheckWellFormed(ctx);
  
  cls = (*env)->GetObjectClass(env, target);
  
  /* Get the attributeDecl method */
  method = (*env)->GetMethodID(env, cls, "attributeDecl",
      "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V");
  
  j_eName = xmljNewString(env, elem);
  j_aName = xmljNewString(env, fullName);
  j_type = xmljAttributeTypeName(env, type);
  j_mode = xmljAttributeModeName(env, def);
  j_value = xmljNewString(env, defaultValue);
  
  /* Invoke the method */
  (*env)->CallVoidMethod(env, target, method, j_eName, j_aName,
                         j_type, j_mode, j_value);
}

void
xmljSAXElementDecl(void *vctx,
    const xmlChar *name,
    int type,
    xmlElementContentPtr content)
{
  xmlParserCtxtPtr ctx;
  SAXParseContext *sax;
  
  JNIEnv *env;
  jobject target;
  jclass cls;
  jmethodID method;
  
  jstring j_name;
  jstring j_model;

  ctx = (xmlParserCtxtPtr)vctx;
  sax = (SAXParseContext *)ctx->_private;
  env = sax->env;
  target = sax->obj;

  xmljCheckWellFormed(ctx);
  
  cls = (*env)->GetObjectClass(env, target);
  
  /* Get the elementDecl method */
  method = (*env)->GetMethodID(env, cls, "elementDecl",
      "(Ljava/lang/String;Ljava/lang/String;)V");
  
  j_name = xmljNewString(env, name);
  j_model = NULL; /* TODO */
  
  /* Invoke the method */
  (*env)->CallVoidMethod(env, target, method, j_name, j_model);
}

void
xmljSAXUnparsedEntityDecl (void *vctx, 
    const xmlChar *name, 
    const xmlChar *publicId, 
    const xmlChar *systemId, 
    const xmlChar *notationName)
{
  xmlParserCtxtPtr ctx;
  SAXParseContext *sax;
  
  JNIEnv * env;
  jobject target;
  jclass cls;
  jmethodID method;
  
  jstring j_name;
  jstring j_publicId;
  jstring j_systemId;
  jstring j_notationName;
  
  ctx = (xmlParserCtxtPtr)vctx;
  sax = (SAXParseContext *)ctx->_private;
  env = sax->env;
  target = sax->obj;

  xmljCheckWellFormed(ctx);
  
  cls = (*env)->GetObjectClass(env, target);
	
  /* Get the unparsedEntityDecl method */
  method = (*env)->GetMethodID(env, cls,
      "unparsedEntityDecl",
      "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V");
  
  j_name = xmljNewString(env, name);
  j_publicId = xmljNewString(env, publicId);
  j_systemId = xmljNewString(env, systemId);
  j_notationName = xmljNewString(env, notationName);
  
  /* Invoke the method */
  (*env)->CallVoidMethod(env, target, method, j_name,
                         j_publicId, j_systemId, j_notationName);
}

void
xmljSAXSetDocumentLocator (void *vctx, 
    xmlSAXLocatorPtr loc)
{
  xmlParserCtxtPtr ctx;
  SAXParseContext *sax;
  
  JNIEnv * env;
  jobject target;
  jclass cls;
  jmethodID method;
  
  ctx = (xmlParserCtxtPtr)vctx;
  sax = (SAXParseContext *)ctx->_private;
  env = sax->env;
  target = sax->obj;

  xmljCheckWellFormed(ctx);

  cls = (*env)->GetObjectClass(env, target);
	
  /* Get the unparsedEntityDecl method */
  method = (*env)->GetMethodID(env, cls,
      "setDocumentLocator",
      "(I)V");
  
  /* Invoke the method */
  (*env)->CallVoidMethod(env, target, method, (jint)loc);
}

void
xmljSAXStartDocument (void *vctx)
{
  xmlParserCtxtPtr ctx;
  SAXParseContext *sax;
  
  JNIEnv * env;
  jobject target;
  jclass cls;
  jmethodID method;
  
  ctx = (xmlParserCtxtPtr)vctx;
  sax = (SAXParseContext *)ctx->_private;
  env = sax->env;
  target = sax->obj;

  xmljCheckWellFormed(ctx);

  cls = (*env)->GetObjectClass(env, target);
  
  /* Get the startDocument method */
  method = (*env)->GetMethodID(env, cls, "startDocument",
      "(Z)V");
  
  /* Invoke the method */
  (*env)->CallVoidMethod(env, target, method, ctx->standalone);
}

void
xmljSAXEndDocument (void *vctx)
{
  xmlParserCtxtPtr ctx;
  SAXParseContext *sax;
  
  JNIEnv * env;
  jobject target;
  jclass cls;
  jmethodID method;
  
  ctx = (xmlParserCtxtPtr)vctx;
  sax = (SAXParseContext *)ctx->_private;
  env = sax->env;
  target = sax->obj;

  xmljCheckWellFormed(ctx);

  cls = (*env)->GetObjectClass(env, target);
  
  /* Get the endDocument method */
  method = (*env)->GetMethodID(env, cls, "endDocument",
      "()V");
  
  /* Invoke the method */
  (*env)->CallVoidMethod(env, target, method);
}

void
xmljSAXStartElement (void *vctx,
    const xmlChar *name,
    const xmlChar **attrs)
{
  xmlParserCtxtPtr ctx;
  SAXParseContext *sax;
  
  JNIEnv * env;
  jobject target;
  jclass cls;
  jmethodID method;
  
  jstring j_name;
  jobjectArray j_attrs;
  jstring j_attr;
  jsize len;
 
  ctx = (xmlParserCtxtPtr)vctx;
  sax = (SAXParseContext *)ctx->_private;
  env = sax->env;
  target = sax->obj;

  xmljCheckWellFormed(ctx);

  cls = (*env)->GetObjectClass(env, target);
  
  /* Get the startElement method */
  method = (*env)->GetMethodID(env, cls, "startElement",
      "(Ljava/lang/String;[Ljava/lang/String;)V");
  
  j_name = xmljNewString(env, name);
  /* build attributes array */
  len = 0;
  for (len = 0; attrs && attrs[len] != NULL; len ++)
  {
  }
  cls = (*env)->FindClass(env, "java/lang/String");
  j_attrs = (*env)->NewObjectArray(env, len, cls, NULL);
  len = 0;
  for (len = 0; attrs && attrs[len] != NULL; len ++)
  {
    j_attr = xmljNewString(env, attrs[len]);
    (*env)->SetObjectArrayElement(env, j_attrs, len,
                                  j_attr);
  }
  
  /* Invoke the method */
  (*env)->CallVoidMethod(env, target, method, j_name,
                         j_attrs);
}

void
xmljSAXEndElement (void *vctx,
    const xmlChar *name)
{
  xmlParserCtxtPtr ctx;
  SAXParseContext *sax;
  
  JNIEnv * env;
  jobject target;
  jclass cls;
  jmethodID method;
  
  jstring j_name;

  ctx = (xmlParserCtxtPtr)vctx;
  sax = (SAXParseContext *)ctx->_private;
  env = sax->env;
  target = sax->obj;
  
  xmljCheckWellFormed(ctx);
  
  cls = (*env)->GetObjectClass(env, target);
  
  /* Get the endElement method */
  method = (*env)->GetMethodID(env, cls, "endElement",
      "(Ljava/lang/String;)V");
  
  j_name = xmljNewString(env, name);
  
  /* Invoke the method */
  (*env)->CallVoidMethod(env, target, method, j_name);
}

void
xmljSAXReference(void *vctx,
    const xmlChar *name)
{
  /* TODO */
}

void
xmljSAXCharacters (void *vctx,
    const xmlChar *ch,
    int len)
{
  xmlParserCtxtPtr ctx;
  SAXParseContext *sax;
  
  JNIEnv * env;
  jobject target;
  jclass cls;
  jmethodID method;
  
  jstring j_ch;
  xmlChar *dup;
  
  ctx = (xmlParserCtxtPtr)vctx;
  sax = (SAXParseContext *)ctx->_private;
  env = sax->env;
  target = sax->obj;

  xmljCheckWellFormed(ctx);
  
  cls = (*env)->GetObjectClass(env, target);
  
  /* Get the characters method */
  method = (*env)->GetMethodID(env, cls, "characters",
      "(Ljava/lang/String;)V");
  
  dup = xmlStrndup(ch, len);
  j_ch = xmljNewString(env, dup);
  
  /* Invoke the method */
  (*env)->CallVoidMethod(env, target, method, j_ch);
  xmlFree(dup);
}

void
xmljSAXIgnorableWhitespace (void *vctx,
    const xmlChar *ch,
    int len)
{
  xmlParserCtxtPtr ctx;
  SAXParseContext *sax;
  
  JNIEnv * env;
  jobject target;
  jclass cls;
  jmethodID method;
  
  jstring j_ch;
  xmlChar *dup;
    
  ctx = (xmlParserCtxtPtr)vctx;
  sax = (SAXParseContext *)ctx->_private;
  env = sax->env;
  target = sax->obj;

  xmljCheckWellFormed(ctx);
  
  cls = (*env)->GetObjectClass(env, target);
  
  /* Get the ignorableWhitespace method */
  method = (*env)->GetMethodID(env, cls,
      "ignorableWhitespace",
      "(Ljava/lang/String;)V");
  
  dup = xmlStrndup(ch, len);
  j_ch = xmljNewString(env, dup);
  
  /* Invoke the method */
  (*env)->CallVoidMethod(env, target, method, j_ch);
  xmlFree(dup);
}

void
xmljSAXProcessingInstruction (void *vctx,
    const xmlChar *targ,
    const xmlChar *data)
{
  xmlParserCtxtPtr ctx;
  SAXParseContext *sax;
  
  JNIEnv * env;
  jobject target;
  jclass cls;
  jmethodID method;
  
  jstring j_targ;
  jstring j_data;
 
  ctx = (xmlParserCtxtPtr)vctx;
  sax = (SAXParseContext *)ctx->_private;
  env = sax->env;
  target = sax->obj;

  xmljCheckWellFormed(ctx);
  
  cls = (*env)->GetObjectClass(env, target);
  
  /* Get the processingInstruction method */
  method = (*env)->GetMethodID(env, cls,
      "processingInstruction",
      "(Ljava/lang/String;Ljava/lang/String;)V");
  
  j_targ = xmljNewString(env, targ);
  j_data = xmljNewString(env, data);
  
  /* Invoke the method */
  (*env)->CallVoidMethod(env, target, method,
                         j_targ, j_data);
}

void
xmljSAXComment(void *vctx,
    const xmlChar *value)
{
  xmlParserCtxtPtr ctx;
  SAXParseContext *sax;
  
  JNIEnv * env;
  jobject target;
  jclass cls;
  jmethodID method;
  
  jstring j_text;
    
  ctx = (xmlParserCtxtPtr)vctx;
  sax = (SAXParseContext *)ctx->_private;
  env = sax->env;
  target = sax->obj;

  xmljCheckWellFormed(ctx);
  
  cls = (*env)->GetObjectClass(env, target);
  
  /* Get the comment method */
  method = (*env)->GetMethodID(env, cls, "comment",
      "(Ljava/lang/String;)V");
  
  j_text = xmljNewString(env, value);
  
  /* Invoke the method */
  (*env)->CallVoidMethod(env, target, method, j_text);
}

void
xmljSAXWarning (void *vctx, 
    const char *msg, 
    ...)
{
	va_list args;

  xmlParserCtxtPtr ctx;
  SAXParseContext *sax;
  
  JNIEnv * env;
  jobject target;
  jclass cls;
  jmethodID method;
  
  xmlChar *x_msg;
  jstring j_msg;
  
  ctx = (xmlParserCtxtPtr)vctx;
  sax = (SAXParseContext *)ctx->_private;
  env = sax->env;
  target = sax->obj;
  
  cls = (*env)->GetObjectClass(env, target);
  
  /* Get the warning method */
  method = (*env)->GetMethodID(env, cls,
      "warning",
      "(Ljava/lang/String;)V");
  
  x_msg = (msg == NULL) ? NULL : xmlCharStrdup(msg);
  j_msg = xmljNewString(env, x_msg);
  
  /* Invoke the method */
  va_start(args, msg);
  (*env)->CallVoidMethod(env, target, method, j_msg);
  va_end(args);
}

void
xmljSAXError (void *vctx, 
    const char *msg, 
    ...)
{
  va_list args;
  
  xmlParserCtxtPtr ctx;
  SAXParseContext *sax;
  
  JNIEnv * env;
  jobject target;
  jclass cls;
  jmethodID method;
  
  xmlChar *x_msg;
  jstring j_msg;
  
  ctx = (xmlParserCtxtPtr)vctx;
  sax = (SAXParseContext *)ctx->_private;
  env = sax->env;
  target = sax->obj;
  
  cls = (*env)->GetObjectClass(env, target);
  
  /* Get the error method */
  method = (*env)->GetMethodID(env, cls,
      "error",
      "(Ljava/lang/String;)V");
  
  x_msg = (msg == NULL) ? NULL : xmlCharStrdup(msg);
  j_msg = xmljNewString(env, x_msg);
  
  /* Invoke the method */
  va_start(args, msg);
  (*env)->CallVoidMethod(env, target, method, j_msg);
  va_end(args);
}

void
xmljSAXFatalError (void *vctx, 
    const char *msg, 
    ...)
{
  va_list args;

  xmlParserCtxtPtr ctx;
  SAXParseContext *sax;
  
  JNIEnv * env;
  jobject target;
  jclass cls;
  jmethodID method;
  
  xmlChar *x_msg;
  jstring j_msg;
  
  ctx = (xmlParserCtxtPtr)vctx;
  sax = (SAXParseContext *)ctx->_private;
  env = sax->env;
  target = sax->obj;

  cls = (*env)->GetObjectClass(env, target);
  
  /* Get the fatalError method */
  method = (*env)->GetMethodID(env, cls,
      "fatalError",
      "(Ljava/lang/String;)V");
  
  x_msg = (msg == NULL) ? NULL : xmlCharStrdup(msg);
  j_msg = xmljNewString(env, x_msg);
  
  /* Invoke the method */
  va_start(args, msg);
  (*env)->CallVoidMethod(env, target, method, j_msg);
  va_end(args);
}

void
xmljCheckWellFormed (xmlParserCtxtPtr ctx)
{
  if (!ctx->wellFormed)
    xmljSAXFatalError(ctx, "document is not well-formed");
  if (ctx->validate && !ctx->valid)
    xmljSAXFatalError(ctx, "document is not valid");
}

/*
 * Convert a libxml2 attribute type to a string.
 */
jstring xmljAttributeTypeName (JNIEnv *env,
    int type)
{
  const char *text;
  
  switch (type)
  {
    case XML_ATTRIBUTE_CDATA:
      text = "CDATA";
      break;
    case XML_ATTRIBUTE_ID:
      text = "ID";
      break;
    case XML_ATTRIBUTE_IDREF:
      text = "IDREF";
      break;
    case XML_ATTRIBUTE_IDREFS:
      text = "IDREFS";
      break;
    case XML_ATTRIBUTE_NMTOKEN:
      text = "NMTOKEN";
      break;
    case XML_ATTRIBUTE_NMTOKENS:
      text = "NMTOKENS";
      break;
    case XML_ATTRIBUTE_ENTITY:
      text = "ID";
      break;
    case XML_ATTRIBUTE_ENTITIES:
      text = "ID";
      break;
    default:
      return NULL;
  }

  return (*env)->NewStringUTF(env, text);
}

/*
 * Convert a libxml2 attribute default value type to a string.
 */
jstring xmljAttributeModeName (JNIEnv *env,
    int type)
{
  const char *text;
  
  switch (type)
  {
    case XML_ATTRIBUTE_IMPLIED:
      text = "#IMPLIED";
      break;
    case XML_ATTRIBUTE_REQUIRED:
      text = "#REQUIRED";
      break;
    case XML_ATTRIBUTE_FIXED:
      text = "#FIXED";
      break;
    default:
      return NULL;
  }
  
  return (*env)->NewStringUTF(env, text);
}

