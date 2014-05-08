/*
 * Scheduler.java
 * Created on 8/24/13 10:16 PM
 *
 * ver0.0.1beta 8/24/13 saint
 * Copyright (c) 2013 Saint Hsu All Rights Reserved.
 */

package flakor.game.core.timer;

import android.util.Log;

import flakor.game.support.math.SmartList;
import flakor.game.tool.FlakorRuntimeException;

import java.lang.reflect.Method;

/**
 * Created by saint on 8/24/13.
 */
public class Scheduler
{
    private SmartList<Timer> scheduledMethods;
    private SmartList<Timer> methodsToRemove;
    private SmartList<Timer> methodsToAdd;
    private float timeScale;
    private static Scheduler singleton = null;

    public float getTimeScale()
    {
        return this.timeScale;
    }
    public void setTimeScale(float timeScale)
    {
        this.timeScale = timeScale;
    }

    //取得本类实例
    public static Scheduler getInstance()
    {
        synchronized (Scheduler.class)
        {
            if (singleton == null)
            {
                singleton = new Scheduler();
            }
            return singleton;
        }
    }

    private Scheduler()
    {
        this.scheduledMethods = new SmartList<Timer>(50);
        this.methodsToRemove = new SmartList<Timer>(20);
        this.methodsToAdd = new SmartList<Timer>(20);
        this.timeScale = 1.0F;
    }

    //添加一个指定的timer
    public void schedule(Timer t)
    {
        if (this.methodsToRemove.contains(t))
        {
            this.methodsToRemove.remove(t);
            return;
        }
        if ((this.scheduledMethods.contains(t))
                || (this.methodsToAdd.contains(t)))
        {
            throw new SchedulerTimerAlreadyScheduledException(
                    "Scheduler.scheduleTimer already scheduled");
        }
        this.methodsToAdd.add(t);
    }

    //卸载一个timer
    public void unschedule(Timer t)
    {
        if (this.methodsToAdd.contains(t))
        {
            this.methodsToAdd.remove(t);
            return;
        }
        if (!this.scheduledMethods.contains(t))
        {
            throw new SchedulerTimerNotFoundException(
                    "Scheduler.unscheduleTimer not found");
        }
        this.methodsToRemove.add(t);
    }

    //各计时器处理，，最核心部分放置在Timer的fire方法中
    public void tick(float delta)
    {
        if (this.timeScale != 1.0F)
        {
            delta *= this.timeScale;
        }
        for (Timer k : this.methodsToRemove)
            this.scheduledMethods.remove(k);
        this.methodsToRemove.clear();

        for (Timer k : this.methodsToAdd)
            this.scheduledMethods.add(k);
        this.methodsToAdd.clear();

        for (Timer t : this.scheduledMethods)
            t.fire(delta);
    }

    public static final class Timer
    {
        //目标对象
        private Object target;
        //函数名称
        private String selector;
        //要执行的方法，函数
        private Method invocation;
        //时间间隔
        private float interval;
        //用于存放已经过去的时间间隔，用于判断是否符合计时器指定的时间
        //如果达到标准则来执行所指定的方法
        private float secondElapsed;

        public Timer(Object t, String s)
        {
            this(t, s, 0.0F);
        }

        public Timer(Object t, String s, float seconds)
        {
            this.target = t;
            this.selector = s;
            this.interval = seconds;
            try
            {
                Class<? extends Object> cls = this.target.getClass();
                this.invocation = cls.getMethod(this.selector,
                        new Class[] { Float.TYPE });
            }
            catch (Exception e)
            {
                Log.w("Engine", "unable to find method: " + this.selector);
            }
        }

        public void setInterval(float i)
        {
            this.interval = i;
        }

        public float getInterval()
        {
            return this.interval;
        }

        //核心部分，处理计时器执行方法
        public void fire(float delta)
        {
            this.secondElapsed += delta;
            if (this.secondElapsed >= this.interval)
            {
                if (this.invocation != null)
                {
                    try
                    {
                        this.invocation.invoke(this.target,
                                new Object[] { Float.valueOf(this.secondElapsed) });
                    }
                    catch (Exception e)
                    {
                        Log.w("Engine", e.toString());
                    }
                }
                this.secondElapsed = 0.0F;
            }
        }
    }

    public class SchedulerTimerAlreadyScheduledException extends FlakorRuntimeException
    {
        /**
		 * 
		 */
		private static final long serialVersionUID = 610275952245105025L;

		public SchedulerTimerAlreadyScheduledException(String e)
        {
            super(e);
        }
    }

    public class SchedulerTimerNotFoundException extends FlakorRuntimeException
    {
        /**
		 * 
		 */
		private static final long serialVersionUID = -3424648460275878156L;

		public SchedulerTimerNotFoundException(String e)
        {
            super(e);
        }
    }
}
