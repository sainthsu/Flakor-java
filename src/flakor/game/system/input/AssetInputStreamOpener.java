package flakor.game.system.input;

import android.content.res.AssetManager;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by longjiyang on 13-7-10.
 */
public class AssetInputStreamOpener implements InputStreamOpenerInterface
{
    // ===========================================================
    // Fields
    // ===========================================================

    private final AssetManager mAssetManager;
    private final String mAssetPath;

    // ===========================================================
    // Constructors
    // ===========================================================

    public AssetInputStreamOpener(final AssetManager pAssetManager, final String pAssetPath) {
        this.mAssetManager = pAssetManager;
        this.mAssetPath = pAssetPath;
    }

    // ===========================================================
    // Getter & Setter
    // ===========================================================

    // ===========================================================
    // Methods for/from SuperClass/Interfaces
    // ===========================================================

    @Override
    public InputStream open() throws IOException
    {
        return this.mAssetManager.open(this.mAssetPath);
    }

}
