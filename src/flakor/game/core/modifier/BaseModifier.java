/*
 * BaseModifier.java
 * Created on 8/21/13 11:25 PM
 *
 * ver0.0.1beta 8/21/13 saint
 * Copyright (c) 2013 Saint Hsu All Rights Reserved.
 */

package flakor.game.core.modifier;

import flakor.game.support.math.SmartList;

/**
 *
 * @param <T>
 */
public abstract  class BaseModifier<T> implements ModifierInterface<T>
{
    // ===========================================================
    // Fields
    // ===========================================================

    protected boolean finished;
    private boolean autoUnregisterWhenFinished = true;
    private final SmartList<ModifierListener<T>> modifierListeners = new SmartList<ModifierListener<T>>(2);

    // ===========================================================
    // Constructors
    // ===========================================================

    public BaseModifier()
    {

    }

    public BaseModifier(final ModifierListener<T> modifierListener)
    {
        this.addModifierListener(modifierListener);
    }

    // ===========================================================
    // Methods for/from SuperClass/Interfaces
    // ===========================================================

    @Override
    public boolean isFinished()
    {
        return this.finished;
    }

    @Override
    public final boolean isAutoUnregisterWhenFinished()
    {
        return this.autoUnregisterWhenFinished;
    }

    @Override
    public final void setAutoUnregisterWhenFinished(final boolean autoUnregisterWhenFinished)
    {
        this.autoUnregisterWhenFinished = autoUnregisterWhenFinished;
    }

    @Override
    public void addModifierListener(final ModifierListener<T> modifierListener)
    {
        if (modifierListener != null)
        {
            this.modifierListeners.add(modifierListener);
        }
    }

    @Override
    public boolean removeModifierListener(final ModifierListener<T> pModifierListener)
    {
        if (pModifierListener == null)
        {
            return false;
        }
        else
        {
            return this.modifierListeners.remove(pModifierListener);
        }
    }

    @Override
    public abstract ModifierInterface<T> deepCopy() throws DeepCopyNotSupportedException;

    // ===========================================================
    // Methods
    // ===========================================================

    protected void onModifierStarted(final T pItem)
    {
        final SmartList<ModifierListener<T>> modifierListeners = this.modifierListeners;
        final int modifierListenerCount = modifierListeners.size();
        for (int i = modifierListenerCount - 1; i >= 0; i--)
        {
            modifierListeners.get(i).onModifierStarted(this, pItem);
        }
    }

    protected void onModifierFinished(final T pItem)
    {
        final SmartList<ModifierListener<T>> modifierListeners = this.modifierListeners;
        final int modifierListenerCount = modifierListeners.size();
        for (int i = modifierListenerCount - 1; i >= 0; i--)
        {
            modifierListeners.get(i).onModifierFinished(this, pItem);
        }
    }

    protected static final <T> void assertNoNullModifier(final ModifierInterface<T> pModifier)
    {
        if (pModifier == null)
        {
            throw new IllegalArgumentException("Illegal 'null' " + ModifierInterface.class.getSimpleName() + " detected!");
        }
    }

    protected static final <T> void assertNoNullModifier(final ModifierInterface<T> ... pModifiers)
    {
        final int modifierCount = pModifiers.length;
        for (int i = 0; i < modifierCount; i++)
        {
            if (pModifiers[i] == null)
            {
                throw new IllegalArgumentException("Illegal 'null' " + ModifierInterface.class.getSimpleName() + " detected at position: '" + i + "'!");
            }
        }
    }

    public static float getSequenceDurationOfModifier(final ModifierInterface<?>[] pModifiers)
    {
        float duration = Float.MIN_VALUE;

        for(int i = pModifiers.length - 1; i >= 0; i--)
        {
            duration += pModifiers[i].getDuration();
        }

        return duration;
    }
}
