package flakor.game.system.graphics.opengl.Texture;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLU;
import android.opengl.GLUtils;
import flakor.game.core.element.Size;
import flakor.game.support.math.MathUtils;
import flakor.game.support.util.StreamUtils;
import flakor.game.system.graphics.NullBitmapException;
import flakor.game.system.graphics.opengl.GLState;
import flakor.game.system.graphics.opengl.PixelFormat;
import flakor.game.system.input.InputStreamOpenerInterface;
import flakor.game.tool.Debug;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Saint Hsu on 13-7-10.
 */
public class BitmapTexture extends Texture
{
    // ===========================================================
    // Fields
    // ===========================================================

    private final InputStreamOpenerInterface mInputStreamOpener;
    private final BitmapTextureFormat mBitmapTextureFormat;

    // ===========================================================
    // Constructors
    // ===========================================================

    public BitmapTexture( final InputStreamOpenerInterface pInputStreamOpener) throws IOException
    {
        this( pInputStreamOpener, BitmapTextureFormat.RGBA_8888, TextureParams.DEFAULT, null);
    }

    public BitmapTexture(final TextureManager pTextureManager, final InputStreamOpenerInterface pInputStreamOpener, final BitmapTextureFormat pBitmapTextureFormat) throws IOException {
        this( pInputStreamOpener, pBitmapTextureFormat, TextureParams.DEFAULT, null);
    }

    public BitmapTexture( final InputStreamOpenerInterface pInputStreamOpener, final TextureParams pTextureOptions) throws IOException {
        this(pInputStreamOpener, BitmapTextureFormat.RGBA_8888, pTextureOptions, null);
    }

    public BitmapTexture( final InputStreamOpenerInterface pInputStreamOpener, final BitmapTextureFormat pBitmapTextureFormat, final TextureParams pTextureOptions) throws IOException
    {
        this(pInputStreamOpener, pBitmapTextureFormat, pTextureOptions, null);
    }

    public BitmapTexture(final InputStreamOpenerInterface pInputStreamOpener, final BitmapTextureFormat pBitmapTextureFormat, final TextureParams pTextureOptions, final TextureStateListener pTextureStateListener) throws IOException
    {
        super(pBitmapTextureFormat.getPixelFormat(), pTextureOptions, pTextureStateListener);

        this.mInputStreamOpener = pInputStreamOpener;
        this.mBitmapTextureFormat = pBitmapTextureFormat;

        final BitmapFactory.Options decodeOptions = new BitmapFactory.Options();
        decodeOptions.inJustDecodeBounds = true;

        final InputStream in = null;
        try
        {
            BitmapFactory.decodeStream(pInputStreamOpener.open(), null, decodeOptions);
        }
        finally
        {
            StreamUtils.close(in);
        }

        this.size = new Size(decodeOptions.outWidth,decodeOptions.outHeight);
    }

    // ===========================================================
    // Getter & Setter
    // ===========================================================

    @Override
    public float getWidth()
    {
        return this.size.width;
    }

    @Override
    public float getHeight()
    {
        return this.size.height;
    }

    // ===========================================================
    // Methods for/from SuperClass/Interfaces
    // ===========================================================

    @Override
    protected void writeTextureToHardware(final GLState pGLState) throws IOException
    {
        final Bitmap.Config bitmapConfig = this.mBitmapTextureFormat.getBitmapConfig();
        final Bitmap bitmap = this.onGetBitmap(bitmapConfig);
        Debug.e("Sprite bitmap write to hardware");
        if(bitmap == null)
        {
            throw new NullBitmapException("Caused by: '" + this.toString() + "'.");
        }

        final boolean useDefaultAlignment = MathUtils.isPowerOfTwo(bitmap.getWidth()) && MathUtils.isPowerOfTwo(bitmap.getHeight()) && (this.pixelFormat == PixelFormat.RGBA_8888);
        if(!useDefaultAlignment)
        {
			/* Adjust unpack alignment. */
            GLES20.glPixelStorei(GLES20.GL_UNPACK_ALIGNMENT, 1);
        }

        final boolean preMultipyAlpha = this.textureParams.preMultiplyAlpha;
        if(preMultipyAlpha)
        {
            GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);
        }
        else
        {
            pGLState.glTexImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0, this.pixelFormat);
        }

        if(!useDefaultAlignment)
        {
			/* Restore default unpack alignment. */
            GLES20.glPixelStorei(GLES20.GL_UNPACK_ALIGNMENT, GLState.GL_UNPACK_ALIGNMENT_DEFAULT);
        }
        Debug.e("gl error=>"+GLU.gluErrorString(GLES20.glGetError()));
        if(GLES20.glIsTexture(this.hardwareTextureID))
        {
        	Debug.e("this is a texture!!!!");
        }
        bitmap.recycle();
    }

    // ===========================================================
    // Methods
    // ===========================================================

    protected Bitmap onGetBitmap(final Bitmap.Config bitmapConfig) throws IOException
    {
        final BitmapFactory.Options decodeOptions = new BitmapFactory.Options();
        decodeOptions.inPreferredConfig = bitmapConfig;

        return BitmapFactory.decodeStream(this.mInputStreamOpener.open(), null, decodeOptions);
    }

}
