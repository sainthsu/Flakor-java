package flakor.game.system.graphics.opengl;


import android.graphics.Bitmap;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.opengl.Matrix;

import flakor.game.console.config.EngineConfig;
import flakor.game.console.config.RenderConfig;
import flakor.game.tool.Debug;

import java.nio.Buffer;
import java.nio.ByteOrder;

import javax.microedition.khronos.egl.EGLConfig;

public class GLState
{
	// ===========================================================
	// Constants
	// ===========================================================
	public static final int GL_UNPACK_ALIGNMENT_DEFAULT = 4;
	
	// ===========================================================
	// Fields
	// ===========================================================
	
	private final int[] hardwareIDContainer = new int[1];
	
	private String version;
	private String renderer;
	private String extensions;

	private int maximumVertexAttributeCount;
	private int maximumVertexShaderUniformVectorCount;
	private int maximumFragmentShaderUniformVectorCount;
	private int maximumTextureSize;
	private int maximumTextureUnits;

	private int currentArrayBufferID = -1;
	private int currentIndexBufferID = -1;
	private int currentShaderProgramID = -1;
	private final int[] currentBoundTextureIDs = new int[GLES20.GL_TEXTURE31 - GLES20.GL_TEXTURE0];
	private int currentFramebufferID = -1;
	private int currentActiveTextureIndex = 0;

	private int currentSourceBlendMode = -1;
	private int currentDestinationBlendMode = -1;

	private boolean ditherEnabled = true;
	private boolean depthTestEnabled = true;

	private boolean scissorTestEnabled = false;
	private boolean blendEnabled = false;
	private boolean cullingEnabled = false;

	private float lineWidth = 1;

	private final GLMatrixStack modelViewGLMatrixStack = new GLMatrixStack();
	private final GLMatrixStack projectionGLMatrixStack = new GLMatrixStack();

	private final float[] modelViewGLMatrix = new float[GLMatrixStack.GLMATRIX_SIZE];
	private final float[] projectionGLMatrix = new float[GLMatrixStack.GLMATRIX_SIZE];
	private final float[] modelViewProjectionGLMatrix = new float[GLMatrixStack.GLMATRIX_SIZE];

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public String getVersion()
	{
		return this.version;
	}

	public String getRenderer()
	{
		return this.renderer;
	}

	public String getExtensions()
	{
		return this.extensions;
	}

	public int getMaximumVertexAttributeCount()
	{
		return this.maximumVertexAttributeCount;
	}

	public int getMaximumVertexShaderUniformVectorCount()
	{
		return this.maximumVertexShaderUniformVectorCount;
	}

	public int getMaximumFragmentShaderUniformVectorCount()
	{
		return this.maximumFragmentShaderUniformVectorCount;
	}

	public int getMaximumTextureUnits()
	{
		return this.maximumTextureUnits;
	}

	public int getMaximumTextureSize()
	{
		return this.maximumTextureSize;
	}
	
	// ===========================================================
	// Methods
	// ===========================================================
	
