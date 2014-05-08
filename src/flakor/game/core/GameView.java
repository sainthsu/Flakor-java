package flakor.game.core;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.view.SurfaceHolder;

import flakor.game.system.graphics.opengl.ConfigChooser;
//import flakor.game.system.input.EventDispatcher;

public class GameView extends GLSurfaceView
{
    private EngineRenderer renderer;
    private ConfigChooser configChooser;
    //事件调度器
    //private EventDispatcher eventDispatcher;

    public GameView(Context context)
	{
		this(context,null);
		// TODO Auto-generated constructor stub
        //this.eventDispatcher = EventDispatcher.getInstance(context);
	}

	public GameView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		// TODO Auto-generated constructor stub
        this.setEGLContextClientVersion(2);

	}

    /**
     * @see android.view.View#measure(int, int)
     */

    @Override
    protected void onMeasure(final int pWidthMeasureSpec, final int pHeightMeasureSpec)
    {
        if(this.isInEditMode())
        {
            super.onMeasure(pWidthMeasureSpec, pHeightMeasureSpec);
            return;
        }
        this.renderer.engine.getEngineOptions().getResolutionPolicy().onMeasure(this, pWidthMeasureSpec, pHeightMeasureSpec);
    }


    @Override
	public void surfaceChanged(SurfaceHolder holder, int format, int w, int h)
	{
		// TODO Auto-generated method stub
		super.surfaceChanged(holder, format, w, h);
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder)
	{
		// TODO Auto-generated method stub
		super.surfaceCreated(holder);
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder)
	{
		// TODO Auto-generated method stub
		super.surfaceDestroyed(holder);
        renderer.onSurfaceDestroyed();
	}

    // ===========================================================
    // Methods
    // ===========================================================

    public void setMeasuredDimensionProxy(final int pMeasuredWidth, final int pMeasuredHeight)
    {
        this.setMeasuredDimension(pMeasuredWidth, pMeasuredHeight);
    }

    public void setRenderer(final Engine engine, final RendererListener rendererListener)
    {
        if(this.configChooser == null)
        {
            final boolean multiSampling = engine.getEngineOptions().getRenderConfig().isMultiSampling();
            this.configChooser = new ConfigChooser(multiSampling);
        }

        this.setEGLConfigChooser(this.configChooser);

        this.setOnTouchListener(engine);
        this.renderer = new EngineRenderer(engine, this.configChooser, rendererListener);
        this.setRenderer(this.renderer);
    }
	
}
