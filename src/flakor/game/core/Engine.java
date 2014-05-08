package flakor.game.core;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import flakor.game.console.config.EngineConfig;
import flakor.game.console.opition.AccelerationSensorOptions;
import flakor.game.console.opition.LocationSensorOptions;
import flakor.game.console.opition.OrientationSensorOptions;
import flakor.game.core.font.FontFactory;
import flakor.game.core.font.FontManager;
import flakor.game.core.camera.Camera;
import flakor.game.core.entity.RunnableHandler;
import flakor.game.core.entity.UpdateHandlerList;
import flakor.game.core.scene.Scene;
import flakor.game.support.util.TimeConstants;
import flakor.game.system.audio.MusicFactory;
import flakor.game.system.audio.MusicManager;
import flakor.game.system.audio.SoundFactory;
import flakor.game.system.audio.SoundManager;
import flakor.game.system.graphics.DrawHandlerList;
import flakor.game.system.graphics.DrawInterface;
import flakor.game.system.graphics.UpdatableInterface;
import flakor.game.system.graphics.opengl.GLState;
import flakor.game.system.graphics.opengl.Shader.ShaderProgramManager;
import flakor.game.system.graphics.opengl.Texture.BitmapTextureAtlasTextureRegionFactory;
import flakor.game.system.graphics.opengl.Texture.TextureManager;
import flakor.game.system.graphics.opengl.VBO.VertexBufferObjectManager;
import flakor.game.system.input.LocationListenerInterface;
import flakor.game.system.input.LocationProviderStatus;
import flakor.game.system.input.Sensor.AccelerationData;
import flakor.game.system.input.Sensor.AccelerationListener;
import flakor.game.system.input.Sensor.OrientationData;
import flakor.game.system.input.Sensor.OrientationListener;
import flakor.game.system.input.Sensor.SensorDelay;
import flakor.game.system.input.touch.MultiTouchController;
import flakor.game.system.input.touch.SingleTouchController;
import flakor.game.system.input.touch.TouchControllerInterface;
import flakor.game.system.input.touch.TouchEvent;
import flakor.game.system.input.touch.TouchEventCallback;
import flakor.game.tool.Debug;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by Saint Hsu on 13-7-11.<br>
 * 引擎包装封装类，执行引擎的加载更新销毁等<br>
 * @version 0.0.1beta<br>
 * 
 */

public class Engine implements SensorEventListener, View.OnTouchListener, TouchEventCallback, LocationListener
{
    // ===========================================================
    // Constants
    // ===========================================================

    private static final SensorDelay SENSOR_DELAY_DEFAULT = SensorDelay.GAME;
    private static final int UPDATE_HANDLERS_CAPACITY_DEFAULT = 8;
    private static final int DRAW_HANDLERS_CAPACITY_DEFAULT = 4;

    // ===========================================================
    // Fields
    // ===========================================================

    private boolean running;
    private boolean destroyed;

    private long lastTick;
    private float secondsElapsedTotal;

    private final EngineLock engineLock;

    private final UpdateThread updateThread;
    private final RunnableHandler updateThreadRunnableHandler = new RunnableHandler();

    private final EngineConfig engineOptions;
    protected final Camera camera;

    private TouchControllerInterface touchController;

    private final VertexBufferObjectManager vertexBufferObjectManager = new VertexBufferObjectManager();
    private final TextureManager textureManager;
    private final FontManager fontManager;
    private final ShaderProgramManager shaderProgramManager = new ShaderProgramManager();

    private final SoundManager soundManager;
    private final MusicManager musicManager;

    private Vibrator vibrator;

    private LocationListenerInterface locationListener;
    private Location location;

    private AccelerationListener accelerationListener;
    private AccelerationData accelerationData;

    private OrientationListener orientationListener;
    private OrientationData orientationData;

    private final UpdateHandlerList updateHandlers = new UpdateHandlerList(Engine.UPDATE_HANDLERS_CAPACITY_DEFAULT);
    private final DrawHandlerList drawHandlers = new DrawHandlerList(Engine.DRAW_HANDLERS_CAPACITY_DEFAULT);

