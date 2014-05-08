package flakor.game.system.input.Key;

import android.view.KeyEvent;

public interface KeyHandlerInterface
{
    /**
     * 按键按下
     */
    public abstract boolean keyDown(KeyEvent keyEvent);

    /**
     * 按键抬起
     */
    public abstract boolean keyUp(KeyEvent keyEvent);  

}
