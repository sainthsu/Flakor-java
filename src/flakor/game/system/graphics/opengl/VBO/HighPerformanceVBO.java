package flakor.game.system.graphics.opengl.VBO;

import android.opengl.GLES20;

import flakor.game.support.util.BufferUtils;
import flakor.game.support.util.SystemUtils;
import flakor.game.system.graphics.opengl.DrawType;

import java.nio.FloatBuffer;

/**
 * Created by Saint Hsu on 13-7-10.
 */
public class HighPerformanceVBO extends VertexBufferObject
{
    // ===========================================================
    // Fields
    // ===========================================================

    protected final float[] bufferData;
    protected final FloatBuffer floatBuffer;

    // ===========================================================
    // Constructors
    // ===========================================================

    public HighPerformanceVBO(final VertexBufferObjectManager VBOManager, final int capacity, final DrawType drawType, final boolean autoDispose, final VertexBufferObjectAttributes VBOAttributes)
    {
        super(VBOManager, capacity, drawType, autoDispose, VBOAttributes);

        this.bufferData = new float[capacity];
        if(SystemUtils.SDK_VERSION_HONEYCOMB_OR_LATER)
        {
            this.floatBuffer = this.byteBuffer.asFloatBuffer();
        }
        else
        {
            this.floatBuffer = null;
        }
    }

    public HighPerformanceVBO(final VertexBufferObjectManager VBOManager, final float[] bufferData, final DrawType drawType, final boolean autoDispose, final VertexBufferObjectAttributes VBOAttributes)
    {
        super(VBOManager, bufferData.length, drawType, autoDispose,VBOAttributes);
        this.bufferData = bufferData;

        if(SystemUtils.SDK_VERSION_HONEYCOMB_OR_LATER)
        {
            this.floatBuffer = this.byteBuffer.asFloatBuffer();
        }
        else
        {
            this.floatBuffer = null;
        }
    }

    // ===========================================================
    // Getter & Setter
    // ===========================================================

    public float[] getBufferData()
    {
        return this.bufferData;
    }

    @Override
    public int getHeapMemoryByteSize()
    {
        return this.getByteCapacity();
    }

    @Override
    public int getNativeHeapMemoryByteSize()
    {
        return this.getByteCapacity();
    }

    // ===========================================================
    // Methods for/from SuperClass/Interfaces
    // ===========================================================

    @Override
    protected void onBufferData()
    {
        // TODO Check if, and how mow this condition affects performance.
        if(SystemUtils.SDK_VERSION_HONEYCOMB_OR_LATER)
        {
            // TODO Check if this is similar fast or faster than the non Honeycomb codepath.
            this.floatBuffer.position(0);
            this.floatBuffer.put(this.bufferData);

            GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, this.byteBuffer.capacity(), this.byteBuffer, this.usage);
        }
        else
        {
            BufferUtils.put(this.byteBuffer, this.bufferData, this.bufferData.length, 0);
            GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, this.byteBuffer.limit(), this.byteBuffer, this.usage);
        }
    }


}
