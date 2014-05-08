/*
 * LoopModifier.java
 * Created on 8/23/13 5:34 PM
 *
 * ver0.0.1beta 8/23/13 saint
 * Copyright (c) 2013 Saint Hsu All Rights Reserved.
 */

package flakor.game.core.modifier;

import flakor.game.core.modifier.ModifierInterface.ModifierListener;
/**
 * Created by saint on 8/23/13.
 */
public class LoopModifier<T> extends BaseModifier<T> implements ModifierListener<T>
{
    // ===========================================================
    // Constants
    // ===========================================================

    public static final int LOOP_CONTINUOUS = -1;

    // ===========================================================
    // Fields
    // ===========================================================

    private float secondsElapsed;
    private final float duration;

    private final ModifierInterface<T> modifier;

    private LoopModifierListener<T> loopModifierListener;

    private final int loopCount;
    private int loop;

    private boolean modifierStartedCalled;
    private boolean finishedCached;

    // ===========================================================
    // Constructors
    // ===========================================================

    public LoopModifier(final ModifierInterface<T> pModifier)
    {
        this(pModifier, LoopModifier.LOOP_CONTINUOUS);
    }

    public LoopModifier(final ModifierInterface<T> pModifier, final int pLoopCount)
    {
        this(pModifier, pLoopCount, null, (ModifierListener<T>)null);
    }

    public LoopModifier(final ModifierInterface<T> pModifier, final int pLoopCount, final ModifierListener<T> pModifierListener) {
        this(pModifier, pLoopCount, null, pModifierListener);
    }

    public LoopModifier(final ModifierInterface<T> pModifier, final int pLoopCount, final LoopModifierListener<T> pLoopModifierListener) {
        this(pModifier, pLoopCount, pLoopModifierListener, (ModifierListener<T>)null);
    }

    public LoopModifier(final ModifierInterface<T> pModifier, final int pLoopCount, final LoopModifierListener<T> pLoopModifierListener, final ModifierListener<T> pModifierListener)
    {
        super(pModifierListener);

        BaseModifier.assertNoNullModifier(pModifier);

        this.modifier = pModifier;
        this.loopCount = pLoopCount;
        this.loopModifierListener = pLoopModifierListener;

        this.loop = 0;
        this.duration = (pLoopCount == LoopModifier.LOOP_CONTINUOUS) ? Float.POSITIVE_INFINITY : pModifier.getDuration() * pLoopCount; // TODO Check if POSITIVE_INFINITY works correct with i.e. SequenceModifier

        this.modifier.addModifierListener(this);
    }

    protected LoopModifier(final LoopModifier<T> pLoopModifier) throws DeepCopyNotSupportedException
    {
        this(pLoopModifier.modifier.deepCopy(), pLoopModifier.loopCount);
    }

    @Override
    public LoopModifier<T> deepCopy() throws DeepCopyNotSupportedException
    {
        return new LoopModifier<T>(this);
    }

    // ===========================================================
    // Getter & Setter
    // ===========================================================

    public LoopModifierListener<T> getLoopModifierListener()
    {
        return this.loopModifierListener;
    }

    public void setLoopModifierListener(final LoopModifierListener<T> pLoopModifierListener)
    {
        this.loopModifierListener = pLoopModifierListener;
    }

    // ===========================================================
    // Methods for/from SuperClass/Interfaces
    // ===========================================================

    @Override
    public float getSecondsElapsed()
    {
        return this.secondsElapsed;
    }

    @Override
    public float getDuration()
    {
        return this.duration;
    }

    @Override
    public float onUpdate(final float pSecondsElapsed, final T pItem)
    {
        if(this.finished)
        {
            return 0;
        }
        else
        {
            float secondsElapsedRemaining = pSecondsElapsed;

            this.finishedCached = false;
            while((secondsElapsedRemaining > 0) && !this.finishedCached)
            {
                secondsElapsedRemaining -= this.modifier.onUpdate(secondsElapsedRemaining, pItem);
            }
            this.finishedCached = false;

            final float secondsElapsedUsed = pSecondsElapsed - secondsElapsedRemaining;
            this.secondsElapsed += secondsElapsedUsed;
            return secondsElapsedUsed;
        }
    }

    @Override
    public void reset()
    {
        this.finished = false;
        this.loop = 0;
        this.secondsElapsed = 0;
        this.modifierStartedCalled = false;

        this.modifier.reset();
    }

    // ===========================================================
    // Methods
    // ===========================================================

    @Override
    public void onModifierStarted(final ModifierInterface<T> pModifier, final T pItem)
    {
        if(!this.modifierStartedCalled)
        {
            this.modifierStartedCalled = true;
            this.onModifierStarted(pItem);
        }
        if(this.loopModifierListener != null)
        {
            this.loopModifierListener.onLoopStarted(this, this.loop, this.loopCount);
        }
    }

    @Override
    public void onModifierFinished(final ModifierInterface<T> pModifier, final T pItem)
    {
        if(this.loopModifierListener != null)
        {
            this.loopModifierListener.onLoopFinished(this, this.loop, this.loopCount);
        }

        if(this.loopCount == LoopModifier.LOOP_CONTINUOUS)
        {
            this.secondsElapsed = 0;
            this.modifier.reset();
        }
        else
        {
            this.loop++;
            if(this.loop >= this.loopCount)
            {
                this.finished = true;
                this.finishedCached = true;
                this.onModifierFinished(pItem);
            }
            else
            {
                this.secondsElapsed = 0;
                this.modifier.reset();
            }
        }
    }

    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================

    public interface LoopModifierListener<T>
    {
        public void onLoopStarted(final LoopModifier<T> pLoopModifier, final int pLoop, final int pLoopCount);
        public void onLoopFinished(final LoopModifier<T> pLoopModifier, final int pLoop, final int pLoopCount);
    }
}
