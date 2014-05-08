package flakor.game.system;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Looper;
import android.os.PowerManager;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.Toast;

import flakor.game.console.config.EngineConfig;
import flakor.game.console.config.ScreenOrientation;
import flakor.game.console.config.WakeLockConfig;
import flakor.game.console.opition.AccelerationSensorOptions;
import flakor.game.console.opition.LocationSensorOptions;
import flakor.game.console.opition.OrientationSensorOptions;
import flakor.game.core.Engine;
import flakor.game.core.font.FontManager;
import flakor.game.core.GameInterface;
import flakor.game.core.GameView;
import flakor.game.core.RendererListener;
import flakor.game.core.entity.Callback;
import flakor.game.core.entity.Constants;
import flakor.game.core.scene.Scene;
import flakor.game.support.util.ActivityUtils;
import flakor.game.support.util.SystemUtils;
import flakor.game.system.audio.MusicManager;
import flakor.game.system.audio.SoundManager;
import flakor.game.system.graphics.opengl.GLState;
import flakor.game.system.graphics.opengl.Shader.ShaderProgramManager;
import flakor.game.system.graphics.opengl.Texture.TextureManager;
import flakor.game.system.graphics.opengl.VBO.VertexBufferObjectManager;
import flakor.game.system.input.LocationListenerInterface;
import flakor.game.system.input.Sensor.AccelerationListener;
import flakor.game.system.input.Sensor.OrientationListener;
import flakor.game.tool.Debug;


/**
 * Created by saint on 7/22/13.
 */
public abstract class GameActivity extends Activity implements GameInterface,RendererListener
{
    private Engine engine;

    private PowerManager.WakeLock wakeLock;

    protected GameView gameView;

    private boolean gamePaused;
    private boolean gameCreated;
    private boolean createGameCalled;
    private boolean onReloadResourcesScheduled;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        if(EngineConfig.DEBUG)
        {
            Debug.d(this.getClass().getSimpleName() + ".onCreate" + " @(Thread: '" + Thread.currentThread().getName() + "')");
        }
        super.onCreate(savedInstanceState);

        gamePaused = true;
        this.engine = this.onCreateEngine(this.onCreateEngineOptions());
        this.engine.startUpdateThread();

