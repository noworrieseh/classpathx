#include <jni.h>
#include <libxml/tree.h>

jstring xmljNewString(JNIEnv *env, const xmlChar *text);

const xmlChar * xmljGetStringChars(JNIEnv *env, jstring text);

void xmljReleaseStringChars(JNIEnv *env, jstring jtext, const xmlChar *xtext);

void xmljThrowDOMException (JNIEnv* env, int code, const char *message);

