package flakor.game.core.animation;

import flakor.game.core.entity.Entity;

import java.util.List;

/**
 * Created by Saint Hsu on 13-7-25.
 */
public interface AnimationInterface
{
    //时间
    public abstract float getDuration();
    //获得帧
    public abstract List<? extends Entity.Frame> getFrames();
    //得到名称
    public abstract String getName();
}
