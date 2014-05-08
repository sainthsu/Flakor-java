package flakor.game.core.scene;

import android.annotation.TargetApi;
import android.opengl.GLES20;
import android.os.Build;

import flakor.game.core.camera.Camera;
import flakor.game.core.element.Color;
import flakor.game.core.entity.ModifierList;
import flakor.game.core.modifier.ModifierInterface;
import flakor.game.system.graphics.opengl.GLState;

/**
 * Created by Saint Hsu on 13-7-8.
 */
public class Background implements BackgroundInterface
{
    // ===========================================================
    // Constants
    // ===========================================================

    private static final int BACKGROUNDMODIFIERS_CAPACITY_DEFAULT = 4;

    // ===========================================================
    // Fields
    // ===========================================================

    private ModifierList<BackgroundInterface> backgroundModifiers = null;

    private final Color  color = new Color(0, 0, 0, 1);
    private boolean colorEnabled = true;

    // ===========================================================
    // Constructors
    // ===========================================================

    protected Background()
    {

    }

    public Background(final float red, final float pGreen, final float pBlue)
    {
        this.color.set(red, pGreen, pBlue);
    }

    public Background(final float pRed, final float pGreen, final float pBlue, final float pAlpha)
    {
        this.color.set(pRed, pGreen, pBlue, pAlpha);
    }

    public Background(final Color pColor)
    {
        this.color.set(pColor);
    }


    @Override
    public void registerBackgroundModifier(ModifierInterface<BackgroundInterface> backgroundModifier)
    {
        if(this.backgroundModifiers == null)
            this.allocateBackgroundModifiers();
        this.backgroundModifiers.add(backgroundModifier);
    }

    @Override
    public boolean unregisterBackgroundModifier(ModifierInterface<BackgroundInterface> backgroundModifier)
    {
        if (this.backgroundModifiers == null)
            return false;
        return this.backgroundModifiers.remove(backgroundModifier);
    }

    @Override
    public void clearBackgroundModifiers()
    {
        if (this.backgroundModifiers != null)
            this.backgroundModifiers.clear();
    }

    @Override
    public boolean isColorEnabled()
    {
        return this.colorEnabled;
    }

    @Override
    public void setColorEnabled(boolean colorEnabled)
    {
        this.colorEnabled = colorEnabled;
    }

    @Override
    public float getRed()
    {
        if (colorEnabled)
            return this.color.getRed();
        else
            return -1;
    }

    @Override
    public float getGreen()
    {
        if (colorEnabled)
            return this.color.getGreen();
        else
            return -1;
    }

    @Override
    public float getBlue()
    {
        if (colorEnabled)
            return this.color.getBlue();
        else
            return -1;
    }

    @Override
    public float getAlpha()
    {
        if (colorEnabled)
            return this.color.getAlpha();
        else
            return -1;
    }

    @Override
    public Color getColor()
    {
        if (colorEnabled)
            return this.color;
        else
            return null;
    }

    @Override
    public void setRed(float red)
    {
        if (colorEnabled)
            this.color.setRed(red);
    }

    @Override
    public void setGreen(float green)
    {
        if (colorEnabled)
            this.color.setGreen(green);
    }

    @Override
    public void setBlue(float blue)
    {
        if (colorEnabled)
            this.color.setBlue(blue);
    }

    @Override
    public void setAlpha(float alpha)
    {
        if (colorEnabled)
            this.color.setAlpha(alpha);
    }

    @Override
    public void setColor(Color color)
    {
        this.color.set(color);
    }

    @Override
    public void setColor(float red, float green, float blue)
    {
        this.color.set(red,green,blue);
    }

    @Override
    public void setColor(float red, float green, float blue, float alpha)
    {
        this.color.set(red,green,blue,alpha);
    }

    @TargetApi(Build.VERSION_CODES.FROYO)
    @Override
    public void onDraw(GLState GLState, Camera camera)
    {
        if(this.colorEnabled)
        {
            GLES20.glClearColor(this.color.getRed(),this.color.getGreen(),this.color.getBlue(),this.color.getAlpha());
            GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
        }
    }

    @Override
    public void onUpdate(float secondsElapsed)
    {
        if(this.backgroundModifiers != null)
        {
            this.backgroundModifiers.onUpdate(secondsElapsed);
        }
    }

    @Override
    public void reset()
    {
        this.backgroundModifiers.reset();
    }

    // ===========================================================
    // Methods
    // ===========================================================

    private void allocateBackgroundModifiers()
    {
        this.backgroundModifiers = new ModifierList<BackgroundInterface>(Background.BACKGROUNDMODIFIERS_CAPACITY_DEFAULT,this);
    }

}
