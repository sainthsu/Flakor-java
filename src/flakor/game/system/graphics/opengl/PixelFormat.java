package flakor.game.system.graphics.opengl;

import android.opengl.GLES20;

/**
 * 像素颜色格式
 */
public enum PixelFormat
{
	// ===========================================================
	// Elements
	// ===========================================================

	UNDEFINED(-1, -1, -1, -1),
	RGBA_4444(GLES20.GL_RGBA, GLES20.GL_RGBA, GLES20.GL_UNSIGNED_SHORT_4_4_4_4, 16),
	RGBA_5551(GLES20.GL_RGB, GLES20.GL_RGBA, GLES20.GL_UNSIGNED_SHORT_5_5_5_1, 16),
	RGBA_8888(GLES20.GL_RGBA, GLES20.GL_RGBA, GLES20.GL_UNSIGNED_BYTE, 32),
	RGB_565(GLES20.GL_RGB, GLES20.GL_RGB, GLES20.GL_UNSIGNED_SHORT_5_6_5, 16),
	A_8(GLES20.GL_ALPHA, GLES20.GL_ALPHA, GLES20.GL_UNSIGNED_BYTE, 8),
	I_8(GLES20.GL_LUMINANCE, GLES20.GL_LUMINANCE, GLES20.GL_UNSIGNED_BYTE, 8),
	AI_88(GLES20.GL_LUMINANCE_ALPHA, GLES20.GL_LUMINANCE_ALPHA, GLES20.GL_UNSIGNED_BYTE, 16);

	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private final int GLInternalFormat;
	private final int GLFormat;
	private final int GLType;
	private final int BitsPerPixel;

	// ===========================================================
	// Constructors
	// ===========================================================

	private PixelFormat(final int pGLInternalFormat, final int pGLFormat, final int pGLType, final int pBitsPerPixel)
    {
		this.GLInternalFormat = pGLInternalFormat;
		this.GLFormat= pGLFormat;
		this.GLType = pGLType;
		this.BitsPerPixel = pBitsPerPixel;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public int getGLInternalFormat()
    {
		return this.GLInternalFormat;
	}

	public int getGLFormat()
    {
		return this.GLFormat;
	}

	public int getGLType()
    {
		return this.GLType;
	}

	public int getBitsPerPixel()
    {
		return this.BitsPerPixel;
	}

}