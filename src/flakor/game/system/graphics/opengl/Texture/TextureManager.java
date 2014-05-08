package flakor.game.system.graphics.opengl.Texture;

import android.content.res.AssetManager;

import flakor.game.system.graphics.opengl.GLState;
import flakor.game.system.graphics.opengl.VBO.TextureWarmUpVBO;
import flakor.game.system.input.AssetInputStreamOpener;
import flakor.game.system.input.InputStreamOpenerInterface;
import flakor.game.tool.Debug;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Created by Saint Hsu on 13-7-9.
 */
public class TextureManager
{
    // ===========================================================
    // Fields
    // ===========================================================
    public static final int MAX_TEXTURE_SIZE = 1024;

    private final HashSet<TextureInterface> texturesManaged = new HashSet<TextureInterface>();
    private final HashMap<String, TextureInterface> texturesMapped = new HashMap<String, TextureInterface>();

    private final ArrayList<TextureInterface> texturesLoaded = new ArrayList<TextureInterface>();

    private final ArrayList<TextureInterface> texturesToBeLoaded = new ArrayList<TextureInterface>();
    private final ArrayList<TextureInterface> texturesToBeUnloaded = new ArrayList<TextureInterface>();

    private TextureWarmUpVBO textureWarmUpVBO;

    private static TextureManager singleton = new TextureManager();

    private TextureManager()
    {
        this.textureWarmUpVBO = new TextureWarmUpVBO();
    }

    // ===========================================================
    // Methods
    // ===========================================================

    public static TextureManager onCreate()
    {
        return singleton;
    }

    public synchronized void onReload()
    {
        final HashSet<TextureInterface> managedTextures = this.texturesManaged;
        if(!managedTextures.isEmpty())
        {
            for(final TextureInterface texture : managedTextures)
            { // TODO Can the use of the iterator be avoided somehow?
                texture.setNotLoadedToHardware();
            }
        }

        if(!this.texturesLoaded.isEmpty())
        {
            this.texturesToBeLoaded.addAll(this.texturesLoaded);
            // TODO Check if addAll uses iterator internally!
            this.texturesLoaded.clear();
        }

        if(!this.texturesToBeUnloaded.isEmpty())
        {
            this.texturesManaged.removeAll(this.texturesToBeUnloaded);
             // TODO Check if removeAll uses iterator internally!
            this.texturesToBeUnloaded.clear();
        }

        this.textureWarmUpVBO.setNotLoadedToHardware();
    }

    public synchronized void onDestroy()
    {
        final HashSet<TextureInterface> managedTextures = this.texturesManaged;
        for(final TextureInterface texture : managedTextures)
        {
           // TODO Can the use of the iterator be avoided somehow?
            texture.setNotLoadedToHardware();
        }

        this.texturesToBeLoaded.clear();
        this.texturesLoaded.clear();
        this.texturesManaged.clear();
        this.texturesMapped.clear();

        this.textureWarmUpVBO.dispose();
        this.textureWarmUpVBO = null;
    }

    public synchronized boolean hasMappedTexture(final String ID)
    {
        if(ID == null)
        {
            throw new IllegalArgumentException("ID must not be null!");
        }
        return this.texturesMapped.containsKey(ID);
    }

    public synchronized TextureInterface getMappedTexture(final String ID)
    {
        if(ID == null)
        {
            throw new IllegalArgumentException("ID must not be null!");
        }
        return this.texturesMapped.get(ID);
    }

    public synchronized void addMappedTexture(final String ID, final TextureInterface texture) throws IllegalArgumentException
    {
        if(ID == null)
        {
            throw new IllegalArgumentException("ID must not be null!");
        }
        else if(texture == null)
        {
            throw new IllegalArgumentException("Texture must not be null!");
        }
        else if(this.texturesMapped.containsKey(ID))
        {
            throw new IllegalArgumentException("collision for ID: '" + ID + "'.");
        }
        this.texturesMapped.put(ID, texture);
    }

    public synchronized TextureInterface removedMappedTexture(final String ID)
    {
        if(ID == null)
        {
            throw new IllegalArgumentException("ID must not be null!");
        }
        return this.texturesMapped.remove(ID);
    }

