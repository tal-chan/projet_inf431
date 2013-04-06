#include "Element.h"
#include "lookup3.c"

JNIEXPORT signed int JNICALL Java_Element_Hash (JNIEnv *env, jobject obj, jstring javaString) {
	// gets the string in a native form
	const char *nativeString = (*env)->GetStringUTFChars(env, javaString, 0);

	// here we hash the string
	uint32_t hash = hashlittle(nativeString, strlen(nativeString), 0x45526A9F);

	// free memory
	(*env)->ReleaseStringUTFChars(env, javaString, nativeString);

	return (signed int)hash;
}