	public void reset(final RenderConfig pRenderOptions,final ConfigChooser configChooser,final EGLConfig pEGLConfig)
	{
		this.version = GLES20.glGetString(GLES20.GL_VERSION);
		this.renderer = GLES20.glGetString(GLES20.GL_RENDERER);
		this.extensions = GLES20.glGetString(GLES20.GL_EXTENSIONS);
		
		this.maximumVertexAttributeCount = this.getInteger(GLES20.GL_MAX_VERTEX_ATTRIBS);
		this.maximumVertexShaderUniformVectorCount = this.getInteger(GLES20.GL_MAX_VERTEX_UNIFORM_VECTORS);
		this.maximumFragmentShaderUniformVectorCount = this.getInteger(GLES20.GL_MAX_FRAGMENT_UNIFORM_VECTORS);
		this.maximumTextureUnits = this.getInteger(GLES20.GL_MAX_TEXTURE_IMAGE_UNITS);
		this.maximumTextureSize = this.getInteger(GLES20.GL_MAX_TEXTURE_SIZE);
		
		if(!EngineConfig.DEBUG)
		{
			Debug.d("VERSION: " + this.version);
			Debug.d("RENDERER: " + this.renderer);
			Debug.d("EGLCONFIG: " + EGLConfig.class.getSimpleName() + "(Red=" + configChooser.getRedSize() + ", Green=" + configChooser.getGreenSize() + ", Blue=" + configChooser.getBlueSize() + ", Alpha=" + configChooser.getAlphaSize() + ", Depth=" + configChooser.getDepthSize() + ", Stencil=" + configChooser.getStencilSize() + ")");
			Debug.d("EXTENSIONS: " + this.extensions);
			Debug.d("MAX_VERTEX_ATTRIBS: " + this.maximumVertexAttributeCount);
			Debug.d("MAX_VERTEX_UNIFORM_VECTORS: " + this.maximumVertexShaderUniformVectorCount);
			Debug.d("MAX_FRAGMENT_UNIFORM_VECTORS: " + this.maximumFragmentShaderUniformVectorCount);
			Debug.d("MAX_TEXTURE_IMAGE_UNITS: " + this.maximumTextureUnits);
			Debug.d("MAX_TEXTURE_SIZE: " + this.maximumTextureSize);
		}
		
		this.modelViewGLMatrixStack.reset();
		this.projectionGLMatrixStack.reset();
		
	}
	
	public boolean isScissorTestEnabled() 
	{
		return this.scissorTestEnabled;
	}
	/**
	 * @return the previous state.
	 */
	public boolean enableScissorTest()
	{
		if(this.scissorTestEnabled)
		{
			return true;
		}
		
		this.scissorTestEnabled = true;
		GLES20.glEnable(GLES20.GL_SCISSOR_TEST);
		return false;
	}
	/**
	 * @return the previous state.
	 */
	public boolean disableScissorTest() 
	{
		if(!this.scissorTestEnabled)
		{
			return false;
		}

		this.scissorTestEnabled = false;
		GLES20.glDisable(GLES20.GL_SCISSOR_TEST);
		return true;
	}
	/**
	 * @return the previous state.
	 */
	public boolean setScissorTestEnabled(final boolean enabled) 
	{
		if(enabled) 
		{
			return this.enableScissorTest();
		} 
		else
		{
			return this.disableScissorTest();
		}
	}

	public boolean isBlendEnabled()
	{
		return this.blendEnabled;
	}
	/**
	 * @return the previous state.
	 */
	public boolean enableBlend()
	{
		if(this.blendEnabled) {
			return true;
		}

		this.blendEnabled = true;
		GLES20.glEnable(GLES20.GL_BLEND);
		return false;
	}
	/**
	 * @return the previous state.
	 */
	public boolean disableBlend()
	{
		if(!this.blendEnabled) {
			return false;
		}

		this.blendEnabled = false;
		GLES20.glDisable(GLES20.GL_BLEND);
		return true;
	}

	/**
	 * @return the previous state.
	 */
	public boolean setBlendEnabled(final boolean enabled)
	{
		if(enabled) {
			return this.enableBlend();
		} else {
			return this.disableBlend();
		}
	}

	public boolean isCullingEnabled()
	{
		return this.cullingEnabled;
	}
	/**
	 * @return the previous state.
	 */
	public boolean enableCulling() {
		if(this.cullingEnabled) {
			return true;
		}

		this.cullingEnabled = true;
		GLES20.glEnable(GLES20.GL_CULL_FACE);
		return false;
	}
	/**
	 * @return the previous state.
	 */
	public boolean disableCulling()
	{
		if(!this.cullingEnabled) {
			return false;
		}

		this.cullingEnabled = false;
		GLES20.glDisable(GLES20.GL_CULL_FACE);
		return true;
	}
	/**
	 * @return the previous state.
	 */
	public boolean setCullingEnabled(final boolean enabled)
	{
		if(enabled) {
			return this.enableCulling();
		} else {
			return this.disableCulling();
		}
	}

