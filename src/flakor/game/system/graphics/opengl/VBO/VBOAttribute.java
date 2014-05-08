package flakor.game.system.graphics.opengl.VBO;

import android.opengl.GLES20;

/**
 * VBO属性
 * Created by Saint Hsu on 13-7-9.
 */
public class VBOAttribute
{
    // ===========================================================
    // Fields
    // ===========================================================

    /* package */ final int location;
    /* package */ final String name;
    /* package */ final int size;
    /* package */ final int type;
    /* package */ final boolean normalized;
    /* package */ final int offset;

    // ===========================================================
    // Constructors
    // ===========================================================

    public VBOAttribute(final int location, final String name, final int size, final int type, final boolean normalized, final int offset)
    {
        this.location = location;
        this.name = name;
        this.size = size;
        this.type = type;
        this.normalized = normalized;
        this.offset = offset;
    }

    // ===========================================================
    // Getter & Setter
    // ===========================================================

    public int getLocation()
    {
        return this.location;
    }

    public String getName()
    {
        return this.name;
    }

    public int getSize()
    {
        return this.size;
    }

    public int getType()
    {
        return this.type;
    }

    public boolean isNormalized()
    {
        return this.normalized;
    }

    public int getOffset()
    {
        return this.offset;
    }

    // ===========================================================
    // Methods for/from SuperClass/Interfaces
    // ===========================================================

    public void glVertexAttribPointer(final int stride)
    {
        GLES20.glVertexAttribPointer(this.location, this.size, this.type, this.normalized, stride, this.offset);
    }

}
