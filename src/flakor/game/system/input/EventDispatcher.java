/*
 * EventDispatcher.java
 * Created on 9/4/13 9:16 PM
 *
 * ver0.0.1beta 9/4/13 saint
 * Copyright (c) 2013 Saint Hsu All Rights Reserved.
 */

package flakor.game.system.input;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.view.KeyEvent;
import android.view.MotionEvent;

import flakor.game.system.input.Key.KeyHandlerInterface;
import flakor.game.system.input.Key.PriorityKeyHandler;
import flakor.game.system.input.Sensor.AccelerometerHandlerInterface;
import flakor.game.system.input.touch.PriorityTouchHandler;
import flakor.game.system.input.touch.TouchHandlerInterface;

import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by saint on 9/4/13.
 */
public class EventDispatcher implements SensorEventListener
{
    private CopyOnWriteArrayList<PriorityTouchHandler> touchHandlers;
    private CopyOnWriteArrayList<PriorityKeyHandler> keyHandlers;
    private CopyOnWriteArrayList<AccelerometerHandlerInterface> accelHandlers;

    private boolean dispatchEvents;
    private SensorManager sensorManager;
    private Sensor accelerometer;
    private boolean sensorRegistered;
    private static EventDispatcher sInstance;

    public boolean getDispatchEvents()
    {
        return this.dispatchEvents;
    }

    public void setDispatchEvents(boolean b)
    {
        this.dispatchEvents = b;
    }

    public static EventDispatcher getInstance(Context context)
    {
        synchronized (EventDispatcher.class)
        {
            if (sInstance == null)
            {
                sInstance = new EventDispatcher(context);
            }
            return sInstance;
        }
    }

    protected EventDispatcher(Context context)
    {
        this.dispatchEvents = true;
        this.touchHandlers = new CopyOnWriteArrayList<PriorityTouchHandler>();
        this.keyHandlers = new CopyOnWriteArrayList<PriorityKeyHandler>();
        this.accelHandlers = new CopyOnWriteArrayList<AccelerometerHandlerInterface>();
        this.sensorManager = ((SensorManager) context.getSystemService("sensor"));
        if (this.sensorManager != null)
            this.accelerometer = this.sensorManager.getDefaultSensor(1);
        else
            this.accelerometer = null;
    }

    /**
     * 添加触摸事件
     */
    private void addTouchHandler(PriorityTouchHandler handler)
    {
        int i = 0;
        for (PriorityTouchHandler h : this.touchHandlers)
        {
            if (h.getPriority() > handler.getPriority())
            {
                ++i;
            }
            if (h.getWrappedHandler() == handler.getWrappedHandler())
                throw new RuntimeException("Handler already added to touch dispatcher.");
        }
        this.touchHandlers.add(i, handler);
    }

    /**
     *添加按键事件
     */
    private void addKeyHandler(PriorityKeyHandler handler)
    {
        int i = 0;
        for (PriorityKeyHandler h : this.keyHandlers)
        {
            if (h.getPriority() > handler.getPriority()) {
                ++i;
            }
            if (h.getWrappedHandler() == handler.getWrappedHandler())
                throw new RuntimeException(
                        "Handler already added to touch dispatcher.");
        }
        this.keyHandlers.add(i, handler);
    }

    //添加TouchHandler
    public void addTouchHandler(TouchHandlerInterface handler)
    {
        addTouchHandler(handler, 0);
    }

    public void addTouchHandler(TouchHandlerInterface handler, int prio)
    {
        addTouchHandler(new PriorityTouchHandler(handler, prio));
    }
    //添加KeyHandler
    public void addKeyHandler(KeyHandlerInterface handler)
    {
        addKeyHandler(handler, 0);
    }
    public void addKeyHandler(KeyHandlerInterface handler, int prio)
    {
        addKeyHandler(new PriorityKeyHandler(handler, prio));
    }

    //添加AccelHandler
    public void addAccelHandler(AccelerometerHandlerInterface handler)
    {
        this.accelHandlers.add(handler);
        checkAccelHandlers();
    }

    /**
     *检测AccelHandler
     *没有注册Sensor监听就注册监听
     */
    private void checkAccelHandlers()
    {
        if (this.accelHandlers.isEmpty())
        {
            if (this.sensorRegistered)
            {
                this.sensorManager.unregisterListener(this);
                this.sensorRegistered = false;
            }
            else
            {
                this.sensorRegistered = this.sensorManager.registerListener(this,
                        this.accelerometer, 1);
            }
        }
    }

