/*
 * Layer.java
 * Created on 8/24/13 8:02 PM
 *
 * ver0.0.1beta 8/24/13 saint
 * Copyright (c) 2013 Saint Hsu All Rights Reserved.
 */

package flakor.game.core.layer;

import android.util.SparseArray;

import flakor.game.core.camera.Camera;
import flakor.game.core.element.Color;
import flakor.game.core.entity.Constants;
import flakor.game.core.entity.Entity;
import flakor.game.core.entity.EntityInterface;
import flakor.game.core.entity.RunnableHandler;
import flakor.game.core.element.Size;
import flakor.game.core.scene.Background;
import flakor.game.core.scene.BackgroundInterface;
import flakor.game.core.scene.Scene;
import flakor.game.support.math.SmartList;
import flakor.game.system.graphics.opengl.GLState;
import flakor.game.system.input.touch.OnAreaTouchListener;
import flakor.game.system.input.touch.OnLayerTouchListener;
import flakor.game.system.input.touch.TouchAreaInterface;
import flakor.game.system.input.touch.TouchEvent;

/**
 * Created by saint on 8/24/13.
 */
public class Layer extends Entity
{
    // ===========================================================
    // Constants
    // ===========================================================

    private static final int TOUCHAREAS_CAPACITY_DEFAULT = 4;

    // ===========================================================
    // Fields
    // ===========================================================

    private float secondsElapsedTotal;

    //是否允许触发某些事件
    protected boolean touchEnabled;
    protected boolean keyEnabled;
    protected boolean accelerometerEnabled;

    protected SmartList<TouchAreaInterface> touchAreas = new SmartList<TouchAreaInterface>(Layer.TOUCHAREAS_CAPACITY_DEFAULT);

    private final RunnableHandler runnableHandler = new RunnableHandler();

    private OnLayerTouchListener onLayerTouchListener;

    private OnAreaTouchListener onAreaTouchListener;

    private BackgroundInterface background = new Background(Color.BLACK);
    private boolean backgroundEnabled = true;

    private boolean onAreaTouchTraversalBackToFront = true;

    private boolean touchAreaBindingOnActionDownEnabled = false;
    private boolean touchAreaBindingOnActionMoveEnabled = false;
    private final SparseArray<TouchAreaInterface> touchAreaBindings = new SparseArray<TouchAreaInterface>();
    private boolean onLayerTouchListenerBindingOnActionDownEnabled = false;
    private final SparseArray<OnLayerTouchListener> onLayerTouchListenerBindings = new SparseArray<OnLayerTouchListener>();

    // ===========================================================
    // Constructors
    // ===========================================================

    public Layer()
    {
        this(new Size(1,1));
    }

    public Layer(Size size)
    {
        //设置锚点
        //setAnchorPercent(0.5F, 0.5F);
        //设置尺寸
    	super(0, 0);
        setContentSize(size);
        setRelativeAnchorPoint(false);
        //默认不触发事件
        this.touchEnabled = false;
        this.keyEnabled = false;
        this.accelerometerEnabled = false;
    }
    // ===========================================================
    // Getter & Setter
    // ===========================================================

    public boolean isTouchEnabled()
    {
        return this.touchEnabled;
    }

    //设置是否允许触摸事件
    public void setTouchEnabled(boolean enabled)
    {
        if (this.touchEnabled != enabled)
        {
            this.touchEnabled = enabled;
        }
    }

    //设置是否允许按键事件
    public void setKeyEnabled(boolean enabled)
    {
        if (this.keyEnabled != enabled)
        {
            this.keyEnabled = enabled;
        }
    }

    public boolean isKeyEnabled()
    {
        return this.keyEnabled;
    }

    public boolean isAccelerometerEnabled()
    {
        return this.accelerometerEnabled;
    }

    //设置是否允许加速器事件
    public void setAccelerometerEnabled(boolean enabled)
    {
        if (this.accelerometerEnabled != enabled)
        {
            this.accelerometerEnabled = enabled;

        }
    }

