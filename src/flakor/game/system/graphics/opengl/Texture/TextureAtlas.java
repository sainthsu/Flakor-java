package flakor.game.system.graphics.opengl.Texture;

import flakor.game.core.element.Size;
import flakor.game.system.graphics.opengl.PixelFormat;

import java.util.ArrayList;

/**
 * Created by Saint Hsu on 13-7-14.
 */
public abstract class TextureAtlas<T extends TextureAtlasSourceInterface> extends Texture implements TextureAtlasInterface<T>
{

    // ===========================================================
    // Fields
    // ===========================================================

    protected final ArrayList<T> mTextureAtlasSources = new ArrayList<T>();

    // ===========================================================
    // Constructors
    // ===========================================================

    public TextureAtlas(final int pWidth, final int pHeight, final PixelFormat pPixelFormat, final TextureParams pTextureOptions, final TextureAtlasStateListener<T> pTextureAtlasStateListener)
    {
        super(pPixelFormat, pTextureOptions, pTextureAtlasStateListener);

        this.size = new Size(pWidth,pHeight);
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

    @Deprecated
    @Override
    public boolean hasTextureStateListener()
    {
        return super.hasTextureStateListener();
    }

    @Override
    public boolean hasTextureAtlasStateListener()
    {
        return super.hasTextureStateListener();
    }

    @SuppressWarnings("unchecked")
    @Override
    public TextureAtlasStateListener<T> getTextureAtlasStateListener()
    {
        return (TextureAtlasStateListener<T>) super.getTextureStateListener();
    }

    @Override
    public void setTextureAtlasStateListener(final TextureAtlasStateListener<T> pTextureAtlasStateListener)
    {
        super.setTextureStateListener(pTextureAtlasStateListener);
    }

    // ===========================================================
    // Methods for/from SuperClass/Interfaces
    // ===========================================================

    @Override
    public void addTextureAtlasSource(final T pTextureAtlasSource, final int pTextureX, final int pTextureY) throws IllegalArgumentException {
        this.checkTextureAtlasSourcePosition(pTextureAtlasSource, pTextureX, pTextureY);
        pTextureAtlasSource.setTextureX(pTextureX);
        pTextureAtlasSource.setTextureY(pTextureY);
        this.mTextureAtlasSources.add(pTextureAtlasSource);
        this.updateOnHardwareNeeded = true;
    }

    @Override
    public void addTextureAtlasSource(final T pTextureAtlasSource, final int pTextureX, final int pTextureY, final int pTextureAtlasSourcePadding) throws IllegalArgumentException {
        this.addTextureAtlasSource(pTextureAtlasSource, pTextureX, pTextureY);

        if(pTextureAtlasSourcePadding > 0) {
			/* Left padding. */
            if(pTextureX >= pTextureAtlasSourcePadding) {
                this.addEmptyTextureAtlasSource(pTextureX - pTextureAtlasSourcePadding, pTextureY, pTextureAtlasSourcePadding, pTextureAtlasSource.getTextureHeight());
            }

			/* Top padding. */
            if(pTextureY >= pTextureAtlasSourcePadding) {
                this.addEmptyTextureAtlasSource(pTextureX, pTextureY - pTextureAtlasSourcePadding, pTextureAtlasSource.getTextureWidth(), pTextureAtlasSourcePadding);
            }

			/* Right padding. */
            if(pTextureX + pTextureAtlasSource.getTextureWidth() - 1 + pTextureAtlasSourcePadding <= this.getWidth()) {
                this.addEmptyTextureAtlasSource(pTextureX + pTextureAtlasSource.getTextureWidth(), pTextureY, pTextureAtlasSourcePadding, pTextureAtlasSource.getTextureHeight());
            }

			/* Bottom padding. */
            if(pTextureY + pTextureAtlasSource.getTextureHeight() - 1 + pTextureAtlasSourcePadding <= this.getHeight()) {
                this.addEmptyTextureAtlasSource(pTextureX, pTextureY + pTextureAtlasSource.getTextureHeight(), pTextureAtlasSource.getTextureWidth(), pTextureAtlasSourcePadding);
            }
        }
    }

    @Override
    public void removeTextureAtlasSource(final T pTextureAtlasSource, final int pTextureX, final int pTextureY) {
        final ArrayList<T> textureSources = this.mTextureAtlasSources;
        for(int i = textureSources.size() - 1; i >= 0; i--)
        {
            final T textureSource = textureSources.get(i);
            if(textureSource == pTextureAtlasSource && textureSource.getTextureX() == pTextureX && textureSource.getTextureY() == pTextureY) {
                textureSources.remove(i);
                this.updateOnHardwareNeeded = true;
                return;
            }
        }
    }

    @Override
    public void clearTextureAtlasSources()
    {
        this.mTextureAtlasSources.clear();
        this.updateOnHardwareNeeded = true;
    }

    // ===========================================================
    // Methods
    // ===========================================================

    private void checkTextureAtlasSourcePosition(final T pTextureAtlasSource, final int pTextureX, final int pTextureY) throws IllegalArgumentException
    {
        if(pTextureX < 0)
        {
            throw new IllegalArgumentException("Illegal negative pTextureX supplied: '" + pTextureX + "'");
        }
        else if(pTextureY < 0)
        {
            throw new IllegalArgumentException("Illegal negative pTextureY supplied: '" + pTextureY + "'");
        }
        else if(pTextureX + pTextureAtlasSource.getTextureWidth() > this.getWidth() || pTextureY + pTextureAtlasSource.getTextureHeight() > this.getHeight())
        {
            throw new IllegalArgumentException("Supplied pTextureAtlasSource must not exceed bounds of Texture.");
        }
    }

    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================
}

