/*
 * ParallelModifier.java
 * Created on 8/23/13 5:32 PM
 *
 * ver0.0.1beta 8/23/13 saint
 * Copyright (c) 2013 Saint Hsu All Rights Reserved.
 */

package flakor.game.core.modifier;

import flakor.game.core.modifier.ModifierInterface.ModifierListener;

import java.util.Arrays;

/**
 * Created by saint on 8/23/13.
 */
public class ParallelModifier<T> extends BaseModifier<T> implements ModifierListener<T>
{
    // ===========================================================
    // Fields
    // ===========================================================

    private float secondsElapsed;
    private final float duration;

    private final ModifierInterface<T>[] modifiers;
    private boolean finishedCached;

    // ===========================================================
    // Constructors
    // ===========================================================

    public ParallelModifier(final ModifierInterface<T> ... pModifiers) throws IllegalArgumentException {
        this(null, pModifiers);
    }

    public ParallelModifier(final ModifierListener<T> pModifierListener, final ModifierInterface<T> ... pModifiers)
            throws IllegalArgumentException
    {
        super(pModifierListener);

        if(pModifiers.length == 0)
        {
            throw new IllegalArgumentException("pModifiers must not be empty!");
        }

        BaseModifier.assertNoNullModifier(pModifiers);

        Arrays.sort(pModifiers, MODIFIER_COMPARATOR_DURATION_DESCENDING);
        this.modifiers = pModifiers;

        final ModifierInterface<T> modifierWithLongestDuration = pModifiers[0];
        this.duration = modifierWithLongestDuration.getDuration();
        modifierWithLongestDuration.addModifierListener(this);
    }

    @SuppressWarnings("unchecked")
    protected ParallelModifier(final ParallelModifier<T> pParallelModifier) throws DeepCopyNotSupportedException
    {
        final ModifierInterface<T>[] otherModifiers = pParallelModifier.modifiers;
        this.modifiers = new ModifierInterface[otherModifiers.length];

        final ModifierInterface<T>[] modifiers = this.modifiers;
        for(int i = modifiers.length - 1; i >= 0; i--)
        {
            modifiers[i] = otherModifiers[i].deepCopy();
        }

        final ModifierInterface<T> modifierWithLongestDuration = modifiers[0];
        this.duration = modifierWithLongestDuration.getDuration();
        modifierWithLongestDuration.addModifierListener(this);
    }

    @Override
    public ParallelModifier<T> deepCopy() throws DeepCopyNotSupportedException
    {
        return new ParallelModifier<T>(this);
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

            final ModifierInterface<T>[] shapeModifiers = this.modifiers;

            this.finishedCached = false;
            while(secondsElapsedRemaining > 0 && !this.finishedCached)
            {
                float secondsElapsedUsed = 0;
                for(int i = shapeModifiers.length - 1; i >= 0; i--)
                {
                    secondsElapsedUsed = Math.max(secondsElapsedUsed, shapeModifiers[i].onUpdate(pSecondsElapsed, pItem));
                }
                secondsElapsedRemaining -= secondsElapsedUsed;
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
        this.secondsElapsed = 0;

        final ModifierInterface<T>[] shapeModifiers = this.modifiers;
        for(int i = shapeModifiers.length - 1; i >= 0; i--)
        {
            shapeModifiers[i].reset();
        }
    }

    @Override
    public void onModifierStarted(final ModifierInterface<T> pModifier, final T pItem)
    {
        this.onModifierStarted(pItem);
    }

    @Override
    public void onModifierFinished(final ModifierInterface<T> pModifier, final T pItem)
    {
        this.finished = true;
        this.finishedCached = true;
        this.onModifierFinished(pItem);
    }
}
