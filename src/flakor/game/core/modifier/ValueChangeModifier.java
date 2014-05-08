/*
 * ValueChangeModifier.java
 * Created on 8/25/13 4:36 PM
 *
 * ver0.0.1beta 8/25/13 saint
 * Copyright (c) 2013 Saint Hsu All Rights Reserved.
 */

package flakor.game.core.modifier;

/**
 * Created by saint on 8/25/13.
 */
public abstract class ValueChangeModifier<T> extends DurationModifier<T>
{

    // ===========================================================
    // Fields
    // ===========================================================
    private final int valueNum;

    private final float[] valueChangePerSecond;

    public ValueChangeModifier(final float duration,  final ModifierListener<T> modifierListener,final float ... valueChange)
    {
        super(duration, modifierListener);
        this.valueNum = valueChange.length;
        valueChangePerSecond = new float[valueNum];
        for (int i=0;i<valueNum;i++)
        {
            valueChangePerSecond[i] = valueChange[i]/duration;
        }
    }

    protected abstract void onChangeValue(final float pSecondsElapsed, final T pItem, final float ...  pValue);

    @Override
    protected void onManagedUpdate(float secondsElapsed, T item)
    {
        float[] values = new float[valueNum];
        for (int i=0;i<valueNum;i++)
        {
            values[i] = valueChangePerSecond[i]*secondsElapsed;
        }
        this.onChangeValue(secondsElapsed, item, values);
    }

    @Override
    protected void onManagedInitialize(T pItem)
    {

    }

}