    protected Scene scene;

    protected int surfaceWidth = 1; // 1 to prevent accidental DIV/0
    protected int surfaceHeight = 1; // 1 to prevent accidental DIV/0

    // ===========================================================
    // Constructors
    // ===========================================================
    
    /**
     * 通过引擎配置创建一个引擎实例
     * @param engineOptions 引擎配置
     * @see EngineConfig
     */
    public Engine(final EngineConfig engineOptions)
    {
		/* Initialize Factory and Manager classes. */
        BitmapTextureAtlasTextureRegionFactory.reset();
        SoundFactory.onCreate();
        MusicFactory.onCreate();
        FontFactory.onCreate();
        this.vertexBufferObjectManager.onCreate();
        this.textureManager = TextureManager.onCreate();
        this.fontManager = FontManager.onCreate();
        this.shaderProgramManager.onCreate();

		/* Apply EngineConfig. */
        this.engineOptions = engineOptions;
        if(this.engineOptions.hasEngineLock())
        {
            this.engineLock = engineOptions.getEngineLock();
        }
        else
        {
            this.engineLock = new EngineLock(false);
        }
        this.camera = engineOptions.getCamera();

		/* Touch. */
        if(this.engineOptions.getTouchConfig().isMultiTouch())
        {
            this.setTouchController(new MultiTouchController());
        }
        else
        {
            this.setTouchController(new SingleTouchController());
        }

		/* Audio. */
        if(this.engineOptions.getAudioConfig().isSoundOn())
        {
            this.soundManager = new SoundManager(this.engineOptions.getAudioConfig().getMaxSimultaneousStreams());
        }
        else
        {
            this.soundManager = null;
        }

        if(this.engineOptions.getAudioConfig().isMusicOn())
        {
            this.musicManager = new MusicManager();
        }
        else
        {
            this.musicManager = null;
        }

		/* Start the UpdateThread. */
        if(this.engineOptions.hasUpdateThread())
        {
            this.updateThread = this.engineOptions.getUpdateThread();
        }
        else
        {
            this.updateThread = new UpdateThread();
        }
        this.updateThread.setEngine(this);
    }

    /**
     * 更新进程开启
     * @throws IllegalThreadStateException
     */
    public void startUpdateThread() throws IllegalThreadStateException
    {
        this.updateThread.start();
    }

    // ===========================================================
    // Getter & Setter
    // ===========================================================

    /**
     * 引擎是否运行
     * @return true if running
     */
    public synchronized boolean isRunning()
    {
        return this.running;
    }

    public synchronized void start()
    {
        if(!this.running)
        {
            this.lastTick = System.nanoTime();
            this.running = true;
        }
    }

    public synchronized void stop()
    {
        if(this.running)
        {
            this.running = false;
        }
    }

    /**
     * {@link EngineLock}经常锁定/解锁，以确保在更新进程和opengl进程直接能准确地运行。<br>
     * The {@link EngineLock} can be used to {@link EngineLock#lock()}/{@link EngineLock#unlock()} on, to ensure the code in between runs mutually exclusive to the {@link UpdateThread} and the GL{@link Thread}.
     * When the caller already is on the {@link UpdateThread} or the GL-{@link Thread}, that code is executed immediately.
     * @return the {@link EngineLock} the {@link Engine} locks on to ensure mutually exclusivity to the {@link UpdateThread} and the GL{@link Thread}.
     */

    public EngineLock getEngineLock()
    {
        return this.engineLock;
    }

    /**
     * 获取引擎主场景
     * @return Scene 场景
     */
    public Scene getScene()
    {
        return this.scene;
    }

    /**
     * 设置引擎主场景
     * @param scene
     */
    public void setScene(final Scene scene)
    {
        this.scene = scene;
    }

    public EngineConfig getEngineOptions()
    {
        return this.engineOptions;
    }

    public Camera getCamera()
    {
        return this.camera;
    }

    /**
     * 获取总的运行时间
     * @return
     */
    public float getSecondsElapsedTotal()
    {
        return this.secondsElapsedTotal;
    }

