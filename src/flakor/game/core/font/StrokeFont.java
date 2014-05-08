package flakor.game.core.font;

import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.FloatMath;

import flakor.game.core.element.Color;
import flakor.game.system.graphics.opengl.Texture.TextureInterface;

/**
 * Created by longjiyang on 13-7-11.
 */
public class StrokeFont extends Font
{
    // ===========================================================
    // Fields
    // ===========================================================

    private final Paint mStrokePaint;
    private final boolean mStrokeOnly;
    private final float mStrokeWidth;

    // ===========================================================
    // Constructors
    // ===========================================================

    public StrokeFont( final TextureInterface pTexture, final Typeface pTypeface, final float pSize, final boolean pAntiAlias, final Color pColor, final float pStrokeWidth, final Color pStrokeColor) {
        this(pTexture, pTypeface, pSize, pAntiAlias, pColor.getARGBPackedInt(), pStrokeWidth, pStrokeColor.getARGBPackedInt());
    }

    public StrokeFont(final TextureInterface pTexture, final Typeface pTypeface, final float pSize, final boolean pAntiAlias, final int pColorARGBPackedInt, final float pStrokeWidth, final int pStrokeColorARGBPackedInt) {
        this( pTexture, pTypeface, pSize, pAntiAlias, pColorARGBPackedInt, pStrokeWidth, pStrokeColorARGBPackedInt, false);
    }

    public StrokeFont(final TextureInterface pTexture, final Typeface pTypeface, final float pSize, final boolean pAntiAlias, final Color pColor, final float pStrokeWidth, final Color pStrokeColor, final boolean pStrokeOnly) {
        this( pTexture, pTypeface, pSize, pAntiAlias, pColor.getARGBPackedInt(), pStrokeWidth, pStrokeColor.getARGBPackedInt(), pStrokeOnly);
    }

    public StrokeFont(final TextureInterface pTexture, final Typeface pTypeface, final float pSize, final boolean pAntiAlias, final int pColorARGBPackedInt, final float pStrokeWidth, final int pStrokeColorARGBPackedInt, final boolean pStrokeOnly) {
        super(pTexture, pTypeface, pSize, pAntiAlias, pColorARGBPackedInt);

        this.mStrokeWidth = pStrokeWidth;

        this.mStrokePaint = new Paint();
        this.mStrokePaint.setTypeface(pTypeface);
        this.mStrokePaint.setStyle(Paint.Style.STROKE);
        this.mStrokePaint.setStrokeWidth(pStrokeWidth);
        this.mStrokePaint.setColor(pStrokeColorARGBPackedInt);
        this.mStrokePaint.setTextSize(pSize);
        this.mStrokePaint.setAntiAlias(pAntiAlias);

        this.mStrokeOnly = pStrokeOnly;
    }

    // ===========================================================
    // Getter & Setter
    // ===========================================================

    // ===========================================================
    // Methods for/from SuperClass/Interfaces
    // ===========================================================

    @Override
    protected void updateTextBounds(final String pCharacterAsString)
    {
        this.mStrokePaint.getTextBounds(pCharacterAsString, 0, 1, this.mTextBounds);
        final int inset = -(int) FloatMath.floor(this.mStrokeWidth * 0.5f);
        this.mTextBounds.inset(inset, inset);
    }

    @Override
    protected void drawLetter(final String pCharacterAsString, final float pLeft, final float pTop)
    {
        if(!this.mStrokeOnly) {
            super.drawLetter(pCharacterAsString, pLeft, pTop);
        }
        this.mCanvas.drawText(pCharacterAsString, pLeft + Font.LETTER_TEXTURE_PADDING, pTop + Font.LETTER_TEXTURE_PADDING, this.mStrokePaint);
    }
}
