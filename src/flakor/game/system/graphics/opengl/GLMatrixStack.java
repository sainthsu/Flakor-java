package flakor.game.system.graphics.opengl;


import android.opengl.Matrix;

import flakor.game.support.math.MathConstants;
import flakor.game.tool.FlakorRuntimeException;


/**
 * 4x4 openGL matrix
 */
public class GLMatrixStack
{
	public static final int GLMATRIXSTACK_DEPTH_MAX = 32;
	public static final int GLMATRIX_SIZE = 16;

	private static final int GLMATRIXSTACKOFFSET_UNDERFLOW = -1 * GLMatrixStack.GLMATRIX_SIZE;
	private static final int GLMATRIXSTACKOFFSET_OVERFLOW = GLMatrixStack.GLMATRIXSTACK_DEPTH_MAX * GLMatrixStack.GLMATRIX_SIZE;

	int matrixStackOffset;
	final float[] matrixStack = new float[GLMatrixStack.GLMATRIXSTACK_DEPTH_MAX * GLMatrixStack.GLMATRIX_SIZE];
	private final float[] temp = new float[2 * GLMatrixStack.GLMATRIX_SIZE];
	
	public GLMatrixStack()
	{
		this.glLoadIdentity();
	}
	
	public void getMatrix(final float[] matrix)
	{
		System.arraycopy(this.matrixStack, this.matrixStackOffset, matrix, 0, GLMatrixStack.GLMATRIX_SIZE);
	}
	// ===========================================================
	// Methods
	// ===========================================================

	public void glLoadIdentity()
	{
			Matrix.setIdentityM(this.matrixStack, this.matrixStackOffset);
	}
	
	public void glTranslatef(final float pX, final float pY, final float pZ)
	{
		Matrix.translateM(this.matrixStack, this.matrixStackOffset, pX, pY, pZ);
	}

	public void glRotatef(final float pAngle, final float pX, final float pY, final float pZ)
	{
		Matrix.setRotateM(this.temp, 0, pAngle, pX, pY, pZ);
		System.arraycopy(this.matrixStack, this.matrixStackOffset, this.temp, GLMatrixStack.GLMATRIX_SIZE, GLMatrixStack.GLMATRIX_SIZE);
		Matrix.multiplyMM(this.matrixStack, this.matrixStackOffset, this.temp, GLMatrixStack.GLMATRIX_SIZE, this.temp, 0);
	}

	public void glScalef(final float scaleX, final float scaleY, final float scaleZ)
	{
		Matrix.scaleM(this.matrixStack, this.matrixStackOffset, scaleX, scaleY, scaleZ);
	}

	public void glSkewf(final float pSkewX, final float pSkewY)
	{
		GLMatrixStack.setSkewM(this.temp, 0, pSkewX, pSkewY);
		System.arraycopy(this.matrixStack, this.matrixStackOffset, this.temp, GLMatrixStack.GLMATRIX_SIZE, GLMatrixStack.GLMATRIX_SIZE);
		Matrix.multiplyMM(this.matrixStack, this.matrixStackOffset, this.temp, GLMatrixStack.GLMATRIX_SIZE, this.temp, 0);
	}

	public void glOrthof(final float pLeft, final float pRight, final float pBottom, final float pTop, final float pZNear, final float pZFar)
	{
		Matrix.orthoM(this.matrixStack, this.matrixStackOffset, pLeft, pRight, pBottom, pTop, pZNear, pZFar);
	}

	public void glPushMatrix() throws GLMatrixStackOverflowException
	{
		if (this.matrixStackOffset + GLMatrixStack.GLMATRIX_SIZE >= GLMatrixStack.GLMATRIXSTACKOFFSET_OVERFLOW)
        {
			throw new GLMatrixStackOverflowException();
		}

		System.arraycopy(this.matrixStack, this.matrixStackOffset, this.matrixStack, this.matrixStackOffset + GLMatrixStack.GLMATRIX_SIZE, GLMatrixStack.GLMATRIX_SIZE);
		this.matrixStackOffset += GLMatrixStack.GLMATRIX_SIZE;
	}

	public void glPopMatrix()
	{
		if (this.matrixStackOffset - GLMatrixStack.GLMATRIX_SIZE <= GLMatrixStack.GLMATRIXSTACKOFFSET_UNDERFLOW) {
			throw new GLMatrixStackUnderflowException();
		}

		this.matrixStackOffset -= GLMatrixStack.GLMATRIX_SIZE;
	}
	
	private static void setSkewM(final float[] pMatrixStack, final int pOffset, final float pSkewX, final float pSkewY)
	{
		pMatrixStack[pOffset + 0] = 1.0f;
		pMatrixStack[pOffset + 1] = (float) Math.tan(-MathConstants.DEG_TO_RAD * pSkewY);
		pMatrixStack[pOffset + 2] = 0.0f;
		pMatrixStack[pOffset + 3] = 0.0f;

		pMatrixStack[pOffset + 4] = (float) Math.tan(-MathConstants.DEG_TO_RAD * pSkewX);
		pMatrixStack[pOffset + 5] = 1.0f;
		pMatrixStack[pOffset + 6] = 0.0f;
		pMatrixStack[pOffset + 7] = 0.0f;

		pMatrixStack[pOffset + 8] = 0.0f;
		pMatrixStack[pOffset + 9] = 0.0f;
		pMatrixStack[pOffset + 10] = 1.0f;
		pMatrixStack[pOffset + 11] = 0.0f;

		pMatrixStack[pOffset + 12] = 0.0f;
		pMatrixStack[pOffset + 13] = 0.0f;
		pMatrixStack[pOffset + 14] = 0.0f;
		pMatrixStack[pOffset + 15] = 1.0f;
	}
	public void reset()
	{
		this.matrixStackOffset = 0;
		this.glLoadIdentity();
	}
	
	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

	public static class GLMatrixStackOverflowException extends FlakorRuntimeException
	{
		private static final long serialVersionUID = 6823405706868628377L;

		public GLMatrixStackOverflowException()
		{
			
		}
			
	}

	public static class GLMatrixStackUnderflowException extends FlakorRuntimeException
	{
		private static final long serialVersionUID = -8053267924491130803L;

		public GLMatrixStackUnderflowException()
		{

		}

	}
}