	public boolean isDitherEnabled() {
		return this.ditherEnabled;
	}
	/**
	 * @return the previous state.
	 */
	public boolean enableDither()
	{
		if(this.ditherEnabled) {
			return true;
		}

		this.ditherEnabled = true;
		GLES20.glEnable(GLES20.GL_DITHER);
		return false;
	}
	/**
	 * @return the previous state.
	 */
	public boolean disableDither()
	{
		if(!this.ditherEnabled) {
			return false;
		}

		this.ditherEnabled = false;
		GLES20.glDisable(GLES20.GL_DITHER);
		return true;
	}
	/**
	 * @return the previous state.
	 */
	public boolean setDitherEnabled(final boolean enabled) 
	{
		if(enabled)
		{
			return this.enableDither();
		} else {
			return this.disableDither();
		}
	}

	public boolean isDepthTestEnabled()
	{
		return this.depthTestEnabled;
	}
	/**
	 * @return the previous state.
	 */
	public boolean enableDepthTest()
	{
		if(this.depthTestEnabled) {
			return true;
		}

		this.depthTestEnabled = true;
		GLES20.glEnable(GLES20.GL_DEPTH_TEST);
		return false;
	}
	/**
	 * @return the previous state.
	 */
	public boolean disableDepthTest()
	{
		if(!this.depthTestEnabled) {
			return false;
		}

		this.depthTestEnabled = false;
		GLES20.glDisable(GLES20.GL_DEPTH_TEST);
		return true;
	}
	/**
	 * @return the previous state.
	 */
	public boolean setDepthTestEnabled(final boolean enabled)
	{
		if(enabled) {
			return this.enableDepthTest();
		} else {
			return this.disableDepthTest();
		}
	}

	public int generateBuffer()
	{
		GLES20.glGenBuffers(1, this.hardwareIDContainer, 0);
		return this.hardwareIDContainer[0];
	}

	public int generateArrayBuffer(final int pSize, final int pUsage)
	{
		GLES20.glGenBuffers(1, this.hardwareIDContainer, 0);
		final int hardwareBufferID = this.hardwareIDContainer[0];

		this.bindArrayBuffer(hardwareBufferID);
		GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, pSize, null, pUsage);
		this.bindArrayBuffer(0);

