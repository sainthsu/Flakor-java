package flakor.game.system.input.Sensor;

/**
 * Created by Saint Hsu on 13-7-11.
 */
public interface OrientationListener
{
	// ===========================================================
    // Methods
    // ===========================================================

    public void onOrientationAccuracyChanged(final OrientationData pOrientationData);
    public void onOrientationChanged(final OrientationData pOrientationData);
}
