package flakor.game.system.input.touch;

import flakor.game.core.entity.MatcherInterface;

/**
 * Created by Saint Hsu on 13-7-8.
 */

public interface TouchAreaInterface
{

    public boolean contains(final float X, final float Y);

    public float[] convertSceneToLocalCoordinates(final float X, final float Y);
    public float[] convertLocalToSceneCoordinates(final float X, final float Y);

    /**
     * This method only fires if this {@link TouchAreaInterface} is registered to the {@link flakor.game.core.scene.Scene} via {@link flakor.game.core.scene.Scene#registerTouchArea(TouchAreaInterface)}.
     * @param sceneTouchEvent
     * @return <code>true</code> if the event was handled (that means {@link flakor.game.system.input.touch.OnAreaTouchListener} of the {@link flakor.game.core.scene.Scene} will not be fired!), otherwise <code>false</code>.
     */
    public boolean onAreaTouched(final TouchEvent sceneTouchEvent, final float touchAreaLocalX, final float touchAreaLocalY);

    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================

    public static interface TouchAreaMatcher extends MatcherInterface<TouchAreaInterface>
    {

    }

}