        this.applyEngineOptions();
        this.onSetContentView();
    }

    @Override
    public Engine onCreateEngine(EngineConfig engineOptions)
    {
        return new Engine(engineOptions);
    }

    @Override
    public synchronized void onSurfaceCreated(GLState glState)
    {
        if(EngineConfig.DEBUG)
        {
            Debug.d(this.getClass().getSimpleName() + ".onSurfaceCreated" + " @(Thread: '" + Thread.currentThread().getName() + "')");
        }

        if(this.gameCreated)
        {
            this.onReloadResources();

            if(this.gamePaused)
            {
                this.onGameResume();
            }
        }
        else
        {
            if(this.createGameCalled)
            {
                this.onReloadResourcesScheduled = true;
            }
            else
            {
                this.createGameCalled = true;
                this.onCreateGame();
            }
        }
    }

    protected abstract void onCreateResources();
    protected abstract Scene onCreateScene();

    @Override
    public final void onCreateResources(final OnCreateResourcesCallback pOnCreateResourcesCallback) throws Exception
    {
        this.onCreateResources();
        pOnCreateResourcesCallback.onCreateResourcesFinished();
    }

    @Override
    public final void onCreateScene(final OnCreateSceneCallback pOnCreateSceneCallback) throws Exception
    {
        final Scene scene = this.onCreateScene();

        pOnCreateSceneCallback.onCreateSceneFinished(scene);
    }

    @Override
    public final void onPopulateScene(final Scene pScene, final OnPopulateSceneCallback pOnPopulateSceneCallback) throws Exception
    {
        pOnPopulateSceneCallback.onPopulateSceneFinished();
    }

    @Override
    public void onSurfaceChanged(GLState glState, int width, int height)
    {
        if(EngineConfig.DEBUG)
        {
            Debug.d(this.getClass().getSimpleName() + ".onSurfaceChanged(Width=" + width + ",  Height=" + height + ")" + " @(Thread: '" + Thread.currentThread().getName() + "')");
        }
        this.onGameResize(width,height);
    }

    protected synchronized void onCreateGame()
    {
        if(EngineConfig.DEBUG)
        {
            Debug.d(this.getClass().getSimpleName() + ".onCreateGame" + " @(Thread: '" + Thread.currentThread().getName() + "')");
        }

        final OnPopulateSceneCallback onPopulateSceneCallback = new OnPopulateSceneCallback()
        {
            @Override
            public void onPopulateSceneFinished()
            {
                try
                {
                    if(EngineConfig.DEBUG)
                    {
                        Debug.d(GameActivity.this.getClass().getSimpleName() + ".onGameCreated" + " @(Thread: '" + Thread.currentThread().getName() + "')");
                    }

                    GameActivity.this.onGameCreated();
                }
                catch(final Throwable pThrowable)
                {
                    Debug.e(GameActivity.this.getClass().getSimpleName() + ".onGameCreated failed." + " @(Thread: '" + Thread.currentThread().getName() + "')", pThrowable);
                }

                GameActivity.this.callGameResumedOnUIThread();
            }
        };

        final OnCreateSceneCallback onCreateSceneCallback = new OnCreateSceneCallback()
        {
            @Override
            public void onCreateSceneFinished(final Scene pScene)
            {
                GameActivity.this.engine.setScene(pScene);

                try
                {
                    if(EngineConfig.DEBUG)
                    {
                        Debug.d(GameActivity.this.getClass().getSimpleName() + ".onPopulateScene" + " @(Thread: '" + Thread.currentThread().getName() + "')");
                    }

                    GameActivity.this.onPopulateScene(pScene, onPopulateSceneCallback);
                }
                catch(final Throwable pThrowable)
                {
                    Debug.e(GameActivity.this.getClass().getSimpleName() + ".onPopulateScene failed." + " @(Thread: '" + Thread.currentThread().getName() + "')", pThrowable);
                }
            }
        };

        final OnCreateResourcesCallback onCreateResourcesCallback = new OnCreateResourcesCallback()
        {
            @Override
            public void onCreateResourcesFinished()
            {
                try
                {
                    if(EngineConfig.DEBUG)
                    {
                        Debug.d(GameActivity.this.getClass().getSimpleName() + ".onCreateScene" + " @(Thread: '" + Thread.currentThread().getName() + "')");
                    }

                    GameActivity.this.onCreateScene(onCreateSceneCallback);
                }
                catch(final Throwable pThrowable)
                {
                    Debug.e(GameActivity.this.getClass().getSimpleName() + ".onCreateScene failed." + " @(Thread: '" + Thread.currentThread().getName() + "')", pThrowable);
                }
            }
        };

        try
        {
            if(EngineConfig.DEBUG)
            {
                Debug.d(this.getClass().getSimpleName() + ".onCreateResources" + " @(Thread: '" + Thread.currentThread().getName() + "')");
            }

            this.onCreateResources(onCreateResourcesCallback);
        }
        catch(final Throwable pThrowable)
        {
            Debug.e(this.getClass().getSimpleName() + ".onCreateGame failed." + " @(Thread: '" + Thread.currentThread().getName() + "')", pThrowable);
        }
    }


    @Override
    public synchronized void onGameCreated()
    {
        this.gameCreated = true;

		/* Since the potential asynchronous resource creation,
		 * the surface might already be invalid
		 * and a resource reloading might be necessary. */
        if(this.onReloadResourcesScheduled)
        {
            this.onReloadResourcesScheduled = false;
            try
            {
                this.onReloadResources();
            }
            catch(final Throwable pThrowable)
            {
                Debug.e(this.getClass().getSimpleName() + ".onReloadResources failed." + " @(Thread: '" + Thread.currentThread().getName() + "')", pThrowable);
            }
        }
    }

    @Override
    protected synchronized void onResume()
    {
        if(EngineConfig.DEBUG)
        {
            Debug.d(this.getClass().getSimpleName() + ".onResume" + " @(Thread: '" + Thread.currentThread().getName() + "')");
        }

        super.onResume();

        this.acquireWakeLock();
        this.gameView.onResume();
    }

    @Override
    public synchronized void onGameResume()
    {
        if(EngineConfig.DEBUG)
        {
            Debug.d(this.getClass().getSimpleName() + ".onResumeGame" + " @(Thread: '" + Thread.currentThread().getName() + "')");
        }

        this.engine.start();

        this.gamePaused = false;
    }

    @Override
    public synchronized void onWindowFocusChanged(final boolean pHasWindowFocus)
    {
        super.onWindowFocusChanged(pHasWindowFocus);

        if(pHasWindowFocus && this.gamePaused && this.gameCreated)
        {
            this.onGameResume();
        }
    }

    @Override
    public void onReloadResources()
    {
        if(EngineConfig.DEBUG) {
            Debug.d(this.getClass().getSimpleName() + ".onReloadResources" + " @(Thread: '" + Thread.currentThread().getName() + "')");
        }

        this.engine.onReloadResources();
    }

    @Override
    protected void onPause()
    {
        if(EngineConfig.DEBUG)
        {
            Debug.d(this.getClass().getSimpleName() + ".onPause" + " @(Thread: '" + Thread.currentThread().getName() + "')");
        }

        super.onPause();

        this.gameView.onPause();
        this.releaseWakeLock();

        if(!this.gamePaused)
        {
            this.onGamePause();
        }
    }

    @Override
    public synchronized void onGamePause()
    {
        if(EngineConfig.DEBUG)
        {
            Debug.d(this.getClass().getSimpleName() + ".onPauseGame" + " @(Thread: '" + Thread.currentThread().getName() + "')");
        }

        this.gamePaused = true;

        this.engine.stop();
    }

    @Override
    protected void onDestroy()
    {
        if(EngineConfig.DEBUG)
        {
            Debug.d(this.getClass().getSimpleName() + ".onDestroy" + " @(Thread: '" + Thread.currentThread().getName() + "')");
        }

        super.onDestroy();

        this.engine.onDestroy();

        try
        {
            this.onDestroyResources();
        }
        catch (final Throwable pThrowable)
        {
            Debug.e(this.getClass().getSimpleName() + ".onDestroyResources failed." + " @(Thread: '" + Thread.currentThread().getName() + "')", pThrowable);
        }

        this.onGameDestroyed();

        this.engine = null;
    }

    @Override
    public void onDestroyResources() throws Exception
    {
        if(EngineConfig.DEBUG)
        {
            Debug.d(this.getClass().getSimpleName() + ".onDestroyResources" + " @(Thread: '" + Thread.currentThread().getName() + "')");
        }

        if(this.engine.getEngineOptions().getAudioConfig().isMusicOn())
        {
            this.getMusicManager().releaseAll();
        }

        if(this.engine.getEngineOptions().getAudioConfig().isSoundOn())
        {
            this.getSoundManager().releaseAll();
        }
    }

    @Override
    public synchronized void onGameDestroyed()
    {
        if(EngineConfig.DEBUG) {
            Debug.d(this.getClass().getSimpleName() + ".onGameDestroyed" + " @(Thread: '" + Thread.currentThread().getName() + "')");
        }

        this.gameCreated = false;
    }

    // ===========================================================
    // Methods
    // ===========================================================

    public void toastOnUIThread(final CharSequence text)
    {
        this.toastOnUIThread(text, Toast.LENGTH_LONG);
    }

    public void toastOnUIThread(final CharSequence text, final int duration)
    {
        if(Looper.getMainLooper().getThread() == Thread.currentThread())
        {
            Toast.makeText(GameActivity.this, text, duration).show();
        }
        else
        {
            this.runOnUiThread(new Runnable()
            {
                @Override
                public void run()
                {
                    Toast.makeText(GameActivity.this, text, duration).show();
                }
            });
        }
    }

    // ===========================================================
    // Getter & Setter
    // ===========================================================

    public Engine getEngine()
    {
        return this.engine;
    }

    public boolean isGamePaused()
    {
        return this.gamePaused;
    }

    public boolean isGameRunning()
    {
        return !this.gamePaused;
    }

    public boolean isGameLoaded()
    {
        return this.gameCreated;
    }

    public VertexBufferObjectManager getVertexBufferObjectManager()
    {
        return this.engine.getVertexBufferObjectManager();
    }

    public TextureManager getTextureManager()
    {
        return this.engine.getTextureManager();
    }

    public FontManager getFontManager()
    {
        return this.engine.getFontManager();
    }

    public ShaderProgramManager getShaderProgramManager()
    {
        return this.engine.getShaderProgramManager();
    }

    public SoundManager getSoundManager()
    {
        return this.engine.getSoundManager();
    }

    public MusicManager getMusicManager() {
        return this.engine.getMusicManager();
    }

    // ===========================================================
    // Methods
    // ===========================================================

    private void callGameResumedOnUIThread()
    {
        GameActivity.this.runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                GameActivity.this.onGameResume();
            }
        });
    }

    protected void onSetContentView()
    {
        this.gameView = new GameView(this);
        //this.gameView.setDebugFlags(GLSurfaceView.DEBUG_CHECK_GL_ERROR | GLSurfaceView.DEBUG_LOG_GL_CALLS|GLSurfaceView.RENDERMODE_WHEN_DIRTY);
        this.gameView.setRenderer(this.engine, this);

        this.setContentView(this.gameView, GameActivity.createSurfaceViewLayoutParams());
    }

    /**
     * @see Engine#runOnUpdateThread(Runnable)
     */
    public void runOnUpdateThread(final Runnable pRunnable)
    {
        this.engine.runOnUpdateThread(pRunnable);
    }

    /**
     * @see Engine#runOnUpdateThread(Runnable, boolean)
     */
    public void runOnUpdateThread(final Runnable pRunnable, final boolean pOnlyWhenEngineRunning)
    {
        this.engine.runOnUpdateThread(pRunnable, pOnlyWhenEngineRunning);
    }

    private void acquireWakeLock()
    {
        this.acquireWakeLock(this.engine.getEngineOptions().getWakeLockOptions());
    }

    private void acquireWakeLock(final WakeLockConfig wakeLockConfig)
    {
        if(wakeLockConfig == WakeLockConfig.SCREEN_ON)
        {
            ActivityUtils.keepScreenOn(this);
        }
        else
        {
            final PowerManager pm = (PowerManager) this.getSystemService(Context.POWER_SERVICE);
            this.wakeLock = pm.newWakeLock(wakeLockConfig.getFlag() | PowerManager.ON_AFTER_RELEASE, Constants.DEBUGTAG);
            try
            {
                this.wakeLock.acquire();
            }
            catch (final SecurityException pSecurityException)
            {
                Debug.e("You have to add\n\t<uses-permission android:name=\"android.permission.WAKE_LOCK\"/>\nto your AndroidManifest.xml !", pSecurityException);
            }
        }
    }

    private void releaseWakeLock()
    {
        if(this.wakeLock != null && this.wakeLock.isHeld())
        {
            this.wakeLock.release();
        }
    }

    private void applyEngineOptions()
    {
        final EngineConfig engineOptions = this.engine.getEngineOptions();

        if(engineOptions.isFullscreen())
        {
            ActivityUtils.requestFullscreen(this);
        }

        if(engineOptions.getAudioConfig().isMusicOn() || engineOptions.getAudioConfig().isSoundOn())
        {
            this.setVolumeControlStream(AudioManager.STREAM_MUSIC);
        }

        switch(engineOptions.getScreenOrientation())
        {
            case LANDSCAPE_FIXED:
                this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                break;
            case LANDSCAPE_SENSOR:
                if(SystemUtils.SDK_VERSION_GINGERBREAD_OR_LATER)
                {
                    this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
                }
                else
                {
                    Debug.d(ScreenOrientation.class.getSimpleName() + "." + ScreenOrientation.LANDSCAPE_SENSOR + " is not supported on this device. Falling back to " + ScreenOrientation.class.getSimpleName() + "." + ScreenOrientation.LANDSCAPE_FIXED);
                    this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                }
                break;
            case PORTRAIT_FIXED:
                this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                break;
            case PORTRAIT_SENSOR:
                if(SystemUtils.SDK_VERSION_GINGERBREAD_OR_LATER)
                {
                    this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
                }
                else
                {
                    Debug.d(ScreenOrientation.class.getSimpleName() + "." + ScreenOrientation.PORTRAIT_SENSOR + " is not supported on this device. Falling back to " + ScreenOrientation.class.getSimpleName() + "." + ScreenOrientation.PORTRAIT_FIXED);
                    this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                }
                break;
        }
    }

    protected static FrameLayout.LayoutParams createSurfaceViewLayoutParams()
    {
        final FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT, android.view.ViewGroup.LayoutParams.MATCH_PARENT);
        layoutParams.gravity = Gravity.CENTER;
        return layoutParams;
    }

    protected void enableVibrator()
    {
        this.engine.enableVibrator(this);
    }

    /**
     * @see {@link Engine#enableLocationSensor(Context, flakor.game.system.input.LocationListenerInterface, LocationSensorOptions)}
     */
    protected void enableLocationSensor(final LocationListenerInterface pLocationListener, final LocationSensorOptions pLocationSensorOptions)
    {
        this.engine.enableLocationSensor(this, pLocationListener, pLocationSensorOptions);
    }

    /**
     * @see {@link Engine#disableLocationSensor(Context)}
     */
    protected void disableLocationSensor()
    {
        this.engine.disableLocationSensor(this);
    }

    /**
     * @see {@link Engine#enableAccelerationSensor(Context, AccelerationListener)}
     */
    protected boolean enableAccelerationSensor(final AccelerationListener pAccelerationListener)
    {
        return this.engine.enableAccelerationSensor(this, pAccelerationListener);
    }

    /**
     * @see {@link Engine#enableAccelerationSensor(Context, AccelerationListener, AccelerationSensorOptions)}
     */
    protected boolean enableAccelerationSensor(final AccelerationListener pAccelerationListener, final AccelerationSensorOptions pAccelerationSensorOptions)
    {
        return this.engine.enableAccelerationSensor(this, pAccelerationListener, pAccelerationSensorOptions);
    }

    /**
     * @see {@link Engine#disableAccelerationSensor(Context)}
     */
    protected boolean disableAccelerationSensor()
    {
        return this.engine.disableAccelerationSensor(this);
    }

    /**
     * @see {@link Engine#enableOrientationSensor(Context, OrientationListener)}
     */
    protected boolean enableOrientationSensor(final OrientationListener pOrientationListener)
    {
        return this.engine.enableOrientationSensor(this, pOrientationListener);
    }

    /**
     * @see {@link Engine#enableOrientationSensor(Context, OrientationListener, OrientationSensorOptions)}
     */
    protected boolean enableOrientationSensor(final OrientationListener pOrientationListener, final OrientationSensorOptions pLocationSensorOptions)
    {
        return this.engine.enableOrientationSensor(this, pOrientationListener, pLocationSensorOptions);
    }

    /**
     * @see {@link Engine#disableOrientationSensor(Context)}
     */
    protected boolean disableOrientationSensor() {
        return this.engine.disableOrientationSensor(this);
    }

    /**
     * Performs a task in the background, showing a {@link android.app.ProgressDialog},
     * while the {@link Callable} is being processed.
     *
     * @param <T>
     * @param pTitleResourceID
     * @param pMessageResourceID
     * @param pCallable
     * @param pCallback
     */
    protected <T> void doAsync(final int pTitleResourceID, final int pMessageResourceID, final Callable<T> pCallable, final Callback<T> pCallback)
    {
        this.doAsync(pTitleResourceID, pMessageResourceID, pCallable, pCallback, null);
    }

    /**
     * Performs a task in the background, showing a indeterminate {@link android.app.ProgressDialog},
     * while the {@link Callable} is being processed.
     *
     * @param <T>
     * @param pTitleResourceID
     * @param pMessageResourceID
     * @param pCallable
     * @param pCallback
     * @param pExceptionCallback
     */
    protected <T> void doAsync(final int pTitleResourceID, final int pMessageResourceID, final Callable<T> pCallable, final Callback<T> pCallback, final Callback<Exception> pExceptionCallback)
    {
        ActivityUtils.doAsync(this, pTitleResourceID, pMessageResourceID, pCallable, pCallback, pExceptionCallback);
    }
    /**
     * Performs a task in the background, showing an indeterminate {@link android.app.ProgressDialog},
     * while the {@link AsyncCallable} is being processed.
     *
     * @param <T>
     * @param pTitleResourceID
     * @param pMessageResourceID
     * @param pAsyncCallable
     * @param pCallback
     * @param pExceptionCallback
     */
    protected <T> void doAsync(final int pTitleResourceID, final int pMessageResourceID, final AsyncCallable<T> pAsyncCallable, final Callback<T> pCallback, final Callback<Exception> pExceptionCallback)
    {
        ActivityUtils.doAsync(this, pTitleResourceID, pMessageResourceID, pAsyncCallable, pCallback, pExceptionCallback);
    }

    /**
     * Performs a task in the background, showing a {@link android.app.ProgressDialog} with an ProgressBar,
     * while the {@link AsyncCallable} is being processed.
     *
     * @param <T>
     * @param pTitleResourceID
     * @param pIconResourceID
     * @param pCallable
     * @param pCallback
     */
    protected <T> void doProgressAsync(final int pTitleResourceID, final int pIconResourceID, final ProgressCallable<T> pCallable, final Callback<T> pCallback) {
        this.doProgressAsync(pTitleResourceID, pIconResourceID, pCallable, pCallback, null);
    }

    /**
     * Performs a task in the background, showing a {@link android.app.ProgressDialog} with a ProgressBar,
     * while the {@link AsyncCallable} is being processed.
     *
     * @param <T>
     * @param pTitleResourceID
     * @param pIconResourceID
     * @param pCallable
     * @param pCallback
     * @param pExceptionCallback
     */
    protected <T> void doProgressAsync(final int pTitleResourceID, final int pIconResourceID, final ProgressCallable<T> pCallable, final Callback<T> pCallback, final Callback<Exception> pExceptionCallback)
    {
        ActivityUtils.doProgressAsync(this, pTitleResourceID, pIconResourceID, pCallable, pCallback, pExceptionCallback);
    }

}
