package flakor.game.system.input.touch;

import android.view.MotionEvent;

public interface TouchHandlerInterface
{
    /**
     * 开始触摸
     */
	public abstract boolean touchesBegan(MotionEvent motionEvent);

    /**
     * 触摸移动过程
     * @param motionEvent
     * @return boolean
     */
    public abstract boolean touchesMoved(MotionEvent motionEvent);

    /**
     * 结束触摸事件
     * @param motionEvent
     * @return boolean
     */
    public abstract boolean touchesEnded(MotionEvent motionEvent);

    /**
     * 取消触摸事件
     * @param motionEvent
     * @return boolean
     */
    public abstract boolean touchesCancelled(MotionEvent motionEvent);  
}
