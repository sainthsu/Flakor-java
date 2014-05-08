#include <jni.h>
#include <GLES2/gl2.h>

// ===========================================================
// com_flakor_system_graphics.opengl.GLES20Fix
// ===========================================================

void Java_com_flakor_system_graphics_opengl_GLES20Fix_glVertexAttribPointer (JNIEnv *env, jclass c, jint index, jint size, jint type, jboolean normalized, jint stride, jint offset)
{
	glVertexAttribPointer(index, size, type, normalized, stride, (void*) offset);
}

void Java_com_flakor_system_graphics_opengl_GLES20Fix_glDrawElements (JNIEnv *env, jclass c, jint mode, jint count, jint type, jint offset)
{
	glDrawElements(mode, count, type, (void*) offset);
}