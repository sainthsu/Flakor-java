package flakor.game.system.graphics.opengl;

import android.opengl.GLES20;

/**
 * Created by Saint Hsu on 13-7-9.
 *
 */
public enum DrawType
{
    // ===========================================================
    // Elements
    // ===========================================================

    STATIC(GLES20.GL_STATIC_DRAW),
    DYNAMIC(GLES20.GL_DYNAMIC_DRAW),
    STREAM(GLES20.GL_STREAM_DRAW);

    // ===========================================================
    // Constants
    // ===========================================================

    private final int usage;

    // ===========================================================
    // Fields
    // ===========================================================


    private DrawType(final int usage)
    {
        this.usage = usage;
    }

    // ===========================================================
    // Getter & Setter
    // ===========================================================

    public int getUsage()
    {
        return this.usage;
    }
}
