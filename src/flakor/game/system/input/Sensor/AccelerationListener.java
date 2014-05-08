package flakor.game.system.input.Sensor;

/**
 * Created by longjiyang on 13-7-11.
 */
public interface AccelerationListener
{

    // ===========================================================
    // Methods
    // ===========================================================

    public void onAccelerationAccuracyChanged(final AccelerationData pAccelerationData);
    public void onAccelerationChanged(final AccelerationData pAccelerationData);
}
