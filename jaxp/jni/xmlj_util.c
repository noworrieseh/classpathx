/*
 * xmlj_util.c
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
#include "xmlj_util.h"

jstring
xmljNewString(JNIEnv *env,
		const xmlChar *text)
{
  char *s_text;
  
  if (text == NULL)
    return NULL;
  s_text = (char *)text; /* TODO signedness? */
  return (*env)->NewStringUTF(env, s_text);
}

xmlChar *
xmljGetStringChars(JNIEnv *env,
		jstring text)
{
  const char *s_text;
  xmlChar *x_text;
  
  if (text == NULL)
    return NULL;
  
  s_text = (*env)->GetStringUTFChars(env, text, 0);
  x_text = (s_text == NULL) ? NULL : xmlCharStrdup(s_text);
  if (s_text != NULL && x_text == NULL)
  {
    /* TODO raise exception */
  }
  (*env)->ReleaseStringUTFChars(env, text, s_text);
  return x_text;
}

void
xmljThrowDOMException (JNIEnv* env,
    int code,
    const char *message)
{
  jclass cls;
  jmethodID method;
  jthrowable ex;

  cls = (*env)->FindClass(env, "org/w3c/dom/DOMException");
  method = (*env)->GetMethodID(env, cls, "<init>", "(ILjava/lang/String;)V");
  ex = (jthrowable)(*env)->NewObject(env, cls, method, code, message);
  (*env)->Throw(env, ex);
}
