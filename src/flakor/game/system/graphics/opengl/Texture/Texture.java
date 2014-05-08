package flakor.game.system.graphics.opengl.Texture;

import flakor.game.core.element.Size;
import flakor.game.system.graphics.opengl.GLState;
import flakor.game.system.graphics.opengl.PixelFormat;

import java.io.IOException;

/**
 * Created by Saint Hsu on 13-7-10.
 */
public abstract class Texture implements TextureInterface
{
    // ===========================================================
    // Constants
    // ===========================================================

    public static final int HARDWARE_TEXTURE_ID_INVALID = -1;

    // ===========================================================
    // Fields
    // ===========================================================
    private int name=-1;

    protected Size size;
    protected final TextureManager textureManager;
    protected final PixelFormat pixelFormat;
    protected final TextureParams textureParams;

    protected int hardwareTextureID = Texture.HARDWARE_TEXTURE_ID_INVALID;
    protected boolean updateOnHardwareNeeded = false;

    protected TextureStateListener textureStateListener;
    protected TextureRegionInterface textureRegion;
    // ===========================================================
    // Constructors
    // ===========================================================

    /**
     * @param pixelFormat
     * @param textureParams the (quality) settings of the Texture.
     * @param textureStateListener to be informed when this {@link Texture} is loaded, unloaded or a {@link TextureAtlasSourceInterface} failed to load.
     */
    public Texture(final PixelFormat pixelFormat, final TextureParams textureParams, final TextureStateListener textureStateListener) throws IllegalArgumentException
    {
        this.textureManager = TextureManager.onCreate();
        this.pixelFormat = pixelFormat;
        this.textureParams = textureParams;
        this.textureStateListener = textureStateListener;
    }

    // ===========================================================
    // Getter & Setter
    // ===========================================================


    @Override
    public void setSize(Size size)
    {
        this.size = size;
    }

    @Override
    public Size getSize()
    {
        return this.size;
    }

    @Override
    public int getHardwareTextureID()
    {
        return this.hardwareTextureID;
    }

    @Override
    public boolean isLoadedToHardware()
    {
        return this.hardwareTextureID != Texture.HARDWARE_TEXTURE_ID_INVALID;
    }

    @Override
    public void setNotLoadedToHardware()
    {
        this.hardwareTextureID = Texture.HARDWARE_TEXTURE_ID_INVALID;
    }

    @Override
    public boolean isUpdateOnHardwareNeeded()
    {
        return this.updateOnHardwareNeeded;
    }

    @Override
    public void setUpdateOnHardwareNeeded(final boolean updateOnHardwareNeeded)
    {
        this.updateOnHardwareNeeded = updateOnHardwareNeeded;
    }

    @Override
    public PixelFormat getPixelFormat()
    {
        return this.pixelFormat;
    }

    @Override
    public TextureParams getTextureOptions()
    {
        return this.textureParams;
    }

    @Override
    public TextureStateListener getTextureStateListener()
    {
        return this.textureStateListener;
    }

    @Override
    public void setTextureStateListener(final TextureStateListener textureStateListener)
    {
        this.textureStateListener = textureStateListener;
    }

    @Override
    public boolean hasTextureStateListener()
    {
        return this.textureStateListener != null;
    }

    @Override
    public TextureRegionInterface getTextureRegion()
    {
        return textureRegion;
    }

    @Override
    public void setTextureRegion(TextureRegionInterface textureRegion)
    {
        this.textureRegion = textureRegion;
    }

    // ===========================================================
    // Methods for/from SuperClass/Interfaces
    // ===========================================================

    protected abstract void writeTextureToHardware(final GLState glState) throws IOException;

    @Override
    public void load()
    {
        if(this.textureRegion == null)
            this.textureRegion = TextureRegionFactory.extractFromTexture(this);
        this.textureManager.loadTexture(this);
    }

    @Override
    public void load(final GLState glState) throws IOException
    {
        if(this.textureRegion == null)
            this.textureRegion = TextureRegionFactory.extractFromTexture(this);
        this.textureManager.loadTexture(glState, this);
    }

    @Override
    public void unload()
    {
        this.textureManager.unloadTexture(this);
    }

    @Override
    public void unload(final GLState glState)
    {
        this.textureManager.unloadTexture(glState, this);
    }

    @Override
    public void loadToHardware(final GLState glState) throws IOException
    {
        this.hardwareTextureID = glState.generateTexture();

        glState.bindTexture(this.hardwareTextureID);
        
        this.textureParams.apply();
        this.writeTextureToHardware(glState);
        
        this.updateOnHardwareNeeded = false;

        if(this.textureStateListener != null)
        {
            this.textureStateListener.onLoadedToHardware(this);
        }
    }

    @Override
    public void unloadFromHardware(final GLState glState)
    {
        glState.deleteTexture(this.hardwareTextureID);

        this.hardwareTextureID = Texture.HARDWARE_TEXTURE_ID_INVALID;

        if(this.textureStateListener != null)
        {
            this.textureStateListener.onUnloadedFromHardware(this);
        }
    }

    @Override
    public void reloadToHardware(final GLState glState) throws IOException
    {
        this.unloadFromHardware(glState);
        this.loadToHardware(glState);
    }

    @Override
    public void bind(final GLState glState)
    {
        glState.bindTexture(this.hardwareTextureID);
    }

    @Override
    public void bind(final GLState glState, final int glActiveTexture)
    {
        glState.activeTexture(glActiveTexture);
        glState.bindTexture(this.hardwareTextureID);
    }

}
