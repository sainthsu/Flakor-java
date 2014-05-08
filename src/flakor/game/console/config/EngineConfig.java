package flakor.game.console.config;


import flakor.game.console.resolution.ResolutionPolicy;
import flakor.game.core.Engine.EngineLock;
import flakor.game.core.Engine.UpdateThread;
import flakor.game.core.camera.Camera;

/**
 * Created by Saint Hsu on 13-7-23.
 * 引擎配置，包括声音，分辨率，是否全屏，点击配置等
 */

public class EngineConfig
{
    public static boolean DEBUG;
    // ===========================================================
    // Fields
    // ===========================================================

    private EngineLock engineLock;

    private final boolean fullscreen;
    private final ScreenOrientation screenOrientation;
    private final ResolutionPolicy resolutionPolicy;
    private final Camera camera;

    private final TouchConfig touchOptions = new TouchConfig();
    private final AudioConfig audioOptions = new AudioConfig();
    private final RenderConfig renderOptions = new RenderConfig();

    private WakeLockConfig wakeLockOptions = WakeLockConfig.SCREEN_ON;

    private UpdateThread updateThread;
    private int updateThreadPriority = android.os.Process.THREAD_PRIORITY_DEFAULT;

    // ===========================================================
    // Constructors
    // ===========================================================

    public EngineConfig(final boolean fullscreen, final ScreenOrientation screenOrientation, final ResolutionPolicy resolutionPolicy, final Camera camera,final boolean debug)
    {
        this.fullscreen = fullscreen;
        this.screenOrientation = screenOrientation;
        this.resolutionPolicy = resolutionPolicy;
        this.camera = camera;
        EngineConfig.DEBUG=debug;
    }

    // ===========================================================
    // Getter & Setter
    // ===========================================================

    public boolean hasEngineLock()
    {
        return this.engineLock != null;
    }

    public EngineLock getEngineLock()
    {
        return this.engineLock;
    }

    public void setEngineLock(final EngineLock engineLock)
    {
        this.engineLock = engineLock;
    }

    public TouchConfig getTouchConfig()
    {
        return this.touchOptions;
    }

    public AudioConfig getAudioConfig()
    {
        return this.audioOptions;
    }

    public RenderConfig getRenderConfig()
    {
        return this.renderOptions;
    }

    public boolean isFullscreen()
    {
        return this.fullscreen;
    }

    public ScreenOrientation getScreenOrientation()
    {
        return this.screenOrientation;
    }

    public ResolutionPolicy getResolutionPolicy()
    {
        return this.resolutionPolicy;
    }

    public Camera getCamera()
    {
        return this.camera;
    }

    public boolean hasUpdateThread()
    {
        return this.updateThread != null;
    }

    public UpdateThread getUpdateThread()
    {
        return this.updateThread;
    }

    public void setUpdateThread(final UpdateThread pUpdateThread)
    {
        this.updateThread = pUpdateThread;
    }

    public int getUpdateThreadPriority()
    {
        return this.updateThreadPriority;
    }

    /**
     * @param updateThreadPriority
     * 使用{@link android.os.Process}的常量来表示更新线程的优先权
     * Use constants from: {@link android.os.Process}.
     */
    public void setUpdateThreadPriority(final int updateThreadPriority)
    {
        this.updateThreadPriority = updateThreadPriority;
    }

    public WakeLockConfig getWakeLockOptions()
    {
        return this.wakeLockOptions;
    }

    public EngineConfig setWakeLockOptions(final WakeLockConfig wakeLockOptions)
    {
        this.wakeLockOptions = wakeLockOptions;
        return this;
    }

}
