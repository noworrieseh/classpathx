/* 
 * Copyright (C) 2003, 2004 Free Software Foundation, Inc.
 * 
 * This file is part of GNU Classpathx/jaxp.
 * 
 * GNU Classpath is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2, or (at your option)
 * any later version.
 *  
 * GNU Classpath is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with GNU Classpath; see the file COPYING.  If not, write to the
 * Free Software Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA
 * 02111-1307 USA.
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
 * obligated to do so.  If you do not wish to do so, delete this
 * exception statement from your version.
 */

#include "xmlj_io.h"
#include "xmlj_error.h"

#include <math.h>
#include <string.h>
#include <stdio.h>
#include <stdarg.h>

#include <libxml/xmlIO.h>
#include <libxml/parserInternals.h>

#include <pthread.h>

#define MIN(a, b) (((a) < (b)) ? (a) : (b))
#define UNSIGN(a) (((a) < 0) ? ((a) + 0x100) : (a))

#define DETECT_BUFFER_SIZE 50

typedef struct _OutputStreamContext
{

  JNIEnv *env;
  jobject outputStream;
  jmethodID outputStreamWriteFunc;
  jmethodID outputStreamCloseFunc;

}
OutputStreamContext;

typedef struct _InputStreamContext
{

  JNIEnv *env;
  jobject inputStream;
  jmethodID inputStreamReadFunc;
  jmethodID inputStreamCloseFunc;
  jobject bufferByteArray;
  jint bufferLength;

}
InputStreamContext;

InputStreamContext *
xmljNewInputStreamContext (JNIEnv * env,
                           jobject inputStream);

void 
xmljFreeInputStreamContext (InputStreamContext * inContext);

int 
xmljInputReadCallback (void *context, char *buffer, int len);

int 
xmljInputCloseCallback (void *context);

int 
xmljOutputWriteCallback (void *context, const char *buffer, int len);

int 
xmljOutputCloseCallback (void *context);

OutputStreamContext *
xmljNewOutputStreamContext (JNIEnv * env,
                            jobject outputStream);


int
xmljOutputWriteCallback (void *context, const char *buffer, int len)
{
  OutputStreamContext *outContext;
  JNIEnv *env;
  jbyteArray byteArray;

  outContext = (OutputStreamContext *) context;
  env = outContext->env;
  byteArray = (*env)->NewByteArray (env, len);

  if (0 != byteArray)
  {
    (*env)->SetByteArrayRegion (env, byteArray, 0, len, (jbyte *) buffer);
    
    (*env)->CallVoidMethod (env,
                            outContext->outputStream,
                            outContext->outputStreamWriteFunc, byteArray);
    
    (*env)->DeleteLocalRef (env, byteArray);
    
    return (*env)->ExceptionOccurred (env) ? -1 : len;
  }
  else
  {
    /* Out of memory, signal error */
    return -1;
  }
}

int
xmljOutputCloseCallback (void *context)
{
  OutputStreamContext *outContext;
  JNIEnv *env;
  
  outContext = (OutputStreamContext *) context;
  env = outContext->env;
  (*env)->CallVoidMethod (env,
			  outContext->outputStream,
			  outContext->outputStreamCloseFunc);

  return (*env)->ExceptionOccurred (env) ? -1 : 0;
}

int
xmljInputReadCallback (void *context, char *buffer, int len)
{
  InputStreamContext *inContext;
  JNIEnv *env;
  jint nread;
  int offset;

  inContext = (InputStreamContext *) context;
  env = inContext->env;
  nread = 0;

  for (offset = 0; offset < len && nread >= 0; )
  {
    nread = (*env)->CallIntMethod (env,
        inContext->inputStream,
        inContext->inputStreamReadFunc,
        inContext->bufferByteArray,
        0, MIN (len - offset,
          inContext->bufferLength));
    
    if (nread > 0)
    {
      (*env)->GetByteArrayRegion (env,
                                  inContext->bufferByteArray,
                                  0, nread, ((jbyte *) buffer) + offset);
      
      offset += nread;
    }
  }
  
  return (*env)->ExceptionOccurred (env) ? -1 : offset;
}

int
xmljInputCloseCallback (void *context)
{
  InputStreamContext *inContext;
  JNIEnv *env;
  
  inContext = (InputStreamContext *) context;
  env = inContext->env;
  (*env)->CallVoidMethod (env, inContext->inputStream,
			  inContext->inputStreamCloseFunc);

  return (*env)->ExceptionOccurred (env) ? -1 : 0;
}

