package flakor.game.system.graphics.opengl.VBO;

/**
 * Created by Saint Hsu on 13-7-9.
 */
public class VertexBufferObjectAttributes
{
    // ===========================================================
    // Fields
    // ===========================================================

    private final int stride;
    private final VBOAttribute[] vertexBufferObjectAttributes;

    // ===========================================================
    // Constructors
    // ===========================================================

    public VertexBufferObjectAttributes(final int stride, final VBOAttribute ... vertexBufferObjectAttributes)
    {
        this.vertexBufferObjectAttributes = vertexBufferObjectAttributes;
        this.stride = stride;
    }


    // ===========================================================
    // Methods
    // ===========================================================

    public void glVertexAttribPointers()
    {
        final VBOAttribute[] vertexBufferObjectAttributes = this.vertexBufferObjectAttributes;

        final int stride = this.stride;

        final int vertexBufferObjectAttributeCount = vertexBufferObjectAttributes.length;
        for(int i = 0; i < vertexBufferObjectAttributeCount; i++)
        {
            vertexBufferObjectAttributes[i].glVertexAttribPointer(stride);
        }
    }

}
