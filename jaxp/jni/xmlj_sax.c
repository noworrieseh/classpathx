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
#include "xmlj_util.h"

/* JNI environment */
JNIEnv *sax_cb_env;

static xmlSAXHandler sax_parser = {
	NULL /* internalSubsetSAXFunc */,
	NULL /* isStandaloneSAXFunc */,
	NULL /* hasInternalSubsetSAXFunc */,
	NULL /* hasExternalSubsetSAXFunc */,
	(resolveEntitySAXFunc)jaxpResolveEntity,
	NULL /* getEntitySAXFunc */,
	NULL /* entityDeclSAXFunc */,
	(notationDeclSAXFunc)jaxpNotationDecl,
	NULL /* attributeDecl */,
	NULL /* elementDecl */,
	(unparsedEntityDeclSAXFunc)jaxpUnparsedEntityDecl,
	(setDocumentLocatorSAXFunc)jaxpSetDocumentLocator,
	(startDocumentSAXFunc)jaxpStartDocument,
	(endDocumentSAXFunc)jaxpEndDocument,
	(startElementSAXFunc)jaxpStartElement,
	(endElementSAXFunc)jaxpEndElement,
	NULL /* referenceSAXFunc */,
	(charactersSAXFunc)jaxpCharacters,
	(ignorableWhitespaceSAXFunc)jaxpIgnorableWhitespace,
	(processingInstructionSAXFunc)jaxpProcessingInstruction,
	NULL /* commentSAXFunc */,
	(warningSAXFunc)jaxpWarning,
	(errorSAXFunc)jaxpError,
	(fatalErrorSAXFunc)jaxpFatalError,
};

/* -- GnomeLocator -- */

/*
 * Class:     gnu_xml_libxmlj_sax_GnomeLocator
 * Method:    getPublicId
 * Signature: ()Ljava/lang/String;
 */
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
	locator = (xmlSAXLocatorPtr)(*env)->GetObjectField(env, self, field);

	ret = locator->getPublicId(locator);
	j_ret = xmljNewString(env, ret);
	return j_ret;
}

/*
 * Class:     gnu_xml_libxmlj_sax_GnomeLocator
 * Method:    getSystemId
 * Signature: ()Ljava/lang/String;
 */
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
	locator = (xmlSAXLocatorPtr)(*env)->GetObjectField(env, self, field);

	ret = locator->getSystemId(locator);
	j_ret = xmljNewString(env, ret);
	return j_ret;
}

/*
 * Class:     gnu_xml_libxmlj_sax_GnomeLocator
 * Method:    getLineNumber
 * Signature: ()I
 */
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

	return locator->getLineNumber(locator);
}

/*
 * Class:     gnu_xml_libxmlj_sax_GnomeLocator
 * Method:    getColumnNumber
 * Signature: ()I
 */
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

	return locator->getColumnNumber(locator);
}

/* -- GnomeXMLReader -- */

/*
 * Class:     gnu_xml_libxmlj_sax_GnomeXMLReader
 * Method:    createContext
 * Signature: ()I
 */
JNIEXPORT jint JNICALL
Java_gnu_xml_libxmlj_sax_GnomeXMLReader_createContext (JNIEnv *env,
		jobject self)
{
	return (jint)xmlCreateIOParserCtxt(&sax_parser, NULL, NULL, NULL, NULL,
		XML_CHAR_ENCODING_UTF8);
}

/*
 * Class:     gnu_xml_libxmlj_sax_GnomeXMLReader
 * Method:    clearContext
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL
Java_gnu_xml_libxmlj_sax_GnomeXMLReader_clearContext (JNIEnv *env,
		jobject self,
		jint context)
{
  xmlClearParserCtxt((xmlParserCtxtPtr)context);
  return 0;
}

/*
 * Class:     gnu_xml_libxmlj_sax_GnomeXMLReader
 * Method:    getFeature
 * Signature: (ILjava/lang/String;)I
 */
JNIEXPORT jint JNICALL
Java_gnu_xml_libxmlj_sax_GnomeXMLReader_getFeature (JNIEnv *env,
		jobject self,
		jint context,
		jstring name)
{
	jint ret;
	const char *s_name;
	void *result;

	s_name = xmljGetStringChars(env, name);
    result = NULL; /* TODO */
	ret = xmlGetFeature((xmlParserCtxtPtr)context, s_name, result);
	xmljReleaseStringChars(env, name, s_name);
	if (ret == -1)
		return ret;
	else
		return 0; /* TODO */
}

