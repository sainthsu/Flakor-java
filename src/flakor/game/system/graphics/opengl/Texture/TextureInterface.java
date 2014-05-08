package flakor.game.system.graphics.opengl.Texture;

import flakor.game.core.element.Size;
import flakor.game.system.graphics.opengl.GLState;
import flakor.game.system.graphics.opengl.PixelFormat;

import java.io.IOException;

/**
 * 纹理接口
 * Created by Saint Hsu on 13-7-9.
 */
public interface TextureInterface
{
    // ===========================================================
    // Methods
    // ===========================================================

    public void setSize(final Size size);
    public Size getSize();
    public float getWidth();
    public float getHeight();

    public int getHardwareTextureID();

    public boolean isLoadedToHardware();
    public void setNotLoadedToHardware();

    public boolean isUpdateOnHardwareNeeded();
    public void setUpdateOnHardwareNeeded(final boolean pUpdateOnHardwareNeeded);

    public void setTextureRegion(TextureRegionInterface textureRegion);
    public TextureRegionInterface getTextureRegion();

    public PixelFormat getPixelFormat();
    public TextureParams getTextureOptions();

    public boolean hasTextureStateListener();
    public TextureStateListener getTextureStateListener();
    public void setTextureStateListener(final TextureStateListener textureStateListener);
    /**
     * @see {@link TextureManager#loadTexture(TextureInterface)}.
     */
    public void load();
    /**
     * @see {@link TextureManager#loadTexture(flakor.game.system.graphics.opengl.GLState, TextureInterface)}.
     */
    public void load(final GLState glState) throws IOException;
    /**
     * @see {@link TextureManager#unloadTexture(TextureInterface)}.
     */
    public void unload();
    /**
     * @see {@link TextureManager#unloadTexture(GLState, TextureInterface)}.
     */
    public void unload(final GLState glState);

    public void loadToHardware(final GLState pGLState) throws IOException;
    public void unloadFromHardware(final GLState pGLState);
    public void reloadToHardware(final GLState pGLState) throws IOException;

    public void bind(final GLState pGLState);
    
    /**
     * @param glActiveTexture from {@link android.opengl.GLES20#GL_TEXTURE0} to {@link android.opengl.GLES20#GL_TEXTURE31}.
     */
    public void bind(final GLState glState, final int glActiveTexture);

}
