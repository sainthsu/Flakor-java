package flakor.game.core.font;

import android.content.res.AssetManager;
import android.graphics.Typeface;

import flakor.game.core.element.Color;
import flakor.game.system.graphics.opengl.Texture.BitmapTextureAtlas;
import flakor.game.system.graphics.opengl.Texture.BitmapTextureFormat;
import flakor.game.system.graphics.opengl.Texture.TextureInterface;
import flakor.game.system.graphics.opengl.Texture.TextureParams;

/**
 * Created by Saint Hsu on 13-7-11.
 */
public class FontFactory
{
    // ===========================================================
    // Constants
    // ===========================================================

    private static final boolean ANTIALIAS_DEFAULT = true;
    private static final int COLOR_DEFAULT = Color.BLACK_ARGB_PACKED_INT;

    // ===========================================================
    // Fields
    // ===========================================================

    private static String sAssetBasePath = "";

    // ===========================================================
    // Constructors
    // ===========================================================

    // ===========================================================
    // Getter & Setter
    // ===========================================================

    /**
     * @param assetBasePath must end with '<code>/</code>' or have <code>.length() == 0</code>.
     */
    public static void setAssetBasePath(final String assetBasePath)
    {
        if(assetBasePath.endsWith("/") || assetBasePath.length() == 0)
        {
            FontFactory.sAssetBasePath = assetBasePath;
        }
        else
        {
            throw new IllegalStateException("assetBasePath must end with '/' or be length of zero.");
        }
    }

    public static String getAssetBasePath()
    {
        return FontFactory.sAssetBasePath;
    }

    public static void onCreate()
    {
        FontFactory.setAssetBasePath("");
    }

    // ===========================================================
    // Methods
    // ===========================================================

    public static Font create(final TextureInterface pTexture, final float pSize)
    {
        return FontFactory.create(pTexture, pSize, FontFactory.ANTIALIAS_DEFAULT, FontFactory.COLOR_DEFAULT);
    }

    public static Font create(final TextureInterface pTexture, final float pSize, final boolean pAntiAlias)
    {
        return FontFactory.create( pTexture, pSize, pAntiAlias, FontFactory.COLOR_DEFAULT);
    }

    public static Font create(final TextureInterface pTexture, final float pSize, final int pColor)
    {
        return FontFactory.create( pTexture, pSize, FontFactory.ANTIALIAS_DEFAULT, pColor);
    }

    public static Font create(final TextureInterface pTexture, final float pSize, final boolean pAntiAlias, final int pColor)
    {
        return FontFactory.create(pTexture, Typeface.create(Typeface.DEFAULT, Typeface.NORMAL), pSize, pAntiAlias, pColor);
    }

    public static Font create(final int pTextureWidth, final int pTextureHeight, final float pSize, final boolean pAntiAlias, final int pColor)
    {
        return FontFactory.create( pTextureWidth, pTextureHeight, TextureParams.DEFAULT, pSize, pAntiAlias, pColor);
    }

    public static Font create( final int pTextureWidth, final int pTextureHeight, final TextureParams pTextureOptions, final float pSize, final boolean pAntiAlias, final int pColor) {
        return FontFactory.create( pTextureWidth, pTextureHeight, pTextureOptions, Typeface.create(Typeface.DEFAULT, Typeface.NORMAL), pSize, pAntiAlias, pColor);
    }

    public static Font create( final int pTextureWidth, final int pTextureHeight, final Typeface pTypeface, final float pSize)
    {
        return FontFactory.create( pTextureWidth, pTextureHeight, TextureParams.DEFAULT, pTypeface, pSize, FontFactory.ANTIALIAS_DEFAULT, FontFactory.COLOR_DEFAULT);
    }

    public static Font create(final int pTextureWidth, final int pTextureHeight, final Typeface pTypeface, final float pSize, final boolean pAntiAlias)
    {
        return FontFactory.create( pTextureWidth, pTextureHeight, TextureParams.DEFAULT, pTypeface, pSize, pAntiAlias, FontFactory.COLOR_DEFAULT);
    }

