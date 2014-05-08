package flakor.game.core.element;

/**
 * Created by Saint Hsu on 13-7-25.
 * 形状接口
 */
public interface SizableInterface
{
    public void setHeight(float height);
    public void setWidth(float width);
    public void setSize(Size size);

    public abstract float getHeight();
    public abstract float getWidth();
    public Size getSize();
}
