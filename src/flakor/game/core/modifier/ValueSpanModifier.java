/*
 * ValueSpanModifier.java
 * Created on 8/25/13 5:06 PM
 *
 * ver0.0.1beta 8/25/13 saint
 * Copyright (c) 2013 Saint Hsu All Rights Reserved.
 */

package flakor.game.core.modifier;

import flakor.game.core.modifier.ease.IEaseFunction;

/**
 * Created by saint on 8/25/13.
 */
public abstract class ValueSpanModifier<T> extends DurationModifier<T>
{

    // ===========================================================
    // Fields
    // ===========================================================

    private int valueNum;
    private float[] fromValue;
    private float[] valueSpan;

    protected final IEaseFunction easeFunction;

    public ValueSpanModifier(final float duration,  final ModifierListener<T> modifierListener, final IEaseFunction easeFunction,final float[] fromValue, final float[] toValue)
    {
        /*
        if(fromValue.length != toValue.length)
        {
            throw ValueNotPairException;
        }
        */

        super(duration, modifierListener);

        this.fromValue = fromValue;
        this.valueNum = fromValue.length;
        this.valueSpan = new float[this.valueNum];
        for(int i=0;i<this.valueNum;i++)
        {
            this.valueSpan[i] = toValue[i] - fromValue[i];
        }

        this.easeFunction = easeFunction;
    }


    // ===========================================================
    // Getter & Setter
    // ===========================================================

    public float[] getFromValue()
    {
        return this.fromValue;
    }

    public float[] getToValue()
    {
        float[] values = new float[this.valueNum];
        for (int i=0;i<this.valueNum;i++)
        {
            values[i] = this.fromValue[i]+this.valueSpan[i];
        }
        return values;
    }

    // ===========================================================
    // Methods for/from SuperClass/Interfaces
    // ===========================================================

    protected abstract void onSetInitialValue(final T pItem, final float[] pValue);
    protected abstract void onSetValue(final T pItem, final float pPercentageDone, final float[] pValue);

    @Override
    protected void onManagedInitialize(final T pItem)
    {
        this.onSetInitialValue(pItem, this.fromValue);
    }

    @Override
    protected void onManagedUpdate(final float pSecondsElapsed, final T pItem)
    {
        final float percentageDone = this.easeFunction.getPercentage(this.getSecondsElapsed(), this.duration);
        float[] values = new float[this.valueNum];
        for (int i=0;i<this.valueNum;i++)
        {
            values[i] = this.fromValue[i]+this.valueSpan[i]*percentageDone;
        }
        this.onSetValue(pItem, percentageDone, values);
    }

    // ===========================================================
    // Methods
    // ===========================================================

    public void reset(final float duration, final float[] fromValue, final float[] toValue)
    {
        super.reset();

        this.duration = duration;
        this.fromValue = fromValue;
        this.valueNum = fromValue.length;

        this.valueSpan = new float[this.valueNum];
        for (int i=0;i<this.valueNum;i++)
        {
            this.valueSpan[i] = toValue[i]-fromValue[i];
        }

    }

}

