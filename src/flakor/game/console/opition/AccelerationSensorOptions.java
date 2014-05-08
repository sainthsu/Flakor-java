package flakor.game.console.opition;

import flakor.game.system.input.Sensor.SensorDelay;

/**
 * Created by Saint Hsu on 13-7-11.
 */
public class AccelerationSensorOptions
{
    // ===========================================================
    // Fields
    // ===========================================================

    final SensorDelay sensorDelay;

    // ===========================================================
    // Constructors
    // ===========================================================

    public AccelerationSensorOptions(final SensorDelay pSensorDelay)
    {
        this.sensorDelay = pSensorDelay;
    }

    // ===========================================================
    // Getter & Setter
    // ===========================================================

    public SensorDelay getSensorDelay()
    {
        return this.sensorDelay;
    }
}
