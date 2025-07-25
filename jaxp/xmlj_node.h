/*
 * xmlj_node.h
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
#ifndef XMLJ_NODE_H
#define XMLJ_NODE_H

#include <jni.h>
#include <libxml/tree.h>

/* -- Utility method definitions -- */

/*
 * Returns the node for the given Java node instance
 */
xmlNodePtr xmljGetNodeID (JNIEnv *, jobject);

/*
 * Returns the Java node instance for the given node
 */
jobject xmljGetNodeInstance (JNIEnv *, xmlNodePtr);

/*
 * Frees the specified document pointer,
 * releasing all its nodes from the cache.
 */
void xmljFreeDoc (JNIEnv *, xmlDocPtr);

/*
 * Match a node name
 */
int xmljMatch (const xmlChar *, xmlNodePtr);

/*
 * Match a node name and namespace
 */
int xmljMatchNS (const xmlChar *, const xmlChar *, xmlNodePtr);

#endif /* !defined XMLJ_NODE_H */