    public float getSecondsElapsedTotal()
    {
        return this.secondsElapsedTotal;
    }

    public BackgroundInterface getBackground()
    {
        return this.background;
    }

    public void setBackground(final BackgroundInterface background)
    {
        this.background = background;
    }

    public boolean isBackgroundEnabled()
    {
        return this.backgroundEnabled;
    }

    public void setBackgroundEnabled(final boolean enabled)
    {
        this.backgroundEnabled  = enabled;
    }

    public void setOnLayerTouchListener(final OnLayerTouchListener onLayerTouchListener)
    {
        this.onLayerTouchListener = onLayerTouchListener;
        this.touchEnabled=true;
    }

    public OnLayerTouchListener getOnLayerTouchListener()
    {
        return this.onLayerTouchListener;
    }

    public boolean hasOnLayerTouchListener()
    {
        return this.onLayerTouchListener != null;
    }

    public void setOnAreaTouchListener(final OnAreaTouchListener onAreaTouchListener)
    {
        this.onAreaTouchListener = onAreaTouchListener;
    }

    public OnAreaTouchListener getOnAreaTouchListener()
    {
        return this.onAreaTouchListener;
    }

    public boolean hasOnAreaTouchListener()
    {
        return this.onAreaTouchListener != null;
    }

    public void setOnAreaTouchTraversalBackToFront()
    {
        this.onAreaTouchTraversalBackToFront = true;
    }

    public void setOnAreaTouchTraversalFrontToBack()
    {
        this.onAreaTouchTraversalBackToFront = false;
    }

    public boolean isTouchAreaBindingOnActionDownEnabled()
    {
        return this.touchAreaBindingOnActionDownEnabled;
    }

    public boolean isTouchAreaBindingOnActionMoveEnabled()
    {
        return this.touchAreaBindingOnActionMoveEnabled;
    }

    /**
     * Enable or disable the binding of TouchAreas to PointerIDs (fingers).
     * When enabled: TouchAreas get bound to a PointerID (finger) when returning true in
     * {@link flakor.game.core.geometry.ShapeInterface#onAreaTouched(TouchEvent, float, float)} or
     * {@link OnAreaTouchListener#onAreaTouched(TouchEvent, TouchAreaInterface, float, float)}
     * with {@link TouchEvent#ACTION_DOWN}, they will receive all subsequent {@link TouchEvent}s
     * that are made with the same PointerID (finger)
     * <b>even if the {@link TouchEvent} is outside of the actual {@link TouchAreaInterface}</b>!
     *
     * @param touchAreaBindingOnActionDownEnabled
     */
    public void setTouchAreaBindingOnActionDownEnabled(final boolean touchAreaBindingOnActionDownEnabled)
    {
        if(this.touchAreaBindingOnActionDownEnabled && !touchAreaBindingOnActionDownEnabled)
        {
            this.touchAreaBindings.clear();
        }
        this.touchAreaBindingOnActionDownEnabled = touchAreaBindingOnActionDownEnabled;
    }

    /**
     * Enable or disable the binding of TouchAreas to PointerIDs (fingers).
     * When enabled: TouchAreas get bound to a PointerID (finger) when returning true in
     * {@link flakor.game.core.geometry.ShapeInterface#onAreaTouched(TouchEvent, float, float)} or
     * {@link OnAreaTouchListener#onAreaTouched(TouchEvent, TouchAreaInterface, float, float)}
     * with {@link TouchEvent#ACTION_MOVE}, they will receive all subsequent {@link TouchEvent}s
     * that are made with the same PointerID (finger)
     * <b>even if the {@link TouchEvent} is outside of the actual {@link TouchAreaInterface}</b>!
     *
     * @param touchAreaBindingOnActionMoveEnabled
     */
    public void setTouchAreaBindingOnActionMoveEnabled(final boolean touchAreaBindingOnActionMoveEnabled)
    {
        if(this.touchAreaBindingOnActionMoveEnabled && !touchAreaBindingOnActionMoveEnabled)
        {
            this.touchAreaBindings.clear();
        }
        this.touchAreaBindingOnActionMoveEnabled = touchAreaBindingOnActionMoveEnabled;
    }

