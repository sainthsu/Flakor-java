package flakor.game.system.input.Sensor;

import android.hardware.SensorManager;
import android.view.Surface;

import java.util.Arrays;

/**
 * Created by Saint Hsu on 13-7-11.
 */
public class AccelerationData extends BaseSensorData
{
    // ===========================================================
    // Constants
    // ===========================================================

    private static final IAxisSwap AXISSWAPS[] = new IAxisSwap[4];

    static {
        AXISSWAPS[Surface.ROTATION_0] = new IAxisSwap() 
        {
            @Override
            public void swapAxis(final float[] pValues) 
            {
                final float x = -pValues[SensorManager.DATA_X];
                final float y = pValues[SensorManager.DATA_Y];
                pValues[SensorManager.DATA_X] = x;
                pValues[SensorManager.DATA_Y] = y;
            }
        };

        AXISSWAPS[Surface.ROTATION_90] = new IAxisSwap() {
            @Override
            public void swapAxis(final float[] pValues) {
                final float x = pValues[SensorManager.DATA_Y];
                final float y = pValues[SensorManager.DATA_X];
                pValues[SensorManager.DATA_X] = x;
                pValues[SensorManager.DATA_Y] = y;
            }
        };

        AXISSWAPS[Surface.ROTATION_180] = new IAxisSwap() {
            @Override
            public void swapAxis(final float[] pValues) {
                final float x = pValues[SensorManager.DATA_X];
                final float y = -pValues[SensorManager.DATA_Y];
                pValues[SensorManager.DATA_X] = x;
                pValues[SensorManager.DATA_Y] = y;
            }
        };

        AXISSWAPS[Surface.ROTATION_270] = new IAxisSwap() {
            @Override
            public void swapAxis(final float[] pValues) {
                final float x = -pValues[SensorManager.DATA_Y];
                final float y = -pValues[SensorManager.DATA_X];
                pValues[SensorManager.DATA_X] = x;
                pValues[SensorManager.DATA_Y] = y;
            }
        };
    }

    // ===========================================================
    // Fields
    // ===========================================================

    // ===========================================================
    // Constructors
    // ===========================================================

    public AccelerationData(final int pDisplayOrientation)
    {
        super(3, pDisplayOrientation);
    }

    // ===========================================================
    // Getter & Setter
    // ===========================================================

    public float getX()
    {
        return this.values[SensorManager.DATA_X];
    }

    public float getY() 
    {
        return this.values[SensorManager.DATA_Y];
    }

    public float getZ()
    {
        return this.values[SensorManager.DATA_Z];
    }

    public void setX(final float pX) 
    {
        this.values[SensorManager.DATA_X] = pX;
    }

    public void setY(final float pY) {
        this.values[SensorManager.DATA_Y] = pY;
    }

    public void setZ(final float pZ) 
    {
        this.values[SensorManager.DATA_Z]  = pZ;
    }

    @Override
    public void setValues(final float[] pValues)
    {
        super.setValues(pValues);

        AccelerationData.AXISSWAPS[this.displayRotation].swapAxis(this.values);
    }

    // ===========================================================
    // Methods for/from SuperClass/Interfaces
    // ===========================================================

    @Override
    public String toString()
    {
        return "Acceleration: " + Arrays.toString(this.values);
    }

    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================

    private static interface IAxisSwap
    {
        // ===========================================================
        // Methods
        // ===========================================================

        public void swapAxis(final float[] pValues);
    }
}
