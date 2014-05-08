package flakor.game.system.graphics.opengl.Texture;

import android.graphics.Bitmap;
import android.opengl.GLES20;
import android.opengl.GLUtils;

import flakor.game.support.math.MathUtils;
import flakor.game.system.graphics.NullBitmapException;
import flakor.game.system.graphics.opengl.GLState;
import flakor.game.system.graphics.opengl.PixelFormat;

import java.util.ArrayList;

/**
 * Created by Saint Hsu on 13-7-14.
 */
public class BitmapTextureAtlas extends TextureAtlas<BitmapTextureAtlasSourceInterface>
{

    // ===========================================================
    // Fields
    // ===========================================================

    private final BitmapTextureFormat mBitmapTextureFormat;

    // ===========================================================
    // Constructors
    // ===========================================================

    /**
    * Uses {@link BitmapTextureFormat#RGBA_8888}.
    */
    public BitmapTextureAtlas(final int pWidth, final int pHeight)
    {
        this( pWidth, pHeight, BitmapTextureFormat.RGBA_8888);
    }

    /**
    * @param pBitmapTextureFormat use {@link BitmapTextureFormat#RGBA_8888} or {@link BitmapTextureFormat#RGBA_4444} for a {@link BitmapTextureAtlas} with transparency and {@link BitmapTextureFormat#RGB_565} for a {@link BitmapTextureAtlas} without transparency.
    */
    public BitmapTextureAtlas(final int pWidth, final int pHeight, final BitmapTextureFormat pBitmapTextureFormat)
    {
        this( pWidth, pHeight, pBitmapTextureFormat, TextureParams.DEFAULT, null);
    }

    /**
     * Uses {@link BitmapTextureFormat#RGBA_8888}.
     *
     * @param pTextureAtlasStateListener to be informed when this {@link BitmapTextureAtlas} is loaded, unloaded or a {@link TextureAtlasSourceInterface} failed to load.
     */
    public BitmapTextureAtlas(final int pWidth, final int pHeight, final TextureAtlasStateListener<BitmapTextureAtlasSourceInterface> pTextureAtlasStateListener)
    {
        this( pWidth, pHeight, BitmapTextureFormat.RGBA_8888, TextureParams.DEFAULT, pTextureAtlasStateListener);
    }

    /**
     * @param pBitmapTextureFormat use {@link BitmapTextureFormat#RGBA_8888} or {@link BitmapTextureFormat#RGBA_4444} for a {@link BitmapTextureAtlas} with transparency and {@link BitmapTextureFormat#RGB_565} for a {@link BitmapTextureAtlas} without transparency.
     * @param pTextureAtlasStateListener to be informed when this {@link BitmapTextureAtlas} is loaded, unloaded or a {@link TextureAtlasSourceInterface} failed to load.
    */
    public BitmapTextureAtlas( final int pWidth, final int pHeight, final BitmapTextureFormat pBitmapTextureFormat, final TextureAtlasStateListener<BitmapTextureAtlasSourceInterface> pTextureAtlasStateListener)
    {
        this( pWidth, pHeight, pBitmapTextureFormat, TextureParams.DEFAULT, pTextureAtlasStateListener);
    }

    /**
    * Uses {@link BitmapTextureFormat#RGBA_8888}.
     *
    * @param pTextureOptions the (quality) settings of this {@link BitmapTextureAtlas}.
     */
    public BitmapTextureAtlas(final int pWidth, final int pHeight, final TextureParams pTextureOptions) throws IllegalArgumentException
    {
        this( pWidth, pHeight, BitmapTextureFormat.RGBA_8888, pTextureOptions, null);
    }

    /**
    * @param pBitmapTextureFormat use {@link BitmapTextureFormat#RGBA_8888} or {@link BitmapTextureFormat#RGBA_4444} for a {@link BitmapTextureAtlas} with transparency and {@link BitmapTextureFormat#RGB_565} for a {@link BitmapTextureAtlas} without transparency.
    * @param pTextureOptions the (quality) settings of this {@link BitmapTextureAtlas}.
     */
    public BitmapTextureAtlas(final int pWidth, final int pHeight, final BitmapTextureFormat pBitmapTextureFormat, final TextureParams pTextureOptions) throws IllegalArgumentException {
        this( pWidth, pHeight, pBitmapTextureFormat, pTextureOptions, null);
    }

    /**
    * Uses {@link BitmapTextureFormat#RGBA_8888}.
    *
    * @param pTextureOptions the (quality) settings of this {@link BitmapTextureAtlas}.
     * @param pTextureAtlasStateListener to be informed when this {@link BitmapTextureAtlas} is loaded, unloaded or a {@link TextureAtlasSourceInterface} failed to load.
     */
    public BitmapTextureAtlas(final int pWidth, final int pHeight, final TextureParams pTextureOptions, final TextureAtlasStateListener<BitmapTextureAtlasSourceInterface> pTextureAtlasStateListener) throws IllegalArgumentException
    {
        this(pWidth, pHeight, BitmapTextureFormat.RGBA_8888, pTextureOptions, pTextureAtlasStateListener);
    }