    //下面4个函数分别为移除Handler
    public void removeTouchHandler(TouchHandlerInterface h)
    {
        if (h == null)
        {
            return;
        }
        for (PriorityTouchHandler handler : this.touchHandlers)
        {
            if (handler.getWrappedHandler() == h)
            {
                this.touchHandlers.remove(handler);
                return;
            }
        }
    }

    public void removeKeyHandler(KeyHandlerInterface h)
    {
        if (h == null)
        {
            return;
        }
        for (PriorityKeyHandler handler : this.keyHandlers)
        {
            if (handler.getWrappedHandler() == h) {
                this.keyHandlers.remove(handler);
                return;
            }
        }
    }
    public void removeAccelHandler(AccelerometerHandlerInterface h)
    {
        this.accelHandlers.remove(h);
        checkAccelHandlers();
    }

    public void removeAllHandlers()
    {
        this.touchHandlers.clear();
        this.keyHandlers.clear();
        this.accelHandlers.clear();
        checkAccelHandlers();
    }

    //下面两个函数用来设置优先级
    public void setTouchPriority(int priority, TouchHandlerInterface h)
    {
        if (h == null) {
            throw new RuntimeException("Got null touch handler");
        }
        int i = 0;
        for (PriorityTouchHandler handler : this.touchHandlers)
        {
            if (handler.getWrappedHandler() == h)
                break;
            ++i;
        }
        if (i == this.touchHandlers.size())
        {
            throw new RuntimeException("Touch handler not found");
        }

        PriorityTouchHandler handler = (PriorityTouchHandler) this.touchHandlers.get(i);
        if (handler.getPriority() != priority)
        {
            handler.setPriority(priority);
            this.touchHandlers.remove(handler);
            addTouchHandler(handler);
        }
    }

    public void setKeyPriority(int priority, KeyHandlerInterface h)
    {
        if (h == null) {
            throw new RuntimeException("Got null key handler");
        }
        int i = 0;
        for (PriorityKeyHandler handler : this.keyHandlers)
        {
            if (handler.getWrappedHandler() == h)
                break;
            ++i;
        }

        if (i == this.keyHandlers.size())
        {
            throw new RuntimeException("Key handler not found");
        }

        PriorityKeyHandler handler = (PriorityKeyHandler) this.keyHandlers.get(i);
        if (handler.getPriority() != priority)
        {
            handler.setPriority(priority);
            this.keyHandlers.remove(handler);
            addKeyHandler(handler);
        }
    }
    //处理事件
    public boolean keyDown(KeyEvent event)
    {
        if (this.dispatchEvents) {
            for (PriorityKeyHandler handler : this.keyHandlers)
            {
                if (handler.keyDown(event)) {
                    return true;
                }
            }
        }
        return false;
    }
    public boolean keyUp(KeyEvent event)
    {
        if (this.dispatchEvents)
        {
            for (PriorityKeyHandler handler : this.keyHandlers)
            {
                if (handler.keyUp(event)) {
                    return true;
                }
            }
        }
        return false;
    }
    public void touchesBegan(MotionEvent event)
    {
        if (this.dispatchEvents)
        {
            for (PriorityTouchHandler handler : this.touchHandlers)
                if (handler.touchesBegan(event))
                    return;
        }
    }

    public void touchesMoved(MotionEvent event)
    {
        if (this.dispatchEvents)
        {
            for (PriorityTouchHandler handler : this.touchHandlers)
                if (handler.touchesMoved(event))
                    return;
        }
    }

    public void touchesEnded(MotionEvent event)
    {
        if (this.dispatchEvents)
            for (PriorityTouchHandler handler : this.touchHandlers)
                if (handler.touchesEnded(event))
                    return;
    }

    public void touchesCancelled(MotionEvent event)
    {
        if (this.dispatchEvents)
        {
            for (PriorityTouchHandler handler : this.touchHandlers)
                if (handler.touchesCancelled(event))
                    return;
        }
    }

    public void onAccuracyChanged(Sensor sensor, int accuracy)
    {

    }

    //Sensor操作
    public void onSensorChanged(SensorEvent event)
    {
        if (event.sensor.getType() == 1)
            for (AccelerometerHandlerInterface handler : this.accelHandlers)
                handler.accelerometerChanged(event.values[0],
                        event.values[1], event.values[2]);
    }
}
