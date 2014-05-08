/*
 * SequenceModifier.java
 * Created on 8/23/13 4:49 PM
 *
 * ver0.0.1beta 8/23/13 saint
 * Copyright (c) 2013 Saint Hsu All Rights Reserved.
 */

package flakor.game.core.modifier;

import flakor.game.core.modifier.ModifierInterface.ModifierListener;

/**
 * Created by saint on 8/23/13.
 */
public class SequenceModifier<T> extends BaseModifier<T> implements ModifierListener<T>
{
    // ===========================================================
    // Fields
    // ===========================================================

    private SubSequenceModifierListener<T> subSequenceModifierListener;

    private final ModifierInterface<T>[] subSequenceModifiers;
    private int currentSubSequenceModifierIndex;

    private float secondsElapsed;
    private final float duration;

    private boolean finishedCached;

    // ===========================================================
    // Constructors
    // ===========================================================

    public SequenceModifier(final ModifierInterface<T> ... pModifiers) throws IllegalArgumentException
    {
        this(null, null, pModifiers);
    }

    public SequenceModifier(final SubSequenceModifierListener<T> pSubSequenceModifierListener, final ModifierInterface<T> ... pModifiers)
            throws IllegalArgumentException
    {
        this(pSubSequenceModifierListener, null, pModifiers);
    }

    public SequenceModifier(final ModifierListener<T> pModifierListener, final ModifierInterface<T> ... pModifiers)
            throws IllegalArgumentException
    {
        this(null, pModifierListener, pModifiers);
    }

    public SequenceModifier(final SubSequenceModifierListener<T> pSubSequenceModifierListener, final ModifierListener<T> pModifierListener, final ModifierInterface<T> ... pModifiers)
            throws IllegalArgumentException
    {
        super(pModifierListener);

        if(pModifiers.length == 0) {
            throw new IllegalArgumentException("pModifiers must not be empty!");
        }

        BaseModifier.assertNoNullModifier(pModifiers);

        this.subSequenceModifierListener = pSubSequenceModifierListener;
        this.subSequenceModifiers = pModifiers;

        this.duration = BaseModifier.getSequenceDurationOfModifier(pModifiers);

        pModifiers[0].addModifierListener(this);
    }

    @SuppressWarnings("unchecked")
    protected SequenceModifier(final SequenceModifier<T> pSequenceModifier) throws DeepCopyNotSupportedException
    {
        this.duration = pSequenceModifier.duration;

        final ModifierInterface<T>[] otherModifiers = pSequenceModifier.subSequenceModifiers;
        this.subSequenceModifiers = new ModifierInterface[otherModifiers.length];

        final ModifierInterface<T>[] subSequenceModifiers = this.subSequenceModifiers;
        for(int i = subSequenceModifiers.length - 1; i >= 0; i--)
        {
            subSequenceModifiers[i] = otherModifiers[i].deepCopy();
        }

        subSequenceModifiers[0].addModifierListener(this);
    }

    @Override
    public SequenceModifier<T> deepCopy() throws DeepCopyNotSupportedException{
        return new SequenceModifier<T>(this);
    }

    // ===========================================================
    // Getter & Setter
    // ===========================================================

    public SubSequenceModifierListener<T> getSubSequenceModifierListener()
    {
        return this.subSequenceModifierListener;
    }

    public void setSubSequenceModifierListener(final SubSequenceModifierListener<T> pSubSequenceModifierListener)
    {
        this.subSequenceModifierListener = pSubSequenceModifierListener;
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
            while(secondsElapsedRemaining > 0 && !this.finishedCached)
            {
                secondsElapsedRemaining -= this.subSequenceModifiers[this.currentSubSequenceModifierIndex].onUpdate(secondsElapsedRemaining, pItem);
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
        if(this.isFinished())
        {
            this.subSequenceModifiers[this.subSequenceModifiers.length - 1].removeModifierListener(this);
        }
        else
        {
            this.subSequenceModifiers[this.currentSubSequenceModifierIndex].removeModifierListener(this);
        }

        this.currentSubSequenceModifierIndex = 0;
        this.finished = false;
        this.secondsElapsed = 0;

        this.subSequenceModifiers[0].addModifierListener(this);

        final ModifierInterface<T>[] subSequenceModifiers = this.subSequenceModifiers;
        for(int i = subSequenceModifiers.length - 1; i >= 0; i--)
        {
            subSequenceModifiers[i].reset();
        }
    }

    @Override
    public void onModifierStarted(final ModifierInterface<T> pModifier, final T pItem)
    {
        if(this.currentSubSequenceModifierIndex == 0)
        {
            this.onModifierStarted(pItem);
        }

        if(this.subSequenceModifierListener != null)
        {
            this.subSequenceModifierListener.onSubSequenceStarted(pModifier, pItem, this.currentSubSequenceModifierIndex);
        }
    }

    @Override
    public void onModifierFinished(final ModifierInterface<T> pModifier, final T pItem)
    {
        if(this.subSequenceModifierListener != null)
        {
            this.subSequenceModifierListener.onSubSequenceFinished(pModifier, pItem, this.currentSubSequenceModifierIndex);
        }
        pModifier.removeModifierListener(this);

        this.currentSubSequenceModifierIndex++;

        if(this.currentSubSequenceModifierIndex < this.subSequenceModifiers.length)
        {
            final ModifierInterface<T> nextSubSequenceModifier = this.subSequenceModifiers[this.currentSubSequenceModifierIndex];
            nextSubSequenceModifier.addModifierListener(this);
        }
        else
        {
            this.finished = true;
            this.finishedCached = true;

            this.onModifierFinished(pItem);
        }
    }

    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================

    public interface SubSequenceModifierListener<T>
    {
        public void onSubSequenceStarted(final ModifierInterface<T> pModifier, final T pItem, final int pIndex);
        public void onSubSequenceFinished(final ModifierInterface<T> pModifier, final T pItem, final int pIndex);
    }
}
