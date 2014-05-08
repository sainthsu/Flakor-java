package flakor.game.console.config;

import android.location.Criteria;

import flakor.game.support.util.TimeConstants;
import flakor.game.system.input.Sensor.SensorDelay;

/**
 * Created by saint on 10/22/13.
 */
public class SensorConfig extends Criteria
{
    // ===========================================================
    // Constants
    // ===========================================================

    private static final long MINIMUMTRIGGERTIME_DEFAULT = 1 * TimeConstants.MILLISECONDS_PER_SECOND;
    private static final long MINIMUMTRIGGERDISTANCE_DEFAULT = 10;

    // ===========================================================
    // Fields
    // ===========================================================

    private boolean mEnabledOnly = true;

    private long mMinimumTriggerTime = MINIMUMTRIGGERTIME_DEFAULT;
    private long mMinimumTriggerDistance = MINIMUMTRIGGERDISTANCE_DEFAULT;

    private SensorDelay accelerationDelay;
    private SensorDelay orientaitionDelay;

    /**
     * @param pAccuracy
     * @param pAltitudeRequired
     * @param pBearingRequired
     * @param pCostAllowed
     * @param pPowerRequirement
     * @param pSpeedRequired
     * @param pEnabledOnly
     * @param pMinimumTriggerTime
     * @param pMinimumTriggerDistance
     */
    public SensorConfig(final int pAccuracy, final boolean pAltitudeRequired, final boolean pBearingRequired, final boolean pCostAllowed, final int pPowerRequirement, final boolean pSpeedRequired, final boolean pEnabledOnly, final long pMinimumTriggerTime, final long pMinimumTriggerDistance)
    {
        this.mEnabledOnly = pEnabledOnly;
        this.mMinimumTriggerTime = pMinimumTriggerTime;
        this.mMinimumTriggerDistance = pMinimumTriggerDistance;

        this.setAccuracy(pAccuracy);
        this.setAltitudeRequired(pAltitudeRequired);
        this.setBearingRequired(pBearingRequired);
        this.setCostAllowed(pCostAllowed);
        this.setPowerRequirement(pPowerRequirement);
        this.setSpeedRequired(pSpeedRequired);
    }

    // ===========================================================
    // Getter & Setter
    // ===========================================================

    public void setEnabledOnly(final boolean pEnabledOnly)
    {
        this.mEnabledOnly = pEnabledOnly;
    }

    public boolean isEnabledOnly()
    {
        return this.mEnabledOnly;
    }

    public long getMinimumTriggerTime()
    {
        return this.mMinimumTriggerTime;
    }

    public void setMinimumTriggerTime(final long pMinimumTriggerTime)
    {
        this.mMinimumTriggerTime = pMinimumTriggerTime;
    }

    public long getMinimumTriggerDistance()
    {
        return this.mMinimumTriggerDistance;
    }

    public void setMinimumTriggerDistance(final long pMinimumTriggerDistance)
    {
        this.mMinimumTriggerDistance = pMinimumTriggerDistance;
    }

    public SensorDelay getAccelerationDelay()
    {
        return accelerationDelay;
    }

    public void setAccelerationDelay(SensorDelay accelerationDelay)
    {
        this.accelerationDelay = accelerationDelay;
    }

    public SensorDelay getOrientaitionDelay()
    {
        return orientaitionDelay;
    }

    public void setOrientaitionDelay(SensorDelay orientaitionDelay)
    {
        this.orientaitionDelay = orientaitionDelay;
    }
}