    public boolean isOnLayerTouchListenerBindingOnActionDownEnabled()
    {
        return this.onLayerTouchListenerBindingOnActionDownEnabled;
    }

    /**
     * Enable or disable the binding of TouchAreas to PointerIDs (fingers).
     * When enabled: The OnSceneTouchListener gets bound to a PointerID (finger) when returning true in
     * {@link flakor.game.core.geometry.Shape#onAreaTouched(TouchEvent, float, float)} or
     * {@link OnAreaTouchListener#onAreaTouched(TouchEvent, TouchAreaInterface, float, float)}
     * with {@link TouchEvent#ACTION_DOWN}, it will receive all subsequent {@link TouchEvent}s
     * that are made with the same PointerID (finger)
     * <b>even if the {@link TouchEvent} is would belong to an overlaying {@link TouchAreaInterface}</b>!
     *
     * @param onLayerTouchListenerBindingOnActionDownEnabled
     */
    public void setOnLayerTouchListenerBindingOnActionDownEnabled(final boolean onLayerTouchListenerBindingOnActionDownEnabled)
    {
        if(this.onLayerTouchListenerBindingOnActionDownEnabled && !onLayerTouchListenerBindingOnActionDownEnabled)
        {
            this.onLayerTouchListenerBindings.clear();
        }
        this.onLayerTouchListenerBindingOnActionDownEnabled = onLayerTouchListenerBindingOnActionDownEnabled;
    }

    // ===========================================================
    // Methods for/from SuperClass/Interfaces
    // ===========================================================

    @Override
    protected void onManagedDraw(final GLState glState, final Camera camera)
    {

            if(this.backgroundEnabled)
            {
                glState.pushProjectionGLMatrix();

                camera.onApplyLayerBackgroundMatrix(glState);
                glState.loadModelViewGLMatrixIdentity();

                this.background.onDraw(glState, camera);

                glState.popProjectionGLMatrix();
            }

            {    
            	glState.pushProjectionGLMatrix();
                camera.onApplySceneMatrix(glState);
                glState.loadModelViewGLMatrixIdentity();
                super.onManagedDraw(glState, camera);
                glState.popProjectionGLMatrix();
            }

    }

    @Override
    protected void onManagedUpdate(final float secondsElapsed)
    {
        this.secondsElapsedTotal += secondsElapsed;

        this.runnableHandler.onUpdate(secondsElapsed);


        this.background.onUpdate(secondsElapsed);
        super.onManagedUpdate(secondsElapsed);

    }