    /**
    * @param pBitmapTextureFormat use {@link BitmapTextureFormat#RGBA_8888} or {@link BitmapTextureFormat#RGBA_4444} for a {@link BitmapTextureAtlas} with transparency and {@link BitmapTextureFormat#RGB_565} for a {@link BitmapTextureAtlas} without transparency.
    * @param pTextureOptions the (quality) settings of this {@link BitmapTextureAtlas}.
    * @param pTextureAtlasStateListener to be informed when this {@link BitmapTextureAtlas} is loaded, unloaded or a {@link TextureAtlasSourceInterface} failed to load.
    */
    public BitmapTextureAtlas(final int pWidth, final int pHeight, final BitmapTextureFormat pBitmapTextureFormat, final TextureParams pTextureOptions, final TextureAtlasStateListener<BitmapTextureAtlasSourceInterface> pTextureAtlasStateListener) throws IllegalArgumentException
    {
        super(pWidth, pHeight, pBitmapTextureFormat.getPixelFormat(), pTextureOptions, pTextureAtlasStateListener);

        this.mBitmapTextureFormat = pBitmapTextureFormat;
    }

    // ===========================================================
    // Getter & Setter
    // ===========================================================

    public BitmapTextureFormat getBitmapTextureFormat()
    {
        return this.mBitmapTextureFormat;
    }

    // ===========================================================
    // Methods for/from SuperClass/Interfaces
    // ===========================================================

    @Override
    public void addEmptyTextureAtlasSource(final int pTextureX, final int pTextureY, final int pWidth, final int pHeight)
    {
        this.addTextureAtlasSource(new EmptyBitmapTextureAtlasSource(pWidth, pHeight), pTextureX, pTextureY);
    }

    // ===========================================================
    // Methods
    // ===========================================================

    @Override
    protected void writeTextureToHardware(final GLState pGLState)
    {
        final PixelFormat pixelFormat = this.mBitmapTextureFormat.getPixelFormat();
        final int glInternalFormat = pixelFormat.getGLInternalFormat();
        final int glFormat = pixelFormat.getGLFormat();
        final int glType = pixelFormat.getGLType();

        GLES20.glTexImage2D(GLES20.GL_TEXTURE_2D, 0, glInternalFormat, (int)this.size.width, (int)this.size.height, 0, glFormat, glType, null);

        final boolean preMultipyAlpha = this.textureParams.preMultiplyAlpha;
		/* Non alpha premultiplied bitmaps are loaded with ARGB_8888 and converted down manually. */
        final Bitmap.Config bitmapConfig = (preMultipyAlpha) ? this.mBitmapTextureFormat.getBitmapConfig() : Bitmap.Config.ARGB_8888;

        final ArrayList<BitmapTextureAtlasSourceInterface> textureSources = this.mTextureAtlasSources;
        final int textureSourceCount = textureSources.size();

        final TextureAtlasStateListener<BitmapTextureAtlasSourceInterface> textureStateListener = this.getTextureAtlasStateListener();
        for(int i = 0; i < textureSourceCount; i++)
        {
            final BitmapTextureAtlasSourceInterface bitmapTextureAtlasSource = textureSources.get(i);
            try
            {
                final Bitmap bitmap = bitmapTextureAtlasSource.onLoadBitmap(bitmapConfig);
                if(bitmap == null)
                {
                    throw new NullBitmapException("Caused by: " + bitmapTextureAtlasSource.getClass().toString() + " --> " + bitmapTextureAtlasSource.toString() + " returned a null Bitmap.");
                }

                final boolean useDefaultAlignment = MathUtils.isPowerOfTwo(bitmap.getWidth()) && MathUtils.isPowerOfTwo(bitmap.getHeight()) && pixelFormat == PixelFormat.RGBA_8888;
                if(!useDefaultAlignment)
                {
					/* Adjust unpack alignment. */
                     GLES20.glPixelStorei(GLES20.GL_UNPACK_ALIGNMENT, 1);
                }

                if(preMultipyAlpha)
                {
                    GLUtils.texSubImage2D(GLES20.GL_TEXTURE_2D, 0, bitmapTextureAtlasSource.getTextureX(), bitmapTextureAtlasSource.getTextureY(), bitmap, glFormat, glType);
                }
                else
                {
                    pGLState.glTexSubImage2D(GLES20.GL_TEXTURE_2D, 0, bitmapTextureAtlasSource.getTextureX(), bitmapTextureAtlasSource.getTextureY(), bitmap, this.pixelFormat);
                }

                if(!useDefaultAlignment)
                {
					/* Restore default unpack alignment. */
                    GLES20.glPixelStorei(GLES20.GL_UNPACK_ALIGNMENT, GLState.GL_UNPACK_ALIGNMENT_DEFAULT);
                }

                bitmap.recycle();

                if(textureStateListener != null)
                {
                    textureStateListener.onTextureAtlasSourceLoaded(this, bitmapTextureAtlasSource);
                }
            }
            catch (final NullBitmapException e)
            {
                if(textureStateListener != null)
                {
                    textureStateListener.onTextureAtlasSourceLoadException(this, bitmapTextureAtlasSource, e);
                }
                else
                {
                    throw e;
                }
            }
        }
    }

}