    /**
     * 设置引擎屏幕的尺寸
     * @param surfaceWidth
     * @param surfaceHeight
     */
    public void setSurfaceSize(final int surfaceWidth, final int surfaceHeight)
    {
        this.surfaceWidth = surfaceWidth;
        this.surfaceHeight = surfaceHeight;
        this.onUpdateCameraSurface();
    }

    /**
     * 更新相机的屏幕尺寸
     */
    protected void onUpdateCameraSurface()
    {
        this.camera.setSurfaceSize(0, 0, this.surfaceWidth, this.surfaceHeight);
    }

    public int getSurfaceWidth()
    {
        return this.surfaceWidth;
    }

    public int getSurfaceHeight()
    {
        return this.surfaceHeight;
    }

    public TouchControllerInterface getTouchController()
    {
        return this.touchController;
    }

    public void setTouchController(final TouchControllerInterface touchController)
    {
        this.touchController = touchController;
        this.touchController.setTouchEventCallback(this);
    }

    public AccelerationData getAccelerationData()
    {
        return this.accelerationData;
    }

    public OrientationData getOrientationData()
    {
        return this.orientationData;
    }

    public VertexBufferObjectManager getVertexBufferObjectManager()
    {
        return this.vertexBufferObjectManager;
    }

    public TextureManager getTextureManager()
    {
        return this.textureManager;
    }

    public FontManager getFontManager()
    {
        return this.fontManager;
    }

    public ShaderProgramManager getShaderProgramManager()
    {
        return this.shaderProgramManager;
    }

    public SoundManager getSoundManager() throws IllegalStateException
    {
        if(this.soundManager != null)
        {
            return this.soundManager;
        }
        else
        {
            throw new IllegalStateException("To enable the SoundManager, check the EngineConfig!");
        }
    }

    public MusicManager getMusicManager() throws IllegalStateException
    {
        if(this.musicManager != null)
        {
            return this.musicManager;
        }
        else
        {
            throw new IllegalStateException("To enable the MusicManager, check the EngineConfig!");
        }
    }

    public void registerUpdateHandler(final UpdatableInterface pUpdateHandler)
    {
        this.updateHandlers.add(pUpdateHandler);
    }

    public void unregisterUpdateHandler(final UpdatableInterface pUpdateHandler)
    {
        this.updateHandlers.remove(pUpdateHandler);
    }

    public void clearUpdateHandlers()
    {
        this.updateHandlers.clear();
    }

    public void registerDrawHandler(final DrawInterface pDrawHandler)
    {
        this.drawHandlers.add(pDrawHandler);
    }

    public void unregisterDrawHandler(final DrawInterface pDrawHandler)
    {
        this.drawHandlers.remove(pDrawHandler);
    }

    public void clearDrawHandlers()
    {
        this.drawHandlers.clear();
    }

    // ===========================================================
    // Methods for/from SuperClass/Interfaces
    // ===========================================================

    @Override
    public void onAccuracyChanged(final Sensor sensor, final int accuracy)
    {
        if(this.running)
        {
            switch(sensor.getType())
            {
                case Sensor.TYPE_ACCELEROMETER:
                    if(this.accelerationData != null)
                    {
                        this.accelerationData.setAccuracy(accuracy);
                        this.accelerationListener.onAccelerationAccuracyChanged(this.accelerationData);
                    }
                    else if(this.orientationData != null)
                    {
                        this.orientationData.setAccelerationAccuracy(accuracy);
                        this.orientationListener.onOrientationAccuracyChanged(this.orientationData);
                    }
                    break;
                case Sensor.TYPE_MAGNETIC_FIELD:
                    this.orientationData.setMagneticFieldAccuracy(accuracy);
                    this.orientationListener.onOrientationAccuracyChanged(this.orientationData);
                    break;
            }
        }
    }

