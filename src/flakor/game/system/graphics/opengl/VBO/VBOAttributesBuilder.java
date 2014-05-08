package flakor.game.system.graphics.opengl.VBO;

import android.opengl.GLES20;
import android.os.Build;

import flakor.game.support.util.SystemUtils;
import flakor.game.system.graphics.opengl.DataConstants;
import flakor.game.tool.FlakorRuntimeException;

/**
 * 属性创建者
 * Created by Saint Hsu on 13-7-9.
 */
public class VBOAttributesBuilder
{
    private static final boolean WORAROUND_GLES2_GLVERTEXATTRIBPOINTER_MISSING;

    static
    {
        WORAROUND_GLES2_GLVERTEXATTRIBPOINTER_MISSING = SystemUtils.isAndroidVersionOrLower(Build.VERSION_CODES.FROYO);
    }

    // ===========================================================
    // Fields
    // ===========================================================

    private int index;
    private final VBOAttribute[] vertexBufferObjectAttributes;

    private int offset;

    // ===========================================================
    // Constructors
    // ===========================================================

    public VBOAttributesBuilder(final int count)
    {
        this.vertexBufferObjectAttributes = new VBOAttribute[count];
    }


    // ===========================================================
    // Methods
    // ===========================================================

    public VBOAttributesBuilder add(final int location, final String name, final int size, final int type, final boolean normalized)
    {
    	
        if(VBOAttributesBuilder.WORAROUND_GLES2_GLVERTEXATTRIBPOINTER_MISSING)
        {
            this.vertexBufferObjectAttributes[this.index] = new VBOAttributeFix(location,name, size, type, normalized, this.offset);
        }
        else
        {
            this.vertexBufferObjectAttributes[this.index] = new VBOAttribute(location, name, size, type, normalized, this.offset);
        }

        switch(type)
        {
            case GLES20.GL_FLOAT:
                this.offset += size * DataConstants.BYTES_PER_FLOAT;
                break;
            case GLES20.GL_UNSIGNED_BYTE:
                this.offset += size * DataConstants.BYTES_PER_BYTE;
                break;
            default:
                throw new IllegalArgumentException("Unexpected pType: '" + type + "'.");
        }

        this.index++;

        return this;
    }

    public VertexBufferObjectAttributes build()
    {
        if(this.index != this.vertexBufferObjectAttributes.length)
        {
            throw new FlakorRuntimeException("Not enough " + VBOAttribute.class.getSimpleName() + "s added to this " + this.getClass().getSimpleName() + ".");
        }

        return new VertexBufferObjectAttributes(this.offset, this.vertexBufferObjectAttributes);
    }

}
