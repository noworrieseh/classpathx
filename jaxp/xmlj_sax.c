/*
 * xmlj_sax.c
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

xmlExternalEntityLoader defaultLoader = NULL;

/* -- GnomeLocator -- */

JNIEXPORT jstring JNICALL
Java_gnu_xml_libxmlj_sax_GnomeLocator_getPublicId (JNIEnv * env,
                                                   jobject self,
                                                   jlong j_ctx,
                                                   jlong j_loc)
{
  xmlParserCtxtPtr ctx;
  xmlSAXLocatorPtr loc;
  SAXParseContext *sax;

  ctx = (xmlParserCtxtPtr) xmljAsPointer (j_ctx);
  loc = (xmlSAXLocatorPtr) xmljAsPointer (j_loc);
  sax = (SAXParseContext *) ctx->_private;
  
  return sax->publicId;
}

JNIEXPORT jstring JNICALL
Java_gnu_xml_libxmlj_sax_GnomeLocator_getSystemId (JNIEnv * env,
                                                   jobject self,
                                                   jlong j_ctx,
                                                   jlong j_loc)
{
  xmlParserCtxtPtr ctx;
  xmlSAXLocatorPtr loc;
  SAXParseContext *sax;

  ctx = (xmlParserCtxtPtr) xmljAsPointer (j_ctx);
  loc = (xmlSAXLocatorPtr) xmljAsPointer (j_loc);
  sax = (SAXParseContext *) ctx->_private;
  
  return sax->systemId;
}

JNIEXPORT jint JNICALL
Java_gnu_xml_libxmlj_sax_GnomeLocator_getLineNumber (JNIEnv * env,
                                                     jobject self,
                                                     jlong j_ctx,
                                                     jlong j_loc)
{
  xmlParserCtxtPtr ctx;
  xmlSAXLocatorPtr loc;

  ctx = (xmlParserCtxtPtr) xmljAsPointer (j_ctx);
  loc = (xmlSAXLocatorPtr) xmljAsPointer (j_loc);
  if (ctx == NULL || ctx->input == NULL)
    {
      return -1;
    }
  return ctx->input->line;
}

JNIEXPORT jint JNICALL
Java_gnu_xml_libxmlj_sax_GnomeLocator_getColumnNumber (JNIEnv * env,
                                                       jobject self,
                                                       jlong j_ctx,
                                                       jlong j_loc)
{
  xmlParserCtxtPtr ctx;
  xmlSAXLocatorPtr loc;

  ctx = (xmlParserCtxtPtr) xmljAsPointer (j_ctx);
  loc = (xmlSAXLocatorPtr) xmljAsPointer (j_loc);
  if (ctx == NULL || ctx->input == NULL)
    {
      return -1;
    }
  return ctx->input->col;
}

/* -- GnomeXMLReader -- */

/*
 * Entry point for SAX parsing.
 */
JNIEXPORT void JNICALL
Java_gnu_xml_libxmlj_sax_GnomeXMLReader_parseStream (JNIEnv * env,
                                                     jobject self,
                                                     jobject in,
                                                     jbyteArray detectBuffer,
                                                     jstring publicId,
                                                     jstring systemId,
                                                     jboolean validate,
                                                     jboolean contentHandler,
                                                     jboolean dtdHandler,
                                                     jboolean entityResolver,
                                                     jboolean errorHandler,
                                                     jboolean
                                                     declarationHandler,
                                                     jboolean lexicalHandler)
{
  xmljParseDocument (env,
                     self,
                     in,
                     detectBuffer,
                     publicId,
                     systemId,
                     validate,
                     0,
                     0,
                     contentHandler,
                     dtdHandler,
                     entityResolver,
                     errorHandler,
                     declarationHandler,
                     lexicalHandler,
                     0);
}

xmlParserInputPtr
xmljExternalEntityLoader (const char *url, const char *id,
                          xmlParserCtxtPtr context)
{
  const xmlChar *systemId;
  const xmlChar *publicId;
  xmlParserInputPtr ret;

  printf("xmljExternalEntityLoader %s %s\n", url, id);
  if (defaultLoader == NULL)
    {
      defaultLoader = xmlGetExternalEntityLoader ();
    }
  systemId = xmlCharStrdup (url);
  publicId = xmlCharStrdup (id);
  ret = xmljSAXResolveEntity (context, publicId, systemId);
  if (ret == NULL)
    {
      ret = defaultLoader (url, id, context);
    }
  return ret;
}

