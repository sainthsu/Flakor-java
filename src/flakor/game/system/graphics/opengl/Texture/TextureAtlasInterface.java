package flakor.game.system.graphics.opengl.Texture;

import flakor.game.console.config.EngineConfig;
import flakor.game.tool.Debug;

/**
 * Created by Saint Hsu on 13-7-14.
 */
public interface TextureAtlasInterface<T extends TextureAtlasSourceInterface> extends TextureInterface
{
    // ===========================================================
    // Methods
    // ===========================================================

    public void addTextureAtlasSource(final T pTextureAtlasSource, final int pTextureX, final int pTextureY) throws IllegalArgumentException;
    public void addTextureAtlasSource(final T pTextureAtlasSource, final int pTextureX, final int pTextureY, final int pTextureAtlasSourcePadding) throws IllegalArgumentException;
    public void addEmptyTextureAtlasSource(final int pTextureX, final int pTextureY, final int pWidth, final int pHeight);
    public void removeTextureAtlasSource(final T pTextureAtlasSource, final int pTextureX, final int pTextureY);
    public void clearTextureAtlasSources();


    public boolean hasTextureAtlasStateListener();

    public TextureAtlasStateListener<T> getTextureAtlasStateListener();

    public void setTextureAtlasStateListener(final TextureAtlasStateListener<T> pTextureAtlasStateListener);

    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================

    public static interface TextureAtlasStateListener<T extends TextureAtlasSourceInterface> extends TextureStateListener
    {
        // ===========================================================
        // Methods
        // ===========================================================

        public void onTextureAtlasSourceLoaded(final TextureAtlasInterface<T> pTextureAtlas, final T pTextureAtlasSource);
        public void onTextureAtlasSourceLoadException(final TextureAtlasInterface<T> pTextureAtlas, final T pTextureAtlasSource, final Throwable pThrowable);

        // ===========================================================
        // Inner and Anonymous Classes
        // ===========================================================

        public static class TextureAtlasStateAdapter<T extends TextureAtlasSourceInterface> implements TextureAtlasStateListener<T>
        {
            @Override
            public void onLoadedToHardware(final TextureInterface pTexture) { }

            @Override
            public void onTextureAtlasSourceLoaded(final TextureAtlasInterface<T> pTextureAtlas, final T pTextureAtlasSource) { }

            @Override
            public void onTextureAtlasSourceLoadException(final TextureAtlasInterface<T> pTextureAtlas, final T pTextureAtlasSource, final Throwable pThrowable) { }

            @Override
            public void onUnloadedFromHardware(final TextureInterface pTexture) { }
        }

        public static class DebugTextureAtlasStateListener<T extends TextureAtlasSourceInterface> implements TextureAtlasStateListener<T>
        {
            @Override
            public void onLoadedToHardware(final TextureInterface pTexture) {
                if(EngineConfig.DEBUG)
                {
                    Debug.d("Texture loaded: " + pTexture.toString());
                }
            }

            @Override
            public void onTextureAtlasSourceLoaded(final TextureAtlasInterface<T> pTextureAtlas, final T pTextureAtlasSource)
            {
                Debug.e("Loaded TextureAtlasSource. TextureAtlas: " + pTextureAtlas.toString() + " TextureAtlasSource: " + pTextureAtlasSource.toString());
            }

            @Override
            public void onTextureAtlasSourceLoadException(final TextureAtlasInterface<T> pTextureAtlas, final T pTextureAtlasSource, final Throwable pThrowable) {
                Debug.e("Exception loading TextureAtlasSource. TextureAtlas: " + pTextureAtlas.toString() + " TextureAtlasSource: " + pTextureAtlasSource.toString(), pThrowable);
            }

            @Override
            public void onUnloadedFromHardware(final TextureInterface pTexture)
            {
                if(EngineConfig.DEBUG)
                {
                    Debug.d("Texture unloaded: " + pTexture.toString());
                }
            }
        }
    }
}