    @Override
    public void onSensorChanged(final SensorEvent event)
    {
        if(this.running)
        {
            switch(event.sensor.getType())
            {
                case Sensor.TYPE_ACCELEROMETER:
                    if(this.accelerationData != null)
                    {
                        this.accelerationData.setValues(event.values);
                        this.accelerationListener.onAccelerationChanged(this.accelerationData);
                    }
                    else if(this.orientationData != null)
                    {
                        this.orientationData.setAccelerationValues(event.values);
                        this.orientationListener.onOrientationChanged(this.orientationData);
                    }
                    break;
                case Sensor.TYPE_MAGNETIC_FIELD:
                    this.orientationData.setMagneticFieldValues(event.values);
                    this.orientationListener.onOrientationChanged(this.orientationData);
                    break;
            }
        }
    }

    @Override
    public void onLocationChanged(final Location location)
    {
        if(this.location == null)
        {
            this.location = location;
        }
        else
        {
            if(location == null)
            {
                this.locationListener.onLocationLost();
            }
            else
            {
                this.location = location;
                this.locationListener.onLocationChanged(location);
            }
        }
    }

    @Override
    public void onProviderDisabled(final String provider)
    {
        this.locationListener.onLocationProviderDisabled();
    }

    @Override
    public void onProviderEnabled(final String provider)
    {
        this.locationListener.onLocationProviderEnabled();
    }

    @Override
    public void onStatusChanged(final String provider, final int status, final Bundle extras)
    {
        switch(status)
        {
            case LocationProvider.AVAILABLE:
                this.locationListener.onLocationProviderStatusChanged(LocationProviderStatus.AVAILABLE, extras);
                break;
            case LocationProvider.OUT_OF_SERVICE:
                this.locationListener.onLocationProviderStatusChanged(LocationProviderStatus.OUT_OF_SERVICE, extras);
                break;
            case LocationProvider.TEMPORARILY_UNAVAILABLE:
                this.locationListener.onLocationProviderStatusChanged(LocationProviderStatus.TEMPORARILY_UNAVAILABLE, extras);
                break;
        }
    }

    @Override
    public boolean onTouch(final View pView, final MotionEvent surfaceMotionEvent)
    {
        if(this.running)
        {
            this.touchController.onHandleMotionEvent(surfaceMotionEvent);
            try
            {
				/* Because a human cannot interact 1000x per second, we pause the UI-Thread for a little. */
                Thread.sleep(this.engineOptions.getTouchConfig().getTouchEventIntervalMilliseconds());
            }
            catch (final InterruptedException e)
            {
                Debug.e(e);
            }
            return true;
        }
        else
        {
            return false;
        }
    }

    /**
     * 触摸事件
     * 引擎决定哪个场景和相机处理这个触摸事件
     */
    @Override
    public boolean onTouchEvent(final TouchEvent surfaceTouchEvent)
    {
		/*
		 * Let the engine determine which scene and camera this event should be
		 * handled by.
		 */
        final Scene scene = this.getSceneFromSurfaceTouchEvent(surfaceTouchEvent);
        final Camera camera = this.getCameraFromSurfaceTouchEvent(surfaceTouchEvent);

        this.convertSurfaceToSceneTouchEvent(camera, surfaceTouchEvent);

        if(this.onTouchHUD(camera, surfaceTouchEvent))
        {
            return true;
        }
        else
        {
			/* If HUD didn't handle it, Scene may handle it. */
            return this.onTouchScene(scene, surfaceTouchEvent);
        }
    }

    protected boolean onTouchHUD(final Camera camera, final TouchEvent sceneTouchEvent)
    {
        if(camera.hasHUD())
        {
            return camera.getHUD().onLayerTouchEvent(sceneTouchEvent);
        }
        else
        {
            return false;
        }
    }

    protected boolean onTouchScene(final Scene scene, final TouchEvent sceneTouchEvent)
    {
        if(scene != null)
        {
            return scene.onSceneTouchEvent(sceneTouchEvent);
        }
        else
        {
            return false;
        }
    }

    // ===========================================================
    // Methods
    // ===========================================================

    public void runOnUpdateThread(final Runnable runnable)
    {
        this.runOnUpdateThread(runnable, true);
    }