    public static Font create(final int pTextureWidth, final int pTextureHeight, final Typeface pTypeface, final float pSize, final int pColor)
    {
        return FontFactory.create( pTextureWidth, pTextureHeight, TextureParams.DEFAULT, pTypeface, pSize, FontFactory.ANTIALIAS_DEFAULT, pColor);
    }

    public static Font create(final int pTextureWidth, final int pTextureHeight, final Typeface pTypeface, final float pSize, final boolean pAntiAlias, final int pColor)
    {
        return FontFactory.create( pTextureWidth, pTextureHeight, TextureParams.DEFAULT, pTypeface, pSize, pAntiAlias, pColor);
    }

    public static Font create(final int pTextureWidth, final int pTextureHeight, final TextureParams pTextureOptions, final Typeface pTypeface, final float pSize) {
        return FontFactory.create(pTextureWidth, pTextureHeight, pTextureOptions, pTypeface, pSize, FontFactory.ANTIALIAS_DEFAULT, FontFactory.COLOR_DEFAULT);
    }

    public static Font create(final int pTextureWidth, final int pTextureHeight, final TextureParams pTextureOptions, final Typeface pTypeface, final float pSize, final boolean pAntiAlias) {
        return FontFactory.create(pTextureWidth, pTextureHeight, pTextureOptions, pTypeface, pSize, pAntiAlias, FontFactory.COLOR_DEFAULT);
    }

    public static Font create(final int pTextureWidth, final int pTextureHeight, final TextureParams pTextureOptions, final Typeface pTypeface, final float pSize, final int pColor) {
        return FontFactory.create( pTextureWidth, pTextureHeight, pTextureOptions, pTypeface, pSize, FontFactory.ANTIALIAS_DEFAULT, pColor);
    }

    public static Font create(final int pTextureWidth, final int pTextureHeight, final TextureParams pTextureOptions, final Typeface pTypeface, final float pSize, final boolean pAntiAlias, final int pColor) {
        return FontFactory.create(pTextureWidth, pTextureHeight, BitmapTextureFormat.RGBA_8888, pTextureOptions, pTypeface, pSize, pAntiAlias, pColor);
    }

    public static Font create(final int pTextureWidth, final int pTextureHeight, final BitmapTextureFormat pBitmapTextureFormat, final TextureParams pTextureOptions, final Typeface pTypeface, final float pSize)
    {
        return FontFactory.create(pTextureWidth, pTextureHeight, pBitmapTextureFormat, pTextureOptions, pTypeface, pSize, FontFactory.ANTIALIAS_DEFAULT, FontFactory.COLOR_DEFAULT);
    }

    public static Font create(final int pTextureWidth, final int pTextureHeight, final BitmapTextureFormat pBitmapTextureFormat, final TextureParams pTextureOptions, final Typeface pTypeface, final float pSize, final boolean pAntiAlias) {
        return FontFactory.create( pTextureWidth, pTextureHeight, pBitmapTextureFormat, pTextureOptions, pTypeface, pSize, pAntiAlias, FontFactory.COLOR_DEFAULT);
    }

    public static Font create(final int pTextureWidth, final int pTextureHeight, final BitmapTextureFormat pBitmapTextureFormat, final TextureParams pTextureOptions, final Typeface pTypeface, final float pSize, final int pColor) {
        return FontFactory.create( pTextureWidth, pTextureHeight, pBitmapTextureFormat, pTextureOptions, pTypeface, pSize, FontFactory.ANTIALIAS_DEFAULT, pColor);
    }

    public static Font create( final int pTextureWidth, final int pTextureHeight, final BitmapTextureFormat pBitmapTextureFormat, final TextureParams pTextureOptions, final Typeface pTypeface, final float pSize, final boolean pAntiAlias, final int pColor) {
        return FontFactory.create(new BitmapTextureAtlas(pTextureWidth, pTextureHeight, pBitmapTextureFormat, pTextureOptions), pTypeface, pSize, pAntiAlias, pColor);
    }

