package flakor.game.system.graphics.opengl.Texture;

import android.graphics.Bitmap;

import flakor.game.core.element.Size;

/**
 * Created by Steve Hsu on 13-7-14.
 */
public class EmptyBitmapTextureAtlasSource extends BaseTextureAtlasSource implements BitmapTextureAtlasSourceInterface
{
    // ===========================================================
    // Constructors
    // ===========================================================

    public EmptyBitmapTextureAtlasSource(final int pTextureWidth, final int pTextureHeight) {
        this(0, 0, pTextureWidth, pTextureHeight);
    }

    public EmptyBitmapTextureAtlasSource(final int pTextureX, final int pTextureY, final int pTextureWidth, final int pTextureHeight) {
        super(pTextureX, pTextureY, pTextureWidth, pTextureHeight);
    }

    @Override
    public Size getTextrueSize()
    {
        return null;
    }

    @Override
    public EmptyBitmapTextureAtlasSource deepCopy() {
        return new EmptyBitmapTextureAtlasSource(this.mTextureX, this.mTextureY, this.mTextureWidth, this.mTextureHeight);
    }

    // ===========================================================
    // Getter & Setter
    // ===========================================================

    // ===========================================================
    // Methods for/from SuperClass/Interfaces
    // ===========================================================

    @Override
    public Bitmap onLoadBitmap(final Bitmap.Config pBitmapConfig) {
        return Bitmap.createBitmap(this.mTextureWidth, this.mTextureHeight, pBitmapConfig);
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "(" + this.mTextureWidth + " x " + this.mTextureHeight + ")";
    }

}