/*
 * Class:     gnu_xml_libxmlj_sax_GnomeXMLReader
 * Method:    setFeature
 * Signature: (ILjava/lang/String;I)I
 */
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

	s_name = xmljGetStringChars(env, name);
	p_value = &value;
	ret = xmlSetFeature((xmlParserCtxtPtr)context, s_name, p_value);
	xmljReleaseStringChars(env, name, s_name);
	return ret;
}

/*
 * Class:     gnu_xml_libxmlj_sax_GnomeXMLReader
 * Method:    parseFile
 * Signature: (ILjava/lang/String;)V
 */
JNIEXPORT jint JNICALL
Java_gnu_xml_libxmlj_sax_GnomeXMLReader_parseFile (JNIEnv *env,
		jobject self,
		jint context,
		jstring filename)
{
	const char *s_filename;
	jint ret;

	sax_cb_env = env;
	s_filename = xmljGetStringChars(env, filename);
	ret = xmlSAXUserParseFile(&sax_parser, self, s_filename);
	xmljReleaseStringChars(env, filename, s_filename);
	return ret;
}

/*
 * Class:     gnu_xml_libxmlj_sax_GnomeXMLReader
 * Method:    parseMemory
 * Signature: (I[B)V
 */
JNIEXPORT jint JNICALL
Java_gnu_xml_libxmlj_sax_GnomeXMLReader_parseMemory (JNIEnv *env,
		jobject self,
		jint context,
		jbyteArray buf)
{
	jint ret;
	char *s_buf;
	jsize len;

	sax_cb_env = env;
	len = (*env)->GetArrayLength(env, buf);
	s_buf = (*env)->GetByteArrayElements(env, buf, 0);
	ret = xmlSAXUserParseMemory(((xmlParserCtxtPtr)context)->sax, self,
		s_buf, len);
	(*env)->ReleaseByteArrayElements(env, buf, s_buf, 0);
	return ret;
}

/* -- Callback functions -- */

xmlParserInputPtr
jaxpResolveEntity (void * ctx,
		const xmlChar * publicId,
		const xmlChar * systemId)
{
	return NULL;
}

void
jaxpNotationDecl (void * ctx, 
		const xmlChar * name, 
		const xmlChar * publicId, 
		const xmlChar * systemId)
{
	jobject target;
	jclass cls;
	jmethodID method;

	jstring j_name;
	jstring j_publicId;
	jstring j_systemId;

	target = (jobject)ctx;
	cls = (*sax_cb_env)->GetObjectClass(sax_cb_env, target);
	
	/* Get the notationDecl method */
	method = (*sax_cb_env)->GetMethodID(sax_cb_env, cls, "notationDecl",
			"(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V");
	if (method == NULL)
		return;

	j_name = xmljNewString(sax_cb_env, name);
	j_publicId = xmljNewString(sax_cb_env, publicId);
	j_systemId = xmljNewString(sax_cb_env, systemId);
	
	/* Invoke the method */
	(*sax_cb_env)->CallVoidMethod(sax_cb_env, target, method, j_name,
									j_publicId, j_systemId);
}

void
jaxpUnparsedEntityDecl (void * ctx, 
		const xmlChar * name, 
		const xmlChar * publicId, 
		const xmlChar * systemId, 
		const xmlChar * notationName)
{
	jobject target;
	jclass cls;
	jmethodID method;

	jstring j_name;
	jstring j_publicId;
	jstring j_systemId;
	jstring j_notationName;

	target = (jobject)ctx;
	cls = (*sax_cb_env)->GetObjectClass(sax_cb_env, target);
	
	/* Get the unparsedEntityDecl method */
	method = (*sax_cb_env)->GetMethodID(sax_cb_env, cls,
			"unparsedEntityDecl",
			"(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V");
	if (method == NULL)
		return;

	j_name = xmljNewString(sax_cb_env, name);
	j_publicId = xmljNewString(sax_cb_env, publicId);
	j_systemId = xmljNewString(sax_cb_env, systemId);
	j_notationName = xmljNewString(sax_cb_env, notationName);
	
	/* Invoke the method */
	(*sax_cb_env)->CallVoidMethod(sax_cb_env, target, method, j_name,
									j_publicId, j_systemId, j_notationName);
}

