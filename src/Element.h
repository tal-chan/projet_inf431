#include <jni.h>
#include <stdio.h>
#include <string.h>

#ifndef ELEMENT_CDMB
#define ELEMENT_CDMB

JNIEXPORT signed int JNICALL Java_Element_Hash (JNIEnv *env, jobject obj, jstring javaString);

#endif
