package flakor.game.system.input.touch;

import flakor.game.core.scene.Scene;

/**
 * Created by Saint Hsu on 13-7-8.
 */
public interface OnSceneTouchListener
{
    public boolean onSceneTouchEvent(final Scene scene, final TouchEvent sceneTouchEvent);
}
