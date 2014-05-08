package flakor.game.system.input.touch;

import android.view.MotionEvent;

/**
 * Created by Saint Hsu on 13-7-11.
 */
public class MultiTouchController extends BaseTouchController
{
    // Methods for/from SuperClass/Interfaces
    // ===========================================================

    @Override
    public void onHandleMotionEvent(final MotionEvent pMotionEvent)
    {
        final int action = pMotionEvent.getAction() & MotionEvent.ACTION_MASK;
        switch(action)
        {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN:
                this.onHandleTouchAction(MotionEvent.ACTION_DOWN, pMotionEvent);
                return;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
                this.onHandleTouchAction(MotionEvent.ACTION_UP, pMotionEvent);
                return;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_OUTSIDE:
                this.onHandleTouchAction(action, pMotionEvent);
                return;
            case MotionEvent.ACTION_MOVE:
                this.onHandleTouchMove(pMotionEvent);
                return;
            default:
                throw new IllegalArgumentException("Invalid Action detected: " + action);
        }
    }

    // ===========================================================
    // Methods
    // ===========================================================

    /**
     * 触摸移动处理
     * @param pMotionEvent android 触摸事件
     */
    private void onHandleTouchMove(final MotionEvent pMotionEvent)
    {
        for(int i = pMotionEvent.getPointerCount() - 1; i >= 0; i--)
        {
            final int pointerIndex = i;
            final int pointerID = pMotionEvent.getPointerId(pointerIndex);
            this.fireTouchEvent(pMotionEvent.getX(pointerIndex), pMotionEvent.getY(pointerIndex), MotionEvent.ACTION_MOVE, pointerID, pMotionEvent);
        }
    }

    /**
     * 触摸动作处理
     * @param pAction
     * @param pMotionEvent
     */
    private void onHandleTouchAction(final int pAction, final MotionEvent pMotionEvent)
    {
        final int pointerIndex = MultiTouchController.getPointerIndex(pMotionEvent);
        final int pointerID = pMotionEvent.getPointerId(pointerIndex);
        this.fireTouchEvent(pMotionEvent.getX(pointerIndex), pMotionEvent.getY(pointerIndex), pAction, pointerID, pMotionEvent);
    }

    /**
     * 得到触摸点ID
     * @param pMotionEvent
     * @return
     */
    private static int getPointerIndex(final MotionEvent pMotionEvent)
    {
        return (pMotionEvent.getAction() & MotionEvent.ACTION_POINTER_INDEX_MASK) >> MotionEvent.ACTION_POINTER_INDEX_SHIFT;
    }
}