		return hardwareBufferID;
	}

	public void bindArrayBuffer(final int pHardwareBufferID)
	{
		if(this.currentArrayBufferID != pHardwareBufferID)
        {
			this.currentArrayBufferID = pHardwareBufferID;
			GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, pHardwareBufferID);
		}
	}

	public void deleteArrayBuffer(final int pHardwareBufferID) 
	{
		if(this.currentArrayBufferID == pHardwareBufferID) {
			this.currentArrayBufferID = -1;
		}
		this.hardwareIDContainer[0] = pHardwareBufferID;
		GLES20.glDeleteBuffers(1, this.hardwareIDContainer, 0);
	}

	public int generateIndexBuffer(final int pSize, final int pUsage)
	{
		GLES20.glGenBuffers(1, this.hardwareIDContainer, 0);
		final int hardwareBufferID = this.hardwareIDContainer[0];
		
		this.bindIndexBuffer(hardwareBufferID);
		GLES20.glBufferData(GLES20.GL_ELEMENT_ARRAY_BUFFER, pSize, null, pUsage);
		this.bindIndexBuffer(0);
		
		return hardwareBufferID;
	}

	public void bindIndexBuffer(final int pHardwareBufferID)
	{
		if(this.currentIndexBufferID != pHardwareBufferID)
        {
			this.currentIndexBufferID = pHardwareBufferID;
			GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, pHardwareBufferID);
		}
	}

	public void deleteIndexBuffer(final int pHardwareBufferID)
	{
		if(this.currentIndexBufferID == pHardwareBufferID)
        {
			this.currentIndexBufferID = -1;
		}
		this.hardwareIDContainer[0] = pHardwareBufferID;
		GLES20.glDeleteBuffers(1, this.hardwareIDContainer, 0);
	}

	public int generateFramebuffer()
	{
		GLES20.glGenFramebuffers(1, this.hardwareIDContainer, 0);
		return this.hardwareIDContainer[0];
	}

	public void bindFramebuffer(final int pFramebufferID) 
	{
		GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, pFramebufferID);
	}

	public int getFramebufferStatus()
	{
		return GLES20.glCheckFramebufferStatus(GLES20.GL_FRAMEBUFFER);
	}

	public void checkFramebufferStatus() throws GLFrameBufferException, GLException 
	{
		final int framebufferStatus = this.getFramebufferStatus();
		switch(framebufferStatus)
        {
			case GLES20.GL_FRAMEBUFFER_COMPLETE:
				return;
			case GLES20.GL_FRAMEBUFFER_UNSUPPORTED:
				throw new GLFrameBufferException(framebufferStatus, "GL_FRAMEBUFFER_UNSUPPORTED");
			case GLES20.GL_FRAMEBUFFER_INCOMPLETE_ATTACHMENT:
				throw new GLFrameBufferException(framebufferStatus, "GL_FRAMEBUFFER_INCOMPLETE_ATTACHMENT");
			case GLES20.GL_FRAMEBUFFER_INCOMPLETE_DIMENSIONS:
				throw new GLFrameBufferException(framebufferStatus, "GL_FRAMEBUFFER_INCOMPLETE_DIMENSIONS");
			case GLES20.GL_FRAMEBUFFER_INCOMPLETE_MISSING_ATTACHMENT:
				throw new GLFrameBufferException(framebufferStatus, "GL_FRAMEBUFFER_INCOMPLETE_MISSING_ATTACHMENT");
			case 0:
				this.checkError();
			default:
				throw new GLFrameBufferException(framebufferStatus);
		}
	}

	public int getActiveFramebuffer() 
	{
		return this.getInteger(GLES20.GL_FRAMEBUFFER_BINDING);
	}

	public void deleteFramebuffer(final int pHardwareFramebufferID)
	{
		if(this.currentFramebufferID == pHardwareFramebufferID) {
			this.currentFramebufferID = -1;
		}
		this.hardwareIDContainer[0] = pHardwareFramebufferID;
		GLES20.glDeleteFramebuffers(1, this.hardwareIDContainer, 0);
	}

	public void useProgram(final int pShaderProgramID) 
	{
		if(this.currentShaderProgramID != pShaderProgramID) 
		{
			this.currentShaderProgramID = pShaderProgramID;
			GLES20.glUseProgram(pShaderProgramID);
		}
	}

	public void deleteProgram(final int pShaderProgramID)
	{
		if(this.currentShaderProgramID == pShaderProgramID)
		{
			this.currentShaderProgramID = -1;
		}
		GLES20.glDeleteProgram(pShaderProgramID);
	}

	public int generateTexture()
	{
		//GLES20.glEnable(GLES20.GL_TEXTURE_2D);
		GLES20.glGenTextures(1, this.hardwareIDContainer, 0);
		return this.hardwareIDContainer[0];
	}

	public boolean isTexture(final int pHardwareTextureID)
	{
		return GLES20.glIsTexture(pHardwareTextureID);
	}

	/**
	 * @return {@link GLES20#GL_TEXTURE0} to {@link GLES20#GL_TEXTURE31}
	 */
	public int getActiveTexture()
	{
		return this.currentActiveTextureIndex + GLES20.GL_TEXTURE0;
	}

	/**
	 * @param pGLActiveTexture from {@link GLES20#GL_TEXTURE0} to {@link GLES20#GL_TEXTURE31}.
	 */
	public void activeTexture(final int pGLActiveTexture)
	{
		final int activeTextureIndex = pGLActiveTexture - GLES20.GL_TEXTURE0;
		if(pGLActiveTexture != this.currentActiveTextureIndex) 
		{
			this.currentActiveTextureIndex = activeTextureIndex;
			GLES20.glActiveTexture(pGLActiveTexture);
		}
	}

	/**
	 * @see {@link GLState#forceBindTexture(GLES20, int)}
	 *
	 * @param pHardwareTextureID
	 */
	public void bindTexture(final int pHardwareTextureID)
	{
		if(this.currentBoundTextureIDs[this.currentActiveTextureIndex] != pHardwareTextureID)
		{
			this.currentBoundTextureIDs[this.currentActiveTextureIndex] = pHardwareTextureID;
			GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, pHardwareTextureID);
		}
	}

    public void forceBindTexture(GLES20 gles20,final int hardwareTextureID)
    {

    }

	public void deleteTexture(final int pHardwareTextureID)
	{
		if(this.currentBoundTextureIDs[this.currentActiveTextureIndex] == pHardwareTextureID)
		{
			this.currentBoundTextureIDs[this.currentActiveTextureIndex] = -1;
		}
		this.hardwareIDContainer[0] = pHardwareTextureID;
		GLES20.glDeleteTextures(1, this.hardwareIDContainer, 0);
	}

	public void blendFunction(final int pSourceBlendMode, final int pDestinationBlendMode)
	{
		if(this.currentSourceBlendMode != pSourceBlendMode || this.currentDestinationBlendMode != pDestinationBlendMode)
		{
			this.currentSourceBlendMode = pSourceBlendMode;
			this.currentDestinationBlendMode = pDestinationBlendMode;
			GLES20.glBlendFunc(pSourceBlendMode, pDestinationBlendMode);
		}
	}

	public void lineWidth(final float pLineWidth) 
	{
		if(this.lineWidth  != pLineWidth) 
		{
			this.lineWidth = pLineWidth;
			GLES20.glLineWidth(pLineWidth);
		}
	}

	public void pushModelViewGLMatrix()
	{
		this.modelViewGLMatrixStack.glPushMatrix();
	}

	public void popModelViewGLMatrix() 
	{
		this.modelViewGLMatrixStack.glPopMatrix();
	}

	public void loadModelViewGLMatrixIdentity()
    {
		this.modelViewGLMatrixStack.glLoadIdentity();
	}

	public void translateModelViewGLMatrixf(final float pX, final float pY, final float pZ)
	{
		this.modelViewGLMatrixStack.glTranslatef(pX, pY, pZ);
	}

	public void rotateModelViewGLMatrixf(final float pAngle, final float pX, final float pY, final float pZ)
	{
		this.modelViewGLMatrixStack.glRotatef(pAngle, pX, pY, pZ);
	}

	public void scaleModelViewGLMatrixf(final float pScaleX, final float pScaleY, final int pScaleZ)
	{
		this.modelViewGLMatrixStack.glScalef(pScaleX, pScaleY, pScaleZ);
	}

	public void skewModelViewGLMatrixf(final float pSkewX, final float pSkewY)
	{
		this.modelViewGLMatrixStack.glSkewf(pSkewX, pSkewY);
	}

	public void orthoModelViewGLMatrixf(final float pLeft, final float pRight, final float pBottom, final float pTop, final float pZNear, final float pZFar) 
	{
		this.modelViewGLMatrixStack.glOrthof(pLeft, pRight, pBottom, pTop, pZNear, pZFar);
	}

	public void pushProjectionGLMatrix()
	{
		this.projectionGLMatrixStack.glPushMatrix();
	}

	public void popProjectionGLMatrix()
	{
		this.projectionGLMatrixStack.glPopMatrix();
	}

	public void loadProjectionGLMatrixIdentity() {
		this.projectionGLMatrixStack.glLoadIdentity();
	}

	public void translateProjectionGLMatrixf(final float pX, final float pY, final float pZ) {
		this.projectionGLMatrixStack.glTranslatef(pX, pY, pZ);
	}

	public void rotateProjectionGLMatrixf(final float pAngle, final float pX, final float pY, final float pZ)
	{
		this.projectionGLMatrixStack.glRotatef(pAngle, pX, pY, pZ);
	}

	public void scaleProjectionGLMatrixf(final float pScaleX, final float pScaleY, final float pScaleZ)
	{
		this.projectionGLMatrixStack.glScalef(pScaleX, pScaleY, pScaleZ);
	}

	public void skewProjectionGLMatrixf(final float pSkewX, final float pSkewY)
	{
		this.projectionGLMatrixStack.glSkewf(pSkewX, pSkewY);
	}

	public void orthoProjectionGLMatrixf(final float pLeft, final float pRight, final float pBottom, final float pTop, final float pZNear, final float pZFar)
	{
		this.projectionGLMatrixStack.glOrthof(pLeft, pRight, pBottom, pTop, pZNear, pZFar);
	}

	public float[] getModelViewGLMatrix() 
	{
		this.modelViewGLMatrixStack.getMatrix(this.modelViewGLMatrix);
		return this.modelViewGLMatrix;
	}

	public float[] getProjectionGLMatrix()
	{
		this.projectionGLMatrixStack.getMatrix(this.projectionGLMatrix);
		return this.projectionGLMatrix;
	}

	public float[] getModelViewProjectionGLMatrix()
	{
		Matrix.multiplyMM(this.modelViewProjectionGLMatrix, 0, this.projectionGLMatrixStack.matrixStack,
				this.projectionGLMatrixStack.matrixStackOffset, this.modelViewGLMatrixStack.matrixStack, this.modelViewGLMatrixStack.matrixStackOffset);
		return this.modelViewProjectionGLMatrix;
	}

	public void resetModelViewGLMatrixStack()
	{
		this.modelViewGLMatrixStack.reset();
	}

	public void resetProjectionGLMatrixStack()
	{
		this.projectionGLMatrixStack.reset();
	}

	public void resetGLMatrixStacks()
	{
		this.modelViewGLMatrixStack.reset();
		this.projectionGLMatrixStack.reset();
	}

	/**
	 * <b>Note:</b> does not pre-multiply the alpha channel!</br>
	 * Except that difference, same as: {@link GLUtils#texSubImage2D(int, int, int, int, Bitmap, int, int)}</br>
	 * </br>
	 * See topic: '<a href="http://groups.google.com/group/android-developers/browse_thread/thread/baa6c33e63f82fca">PNG loading that doesn't premultiply alpha?</a>'
	 * @param pBorder
	 */
	public void glTexImage2D(final int pTarget, final int pLevel, final Bitmap pBitmap, final int pBorder, final PixelFormat pPixelFormat)
	{
		final Buffer pixelBuffer = GLHelper.getPixels(pBitmap, pPixelFormat, ByteOrder.BIG_ENDIAN);
		
		GLES20.glTexImage2D(pTarget, pLevel, pPixelFormat.getGLInternalFormat(), pBitmap.getWidth(), pBitmap.getHeight(), pBorder, pPixelFormat.getGLFormat(), pPixelFormat.getGLType(), pixelBuffer);
	}

	/**
	 * <b>Note:</b> does not pre-multiply the alpha channel!</br>
	 * Except that difference, same as: {@link GLUtils#texSubImage2D(int, int, int, int, Bitmap, int, int)}</br>
	 * </br>
	 * See topic: '<a href="http://groups.google.com/group/android-developers/browse_thread/thread/baa6c33e63f82fca">PNG loading that doesn't premultiply alpha?</a>'
	 */
	public void glTexSubImage2D(final int pTarget, final int pLevel, final int pX, final int pY, final Bitmap pBitmap, final PixelFormat pPixelFormat)
	{
		final Buffer pixelBuffer = GLHelper.getPixels(pBitmap, pPixelFormat, ByteOrder.BIG_ENDIAN);

		GLES20.glTexSubImage2D(pTarget, pLevel, pX, pY, pBitmap.getWidth(), pBitmap.getHeight(), pPixelFormat.getGLFormat(), pPixelFormat.getGLType(), pixelBuffer);
	}

	
	/**
	 * Tells the OpenGL driver to send all pending commands to the GPU immediately.
	 *
	 * @see {@link GLState#finish()},
	 * 		{@link flakor.game.system.graphics.opengl.Texture.RenderTexture#end(GLState, boolean, boolean)}.
	 */
	public void flush()
	{
		GLES20.glFlush();
	}
	
	public void finish()
	{
		GLES20.glFinish();
	}
	
	public int getInteger(final int pAttribute)
	{
		GLES20.glGetIntegerv(pAttribute, this.hardwareIDContainer, 0);
		return this.hardwareIDContainer[0];
	}
	
	public int getError()
	{
		return GLES20.glGetError();
	}

	public void checkError() throws GLException
	{
		final int error = GLES20.glGetError();
		if(error != GLES20.GL_NO_ERROR)
		{
			throw new GLException(error);
		}
	}

	public void clearError()
	{
		GLES20.glGetError();
	}

}
