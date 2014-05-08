package flakor.game.system.graphics.opengl.Texture;

/**
 * Created by Saint Hsu on 13-7-14.
 */
public class BuildableBitmapTextureAtlas extends BuildableTextureAtlas<BitmapTextureAtlasSourceInterface, BitmapTextureAtlas>
{
    // ===========================================================
    // Constructors
    // ===========================================================

    /**
     * Uses {@link BitmapTextureFormat#RGBA_8888}.
     */
    public BuildableBitmapTextureAtlas(final int pWidth, final int pHeight)
    {
        this(pWidth, pHeight, BitmapTextureFormat.RGBA_8888);
    }

    /**
     * @param pBitmapTextureFormat use {@link BitmapTextureFormat#RGBA_8888} or {@link BitmapTextureFormat#RGBA_4444}  for a {@link BitmapTextureAtlas} with transparency and {@link BitmapTextureFormat#RGB_565} for a {@link BitmapTextureAtlas} without transparency.
     */
    public BuildableBitmapTextureAtlas(final int pWidth, final int pHeight, final BitmapTextureFormat pBitmapTextureFormat)
    {
        this(pWidth, pHeight, pBitmapTextureFormat, TextureParams.DEFAULT, null);
    }

    /**
     * Uses {@link BitmapTextureFormat#RGBA_8888}.
     *
     * @param pTextureStateListener to be informed when this {@link BitmapTextureAtlas} is loaded, unloaded or a {@link TextureAtlasSourceInterface} failed to load.
     */
    public BuildableBitmapTextureAtlas(final int pWidth, final int pHeight, final TextureAtlasStateListener<BitmapTextureAtlasSourceInterface> pTextureStateListener)
    {
        this( pWidth, pHeight, BitmapTextureFormat.RGBA_8888, TextureParams.DEFAULT, pTextureStateListener);
    }

    /**
     * @param pBitmapTextureFormat use {@link BitmapTextureFormat#RGBA_8888} or {@link BitmapTextureFormat#RGBA_4444}  for a {@link BitmapTextureAtlas} with transparency and {@link BitmapTextureFormat#RGB_565} for a {@link BitmapTextureAtlas} without transparency.
     * @param pTextureStateListener to be informed when this {@link BitmapTextureAtlas} is loaded, unloaded or a {@link TextureAtlasSourceInterface} failed to load.
     */
    public BuildableBitmapTextureAtlas(final int pWidth, final int pHeight, final BitmapTextureFormat pBitmapTextureFormat, final TextureAtlasStateListener<BitmapTextureAtlasSourceInterface> pTextureStateListener)
    {
        this(pWidth, pHeight, pBitmapTextureFormat, TextureParams.DEFAULT, pTextureStateListener);
    }

    /**
     * Uses {@link BitmapTextureFormat#RGBA_8888}.
     *
     * @param pTextureOptions the (quality) settings of the BitmapTexture.
     */
    public BuildableBitmapTextureAtlas(final int pWidth, final int pHeight, final TextureParams pTextureOptions) throws IllegalArgumentException
    {
        this(pWidth, pHeight, BitmapTextureFormat.RGBA_8888, pTextureOptions, null);
    }

    /**
     * @param pBitmapTextureFormat use {@link BitmapTextureFormat#RGBA_8888} or {@link BitmapTextureFormat#RGBA_4444}  for a {@link BitmapTextureAtlas} with transparency and {@link BitmapTextureFormat#RGB_565} for a {@link BitmapTextureAtlas} without transparency.
     * @param pTextureOptions the (quality) settings of the BitmapTexture.
     */
    public BuildableBitmapTextureAtlas(final int pWidth, final int pHeight, final BitmapTextureFormat pBitmapTextureFormat, final TextureParams pTextureOptions) throws IllegalArgumentException
    {
        this( pWidth, pHeight, pBitmapTextureFormat, pTextureOptions, null);
    }

    /**
     * Uses {@link BitmapTextureFormat#RGBA_8888}.
     *
     * @param pTextureOptions the (quality) settings of the BitmapTexture.
     * @param pTextureStateListener to be informed when this {@link BitmapTextureAtlas} is loaded, unloaded or a {@link TextureAtlasSourceInterface} failed to load.
     */
    public BuildableBitmapTextureAtlas(final int pWidth, final int pHeight, final TextureParams pTextureOptions, final TextureAtlasStateListener<BitmapTextureAtlasSourceInterface> pTextureStateListener) throws IllegalArgumentException
    {
        this(pWidth, pHeight, BitmapTextureFormat.RGBA_8888, pTextureOptions, pTextureStateListener);
    }

    /**
     * @param pBitmapTextureFormat use {@link BitmapTextureFormat#RGBA_8888} or {@link BitmapTextureFormat#RGBA_4444}  for a {@link BitmapTextureAtlas} with transparency and {@link BitmapTextureFormat#RGB_565} for a {@link BitmapTextureAtlas} without transparency.
     * @param pTextureOptions the (quality) settings of the BitmapTexture.
     * @param pTextureStateListener to be informed when this {@link BitmapTextureAtlas} is loaded, unloaded or a {@link TextureAtlasSourceInterface} failed to load.
     */
    public BuildableBitmapTextureAtlas(final int pWidth, final int pHeight, final BitmapTextureFormat pBitmapTextureFormat, final TextureParams pTextureOptions, final TextureAtlasStateListener<BitmapTextureAtlasSourceInterface> pTextureStateListener) throws IllegalArgumentException
    {
        super(new BitmapTextureAtlas(pWidth, pHeight, pBitmapTextureFormat, pTextureOptions, pTextureStateListener));
    }

}
