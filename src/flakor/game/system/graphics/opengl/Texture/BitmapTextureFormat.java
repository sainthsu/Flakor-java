package flakor.game.system.graphics.opengl.Texture;

import android.graphics.Bitmap;

import flakor.game.system.graphics.opengl.PixelFormat;

/**
 * Created by Saint Hsu on 13-7-10.
 */
public enum BitmapTextureFormat
{	// ===========================================================
    // Elements
    // ===========================================================

    RGBA_8888(Bitmap.Config.ARGB_8888, PixelFormat.RGBA_8888),
    RGB_565(Bitmap.Config.RGB_565, PixelFormat.RGB_565),
    RGBA_4444(Bitmap.Config.ARGB_4444, PixelFormat.RGBA_4444),
    A_8(Bitmap.Config.ALPHA_8, PixelFormat.A_8); // TODO

    // ===========================================================
    // Constants
    // ===========================================================

    // ===========================================================
    // Fields
    // ===========================================================

    private final Bitmap.Config mBitmapConfig;
    private final PixelFormat mPixelFormat;

    // ===========================================================
    // Constructors
    // ===========================================================

    private BitmapTextureFormat(final Bitmap.Config pBitmapConfig, final PixelFormat pPixelFormat)
    {
        this.mBitmapConfig = pBitmapConfig;
        this.mPixelFormat = pPixelFormat;
    }

    public static BitmapTextureFormat fromPixelFormat(final PixelFormat pPixelFormat)
    {
        switch(pPixelFormat) {
            case RGBA_8888:
                return RGBA_8888;
            case RGBA_4444:
                return RGBA_4444;
            case RGB_565:
                return RGB_565;
            case A_8:
                return A_8;
            default:
                throw new IllegalArgumentException("Unsupported " + PixelFormat.class.getName() + ": '" + pPixelFormat + "'.");
        }
    }

    // ===========================================================
    // Getter & Setter
    // ===========================================================

    public Bitmap.Config getBitmapConfig()
    {
        return this.mBitmapConfig;
    }

    public PixelFormat getPixelFormat()
    {
        return this.mPixelFormat;
    }
}