    /**
     * 当你想要在更新进程{@link UpdateThread}中执行代码，这个方法就会用到。
     * This method is useful when you want to execute code on the {@link UpdateThread}, even though the Engine is paused.
     *
     * @param runnable the {@link Runnable} to be run on the {@link UpdateThread}.
     * @param onlyWhenEngineRunning if <code>true</code>, the execution of the {@link Runnable} will be delayed until the next time {@link Engine#onUpdateUpdateHandlers(float)} is picked up, which is when {@link Engine#isRunning()} is <code>true</code>.
     * 								 if <code>false</code>, the execution of the {@link Runnable} will happen as soon as possible on the {@link UpdateThread}, no matter what {@link Engine#isRunning()} is.
     */
    public void runOnUpdateThread(final Runnable runnable, final boolean onlyWhenEngineRunning)
    {
        if(onlyWhenEngineRunning)
        {
            this.updateThreadRunnableHandler.postRunnable(runnable);
        }
        else
        {
            this.updateThread.postRunnable(runnable);
        }
    }

    /**
     * @param runnable the {@link Runnable} to run mutually exclusive to the {@link UpdateThread} and the GL-{@link Thread}.
     * When the caller already is on the {@link UpdateThread} or the GL-{@link Thread}, the {@link Runnable} is executed immediately.
     * @see {@link Engine#getEngineLock()} to manually {@link EngineLock#lock()}/{@link EngineLock#unlock()} on, while avoiding creating a {@link Runnable}.
     */
    public void runSafely(final Runnable runnable)
    {
        this.engineLock.lock();
        try
        {
            runnable.run();
        }
        finally
        {
            this.engineLock.unlock();
        }
    }

    public void onDestroy()
    {
        this.engineLock.lock();
        try
        {
            this.destroyed = true;
            this.engineLock.notifyCanUpdate();
        }
        finally
        {
            this.engineLock.unlock();
        }
        try
        {
            this.updateThread.join();
        }
        catch (final InterruptedException e)
        {
            Debug.e("Could not join UpdateThread.", e);
            Debug.warn("Trying to manually interrupt UpdateThread.");
            this.updateThread.interrupt();
        }

        this.vertexBufferObjectManager.onDestroy();
        this.textureManager.onDestroy();
        this.fontManager.onDestroy();
        this.shaderProgramManager.onDestroy();
    }

    /**
     * 引擎资源重载。包括以下管理器的重载：<br>
     * {@link VertexBufferObjectManager}，{@link TextureManager}，{@link FontManager}，{@link ShaderProgramManager}
     */
    public void onReloadResources()
    {
        this.vertexBufferObjectManager.onReload();
        this.textureManager.onReload();
        this.fontManager.onReload();
        this.shaderProgramManager.onReload();
    }

    protected Camera getCameraFromSurfaceTouchEvent(final TouchEvent touchEvent)
    {
        return this.getCamera();
    }

    protected Scene getSceneFromSurfaceTouchEvent(final TouchEvent touchEvent)
    {
        return this.scene;
    }

    protected void convertSurfaceToSceneTouchEvent(final Camera camera, final TouchEvent surfaceTouchEvent)
    {
        camera.convertSurfaceToSceneTouchEvent(surfaceTouchEvent, this.surfaceWidth, this.surfaceHeight);
    }

    protected void convertSceneToSurfaceTouchEvent(final Camera camera, final TouchEvent surfaceTouchEvent)
    {
        camera.convertSceneToSurfaceTouchEvent(surfaceTouchEvent, this.surfaceWidth, this.surfaceHeight);
    }

    /**
     * 微秒更新
     * @throws InterruptedException
     */
    void onTickUpdate() throws InterruptedException
    {
        if(this.running)
        {
            final long secondsElapsed = this.getNanosecondsElapsed();

            this.engineLock.lock();
            try
            {
                this.throwOnDestroyed();

                this.onUpdate(secondsElapsed);

                this.throwOnDestroyed();

                this.engineLock.notifyCanDraw();
                this.engineLock.waitUntilCanUpdate();
            }
            finally
            {
                this.engineLock.unlock();
            }
        }
        else
        {
            this.engineLock.lock();
            try
            {
                this.throwOnDestroyed();

                this.engineLock.notifyCanDraw();
                this.engineLock.waitUntilCanUpdate();
            }
            finally
            {
                this.engineLock.unlock();
            }

            Thread.sleep(16);
        }
    }

