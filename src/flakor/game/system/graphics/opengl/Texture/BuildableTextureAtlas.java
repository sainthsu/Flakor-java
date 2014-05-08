package flakor.game.system.graphics.opengl.Texture;

import flakor.game.core.entity.Callback;
import flakor.game.core.element.Size;
import flakor.game.system.graphics.opengl.GLState;
import flakor.game.system.graphics.opengl.PixelFormat;
import flakor.game.system.graphics.opengl.Texture.TextureAtlasBuilderInterface.TextureAtlasBuilderException;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Saint Hsu on 13-7-14.
 */
public class BuildableTextureAtlas<S extends TextureAtlasSourceInterface, T extends TextureAtlasInterface<S>> implements BuildableTextureAtlasInterface<S, T>
{
    // ===========================================================
    // Constants
    // ===========================================================

    // ===========================================================
    // Fields
    // ===========================================================

    private final T textureAtlas;
    private final ArrayList<TextureAtlasSourceWithWithLocationCallback<S>> mTextureAtlasSourcesToPlace = new ArrayList<TextureAtlasSourceWithWithLocationCallback<S>>();

    // ===========================================================
    // Constructors
    // ===========================================================

    public BuildableTextureAtlas(final T pTextureAtlas)
    {
        this.textureAtlas = pTextureAtlas;
    }

    // ===========================================================
    // Getter & Setter
    // ===========================================================

    // ===========================================================
    // Methods for/from SuperClass/Interfaces
    // ===========================================================

    @Override
    public void setSize(Size size)
    {
        this.textureAtlas.setSize(size);
    }

    @Override
    public Size getSize()
    {
        return this.textureAtlas.getSize();
    }

    @Override
    public float getWidth()
    {
        return this.textureAtlas.getWidth();
    }

    @Override
    public float getHeight()
    {
        return this.textureAtlas.getHeight();
    }

    @Override
    public int getHardwareTextureID()
    {
        return this.textureAtlas.getHardwareTextureID();
    }

    @Override
    public boolean isLoadedToHardware()
    {
        return this.textureAtlas.isLoadedToHardware();
    }

    @Override
    public void setNotLoadedToHardware()
    {
        this.textureAtlas.setNotLoadedToHardware();
    }

    @Override
    public boolean isUpdateOnHardwareNeeded()
    {
        return this.textureAtlas.isUpdateOnHardwareNeeded();
    }

    @Override
    public void setUpdateOnHardwareNeeded(final boolean pUpdateOnHardwareNeeded)
    {
        this.textureAtlas.setUpdateOnHardwareNeeded(pUpdateOnHardwareNeeded);
    }

    @Override
    public void setTextureRegion(TextureRegionInterface textureRegion) {

    }

    @Override
    public TextureRegionInterface getTextureRegion() {
        return null;
    }

    @Override
    public void load()
    {
        this.textureAtlas.load();
    }

    @Override
    public void load(final GLState pGLState) throws IOException
    {
        this.textureAtlas.load(pGLState);
    }

    @Override
    public void unload()
    {
        this.textureAtlas.unload();
    }

    @Override
    public void unload(final GLState pGLState)
    {
        this.textureAtlas.unload(pGLState);
    }

    @Override
    public void loadToHardware(final GLState pGLState) throws IOException
    {
        this.textureAtlas.loadToHardware(pGLState);
    }

    @Override
    public void unloadFromHardware(final GLState pGLState)
    {
        this.textureAtlas.unloadFromHardware(pGLState);
    }

    @Override
    public void reloadToHardware(final GLState pGLState) throws IOException
    {
        this.textureAtlas.reloadToHardware(pGLState);
    }

    @Override
    public void bind(final GLState pGLState)
    {
        this.textureAtlas.bind(pGLState);
    }

    @Override
    public void bind(final GLState pGLState, final int pGLActiveTexture)
    {
        this.textureAtlas.bind(pGLState, pGLActiveTexture);
    }

    @Override
    public PixelFormat getPixelFormat()
    {
        return this.textureAtlas.getPixelFormat();
    }

    @Override
    public TextureParams getTextureOptions()
    {
        return this.textureAtlas.getTextureOptions();
    }

