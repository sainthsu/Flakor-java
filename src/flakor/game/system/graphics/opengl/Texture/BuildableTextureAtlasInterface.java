package flakor.game.system.graphics.opengl.Texture;

import flakor.game.core.entity.Callback;
import flakor.game.system.graphics.opengl.Texture.TextureAtlasBuilderInterface.TextureAtlasBuilderException;
/**
 * Created by Saint Hsu on 13-7-14.
 */
public interface BuildableTextureAtlasInterface<S extends TextureAtlasSourceInterface, T extends TextureAtlasInterface<S>> extends TextureAtlasInterface<S>
{
    // ===========================================================
    // Methods
    // ===========================================================

    /**
     * When all {@link TextureAtlasSourceInterface}s are added you have to call {@link BuildableBitmapTextureAtlas#build(TextureAtlasBuilderInterface)}.
     *
     * @param pTextureAtlasSource to be added.
     * @param pCallback
     */
    public void addTextureAtlasSource(final S pTextureAtlasSource, final Callback<S> pCallback);

    /**
     * Removes a {@link TextureAtlasSourceInterface} before {@link BuildableBitmapTextureAtlas#build(TextureAtlasBuilderInterface)} is called.
     * @param pTextureAtlasSource to be removed.
     */
    public void removeTextureAtlasSource(final TextureAtlasSourceInterface pTextureAtlasSource);

    /**
     * May draw over already added {@link TextureAtlasSourceInterface}.
     *
     * @param pTextureAtlasBuilder the {@link TextureAtlasBuilderInterface} to use for building the {@link TextureAtlasSourceInterface} in this {@link BuildableBitmapTextureAtlas}.
     * @return itself for method chaining.
     * @throws TextureAtlasBuilderException i.e. when the {@link TextureAtlasSourceInterface} didn't fit into this {@link BuildableBitmapTextureAtlas}.
     */
    public BuildableTextureAtlasInterface<S, T> build(final TextureAtlasBuilderInterface<S, T> pTextureAtlasBuilder) throws TextureAtlasBuilderException;
}
