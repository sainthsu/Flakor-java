package flakor.game.console.opition;

import flakor.game.system.input.Sensor.SensorDelay;

/**
 * Created by Saint Hsu on 13-7-11.
 */
public class OrientationSensorOptions
{
	// ===========================================================
    // Fields
    // ===========================================================

    final SensorDelay mSensorDelay;

    // ===========================================================
    // Constructors
    // ===========================================================

    public OrientationSensorOptions(final SensorDelay pSensorDelay)
    {
        this.mSensorDelay = pSensorDelay;
    }

    // ===========================================================
    // Getter & Setter
    // ===========================================================

    public SensorDelay getSensorDelay()
    {
        return this.mSensorDelay;
    }

}
