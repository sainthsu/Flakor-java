package flakor.game.core.animation;

import flakor.game.core.entity.Entity;

/**
 * 实现此接口表示支持对单帧进行操作, 并且支持添加动画
 * Created by Saint Hsu on 13-7-25.
 */
public interface FramesInterface
{
    //添加动画
    public abstract void addAnimation(AnimationInterface animation);
    //得到动画名称
    public abstract AnimationInterface getAnimationByName(String name);
    public abstract Entity.Frame getDisplayFrame();
    //是否显示
    public abstract boolean isFrameDisplayed(Entity.Frame frame);
    //设置显示帧
    public abstract void setDisplayFrame(Entity.Frame frame);
    public abstract void setDisplayFrame(String paramString, int paramInt);
}
