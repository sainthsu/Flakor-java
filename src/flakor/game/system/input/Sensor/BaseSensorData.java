package flakor.game.system.input.Sensor;

import java.util.Arrays;

//import android.content.Context;
//import android.hardware.SensorManager;

/**
 * Created by Saint Hsu on 13-7-11.
 */
public class BaseSensorData
{
	public final int DATA_X = 0x00000000;
	public final int DATA_Y = 0x00000001;
	public final int DATA_Z = 0x00000002;
    // ===========================================================
    // Fields
    // ===========================================================

    protected final float[] values;
    protected int accuracy;
    protected int displayRotation;
    //protected SensorManager sensorManager;
    // ===========================================================
    // Constructors
    // ===========================================================

    public BaseSensorData(final int valueCount, int displayRotation)
    {
        this.values = new float[valueCount];
     
        this.displayRotation = displayRotation;
        //this.sensorManager = (SensorManager)context.getSystemService(Context.SENSOR_SERVICE);
    }

    // ===========================================================
    // Getter & Setter
    // ===========================================================

    public float[] getValues()
    {
        return this.values;
    }

    public void setValues(final float[] values)
    {
        System.arraycopy(values, 0, this.values, 0, values.length);
    }

    public void setAccuracy(final int accuracy)
    {
        this.accuracy = accuracy;
    }

    public int getAccuracy()
    {
        return this.accuracy;
    }

    // ===========================================================
    // Methods for/from SuperClass/Interfaces
    // ===========================================================

    @Override
    public String toString()
    {
        return "Sensor Data Values: " + Arrays.toString(this.values);
    }


}
