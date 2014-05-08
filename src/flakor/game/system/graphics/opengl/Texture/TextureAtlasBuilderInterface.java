package flakor.game.system.graphics.opengl.Texture;

import flakor.game.system.graphics.opengl.Texture.BuildableTextureAtlas.TextureAtlasSourceWithWithLocationCallback;

import java.util.ArrayList;
/**
 * Created by Saint Hsu on 13-7-14.
 */
public interface TextureAtlasBuilderInterface<T extends TextureAtlasSourceInterface, A extends TextureAtlasInterface<T>>
{
    // ===========================================================
    // Constants
    // ===========================================================

    // ===========================================================
    // Methods
    // ===========================================================

    public void build(final A pTextureAtlas, final ArrayList<TextureAtlasSourceWithWithLocationCallback<T>> pTextureAtlasSourcesWithLocationCallback) throws TextureAtlasBuilderException;

    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================

    public static class TextureAtlasBuilderException extends Exception
    {
        // ===========================================================
        // Constants
        // ===========================================================

        private static final long serialVersionUID = 4700734424214372671L;

        // ===========================================================
        // Constructors
        // ===========================================================

        public TextureAtlasBuilderException(final String pMessage)
        {
            super(pMessage);
        }
    }
}
