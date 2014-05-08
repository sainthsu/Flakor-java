package flakor.game.system.input.touch;

import android.view.MotionEvent;

import flakor.game.system.graphics.UpdatableInterface;

/**
 * Created by Saint Hsu on 13-7-11.
 */
public interface TouchControllerInterface extends UpdatableInterface
{
    // ===========================================================
    // Methods
    // ===========================================================

    /**
     * 设置触摸回调
     * @param pTouchEventCallback
     */
    public void setTouchEventCallback(final TouchEventCallback pTouchEventCallback);

    /**
     * 触摸事件处理
     * @param pMotionEvent
     */
    public void onHandleMotionEvent(final MotionEvent pMotionEvent);
}
