/*
 * DurationModifier.java
 * Created on 8/25/13 2:28 PM
 *
 * ver0.0.1beta 8/25/13 saint
 * Copyright (c) 2013 Saint Hsu All Rights Reserved.
 */

package flakor.game.core.modifier;

/**
 * Created by saint on 8/25/13.
 */

public abstract class DurationModifier<T> extends BaseModifier<T>
{
    // ===========================================================
    // Fields
    // ===========================================================

    private float secondsElapsed;
    protected float duration;

    // ===========================================================
    // Constructors
    // ===========================================================

    public DurationModifier(final float duration)
    {
        this.duration = duration;
    }

    public DurationModifier(final float pDuration, final ModifierListener<T> pModifierListener)
    {
        super(pModifierListener);

        this.duration = pDuration;
    }

    protected DurationModifier(final DurationModifier<T> pBaseModifier)
    {
        this(pBaseModifier.duration);
    }

    // ===========================================================
    // Getter & Setter
    // ===========================================================

    @Override
    public float getSecondsElapsed()
    {
        return this.secondsElapsed;
    }

    // ===========================================================
    // Methods for/from SuperClass/Interfaces
    // ===========================================================

    @Override
    public float getDuration()
    {
        return this.duration;
    }

    protected abstract void onManagedUpdate(final float pSecondsElapsed, final T pItem);

    protected abstract void onManagedInitialize(final T pItem);

    @Override
    public final float onUpdate(final float pSecondsElapsed, final T pItem)
    {
        if(this.finished)
        {
            return 0;
        }
        else
        {
            if(this.secondsElapsed == 0)
            {
                this.onManagedInitialize(pItem);
                this.onModifierStarted(pItem);
            }

            final float secondsElapsedUsed;
            if(this.secondsElapsed + pSecondsElapsed < this.duration)
            {
                secondsElapsedUsed = pSecondsElapsed;
            }
            else
            {
                secondsElapsedUsed = this.duration - this.secondsElapsed;
            }

            this.secondsElapsed += secondsElapsedUsed;
            this.onManagedUpdate(secondsElapsedUsed, pItem);

            if(this.duration != -1 && this.secondsElapsed >= this.duration)
            {
                this.secondsElapsed = this.duration;
                this.finished = true;
                this.onModifierFinished(pItem);
            }
            return secondsElapsedUsed;
        }
    }

    @Override
    public void reset()
    {
        this.finished = false;
        this.secondsElapsed = 0;
    }
}