    /**
     * 加载纹理到管理器，为加载到GPU做准备
     * @param texture the {@link TextureInterface} to be loaded before the very next frame is drawn (Or prevent it from being unloaded then).
     * @return <code>true</code> when the {@link TextureInterface} was previously not managed by this {@link TextureManager}, <code>false</code> if it was already managed.
     */
    public synchronized boolean loadTexture(final TextureInterface texture)
    {
        if(texture == null)
        {
            throw new IllegalArgumentException("texture must not be null!");
        }

        if(this.texturesManaged.contains(texture))
        {
			/* Just make sure it doesn't get deleted. */
            this.texturesToBeUnloaded.remove(texture);
            return false;
        }
        else
        {
            this.texturesManaged.add(texture);
            this.texturesToBeLoaded.add(texture);
            return true;
        }
    }

    /**
     * Must be called from the GL-{@link Thread}.
     * 加载到GPU
     * @param glState openGl State machine
     * @param texture the {@link TextureInterface} to be loaded right now, if it is not loaded.
     * @return <code>true</code> when the {@link TextureInterface} was previously not managed by this {@link TextureManager}, <code>false</code> if it was already managed.
     */
    public synchronized boolean loadTexture(final GLState glState, final TextureInterface texture) throws IOException
    {
        if(texture == null)
        {
            throw new IllegalArgumentException("Texture must not be null!");
        }

        if(!texture.isLoadedToHardware())
        {
            texture.loadToHardware(glState);
        }
        else if(texture.isUpdateOnHardwareNeeded())
        {
            texture.reloadToHardware(glState);
        }

        if(this.texturesManaged.contains(texture))
        {
			/* Just make sure it doesn't get deleted. */
            this.texturesToBeUnloaded.remove(texture);
            return false;
        }
        else
        {
            this.texturesManaged.add(texture);
            this.texturesLoaded.add(texture);
            return true;
        }
    }

    /**
     * @param pTexture the {@link TextureInterface} to be unloaded before the very next frame is drawn (Or prevent it from being loaded then).
     * @return <code>true</code> when the {@link TextureInterface} was already managed by this {@link TextureManager}, <code>false</code> if it was not managed.
     */
    public synchronized boolean unloadTexture(final TextureInterface pTexture)
    {
        if(pTexture == null) 
        {
            throw new IllegalArgumentException("Texture must not be null!");
        }

        if(this.texturesManaged.contains(pTexture))
        {
			/* If the Texture is loaded, unload it.
			 * If the Texture is about to be loaded, stop it from being loaded. */
            if(this.texturesLoaded.contains(pTexture))
            {
                this.texturesToBeUnloaded.add(pTexture);
            }
            else if(this.texturesToBeLoaded.remove(pTexture))
            {
                this.texturesManaged.remove(pTexture);
            }
            return true;
        }
        else
        {
            return false;
        }
    }

    /**
     * Must be called from the GL-{@link Thread}.
     *
     * @param pGLState
     * @param pTexture the {@link TextureInterface} to be unloaded right now, if it is loaded.
     * @return <code>true</code> when the {@link TextureInterface} was already managed by this {@link TextureManager}, <code>false</code> if it was not managed.
     */
    public synchronized boolean unloadTexture(final GLState pGLState, final TextureInterface pTexture)
    {
        if(pTexture == null)
        {
            throw new IllegalArgumentException("pTexture must not be null!");
        }
        else if(pTexture.isLoadedToHardware())
        {
            pTexture.unloadFromHardware(pGLState);
        }

        if(this.texturesManaged.contains(pTexture))
        {
			/* Just make sure it doesn't get loaded. */
            this.texturesLoaded.remove(pTexture);
            this.texturesToBeLoaded.remove(pTexture);

            return true;
        }
        else
        {
            return false;
        }
    }

