package flakor.game.system.input.touch;

import android.view.MotionEvent;

import flakor.game.core.entity.RunnablePoolItem;
import flakor.game.core.entity.RunnablePoolUpdateHandler;

/**
 * Created by Saint Hsu on 13-7-11.
 */
public abstract class BaseTouchController implements TouchControllerInterface
{	// ===========================================================
    // Fields
    // ===========================================================

    private TouchEventCallback mTouchEventCallback;

    private final RunnablePoolUpdateHandler<TouchEventRunnablePoolItem> mTouchEventRunnablePoolUpdateHandler = new RunnablePoolUpdateHandler<TouchEventRunnablePoolItem>()
    {
        @Override
        protected TouchEventRunnablePoolItem onAllocatePoolItem()
        {
            return new TouchEventRunnablePoolItem();
        }
    };

    // ===========================================================
    // Constructors
    // ===========================================================

    public BaseTouchController()
    {

    }

    // ===========================================================
    // Getter & Setter
    // ===========================================================

    @Override
    public void setTouchEventCallback(final TouchEventCallback pTouchEventCallback)
    {
        this.mTouchEventCallback = pTouchEventCallback;
    }

    // ===========================================================
    // Methods for/from SuperClass/Interfaces
    // ===========================================================

    @Override
    public void reset()
    {
        this.mTouchEventRunnablePoolUpdateHandler.reset();
    }

    @Override
    public void onUpdate(final float pSecondsElapsed)
    {
        this.mTouchEventRunnablePoolUpdateHandler.onUpdate(pSecondsElapsed);
    }

    protected void fireTouchEvent(final float pX, final float pY, final int pAction, final int pPointerID, final MotionEvent pMotionEvent)
    {
        final TouchEvent touchEvent = TouchEvent.obtain(pX, pY, pAction, pPointerID, MotionEvent.obtain(pMotionEvent));

        final TouchEventRunnablePoolItem touchEventRunnablePoolItem = this.mTouchEventRunnablePoolUpdateHandler.obtainPoolItem();
        touchEventRunnablePoolItem.set(touchEvent);
        this.mTouchEventRunnablePoolUpdateHandler.postPoolItem(touchEventRunnablePoolItem);
    }

    // ===========================================================
    // Methods
    // ===========================================================

    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================

    class TouchEventRunnablePoolItem extends RunnablePoolItem
    {
        // ===========================================================
        // Fields
        // ===========================================================

        private TouchEvent touchEvent;

        // ===========================================================
        // Getter & Setter
        // ===========================================================

        public void set(final TouchEvent pTouchEvent)
        {
            this.touchEvent = pTouchEvent;
        }

        // ===========================================================
        // Methods for/from SuperClass/Interfaces
        // ===========================================================

        @Override
        public void run()
        {
            BaseTouchController.this.mTouchEventCallback.onTouchEvent(this.touchEvent);
        }

        @Override
        protected void onRecycle()
        {
            super.onRecycle();
            final TouchEvent touchEvent = this.touchEvent;
            touchEvent.getMotionEvent().recycle();
            touchEvent.recycle();
        }
    }
}
