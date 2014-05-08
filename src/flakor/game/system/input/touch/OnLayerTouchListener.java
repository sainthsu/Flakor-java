package flakor.game.system.input.touch;

import flakor.game.core.layer.Layer;

/**
 * Created by saint on 9/8/13.
 */
public interface OnLayerTouchListener
{
    public boolean onLayerTouchEvent(final Layer layer, final TouchEvent sceneTouchEvent);
}
