package flakor.game.core.geometry;

import flakor.game.core.element.SizableInterface;

/**
 * Created by Saint Hsu on 13-7-9.
 */
public interface AreaShapeInterface extends ShapeInterface,SizableInterface
{
    // ===========================================================
    // Methods
    // ===========================================================

    public float getWidthScaled();
    public float getHeightScaled();

}
