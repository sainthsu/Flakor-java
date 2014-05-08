/*
 * BaseResolutionPolicy.java
 * Created on 9/9/13 4:05 PM
 *
 * ver0.0.1beta 9/9/13 saint
 * Copyright (c) 2013 Saint Hsu All Rights Reserved.
 */

package flakor.game.console.resolution;

import android.view.View;

/**
 * Created by saint on 9/9/13.
 */
public abstract class BaseResolutionPolicy implements ResolutionPolicy
{

    // ===========================================================
    // Methods for/from SuperClass/Interfaces
    // ===========================================================

    protected static void throwOnNotMeasureSpecEXACTLY(final int pWidthMeasureSpec, final int pHeightMeasureSpec)
    {
        final int specWidthMode = View.MeasureSpec.getMode(pWidthMeasureSpec);
        final int specHeightMode = View.MeasureSpec.getMode(pHeightMeasureSpec);

        if (specWidthMode != View.MeasureSpec.EXACTLY || specHeightMode != View.MeasureSpec.EXACTLY)
        {
            throw new IllegalStateException("This ResolutionPolicy requires MeasureSpec.EXACTLY ! That means ");
        }
    }

}