/*
 * Allocates and configures a SAX handler that can report the various
 * classes of callback.
 */
xmlSAXHandlerPtr
xmljNewSAXHandler (xmlSAXHandlerPtr orig,
                   jboolean contentHandler,
                   jboolean dtdHandler,
                   jboolean entityResolver,
                   jboolean errorHandler,
                   jboolean declarationHandler,
                   jboolean lexicalHandler)
{
  xmlSAXHandlerPtr sax;

  sax = (xmlSAXHandlerPtr) malloc (sizeof (xmlSAXHandler));

  if (dtdHandler)
    {
      sax->internalSubset = &xmljSAXInternalSubset;
    }
  else
    {
      sax->internalSubset = (orig == NULL) ? NULL : orig->internalSubset;
    }
  sax->isStandalone = &xmljSAXIsStandalone;
  sax->hasInternalSubset = &xmljSAXHasInternalSubset;
  sax->hasExternalSubset = &xmljSAXHasExternalSubset;
  if (entityResolver)
    {
      sax->resolveEntity = &xmljSAXResolveEntity;
      /* The above function is never called in libxml2 */
      printf ("Set custom external entity loader\n");
      xmlSetExternalEntityLoader (xmljExternalEntityLoader);
    }
  else
    {
      sax->resolveEntity = (orig == NULL) ? NULL : orig->resolveEntity;
      xmlSetExternalEntityLoader (defaultLoader);
    }

  if (declarationHandler)
    {
      sax->entityDecl = &xmljSAXEntityDecl;
      sax->notationDecl = &xmljSAXNotationDecl;
      sax->attributeDecl = &xmljSAXAttributeDecl;
      sax->elementDecl = &xmljSAXElementDecl;
      sax->unparsedEntityDecl = &xmljSAXUnparsedEntityDecl;
    }
  else
    {
      sax->entityDecl = (orig == NULL) ? NULL : orig->entityDecl;
      sax->notationDecl = (orig == NULL) ? NULL : orig->notationDecl;
      sax->attributeDecl = (orig == NULL) ? NULL : orig->attributeDecl;
      sax->elementDecl = (orig == NULL) ? NULL : orig->elementDecl;
      sax->unparsedEntityDecl = (orig == NULL) ? NULL : orig->unparsedEntityDecl;
    }

  /* We always listen for the locator callback */
  sax->setDocumentLocator = &xmljSAXSetDocumentLocator;
  if (contentHandler)
    {
      sax->startDocument = &xmljSAXStartDocument;
      sax->endDocument = &xmljSAXEndDocument;
      sax->startElement = &xmljSAXStartElement;
      sax->endElement = &xmljSAXEndElement;
      sax->characters = &xmljSAXCharacters;
      sax->ignorableWhitespace = &xmljSAXIgnorableWhitespace;
      sax->processingInstruction = &xmljSAXProcessingInstruction;
    }
  else
    {
      sax->startDocument = (orig == NULL) ? NULL : orig->startDocument;
      sax->endDocument = (orig == NULL) ? NULL : orig->endDocument;
      sax->startElement = (orig == NULL) ? NULL : orig->startElement;;
      sax->endElement = (orig == NULL) ? NULL : orig->endElement;
      sax->characters = (orig == NULL) ? NULL : orig->characters;
      sax->ignorableWhitespace = (orig == NULL) ? NULL : orig->ignorableWhitespace;
      sax->processingInstruction = (orig == NULL) ? NULL : orig->processingInstruction;
    }

  if (lexicalHandler)
    {
      /* FIXME sax->getEntity = &xmljSAXGetEntity; */
      sax->getEntity = (orig == NULL) ? NULL : orig->getEntity;
      sax->reference = &xmljSAXReference;
      sax->comment = &xmljSAXComment;
      sax->cdataBlock = &xmljSAXCdataBlock;
    }
  else
    {
      sax->getEntity = (orig == NULL) ? NULL : orig->getEntity;
      sax->reference = (orig == NULL) ? NULL : orig->reference;
      sax->comment = (orig == NULL) ? NULL : orig->comment;
      sax->cdataBlock = (orig == NULL) ? NULL : orig->cdataBlock;
    }

  if (errorHandler)
    {
      sax->warning = &xmljSAXWarning;
      sax->error = &xmljSAXError;
      sax->fatalError = &xmljSAXFatalError;
    }
  else
    {
      sax->warning = (orig == NULL) ? NULL : orig->warning;
      sax->error = (orig == NULL) ? NULL : orig->error;
      sax->fatalError = (orig == NULL) ? NULL : orig->fatalError;
    }

  /* Remaining fields */
  sax->getParameterEntity = (orig == NULL) ? NULL : orig->getParameterEntity;
  sax->externalSubset = (orig == NULL) ? NULL : orig->externalSubset;
  sax->initialized = (orig == NULL) ? 0 : orig->initialized;
  sax->_private = NULL;
  sax->startElementNs = (orig == NULL) ? NULL : orig->startElementNs;
  sax->endElementNs = (orig == NULL) ? NULL : orig->endElementNs;
  sax->serror = NULL;

  return sax;
}