    public boolean onLayerTouchEvent(final TouchEvent sceneTouchEvent)
    {
        final int action = sceneTouchEvent.getAction();
        final boolean isActionDown = sceneTouchEvent.isActionDown();
        final boolean isActionMove = sceneTouchEvent.isActionMove();

        if(!isActionDown)
        {
            if(this.onLayerTouchListenerBindingOnActionDownEnabled)
            {
                final OnLayerTouchListener boundOnSceneTouchListener = this.onLayerTouchListenerBindings.get(sceneTouchEvent.getPointerID());
                if (boundOnSceneTouchListener != null)
                {
					/* Check if boundTouchArea needs to be removed. */
                    switch(action)
                    {
                        case TouchEvent.ACTION_UP:
                        case TouchEvent.ACTION_CANCEL:
                            this.onLayerTouchListenerBindings.remove(sceneTouchEvent.getPointerID());
                    }
                    final Boolean handled = this.onLayerTouchListener.onLayerTouchEvent(this, sceneTouchEvent);
                    if(handled != null && handled)
                    {
                        return true;
                    }
                }
            }
            if(this.touchAreaBindingOnActionDownEnabled)
            {
                final SparseArray<TouchAreaInterface> touchAreaBindings = this.touchAreaBindings;
                final TouchAreaInterface boundTouchArea = touchAreaBindings.get(sceneTouchEvent.getPointerID());
				/* In the case a TouchAreaInterface has been bound to this PointerID,
				 * we'll pass this TouchEvent to the same TouchAreaInterface. */
                if(boundTouchArea != null)
                {
                    final float sceneTouchEventX = sceneTouchEvent.getX();
                    final float sceneTouchEventY = sceneTouchEvent.getY();

					/* Check if boundTouchArea needs to be removed. */
                    switch(action)
                    {
                        case TouchEvent.ACTION_UP:
                        case TouchEvent.ACTION_CANCEL:
                            touchAreaBindings.remove(sceneTouchEvent.getPointerID());
                    }
                    final Boolean handled = this.onAreaTouchEvent(sceneTouchEvent, sceneTouchEventX, sceneTouchEventY, boundTouchArea);
                    if(handled != null && handled)
                    {
                        return true;
                    }
                }
            }
        }

        final float sceneTouchEventX = sceneTouchEvent.getX();
        final float sceneTouchEventY = sceneTouchEvent.getY();

        final SmartList<TouchAreaInterface> touchAreas = this.touchAreas;
        if(touchAreas != null)
        {
            final int touchAreaCount = touchAreas.size();
            if(touchAreaCount > 0)
            {
                if(this.onAreaTouchTraversalBackToFront)
                {
                /* Back to Front. */
                    for(int i = 0; i < touchAreaCount; i++)
                    {
                        final TouchAreaInterface touchArea = touchAreas.get(i);
                        if(touchArea.contains(sceneTouchEventX, sceneTouchEventY))
                        {
                            final Boolean handled = this.onAreaTouchEvent(sceneTouchEvent, sceneTouchEventX, sceneTouchEventY, touchArea);
                            if(handled != null && handled)
                            {
								/* If binding of TouchAreas is enabled and this is an ACTION_DOWN event,
								 *  bind this TouchArea to the PointerID. */
                                if((this.touchAreaBindingOnActionDownEnabled && isActionDown) || (this.touchAreaBindingOnActionMoveEnabled && isActionMove))
                                {
                                    this.touchAreaBindings.put(sceneTouchEvent.getPointerID(), touchArea);
                                }
                                return true;
                            }
                        }
                    }
                }
                else
                { /* Front to back. */
                    for(int i = touchAreaCount - 1; i >= 0; i--)
                    {
                        final TouchAreaInterface touchArea = touchAreas.get(i);
                        if(touchArea.contains(sceneTouchEventX, sceneTouchEventY))
                        {
                            final Boolean handled = this.onAreaTouchEvent(sceneTouchEvent, sceneTouchEventX, sceneTouchEventY, touchArea);
                            if(handled != null && handled) {
								/* If binding of ITouchAreas is enabled and this is an ACTION_DOWN event,
								 *  bind this ITouchArea to the PointerID. */
                                if((this.touchAreaBindingOnActionDownEnabled && isActionDown) || (this.touchAreaBindingOnActionMoveEnabled && isActionMove))
                                {
                                    this.touchAreaBindings.put(sceneTouchEvent.getPointerID(), touchArea);
                                }
                                return true;
                            }
                        }
                    }
                }
            }
        }

		/* If no area was touched, the Layer itself was touched as a fallback. */
        if(this.onLayerTouchListener != null&&this.touchEnabled)
        {
            final Boolean handled = this.onLayerTouchListener.onLayerTouchEvent(this, sceneTouchEvent);
            if(handled != null && handled)
            {
				/* If binding of TouchAreas is enabled and this is an ACTION_DOWN event,
				 *  bind the active OnSceneTouchListener to the PointerID. */
                if(this.onLayerTouchListenerBindingOnActionDownEnabled && isActionDown)
                {
                    this.onLayerTouchListenerBindings.put(sceneTouchEvent.getPointerID(), this.onLayerTouchListener);
                }
                return true;
            }
            else
            {
                return false;
            }
        }
        else
        {
            return false;
        }
    }

