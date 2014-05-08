package flakor.game.system.input.touch;

/**
 * Created by Saint Hsu on 13-7-11.
 */
public interface TouchEventCallback
{
	// ===========================================================
    // Methods
    // ===========================================================

    /**
     * 触摸事件回调处理
     * @param pTouchEvent
     * @return
     */
    public boolean onTouchEvent(final TouchEvent pTouchEvent);
}
