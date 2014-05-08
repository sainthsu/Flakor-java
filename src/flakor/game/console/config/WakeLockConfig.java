package flakor.game.console.config;

import android.os.PowerManager;

/**
 * Created by Saint Hsu on 13-7-23.
 */
public enum WakeLockConfig
{
    // ===========================================================
    // Elements
    // ===========================================================

    CPU_USE(PowerManager.PARTIAL_WAKE_LOCK),
    ACQUIRE_CAUSES(PowerManager.ACQUIRE_CAUSES_WAKEUP),
    RELEASE_ON(PowerManager.ON_AFTER_RELEASE),
    /** 
     * Screen is on at full brightness. Keyboard backlight is on at full brightness. Requires <b>WAKE_LOCK</b> permission! 
     */
    BRIGHT(PowerManager.FULL_WAKE_LOCK),
    /** Screen is on at full brightness. Keyboard backlight will be allowed to go off. Requires <b>WAKE_LOCK</b> permission!*/
    SCREEN_BRIGHT(PowerManager.SCREEN_BRIGHT_WAKE_LOCK),
    /** Screen is on but may be dimmed. Keyboard backlight will be allowed to go off. Requires <b>WAKE_LOCK</b> permission!*/
    SCREEN_DIM(PowerManager.SCREEN_DIM_WAKE_LOCK),
    /** Screen is on at full brightness. Does <b>not</b> require <b>WAKE_LOCK</b> permission! */
    SCREEN_ON(-1);

    // ===========================================================
    // Fields
    // ===========================================================

    private final int flag;

    // ===========================================================
    // Constructors
    // ===========================================================

    private WakeLockConfig(final int pFlag)
    {
        this.flag = pFlag;
    }

    // ===========================================================
    // Getter & Setter
    // ===========================================================

    public int getFlag()
    {
        return this.flag;
    }
}
