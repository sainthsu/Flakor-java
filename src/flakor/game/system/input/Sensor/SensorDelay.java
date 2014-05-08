package flakor.game.system.input.Sensor;

import android.hardware.SensorManager;

/**
 * Created by Saint Hsu on 13-7-11.
 */
public enum SensorDelay
{
    // ===========================================================
    // Elements
    // ===========================================================

    NORMAL(SensorManager.SENSOR_DELAY_NORMAL),
    UI(SensorManager.SENSOR_DELAY_UI),
    GAME(SensorManager.SENSOR_DELAY_GAME),
    FASTEST(SensorManager.SENSOR_DELAY_FASTEST);

    // ===========================================================
    // Fields
    // ===========================================================

    private final int delay;

    // ===========================================================
    // Constructors
    // ===========================================================

    private SensorDelay(final int delay)
    {
        this.delay = delay;
    }

    // ===========================================================
    // Getter & Setter
    // ===========================================================

    public int getDelay()
    {
        return this.delay;
    }
}