InputStreamContext *
xmljNewInputStreamContext (JNIEnv * env, jobject inputStream)
{
  jclass inputStreamClass;
  InputStreamContext *result;
  
  inputStreamClass = (*env)->FindClass (env, "java/io/InputStream");
  if (inputStreamClass == NULL)
  {
    xmljThrowException(env, "java/lang/ClassNotFoundException",
        "java.io.InputStream");
    return NULL;
  }
  result = (InputStreamContext *) malloc (sizeof (InputStreamContext));
  if (result == NULL)
    return NULL;

  result->env = env;
  result->inputStream = inputStream;
  result->inputStreamReadFunc =
    (*env)->GetMethodID (env, inputStreamClass, "read", "([BII)I");
  result->inputStreamCloseFunc =
    (*env)->GetMethodID (env, inputStreamClass, "close", "()V");
  result->bufferLength = 4096;
  result->bufferByteArray = (*env)->NewByteArray (env, result->bufferLength);
  return result;
}

void
xmljFreeInputStreamContext (InputStreamContext * inContext)
{
  JNIEnv *env;
  
  env = inContext->env;
  (*env)->DeleteLocalRef (env, inContext->bufferByteArray);
  free (inContext);
}

OutputStreamContext *
xmljNewOutputStreamContext (JNIEnv * env, jobject outputStream)
{
  jclass outputStreamClass;
  OutputStreamContext *result;
  
  outputStreamClass = (*env)->FindClass (env, "java/io/OutputStream");
  if (outputStreamClass == NULL)
  {
    xmljThrowException(env, "java/lang/ClassNotFoundException",
        "java.io.OutputStream");
    return NULL;
  }
  result = (OutputStreamContext *) malloc (sizeof (OutputStreamContext));
  if (result == NULL)
    return NULL;
  
  result->env = env;
  result->outputStream = outputStream;
  result->outputStreamWriteFunc =
    (*env)->GetMethodID (env, outputStreamClass, "write", "([B)V");
  result->outputStreamCloseFunc =
    (*env)->GetMethodID (env, outputStreamClass, "close", "()V");
  return result;
}


void
xmljFreeOutputStreamContext (OutputStreamContext * outContext)
{
  free (outContext);
}

xmlCharEncoding
xmljDetectCharEncoding (JNIEnv *env,
    jobject in)
{
  xmlCharEncoding ret;
  jclass cls;
  jbyteArray buffer;
  jmethodID readMethod;
  jmethodID unreadMethod;
  jint nread;
  int i;

  /* Find PushbackInputStream class */
  cls = (*env)->FindClass (env, "java/io/PushbackInputStream");
  if (cls == NULL)
  {
    xmljThrowException(env, "java/lang/ClassNotFoundException",
        "java.io.PushbackInputStream");
    return XML_CHAR_ENCODING_ERROR;
  }
  /* Find read and unread methods */
  readMethod = (*env)->GetMethodID (env, cls, "read", "([B)I");
  unreadMethod = (*env)->GetMethodID (env, cls, "unread", "([BII)V");

  /* Allocate buffer and read bytes */
  buffer = (*env)->NewByteArray (env, DETECT_BUFFER_SIZE);
  nread = (*env)->CallIntMethod (env, in, readMethod, buffer);

  if (!(*env)->ExceptionOccurred(env) && nread > 0)
  {
    jbyte nativeBuffer[DETECT_BUFFER_SIZE + 1];
    unsigned char converted[DETECT_BUFFER_SIZE + 1];
    
    /* Unread bytes back into pushback input stream */
    (*env)->CallVoidMethod (env, in, unreadMethod, buffer, 0, nread);
    
    if (nread >= 5)
    {
      memset (nativeBuffer, 0, DETECT_BUFFER_SIZE + 1);
      (*env)->GetByteArrayRegion (env, buffer, 0, nread, nativeBuffer);
      /* Convert from signed to unsigned */
      for (i = 0; i < DETECT_BUFFER_SIZE + 1; i++)
      {
        converted[i] = UNSIGN(nativeBuffer[i]);
      }
      ret = xmlDetectCharEncoding (converted, nread);
    }
    else
    {
      ret = XML_CHAR_ENCODING_NONE;
    }
  }
  else
  {
    if (!(*env)->ExceptionOccurred(env))
    {
      xmljThrowException(env, "java/io/IOException",
          "document is empty");
    }
    ret = XML_CHAR_ENCODING_ERROR;
  }
  
  /* Unallocate buffer */
  (*env)->DeleteLocalRef (env, buffer);

  return ret;
}

