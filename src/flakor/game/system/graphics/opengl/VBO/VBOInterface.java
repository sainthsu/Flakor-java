package flakor.game.system.graphics.opengl.VBO;

import flakor.game.system.graphics.DisposableInterface;
import flakor.game.system.graphics.opengl.GLState;
import flakor.game.system.graphics.opengl.Shader.ShaderProgram;

/**
 * Created by Saint Hsu on 13-7-9.
 *
 * VBO VertexBufferObject
 */
public interface VBOInterface extends DisposableInterface
{
    // ===========================================================
    // Constants
    // ===========================================================

    public static final int HARDWARE_BUFFER_ID_INVALID = -1;

    // ===========================================================
    // Methods
    // ===========================================================

    /**
     * 是否使用后自动销毁
     * @return true if auto
     */
    public boolean isAutoDispose();

    /**
     * 获取该VBO对应的显卡bufferID
     * @return int bufferID
     */
    public int getHardwareBufferID();

    /**
     * 是否已载入显卡
     * @return true if loaded
     */
    public boolean isLoadedToHardware();

    /**
     * 标记VBO对象没有载入显卡硬件。如果要再次使用此VBO，将重载
     * Mark this {@link VertexBufferObject} as not not loaded to hardware.
     * It will reload itself to hardware when it gets used again.
     */
    public void setNotLoadedToHardware();

    /**
     * 从显卡上卸载下来
     * @param glState Opengle 状态
     */
    public void unloadFromHardware(final GLState glState);

    /**
     * 是否VBO数据已过期
     * @return true if its dirty
     */
    public boolean isDirtyOnHardware();

    /**
     * 标记数据已过期
     * Mark this {@link VertexBufferObject} dirty so it gets updated on the hardware.
     */
    public void setDirtyOnHardware();

    /**
     * 浮点数的容量数
     * @return the number of <code>float</code>s that fit into this {@link VBOInterface}.
     */
    public int getCapacity();

    /**
     * Byte数的容量数
     * @return the number of <code>byte</code>s that fit into this {@link VBOInterface}.
     */
    public int getByteCapacity();

    /**
     * 在堆中分配的内存
     * @return the number of <code>byte</code>s that are allocated on the heap.
     */
    public int getHeapMemoryByteSize();

    /**
     * 在native堆中分配的内存
     * @return the number of <code>byte</code>s that are allocated on the native heap (through direct {@link java.nio.ByteBuffer}s).
     */
    public int getNativeHeapMemoryByteSize();

    /**
     * 获取显卡CPU中的Byte大小
     * @return the number of <code>byte</code>s that are allocated on the GPU.
     */
    public int getGPUMemoryByteSize();

    /**
     * 与显卡bufferID绑定
     * @param glState
     */
    public void bind(final GLState glState);
    public void bind(final GLState glState, final ShaderProgram shaderProgram);
    public void unbind(final GLState glState, final ShaderProgram shaderProgram);

    public VertexBufferObjectManager getVertexBufferObjectManager();

    /**
     * 画VBO存储的信息
     * @param primitiveType int
     * @param count int
     */
    public void draw(final int primitiveType, final int count);
    public void draw(final int primitiveType, final int offset, final int count);
}