/* -- Callback functions -- */

void
xmljSAXInternalSubset (void *vctx,
                       const xmlChar * name,
                       const xmlChar * publicId, const xmlChar * systemId)
{
  xmlParserCtxtPtr ctx;
  SAXParseContext *sax;
  JNIEnv *env;
  jobject target;
  jstring j_name;
  jstring j_publicId;
  jstring j_systemId;

  ctx = (xmlParserCtxtPtr) vctx;
  sax = (SAXParseContext *) ctx->_private;
  env = sax->env;
  target = sax->obj;

  xmljCheckWellFormed (ctx);

  if (sax->startDTD == NULL)
    {
      sax->startDTD =
        xmljGetMethodID (env,
                         target,
                         "startDTD",
                         "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V");
      if (sax->startDTD == NULL)
        {
          return;
        }
    }

  j_name = xmljNewString (env, name);
  j_publicId = xmljNewString (env, publicId);
  j_systemId = xmljNewString (env, systemId);

  (*env)->CallVoidMethod (env,
                          target,
                          sax->startDTD,
                          j_name,
                          j_publicId,
                          j_systemId);
}

int
xmljSAXIsStandalone (void *vctx)
{
  xmlParserCtxtPtr ctx;

  ctx = (xmlParserCtxtPtr) vctx;
  return ctx->standalone;
}

int
xmljSAXHasInternalSubset (void *vctx)
{
  xmlParserCtxtPtr ctx;
  xmlDocPtr doc;

  ctx = (xmlParserCtxtPtr) vctx;
  doc = ctx->myDoc;
  return (doc->intSubset != NULL);
}

int
xmljSAXHasExternalSubset (void *vctx)
{
  xmlParserCtxtPtr ctx;

  ctx = (xmlParserCtxtPtr) vctx;
  return ctx->hasExternalSubset;
}

xmlParserInputPtr
xmljSAXResolveEntity (void *vctx,
                      const xmlChar * publicId, const xmlChar * systemId)
{
  xmlParserCtxtPtr ctx;
  SAXParseContext *sax;
  JNIEnv *env;
  jobject target;
  jstring j_publicId;
  jstring j_systemId;
  jobject inputStream;

  ctx = (xmlParserCtxtPtr) vctx;
  sax = (SAXParseContext *) ctx->_private;
  env = sax->env;
  target = sax->obj;

  if (sax->resolveEntity == NULL)
    {
      sax->resolveEntity =
        xmljGetMethodID (env,
                         target,
                         "resolveEntity",
                         "(Ljava/lang/String;Ljava/lang/String;)Ljava/io/InputStream;");
      if (sax->resolveEntity == NULL)
        {
          return NULL;
        }
    }

  j_publicId = xmljNewString (env, publicId);
  j_systemId = xmljNewString (env, systemId);

  inputStream = (*env)->CallObjectMethod (env,
                                          target,
                                          sax->resolveEntity,
                                          j_publicId,
                                          j_systemId);

  /* Return an xmlParserInputPtr corresponding to the input stream */
  if (inputStream != NULL)
    {
      jbyteArray detectBuffer;
      jmethodID getDetectBuffer;
      jclass cls;

      /* Get the detect buffer from the NamedInputStream */
      cls = (*env)->GetObjectClass (env, inputStream);
      getDetectBuffer = (*env)->GetMethodID (env, inputStream,
                                             "getDetectBuffer",
                                             "()[B");
      detectBuffer = (*env)->CallObjectMethod (env, inputStream,
                                               getDetectBuffer);
      
      return xmljNewParserInput (env, inputStream, detectBuffer, ctx);
    }
  else
    {
      return NULL;
    }
}

