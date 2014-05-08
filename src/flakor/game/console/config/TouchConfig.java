package flakor.game.console.config;

/**
 * Created by Saint Hsu on 13-7-23.
 * 触摸配置
 */
public class TouchConfig
{
    // ===========================================================
    // Constants
    // ===========================================================

    private static final long TOUCHEVENT_INTERVAL_MILLISECONDS_DEFAULT = 20;

    // ===========================================================
    // Fields
    // ===========================================================

    /**
     * 是否支持多点
     */
    private boolean multiTouch;
    /**
     * 点击间隔 毫秒数
     */
    private long touchEventIntervalMilliseconds = TouchConfig.TOUCHEVENT_INTERVAL_MILLISECONDS_DEFAULT;

    public boolean isMultiTouch()
    {
        return multiTouch;
    }

    public void setMultiTouch(boolean multiTouch)
    {
        this.multiTouch = multiTouch;
    }

    public long getTouchEventIntervalMilliseconds()
    {
        return touchEventIntervalMilliseconds;
    }

    public void setTouchEventIntervalMilliseconds(long touchEventIntervalMilliseconds)
    {
        this.touchEventIntervalMilliseconds = touchEventIntervalMilliseconds;
    }
}
