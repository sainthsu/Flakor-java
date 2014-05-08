package flakor.game.core;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;

import flakor.game.console.config.RenderConfig;
import flakor.game.system.graphics.opengl.ConfigChooser;
import flakor.game.system.graphics.opengl.GLState;
import flakor.game.tool.Debug;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by Saint Hsu on 13-7-11.
 */

public class EngineRenderer implements GLSurfaceView.Renderer
{
    // ===========================================================
    // Constants
    // ===========================================================

    //投影的类型,分为2D,3D等
    public static final int PROJECTION_2D = 1;
    public static final int PROJECTION_3D = 2;
    public static final int PROJECTION_CUSTOM = 3;
    //默认为2D
    public static final int PROJECTION_DEFAULT = PROJECTION_2D;

    // ===========================================================
    // Fields
    // ===========================================================

    final int projection = EngineRenderer.PROJECTION_DEFAULT;
    final Engine engine;
    final ConfigChooser configChooser;
    final boolean multiSampling;
    final RendererListener rendererListener;
    final GLState glState;

    // ===========================================================
    // Constructors
    // ===========================================================

    public EngineRenderer(final Engine engine, final ConfigChooser configChooser, final RendererListener rendererListener)
    {
        this.engine = engine;
        this.configChooser = configChooser;
        this.rendererListener = rendererListener;
        this.glState = new GLState();
        this.multiSampling = this.engine.getEngineOptions().getRenderConfig().isMultiSampling();
    }


    // ===========================================================
    // Methods for/from SuperClass/Interfaces
    // ===========================================================

    @Override
    public void onSurfaceCreated(final GL10 pGL, final EGLConfig pEGLConfig)
    {
        synchronized(GLState.class)
        {
            final RenderConfig renderOptions = this.engine.getEngineOptions().getRenderConfig();
            this.glState.reset(renderOptions, this.configChooser, pEGLConfig);

            // TODO Check if available and make available through EngineOptions-RenderOptions
//			GLES20.glEnable(GLES20.GL_POLYGON_SMOOTH);
//			GLES20.glHint(GLES20.GL_POLYGON_SMOOTH_HINT, GLES20.GL_NICEST);
//			GLES20.glEnable(GLES20.GL_LINE_SMOOTH);
//			GLES20.glHint(GLES20.GL_LINE_SMOOTH_HINT, GLES20.GL_NICEST);
//			GLES20.glEnable(GLES20.GL_POINT_SMOOTH);
//			GLES20.glHint(GLES20.GL_POINT_SMOOTH_HINT, GLES20.GL_NICEST);

            this.glState.disableDepthTest();
            this.glState.enableBlend();
            this.glState.setDitherEnabled(renderOptions.isDithering());

			/* Enabling culling doesn't really make sense, because triangles are never drawn 'backwards' on purpose. */
//			this.mGLState.enableCulling();
//			GLES20.glFrontFace(GLES20.GL_CCW);
//			GLES20.glCullFace(GLES20.GL_BACK);
            //GLES20.glClearColor(.0f, .0f, .0f, 1.0f);
            //GLES20.glClear( GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);
            //GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
            
            if(this.rendererListener != null)
            {
                this.rendererListener.onSurfaceCreated(this.glState);
            }
        }
    }

    @Override
    public void onSurfaceChanged(final GL10 pGL, final int pWidth, final int pHeight)
    {
        this.engine.setSurfaceSize(pWidth, pHeight);
        GLES20.glViewport(0, 0, pWidth, pHeight);
        this.glState.loadProjectionGLMatrixIdentity();

        if(this.rendererListener != null)
        {
            this.rendererListener.onSurfaceChanged(this.glState, pWidth, pHeight);
        }
    }

    public void onSurfaceDestroyed()
    {

    }

    @Override
    public void onDrawFrame(final GL10 pGL)
    {
        synchronized(GLState.class)
        {
            if (this.multiSampling && this.configChooser.isCoverageMultiSampling())
            {
                final int GL_COVERAGE_BUFFER_BIT_NV = 0x8000;
                GLES20.glClear(GL_COVERAGE_BUFFER_BIT_NV);
            }

            try
            {
                this.engine.onDrawFrame(this.glState);
            }
            catch (final InterruptedException e)
            {
                Debug.e("GLThread interrupted!", e);
            }
        }
    }
}