xmlParserCtxtPtr
xmljEstablishParserContext (JNIEnv * env,
    jobject inputStream,
    jstring inSystemId,
    jstring inPublicId,
    jboolean validate,
    jboolean coalesce,
    jboolean expandEntities,
    jobject saxEntityResolver,
    jobject saxErrorAdapter,
    int useSaxErrorContext)
{
  InputStreamContext *inputContext;
  xmlCharEncoding encoding;
  xmlParserCtxtPtr parserContext;
  int options;
  SaxErrorContext *saxErrorContext;
  
  encoding = xmljDetectCharEncoding (env, inputStream);
  if (encoding != XML_CHAR_ENCODING_ERROR)
  {
    inputContext = xmljNewInputStreamContext (env, inputStream);
    if (NULL != inputContext)
    {
      parserContext = xmlCreateIOParserCtxt (NULL, NULL,
          /* NOTE: userdata must be NULL for DOM to work */
          xmljInputReadCallback,
          xmljInputCloseCallback,
          inputContext,
          encoding);
      if (NULL != parserContext)
      {
        /* Set parsing options */
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
        
        xmlCtxtUseOptions(parserContext, options);
        parserContext->userData = parserContext;
        
        if (!useSaxErrorContext)
          return parserContext;
        
        /* TODO set up SAX entity resolver */
        
        xmljInitErrorHandling (parserContext->sax);
        
        /* Set up SAX error context */
        saxErrorContext = xmljCreateSaxErrorContext (env,
            saxErrorAdapter,
            inSystemId,
            inPublicId);
        if (NULL != saxErrorContext)
        {
          parserContext->_private = saxErrorContext;
          return parserContext;
        }
        else
        {
          xmlFreeParserCtxt (parserContext);
          xmljFreeSaxErrorContext (saxErrorContext);
        }
      }
      xmljFreeInputStreamContext (inputContext);
    }
  }
  return NULL;
}

void
xmljReleaseParserContext (xmlParserCtxtPtr parserContext)
{
  SaxErrorContext *saxErrorContext;
  InputStreamContext *inputStreamContext;
  
  saxErrorContext
    = (SaxErrorContext *) parserContext->_private;

  inputStreamContext
    = (InputStreamContext *) parserContext->input->buf->context;

  xmljFreeSaxErrorContext (saxErrorContext);

  xmlFreeParserCtxt (parserContext);

  xmljFreeInputStreamContext (inputStreamContext);
}

xmlDocPtr
xmljParseJavaInputStream (JNIEnv * env,
			  jobject inputStream,
			  jstring inSystemId,
			  jstring inPublicId,
              jboolean validate,
              jboolean coalesce,
              jboolean expandEntities,
              jobject saxEntityResolver,
              jobject saxErrorAdapter)
{
  xmlDocPtr tree = NULL;

  xmlParserCtxtPtr parserContext
    = xmljEstablishParserContext (env, inputStream,
				  inSystemId,
				  inPublicId,
                  validate,
                  coalesce,
                  expandEntities,
                  saxEntityResolver,
				  saxErrorAdapter,
                  1);

  if (NULL != parserContext)
  {
    tree = xmljParseDocument(env, parserContext);
    xmljReleaseParserContext (parserContext);
  }
  
  return tree;
}

xmlDocPtr
xmljParseDocument (JNIEnv *env,
    xmlParserCtxtPtr parserContext)
{
  xmlDocPtr tree = NULL;
  int ret;

  xmljSetThreadContext ((SaxErrorContext *) parserContext->_private);

  ret = xmlParseDocument (parserContext);
  if (0 == ret)
    tree = parserContext->myDoc;
  else
  {
    printf("ERROR[%d]: %s\n", ret, parserContext->lastError.message);
    xmljThrowDOMException (env, ret, parserContext->lastError.message);
  }
  
  xmljClearThreadContext ();

  return tree;
}

void
xmljSaveFileToJavaOutputStream (JNIEnv * env, jobject outputStream,
				xmlDocPtr tree,
				const char *outputEncodingName)
{
  OutputStreamContext *outputContext =
    xmljNewOutputStreamContext (env, outputStream);

  xmlCharEncoding outputEncoding = xmlParseCharEncoding (outputEncodingName);

  xmlOutputBufferPtr outputBuffer =
    xmlOutputBufferCreateIO (xmljOutputWriteCallback,
			     xmljOutputCloseCallback,
			     outputContext,
			     xmlGetCharEncodingHandler (outputEncoding));

  /* Write result to output stream */

  xmlSaveFileTo (outputBuffer, tree, outputEncodingName);

  xmljFreeOutputStreamContext (outputContext);
}

