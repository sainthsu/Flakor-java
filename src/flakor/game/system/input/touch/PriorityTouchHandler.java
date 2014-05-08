/*
 * PriorityTouchHandler.java
 * Created on 9/4/13 9:07 PM
 *
 * ver0.0.1beta 9/4/13 saint
 * Copyright (c) 2013 Saint Hsu All Rights Reserved.
 */

package flakor.game.system.input.touch;

import android.view.MotionEvent;

/**
 * Created by saint on 9/4/13.
 */

public class PriorityTouchHandler implements TouchHandlerInterface
{
    private TouchHandlerInterface touchHandler;
    private int priority;

    public TouchHandlerInterface getWrappedHandler()
    {
        return this.touchHandler;
    }

    public int getPriority()
    {
        return this.priority;
    }

    public void setPriority(int prio)
    {
        this.priority = prio;
    }

    public PriorityTouchHandler(TouchHandlerInterface handler, int priority)
    {
        this.touchHandler = handler;
        this.priority = priority;
    }

    public boolean touchesBegan(MotionEvent event)
    {
        if (this.touchHandler != null)
            return this.touchHandler.touchesBegan(event);
        return false;
    }

    public boolean touchesMoved(MotionEvent event)
    {
        if (this.touchHandler != null)
            return this.touchHandler.touchesMoved(event);
        return false;
    }

    public boolean touchesEnded(MotionEvent event)
    {
        if (this.touchHandler != null)
            return this.touchHandler.touchesEnded(event);
        return false;
    }

    public boolean touchesCancelled(MotionEvent event)
    {
        if (this.touchHandler != null)
            return this.touchHandler.touchesCancelled(event);
        return false;
    }
}