    public static Font create(final TextureInterface pTexture, final Typeface pTypeface, final float pSize, final boolean pAntiAlias, final int pColor)
    {
        return new Font(pTexture, pTypeface, pSize, pAntiAlias, pColor);
    }

    public static Font createFromAsset(final TextureInterface pTexture, final AssetManager pAssetManager, final String pAssetPath, final float pSize, final boolean pAntiAlias, final int pColor)
    {
        return new Font( pTexture, Typeface.createFromAsset(pAssetManager, FontFactory.sAssetBasePath + pAssetPath), pSize, pAntiAlias, pColor);
    }

    public static Font createFromAsset(final int pTextureWidth, final int pTextureHeight, final AssetManager pAssetManager, final String pAssetPath, final float pSize, final boolean pAntiAlias, final int pColor)
    {
        return FontFactory.createFromAsset( pTextureWidth, pTextureHeight, TextureParams.DEFAULT, pAssetManager, pAssetPath, pSize, pAntiAlias, pColor);
    }

    public static Font createFromAsset(final int pTextureWidth, final int pTextureHeight, final TextureParams pTextureOptions, final AssetManager pAssetManager, final String pAssetPath, final float pSize, final boolean pAntiAlias, final int pColor)
    {
        return FontFactory.createFromAsset(pTextureWidth, pTextureHeight, BitmapTextureFormat.RGBA_8888, pTextureOptions, pAssetManager, pAssetPath, pSize, pAntiAlias, pColor);
    }

    public static Font createFromAsset( final int pTextureWidth, final int pTextureHeight, final BitmapTextureFormat pBitmapTextureFormat, final TextureParams pTextureOptions, final AssetManager pAssetManager, final String pAssetPath, final float pSize, final boolean pAntiAlias, final int pColor)
    {
        return new Font(new BitmapTextureAtlas( pTextureWidth, pTextureHeight, pBitmapTextureFormat, pTextureOptions), Typeface.createFromAsset(pAssetManager, FontFactory.sAssetBasePath + pAssetPath), pSize, pAntiAlias, pColor);
    }


    public static StrokeFont createStroke(final TextureInterface pTexture, final Typeface pTypeface, final float pSize, final boolean pAntiAlias, final int pColor, final float pStrokeWidth, final int pStrokeColor)
    {
        return new StrokeFont(pTexture, pTypeface, pSize, pAntiAlias, pColor, pStrokeWidth, pStrokeColor);
    }

    public static StrokeFont createStroke(final TextureInterface pTexture, final Typeface pTypeface, final float pSize, final boolean pAntiAlias, final int pColor, final float pStrokeWidth, final int pStrokeColor, final boolean pStrokeOnly)
    {
        return new StrokeFont( pTexture, pTypeface, pSize, pAntiAlias, pColor, pStrokeWidth, pStrokeColor, pStrokeOnly);
    }

    public static StrokeFont createStrokeFromAsset(final TextureInterface pTexture, final AssetManager pAssetManager, final String pAssetPath, final float pSize, final boolean pAntiAlias, final int pColor, final float pStrokeWidth, final int pStrokeColor)
    {
        return new StrokeFont(pTexture, Typeface.createFromAsset(pAssetManager, FontFactory.sAssetBasePath + pAssetPath), pSize, pAntiAlias, pColor, pStrokeWidth, pStrokeColor);
    }

    public static StrokeFont createStrokeFromAsset(final TextureInterface pTexture, final AssetManager pAssetManager, final String pAssetPath, final float pSize, final boolean pAntiAlias, final int pColor, final float pStrokeWidth, final int pStrokeColor, final boolean pStrokeOnly)
    {
        return new StrokeFont(pTexture, Typeface.createFromAsset(pAssetManager, FontFactory.sAssetBasePath + pAssetPath), pSize, pAntiAlias, pColor, pStrokeWidth, pStrokeColor, pStrokeOnly);
    }

}
