#include <jni.h>
#include <libxml/tree.h>

#ifndef _Included_node_h
#define _Included_node_h

// Utility method definitions

/*
 * Returns the node for the given Java node instance
 */
xmlNodePtr xmljGetNodeID (JNIEnv *, jobject);

/*
 * Returns the Java node instance for the given node
 */
jobject xmljGetNodeInstance (JNIEnv *, xmlNodePtr);

/*
 * Returns the Java class name for the given node type
 */
char *xmljNodeClass (xmlElementType);

/*
 * Match a node name
 */
int xmljMatch (const xmlChar *, xmlNodePtr);

/*
 * Match a node name and namespace
 */
int xmljMatchNS (const xmlChar *, const xmlChar *, xmlNodePtr);

#endif
