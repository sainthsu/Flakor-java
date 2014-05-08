/*
 * SpriteFrame.java
 * Created on 9/4/13 8:07 PM
 *
 * ver0.0.1beta 9/4/13 saint
 * Copyright (c) 2013 Saint Hsu All Rights Reserved.
 */

package flakor.game.core.animation;

import flakor.game.core.element.Point;
import flakor.game.core.element.Rect;
import flakor.game.core.element.Size;

/**
 * Created by saint on 9/4/13.
 */
public class SpriteFrame
{
    //帧，，x,y,w,h
    private Rect frame;
    //偏移量
    private Point offset;
    //原图尺寸
    private Size sourceSize;

    public SpriteFrame(Rect frame, Point offset, Size sourceSize)
    {
        this.frame = frame;
        this.offset = offset;
        this.sourceSize = sourceSize;
    }

    public Rect getFrame()
    {
        return this.frame;
    }

    public Point getOffset()
    {
        return this.offset;
    }

    public Size getSourceSize()
    {
        return this.sourceSize;
    }
}
