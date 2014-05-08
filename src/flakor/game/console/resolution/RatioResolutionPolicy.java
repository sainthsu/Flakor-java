/*
 * RatioResolutionPolicy.java
 * Created on 9/9/13 4:07 PM
 *
 * ver0.0.1beta 9/9/13 saint
 * Copyright (c) 2013 Saint Hsu All Rights Reserved.
 */

package flakor.game.console.resolution;

import android.view.View;

import flakor.game.core.GameView;

/**
 * Created by saint on 9/9/13.
 */
public class RatioResolutionPolicy extends BaseResolutionPolicy
{
    // ===========================================================
    // Fields
    // ===========================================================

    private final float mRatio;

    // ===========================================================
    // Constructors
    // ===========================================================

    public RatioResolutionPolicy(final float pRatio)
    {
        this.mRatio = pRatio;
    }

    public RatioResolutionPolicy(final float pWidthRatio, final float pHeightRatio)
    {
        this.mRatio = pWidthRatio / pHeightRatio;
    }

    // ===========================================================
    // Methods for/from SuperClass/Interfaces
    // ===========================================================

    @Override
    public void onMeasure(final GameView pRenderSurfaceView, final int pWidthMeasureSpec, final int pHeightMeasureSpec)
    {
        throwOnNotMeasureSpecEXACTLY(pWidthMeasureSpec, pHeightMeasureSpec);

        final int specWidth = View.MeasureSpec.getSize(pWidthMeasureSpec);
        final int specHeight = View.MeasureSpec.getSize(pHeightMeasureSpec);

        final float desiredRatio = this.mRatio;
        final float realRatio = (float)specWidth / specHeight;

        int measuredWidth;
        int measuredHeight;
        if(realRatio < desiredRatio) {
            measuredWidth = specWidth;
            measuredHeight = Math.round(measuredWidth / desiredRatio);
        } else {
            measuredHeight = specHeight;
            measuredWidth = Math.round(measuredHeight * desiredRatio);
        }

        pRenderSurfaceView.setMeasuredDimensionProxy(measuredWidth, measuredHeight);
    }

}