void
jaxpSetDocumentLocator (void * ctx, 
		xmlSAXLocatorPtr loc)
{
	jobject target;
	jclass cls;
	jmethodID method;

	target = (jobject)ctx;
	cls = (*sax_cb_env)->GetObjectClass(sax_cb_env, target);
	
	/* Get the unparsedEntityDecl method */
	method = (*sax_cb_env)->GetMethodID(sax_cb_env, cls,
			"setDocumentLocator",
			"(I)V");
	if (method == NULL)
		return;
	
	/* Invoke the method */
	(*sax_cb_env)->CallVoidMethod(sax_cb_env, target, method, (jint)loc);
}

void
jaxpStartDocument (void * ctx)
{
	jobject target;
	jclass cls;
	jmethodID method;

	target = (jobject)ctx;
	cls = (*sax_cb_env)->GetObjectClass(sax_cb_env, target);
	
	/* Get the startDocument method */
	method = (*sax_cb_env)->GetMethodID(sax_cb_env, cls, "startDocument",
			"()V");
	if (method == NULL)
		return;
	
	/* Invoke the method */
	(*sax_cb_env)->CallVoidMethod(sax_cb_env, target, method);
}

void
jaxpEndDocument (void * ctx)
{
	jobject target;
	jclass cls;
	jmethodID method;

	target = (jobject)ctx;
	cls = (*sax_cb_env)->GetObjectClass(sax_cb_env, target);
	
	/* Get the endDocument method */
	method = (*sax_cb_env)->GetMethodID(sax_cb_env, cls, "endDocument",
			"()V");
	if (method == NULL)
		return;
	
	/* Invoke the method */
	(*sax_cb_env)->CallVoidMethod(sax_cb_env, target, method);
}

void
jaxpStartElement (void * ctx,
		const xmlChar * name,
		const xmlChar ** attrs)
{
	jobject target;
	jclass cls;
	jmethodID method;

	jstring j_name;
	jobjectArray j_attrs;
	jstring j_attr;
	jsize len;

	target = (jobject)ctx;
	cls = (*sax_cb_env)->GetObjectClass(sax_cb_env, target);
	
	/* Get the startElement method */
	method = (*sax_cb_env)->GetMethodID(sax_cb_env, cls, "startElement",
			"(Ljava/lang/String;[Ljava/lang/String;)V");
	if (method == NULL)
		return;

	j_name = xmljNewString(sax_cb_env, name);
	/* build attributes array */
	len = 0;
	for (len = 0; attrs && attrs[len] != NULL; len ++)
	{
	}
	cls = (*sax_cb_env)->FindClass(sax_cb_env, "java/lang/String");
	j_attrs = (*sax_cb_env)->NewObjectArray(sax_cb_env, len, cls, NULL);
	len = 0;
	for (len = 0; attrs && attrs[len] != NULL; len ++)
 	{
		j_attr = xmljNewString(sax_cb_env, attrs[len]);
		(*sax_cb_env)->SetObjectArrayElement(sax_cb_env, j_attrs, len,
											   j_attr);
	}
	
	/* Invoke the method */
	(*sax_cb_env)->CallVoidMethod(sax_cb_env, target, method, j_name,
									j_attrs);
}

void
jaxpEndElement (void * ctx,
		const xmlChar * name)
{
	jobject target;
	jclass cls;
	jmethodID method;

	jstring j_name;

	target = (jobject)ctx;
	cls = (*sax_cb_env)->GetObjectClass(sax_cb_env, target);
	
	/* Get the endElement method */
	method = (*sax_cb_env)->GetMethodID(sax_cb_env, cls, "endElement",
			"(Ljava/lang/String;)V");
	if (method == NULL)
		return;

	j_name = xmljNewString(sax_cb_env, name);
	
	/* Invoke the method */
	(*sax_cb_env)->CallVoidMethod(sax_cb_env, target, method, j_name);
}

