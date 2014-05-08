package flakor.game.system.input.touch;

import android.view.MotionEvent;

/**
 * Created by Saint Hsu on 13-7-11.
 * 单点触摸控制类
 */
public class SingleTouchController extends BaseTouchController
{
    // ===========================================================
    // Constructors
    // ===========================================================

    public SingleTouchController()
    {

    }

    // ===========================================================
    // Getter & Setter
    // ===========================================================

    // ===========================================================
    // Methods for/from SuperClass/Interfaces
    // ===========================================================

    @Override
    public void onHandleMotionEvent(final MotionEvent pMotionEvent)
    {
        this.fireTouchEvent(pMotionEvent.getX(), pMotionEvent.getY(), pMotionEvent.getAction(), 0, pMotionEvent);
    }

}
