/*
 * PriorityKeyHandler.java
 * Created on 9/4/13 9:01 PM
 *
 * ver0.0.1beta 9/4/13 saint
 * Copyright (c) 2013 Saint Hsu All Rights Reserved.
 */

package flakor.game.system.input.Key;

import android.view.KeyEvent;

/**
 * Created by saint on 9/4/13.
 */
public class PriorityKeyHandler implements KeyHandlerInterface
{
    private KeyHandlerInterface keyHandler;
    private int priority;

    public KeyHandlerInterface getWrappedHandler()
    {
        return this.keyHandler;
    }

    public int getPriority()
    {
        return this.priority;
    }

    public void setPriority(int prio)
    {
        this.priority = prio;
    }

    public PriorityKeyHandler(KeyHandlerInterface handler, int priority)
    {
        this.keyHandler = handler;
        this.priority = priority;
    }

    public boolean keyDown(KeyEvent event)
    {
        if (this.keyHandler != null)
            return this.keyHandler.keyDown(event);
        return false;
    }

    public boolean keyUp(KeyEvent event)
    {
        if (this.keyHandler != null)
            return this.keyHandler.keyUp(event);
        return false;
    }
}
