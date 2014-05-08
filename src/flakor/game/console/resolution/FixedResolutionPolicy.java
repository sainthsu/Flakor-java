/*
 * FixedResolutionPolicy.java
 * Created on 9/9/13 4:09 PM
 *
 * ver0.0.1beta 9/9/13 saint
 * Copyright (c) 2013 Saint Hsu All Rights Reserved.
 */

package flakor.game.console.resolution;

import flakor.game.core.GameView;

/**
 * Created by saint on 9/9/13.
 */
public class FixedResolutionPolicy extends BaseResolutionPolicy
{
    // ===========================================================
    // Fields
    // ===========================================================

    private final int mWidth;
    private final int mHeight;

    // ===========================================================
    // Constructors
    // ===========================================================

    public FixedResolutionPolicy(final int pWidth, final int pHeight) {
        this.mWidth = pWidth;
        this.mHeight = pHeight;
    }

    // ===========================================================
    // Getter & Setter
    // ===========================================================

    // ===========================================================
    // Methods for/from SuperClass/Interfaces
    // ===========================================================

    @Override
    public void onMeasure(final GameView pRenderSurfaceView, final int pWidthMeasureSpec, final int pHeightMeasureSpec)
    {
        pRenderSurfaceView.setMeasuredDimensionProxy(this.mWidth, this.mHeight);
    }

}
