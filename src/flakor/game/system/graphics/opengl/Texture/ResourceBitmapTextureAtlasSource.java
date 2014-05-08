package flakor.game.system.graphics.opengl.Texture;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import flakor.game.core.element.Size;

/**
 * Created by Steve Hsu on 13-7-14.
 */
public class ResourceBitmapTextureAtlasSource extends BaseTextureAtlasSource implements BitmapTextureAtlasSourceInterface
{
    // ===========================================================
    // Fields
    // ===========================================================

    private final Resources mResources;
    private final int mDrawableResourceID;

    // ===========================================================
    // Constructors
    // ===========================================================

    public static ResourceBitmapTextureAtlasSource create(final Resources pResources, final int pDrawableResourceID)
    {
        return ResourceBitmapTextureAtlasSource.create(pResources, pDrawableResourceID, 0, 0);
    }

    public static ResourceBitmapTextureAtlasSource create(final Resources pResources, final int pDrawableResourceID, final int pTextureX, final int pTextureY)
    {
        final BitmapFactory.Options decodeOptions = new BitmapFactory.Options();
        decodeOptions.inJustDecodeBounds = true;
//		decodeOptions.inScaled = false; // TODO Check how this behaves with drawable-""/nodpi/ldpi/mdpi/hdpi folders

        BitmapFactory.decodeResource(pResources, pDrawableResourceID, decodeOptions);

        return new ResourceBitmapTextureAtlasSource(pResources, pDrawableResourceID, pTextureX, pTextureY, decodeOptions.outWidth, decodeOptions.outHeight);
    }

    public ResourceBitmapTextureAtlasSource(final Resources pResources, final int pDrawableResourceID, final int pTextureX, final int pTextureY, final int pTextureWidth, final int pTextureHeight) {
        super(pTextureX, pTextureY, pTextureWidth, pTextureHeight);

        this.mResources = pResources;
        this.mDrawableResourceID = pDrawableResourceID;
    }

    @Override
    public Size getTextrueSize()
    {
        return null;
    }

    @Override
    public ResourceBitmapTextureAtlasSource deepCopy()
    {
        return new ResourceBitmapTextureAtlasSource(this.mResources, this.mDrawableResourceID, this.mTextureX, this.mTextureY, this.mTextureWidth, this.mTextureHeight);
    }

    // ===========================================================
    // Getter & Setter
    // ===========================================================

    // ===========================================================
    // Methods for/from SuperClass/Interfaces
    // ===========================================================

    @Override
    public Bitmap onLoadBitmap(final Bitmap.Config pBitmapConfig)
    {
        final BitmapFactory.Options decodeOptions = new BitmapFactory.Options();
        decodeOptions.inPreferredConfig = pBitmapConfig;
//		decodeOptions.inScaled = false; // TODO Check how this behaves with drawable-""/nodpi/ldpi/mdpi/hdpi folders
        return BitmapFactory.decodeResource(this.mResources, this.mDrawableResourceID, decodeOptions);
    }

    @Override
    public String toString()
    {
        return this.getClass().getSimpleName() + "(" + this.mDrawableResourceID + ")";
    }

}