    private void throwOnDestroyed() throws EngineDestroyedException
    {
        if(this.destroyed)
        {
            throw new EngineDestroyedException();
        }
    }

    /**
     * 引擎主更新方法
     * @param nanosecondsElapsed
     * @throws InterruptedException
     */
    public void onUpdate(final long nanosecondsElapsed) throws InterruptedException
    {
        final float pSecondsElapsed = nanosecondsElapsed * TimeConstants.SECONDS_PER_NANOSECOND;

        this.secondsElapsedTotal += pSecondsElapsed;
        this.lastTick += nanosecondsElapsed;

        this.touchController.onUpdate(pSecondsElapsed);
        this.onUpdateUpdateHandlers(pSecondsElapsed);
        this.onUpdateScene(pSecondsElapsed);
    }

    protected void onUpdateScene(final float secondsElapsed)
    {
        if(this.scene != null)
        {
            this.scene.onUpdate(secondsElapsed);
        }
    }

    protected void onUpdateUpdateHandlers(final float secondsElapsed)
    {
        this.updateThreadRunnableHandler.onUpdate(secondsElapsed);
        this.updateHandlers.onUpdate(secondsElapsed);
        this.getCamera().onUpdate(secondsElapsed);
    }

    protected void onUpdateDrawHandlers(final GLState glState, final Camera camera)
    {
        this.drawHandlers.onDraw(glState, camera);
    }

    public void onDrawFrame(final GLState pGLState) throws InterruptedException
    {
        final EngineLock engineLock = this.engineLock;

        engineLock.lock();
        try
        {
            engineLock.waitUntilCanDraw();

            this.vertexBufferObjectManager.updateVertexBufferObjects(pGLState);
            this.textureManager.updateTextures(pGLState);
            this.fontManager.updateFonts(pGLState);

            this.onUpdateDrawHandlers(pGLState, this.camera);
            this.onDrawScene(pGLState, this.camera);

            engineLock.notifyCanUpdate();
        } 
        finally
        {
            engineLock.unlock();
        }
    }

    protected void onDrawScene(final GLState glState, final Camera camera)
    {
        if(this.scene != null)
        {
            this.scene.onDraw(glState, camera);
        }

        camera.onDrawHUD(glState);
    }

    
    private long getNanosecondsElapsed()
    {
        final long now = System.nanoTime();

        return now - this.lastTick;
    }

    /**
     * 开启震动
     * @param context
     * @return
     */
    public boolean enableVibrator(final Context context)
    {
        this.vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        return this.vibrator != null;
    }

    public void vibrate(final long pMilliseconds) throws IllegalStateException
    {
        if(this.vibrator != null)
        {
            this.vibrator.vibrate(pMilliseconds);
        }
        else
        {
            throw new IllegalStateException("You need to enable the Vibrator before you can use it!");
        }
    }

    public void vibrate(final long[] pPattern, final int pRepeat) throws IllegalStateException
    {
        if(this.vibrator != null)
        {
            this.vibrator.vibrate(pPattern, pRepeat);
        }
        else
        {
            throw new IllegalStateException("You need to enable the Vibrator before you can use it!");
        }
    }

    /**
     * 关闭震动
     */
    public void disableVibrate()
    {
    	this.vibrator = null;
    }
    
    public void enableLocationSensor(final Context context, final LocationListenerInterface locationListener, final LocationSensorOptions locationSensorOptions)
    {
        this.locationListener = locationListener;

        final LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        final String locationProvider = locationManager.getBestProvider(locationSensorOptions, locationSensorOptions.isEnabledOnly());

        // TODO locationProvider can be null, in that case return false. Successful case should return true.
        locationManager.requestLocationUpdates(locationProvider, locationSensorOptions.getMinimumTriggerTime(), locationSensorOptions.getMinimumTriggerDistance(), this);

        this.onLocationChanged(locationManager.getLastKnownLocation(locationProvider));
    }