    @Override
    @Deprecated
    public boolean hasTextureStateListener()
    {
        return false;
    }

    @Override
    @Deprecated
    public TextureStateListener getTextureStateListener()
    {
        return null;
    }

    @Override
    public void removeTextureAtlasSource(final S pTextureAtlasSource, final int pTextureX, final int pTextureY) {
        this.textureAtlas.removeTextureAtlasSource(pTextureAtlasSource, pTextureX, pTextureY);
    }

    @Override
    public void clearTextureAtlasSources()
    {
        this.textureAtlas.clearTextureAtlasSources();
        this.mTextureAtlasSourcesToPlace.clear();
    }

    @Override
    public void setTextureStateListener(TextureStateListener textureStateListener)
    {

    }

    @Override
    public boolean hasTextureAtlasStateListener()
    {
        return this.textureAtlas.hasTextureAtlasStateListener();
    }

    @Override
    public TextureAtlasStateListener<S> getTextureAtlasStateListener()
    {
        return this.textureAtlas.getTextureAtlasStateListener();
    }


    @Override
    public void setTextureAtlasStateListener(final TextureAtlasStateListener<S> pTextureAtlasStateListener) {
        this.textureAtlas.setTextureAtlasStateListener(pTextureAtlasStateListener);
    }

    // ===========================================================
    // Methods
    // ===========================================================

    @Override
    @Deprecated
    public void addTextureAtlasSource(S pTextureAtlasSource, int pTextureX, int pTextureY) throws IllegalArgumentException {

    }

    @Override
    @Deprecated
    public void addTextureAtlasSource(S pTextureAtlasSource, int pTextureX, int pTextureY, int pTextureAtlasSourcePadding) throws IllegalArgumentException {

    }

    @Override
    public void addEmptyTextureAtlasSource(final int pTextureX, final int pTextureY, final int pWidth, final int pHeight)
    {
        this.textureAtlas.addEmptyTextureAtlasSource(pTextureX, pTextureY, pWidth, pHeight);
    }

    @Override
    public void addTextureAtlasSource(final S pTextureAtlasSource, final Callback<S> pCallback)
    {
        this.mTextureAtlasSourcesToPlace.add(new TextureAtlasSourceWithWithLocationCallback<S>(pTextureAtlasSource, pCallback));
    }

    @Override
    public void removeTextureAtlasSource(final TextureAtlasSourceInterface pTextureAtlasSource)
    {
        final ArrayList<TextureAtlasSourceWithWithLocationCallback<S>> textureSources = this.mTextureAtlasSourcesToPlace;
        for(int i = textureSources.size() - 1; i >= 0; i--) {
            final TextureAtlasSourceWithWithLocationCallback<S> textureSource = textureSources.get(i);
            if(textureSource.mTextureAtlasSource == pTextureAtlasSource) {
                textureSources.remove(i);
                this.textureAtlas.setUpdateOnHardwareNeeded(true);
                return;
            }
        }
    }

    @Override
    public BuildableTextureAtlasInterface<S, T> build(final TextureAtlasBuilderInterface<S, T> pTextureAtlasBuilder) throws TextureAtlasBuilderException
    {
        pTextureAtlasBuilder.build(this.textureAtlas, this.mTextureAtlasSourcesToPlace);
        this.mTextureAtlasSourcesToPlace.clear();
        this.textureAtlas.setUpdateOnHardwareNeeded(true);

        return this;
    }

    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================

    public static class TextureAtlasSourceWithWithLocationCallback<T extends TextureAtlasSourceInterface> {
        // ===========================================================
        // Constants
        // ===========================================================

        // ===========================================================
        // Fields
        // ===========================================================

        private final T mTextureAtlasSource;
        private final Callback<T> mCallback;

        // ===========================================================
        // Constructors
        // ===========================================================

        public TextureAtlasSourceWithWithLocationCallback(final T pTextureAtlasSource, final Callback<T> pCallback) {
            this.mTextureAtlasSource = pTextureAtlasSource;
            this.mCallback = pCallback;
        }

        // ===========================================================
        // Getter & Setter
        // ===========================================================

        public T getTextureAtlasSource() {
            return this.mTextureAtlasSource;
        }

        public Callback<T> getCallback() {
            return this.mCallback;
        }

    }
}
