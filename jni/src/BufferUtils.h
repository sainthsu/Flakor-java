#include <jni.h>

extern "C"
{
	// ===========================================================
	// com_flakor_game_support.util.BufferUtils
	// ===========================================================
	
	JNIEXPORT void JNICALL Java_com_flakor_game_support_util_BufferUtils_jniPut(JNIEnv *, jclass, jobject, jfloatArray, jint, jint);
	JNIEXPORT jobject JNICALL Java_com_flakor_game_support_util_BufferUtils_jniAllocateDirect(JNIEnv *, jclass, jint);
	JNIEXPORT void JNICALL Java_com_flakor_game_support_util_BufferUtils_jniFreeDirect(JNIEnv *, jclass, jobject);
}