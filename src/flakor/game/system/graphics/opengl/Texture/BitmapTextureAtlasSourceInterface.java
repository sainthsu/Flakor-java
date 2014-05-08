package flakor.game.system.graphics.opengl.Texture;

import android.graphics.Bitmap;

/**
 * Created by Saint Hsu on 13-7-14.
 */
public interface BitmapTextureAtlasSourceInterface extends TextureAtlasSourceInterface
{
    // ===========================================================
    // Methods
    // ===========================================================

    @Override
    public BitmapTextureAtlasSourceInterface deepCopy();

    public Bitmap onLoadBitmap(final Bitmap.Config bitmapConfig);
}