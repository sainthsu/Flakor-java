package flakor.game.system.graphics.opengl.Texture;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import flakor.game.core.element.Size;
import flakor.game.support.util.StreamUtils;
import flakor.game.tool.Debug;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Saint Hsu on 13-7-14.
 */
public class AssetBitmapTextureAtlasSource extends BaseTextureAtlasSource implements BitmapTextureAtlasSourceInterface
{
    // ===========================================================
    // Fields
    // ===========================================================

    private final AssetManager mAssetManager;
    private final String mAssetPath;

    // ===========================================================
    // Constructors
    // ===========================================================

    public static AssetBitmapTextureAtlasSource create(final AssetManager pAssetManager, final String pAssetPath) {
        return AssetBitmapTextureAtlasSource.create(pAssetManager, pAssetPath, 0, 0);
    }

    public static AssetBitmapTextureAtlasSource create(final AssetManager pAssetManager, final String pAssetPath, final int pTextureX, final int pTextureY) {
        final BitmapFactory.Options decodeOptions = new BitmapFactory.Options();
        decodeOptions.inJustDecodeBounds = true;

        InputStream in = null;
        try {
            in = pAssetManager.open(pAssetPath);
            BitmapFactory.decodeStream(in, null, decodeOptions);
        } catch (final IOException e) {
            Debug.e("Failed loading Bitmap in AssetBitmapTextureAtlasSource. AssetPath: " + pAssetPath, e);
        } finally {
            StreamUtils.close(in);
        }

        return new AssetBitmapTextureAtlasSource(pAssetManager, pAssetPath, pTextureX, pTextureY, decodeOptions.outWidth, decodeOptions.outHeight);
    }

    AssetBitmapTextureAtlasSource(final AssetManager pAssetManager, final String pAssetPath, final int pTextureX, final int pTextureY, final int pTextureWidth, final int pTextureHeight) {
        super(pTextureX, pTextureY, pTextureWidth, pTextureHeight);

        this.mAssetManager = pAssetManager;
        this.mAssetPath = pAssetPath;
    }

    @Override
    public Size getTextrueSize() {
        return null;
    }

    @Override
    public AssetBitmapTextureAtlasSource deepCopy() {
        return new AssetBitmapTextureAtlasSource(this.mAssetManager, this.mAssetPath, this.mTextureX, this.mTextureY, this.mTextureWidth, this.mTextureHeight);
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
        InputStream in = null;
        try {
            final BitmapFactory.Options decodeOptions = new BitmapFactory.Options();
            decodeOptions.inPreferredConfig = pBitmapConfig;

            in = this.mAssetManager.open(this.mAssetPath);
            return BitmapFactory.decodeStream(in, null, decodeOptions);
        } catch (final IOException e) {
            Debug.e("Failed loading Bitmap in " + this.getClass().getSimpleName() + ". AssetPath: " + this.mAssetPath, e);
            return null;
        } finally {
            StreamUtils.close(in);
        }
    }

    @Override
    public String toString()
    {
        return this.getClass().getSimpleName() + "(" + this.mAssetPath + ")";
    }
}