    public void disableLocationSensor(final Context pContext)
    {
        final LocationManager locationManager = (LocationManager) pContext.getSystemService(Context.LOCATION_SERVICE);
        locationManager.removeUpdates(this);
    }

    /**
     * @see {@link Engine#enableAccelerationSensor(Context, AccelerationListener, flakor.game.console.opition.AccelerationSensorOptions)}
     */
    public boolean enableAccelerationSensor(final Context pContext, final AccelerationListener pAccelerationListener)
    {
        return this.enableAccelerationSensor(pContext, pAccelerationListener, new AccelerationSensorOptions(Engine.SENSOR_DELAY_DEFAULT));
    }

    /**
     * @return <code>true</code> when the sensor was successfully enabled, <code>false</code> otherwise.
     */
    public boolean enableAccelerationSensor(final Context context, final AccelerationListener pAccelerationListener, final AccelerationSensorOptions pAccelerationSensorOptions)
    {
        final SensorManager sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        if(Engine.isSensorSupported(sensorManager, Sensor.TYPE_ACCELEROMETER))
        {
            this.accelerationListener = pAccelerationListener;

            if(this.accelerationData == null)
            {
                final Display display = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
                final int displayRotation = display.getRotation();
                this.accelerationData = new AccelerationData(displayRotation);
            }

            this.registerSelfAsSensorListener(sensorManager, Sensor.TYPE_ACCELEROMETER, pAccelerationSensorOptions.getSensorDelay());

            return true;
        }
        else
        {
            return false;
        }
    }


    /**
     * @return <code>true</code> when the sensor was successfully disabled, <code>false</code> otherwise.
     */
    public boolean disableAccelerationSensor(final Context context)
    {
        final SensorManager sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        if(Engine.isSensorSupported(sensorManager, Sensor.TYPE_ACCELEROMETER))
        {
            this.unregisterSelfAsSensorListener(sensorManager, Sensor.TYPE_ACCELEROMETER);
            return true;
        }
        else
        {
            return false;
        }
    }

    /**
     * @see {@link Engine#enableOrientationSensor(Context, OrientationListener, flakor.game.console.opition.OrientationSensorOptions)}
     */
    public boolean enableOrientationSensor(final Context pContext, final OrientationListener pOrientationListener) {
        return this.enableOrientationSensor(pContext, pOrientationListener, new OrientationSensorOptions(Engine.SENSOR_DELAY_DEFAULT));
    }