xmlEntityPtr
xmljSAXGetEntity (void *vctx, const xmlChar * name)
{
  /* TODO */
  printf ("xmljSAXGetEntity %s\n", name);
  return NULL;
}

void
xmljSAXEntityDecl (void *vctx,
                   const xmlChar * name,
                   int type,
                   const xmlChar * publicId,
                   const xmlChar * systemId,
                   xmlChar * content)
{
  xmlParserCtxtPtr ctx;
  SAXParseContext *sax;
  JNIEnv *env;
  jobject target;
  jstring j_name;
  jstring j_publicId;
  jstring j_systemId;
  jstring j_value;

  ctx = (xmlParserCtxtPtr) vctx;
  sax = (SAXParseContext *) ctx->_private;
  env = sax->env;
  target = sax->obj;

  xmljCheckWellFormed (ctx);

  j_name = xmljNewString (env, name);
  switch (type)
    {
    case XML_INTERNAL_GENERAL_ENTITY:
    case XML_INTERNAL_PARAMETER_ENTITY:
    case XML_INTERNAL_PREDEFINED_ENTITY:
      if (sax->internalEntityDecl == NULL)
        {
          sax->internalEntityDecl =
            xmljGetMethodID (env,
                             target,
                             "internalEntityDecl",
                             "(Ljava/lang/String;Ljava/lang/String;)V");
          if (sax->internalEntityDecl == NULL)
            {
              return;
            }
        }
      j_value = xmljNewString (env, content);
      (*env)->CallVoidMethod (env,
                              target,
                              sax->internalEntityDecl,
                              j_name,
                              j_value);
      break;
    default:
      if (sax->externalEntityDecl == NULL)
        {
          sax->externalEntityDecl =
            xmljGetMethodID (env,
                             target,
                             "externalEntityDecl",
                             "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V");
          if (sax->externalEntityDecl == NULL)
            {
              return;
            }
        }
      j_publicId = xmljNewString (env, publicId);
      j_systemId = xmljNewString (env, systemId);
      (*env)->CallVoidMethod (env,
                              target,
                              sax->externalEntityDecl,
                              j_name,
                              j_publicId,
                              j_systemId);
    }
}

void
xmljSAXNotationDecl (void *vctx,
                     const xmlChar * name,
                     const xmlChar * publicId,
                     const xmlChar * systemId)
{
  xmlParserCtxtPtr ctx;
  SAXParseContext *sax;
  JNIEnv *env;
  jobject target;
  jstring j_name;
  jstring j_publicId;
  jstring j_systemId;

  ctx = (xmlParserCtxtPtr) vctx;
  sax = (SAXParseContext *) ctx->_private;
  env = sax->env;
  target = sax->obj;

  xmljCheckWellFormed (ctx);

  if (sax->notationDecl == NULL)
    {
      sax->notationDecl =
        xmljGetMethodID (env,
                         target,
                         "notationDecl",
                         "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V");
      if (sax->notationDecl == NULL)
        {
          return;
        }
    }

  j_name = xmljNewString (env, name);
  j_publicId = xmljNewString (env, publicId);
  j_systemId = xmljNewString (env, systemId);

  /* Invoke the method */
  (*env)->CallVoidMethod (env,
                          target,
                          sax->notationDecl,
                          j_name,
                          j_publicId,
                          j_systemId);
}

void
xmljSAXAttributeDecl (void *vctx,
                      const xmlChar * elem,
                      const xmlChar * fullName,
                      int type,
                      int def,
                      const xmlChar * defaultValue,
                      xmlEnumerationPtr tree)
{
  xmlParserCtxtPtr ctx;
  SAXParseContext *sax;
  JNIEnv *env;
  jobject target;
  jstring j_eName;
  jstring j_aName;
  jstring j_type;
  jstring j_mode;
  jstring j_value;

  ctx = (xmlParserCtxtPtr) vctx;
  sax = (SAXParseContext *) ctx->_private;
  env = sax->env;
  target = sax->obj;

  xmljCheckWellFormed (ctx);

  if (sax->attributeDecl == NULL)
    {
      sax->attributeDecl =
        xmljGetMethodID (env,
                         target,
                         "attributeDecl",
                         "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V");
      if (sax->attributeDecl == NULL)
        {
          return;
        }
    }

  j_eName = xmljNewString (env, elem);
  j_aName = xmljNewString (env, fullName);
  j_type = xmljAttributeTypeName (env, type);
  j_mode = xmljAttributeModeName (env, def);
  j_value = xmljNewString (env, defaultValue);

  (*env)->CallVoidMethod (env,
                          target,
                          sax->attributeDecl,
                          j_eName,
                          j_aName,
                          j_type,
                          j_mode,
                          j_value);
}

