package flakor.game.system.graphics.opengl.VBO;

import flakor.game.system.graphics.opengl.GLState;

import java.util.ArrayList;

/**
 * Created by Saint Hsu on 13-7-9.
 */
public class VertexBufferObjectManager
{
    // ===========================================================
    // Fields
    // ===========================================================
    public final ArrayList<VBOInterface> VBOLoaded = new ArrayList<VBOInterface>();
    public final ArrayList<VBOInterface> VBOToBeUnloaded = new ArrayList<VBOInterface>();

    // ===========================================================
    // Getter & Setter
    // ===========================================================

    public synchronized int getHeapMemoryByteSize()
    {
        int byteSize = 0;
        final ArrayList<VBOInterface> vertexBufferObjectsLoaded = this.VBOLoaded;
        for(int i = vertexBufferObjectsLoaded.size() - 1; i >= 0; i--)
        {
            byteSize += vertexBufferObjectsLoaded.get(i).getHeapMemoryByteSize();
        }
        return byteSize;
    }

    public synchronized int getNativeHeapMemoryByteSize()
    {
        int byteSize = 0;
        final ArrayList<VBOInterface> vertexBufferObjectsLoaded = this.VBOLoaded;
        for(int i = vertexBufferObjectsLoaded.size() - 1; i >= 0; i--)
        {
            byteSize += vertexBufferObjectsLoaded.get(i).getNativeHeapMemoryByteSize();
        }
        return byteSize;
    }

    public synchronized int getGPUHeapMemoryByteSize()
    {
        int byteSize = 0;
        final ArrayList<VBOInterface> vertexBufferObjectsLoaded = this.VBOLoaded;
        for(int i = vertexBufferObjectsLoaded.size() - 1; i >= 0; i--)
        {
            byteSize += vertexBufferObjectsLoaded.get(i).getGPUMemoryByteSize();
        }
        return byteSize;
    }


    // ===========================================================
    // Methods
    // ===========================================================

    public void onCreate()
    {

    }

    public synchronized void onDestroy()
    {
        final ArrayList<VBOInterface> vertexBufferObjectsLoaded = this.VBOLoaded;
        for(int i = vertexBufferObjectsLoaded.size() - 1; i >= 0; i--) {
            vertexBufferObjectsLoaded.get(i).setNotLoadedToHardware();
        }

        vertexBufferObjectsLoaded.clear();
    }

    public synchronized void onVertexBufferObjectLoaded(final VBOInterface vertexBufferObject)
    {
        this.VBOLoaded.add(vertexBufferObject);
    }

    public synchronized void onUnloadVertexBufferObject(final VBOInterface vertexBufferObject)
    {
        if(this.VBOLoaded.remove(vertexBufferObject))
        {
            this.VBOToBeUnloaded.add(vertexBufferObject);
        }
    }

    public synchronized void onReload()
    {
        final ArrayList<VBOInterface> vertexBufferObjectsLoaded = this.VBOLoaded;
        for(int i = vertexBufferObjectsLoaded.size() - 1; i >= 0; i--)
        {
            vertexBufferObjectsLoaded.get(i).setNotLoadedToHardware();
        }

        vertexBufferObjectsLoaded.clear();
    }

    public synchronized void updateVertexBufferObjects(final GLState pGLState)
    {
        final ArrayList<VBOInterface> vertexBufferObjectsLoaded = this.VBOLoaded;
        final ArrayList<VBOInterface> vertexBufferObjectsToBeUnloaded = this.VBOToBeUnloaded;

		/* Unload pending VertexBufferObjects. */
        for(int i = vertexBufferObjectsToBeUnloaded.size() - 1; i >= 0; i--)
        {
            final VBOInterface vertexBufferObjectToBeUnloaded = vertexBufferObjectsToBeUnloaded.remove(i);
            if(vertexBufferObjectToBeUnloaded.isLoadedToHardware())
            {
                vertexBufferObjectToBeUnloaded.unloadFromHardware(pGLState);
            }
            vertexBufferObjectsLoaded.remove(vertexBufferObjectToBeUnloaded);
        }
    }
}
