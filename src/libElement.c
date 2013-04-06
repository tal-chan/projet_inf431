#include <stdio.h>
#include "Element.h"

JNIEXPORT void JNICALL
Java_Element_Hash (JNIEnv *end, jobject obj) {
	printf ("test\n");
	return;
}