void
xmljSAXElementDecl (void *vctx,
                    const xmlChar * name,
                    int type,
                    xmlElementContentPtr content)
{
  xmlParserCtxtPtr ctx;
  SAXParseContext *sax;
  JNIEnv *env;
  jobject target;
  jstring j_name;
  jstring j_model;

  ctx = (xmlParserCtxtPtr) vctx;
  sax = (SAXParseContext *) ctx->_private;
  env = sax->env;
  target = sax->obj;

  xmljCheckWellFormed (ctx);

  if (sax->elementDecl == NULL)
    {
      sax->elementDecl =
        xmljGetMethodID (env,
                         target,
                         "elementDecl",
                         "(Ljava/lang/String;Ljava/lang/String;)V");
      if (sax->elementDecl == NULL)
        {
          return;
        }
    }

  j_name = xmljNewString (env, name);
  j_model = NULL;		/* TODO */

  (*env)->CallVoidMethod (env,
                          target,
                          sax->elementDecl,
                          j_name,
                          j_model);
}

void
xmljSAXUnparsedEntityDecl (void *vctx,
                           const xmlChar * name,
                           const xmlChar * publicId,
                           const xmlChar * systemId,
                           const xmlChar * notationName)
{
  xmlParserCtxtPtr ctx;
  SAXParseContext *sax;
  JNIEnv *env;
  jobject target;
  jstring j_name;
  jstring j_publicId;
  jstring j_systemId;
  jstring j_notationName;

  ctx = (xmlParserCtxtPtr) vctx;
  sax = (SAXParseContext *) ctx->_private;
  env = sax->env;
  target = sax->obj;

  xmljCheckWellFormed (ctx);

  if (sax->unparsedEntityDecl == NULL)
    {
      sax->unparsedEntityDecl =
        xmljGetMethodID (env,
                         target,
                         "unparsedEntityDecl",
                         "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V");
      if (sax->unparsedEntityDecl == NULL)
        {
          return;
        }
    }

  j_name = xmljNewString (env, name);
  j_publicId = xmljNewString (env, publicId);
  j_systemId = xmljNewString (env, systemId);
  j_notationName = xmljNewString (env, notationName);

  (*env)->CallVoidMethod (env,
                          target,
                          sax->unparsedEntityDecl,
                          j_name,
                          j_publicId,
                          j_systemId,
                          j_notationName);
}

void
xmljSAXSetDocumentLocator (void *vctx, xmlSAXLocatorPtr loc)
{
  xmlParserCtxtPtr ctx;
  SAXParseContext *sax;
  JNIEnv *env;
  jobject target;

  ctx = (xmlParserCtxtPtr) vctx;
  sax = (SAXParseContext *) ctx->_private;
  env = sax->env;
  target = sax->obj;

  /* Update locator on sax context */
  sax->loc = loc;

  if (sax->setDocumentLocator == NULL)
    {
      sax->setDocumentLocator = xmljGetMethodID (env,
                                                 target,
                                                 "setDocumentLocator",
                                                 "(JJ)V");
      if (sax->setDocumentLocator == NULL)
        {
          return;
        }
    }

  (*env)->CallVoidMethod (env,
                          target,
                          sax->setDocumentLocator,
                          xmljAsField (ctx),
                          xmljAsField (loc));
}

void
xmljSAXStartDocument (void *vctx)
{
  xmlParserCtxtPtr ctx;
  SAXParseContext *sax;
  JNIEnv *env;
  jobject target;

  ctx = (xmlParserCtxtPtr) vctx;
  sax = (SAXParseContext *) ctx->_private;
  env = sax->env;
  target = sax->obj;

  xmljCheckWellFormed (ctx);

  if (sax->startDocument == NULL)
    {
      sax->startDocument = xmljGetMethodID (env,
                                            target,
                                            "startDocument",
                                            "(Z)V");
      if (sax->startDocument == NULL)
        {
          return;
        }
    }

  (*env)->CallVoidMethod (env,
                          target,
                          sax->startDocument,
                          ctx->standalone);
}

