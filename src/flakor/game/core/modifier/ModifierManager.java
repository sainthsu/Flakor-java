/*
 * ModifierManager.java
 * Created on 8/25/13 1:06 PM
 *
 * ver0.0.1beta 8/25/13 saint
 * Copyright (c) 2013 Saint Hsu All Rights Reserved.
 */

package flakor.game.core.modifier;

import flakor.game.core.entity.EntityInterface;
import flakor.game.core.timer.Scheduler;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by saint on 8/25/13.
 */
public class ModifierManager
{
    //所有的动作列表，包括每个节点的动画
    private ConcurrentHashMap<EntityInterface, HashElement> targets;
    private static ModifierManager sInstance;

    static {
        sInstance = null;
    }

    //取得本类实例对象
    public static ModifierManager getInstance()
    {
        synchronized (ModifierManager.class) {
            if (sInstance == null) {
                sInstance = new ModifierManager();
            }
            return sInstance;
        }
    }

    //构建动作管理器对象
    private ModifierManager()
    {
        synchronized (ModifierManager.class)
        {
            //构建一个计时器
            //指定更新函数为tick
            Scheduler.getInstance().schedule(new Scheduler.Timer(this, "tick"));
            this.targets = new ConcurrentHashMap<EntityInterface, HashElement>(131);
        }
    }
    //清楚动作
    private void deleteHashElement(HashElement element)
    {
        element.actions.clear();
        this.targets.remove(element.target);
    }

    //动作列表
    private void actionAlloc(HashElement element) {
        if (element.actions == null)
            element.actions = new CopyOnWriteArrayList<Action>();
    }
    //移除指定动作
    private void removeAction(int index, HashElement element) {
        element.actions.remove(index);

        if (element.actions.size() == 0)
            deleteHashElement(element);
    }
    //暂停所有动作
    public void pauseAllActions(EntityInterface target) {
        HashElement element = (HashElement) this.targets.get(target);
        if (element != null)
            element.paused = true;
    }
    //重新开始所有动作
    public void resumeAllActions(EntityInterface target) {
        HashElement element = (HashElement) this.targets.get(target);
        if (element != null)
            element.paused = false;
    }
    //添加动作
    public void addAction(Action action, EntityInterface target, boolean paused) {
        assert (action != null) : "Argument action must be non-null";
        assert (target != null) : "Argument target must be non-null";

        HashElement element = (HashElement) this.targets.get(target);
        if (element == null) {
            element = new HashElement(target, paused);
            this.targets.put(target, element);
        }

        actionAlloc(element);

        assert (!element.actions.contains(action)) : "runAction: Action already running";

        element.actions.add(action);
        //开始动作
        action.start(target);
    }
    //移除所有动作
    public void removeAllActions()
    {
        for (HashElement element : this.targets.values())
            removeAllActions(element.target);
    }
    //移除指定节点的所有动作
    public void removeAllActions(EntityInterface target)
    {
        if (target == null) {
            return;
        }
        //先找到指定节点
        HashElement element = (HashElement) this.targets.get(target);
        if (element != null) {
            element.actions.clear();
            deleteHashElement(element);
        }
    }
    //移除指定动作
    public void removeAction(Action action)
    {
        //先找到动作的源目标
        HashElement element = (HashElement) this.targets.get(action
                .getOriginalTarget());
        if (element != null) {
            int i = element.actions.indexOf(action);
            if (i != Action.INVALID_TAG)
                removeAction(i, element);
        }
    }
    //移除指定
    public void removeAction(int tag, EntityInterface target)
    {
        assert (tag != Action.INVALID_TAG) : "Invalid tag";
        HashElement element = (HashElement) this.targets.get(target);
        if ((element == null) || (element.actions == null))
            return;
        int limit = element.actions.size();
        for (int i = 0; i < limit; ++i) {
            Action a = (Action) element.actions.get(i);
            if ((a.getTag() == tag) && (a.getOriginalTarget() == target))
                removeAction(i, element);
        }
    }
    //得到指定目标的指定动作
    public Action getAction(int tag, EntityInterface target)
    {
        assert (tag != Action.INVALID_TAG) : "Invalid tag";
        HashElement element = (HashElement) this.targets.get(target);
        if ((element != null) && (element.actions != null)) {
            int limit = element.actions.size();
            for (int i = 0; i < limit; ++i) {
                Action a = (Action) element.actions.get(i);
                if (a.getTag() == tag) {
                    return a;
                }
            }
        }
        return null;
    }
    //得到运行中的动作的数量
    public int getRunningActionCount(EntityInterface target)
    {
        HashElement element = (HashElement) this.targets.get(target);
        if (element != null) {
            return (element.actions != null) ? element.actions.size() : 0;
        }
        return 0;
    }
    //计时
    public void tick(float dt)
    {
        for (HashElement currentTarget : this.targets.values()) {
            if (!currentTarget.paused) {
                for (int actionIndex = 0; actionIndex < currentTarget.actions.size(); ++actionIndex) {
                    //取得动作
                    Action currentAction = (Action) currentTarget.actions
                            .get(actionIndex);
                    //设置单步执行
                    currentAction.step(dt);
                    //如果动作完成则调用所指定的回调函数
                    if (currentAction.isDone()) {
                        currentAction.stop();
                        if (currentAction.getCallback() != null) {
                            currentAction.getCallback().onDone(currentAction);
                        }
                        removeAction(currentAction);
                    }
                }
            }
            //没有动作就清楚
            if (currentTarget.actions.size() == 0)
                deleteHashElement(currentTarget);
        }
    }

    static class HashElement
    {
        //动作列表
        CopyOnWriteArrayList<Action> actions;
        //目标节点
        EntityInterface target;
        //是否暂停
        boolean paused;
        HashElement(EntityInterface t, boolean p)
        {
            this.target = t;
            this.paused = p;
        }

        public String toString()
        {
            String s = "target=" + this.target + ", paused=" + this.paused
                    + ", actions=" + this.actions + "\n";
            for (Action a : this.actions) {
                s = s + a.toString() + "\n";
            }
            return s;
        }
    }
}
