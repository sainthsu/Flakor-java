package flakor.game.system.input;

import android.location.Location;
import android.os.Bundle;

/**
 * Created by Saint Hsu on 13-7-11.
 */
public interface LocationListenerInterface
{	// ===========================================================
    // Methods
    // ===========================================================

    /**
     * @see {@link android.location.LocationListener#onProviderEnabled(String)}
     */
    public void onLocationProviderEnabled();

    /**
     * @see {@link android.location.LocationListener#onLocationChanged(android.location.Location)}
     */
    public void onLocationChanged(final Location pLocation);

    public void onLocationLost();

    /**
     * @see {@link android.location.LocationListener#onProviderDisabled(String)}
     */
    public void onLocationProviderDisabled();

    /**
     * @see {@link android.location.LocationListener#onStatusChanged(String,int,android.os.Bundle)}
     */
    public void onLocationProviderStatusChanged(final LocationProviderStatus pLocationProviderStatus, final Bundle pBundle);

}