void
xmljSAXEndDocument (void *vctx)
{
  xmlParserCtxtPtr ctx;
  SAXParseContext *sax;
  JNIEnv *env;
  jobject target;

  ctx = (xmlParserCtxtPtr) vctx;
  sax = (SAXParseContext *) ctx->_private;
  env = sax->env;
  target = sax->obj;

  xmljCheckWellFormed (ctx);

  if (sax->endDocument == NULL)
    {
      sax->endDocument = xmljGetMethodID (env,
                                          target,
                                          "endDocument",
                                          "()V");
      if (sax->endDocument == NULL)
        {
          return;
        }
    }

  (*env)->CallVoidMethod (env,
                          target,
                          sax->endDocument);
}

void
xmljSAXStartElement (void *vctx,
                     const xmlChar * name,
                     const xmlChar ** attrs)
{
  xmlParserCtxtPtr ctx;
  SAXParseContext *sax;
  JNIEnv *env;
  jobject target;
  jclass cls;
  jstring j_name;
  jobjectArray j_attrs;
  jstring j_attr;
  jsize len;

  ctx = (xmlParserCtxtPtr) vctx;
  sax = (SAXParseContext *) ctx->_private;
  env = sax->env;
  target = sax->obj;

  xmljCheckWellFormed (ctx);

  if (sax->startElement == NULL)
    {
      sax->startElement =
        xmljGetMethodID (env,
                         target,
                         "startElement",
                         "(Ljava/lang/String;[Ljava/lang/String;)V");
      if (sax->startElement == NULL)
        {
          return;
        }
    }

  j_name = xmljNewString (env, name);
  /* build attributes array */
  len = 0;
  for (len = 0; attrs && attrs[len] != NULL; len++)
    {
    }
  cls = (*env)->FindClass (env, "java/lang/String");
  j_attrs = (*env)->NewObjectArray (env, len, cls, NULL);
  len = 0;
  for (len = 0; attrs && attrs[len] != NULL; len++)
    {
      j_attr = xmljNewString (env, attrs[len]);
      (*env)->SetObjectArrayElement (env, j_attrs, len, j_attr);
    }

  (*env)->CallVoidMethod (env,
                          target,
                          sax->startElement,
                          j_name,
                          j_attrs);
  /* TODO free array? */
}

void
xmljSAXEndElement (void *vctx,
                   const xmlChar * name)
{
  xmlParserCtxtPtr ctx;
  SAXParseContext *sax;
  JNIEnv *env;
  jobject target;
  jstring j_name;

  ctx = (xmlParserCtxtPtr) vctx;
  sax = (SAXParseContext *) ctx->_private;
  env = sax->env;
  target = sax->obj;

  xmljCheckWellFormed (ctx);

  if (sax->endElement == NULL)
    {
      sax->endElement = xmljGetMethodID (env,
                                         target,
                                         "endElement",
                                         "(Ljava/lang/String;)V");
      if (sax->endElement == NULL)
        {
          return;
        }
    }

  j_name = xmljNewString (env, name);

  (*env)->CallVoidMethod (env,
                          target,
                          sax->endElement,
                          j_name);
}

void
xmljSAXReference (void *vctx,
                  const xmlChar * name)
{
  /* TODO */
}

void
xmljSAXCharacters (void *vctx,
                   const xmlChar * ch,
                   int len)
{
  xmlParserCtxtPtr ctx;
  SAXParseContext *sax;
  JNIEnv *env;
  jobject target;
  jstring j_ch;
  xmlChar *dup;

  ctx = (xmlParserCtxtPtr) vctx;
  sax = (SAXParseContext *) ctx->_private;
  env = sax->env;
  target = sax->obj;

  xmljCheckWellFormed (ctx);

  if (sax->characters == NULL)
    {
      sax->characters = xmljGetMethodID (env,
                                         target,
                                         "characters",
                                         "(Ljava/lang/String;)V");
      if (sax->characters == NULL)
        {
          return;
        }
    }

  dup = xmlStrndup (ch, len);
  j_ch = xmljNewString (env, dup);

  (*env)->CallVoidMethod (env,
                          target,
                          sax->characters,
                          j_ch);
  xmlFree (dup);
}

