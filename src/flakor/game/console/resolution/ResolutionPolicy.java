package flakor.game.console.resolution;

import flakor.game.core.GameView;

/**
 * Created by Saint Hsu on 13-7-11.
 * <p>MeasureSpec.EXACTLY - A view should be exactly this many pixels regardless of how big it actually
 * wants to be.
 * <p>MeasureSpec.AT_MOST - A view can be this size or smaller if it measures out to be smaller.
 * <p>MeasureSpec.UNSPECIFIED - A view can be whatever size it needs to be in order to
 * show the content it needs to show.
 * <p>MeasureSpec.AT_MOST will be applied to views that have been set to WRAP_CONTENT if the parent view is bound in size. For example, your parent View might be bound to the screen size. It's children will be also bound to this size, but it might not be that big. Thus, the parent view will set the MeasureSpec to be AT_MOST which tells the child that it can be anywhere between 0 and screen size. The child will have to
 * make adjustments to ensure that it fits within the bounds that was provided.
 * In special cases, the bounds do not matter. For example, a ScrollView. In the case of a ScrollView, the height of the child Views are irrelevant. As such, it will supply an UNSPECIFIED to the children Views which tells the children that they can be as tall as they need to be. The ScrollView will handle the drawing and placement for them.
 */

public interface ResolutionPolicy
{
    public void onMeasure(final GameView gameView, final int widthMeasureSpec, final int heightMeasureSpec);
}
