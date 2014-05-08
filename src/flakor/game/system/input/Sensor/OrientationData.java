package flakor.game.system.input.Sensor;

//import android.content.Context;
//import android.hardware.Sensor;

import android.hardware.SensorManager;
import android.view.Surface;

import flakor.game.support.math.MathConstants;

import java.util.Arrays;

/**
 * Created by Saint Hsu on 13-7-11.
 */
public class OrientationData extends BaseSensorData
{
    // ===========================================================
    // Fields
    // ===========================================================

    private final float[] accelerationValues = new float[3];
    private final float[] magneticFieldValues = new float[3];
    private final float[] rotationMatrix = new float[16];

    private int magneticFieldAccuracy;
    //private Sensor orientationSensor;
    // ===========================================================
    // Constructors
    // ===========================================================

    public OrientationData(final int displayRotation)
    {
        super(3, displayRotation);
        //this.orientationSensor = this.sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
    }

    // ===========================================================
    // Getter & Setter
    // ===========================================================

    public float getRoll() 
    {
        return super.values[SensorManager.DATA_Z];
    }

    public float getPitch()
    {
        return super.values[SensorManager.DATA_Y];
    }

    public float getYaw() 
    {
        return super.values[SensorManager.DATA_X];
    }

    @Override
    @Deprecated
    public void setValues(final float[] values) 
    {
        super.setValues(values);
    }

    @Override
    @Deprecated
    public void setAccuracy(final int pAccuracy)
    {
        super.setAccuracy(pAccuracy);
    }

    public void setAccelerationValues(final float[] pValues) 
    {
        System.arraycopy(pValues, 0, this.accelerationValues, 0, pValues.length);
        this.updateOrientation();
    }

    public void setMagneticFieldValues(final float[] pValues) 
    {
        System.arraycopy(pValues, 0, this.magneticFieldValues, 0, pValues.length);
        this.updateOrientation();
    }

    private void updateOrientation()
    {
        SensorManager.getRotationMatrix(this.rotationMatrix, null, this.accelerationValues, this.magneticFieldValues);

        // TODO Use dont't use identical matrixes in remapCoordinateSystem, due to performance reasons.
        switch(this.displayRotation) 
        {
            case Surface.ROTATION_0:
				/* Nothing. */
                break;
            case Surface.ROTATION_90:
                SensorManager.remapCoordinateSystem(this.rotationMatrix, SensorManager.AXIS_Y, SensorManager.AXIS_MINUS_X, this.rotationMatrix);
                break;
//			case Surface.ROTATION_180:
//				SensorManager.remapCoordinateSystem(this.mRotationMatrix, SensorManager.AXIS_?, SensorManager.AXIS_?, this.mRotationMatrix);
//				break;
//			case Surface.ROTATION_270:
//				SensorManager.remapCoordinateSystem(this.mRotationMatrix, SensorManager.AXIS_?, SensorManager.AXIS_?, this.mRotationMatrix);
//				break;
        }

        final float[] values = this.values;
        SensorManager.getOrientation(this.rotationMatrix, values);

        for(int i = values.length - 1; i >= 0; i--) {
            values[i] = values[i] * MathConstants.RAD_TO_DEG;
        }
    }

    public int getAccelerationAccuracy() {
        return this.getAccuracy();
    }

    public void setAccelerationAccuracy(final int pAccelerationAccuracy) {
        super.setAccuracy(pAccelerationAccuracy);
    }

    public int getMagneticFieldAccuracy() 
    {
        return this.magneticFieldAccuracy;
    }

    public void setMagneticFieldAccuracy(final int magneticFieldAccuracy)
    {
        this.magneticFieldAccuracy = magneticFieldAccuracy;
    }

    // ===========================================================
    // Methods for/from SuperClass/Interfaces
    // ===========================================================

    @Override
    public String toString()
    {
        return "Orientation: " + Arrays.toString(this.values);
    }

}