    private Boolean onAreaTouchEvent(final TouchEvent pSceneTouchEvent, final float sceneTouchEventX, final float sceneTouchEventY, final TouchAreaInterface touchArea)
    {
        final float[] touchAreaLocalCoordinates = touchArea.convertSceneToLocalCoordinates(sceneTouchEventX, sceneTouchEventY);
        final float touchAreaLocalX = touchAreaLocalCoordinates[Constants.VERTEX_INDEX_X];
        final float touchAreaLocalY = touchAreaLocalCoordinates[Constants.VERTEX_INDEX_Y];

        final boolean handledSelf = touchArea.onAreaTouched(pSceneTouchEvent, touchAreaLocalX, touchAreaLocalY);
        if(handledSelf) {
            return Boolean.TRUE;
        } else if(this.onAreaTouchListener != null) {
            return this.onAreaTouchListener.onAreaTouched(pSceneTouchEvent, touchArea, touchAreaLocalX, touchAreaLocalY);
        } else {
            return null;
        }
    }

    @Override
    public void reset()
    {
        super.reset();
    }

    @Override
	public void setParent(EntityInterface entity)
    {
		// TODO Auto-generated method stub
    	if(entity instanceof Scene)
    		super.setParent(entity);
	}

	public void setParent(final Scene scene)
    {
		super.setParent(scene);
    }

    // ===========================================================
    // Methods
    // ===========================================================

    public void postRunnable(final Runnable pRunnable)
    {
        this.runnableHandler.postRunnable(pRunnable);
    }

    public void registerTouchArea(final TouchAreaInterface pTouchArea)
    {
        this.touchAreas.add(pTouchArea);
    }

    public boolean unregisterTouchArea(final TouchAreaInterface pTouchArea) {
        return this.touchAreas.remove(pTouchArea);
    }

    public boolean unregisterTouchAreas(final TouchAreaInterface.TouchAreaMatcher touchAreaMatcher)
    {
        return this.touchAreas.removeAll(touchAreaMatcher);
    }

    public void clearTouchAreas()
    {
        this.touchAreas.clear();
    }

    public SmartList<TouchAreaInterface> getTouchAreas()
    {
        return this.touchAreas;
    }

    /*
    //注册和卸载各种事件
    protected void registerTouchHandler()
    {
        EventDispatcher.getInstance().addTouchHandler(this);
    }

    protected void unregisterTouchHandler()
    {
        EventDispatcher.getInstance().removeTouchHandler(this);
    }

    protected void registerKeyHandler()
    {
        EventDispatcher.getInstance().addKeyHandler(this);
    }

    protected void unregisterKeyHandler()
    {
        EventDispatcher.getInstance().removeKeyHandler(this);
    }

    protected void registerAccelerometerHandler()
    {
        EventDispatcher.getInstance().addAccelHandler(this);
    }
    protected void unregisterAccelerometerHandler()
    {
        EventDispatcher.getInstance().removeAccelHandler(this);
    }

    //当图层被激活时
    public void onEnter()
    {
        if (this.mTouchEnabled)
        {
            registerTouchHandler();
        }
        if (this.mKeyEnabled)
        {
            registerKeyHandler();
        }
        super.onEnter();
        if (this.mAccelerometerEnabled)
            registerAccelerometerHandler();
    }
    //当图层退出，销毁时
    public void onExit()
    {
        if (this.touchEnabled)
        {
            unregisterTouchHandler();
        }
        if (this.keyEnabled)
        {
            unregisterKeyHandler();
        }
        if (this.accelerometerEnabled)
        {
            unregisterAccelerometerHandler();
        }
        super.onExit();
    }

    //具体事件处理函数
    public boolean touchesBegan(MotionEvent event)
    {
        return false;
    }

    public boolean touchesMoved(MotionEvent event)
    {
        return false;
    }

    public boolean touchesEnded(MotionEvent event)
    {
        return false;
    }

    public boolean touchesCancelled(MotionEvent event)
    {
        return false;
    }
    public void accelerometerChanged(float accelX, float accelY, float accelZ)
    {
    }

    public boolean keyDown(KeyEvent event)
    {
        return false;
    }

    public boolean keyUp(KeyEvent event)
    {
        return false;
    }
    */
}
