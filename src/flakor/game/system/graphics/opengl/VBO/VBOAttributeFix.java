package flakor.game.system.graphics.opengl.VBO;

import flakor.game.system.graphics.opengl.GLES20Fix;

/**
 * Created by Saint Hsu on 13-7-9.
 */
public class VBOAttributeFix extends VBOAttribute
{
    // ===========================================================
    // Constructors
    // ===========================================================

    public VBOAttributeFix(final int location, final String pName, final int pSize, final int pType, final boolean pNormalized, final int pOffset) 
    {
        super(location, pName, pSize, pType, pNormalized, pOffset);
    }


    // ===========================================================
    // Methods for/from SuperClass/Interfaces
    // ===========================================================

    @Override
    public void glVertexAttribPointer(final int stride)
    {
        GLES20Fix.glVertexAttribPointer(this.location, this.size, this.type, this.normalized, stride, this.offset);
    }

}