jobject
xmljResolveURI (SaxErrorContext * saxErrorContext,
		const char *URL, const char *ID)
{
  JNIEnv *env = saxErrorContext->env;

  jstring hrefString = (*env)->NewStringUTF (env, URL);
  jstring baseString = saxErrorContext->systemId;

  jobject sourceWrapper = (*env)->CallObjectMethod (env,
						    saxErrorContext->
						    saxErrorAdapter,
						    saxErrorContext->
						    resolveURIMethodID,
						    hrefString,
						    baseString);
  (*env)->DeleteLocalRef (env, hrefString);

  if (NULL == sourceWrapper)
    {
      return NULL;
    }
  else
    {
      jobject sourceInputStream = (*env)->CallObjectMethod (env,
							    sourceWrapper,
							    saxErrorContext->
							    getInputStreamMethodID);

      (*env)->DeleteLocalRef (env, sourceWrapper);

      if ((*env)->ExceptionOccurred (env))
	{
	  /* Report to ErrorAdapter here? */
	  return NULL;
	}

      return sourceInputStream;
    }
}

xmlDocPtr
xmljResolveURIAndOpen (SaxErrorContext * saxErrorContext,
		       const char *URL, const char *ID)
{
  JNIEnv *env = saxErrorContext->env;

  jstring hrefString = (*env)->NewStringUTF (env, URL);
  jstring baseString = saxErrorContext->systemId;

  jobject libxmlDocument
    = (*env)->CallObjectMethod (env,
                                saxErrorContext->saxErrorAdapter,
                                saxErrorContext->
                                resolveURIAndOpenMethodID,
                                hrefString,
                                baseString);

  jlong tree
    = (*env)->CallLongMethod (env,
                              libxmlDocument,
                              saxErrorContext->
                              getNativeHandleMethodID);

  (*env)->DeleteLocalRef(env, libxmlDocument);

  if ((*env)->ExceptionOccurred (env))
    {
      /* Report to ErrorAdapter here? */
      return NULL;
    }
  else
    {
      return (xmlDocPtr) (int) tree;
    }
}

xmlParserInputPtr
xmljLoadExternalEntity (const char *URL, const char *ID,
			xmlParserCtxtPtr ctxt)
{
  SaxErrorContext *saxErrorContext = xmljGetThreadContext ();

  JNIEnv *env = saxErrorContext->env;

  jstring hrefString = (*env)->NewStringUTF (env, URL);
  jstring baseString = saxErrorContext->systemId;

  jobject sourceWrapper = (*env)->CallObjectMethod (env,
						    saxErrorContext->
						    saxErrorAdapter,
						    saxErrorContext->
						    resolveURIMethodID,
						    hrefString,
						    baseString);

  (*env)->DeleteLocalRef (env, hrefString);

  if (NULL == sourceWrapper)
    {
      return NULL;
    }
  else
    {
      InputStreamContext *inputContext;
      xmlParserInputBufferPtr inputBuffer;
      xmlParserInputPtr inputStream;

      jobject sourceInputStream = (*env)->CallObjectMethod (env,
							    sourceWrapper,
							    saxErrorContext->
							    getInputStreamMethodID);

      (*env)->DeleteLocalRef (env, sourceWrapper);

      if ((*env)->ExceptionOccurred (env))
	{
	  /* Report to ErrorAdapter */
	  return NULL;
	}

      inputContext =
	xmljNewInputStreamContext (env, sourceInputStream);

      inputBuffer
	= xmlParserInputBufferCreateIO (xmljInputReadCallback,
					xmljInputCloseCallback,
					inputContext,
					XML_CHAR_ENCODING_NONE);

      inputStream = xmlNewInputStream (ctxt);
      if (inputStream == NULL)
	{
	  return (NULL);
	}

      inputStream->filename = NULL;
      inputStream->directory = NULL;
      inputStream->buf = inputBuffer;

      inputStream->base = inputStream->buf->buffer->content;
      inputStream->cur = inputStream->buf->buffer->content;
      inputStream->end = &inputStream->base[inputStream->buf->buffer->use];
      if ((ctxt->directory == NULL) && (inputStream->directory != NULL))
	ctxt->directory =
	  (char *) xmlStrdup ((const xmlChar *) inputStream->directory);
      return (inputStream);
    }
}

/* Key for the thread-specific buffer */
static pthread_key_t thread_context_key;

/* Once-only initialisation of the key */
static pthread_once_t thread_context_once = PTHREAD_ONCE_INIT;

/* Allocate the key */
static void 
thread_context_key_alloc()
{
  pthread_key_create(&thread_context_key, NULL);
}

void 
xmljSetThreadContext(SaxErrorContext *context)
{
  pthread_once(&thread_context_once, thread_context_key_alloc);
  pthread_setspecific(thread_context_key, context);
}

void 
xmljClearThreadContext(void)
{
  pthread_setspecific(thread_context_key, NULL);
}

/* Return the thread-specific buffer */
SaxErrorContext *
xmljGetThreadContext(void)
{
  return (SaxErrorContext *) pthread_getspecific(thread_context_key);
}
