/*
 * EaseFunction.java
 * Created on 8/25/13 3:06 PM
 *
 * ver0.0.1beta 8/25/13 saint
 * Copyright (c) 2013 Saint Hsu All Rights Reserved.
 */

package flakor.game.core.modifier;

import android.util.FloatMath;

import flakor.game.support.math.MathConstants;

/**
 * Created by saint on 8/25/13.
 */
public class EaseFunction
{
    private static final float OVERSHOOT_CONSTANT = 1.70158f;

    // ===========================================================
    // Fields
    // ===========================================================

    private static EaseFunction INSTANCE;
    private EaseType type;
    // ===========================================================
    // Constructors
    // ===========================================================

    private EaseFunction()
    {

    }

    public static EaseFunction getInstance(EaseType type)
    {
        if(null == INSTANCE)
        {
            INSTANCE = new EaseFunction();
        }

        return INSTANCE;
    }


    public float getPercentage(final float secondsElapsed, final float duration)
    {
        final float percentage = secondsElapsed/duration;
        switch (type)
        {
            case Linear:
                return percentage;

            case BackIn:
                return EaseFunction.getBackInValue(percentage);

            case BackInOut:
                return this.getBackInOutValue(percentage);

            default:
                return percentage;
        }

    }

    // ===========================================================
    // Methods
    // ===========================================================


    public static float getBackInValue(final float pPercentage)
    {
        return pPercentage * pPercentage * ((OVERSHOOT_CONSTANT + 1) * pPercentage - OVERSHOOT_CONSTANT);
    }

    public float getBackInOutValue( final float percentage)
    {
        if(percentage < 0.5f)
        {
            return 0.5f * getBackInValue(2 * percentage);
        }
        else
        {
            return 0.5f + 0.5f * getBackOutValue(percentage * 2 - 1);
        }
    }

    public static float getBackOutValue(final float pPercentage)
    {
        final float t = pPercentage - 1;
        return 1 + t * t * ((1.70158f + 1) * t + 1.70158f);
    }

    public static float getBounceInValue(final float pPercentage)
    {
        // TODO Inline?
        return 1 - getBounceOutValue(1 - pPercentage);
    }

    public float getBounceInOutValue(final float percentage)
    {
        if(percentage < 0.5f)
        {
            return 0.5f * getBounceInValue(2 * percentage);
        }
        else
        {
            return 0.5f + 0.5f * getBounceOutValue(percentage * 2 - 1);
        }
    }

    public static float getBounceOutValue(final float pPercentage)
    {
        if(pPercentage < (1f / 2.75f))
        {
            return 7.5625f * pPercentage * pPercentage;
        }
        else if(pPercentage < (2f / 2.75f))
        {
            final float t = pPercentage - (1.5f / 2.75f);
            return 7.5625f * t * t + 0.75f;
        }
        else if(pPercentage < (2.5f / 2.75f))
        {
            final float t = pPercentage - (2.25f / 2.75f);
            return 7.5625f * t * t + 0.9375f;
        }
        else
        {
            final float t = pPercentage - (2.625f / 2.75f);
            return 7.5625f * t * t + 0.984375f;
        }
    }

    public static float getCircularInValue(final float pPercentage)
    {
        return -(FloatMath.sqrt(1 - pPercentage * pPercentage) - 1.0f);
    }

    public float getCircularInOutValue(final float percentage)
    {

        if(percentage < 0.5f)
        {
            return 0.5f * getCircularInValue(2 * percentage);
        }
        else
        {
            return 0.5f + 0.5f * getCircularOutValue(percentage * 2 - 1);
        }
    }

    public static float getCircularOutValue(final float pPercentage)
    {
        final float t = pPercentage - 1;
        return FloatMath.sqrt(1 - t * t);
    }

    public static float getCubicInValue(final float pPercentage)
    {
        return pPercentage * pPercentage * pPercentage;
    }

    public float getCubicInOutValue(final float percentage)
    {

        if(percentage < 0.5f) {
            return 0.5f * getCubicInValue(2 * percentage);
        } else {
            return 0.5f + 0.5f * getCubicOutValue(percentage * 2 - 1);
        }
    }

    public static float getCubicOutValue(final float pPercentage)
    {
        final float t = pPercentage - 1;
        return 1 + (t * t * t);
    }

    public static float getElasticInValue(final float pSecondsElapsed, final float pDuration, final float pPercentage)
    {
        if(pSecondsElapsed == 0)
        {
            return 0;
        }
        if(pSecondsElapsed == pDuration)
        {
            return 1;
        }

        final float p = pDuration * 0.3f;
        final float s = p / 4;

        final float t = pPercentage - 1;
        return -(float)Math.pow(2, 10 * t) * FloatMath.sin((t * pDuration - s) * MathConstants.PI_TWICE / p);
    }