    /**
     * @return <code>true</code> when the sensor was successfully enabled, <code>false</code> otherwise.
     */
    public boolean enableOrientationSensor(final Context pContext, final OrientationListener pOrientationListener, final OrientationSensorOptions pOrientationSensorOptions)
    {
        final SensorManager sensorManager = (SensorManager) pContext.getSystemService(Context.SENSOR_SERVICE);
        if(Engine.isSensorSupported(sensorManager, Sensor.TYPE_ACCELEROMETER) && Engine.isSensorSupported(sensorManager, Sensor.TYPE_MAGNETIC_FIELD)) {
            this.orientationListener = pOrientationListener;

            if(this.orientationData == null)
            {
                final Display display = ((WindowManager) pContext.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
                final int displayRotation = display.getRotation();
                this.orientationData = new OrientationData(displayRotation);
            }

            this.registerSelfAsSensorListener(sensorManager, Sensor.TYPE_ACCELEROMETER, pOrientationSensorOptions.getSensorDelay());
            this.registerSelfAsSensorListener(sensorManager, Sensor.TYPE_MAGNETIC_FIELD, pOrientationSensorOptions.getSensorDelay());

            return true;
        } else {
            return false;
        }
    }


    /**
     * @return <code>true</code> when the sensor was successfully disabled, <code>false</code> otherwise.
     */
    public boolean disableOrientationSensor(final Context pContext)
    {
        final SensorManager sensorManager = (SensorManager) pContext.getSystemService(Context.SENSOR_SERVICE);
        if(Engine.isSensorSupported(sensorManager, Sensor.TYPE_ACCELEROMETER) && Engine.isSensorSupported(sensorManager, Sensor.TYPE_MAGNETIC_FIELD)) {
            this.unregisterSelfAsSensorListener(sensorManager, Sensor.TYPE_ACCELEROMETER);
            this.unregisterSelfAsSensorListener(sensorManager, Sensor.TYPE_MAGNETIC_FIELD);
            return true;
        } else {
            return false;
        }
    }

    private static boolean isSensorSupported(final SensorManager pSensorManager, final int pType)
    {
        return pSensorManager.getSensorList(pType).size() > 0;
    }

    private void registerSelfAsSensorListener(final SensorManager pSensorManager, final int pType, final SensorDelay pSensorDelay)
    {
        final Sensor sensor = pSensorManager.getSensorList(pType).get(0);
        pSensorManager.registerListener(this, sensor, pSensorDelay.getDelay());
    }

    private void unregisterSelfAsSensorListener(final SensorManager pSensorManager, final int pType)
    {
        final Sensor sensor = pSensorManager.getSensorList(pType).get(0);
        pSensorManager.unregisterListener(this, sensor);
    }

    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================
    /**
     * 游戏引擎更新进程
     * @author saint
     *
     */
    public static class UpdateThread extends Thread
    {

        // ===========================================================
        // Fields
        // ===========================================================

        private Engine engine;
        private final RunnableHandler runnableHandler = new RunnableHandler();

        // ===========================================================
        // Constructors
        // ===========================================================

        public UpdateThread()
        {
            super(UpdateThread.class.getSimpleName());
        }

        // ===========================================================
        // Getter & Setter
        // ===========================================================

        public void setEngine(final Engine engine)
        {
            this.engine = engine;
        }

        // ===========================================================
        // Methods for/from SuperClass/Interfaces
        // ===========================================================

        @Override
        public void run()
        {
            android.os.Process.setThreadPriority(this.engine.getEngineOptions().getUpdateThreadPriority());
            try
            {
                while(true)
                {
                    this.runnableHandler.onUpdate(0);
                    this.engine.onTickUpdate();
                }
            }
            catch (final InterruptedException e)
            {
                if(EngineConfig.DEBUG)
                {
                    Debug.d(this.getClass().getSimpleName() + " interrupted. Don't worry - this " + e.getClass().getSimpleName() + " is most likely expected!", e);
                }
                this.interrupt();
            }
        }

        // ===========================================================
        // Methods
        // ===========================================================

        public void postRunnable(final Runnable runnable)
        {
            this.runnableHandler.postRunnable(runnable);
        }

    }

    public class EngineDestroyedException extends InterruptedException
    {
        // ===========================================================
        // Constants
        // ===========================================================

        private static final long serialVersionUID = -4691263961728972560L;

        // ===========================================================
        // Constructors
        // ===========================================================

    }

    public static class EngineLock extends ReentrantLock
    {
        // ===========================================================
        // Constants
        // ===========================================================

        private static final long serialVersionUID = 671220941302523934L;

        // ===========================================================
        // Fields
        // ===========================================================

        final Condition drawingCondition = this.newCondition();
        final AtomicBoolean drawing = new AtomicBoolean(false);

        // ===========================================================
        // Constructors
        // ===========================================================

        public EngineLock(final boolean fair)
        {
            super(fair);
        }

        // ===========================================================
        // Methods
        // ===========================================================

        void notifyCanDraw()
        {
            this.drawing.set(true);
            this.drawingCondition.signalAll();
        }

        void notifyCanUpdate()
        {
            this.drawing.set(false);
            this.drawingCondition.signalAll();
        }

        void waitUntilCanDraw() throws InterruptedException
        {
            while(!this.drawing.get())
            {
                this.drawingCondition.await();
            }
        }

        void waitUntilCanUpdate() throws InterruptedException
        {
            while(this.drawing.get())
            {
                this.drawingCondition.await();
            }
        }

    }
}
