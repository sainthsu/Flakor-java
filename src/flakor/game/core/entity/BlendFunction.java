/*
 * BlendFunction.java
 * Created on 8/4/13 8:45 PM
 *
 * ver0.0.1beta 8/4/13 saint
 * Copyright (c) 2013 Saint Hsu All Rights Reserved.
 */

package flakor.game.core.entity;

import android.opengl.GLES20;

/**
 * Created by saint on 8/4/13.
 */
public class BlendFunction
{
    public static final int BLENDFUNCTION_SOURCE_DEFAULT = GLES20.GL_SRC_ALPHA;
    public static final int BLENDFUNCTION_DESTINATION_DEFAULT = GLES20.GL_ONE_MINUS_SRC_ALPHA;

    public static final int BLENDFUNCTION_SOURCE_PREMULTIPLYALPHA_DEFAULT = GLES20.GL_ONE;
    public static final int BLENDFUNCTION_DESTINATION_PREMULTIPLYALPHA_DEFAULT = GLES20.GL_ONE_MINUS_SRC_ALPHA;

    protected int source;
    protected int destination;

    public BlendFunction()
    {
        this.source = BlendFunction.BLENDFUNCTION_SOURCE_DEFAULT;
        this.destination = BlendFunction.BLENDFUNCTION_DESTINATION_DEFAULT;
    }

    public BlendFunction(int src,int dst)
    {
        this.source = src;
        this.destination = dst;
    }

    public int getBlendFunctionSource()
    {
        return  this.source;
    }

    public int getBlendFunctionDestination()
    {
        return  this.destination;
    }

    public void setBlendFunctionSource(final int blendFunctionSource)
    {
        this.source = blendFunctionSource;
    }

    public void setBlendFunctionDestination(final int blendFunctionDestination)
    {
        this.destination = blendFunctionDestination;
    }

    public void setBlendFunction(final int blendFunctionSource, final int blendFunctionDestination)
    {
        this.source = blendFunctionSource;
        this.destination = blendFunctionDestination;
    }

}