    public float getElasticInOutPercentage(final float pSecondsElapsed, final float pDuration)
    {
        final float percentage = pSecondsElapsed / pDuration;

        if(percentage < 0.5f)
        {
            return 0.5f * getElasticInValue(2 * pSecondsElapsed, pDuration, 2 * percentage);
        }
        else
        {
            return 0.5f + 0.5f * getElasticOutValue(pSecondsElapsed * 2 - pDuration, pDuration, percentage * 2 - 1);
        }
    }

    public static float getElasticOutValue(final float pSecondsElapsed, final float pDuration, final float pPercentageDone)
    {
        if(pSecondsElapsed == 0)
        {
            return 0;
        }
        if(pSecondsElapsed == pDuration)
        {
            return 1;
        }

        final float p = pDuration * 0.3f;
        final float s = p / 4;

        return 1 + (float)Math.pow(2, -10 * pPercentageDone) * FloatMath.sin((pPercentageDone * pDuration - s) * MathConstants.PI_TWICE / p);
    }

    public static float getExponentialInValue(final float pPercentage)
    {
        return (float) ((pPercentage == 0) ? 0 : Math.pow(2, 10 * (pPercentage - 1)) - 0.001f);
    }

    public float getExponentialInOutPercentage(final float pSecondsElapsed, final float pDuration)
    {
        final float percentage = pSecondsElapsed / pDuration;

        if(percentage < 0.5f)
        {
            return 0.5f * getExponentialInValue(2 * percentage);
        } else {
            return 0.5f + 0.5f * getExponentialOutValue(percentage * 2 - 1);
        }
    }

    public static float getExponentialOutValue(final float pPercentage)
    {
        return (pPercentage == 1) ? 1 : (-(float)Math.pow(2, -10 * pPercentage) + 1);
    }

    public float getLinearPercentage(final float pSecondsElapsed, final float pDuration)
    {
        return pSecondsElapsed / pDuration;
    }

    public static float getQuadInValue(final float pPercentage) {
        return pPercentage * pPercentage;
    }

    public float getQuadInOutPercentage(final float pSecondsElapsed, final float pDuration)
    {
        final float percentage = pSecondsElapsed / pDuration;

        if(percentage < 0.5f)
        {
            return 0.5f * getQuadInValue(2 * percentage);
        }
        else
        {
            return 0.5f + 0.5f * getQuadOutValue(percentage * 2 - 1);
        }
    }


    public static float getQuadOutValue(final float pPercentage)
    {
        return -pPercentage * (pPercentage - 2);
    }

    public static float getQuartInValue(final float pPercentage)
    {
        return pPercentage * pPercentage * pPercentage * pPercentage;
    }

    public float getQuartInOutPercentage(final float pSecondsElapsed, final float pDuration)
    {
        final float percentage = pSecondsElapsed / pDuration;

        if(percentage < 0.5f) {
            return 0.5f * getQuartInValue(2 * percentage);
        } else {
            return 0.5f + 0.5f * getQuartOutValue(percentage * 2 - 1);
        }
    }

    public static float getQuartOutValue(final float pPercentage)
    {
        final float t = pPercentage - 1;
        return 1 - (t * t * t * t);
    }

    public static float getQuintInValue(final float pPercentage)
    {
        return pPercentage * pPercentage * pPercentage * pPercentage * pPercentage;
    }

    public float getQuintInOutPercentage(final float pSecondsElapsed, final float pDuration)
    {
        final float percentage = pSecondsElapsed / pDuration;

        if(percentage < 0.5f) {
            return 0.5f * getQuintInValue(2 * percentage);
        } else {
            return 0.5f + 0.5f * getQuintOutValue(percentage * 2 - 1);
        }
    }

    public static float getQuintOutValue(final float pPercentage) {
        final float t = pPercentage - 1;
        return 1 + (t * t * t * t * t);
    }

    public static float getSineInValue(final float pPercentage) {
        return -FloatMath.cos(pPercentage * MathConstants.PI_HALF) + 1;
    }

    public float getSineInOutPercentage(final float pSecondsElapsed, final float pDuration) {
        final float percentage = pSecondsElapsed / pDuration;

        return -0.5f * (FloatMath.cos(percentage * MathConstants.PI) - 1);
    }

    public static float getSineOutValue(final float pPercentage) {
        return FloatMath.sin(pPercentage * MathConstants.PI_HALF);
    }

    public static float getStrongInValue(final float pPercentage) {
        return pPercentage * pPercentage * pPercentage * pPercentage * pPercentage;
    }

    public float getStrongInOutPercentage(final float pSecondsElapsed, final float pDuration)
    {
        final float percentage = pSecondsElapsed / pDuration;

        if(percentage < 0.5f) {
            return 0.5f *getStrongInValue(2 * percentage);
        } else {
            return 0.5f + 0.5f * getStrongOutValue(percentage * 2 - 1);
        }
    }

    public static float getStrongOutValue(final float pPercentage)
    {
        final float t = pPercentage - 1;
        return 1 + (t * t * t * t * t);
    }
}