void
xmljSAXIgnorableWhitespace (void *vctx,
                            const xmlChar * ch,
                            int len)
{
  xmlParserCtxtPtr ctx;
  SAXParseContext *sax;
  JNIEnv *env;
  jobject target;
  jstring j_ch;
  xmlChar *dup;

  ctx = (xmlParserCtxtPtr) vctx;
  sax = (SAXParseContext *) ctx->_private;
  env = sax->env;
  target = sax->obj;

  xmljCheckWellFormed (ctx);

  if (sax->ignorableWhitespace == NULL)
    {
      sax->ignorableWhitespace = xmljGetMethodID (env,
                                                  target,
                                                  "ignorableWhitespace",
                                                  "(Ljava/lang/String;)V");
      if (sax->ignorableWhitespace == NULL)
        {
          return;
        }
    }

  dup = xmlStrndup (ch, len);
  j_ch = xmljNewString (env, dup);

  (*env)->CallVoidMethod (env,
                          target,
                          sax->ignorableWhitespace,
                          j_ch);
  xmlFree (dup);
}

void
xmljSAXProcessingInstruction (void *vctx,
                              const xmlChar * targ,
                              const xmlChar * data)
{
  xmlParserCtxtPtr ctx;
  SAXParseContext *sax;
  JNIEnv *env;
  jobject target;
  jstring j_targ;
  jstring j_data;

  ctx = (xmlParserCtxtPtr) vctx;
  sax = (SAXParseContext *) ctx->_private;
  env = sax->env;
  target = sax->obj;

  xmljCheckWellFormed (ctx);

  if (sax->processingInstruction == NULL)
    {
      sax->processingInstruction =
        xmljGetMethodID (env,
                         target,
                         "processingInstruction",
                         "(Ljava/lang/String;Ljava/lang/String;)V");
      if (sax->processingInstruction == NULL)
        {
          return;
        }
    }

  j_targ = xmljNewString (env, targ);
  j_data = xmljNewString (env, data);

  (*env)->CallVoidMethod (env,
                          target,
                          sax->processingInstruction,
                          j_targ,
                          j_data);
}

void
xmljSAXComment (void *vctx,
                const xmlChar * value)
{
  xmlParserCtxtPtr ctx;
  SAXParseContext *sax;
  JNIEnv *env;
  jobject target;
  jstring j_text;

  ctx = (xmlParserCtxtPtr) vctx;
  sax = (SAXParseContext *) ctx->_private;
  env = sax->env;
  target = sax->obj;

  xmljCheckWellFormed (ctx);

  if (sax->comment == NULL)
    {
      sax->comment =
        xmljGetMethodID (env,
                         target,
                         "comment",
                         "(Ljava/lang/String;)V");
      if (sax->comment == NULL)
        {
          return;
        }
    }

  j_text = xmljNewString (env, value);

  (*env)->CallVoidMethod (env,
                          target,
                          sax->comment,
                          j_text);
}

void
xmljSAXCdataBlock (void *vctx,
                   const xmlChar * ch,
                   int len)
{
  xmlParserCtxtPtr ctx;
  SAXParseContext *sax;
  JNIEnv *env;
  jobject target;
  jstring j_ch;
  xmlChar *dup;

  ctx = (xmlParserCtxtPtr) vctx;
  sax = (SAXParseContext *) ctx->_private;
  env = sax->env;
  target = sax->obj;

  xmljCheckWellFormed (ctx);

  if (sax->cdataBlock == NULL)
    {
      sax->cdataBlock =
        xmljGetMethodID (env,
                         target,
                         "cdataBlock",
                         "(Ljava/lang/String;)V");
      if (sax->cdataBlock == NULL)
        {
          return;
        }
    }

  dup = xmlStrndup (ch, len);
  j_ch = xmljNewString (env, dup);

  (*env)->CallVoidMethod (env,
                          target,
                          sax->cdataBlock,
                          j_ch);
  xmlFree (dup);
}

void
xmljSAXWarning (void *vctx,
                const char *msg,
                ...)
{
  va_list args;

  xmlParserCtxtPtr ctx;
  SAXParseContext *sax;
  xmlSAXLocatorPtr loc;
  JNIEnv *env;
  jobject target;
  xmlChar *x_msg;
  jstring j_msg;
  jint lineNumber;
  jint columnNumber;
  jstring publicId;
  jstring systemId;

  ctx = (xmlParserCtxtPtr) vctx;
  sax = (SAXParseContext *) ctx->_private;
  loc = (xmlSAXLocatorPtr) sax->loc;
  env = sax->env;
  target = sax->obj;

  if (sax->warning == NULL)
    {
      sax->warning =
        xmljGetMethodID (env,
                         target,
                         "warning",
                         "(Ljava/lang/String;IILjava/lang/String;Ljava/lang/String;)V");
      if (sax->warning == NULL)
        {
          return;
        }
    }

  x_msg = (msg == NULL) ? NULL : xmlCharStrdup (msg);
  j_msg = xmljNewString (env, x_msg);
  lineNumber = loc->getLineNumber (ctx);
  columnNumber = loc->getColumnNumber (ctx);
  publicId = xmljNewString (env, loc->getPublicId (ctx));
  systemId = xmljNewString (env, loc->getSystemId (ctx));

  va_start (args, msg);
  (*env)->CallVoidMethod (env,
                          target,
                          sax->warning,
                          j_msg,
                          lineNumber,
                          columnNumber,
                          publicId,
                          systemId);
  va_end (args);
}

