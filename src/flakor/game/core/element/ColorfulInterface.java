package flakor.game.core.element;

/**
 * Created by Saint Hsu on 13-7-25.
 */
public interface ColorfulInterface
{
    public float getRed();
    public float getGreen();
    public float getBlue();
    public float getAlpha();
    public Color getColor();

    public void setRed(final float red);
    public void setGreen(final float green);
    public void setBlue(final float blue);
    public void setAlpha(final float alpha);
    public void setColor(final Color color);
    public void setColor(final float red, final float green, final float blue);
    public void setColor(final float red, final float green, final float blue, final float alpha);
}
