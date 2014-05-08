/*
 * Action.java
 * Created on 8/25/13 12:42 PM
 *
 * ver0.0.1beta 8/25/13 saint
 * Copyright (c) 2013 Saint Hsu All Rights Reserved.
 */

package flakor.game.core.modifier;

import flakor.game.core.entity.Entity;
import flakor.game.core.entity.EntityInterface;

/**
 * Created by saint on 8/25/13.
 */
public abstract class Action implements Copyable
{
    //非法的标记，为-1
    public static final int INVALID_TAG = -1;
    //源目标
    private EntityInterface mOriginalTarget;
    //目标节点
    public EntityInterface mTarget;
    //动画标记
    private int mTag;
    //回调
    private Callback mCallback;
    //得到源节点
    public EntityInterface getOriginalTarget()
    {
        return this.mOriginalTarget;
    }
    //设置源节点
    public void setOriginalTarget(Entity value)
    {
        this.mOriginalTarget = value;
    }
    //得到目标
    public EntityInterface getTarget()
    {
        return this.mTarget;
    }
    //设置目标
    public void setTarget(Entity value)
    {
        this.mTarget = value;
    }
    //设置回调
    public void setCallback(Callback callback)
    {
        this.mCallback = callback;
    }
    //得到回调
    public Callback getCallback()
    {
        return this.mCallback;
    }
    //得到，设置tag
    public int getTag()
    {
        return this.mTag;
    }
    public void setTag(int value)
    {
        this.mTag = value;
    }
    //构建动作
    protected Action() {
        this.mTarget = (this.mOriginalTarget = null);
        this.mTag = INVALID_TAG;
    }
    //拷贝动作
    public abstract Action copy();
    //生成反响动作
    public abstract Action reverse();
    //开始在指定节点上执行动作
    public void start(EntityInterface targer)
    {
        this.mOriginalTarget = (this.mTarget = targer);
    }
    //停止
    public void stop()
    {
        this.mTarget = null;
    }
    //是否完成
    public boolean isDone()
    {
        return true;
    }
    //单步执行
    public abstract void step(float paramFloat);
    //更新
    public abstract void update(float paramFloat);
    //回调接口，动作完成之后进入
    public static abstract interface Callback
    {
        public abstract void onDone(Action paramAction);
    }
}