void
jaxpCharacters (void * ctx,
		const xmlChar * ch,
		int len)
{
	jobject target;
	jclass cls;
	jmethodID method;

	jstring j_ch;

	target = (jobject)ctx;
	cls = (*sax_cb_env)->GetObjectClass(sax_cb_env, target);
	
	/* Get the characters method */
	method = (*sax_cb_env)->GetMethodID(sax_cb_env, cls, "characters",
			"(Ljava/lang/String;I)V");
	if (method == NULL)
		return;

	j_ch = xmljNewString(sax_cb_env, ch);
	
	/* Invoke the method */
	(*sax_cb_env)->CallVoidMethod(sax_cb_env, target, method, j_ch, len);
}

void
jaxpIgnorableWhitespace (void * ctx,
		const xmlChar * ch,
		int len)
{
	jobject target;
	jclass cls;
	jmethodID method;

	jstring j_ch;

	target = (jobject)ctx;
	cls = (*sax_cb_env)->GetObjectClass(sax_cb_env, target);
	
	/* Get the ignorableWhitespace method */
	method = (*sax_cb_env)->GetMethodID(sax_cb_env, cls,
			"ignorableWhitespace",
			"(Ljava/lang/String;I)V");
	if (method == NULL)
		return;

	j_ch = xmljNewString(sax_cb_env, ch);
	
	/* Invoke the method */
	(*sax_cb_env)->CallVoidMethod(sax_cb_env, target, method, j_ch, len);
}

void
jaxpProcessingInstruction (void * ctx,
		const xmlChar * targ,
		const xmlChar * data)
{
	jobject target;
	jclass cls;
	jmethodID method;

	jstring j_targ;
	jstring j_data;

	target = (jobject)ctx;
	cls = (*sax_cb_env)->GetObjectClass(sax_cb_env, target);
	
	/* Get the processingInstruction method */
	method = (*sax_cb_env)->GetMethodID(sax_cb_env, cls,
			"processingInstruction",
			"(Ljava/lang/String;Ljava/lang/String;)V");
	if (method == NULL)
		return;

	j_targ = xmljNewString(sax_cb_env, targ);
	j_data = xmljNewString(sax_cb_env, data);
	
	/* Invoke the method */
	(*sax_cb_env)->CallVoidMethod(sax_cb_env, target, method,
									j_targ, j_data);
}

void
jaxpWarning (void * ctx, 
		const char * msg, 
		...)
{
	va_list args;

	jobject target;
	jclass cls;
	jmethodID method;

	jstring j_msg;

	target = (jobject)ctx;
	cls = (*sax_cb_env)->GetObjectClass(sax_cb_env, target);
	
	/* Get the warning method */
	method = (*sax_cb_env)->GetMethodID(sax_cb_env, cls,
			"warning",
			"(Ljava/lang/String;)V");
	if (method == NULL)
		return;

	j_msg = xmljNewString(sax_cb_env, msg);
	
	/* Invoke the method */
	va_start(args, msg);
	(*sax_cb_env)->CallVoidMethod(sax_cb_env, target, method, j_msg);
	va_end(args);
}

void
jaxpError (void * ctx, 
		const char * msg, 
		...)
{
	va_list args;

	jobject target;
	jclass cls;
	jmethodID method;

	jstring j_msg;

	target = (jobject)ctx;
	cls = (*sax_cb_env)->GetObjectClass(sax_cb_env, target);
	
	/* Get the error method */
	method = (*sax_cb_env)->GetMethodID(sax_cb_env, cls,
			"error",
			"(Ljava/lang/String;)V");
	if (method == NULL)
		return;

	j_msg = xmljNewString(sax_cb_env, msg);
	
	/* Invoke the method */
	va_start(args, msg);
	(*sax_cb_env)->CallVoidMethod(sax_cb_env, target, method, j_msg);
	va_end(args);
}

void
jaxpFatalError (void * ctx, 
		const char * msg, 
		...)
{
	va_list args;

	jobject target;
	jclass cls;
	jmethodID method;

	jstring j_msg;

	target = (jobject)ctx;
	cls = (*sax_cb_env)->GetObjectClass(sax_cb_env, target);
	
	/* Get the fatalError method */
	method = (*sax_cb_env)->GetMethodID(sax_cb_env, cls,
			"fatalError",
			"(Ljava/lang/String;)V");
	if (method == NULL)
		return;

	j_msg = xmljNewString(sax_cb_env, msg);
	
	/* Invoke the method */
	va_start(args, msg);
	(*sax_cb_env)->CallVoidMethod(sax_cb_env, target, method, j_msg);
	va_end(args);
}