    /**
     * 更新纹理 被engine调用
     * @param pGLState
     */
    public synchronized void updateTextures(final GLState pGLState)
    {
        final HashSet<TextureInterface> texturesManaged = this.texturesManaged;
        final ArrayList<TextureInterface> texturesLoaded = this.texturesLoaded;
        final ArrayList<TextureInterface> texturesToBeLoaded = this.texturesToBeLoaded;
        final ArrayList<TextureInterface> texturesToBeUnloaded = this.texturesToBeUnloaded;

		/* First reload Textures that need to be updated. */
        for(int i = texturesLoaded.size() - 1; i >= 0; i--)
        {
            final TextureInterface textureToBeReloaded = texturesLoaded.get(i);
            if(textureToBeReloaded.isUpdateOnHardwareNeeded())
            {
                try
                {
                    textureToBeReloaded.reloadToHardware(pGLState);
                }
                catch (final IOException e)
                {
                    Debug.e(e);
                }
            }
        }

		/* Then load pending Textures. */
        final int texturesToBeLoadedCount = texturesToBeLoaded.size();

        if(texturesToBeLoadedCount > 0)
        {
            for(int i = texturesToBeLoadedCount - 1; i >= 0; i--)
            {
                final TextureInterface textureToBeLoaded = texturesToBeLoaded.remove(i);
                if(!textureToBeLoaded.isLoadedToHardware())
                {
                    try
                    {
                        textureToBeLoaded.loadToHardware(pGLState);

						/* Execute the warm-up to ensure the texture data is actually moved to the GPU. */
                        if(this.textureWarmUpVBO==null)
                        	this.textureWarmUpVBO = new TextureWarmUpVBO();
                        //this.textureWarmUpVBO.warmup(pGLState, textureToBeLoaded);
                    }
                    catch (final IOException e)
                    {
                    	
                        Debug.e(e);
                    }
                }
                texturesLoaded.add(textureToBeLoaded);
            }
        }

		/* Then unload pending Textures. */
        final int texturesToBeUnloadedCount = texturesToBeUnloaded.size();

        if(texturesToBeUnloadedCount > 0) {
            for(int i = texturesToBeUnloadedCount - 1; i >= 0; i--) {
                final TextureInterface textureToBeUnloaded = texturesToBeUnloaded.remove(i);
                if(textureToBeUnloaded.isLoadedToHardware()) {
                    textureToBeUnloaded.unloadFromHardware(pGLState);
                }
                texturesLoaded.remove(textureToBeUnloaded);
                texturesManaged.remove(textureToBeUnloaded);
            }
        }

		/* Finally invoke the GC if anything has changed. */
        if((texturesToBeLoadedCount > 0) || (texturesToBeUnloadedCount > 0)) 
        {
            System.gc();
        }
    }

    public synchronized TextureInterface getTexture(final String pID, final AssetManager pAssetManager, final String pAssetPath) throws IOException
    {
        return this.getTexture(pID, pAssetManager, pAssetPath, TextureParams.DEFAULT);
    }

    public synchronized TextureInterface getTexture(final String pID, final AssetManager pAssetManager, final String pAssetPath, final TextureParams pTextureOptions) throws IOException
    {
        if(this.hasMappedTexture(pID))
        {
            return this.getMappedTexture(pID);
        }
        else
        {
            final TextureInterface texture = new BitmapTexture(new AssetInputStreamOpener(pAssetManager, pAssetPath), pTextureOptions);
            this.loadTexture(texture);
            this.addMappedTexture(pID, texture);

            return texture;
        }
    }

    public synchronized TextureInterface getTexture(final String pID, final InputStreamOpenerInterface pInputStreamOpener) throws IOException
    {
        return this.getTexture(pID, pInputStreamOpener, TextureParams.DEFAULT);
    }

    public synchronized TextureInterface getTexture(final String pID, final InputStreamOpenerInterface pInputStreamOpener, final TextureParams textureParams) throws IOException
    {
        return this.getTexture(pID, pInputStreamOpener, BitmapTextureFormat.RGBA_8888, textureParams);
    }

    public synchronized TextureInterface getTexture(final String pID, final InputStreamOpenerInterface pInputStreamOpener, final BitmapTextureFormat pBitmapTextureFormat, final TextureParams pTextureOptions) throws IOException {
        return this.getTexture(pID, pInputStreamOpener, pBitmapTextureFormat, pTextureOptions, true);
    }

    public synchronized TextureInterface getTexture(final String pID, final InputStreamOpenerInterface pInputStreamOpener, final BitmapTextureFormat pBitmapTextureFormat, final TextureParams pTextureOptions, final boolean pLoadToHardware) throws IOException
    {
        if(this.hasMappedTexture(pID))
        {
            return this.getMappedTexture(pID);
        }
        else
        {
            final TextureInterface texture = new BitmapTexture(pInputStreamOpener, pBitmapTextureFormat, pTextureOptions);
            if(pLoadToHardware) {
                this.loadTexture(texture);
            }
            this.addMappedTexture(pID, texture);

            return texture;
        }
    }

}
