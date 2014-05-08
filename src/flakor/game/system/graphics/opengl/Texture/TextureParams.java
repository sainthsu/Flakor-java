package flakor.game.system.graphics.opengl.Texture;

import android.opengl.GLES20;

/**
 * Created by Saint Hsu on 13-7-9.
 */
public class TextureParams
{
    // ===========================================================
    // Constants
    // ===========================================================

    public static final TextureParams NEAREST = new TextureParams(GLES20.GL_NEAREST, GLES20.GL_NEAREST, GLES20.GL_CLAMP_TO_EDGE, GLES20.GL_CLAMP_TO_EDGE, false);
    public static final TextureParams BILINEAR = new TextureParams(GLES20.GL_LINEAR, GLES20.GL_LINEAR, GLES20.GL_CLAMP_TO_EDGE, GLES20.GL_CLAMP_TO_EDGE, false);
    public static final TextureParams REPEATING_NEAREST = new TextureParams(GLES20.GL_NEAREST, GLES20.GL_NEAREST, GLES20.GL_REPEAT, GLES20.GL_REPEAT, false);
    public static final TextureParams REPEATING_BILINEAR = new TextureParams(GLES20.GL_LINEAR, GLES20.GL_LINEAR, GLES20.GL_REPEAT, GLES20.GL_REPEAT, false);

    public static final TextureParams NEAREST_PREMULTIPLYALPHA = new TextureParams(GLES20.GL_NEAREST, GLES20.GL_NEAREST, GLES20.GL_CLAMP_TO_EDGE, GLES20.GL_CLAMP_TO_EDGE, true);
    public static final TextureParams BILINEAR_PREMULTIPLYALPHA = new TextureParams(GLES20.GL_LINEAR, GLES20.GL_LINEAR, GLES20.GL_CLAMP_TO_EDGE, GLES20.GL_CLAMP_TO_EDGE, true);
    public static final TextureParams REPEATING_NEAREST_PREMULTIPLYALPHA = new TextureParams(GLES20.GL_NEAREST, GLES20.GL_NEAREST, GLES20.GL_REPEAT, GLES20.GL_REPEAT, true);
    public static final TextureParams REPEATING_BILINEAR_PREMULTIPLYALPHA = new TextureParams(GLES20.GL_LINEAR, GLES20.GL_LINEAR, GLES20.GL_REPEAT, GLES20.GL_REPEAT, true);

    public static final TextureParams DEFAULT = NEAREST;

    // ===========================================================
    // Fields
    // ===========================================================
    public final int minFilter;
    public final int magFilter;
    public final int wrapT;
    public final int wrapS;
    public final boolean preMultiplyAlpha;

    // ===========================================================
    // Constructors
    // ===========================================================

    public TextureParams(final int minFilter, final int magFilter, final int wrapT, final int wrapS, final boolean preMultiplyAlpha)
    {
        this.minFilter = minFilter;
        this.magFilter = magFilter;
        this.wrapT = wrapT;
        this.wrapS = wrapS;
        this.preMultiplyAlpha = preMultiplyAlpha;
    }

    // ===========================================================
    // Getter & Setter
    // ===========================================================

    // ===========================================================
    // Methods
    // ===========================================================
    public TextureParams copy()
    {
        return new TextureParams(this.minFilter,this.magFilter,this.wrapT,this.wrapS,this.preMultiplyAlpha);
    }

    public void apply()
    {
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, this.minFilter);
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, this.magFilter);
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, this.wrapS);
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, this.wrapT);
    }

}