void
xmljSAXError (void *vctx,
              const char *msg,
              ...)
{
  va_list args;

  xmlParserCtxtPtr ctx;
  SAXParseContext *sax;
  xmlSAXLocatorPtr loc;
  JNIEnv *env;
  jobject target;
  xmlChar *x_msg;
  jstring j_msg;
  jint lineNumber;
  jint columnNumber;
  jstring publicId;
  jstring systemId;

  ctx = (xmlParserCtxtPtr) vctx;
  sax = (SAXParseContext *) ctx->_private;
  loc = (xmlSAXLocatorPtr) sax->loc;
  env = sax->env;
  target = sax->obj;

  if (sax->error == NULL)
    {
      sax->error =
        xmljGetMethodID (env,
                         target,
                         "error",
                         "(Ljava/lang/String;IILjava/lang/String;Ljava/lang/String;)V");
      if (sax->error == NULL)
        {
          return;
        }
    }

  x_msg = (msg == NULL) ? NULL : xmlCharStrdup (msg);
  j_msg = xmljNewString (env, x_msg);
  lineNumber = loc->getLineNumber (ctx);
  columnNumber = loc->getColumnNumber (ctx);
  publicId = xmljNewString (env, loc->getPublicId (ctx));
  systemId = xmljNewString (env, loc->getSystemId (ctx));

  va_start (args, msg);
  (*env)->CallVoidMethod (env,
                          target,
                          sax->error,
                          j_msg,
                          lineNumber,
                          columnNumber,
                          publicId,
                          systemId);
  va_end (args);
}

void
xmljSAXFatalError (void *vctx,
                   const char *msg,
                   ...)
{
  va_list args;

  xmlParserCtxtPtr ctx;
  SAXParseContext *sax;
  xmlSAXLocatorPtr loc;
  JNIEnv *env;
  jobject target;
  xmlChar *x_msg;
  jstring j_msg;
  jint lineNumber;
  jint columnNumber;
  jstring publicId;
  jstring systemId;

  ctx = (xmlParserCtxtPtr) vctx;
  sax = (SAXParseContext *) ctx->_private;
  loc = (xmlSAXLocatorPtr) sax->loc;
  env = sax->env;
  target = sax->obj;

  if (sax->fatalError == NULL)
    {
      sax->fatalError =
        xmljGetMethodID (env,
                         target,
                         "fatalError",
                         "(Ljava/lang/String;IILjava/lang/String;Ljava/lang/String;)V");
      if (sax->fatalError == NULL)
        {
          return;
        }
    }

  x_msg = (msg == NULL) ? NULL : xmlCharStrdup (msg);
  j_msg = xmljNewString (env, x_msg);
  lineNumber = loc->getLineNumber (ctx);
  columnNumber = loc->getColumnNumber (ctx);
  publicId = xmljNewString (env, loc->getPublicId (ctx));
  systemId = xmljNewString (env, loc->getSystemId (ctx));

  va_start (args, msg);
  (*env)->CallVoidMethod (env,
                          target,
                          sax->fatalError,
                          j_msg,
                          lineNumber,
                          columnNumber,
                          publicId,
                          systemId);
  va_end (args);
}

void
xmljCheckWellFormed (xmlParserCtxtPtr ctx)
{
  if (!ctx->wellFormed)
    xmljSAXFatalError (ctx, "document is not well-formed");
  if (ctx->validate && !ctx->valid)
    xmljSAXFatalError (ctx, "document is not valid");
}

/*
 * Convert a libxml2 attribute type to a string.
 */
jstring
xmljAttributeTypeName (JNIEnv * env, int type)
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

  return (*env)->NewStringUTF (env, text);
}

/*
 * Convert a libxml2 attribute default value type to a string.
 */
jstring
xmljAttributeModeName (JNIEnv * env, int type)
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

  return (*env)->NewStringUTF (env, text);
}
