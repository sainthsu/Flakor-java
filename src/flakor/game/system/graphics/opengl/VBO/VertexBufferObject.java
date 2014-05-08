package flakor.game.system.graphics.opengl.VBO;

import android.opengl.GLES20;

import flakor.game.support.util.BufferUtils;
import flakor.game.system.graphics.opengl.DataConstants;
import flakor.game.system.graphics.opengl.DrawType;
import flakor.game.system.graphics.opengl.GLState;
import flakor.game.system.graphics.opengl.Shader.ShaderProgram;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * Created by Saint Hsu on 13-7-9.
 */
public abstract class VertexBufferObject implements VBOInterface
{
    // ===========================================================
    // Fields
    // ===========================================================

    protected final int capacity;
    protected final boolean autoDispose;
    protected final int usage;
    protected final ByteBuffer byteBuffer;

    protected int hardwareBufferID = HARDWARE_BUFFER_ID_INVALID;
    protected boolean dirtyOnHardware = true;

    protected boolean disposed;

    protected final VertexBufferObjectManager vertexBufferObjectManager;
    protected final VertexBufferObjectAttributes vertexBufferObjectAttributes;

    // ===========================================================
    // Constructors
    // ===========================================================

    /**
     * @param VBOManager (Optional, if you manage reloading on your own.)
     * @param capacity
     * @param drawType
     * @param autoDispose when passing <code>true</code> this {@link VertexBufferObject} loads itself to the active {@link VertexBufferObjectManager}. <b><u>WARNING:</u></b> When passing <code>false</code> one needs to take care of that by oneself!
     * @param VBOAttributes to be automatically enabled on the {@link flakor.game.system.graphics.opengl.Shader.ShaderProgram} used in {@link VertexBufferObject#bind(GLState, flakor.game.system.graphics.opengl.Shader.ShaderProgram)}.
     */
    public VertexBufferObject(final VertexBufferObjectManager VBOManager, final int capacity, final DrawType drawType, final boolean autoDispose, final VertexBufferObjectAttributes VBOAttributes)
    {
        this.vertexBufferObjectManager = VBOManager;
        this.capacity = capacity;
        this.usage = drawType.getUsage();
        this.autoDispose = autoDispose;
        this.vertexBufferObjectAttributes = VBOAttributes;

        this.byteBuffer = BufferUtils.allocateDirectByteBuffer(capacity * DataConstants.BYTES_PER_FLOAT);

        this.byteBuffer.order(ByteOrder.nativeOrder());
    }

    // ===========================================================
    // Getter & Setter
    // ===========================================================

    @Override
    public VertexBufferObjectManager getVertexBufferObjectManager()
    {
        return this.vertexBufferObjectManager;
    }

    @Override
    public boolean isDisposed()
    {
        return this.disposed;
    }

    @Override
    public boolean isAutoDispose()
    {
        return this.autoDispose;
    }

    @Override
    public int getHardwareBufferID()
    {
        return this.hardwareBufferID;
    }

    @Override
    public boolean isLoadedToHardware()
    {
        return this.hardwareBufferID != HARDWARE_BUFFER_ID_INVALID;
    }

    @Override
    public void setNotLoadedToHardware()
    {
        this.hardwareBufferID = HARDWARE_BUFFER_ID_INVALID;
        this.dirtyOnHardware = true;
    }

    @Override
    public boolean isDirtyOnHardware()
    {
        return this.dirtyOnHardware;
    }

    @Override
    public void setDirtyOnHardware()
    {
        this.dirtyOnHardware = true;
    }

    @Override
    public int getCapacity()
    {
        return this.capacity;
    }

    @Override
    public int getByteCapacity()
    {
        return this.byteBuffer.capacity();
    }

    @Override
    public int getGPUMemoryByteSize()
    {
        if(this.isLoadedToHardware())
        {
            return this.getByteCapacity();
        }
        else
        {
            return 0;
        }
    }

    // ===========================================================
    // Methods for/from SuperClass/Interfaces
    // ===========================================================

    protected abstract void onBufferData();

    @Override
    public void bind(final GLState glState)
    {
        if(this.hardwareBufferID == HARDWARE_BUFFER_ID_INVALID)
        {
            this.loadToHardware(glState);

            if(this.vertexBufferObjectManager != null)
            {
                this.vertexBufferObjectManager.onVertexBufferObjectLoaded(this);
            }
        }

        glState.bindArrayBuffer(this.hardwareBufferID);

        if(this.dirtyOnHardware)
        {
            this.onBufferData();

            this.dirtyOnHardware = false;
        }
    }

    @Override
    public void bind(final GLState glState, final ShaderProgram shaderProgram)
    {
        this.bind(glState);

        shaderProgram.bind(glState, this.vertexBufferObjectAttributes);
    }


    @Override
    public void unbind(final GLState glState, final ShaderProgram pShaderProgram)
    {
        pShaderProgram.unbind(glState);

        //glState.bindBuffer(0);
        // TODO Does this have an positive/negative impact on performance?
    }

    @Override
    public void unloadFromHardware(final GLState pGLState)
    {
        pGLState.deleteArrayBuffer(this.hardwareBufferID);

        this.hardwareBufferID = HARDWARE_BUFFER_ID_INVALID;
    }

    @Override
    public void draw(final int primitiveType, final int count)
    {
        GLES20.glDrawArrays(primitiveType, 0, count);
        //Debug.d("VBO draw");
    }

    @Override
    public void draw(final int primitiveType, final int offset, final int count)
    {
        GLES20.glDrawArrays(primitiveType, offset, count);
        //Debug.d("VBO draw");
    }

    @Override
    public void dispose()
    {
        if(!this.disposed)
        {
            this.disposed = true;

            if(this.vertexBufferObjectManager != null)
            {
                this.vertexBufferObjectManager.onUnloadVertexBufferObject(this);
            }

            BufferUtils.freeDirectByteBuffer(this.byteBuffer);
        } 
        else
        {
            throw new AlreadyDisposedException();
        }
    }

    @Override
    protected void finalize() throws Throwable
    {
        super.finalize();

        if(!this.disposed)
        {
            this.dispose();
        }
    }

    // ===========================================================
    // Methods
    // ===========================================================

    private void loadToHardware(final GLState glState)
    {
        this.hardwareBufferID = glState.generateBuffer();
        this.dirtyOnHardware = true;
    }
}
